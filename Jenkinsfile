pipeline {
    agent any

    environment {
        // Optional environment variables
        JAVA_HOME = '/usr/lib/jvm/java-17-openjdk'
    }

    stages {
        stage('Clone') {
            steps {
                git credentialsId: 'github-credentials', url: 'https://github.com/cheya01/Student-Mentor-Platform.git'
            }
        }

        stage('Build') {
            steps {
                sh './mvnw clean package'
            }
        }

        stage('Test') {
            steps {
                sh './mvnw test'
            }
        }

        stage('Deploy') {
            when {
                branch 'main'
            }
            steps {
                echo 'Deploy step here (e.g., docker push, render deploy, etc.)'
            }
        }
    }

    post {
        always {
            echo 'Pipeline finished.'
        }
        success {
            echo 'Build succeeded!'
        }
        failure {
            echo 'Build failed!'
        }
    }
}
