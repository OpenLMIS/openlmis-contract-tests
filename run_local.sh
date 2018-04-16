#!/usr/bin/env bash

FILENAME=${1}
PULL_IMAGES=${2}

#Set our environment variables to the values specified within settings.env and export (expose) the BASE_URL one.
source ./settings.env
export BASE_URL

#pull all images
if [ "$PULL_IMAGES" = "pull" ]; then
/usr/local/bin/docker-compose -f docker-compose.yml -f ${FILENAME} pull
fi

#run docker file
/usr/local/bin/docker-compose -f docker-compose.yml -f ${FILENAME} run contract_tests
