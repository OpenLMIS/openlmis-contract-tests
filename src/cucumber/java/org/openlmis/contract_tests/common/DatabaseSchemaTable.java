package org.openlmis.contract_tests.common;

import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

/**
 * This class contains information about schema tables. Is it possible to set which tables should
 * be skipped in the process of generating a dump of database. Also for some schema the table
 * order is important and can be set in this class.
 */
public class DatabaseSchemaTable {
  private List<String> excluded;
  private List<String> ordered;
  private List<String> all;

  public List<String> getExcluded() {
    return excluded;
  }

  public void setExcluded(List<String> excluded) {
    this.excluded = excluded;
  }

  public List<String> getOrdered() {
    return ordered;
  }

  public void setOrdered(List<String> ordered) {
    this.ordered = ordered;
  }

  void readAllTables(DatabaseMetaData metaData, String schema) throws SQLException {
    List<String> remaining = new ArrayList<>();

    try (ResultSet rs = metaData.getTables(null, schema, null, null)) {
      while (rs.next()) {
        String tableName = rs.getString("TABLE_NAME");
        String tableType = rs.getString("TABLE_TYPE");

        if (acceptTable(tableType, tableName)) {
          remaining.add(tableName);
        }

      }
    }

    remaining.removeAll(ordered);

    all = new ArrayList<>();
    all.addAll(ordered);
    all.addAll(remaining);
  }

  void dumpData(Connection connection, PrintWriter result, String schema) throws SQLException {
    for (String table : all) {
      String tableName = schema + '.' + table;
      String sql = prepareSql(tableName);

      try (PreparedStatement select = connection.prepareStatement(sql)) {
        try (ResultSet data = select.executeQuery()) {
          if (!data.isBeforeFirst()) {
            continue;
          }

          result
              .append("INSERT INTO ")
              .append(tableName)
              .append("(SELECT * FROM json_populate_recordset(NULL::")
              .append(tableName)
              .append(", '[");

          while (data.next()) {
            result.append(data.getString(1));

            if (!data.isLast()) {
              result.append(",");
            }
          }

          result.append("]'));\n");
        }
      }
    }
  }

  void removeData(Connection connection, String schema) throws SQLException {
    try (Statement statement = connection.createStatement()) {
      for (String table : all) {
        String sql = String.format("TRUNCATE %s.%s RESTART IDENTITY CASCADE;", schema, table);
        statement.execute(sql);
      }
    }
  }

  private boolean acceptTable(String tableType, String tableName) {
    return "TABLE".equalsIgnoreCase(tableType)
        && !excluded.contains(tableName);
  }

  private String prepareSql(String tableName) {
    return "SELECT row_to_json(x) FROM " + tableName + " AS x";
  }
}
