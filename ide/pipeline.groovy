pipeline {
    agent any
    
    // by Marlon Braga
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
                }
            }
        }
    }
}
