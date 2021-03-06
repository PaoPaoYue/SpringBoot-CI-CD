# SpringBoot-CI-CD
- A demo of SpringBoot REST API service
- Using github action to auto deploy service as a docker

### What this Github Action do

1. Build a your project via Gradle
2. Build the docker using your predefined dockerfile
3. Push the docker to your remote docker repo
4. Using ssh to connect to your server
5. Pull down your docker from the remote docker repo
6. Run the Springboot application

### Instructions
1. Define some secrect In your Github settings
- DOCKER_PASSWORD - password of your remote docker repo
- PRIVATE_SSH_HOST - your server host
- PRIVATE_SSH_USERNAME - your server username
- PRIVATE_SSH_PASSWORD - your server password
2. Go to Github action, select `Springboot-CI-CD` and enter parameters
3. Execute the action

### Source Code
 The Github action is defined in ci-cd.yml
 
      name: springboot-ci-cd

      on:
        workflow_dispatch:
          inputs:
            environment:
              description: 'environment profile name'
              required: true
              default: 'prod'
            version:
              description: 'docker version (default 0.0.1)'
              required: true
              default: '0.0.1'
            port:
              description: 'application port'
              required: true
              default: '5110'
            docker_repo:
              description: 'docker repositry'
              required: true
              default: 'py846260131/springboot-ci-cd'
            docker_user:
              description: 'docker username'
              required: true
              default: 'paopaoyue.cn@gmail.com'
            docker_registry:
              description: 'docker registry'
              required: true
              default: 'registry.cn-shanghai.aliyuncs.com'
            docker_container:
              description: 'name of the docker container'
              required: true
              default: 'springboot-ci-cd'

      jobs:

        ci-cd:

          runs-on: ubuntu-latest

          steps:
          - name: Set environment variables
            run: |
              echo "::set-env name=ENVIRONMENT::${{github.event.inputs.environment}}"
              echo "::set-env name=VERSION::${{github.event.inputs.version}}"
              echo "::set-env name=APP_PORT::${{github.event.inputs.port}}"
              echo "::set-env name=DOCKER_REPO::${{github.event.inputs.docker_repo}}"
              echo "::set-env name=DOCKER_USER::${{github.event.inputs.docker_user}}"
              echo "::set-env name=DOCKER_REGISTRY::${{github.event.inputs.docker_registry}}"
              echo "::set-env name=DOCKER_CONTAINER::${{github.event.inputs.docker_container}}"
          - uses: actions/checkout@v2
          - uses: actions/cache@v2
            with:
              path: |
                ~/.gradle/caches
                ~/.gradle/wrapper
              key: ${{ runner.os }}-gradle-${{ hashFiles('**/*.gradle*') }}
              restore-keys: |
                ${{ runner.os }}-gradle-
          - name: Set up JDK 1.8
            uses: actions/setup-java@v1
            with:
              java-version: 1.8
          - name: Grant execute permission for gradlew
            run: chmod +x gradlew
          - name: Build with Gradle
            run: ./gradlew build
          - name: Build and push Docker image
            uses: docker/build-push-action@v1.1.0
            with:
              username: ${{ env.DOCKER_USER }}
              password: ${{ secrets.DOCKER_PASSWORD }}
              repository: ${{ env.DOCKER_REPO }}
              registry: ${{ env.DOCKER_REGISTRY }}
              tags: ${{env.ENVIRONMENT}}-${{env.VERSION}}
          - name: deploy to remote server
            uses: appleboy/ssh-action@master
            with:
              host: ${{ secrets.PRIVATE_SSH_HOST }}
              username: ${{ secrets.PRIVATE_SSH_USERNAME }}
              password: ${{ secrets.PRIVATE_SSH_PASSWORD }}
              script: | 
                sudo docker stop ${{env.DOCKER_CONTAINER}}-${{env.ENVIRONMENT}}
                sudo docker rm ${{env.DOCKER_CONTAINER}}-${{env.ENVIRONMENT}}
                sudo docker rmi ${{env.DOCKER_REGISTRY}}/${{env.DOCKER_REPO}}:${{env.ENVIRONMENT}}-${{env.VERSION}}
                sudo docker pull ${{env.DOCKER_REGISTRY}}/${{env.DOCKER_REPO}}:${{env.ENVIRONMENT}}-${{env.VERSION}}
                sudo docker run -d -p ${{env.APP_PORT}}:${{env.APP_PORT}} -p 10200:10200 -e JAVA_OPTS=-Dspring.profiles.active=${{env.ENVIRONMENT}} --name=${{env.DOCKER_CONTAINER}}-${{env.ENVIRONMENT}} ${{env.DOCKER_REGISTRY}}/${{env.DOCKER_REPO}}:${{env.ENVIRONMENT}}-${{env.VERSION}}
