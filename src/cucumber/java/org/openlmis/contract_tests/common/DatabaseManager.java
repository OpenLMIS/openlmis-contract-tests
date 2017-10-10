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

import org.yaml.snakeyaml.Yaml;

import java.io.IOException;
import java.io.InputStream;
import java.sql.SQLException;

public class DatabaseManager {
  private static final String DATABASE_URL = System.getenv("DATABASE_URL");
  private static final String USER_NAME = System.getenv("POSTGRES_USER");
  private static final String PASSWORD = System.getenv("POSTGRES_PASSWORD");

  private final Object LOCK = new Object();
  private boolean initiated;

  private DatabaseSchemata schemata;

  public void init() {
    if (!initiated) {
      synchronized (LOCK) {
        if (!initiated) {
          ClassLoader classLoader = getClass().getClassLoader();
          Yaml yaml = new Yaml();

          try (InputStream stream = classLoader.getResourceAsStream("database_schemas.yml")) {
            schemata = yaml.loadAs(stream, DatabaseSchemata.class);

            schemata.init(DATABASE_URL, USER_NAME, PASSWORD);
            initiated = true;
          } catch (SQLException | IOException exp) {
            throw new InitialDataException(exp);
          }
        }
      }
    }

  }

  public void removeData() {
    try {
      schemata.removeData(DATABASE_URL, USER_NAME, PASSWORD);
    } catch (SQLException exp) {
      throw new RemoveDataException(exp);
    }
  }

  public void loadData() {
    try {
      schemata.loadData(DATABASE_URL, USER_NAME, PASSWORD);
    } catch (SQLException | IOException exp) {
      throw new LoadDataException(exp);
    }
  }

}
