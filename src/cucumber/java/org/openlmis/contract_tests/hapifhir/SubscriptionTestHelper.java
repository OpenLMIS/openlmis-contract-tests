package org.openlmis.contract_tests.hapifhir;

class SubscriptionTestHelper extends FhirResourceTestHelper {

  @Override
  String getResourceUrl() {
    return SUBSCRIPTION_URL;
  }

  @Override
  public void verifyFhirResourceAfterCreate(String resource, Object resourceId) {
    throw new UnsupportedOperationException("No related resources are created for subscriptions");
  }

  @Override
  public void verifyFhirResourceAfterUpdate(String resource, Object resourceId) {
    throw new UnsupportedOperationException("No related resources are updated for subscriptions");
  }

}
