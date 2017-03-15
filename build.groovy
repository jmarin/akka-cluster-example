pipeline {
    agent any

    tools {
        jdk 'jdk8'
    }

    stages {
        stage('Initialize') {
            sh '''
              echo "PATH = $PATH"
            '''
        }

        stage('Build') {
            steps {
                echo 'This is a minimal pipeline'
            }
        }
    }
}