pipeline {
    agent any
    options {
        buildDiscarder(logRotator(numToKeepStr: '30'))
        disableConcurrentBuilds()
    }
    environment {
        PATH = "/usr/local/bin/:$PATH"
    }
    parameters {
        string(name: 'serviceName', defaultValue: '', description: 'Name of the service. Used to determine which contract tests to run.')
        text(name: 'customEnv', defaultValue: '', description: 'A list of environment variables that will be appended to the .env file. This parameter can be used to provide image versions.')
    }
    stages {
        stage('Preparation') {
            steps {
                script {
                    if (params.serviceName) {
                        currentBuild.displayName += " - " + params.serviceName
                    }
                }
                checkout scm

                dir('openlmis-config') {
                    git branch: 'master',
                        credentialsId: 'OpenLMISConfigKey',
                        url: 'git@github.com:villagereach/openlmis-config.git'
                }
                sh 'set +x'
                sh 'cp ./openlmis-config/contract_tests.env ./settings.env'

                sh "echo \"${params.customEnv}\" >> .env"
            }
            post {
                failure {
                    slackSend color: 'danger', message: "${env.JOB_NAME} - #${env.BUILD_NUMBER} FAILED (<${env.BUILD_URL}|Open>)"
                }
            }
        }
        stage('Contract tests') {
            when {
                expression {
                    def exists = fileExists "docker-compose.${params.serviceName}.yml"
                    return exists
                }
            }
            steps {
                sh '''
                    sudo rm -rf build
                    # fixed problem with empty test_report.xml on slave (OLMIS-4386)
                    mkdir -p build/cucumber/junit
                    touch build/cucumber/junit/test_report.xml
                '''
                sh "./run_contract_tests.sh docker-compose.${params.serviceName}.yml -v"
            }
            post {
                always {
                    junit healthScaleFactor: 1.0, testResults: 'build/cucumber/junit/**.xml'
                }
                failure {
                    slackSend color: 'danger', message: "${env.JOB_NAME} - #${env.BUILD_NUMBER} - ${params.serviceName} FAILED (<${env.BUILD_URL}|Open>)"
                }
            }
        }
    }
    post {
        always {
            sh '''
                rm -Rf ./openlmis-config
                rm -f ./settings.env
            '''
        }
    }
}
