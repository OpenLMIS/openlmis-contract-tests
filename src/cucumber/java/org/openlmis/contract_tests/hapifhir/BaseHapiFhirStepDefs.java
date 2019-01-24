package org.openlmis.contract_tests.hapifhir;

import cucumber.api.java.Before;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

class BaseHapiFhirStepDefs {

  private final Map<String, TestHelper> helpers = new HashMap<>();

  @Before("@HapiFhirTests")
  public void setUp() {
    helpers.put(TestHelper.FACILITY, new FacilityTestHelper());
    helpers.put(TestHelper.GEO_ZONE, new GeographicZoneTestHelper());
    helpers.put(TestHelper.LOCATION, new LocationTestHelper());
    helpers.put(TestHelper.SUBSCRIPTION, new SubscriptionTestHelper());
  }

  TestHelper findHelper(String resource) {
    return Optional
        .ofNullable(helpers.get(resource))
        .orElseThrow(() -> new IllegalStateException("Unsupported resource: " + resource));
  }

}
