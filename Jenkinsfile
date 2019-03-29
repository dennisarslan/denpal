pipeline {
    agent any
    options {
        buildDiscarder(logRotator(numToKeepStr: '10', artifactNumToKeepStr: '10'))
    }
    parameters {
        booleanParam(name: 'cli', defaultValue: false, description: 'Should I rebuild the cli Docker image?')
        booleanParam(name: 'nginx', defaultValue: false, description: 'Should I rebuild the nginx Docker image?')
        booleanParam(name: 'php', defaultValue: false, description: 'Should I rebuild the php Docker image?')
    }
		environment {
   		DOCKER_CREDS = credentials('dockerlogin')
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
                docker login --username $DOCKER_CREDS_USR --password $DOCKER_CREDS_PSW
                """
            }
        }
        stage('Docker-compose') {
            steps {
                sh """
                ls -al
                ./ahoy.sh
                """
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
        stage('Verification tests')
            steps {
                sh """
                docker-compose exec -T cli drush status
                curl http://denpal.docker.amazee.io
                if [ $? -eq 0 ]; then
                    echo "OK!"
                else
                    echo "FAIL"
                fi
                """
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
