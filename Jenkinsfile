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

        stage('SonarQube Analysis') {
            steps {
                dir('back-end') {
                    withSonarQubeEnv('SonarQube') {
                        sh 'mvn clean verify sonar:sonar -Dsonar.projectKey=qualiair-back'
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