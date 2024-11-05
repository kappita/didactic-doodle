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
                    sh 'sudo docker build -t kappappita/prestabanco-backend:latest .'
                }
                
                withCredentials([string(credentialsId: 'dhpswid', variable: 'dhpsw')]) {
                    script {
                        sh 'sudo docker login -u kappappita -p $dhpsw'
                    }
                }

                script {
                    sh 'sudo docker push kappappita/prestabanco-backend:latest'
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
