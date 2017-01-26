package com.clouway.nvuapp.adapter.persistence.dao;

import java.sql.ResultSet;
import java.sql.SQLException;


/**
 * @author Vasil Mitov <v.mitov.clouway@gmail.com>
 */
public interface RowFetcher<T> {
  T fetchRow(ResultSet resultSet) throws SQLException;
}
