pipeline {
    agent any
    parameters {
        string(name: 'Package', defaultValue: 'apache2', description: 'What package should I install?')
    }
    stages {
        stage('Install a package') {
            steps {
                sh "ssh root@centos apt-get install -y ${params.Package}"
            }
        }
    }
}