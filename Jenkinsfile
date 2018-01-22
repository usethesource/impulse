node {
  env.JAVA_HOME="${tool 'jdk-oracle-8'}"
  env.PATH="${env.JAVA_HOME}/bin:${mvnHome}/bin:${env.PATH}"
  try {
    stage('Clone') {
      checkout scm
    }
    
    withMaven(maven: 'M3', jdk: 'jdk-oracle-8', options: [artifactsPublisher(disabled: true)] ) {
        stage('Build') {
          sh "mvn -DskipTest clean compile"
        }
        
        stage('Packaging') {
          sh "mvn -DskipTest package"
        }
        
        stage ('Deploy') {
          sh "mvn -DskipTests deploy"
        }
    }
        
    if (currentBuild.previousBuild.result == "FAILURE") { 
       slackSend (color: '#5cb85c', channel: "#usethesource",  message: "BUILD BACK TO NORMAL: <${env.BUILD_URL}|${env.JOB_NAME} [${env.BUILD_NUMBER}]>")
    }

    build job: '../rascal-eclipse/master', wait: false
  } catch(e) {
      slackSend (color: '#d9534f', channel: "#usethesource",  message: "FAILED: <${env.BUILD_URL}|${env.JOB_NAME} [${env.BUILD_NUMBER}]>")
      throw e
  }
}
