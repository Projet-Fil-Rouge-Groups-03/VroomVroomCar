pipeline {
    agent any

    tools {
        maven 'maven'
    }

    stages {

        stage('Clean up') {
            steps {
                sh 'rm -rf back-end'
                sh 'rm -rf front-end'
            }
        }

        stage('Git Clone Repos') {
            parallel {
                stage('Back-end') {
                    steps {
                        dir('back-end') {
                            git url: 'https://github.com/Projet-Fil-Rouge-Groups-03/VroomVroomCar', branch: 'jenkins'
                        }
                    }
                }
                stage('Front-end') {
                    steps {
                        dir('front-end') {
                            git url: 'https://github.com/Projet-Fil-Rouge-Groups-03/VroomVroomCar-front', branch: 'dev'
                        }
                    }
                }
            }
        }

        stage('Write Spring config') {
            steps {
                dir('back-end') {
                    withCredentials([
                        file(credentialsId: 'API_PROPERTIES_APPLICATION', variable: 'app_properties'),
                        file(credentialsId: 'API_ENV_PROPERTIES_APPLICATION', variable: 'app_env_properties')
                    ]) {
                        sh '''
                            mkdir -p src/main/resources
                            cp $app_properties src/main/resources/application.properties
                        '''
                    }
                }
            }
        }

        stage('SonarQube Analysis') {
            steps {
                dir('back-end') {
                    withSonarQubeEnv('SonarQube') {
                        sh 'mvn clean verify sonar:sonar -Dsonar.projectKey=VroomVroomCar'
                    }
                }
            }
        }

        stage('Compile with Maven') {
            steps {
                dir('back-end') {
                    sh 'mvn clean install'
                }
            }
        }
    }
}