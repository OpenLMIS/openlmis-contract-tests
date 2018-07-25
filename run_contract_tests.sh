#!/usr/bin/env bash

FILENAME=${1}
TEST_RESULTS_DIR="test-results"

#Set our environment variables to the values specified within settings.env and export (expose) the BASE_URL one.
source ./settings.env
export BASE_URL

# prepare files because docker changes owner of build directory
mkdir -p ${TEST_RESULTS_DIR}

#make sure that old containers have been stopped
/usr/local/bin/docker-compose -f docker-compose.yml -f ${FILENAME} down -v --remove-orphans

#pull all images
/usr/local/bin/docker-compose -f docker-compose.yml -f ${FILENAME} pull

#run docker file
/usr/local/bin/docker-compose -f docker-compose.yml -f ${FILENAME} run contract_tests

#cleaning after tests
contract_test_result=$?

/usr/local/bin/docker-compose logs --no-color > ${TEST_RESULTS_DIR}/container-logs
/usr/local/bin/docker-compose exec -T log cat /var/log/messages > ${TEST_RESULTS_DIR}/sys-logs
/usr/local/bin/docker-compose exec -T nginx cat /etc/nginx/conf.d/default.conf > ${TEST_RESULTS_DIR}/nginx

echo "Logs and nginx settings can be found in ${TEST_RESULTS_DIR}"

/usr/local/bin/docker-compose -f docker-compose.yml -f ${FILENAME} down -v --remove-orphans

exit ${contract_test_result}
#this line above makes sure when jenkins runs this script
#the build success/failure result is determined by contract test run not by the clean up run
