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
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.function.BiConsumer;
import lombok.Getter;
import lombok.Setter;

/**
 * This class contains information about database schemata.
 *
 * @see DatabaseSchema
 */
public class DatabaseSchemata {
  private static final String DATABASE_URL = System.getenv("DATABASE_URL");
  private static final String USER_NAME = System.getenv("POSTGRES_USER");
  private static final String PASSWORD = System.getenv("POSTGRES_PASSWORD");

  @Getter
  @Setter
  private List<DatabaseSchema> schemata = new ArrayList<>();

  void init() throws SQLException, IOException {
    try (Connection connection = getConnection()) {
      for (DatabaseSchema schema : schemata) {
        schema.init(connection);
      }
    }
  }

  void removeData() throws SQLException {
    try (Connection connection = getConnection()) {
      doInTransaction(connection, (statement, schema) -> schema.removeData(statement));
    }
  }

  void loadData() throws SQLException {
    try (Connection connection = getConnection()) {
      doInTransaction(connection, (statement, schema) -> schema.loadData(statement));
    }
  }

  private Connection getConnection() throws SQLException {
    return DriverManager.getConnection(DATABASE_URL, USER_NAME, PASSWORD);
  }

  private void doInTransaction(Connection connection, BiConsumer<Statement, DatabaseSchema> action)
      throws SQLException {
    boolean oldAutoCommit = connection.getAutoCommit();
    connection.setAutoCommit(false);

    try {
      try (Statement statement = connection.createStatement()) {
        for (DatabaseSchema schema : schemata) {
          action.accept(statement, schema);
        }

        statement.executeBatch();
        connection.commit();
      }
    } catch (Exception ignored) {
      connection.rollback();
    } finally {
      connection.setAutoCommit(oldAutoCommit);
    }
  }

}
