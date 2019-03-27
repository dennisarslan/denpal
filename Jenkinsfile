pipeline {
    agent any
    parameters {
        booleanParam(name: 'cli', defaultValue: true, description: 'Should I rebuild the cli Docker image?')
        booleanParam(name: 'nginx', defaultValue: true, description: 'Should I rebuild the nginx Docker image?')
        booleanParam(name: 'php', defaultValue: true, description: 'Should I rebuild the php Docker image?')
    }
    stages {
        stage('Build Image: cli') {
            when {
                cli: true
            }
            steps {
                sh """
				docker build -t dennisarslan/denpal-cli -f Dockerfile.cli .
                docker push dennisarslan/denpal-cli
                """
            }
        }
        stage('Build Image: nginx') {
            when {
                nginx: true
            }
            steps {
                sh """
				docker build -t dennisarslan/denpal-nginx -f Dockerfile.nginx .
                docker push dennisarslan/denpal-nginx
                """
            }
        }
        stage('Build Image: php') {
            when {
                php: true
            }
            steps {
                sh """
				docker build -t dennisarslan/denpal-php -f Dockerfile.php .
                docker push dennisarslan/denpal-php
                """
            }
        }
    }
}
