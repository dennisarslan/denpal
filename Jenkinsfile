pipeline {
    agent any
    parameters {
        booleanParam(name: 'cli', defaultValue: true, description: 'Should I rebuild the cli Docker image?')
        booleanParam(name: 'nginx', defaultValue: true, description: 'Should I rebuild the nginx Docker image?')
        booleanParam(name: 'php', defaultValue: true, description: 'Should I rebuild the php Docker image?')
    }
		environment {
   		docker_credentials = credentials('docker-login')
		}
    stages {
        stage('Docker login') {
            steps {
                sh """
                env
                docker login --username $username --password $password
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
