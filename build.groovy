pipeline {
    agent any

    tools {
        jdk 'jdk8'
        org.jvnet.hudson.plugins.SbtPluginBuilder$SbtInstallation 'sbt'
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