pipeline {
    agent any

    tools {
        jdk 'jdk8'
        sbt 'sbt'
    }

    stages {
        stage('Initialize') {
            steps {
                sh '''
                  echo "PATH = $PATH"
                '''
            }
        }

        stage('Build') {
            steps {
                echo 'This is a minimal pipeline'
            }
        }
    }
}