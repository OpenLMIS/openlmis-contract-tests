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

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class DatabaseSchema {
  private String name;
  private DatabaseSchemaTable table;

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public DatabaseSchemaTable getTable() {
    return table;
  }

  public void setTable(DatabaseSchemaTable table) {
    this.table = table;
  }

  private Path getFilePath() {
    return Paths.get("/app", "dump", name + ".sql");
  }

  void init(Connection connection) {
    createFile();

    try {
      DatabaseMetaData metaData = connection.getMetaData();
      table.readAllTables(metaData, name);

      try (PrintWriter result = new PrintWriter(getFilePath().toFile(), "UTF-8")) {
        table.dumpData(connection, result, name);
      }
    } catch (Exception exp) {
      throw new IllegalStateException("Can't create dump for schema: " + this.name, exp);
    }
  }

  void loadData(Connection connection) throws InitialDataException {
    try (Statement statement = connection.createStatement()) {
      try (BufferedReader reader = new BufferedReader(new FileReader(getFilePath().toFile()))) {
        String line;

        while ((line = reader.readLine()) != null) {
          statement.execute(line);
        }
      }
    } catch (SQLException | IOException exp) {
      throw new InitialDataException(exp);
    }
  }

  void removeData(Connection connection) throws InitialDataException {
    table.removeData(connection, name);
  }

  private void createFile() {
    try {
      Path filePath = getFilePath();

      if (Files.exists(filePath)) {
        Files.delete(filePath);
      }

      Path parent = filePath.getParent();

      if (Files.notExists(parent)) {
        Files.createDirectories(parent);
      }

      Files.createFile(filePath);
    } catch (IOException exp) {
      throw new InitialDataException(exp);
    }
  }

}
