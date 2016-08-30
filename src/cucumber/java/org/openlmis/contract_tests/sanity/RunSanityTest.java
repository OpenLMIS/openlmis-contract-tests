package org.openlmis.contract_tests.sanity;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import org.junit.runner.RunWith;

@RunWith(Cucumber.class)
@CucumberOptions(plugin = {"html:build/cucumber"})
//this is just a sanity check test to make sure the project is set up correctly
public class RunSanityTest {
}
