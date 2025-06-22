pipeline {
    agent { label 'agent-jdk21' }

    tools {
        git 'Default'
    }

    stages {
        stage('Prepare Environment') {
            steps {
                sh 'chmod +x ./gradlew'
            }
        }
        stage('Check') {
            steps {
                sh './gradlew check -P"dotenv.filename"="/var/agent-jdk21/env/.env.develop"'
            }
        }
        stage('Package') {
            steps {
                sh './gradlew build -P"dotenv.filename"="/var/agent-jdk21/env/.env.develop"'
            }
        }
        stage('JaCoCo Report') {
            steps {
                sh './gradlew jacocoTestReport -P"dotenv.filename"="/var/agent-jdk21/env/.env.develop"'
            }
        }
        stage('JaCoCo Verification') {
            steps {
                sh './gradlew jacocoTestCoverageVerification -P"dotenv.filename"="/var/agent-jdk21/env/.env.develop"'
            }
        }

        stage('Check Git Tag') {
            steps {
                script {
                    env.GIT_TAG = sh(script: 'git describe --tags --exact-match || echo ""', returnStdout: true).trim()
                    if (env.GIT_TAG) {
                        echo "Git tag detected: ${env.GIT_TAG}"
                    } else {
                        echo "No Git tag found. Proceeding without tagging/publishing Docker image."
                    }
                }
            }
        }

        stage('Docker Build') {
            steps {
                sh 'docker build -t job4j_devops:latest .'
            }
        }

        stage('Publish Maven Artifact') {
            when {
                expression { return env.GIT_TAG }
            }
            steps {
                withCredentials([usernamePassword(
                        credentialsId: 'nexus-credentials-id',
                        usernameVariable: 'NEXUS_USER',
                        passwordVariable: 'NEXUS_PASS'
                )]) {
                    sh '''
                        ./gradlew publish                       \
                                 -PnexusUsername="$NEXUS_USER"  \
                                 -PnexusPassword="$NEXUS_PASS"  \
                                 -P"dotenv.filename"="/var/agent-jdk21/env/.env.develop"
                    '''
                }
            }
        }

        stage('Tag and Push to Nexus') {
            when { expression { env.GIT_TAG } }
            steps {
                sh """
                    docker tag job4j_devops:latest 192.168.0.56:8082/repository/my-docker-repo/job4j_devops:${GIT_TAG}
                    docker push 192.168.0.56:8082/repository/my-docker-repo/job4j_devops:${GIT_TAG}
                """
            }
        }

        stage('Update DB') {
            steps {
                script {
                    sh './gradlew update -P"dotenv.filename"="/var/agent-jdk21/env/.env.develop"'
                }
            }
        }
    }

    post {
        always {
            script {
                def buildInfo = """
                    Build number: ${currentBuild.number}
                    Build status: ${currentBuild.currentResult}
                    Started at: ${new Date(currentBuild.startTimeInMillis)}
                    Duration: ${currentBuild.durationString}
                """
                telegramSend(message: buildInfo)
            }
        }
    }
}
