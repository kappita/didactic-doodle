pipeline {
    agent any    

    stages {

        stage('Checkout repository') {
            steps {
                checkout scmGit(branches: [[name: '*/main']], extensions: [], userRemoteConfigs: [[url: 'https://github.com/kappita/prestabanco-backend']])
            }
        }


        stage('Build backend') {
            steps {
                script {
                    sh 'docker build -t kappappita/backend-prestabanco:latest .'
                }
                
                withCredentials([string(credentialsId: 'dhpswid', variable: 'dhpsw')]) {
                    script {
                        sh 'docker login -u kappappita -p $dhpsw'
                    }
                }

                script {
                    sh 'docker push kappappita/backend-prestabanco:latest'
                }
            }
        }

        stage('Test backend') {
            steps {
                script {
                    if (isUnix()) {
                        sh './gradlew test'
                    } else {
                        bat './gradlew test'
                    }
                }
            }
        }
    }
}
