    stage('Git clone') {
      /*
      This is needed for local development, because Jenkins uses locally pasted pipeline code in a textarea box and doesn't know where the Git repo is.
      This also means we have no multibranch, but that's no problem for local development.
      */
      steps {
        git branch: 'feature/Jenkinsfile',
          credentialsId: 'denpal',
          url: 'git@github.com:dennisarslan/denpal.git'
        /*
        git url: 'github.com/dennisarslan/denpal', branch: 'feature/Jenkinsfile'
        */
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
    stage('Tagging') {
      steps {
        withCredentials([sshUserPrivateKey(credentialsId: 'denpal', keyFileVariable: 'KEY_FILE')]) {
          sh '''
          eval `ssh-agent -s`
          ssh-add ${KEY_FILE}
          ssh-add -L
          git config --global user.name "Dennis Arslan (Jenkins)"
          git config --global user.email "dennis.arslan@amazee.com"
          ./archive/tag_git_repo.sh
          '''
        }
      }
    }