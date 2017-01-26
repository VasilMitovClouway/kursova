package com.clouway.nvuapp.core;

import org.apache.commons.dbcp.BasicDataSource;

/**
 * @author Denis Dimitrov <denis.k.dimitrov@gmail.com>.
 */
public class ConnectionPool {
  private static BasicDataSource source = null;

  public static synchronized BasicDataSource get() {
    if (source == null) {
      source = new BasicDataSource();
      String host = System.getenv("host");
      String user = System.getenv("user");
      String pass = System.getenv("pass");
      String dbName = System.getenv("db");

      source.setDriverClassName("com.mysql.jdbc.Driver");
      source.setUsername(user);
      source.setPassword(pass);
      source.setUrl("jdbc:mysql://" + host + "/" + dbName + "?autoReconnect=true&useSSL=false&characterEncoding = UTF-8&useUnicode=true");
      source.setInitialSize(3);
    }
    return source;
  }
}
