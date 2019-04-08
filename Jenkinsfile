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
    stage('Waiting') {
      steps {
        sh """
        sleep 5s
        """
      }
    }
    stage('Verification') {
      steps {
        sh '''
        docker-compose exec -T cli drush status
        docker-compose exec -T cli curl http://nginx:8080 -v
        if [ $? -eq 0 ]; then
          echo "OK!"
        else
          echo "FAIL"
          /bin/false
        fi
        docker-compose down
        '''
      }
    }
    stage('Docker Push') {
      steps {
        sh '''
        #!/bin/bash
        echo bash
        echo "Branch: $GIT_BRANCH"
        docker images | head

        for variant in '' _nginx _php; do
            echo docker tag denpal$variant amazeeiodevelopment/denpal$variant:$GIT_BRANCH
            echo docker push amazeeiodevelopment/denpal$variant:$GIT_BRANCH

            if [ $GIT_BRANCH == "develop" ]; then
              echo docker tag denpal$variant amazeeiodevelopment/denpal$variant:latest
              echo docker push amazeeiodevelopment/denpal$variant:latest
            fi

        done

        /bin/false
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
}
