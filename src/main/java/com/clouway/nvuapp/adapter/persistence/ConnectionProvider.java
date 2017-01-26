package com.clouway.nvuapp.adapter.persistence;

import com.clouway.nvuapp.core.ConnectionPool;
import com.clouway.nvuapp.core.Provider;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * @author Vasil Mitov <v.mitov.clouway@gmail.com>
 */
public class ConnectionProvider implements Provider<Connection> {

  @Override
  public Connection get() {
    Connection connection = null;
    try {
      connection = ConnectionPool.get().getConnection();
    } catch (SQLException e) {
      e.printStackTrace();
    }
    return connection;
  }
}
