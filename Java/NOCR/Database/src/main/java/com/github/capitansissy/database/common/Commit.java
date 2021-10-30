package com.github.capitansissy.database.common;

import com.mchange.v2.c3p0.AbstractConnectionCustomizer;
import org.jetbrains.annotations.NotNull;

import java.sql.Connection;

public class Commit extends AbstractConnectionCustomizer {
  @Override
  public void onCheckOut(@NotNull Connection c, String parentDataSourceIdentityToken) throws Exception {
    c.setAutoCommit(false);
  }
}
