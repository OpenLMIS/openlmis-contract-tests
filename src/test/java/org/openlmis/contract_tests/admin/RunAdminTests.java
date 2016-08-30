package org.openlmis.contract_tests.admin;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"html:build/cucumber"}, glue = {"org.openlmis.contract_tests"})
public class RunAdminTests {
}
