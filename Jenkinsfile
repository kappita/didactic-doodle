pipeline {
    agent any

    tools {
        gradle 8.10-2
    }

    stages {
        stage('Build backend') {
            steps {
                git url: 'https://github.com/kappita/prestabanco-backend'
                sh './gradlew build'
                sh './gradlew test'
            }
        }

        stage('Build backend') {
        }

    }



    environment {
        // Specify any environment variables if needed
        JAVA_HOME = '/usr/lib/jvm/java-17-openjdk' // Adjust Java version if needed
        GRADLE_HOME = '/usr/local/gradle' // Path to Gradle, if not set globally
        PATH = "${GRADLE_HOME}/bin:${env.PATH}"
    }

    stages {
        stage('Checkout') {
            steps {
                // Checkout the code from the repository
                git branch: 'main', url: 'https://github.com/your-repo/your-spring-boot-app.git'
            }
        }

        stage('Build') {
            steps {
                // Build the application using Gradle
                sh './gradlew clean build'
            }
        }

        stage('Test') {
            steps {
                // Run tests with Gradle
                sh './gradlew test'
            }

            post {
                // Archive test results
                always {
                    junit '**/build/test-results/test/*.xml'
                }
            }
        }

        stage('Package') {
            steps {
                // Package the application into a JAR file
                sh './gradlew bootJar'
            }

            post {
                // Archive the JAR file as a build artifact
                always {
                    archiveArtifacts artifacts: 'build/libs/*.jar', allowEmptyArchive: true
                }
            }
        }

        stage('Deploy') {
            when {
                branch 'main'
            }
            steps {
                // Deploy the application, e.g., copy to a server or use Docker
                // Customize this step based on your deployment needs

                // Example: SSH to deploy the JAR file to a remote server
                sshagent(['your-ssh-credentials-id']) {
                    sh """
                    scp build/libs/your-spring-boot-app.jar user@your-server:/path/to/deploy/
                    ssh user@your-server 'nohup java -jar /path/to/deploy/your-spring-boot-app.jar &'
                    """
                }
            }
        }
    }

    post {
        // Notify on build success or failure
        success {
            echo 'Build and deployment successful!'
        }
        failure {
            echo 'Build or deployment failed.'
        }
    }
}
