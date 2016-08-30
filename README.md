# openlmis-contract-tests

This repository is intended to contain contract tests of OpenLMIS 3.0

Contract tests should be organized around business scenarios that requires multiple services to work together.

Contract tests should be sending real http requests to and verifying real http response from running services. Mocking and stubbing techniques should reside within component tests of each single service.

## Example

For a certain business scenario named A, it requires:

  1. a post end point of service X
  2. a get end point of service Y

to work together.

In that case, we should create a number of contract tests in this repository, which of course would call those two end points.
All of them organized in the same [suite](https://github.com/junit-team/junit4/wiki/aggregating-tests-in-suites) under business scenario A's name.

Then in service **X's CI pipeline** we could have:

run X [individual job](https://docs.google.com/document/d/1TZ55h0F1fHr901bNN76-A5cc_7PeiD02rla5F9eyPEk/edit#heading=h.opoz13632el) -> run contract tests that involves **X**(including scenario A) -> other steps

And in service **Y's CI pipeline** we could have:

run Y [individual job](https://docs.google.com/document/d/1TZ55h0F1fHr901bNN76-A5cc_7PeiD02rla5F9eyPEk/edit#heading=h.opoz13632el) -> run contract tests that involves **Y**(including scenario A) -> other steps


The example above is simple, it involves only 2 services and 2 end points.

However, the 
##principle
applies for more complex situations as well:

  1. organize contract tests by business scenarios
  2. if a certain scenario involves a certain service, run it as a part of that service's pipeline

The intention to organize contract tests in suites is to enable us to run them separately in different pipelines.
So that the contract test is ran whenever **any service that is involved** in this scenario has any code change.

Then intention to put all contract tests in this one repository is to avoid explosion of docker compose files in service repositories, as interaction between services increase. And it also provides convenience to run all contract test as a part of full regression.
