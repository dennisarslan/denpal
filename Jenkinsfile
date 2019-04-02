pipeline {
  agent any
  options {
    buildDiscarder(logRotator(numToKeepStr: '10', artifactNumToKeepStr: '10'))
  }
  parameters {
    booleanParam(name: 'dependencies', defaultValue: false, description: 'Should I install the Docker-in-Docker dependencies?')
    booleanParam(name: 'cli', defaultValue: false, description: 'Should I rebuild the cli Docker image?')
    booleanParam(name: 'nginx', defaultValue: false, description: 'Should I rebuild the nginx Docker image?')
    booleanParam(name: 'php', defaultValue: false, description: 'Should I rebuild the php Docker image?')
  }
  environment {
    DOCKER_CREDS = credentials('amazeeiojenkins-dockerhub-password')
  }
  stages {
    stage('Git clone') {
      /*
      This is needed for local development, because Jenkins uses locally pasted pipeline code in a textarea box and doesn't know where the Git repo is.
      This also means we have no multibranch, but that's no problem for local development.
      */
      steps {
        git url: 'https://github.com/dennisarslan/denpal', branch: 'feature/Jenkinsfile'
      }
    }
    stage('Docker login') {
      steps {
        sh """
        docker login --username amazeeiojenkins --password $DOCKER_CREDS
        """
      }
    }
    stage('Install dependencies') {
      when { expression { return params.dependencies } }
      steps {
        sh """
        echo disabled
        """
        }
    }
    stage('Pre-build tests') {
      steps {
        sh """
        ls -al
        """
      }
    }
    stage('Docker-compose') {
      steps {
        sh '''
        docker-compose config -q
        docker network prune -f && docker network inspect amazeeio-network >/dev/null || docker network create amazeeio-network
        COMPOSE_PROJECT_NAME=denpal docker-compose down
        COMPOSE_PROJECT_NAME=denpal docker-compose up -d --build "$@"
        docker-compose ps
        docker network list
        '''
      }
    }
    stage('Build Image: cli') {
      when { expression { return params.cli } }
      steps {
        sh """
        docker build -t dennisarslan/denpal-cli -f Dockerfile.cli .
        docker push dennisarslan/denpal-cli
        """
      }
    }
    stage('Build Image: nginx') {
      when { expression { return params.nginx } }
      steps {
        sh """
        docker build -t dennisarslan/denpal-nginx -f Dockerfile.nginx --build-arg CLI_IMAGE=dennisarslan/denpal-cli .
        docker push dennisarslan/denpal-nginx
        """
      }
    }
    stage('Build Image: php') {
      when { expression { return params.php } }
      steps {
        sh """
        docker build -t dennisarslan/denpal-php -f Dockerfile.php --build-arg CLI_IMAGE=dennisarslan/denpal-cli .
        docker push dennisarslan/denpal-php
        """
      }
    }
    stage('Docker push images') {
      steps {
        sh """
        docker tag denpal:latest amazeeiodevelopment/denpal:latest
        docker push amazeeiodevelopment/denpal:latest
        docker tag denpal_nginx:latest amazeeiodevelopment/denpal_nginx:latest
        docker push amazeeiodevelopment/denpal_nginx:latest
        docker tag denpal_php:latest amazeeiodevelopment/denpal_php:latest
        docker push amazeeiodevelopment/denpal_php:latest
        """
      }
    }
    stage('Verification tests') {
      steps {
        sh """
        docker ps | head
        docker images | head
        docker-compose ps
        curl -v http://localhost:10000/
        curl -v http://localhost:10001/
        docker-compose exec -T cli drush status
        echo curl http://denpal.docker.amazee.io
        """
        /*
        if [ $? -eq 0 ]; then
          echo "OK!"
        else
          echo "FAIL"
        fi
        */
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
