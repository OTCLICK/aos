pipeline {
    agent any

    tools {
        maven 'M3'  
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
            }
        }

        stage('Build') {
            steps {
                sh 'cd antiplagiarism && mvn clean package -DskipTests'
                sh 'cd audit-service && mvn clean package -DskipTests'
                sh 'cd notification-service && mvn clean package -DskipTests'
                sh 'cd statistics-service && mvn clean package -DskipTests'
                sh 'cd plagiarism-analysis-service && mvn clean package -DskipTests'
            }
        }

        stage('Deploy') {
            steps {
                sh 'docker-compose down'

                sh 'docker-compose up --build -d'
            }
        }
    }
}
