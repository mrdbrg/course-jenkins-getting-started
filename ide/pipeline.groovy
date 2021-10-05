pipeline {
    agent any
    // Triggers every minute
    triggers { 
        pollSCM('* * * * *') 
    }
    stages {
        stage('Checkout') {
            steps {
                git url: 'https://github.com/mrdbrg/jgsu-spring-petclinic', branch: 'main'
            }
        }
        
        stage('Build') {
            steps {
                sh './mvnw clean package'
            }

            post {
                always {
                    junit '**/target/surefire-reports/TEST-*.xml'
                    archiveArtifacts 'target/*.jar'

                    emailext subject: "Job \'${JOB_NAME}\' (${BUILD_NUMBER}) is waiting for input", 
                    body: "Pleas go to ${BUILD_URL} and verify the build", 
                    compressLog: true, 
                    attachLog: true,
                    to: "test@jenkins",
                    recipientProviders: [upstreamDevelopers(), requestor()]
                }
            }
        }
    }
}
