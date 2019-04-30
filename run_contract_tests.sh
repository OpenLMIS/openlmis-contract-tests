#!/usr/bin/env bash

FILENAME=${1}
TEST_RESULTS_DIR="test-results"
export PATH="/usr/local/bin:$PATH"

cleanup() {
    docker-compose -f docker-compose.yml -f "${FILENAME}" down -v --remove-orphans
}
trap cleanup EXIT

#Set our environment variables to the values specified within settings.env and export (expose) the BASE_URL one.
source ./settings.env
export BASE_URL

# prepare files because docker changes owner of build directory
mkdir -p ${TEST_RESULTS_DIR}

# fixed problem with empty test_report.xml on slave (OLMIS-4386)
touch ${TEST_RESULTS_DIR}/cucumber-junit.xml

#make sure that old containers have been stopped
docker-compose -f docker-compose.yml -f "${FILENAME}" down -v --remove-orphans

#pull all images
docker-compose -f docker-compose.yml -f "${FILENAME}" pull

#run docker file
docker-compose -f docker-compose.yml -f "${FILENAME}" run contract_tests

#cleaning after tests
contract_test_result=$?

docker-compose logs --no-color --timestamps > ${TEST_RESULTS_DIR}/container-logs
docker-compose exec -T log cat /var/log/messages > ${TEST_RESULTS_DIR}/sys-logs
docker-compose exec -T nginx cat /etc/nginx/conf.d/default.conf > ${TEST_RESULTS_DIR}/nginx

echo
echo "=========================================================================="
echo "  Logs and nginx settings can be found in ${TEST_RESULTS_DIR}"
echo "=========================================================================="
echo "  CONTAINER LOGS"
echo "=========================================================================="
cat ${TEST_RESULTS_DIR}/container-logs
echo "=========================================================================="
echo "  SYSTEM LOGS"
echo "=========================================================================="
cat ${TEST_RESULTS_DIR}/sys-logs | egrep -v "(Resource2Db|RightAssignmentService)"
echo "=========================================================================="
echo "  LOADED DEMO DATA"
echo "=========================================================================="
egrep -o "Resource2Db (.+)\.(.+): \[" test-results/sys-logs | awk '{print substr($0, 13, length($0) - 16)}' | sort | uniq -c | sort -nr
echo "=========================================================================="
echo "  NGINX SETTINGS"
echo "=========================================================================="
cat ${TEST_RESULTS_DIR}/nginx
echo

exit ${contract_test_result}
#this line above makes sure when jenkins runs this script
#the build success/failure result is determined by contract test run
