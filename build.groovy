node('platform-ops') {
    stage('Git') {
        git 'https://github.com/jmarin/akka-cluster-example.git'
    }

    stage('Scala Build') {
        // assumes you have the sbt plugin installed and created an sbt installation named 'sbt-0.13.1'
        sh "${tool name: 'sbt 0.13.1', type: 'org.jvnet.hudson.plugins.SbtPluginBuilder$SbtInstallation'}/bin/sbt clean test assembly"
    }

    withCredentials([[$class: 'UsernamePasswordMultiBinding', credentialsId: 'dtr',
                      usernameVariable: 'PLATFORM_USERNAME', passwordVariable: 'PLATFORM_PASSWORD']]) {
        //available as an env variable, but will be masked if you try to print it out any which way
        echo "${env.USERNAME}"

        docker.withRegistry('https://dtr.cfpb.gov', 'dtr') {
            stage('Docker Build') {
                sh "git rev-parse HEAD > .git/commit-id"
                def commit_id = readFile('.git/commit-id').trim()
                println(commit_id)

                def seedImage = docker.build("dtr.cfpb.gov/akka-cluster-example-seed:${env.BUILD_TAG}", "--build-arg ./seed")
                seedImage.push('latest')
            }
        }
    }
}