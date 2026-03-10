package com.sa.orm;

import java.sql.SQLSyntaxErrorException;
import java.util.List;

/**
 * <h4>Description</h4>Class to hold details of the <code>FROM</code> clause in
 * an SQL statement.
 * <p>It allows the developer to specify custom <code>FROM</code> clauses when
 * there are special relationships between the tables or when more than one
 * aliases are needed for a table.</p>
 * 
 * <h4>Copyrights</h4>Copyrights &copy; 2008 - 2011 Soft Assets.
 * All Rights Reserved.
 * 
 * <h4>Company</h4>Soft Assets
 * <h4>Created</h4>May 05, 2010
 * @author Ali Ahsan
 * @version 1.0
 */
public class FromClause implements From {
  
  /**
   * List of <code>FROM</code> elements to be included the <code>FROM</code>
   * clause separated by comma.
   */
  protected List<? extends From> fromElements;
  
  /**
   * Returns a string which is comma separated strings generated from each of
   * {@link From} objects in the {@link #fromElements}.
   * 
   * @return A string that can be used as <code>FROM</code> clause, except the
   * keyword <code>FROM</code>
   */
  public String getFromString() throws SQLSyntaxErrorException {
    if(fromElements == null || fromElements.size() < 1) {
      throw new SQLSyntaxErrorException("At least one FromElement shall be provided.");
    }
    StringBuilder fromClause = new StringBuilder();
    for (From fromObj : fromElements) {
      if(fromClause.length() > 0) {
        fromClause.append(", ");
      }
      fromClause.append(fromObj.getFromString());
    }
    
    return fromClause.toString();
  } // end of method getFromString
  
} // end of class FromClause
