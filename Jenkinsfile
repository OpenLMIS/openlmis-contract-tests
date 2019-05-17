properties([
  [
    $class: 'ThrottleJobProperty',
    categories: ['pipeline'],
    throttleEnabled: true,
    throttleOption: 'category'
  ]
])
pipeline {
    agent {
        node {
            label '!master'
        }
    }
    options {
        buildDiscarder(logRotator(numToKeepStr: '30'))
        skipStagesAfterUnstable()
    }
    environment {
        PATH = "/usr/local/bin/:$PATH"
        COMPOSE_PROJECT_NAME = "contract-tests-${BRANCH_NAME}"
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
                    script {
                        notifyAfterFailure()
                    }
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
                timeout(time: 60, unit: 'MINUTES') {
                    script {
                        try {
                            sh "sudo rm -rf test-results"
                            sh "./run_contract_tests.sh docker-compose.${params.serviceName}.yml"
                        }
                        catch (exc) {
                            currentBuild.result = 'UNSTABLE'
                        }
                    }
                }
            }
            post {
                always {
                    junit healthScaleFactor: 1.0, testResults: 'test-results/cucumber-junit.xml'
                }
                unstable {
                    script {
                        notifyAfterFailure()
                    }
                }
                failure {
                    script {
                        notifyAfterFailure()
                    }
                }
                cleanup {
                    sh "sudo rm -rf test-results"
                }
            }
        }
    }
    post {
        cleanup {
            sh '''
                rm -Rf ./openlmis-config
                rm -f ./settings.env
            '''
        }
        fixed {
            slackSend color: 'good', message: "${env.JOB_NAME} - #${env.BUILD_NUMBER} - ${params.serviceName} Back to normal"
        }
    }
}

def notifyAfterFailure() {
    slackSend color: 'danger', message: "${env.JOB_NAME} - #${env.BUILD_NUMBER} - ${params.serviceName} ${currentBuild.result} (<${env.BUILD_URL}|Open>)"
    emailext subject: "${env.JOB_NAME} - #${env.BUILD_NUMBER} ${env.STAGE_NAME} ${currentBuild.result}",
        body: """<p>${env.JOB_NAME} - #${env.BUILD_NUMBER} ${env.STAGE_NAME} ${currentBuild.result}</p><p>Check console <a href="${env.BUILD_URL}">output</a> to view the results.</p>""",
        recipientProviders: [[$class: 'CulpritsRecipientProvider'], [$class: 'DevelopersRecipientProvider']]
}
