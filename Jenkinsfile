pipeline {
    agent any

    environment {
        DOCKER_COMPOSE_FILE = 'docker-compose.yml'
    }

    stages {
        stage('Checkout') {
            steps {
                echo '🔄 Clonage du dépôt...'
                checkout scm
            }
        }

        stage('Build Frontend Image') {
            steps {
                echo '🌐 Construction de l’image Docker du frontend (Vite)...'
                dir('frontend') {
                    sh 'docker build -t azbane-frontend .'
                }
            }
        }

        stage('Build Backend Image') {
            steps {
                echo '⚙️ Construction de l’image Docker du backend (Spring Boot)...'
                dir('backend') {
                    sh 'docker build -t azbane-backend .'
                }
            }
        }

        stage('Run with Docker Compose') {
            steps {
                echo '🚀 Lancement des conteneurs avec docker-compose...'
                sh 'docker-compose down || true' // au cas où un vieux conteneur tourne
                sh 'docker-compose up -d --build'
            }
        }
    }

    post {
        always {
            echo '✅ Pipeline terminée.'
            sh 'docker ps'
        }
        failure {
            echo '❌ La pipeline a échoué.'
        }
    }
}
