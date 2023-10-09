pipeline {
    agent any
    
    tools {
        jdk  'jdk17'
        maven  'maven'
    }
    stages {
        stage('Git Checkout') {
          steps {
            git branch: 'master', url: 'https://github.com/IliasBrinias/CarApi'
          }
        }
        stage('Code Stability') {
            steps {
                sh "mvn clean compile -DskipTests=true"
            }
        }
        stage('Docker Check'){
            steps {
                sh '~/bin/trivy image nginx:alpine'
            }
        }
        stage('SonarQube') {
            steps{
                withSonarQubeEnv('sonarqube-9.9.2'){
                    sh '''
                        mvn clean verify sonar:sonar \
                          -Dsonar.projectKey=SchoolAPI \
                          -Dsonar.host.url=https://642b-109-178-143-5.ngrok-free.app\
                          -Dsonar.login=sqp_a6a4c1f157c8b472314e424c94c98eef7513b6de
                    '''
                }
            }
        }
        stage('Build') {
            steps {
                sh "mvn clean package -DskipTests=true"
            }
        }
   }
}
