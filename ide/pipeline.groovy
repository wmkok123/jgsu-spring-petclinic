pipeline {
    agent any

    stages {
        //stage('Checkout') {
        //    steps {
        //        git url: 'https://github.com/wmkok123/jgsu-spring-petclinic.git', branch: 'main'
        //    }
        //}
        
        stage('Build') {
            steps {
                sh './mvnw clean package'
            }

            post {
                always {
                    junit '**/target/surefire-reports/TEST-*.xml'
                    archiveArtifacts 'target/*.jar'
                }
                
                changed {
                    emailext subject: 'Job \'${JOB_NAME}\' (${BUILD_NUMBER}) is waiting for input', 
                         attachLog: true, 
                         body: 'Please go to ${BUILD_URL} and verify the build', 
                         compressLog: true,
                         recipientProviders: [upstreamDevelopers(), requestor()], 
                         to: "test@jenkins"
                }
            }
        }
    }
}
