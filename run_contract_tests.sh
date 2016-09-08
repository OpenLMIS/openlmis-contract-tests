#!/usr/bin/env bash

set -e

curl -LO https://raw.githubusercontent.com/OpenLMIS/openlmis-config/master/.env

/usr/local/bin/docker-compose -f $1 run contract_tests

contract_test_result=$?

/usr/local/bin/docker-compose -f $1 down $2
#don't remove the $2 in the line above
#CI will append -v to it, so all dangling volumes are removed after the job runs

exit ${contract_test_result}
#this line above makes sure when jenkins runs this script
#the build success/failure result is determined by contract test run
#not by the clean up run