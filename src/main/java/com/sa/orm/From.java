package com.sa.orm;

import java.sql.SQLSyntaxErrorException;

/**
 * <h4>Description</h4>Represents the <code>FROM</code> clause in an SQL
 * statement.
 * 
 * <h4>Copyrights</h4>Copyrights &copy; 2008 - 2011 Soft Assets.
 * All Rights Reserved.
 * 
 * <h4>Company</h4>Soft Assets
 * <h4>Created</h4>May 05, 2010
 * @author Ali Ahsan
 * @version 1.0
 */
public interface From {
  
  /**
   * Returns string that can be used as <code>FROM</code> clause, except the
   * keyword <code>FROM</code> in an SQL statement.
   * 
   * @return A string that can be used as <code>FROM</code> clause, except the
   * keyword <code>FROM</code>
   */
  public String getFromString() throws SQLSyntaxErrorException;
  
} // end of interface From
