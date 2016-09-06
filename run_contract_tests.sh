#!/usr/bin/env bash

set -e

curl -LO https://raw.githubusercontent.com/OpenLMIS/openlmis-config/master/.env

/usr/local/bin/docker-compose -f $1 run contract_tests

/usr/local/bin/docker-compose -f $1 down $2
#don't remove the $2 in the line above
#CI will append -v to it, so all dangling volumes are removed after the job runs