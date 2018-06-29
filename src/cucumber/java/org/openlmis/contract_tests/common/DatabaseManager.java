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

/**
 * The {@code DatabaseManager} is responsible for generating dump of current database, removing
 * data from the database and loading earlier created dump back to database. To create the dump
 * of database the class uses {@code database_schemas.yml}.
 *
 * @see DatabaseSchemata
 * @see DatabaseSchema
 * @see DatabaseSchemaTable
 */
public class DatabaseManager {
  private final Object lock = new Object();
  private boolean initiated;

  private DatabaseSchemata schemata;

  /**
   * Generates dump of current database. This method can be executed many times but
   * initialization process will occur only one time.
   */
  public void init() {
    if (!initiated) {
      synchronized (lock) {
        if (!initiated) {
          ClassLoader classLoader = getClass().getClassLoader();
          Yaml yaml = new Yaml();

          try (InputStream stream = classLoader.getResourceAsStream("database_schemas.yml")) {
            schemata = yaml.loadAs(stream, DatabaseSchemata.class);

            schemata.init();
            initiated = true;
          } catch (SQLException | IOException exp) {
            throw new InitialDataException(exp);
          }
        }
      }
    }

  }

  /**
   * Removes data from the database.
   */
  public void removeData() {
    try {
      schemata.removeData();
    } catch (SQLException exp) {
      throw new RemoveDataException(exp);
    }
  }

  /**
   * Loads data to the database.
   */
  public void loadData() {
    try {
      schemata.loadData();
    } catch (SQLException exp) {
      throw new LoadDataException(exp);
    }
  }

}
