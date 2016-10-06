package org.openlmis.contract_tests.common;

import java.io.IOException;
import java.util.Properties;

public class TestVariableReader {

    private static final String PASSWORD_SUFFIX = ".password";
    private static final String SERVICE_BASE_URL_SUFFIX = ".service.base.url";

    private static final String VIRTUAL_HOST_SUFFIX = "http://" + System.getenv("VIRTUAL_HOST");
    public static final Properties properties = new Properties();

    static {
        try {
            properties.load(TestVariableReader.class.getClassLoader()
                .getResourceAsStream("test_variables.properties"));
        } catch (IOException ignored) {
        }
    }

    public static String passwordOf(String userName) {
        return properties.getProperty(userName + PASSWORD_SUFFIX);
    }

    public static String baseUrlOfService(String serviceName) {
        return VIRTUAL_HOST_SUFFIX + properties.getProperty(serviceName + SERVICE_BASE_URL_SUFFIX);
    }
}
