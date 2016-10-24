package org.openlmis.contract_tests.exception;

public class InitialDataExeption extends Exception {

  public InitialDataExeption(String message) {
    super(message);
  }

  public InitialDataExeption(String message, Throwable cause) {
    super(message, cause);
  }
}
