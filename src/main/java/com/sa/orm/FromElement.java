package com.sa.orm;

import java.sql.SQLSyntaxErrorException;

import com.sa.orm.util.StringUtils;

/**
 * <h4>Description</h4>Represents an element (table) in the <code>FROM</code>
 * clause of an SQL statement.
 * <p>It can be used independently in an SQL statement or can be added to
 * {@link FromClause} class, which in turn can be used in an SQL statement.</p>
 * 
 * <h4>Copyrights</h4>Copyrights &copy; 2008 - 2011 Soft Assets.
 * All Rights Reserved.
 * 
 * <h4>Company</h4>Soft Assets
 * <h4>Created</h4>May 05, 2010
 * @author Ali Ahsan
 * @version 1.0
 */
public class FromElement implements From {
  
  /**
   * Name of the table to be used in <code>FROM</code> clause.
   */
  protected String tableName;
  
  /**
   * Alias of the table to be used in <code>FROM</code> clause.
   * <p>It is ignored if empty</p>
   */
  protected String alias;
  
  public String getTableName() {
    return tableName;
  }

  public void setTableName(String tableName) {
    this.tableName = tableName;
  }

  public String getAlias() {
    return alias;
  }

  public void setAlias(String alias) {
    this.alias = alias;
  }
  
  /**
   * Returns the string that can be used as <code>FROM</code> clause, except
   * <code>FROM</code> keyword, of an SQL statement.
   * 
   * @return String to be used in <code>FROM</code> clause of an SQL statement.
   * 
   * @throws SQLSyntaxErrorException In case it can not generate a structurally
   * valid <code>FROM</code> clause code.
   */
  public String getFromString() throws SQLSyntaxErrorException {
    if(this.tableName == null || this.tableName.trim().length() < 1) {
      throw new SQLSyntaxErrorException("Table name must be specifed. It can not be empty");
    }
    
    StringBuilder fromClause = new StringBuilder(tableName.trim());
    if(StringUtils.getNull(alias) != null) {
      fromClause.append(" ");
      fromClause.append(alias);
    }
    return fromClause.toString();
  }

} // end of class FromElement
