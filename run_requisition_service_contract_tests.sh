#!/usr/bin/env bash
curl -LO https://raw.githubusercontent.com/OpenLMIS/openlmis-config/master/.env

docker-compose -f docker-compose.requisition-service.yml run contract_tests

docker-compose -f docker-compose.requisition-service.yml down $1
#don't remove the $1 in the line above
#CI will append -v to it, so all dangling volumes are removed after the job runs