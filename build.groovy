pipeline {
    agent any

    tools {
        sbt 'sbt-0.13.13'
        jdk8 'jdk8'
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