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
            }
        }

                stage('Build Docker Images') {
                    steps {
                        echo 'Сборка Docker образов...'
                        sh 'docker compose build'
                    }
                }

                stage('Deploy to Docker') {
                    steps {
                        echo 'Развертывание в Docker...'
                        sh 'docker compose down --remove-orphans || true'
                        sh 'docker compose up -d'
                    }
                }

                stage('Health Check') {
                    steps {
                        echo 'Проверка состояния сервисов...'
                        sleep(time: 30, unit: 'SECONDS')
                        sh 'curl -f http://localhost:8080/actuator/health || exit 1'
                    }
                }

//         stage('Docker Compose Build') {
//             steps {
//                 script {
//                             // Убедитесь, что Docker и docker-compose доступны
//                     sh 'docker --version'
//                     sh 'docker-compose --version || docker compose version'
//
//                             // Удалить старые контейнеры и образы (опционально)
//                     sh 'docker-compose down --remove-orphans || true'
//
//                             // Пересобрать образы
//                     sh 'docker-compose build --no-cache'
//
//                             // Запустить контейнеры (если нужно)
//                     sh 'docker-compose up -d'
//                 }
//             }
//         }



//         stage('Deploy') {
//             steps {
//                 sh 'pwd'
//                 sh 'ls -la'
//                 sh 'ls -la prometheus/'
//
//                 sh 'test -f prometheus/prometheus.yml && echo "✅ prometheus.yml FOUND" || echo "❌ prometheus.yml MISSING"'
//                 sh 'docker-compose down'
//                 sh 'docker-compose up --build -d'
//             }
//         }
    }
}