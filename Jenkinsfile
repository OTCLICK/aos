pipeline {
    agent any

    tools {
        maven 'M3'
    }

    stages {
        stage('Checkout') {
            steps {
                checkout scm
                sh 'git submodule init || true'
                sh 'git submodule update --recursive --remote || true'
            }
        }

        stage('Build Contracts') {
            steps {
                sh 'cd antiplagiarism-api && mvn clean install -DskipTests'
                sh 'cd events-contract && mvn clean install -DskipTests'
            }
        }

        stage('Build Services') {
            steps {
                sh 'cd antiplagiarism && mvn clean package -DskipTests'
                sh 'cd audit-service && mvn clean package -DskipTests'
                sh 'cd notification-service && mvn clean package -DskipTests'
                sh 'cd statistics-service && mvn clean package -DskipTests'
                sh 'cd plagiarism-analysis-service && mvn clean package -DskipTests'
                sh 'cd work-grpc-server && mvn clean package -DskipTests'
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