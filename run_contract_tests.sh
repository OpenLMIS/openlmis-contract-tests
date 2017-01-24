#!/usr/bin/env bash

FILENAME=${1}

#download env file
curl -LO https://raw.githubusercontent.com/OpenLMIS/openlmis-config/master/.env

#pull all images
/usr/local/bin/docker-compose -f docker-compose.yml -f ${FILENAME} pull

#change VIRTUAL_HOST value from localhost to nginx
ip="VIRTUAL_HOST=nginx"
sed -e "s/VIRTUAL_HOST=localhost/$ip/g" -i .env

#change CONSUL_HOST value from localhost to consul
ip="CONSUL_HOST=consul"
sed -e "s/CONSUL_HOST=localhost/$ip/g" -i .env

#change MAIL_HOST value from localhost to smtp.gmail.com
mail="MAIL_HOST=smtp.gmail.com"
sed -e "s/MAIL_HOST=localhost/$mail/g" -i .env

#change MAIL_PORT value from 25 to 465
mail="MAIL_PORT=465"
sed -e "s/MAIL_PORT=25/$mail/g" -i .env

#change MAIL_USERNAME value from noreply@openlmis.org to contract.tests@gmail.com
mail="MAIL_USERNAME=contract.tests@gmail.com"
sed -e "s/MAIL_USERNAME=noreply@openlmis.org/$mail/g" -i .env

#change MAIL_PASSWORD value from "" to olmis1234
mail="MAIL_PASSWORD=olmis1234"
sed -e "s/MAIL_PASSWORD=/$mail/g" -i .env

#run docker file
/usr/local/bin/docker-compose -f docker-compose.yml -f ${FILENAME} run contract_tests

#cleaning after tests
contract_test_result=$?

if [ $contract_test_result -ne 0 ]; then
  echo "========== Logging output from containers =========="
  /usr/local/bin/docker-compose logs
fi

/usr/local/bin/docker-compose -f docker-compose.yml -f ${FILENAME} down $2
#don't remove the $1 in the line above
#CI will append -v to it, so all dangling volumes are removed after the job runs

exit ${contract_test_result}
#this line above makes sure when jenkins runs this script

#the build success/failure result is determined by contract test run not by the clean up run
