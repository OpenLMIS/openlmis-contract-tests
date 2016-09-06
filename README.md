# openlmis-contract-tests

This repository is intended to contain contract tests of OpenLMIS 3.0

Contract tests should be organized around business scenarios that requires multiple services to work together.

Contract tests should be sending real http requests to and verifying real http response from running services. Mocking and stubbing techniques should reside within component tests of each single service.

## Hypothetical Example

For a certain business scenario named A, it requires:

  1. a post end point of service X
  2. a get end point of service Y

to work together.

In that case, we should create a number of contract tests in this repository, which of course would call those two end points.
All of them organized in the same [suite](https://github.com/cucumber/cucumber/wiki/Tags) under business scenario A's name.

Then in service **X's CI pipeline** we could have:

run X [individual job](https://docs.google.com/document/d/1TZ55h0F1fHr901bNN76-A5cc_7PeiD02rla5F9eyPEk/edit#heading=h.opoz13632el) -> run contract tests that involves **X**(including scenario A) -> other steps

And in service **Y's CI pipeline** we could have:

run Y [individual job](https://docs.google.com/document/d/1TZ55h0F1fHr901bNN76-A5cc_7PeiD02rla5F9eyPEk/edit#heading=h.opoz13632el) -> run contract tests that involves **Y**(including scenario A) -> other steps


The example above is simple, it involves only 2 services and 2 end points.

However, the 

## Principle

applies for more complex situations as well:

  1. organize contract tests by business scenarios
  2. if a certain scenario involves a certain service, run it as a part of that service's pipeline

The intention to organize contract tests in suites is to enable us to run them separately in different pipelines.
So that the contract test is ran whenever **any service that is involved** in this scenario has any code change.

Then intention to put all contract tests in this one repository is to avoid explosion of docker compose files in service repositories, as interaction between services increase. And it also provides convenience to run all contract test as a part of full regression.

## Concrete example

For of scenario of "admin user should be able set up the system". 
It needs both auth and requisition services. 

The following things should be done:

#### Create cucumber feature file
This file describes a list of steps that the test should take, in a human readability friendly manner.
Look at [administration.feature](https://github.com/OpenLMIS/openlmis-contract-tests/blob/master/src/cucumber/resources/org/openlmis/contract_tests/admin/administration.feature) for example.

#### Create step definitions
The step definition file provides the feature file with runnable code.
Both need to be present for cucumber to run.
Look at [AdminStepDefs.java](https://github.com/OpenLMIS/openlmis-contract-tests/blob/master/src/cucumber/java/org/openlmis/contract_tests/admin/AdminStepDefs.java) for example.

#### Assign cucumber feature appropriate tag 
Tags allow us to run different test cases in pipelines of different services.
Look at the first line of [administration.feature](https://github.com/OpenLMIS/openlmis-contract-tests/blob/master/src/cucumber/resources/org/openlmis/contract_tests/admin/administration.feature) for example.

When tag is defined, it allows us to run:
`gradle clean cucumber -Ptags=@admin,@otherTag,@yetAnotherTag`
This will only run test cases that match the tags, nothing else.

#### Define docker compose file (if not already present)
This docker compose file will be used by CI to link required containers together and run the tests.
Look at [docker-compose.requisition-service.yml](https://github.com/OpenLMIS/openlmis-contract-tests/blob/master/docker-compose.requisition-service.yml) for example.

#### Configure Jenkins job (if not already present)
Create a new job in both auth service and requisition service's pipelines, which uses the compose file defined in the previous step to run tests.

By doing this step, we can ensure that when either service's code changes, the test is ran.
Look at [this job](http://build.openlmis.org/view/Requisitoin/job/OpenLMIS-requisition-contract-test/configure) for example.