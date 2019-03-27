pipeline {
    agent any
    parameters {
        booleanParam(name: 'cli', defaultValue: true, description: 'Should I rebuild the cli Docker image?')
        booleanParam(name: 'nginx', defaultValue: true, description: 'Should I rebuild the nginx Docker image?')
        booleanParam(name: 'php', defaultValue: true, description: 'Should I rebuild the php Docker image?')
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
    	  		    git url: 'https://github.com/dennisarslan/denpal'
    	  	}
    	  }
        stage('Docker login') {
            steps {
                sh """
                docker login --username $DOCKER_CREDS_USR --password $DOCKER_CREDS_PSW
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
            steps {
                sh """
				docker build -t dennisarslan/denpal-nginx -f Dockerfile.nginx .
                docker push dennisarslan/denpal-nginx
                """
            }
        }
        stage('Build Image: php') {
            steps {
                sh """
				docker build -t dennisarslan/denpal-php -f Dockerfile.php .
                docker push dennisarslan/denpal-php
                """
            }
        }
    }
}
