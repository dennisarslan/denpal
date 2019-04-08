 pipeline {
  agent any
  options {
    buildDiscarder(logRotator(numToKeepStr: '10'))
  }
  environment {
    DOCKER_CREDS = credentials('amazeeiojenkins-dockerhub-password')
    COMPOSE_PROJECT_NAME = "denpal-${BUILD_ID}"
  }
  stages {
    stage('Docker login') {
      steps {
        sh '''
        env
        docker login --username amazeeiojenkins --password $DOCKER_CREDS
        '''
      }
    }
    stage('Docker Build') {
      steps {
        sh '''
        docker-compose config -q
        docker-compose down
        docker-compose up -d --build "$@"
        '''
      }
    }
    stage('Waiting 10 seconds') {
      steps {
        sh """
        sleep 10s
        """
      }
    }
    stage('Verification') {
      steps {
        sh '''
        export COMPOSE_PROJECT_NAME="denpal-${BUILD_ID}"
        docker-compose exec -T cli drush status
        docker-compose exec -T cli curl http://nginx:8080 -v
        curl -v http://localhost:10000/
        curl -v http://localhost:10001/
        if [ $? -eq 0 ]; then
          echo "OK!"
        else
          echo "FAIL"
          /bin/false
        fi
        '''
      }
    }
    stage('Docker Push') {
      steps {
        sh '''
        tag=$(git describe --abbrev=0 --tags)
        branch=$(git describe --all --contains --abbrev=4)
        echo "Branch: "
        echo $branch

        docker tag denpal:latest amazeeiodevelopment/denpal:latest
        docker tag denpal:latest amazeeiodevelopment/denpal:$tag
        docker push amazeeiodevelopment/denpal:latest
        docker push amazeeiodevelopment/denpal:$tag

        docker tag denpal_nginx:latest amazeeiodevelopment/denpal_nginx:latest
        docker tag denpal_nginx:latest amazeeiodevelopment/denpal_nginx:$tag
        docker push amazeeiodevelopment/denpal_nginx:latest
        docker push amazeeiodevelopment/denpal_nginx:$tag

        docker tag denpal_php:latest amazeeiodevelopment/denpal_php:latest
        docker tag denpal_php:latest amazeeiodevelopment/denpal_php:$tag
        docker push amazeeiodevelopment/denpal_php:latest
        docker push amazeeiodevelopment/denpal_php:$tag
        '''
      }
    }
  }
  post {
    always {
      script {
        currentBuild.setDescription("CLI: ${params.cli} - NGINX: ${params.nginx} - PHP: ${params.php}")
      }
      echo 'I will always say Hello again!'
    }
  }
}
