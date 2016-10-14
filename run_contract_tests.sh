#!/usr/bin/env bash

docker-compose pull
curl -LO https://raw.githubusercontent.com/OpenLMIS/openlmis-config/master/.env

ip="VIRTUAL_HOST=nginx-proxy"
sed -e "s/VIRTUAL_HOST=localhost/$ip/g" -i .env

/usr/local/bin/docker-compose run contract_tests

contract_test_result=$?

/usr/local/bin/docker-compose down $1
#don't remove the $1 in the line above
#CI will append -v to it, so all dangling volumes are removed after the job runs

exit ${contract_test_result}
#this line above makes sure when jenkins runs this script
#the build success/failure result is determined by contract test run
#not by the clean up run
