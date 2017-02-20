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

import com.ibatis.common.jdbc.ScriptRunner;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

public class TestDatabaseConnection {

  private static final String DATABASE_URL = System.getenv("DATABASE_URL");
  private static final String USER_NAME = System.getenv("POSTGRES_USER");
  private static final String PASSWORD = System.getenv("POSTGRES_PASSWORD");
  private static final String PATH_TO_SCRIPT = "/demo-data/";
  private static final String FILE_NAME_SUFFIX = "_input.sql";

  List<String> SCHEMAS = Collections.unmodifiableList(
      Arrays.asList("referencedata", "requisition", "auth", "fulfillment"));

  public void loadData() throws InitialDataException {
    Connection connection = null;
    Statement statementToLoadScrpit = null;
    try {
      connection = DriverManager.getConnection(DATABASE_URL, USER_NAME, PASSWORD);
      statementToLoadScrpit = connection.createStatement();

      ScriptRunner runner = new ScriptRunner(connection, false, true);
      try {
        for (String schema : SCHEMAS) {
          runner.runScript(new BufferedReader(new FileReader(
              String.valueOf(Paths.get(getClass()
                  .getResource(PATH_TO_SCRIPT + schema + FILE_NAME_SUFFIX).toURI())))));
        }
      } catch (IOException ex) {
        throw new InitialDataException("InitialDataException: Not find demo-data file.", ex);
      } catch (URISyntaxException ex) {
        throw new InitialDataException(
            "InitialDataException: Path to file not be parsed as a URI.", ex);
      }

    } catch (SQLException ex) {
      throw new InitialDataException("InitialDataException: Database connection error.", ex);
    } finally {
      try {

        if (statementToLoadScrpit != null) {
          statementToLoadScrpit.close();
        }
        if (connection != null) {
          connection.close();
        }
      } catch (SQLException ex) {
        throw new InitialDataException(
            "InitialDataException: Cannot close database connection.", ex);
      }
    }
  }

  public void removeData() throws InitialDataException {
    Connection connection = null;
    Statement statementToReadTablesName = null;
    Statement statementToTruncateTable = null;
    ResultSet resultSetOfTablesNameQuery = null;
    try {
      connection = DriverManager.getConnection(DATABASE_URL, USER_NAME, PASSWORD);
      statementToReadTablesName = connection.createStatement();
      statementToTruncateTable = connection.createStatement();

      for (String schema : SCHEMAS) {
        resultSetOfTablesNameQuery = statementToReadTablesName.executeQuery(
            "SELECT table_name \n" +
                " FROM information_schema.tables\n" +
                " WHERE table_schema='" + schema + "';");

        while (!resultSetOfTablesNameQuery.isClosed() && resultSetOfTablesNameQuery.next()) {
          String tableName = resultSetOfTablesNameQuery.getString(1);
          statementToTruncateTable.execute("TRUNCATE " + schema + "." +
              tableName + " RESTART IDENTITY CASCADE;");
        }
      }

    } catch (SQLException ex) {
      throw new InitialDataException("InitialDataException: Database connection error.", ex);
    } finally {
      try {
        if (resultSetOfTablesNameQuery != null) {
          resultSetOfTablesNameQuery.close();
        }
        if (statementToReadTablesName != null) {
          statementToReadTablesName.close();
        }
        if (statementToTruncateTable != null) {
          statementToTruncateTable.close();
        }
        if (connection != null) {
          connection.close();
        }
      } catch (SQLException ex) {
        throw new InitialDataException(
            "InitialDataException: Cannot close database connection.", ex);
      }
    }
  }

}
