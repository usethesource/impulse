node {
  def mvnHome = tool 'M3'
  env.JAVA_HOME="${tool 'jdk-oracle-8'}"
  env.PATH="${env.JAVA_HOME}/bin:${mvnHome}/bin:${env.PATH}"

  try {
    stage 'Clone'
    checkout scm
    
    stage 'Build'
    sh "mvn -DskipTest -B clean compile"
    
    stage 'Test'
    sh "mvn -B test"
    
    stage 'Packaging'
    sh "mvn -DskipTest -B package"
    
    stage 'Deploy'
    sh "mvn -s ${env.HOME}/usethesource-maven-settings.xml -DskipTests -B deploy"
        
    if (currentBuild.previousBuild.result == "FAILURE") { 
		slackSend (color: '#00FF00', message: "BUILD BACK TO NORMAL: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
    }
    
    build job: '../rascal-eclipse/master', wait: false
  } catch(e) {
	  slackSend (color: '#FF0000', message: "FAILED: Job '${env.JOB_NAME} [${env.BUILD_NUMBER}]' (${env.BUILD_URL})")
      throw e
  }
}
