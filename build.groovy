pipeline {
    agent any

    tools {
        org.jvnet.hudson.plugins.SbtPluginBuilder$SbtInstallation 'sbt-0.13.13'
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