/*
 * This program is part of the OpenLMIS logistics management information system platform software.
 * Copyright © 2017 VillageReach
 *
 * This program is free software: you can redistribute it and/or modify it under the terms
 * of the GNU Affero General Public License as published by the Free Software Foundation, either
 * version 3 of the License, or (at your option) any later version.
 *  
 * This program is distributed in the hope that it will be useful, but WITHOUT ANY WARRANTY;
 * without even the implied warranty of MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. 
 * See the GNU Affero General Public License for more details. You should have received a copy of
 * the GNU Affero General Public License along with this program. If not, see
 * http://www.gnu.org/licenses.  For additional information contact info@OpenLMIS.org. 
 */

package org.openlmis.contract_tests.common;

import java.io.IOException;
import java.util.Properties;

public class TestVariableReader {

    private static final String PASSWORD_SUFFIX = ".password";
    private static final String SERVICE_BASE_URL_SUFFIX = ".service.base.url";

    public static final Properties properties = new Properties();

    static {
        try {
            properties.load(TestVariableReader.class.getClassLoader()
                .getResourceAsStream("test_variables.properties"));
        } catch (IOException ignored) {
        }
    }

    public static String passwordOf(String userName) {
        return properties.getProperty(userName.toLowerCase() + PASSWORD_SUFFIX);
    }

    public static String baseUrlOfService(String serviceName) {
        return System.getenv("BASE_URL") + properties.getProperty(serviceName + SERVICE_BASE_URL_SUFFIX);
    }
}
