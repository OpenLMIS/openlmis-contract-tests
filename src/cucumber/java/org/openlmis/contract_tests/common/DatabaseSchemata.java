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

import static java.sql.DriverManager.getConnection;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DatabaseSchemata {
  private List<DatabaseSchema> schemata = new ArrayList<>();

  public List<DatabaseSchema> getSchemata() {
    return schemata;
  }

  public void setSchemata(List<DatabaseSchema> schemata) {
    this.schemata = schemata;
  }

  void init(String url, String username, String password) {
    try (Connection connection = getConnection(url, username, password)) {
      schemata.forEach(schema -> schema.init(connection));
    } catch (SQLException exp) {
      throw new InitialDataException(exp);
    }
  }

  void removeData(String url, String username, String password) {
    try (Connection connection = getConnection(url, username, password)) {
      schemata.forEach(schema -> schema.removeData(connection));
    } catch (SQLException exp) {
      throw new InitialDataException(exp);
    }
  }

  void loadData(String url, String username, String password) {
    try (Connection connection = getConnection(url, username, password)) {
      schemata.forEach(schema -> schema.loadData(connection));
    } catch (SQLException exp) {
      throw new InitialDataException(exp);
    }
  }

}
