node {
    stage('Git') {
        git 'https://github.com/jmarin/akka-cluster-example.git'
    }

    stage('Scala Build') {
        // assumes you have the sbt plugin installed and created an sbt installation named 'sbt-0.13.13'
        sh "${tool name: 'sbt', type: 'org.jvnet.hudson.plugins.SbtPluginBuilder$SbtInstallation'}/bin/sbt clean test assembly"
    }

    docker.withRegistry('https://dtr.cfpb.gov', 'dtr') {
      stage('Docker Build') {
          //sh "cp seed/target/scala-2.12/seed.jar seed/."
          //sh "cp frontend/target/scala-2.12/frontend.jar frontend/."
          //sh "cp backend/target/scala-2.12/backend.jar backend/."


          sh "git rev-parse HEAD > .git/commit-id"
          def commit_id = readFile('.git/commit-id').trim()
          println(commit_id)

          def seedImage = docker.build("dtr.cfpb.gov/akka-cluster-example-seed:${commit_id}", "--build-arg PACKAGE_VERSION=${commit_id} ./seed")
          seedImage.push()
      }
    }
}