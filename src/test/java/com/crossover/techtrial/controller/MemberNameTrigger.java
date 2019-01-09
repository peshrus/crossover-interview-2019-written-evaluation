package com.crossover.techtrial.controller;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.h2.tools.TriggerAdapter;

@SuppressWarnings("unused")
public class MemberNameTrigger extends TriggerAdapter {

  @Override
  public void fire(Connection conn, ResultSet oldRow, ResultSet newRow) throws SQLException {
    final String newName = newRow.getString("name");
    final Pattern pattern = Pattern.compile("^([a-zA-Z]+.+){2,100}$");
    final Matcher matcher = pattern.matcher(newName);

    if (!matcher.matches()) {
      throw new SQLException(
          "Name has to have the length of 2 to 100 and should always start with an alphabet");
    }
  }
}
