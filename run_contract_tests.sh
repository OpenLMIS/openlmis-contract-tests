#!/usr/bin/env bash

FILENAME=${1}

#Set our environment variables to the values specified within settings.env and export (expose) the BASE_URL one.
source ./settings.env
export BASE_URL

#make sure that old containers have been stopped
/usr/local/bin/docker-compose -f docker-compose.yml -f ${FILENAME} down -v --remove-orphans

#pull all images
/usr/local/bin/docker-compose -f docker-compose.yml -f ${FILENAME} pull

#run docker file
/usr/local/bin/docker-compose -f docker-compose.yml -f ${FILENAME} run contract_tests

#cleaning after tests
contract_test_result=$?

echo "========== Logging output from containers =========="
/usr/local/bin/docker-compose logs
echo "========== Logging output from syslog =========="
cp ../logs/requisition/messages build/syslog
echo "========== Logging nginx settings =========="
/usr/local/bin/docker-compose exec nginx cat /etc/nginx/conf.d/default.conf

/usr/local/bin/docker-compose -f docker-compose.yml -f ${FILENAME} down -v --remove-orphans

exit ${contract_test_result}
#this line above makes sure when jenkins runs this script
#the build success/failure result is determined by contract test run not by the clean up run
