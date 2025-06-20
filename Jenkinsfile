pipeline {
    agent any

    stages {
        stage('Build') {
            steps {
                sh './mvn clean package'
            }
        }

        stage('Test') {
            steps {
                sh './mvn test'
            }
        }

        stage('Deploy') {
            when {
                branch 'main'
            }
            steps {
                echo 'Deploying...'
            }
        }
    }

    post {
        success {
            echo '✅ Build and deploy succeeded.'
        }
        failure {
            echo '❌ Build failed.'
        }
    }
}
