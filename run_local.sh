#!/usr/bin/env bash

FILENAME=${1}
PULL_IMAGES=${2}

#Set our environment variables to the values specified within settings.env and export (expose) the BASE_URL one.
source ./settings.env
export BASE_URL

#pull all images
if [ "$PULL_IMAGES" = "pull" ]; then
docker-compose -f docker-compose.yml -f ${FILENAME} pull
fi

#run docker file
docker-compose -f docker-compose.yml -f ${FILENAME} run contract_tests
contract_test_result=$?

#save logs
docker-compose logs > build/logs
docker-compose exec nginx cat /etc/nginx/conf.d/default.conf > build/nginx.conf

#cleaning after tests
docker-compose -f docker-compose.yml -f ${FILENAME} down -v

#this line makes sure when jenkins runs this script
#the build success/failure result is determined by contract test run not by the clean up run
exit ${contract_test_result}
