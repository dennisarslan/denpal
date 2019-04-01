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
                echo docker
                """
/*
                docker login --username amazeeiojenkins --password $DOCKER_CREDS_PSW
*/
            }
        }
        stage('Install dependencies') {
            steps {
                sh """
                id
                env
                sudo ls
                """
/*
    	          apt-get update
								apt-get install -y openssh-server net-tools inetutils-ping python-pip rubygems
								apt-get install -y \
								    apt-transport-https \
								    ca-certificates \
								    curl \
								    gnupg2 \
								    software-properties-common

								curl -fsSL https://download.docker.com/linux/debian/gpg | apt-key add -

								add-apt-repository \
								   "deb [arch=amd64] https://download.docker.com/linux/debian \
								   $(lsb_release -cs) \
								   stable"

								apt-get update
								apt-get install -y docker-ce docker-ce-cli containerd.io
								curl -L "https://github.com/docker/compose/releases/download/1.23.2/docker-compose-$(uname -s)-$(uname -m)" -o /usr/local/bin/docker-compose
								chmod +x /usr/local/bin/docker-compose
								gem install serverspec pygmy
                """
            */
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
        stage('Verification tests') {
            steps {
                sh """
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
