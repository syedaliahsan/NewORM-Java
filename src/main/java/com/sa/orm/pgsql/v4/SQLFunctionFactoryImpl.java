package com.sa.orm.pgsql.v4;

import com.sa.orm.SQLFunction;
import com.sa.orm.SQLFunctionFactory;
import com.sa.orm.util.SQLFieldWrapper;

/**
 * <h4>Description</h4>Provides factory methods to create function
 * objects for MS-Access database.
 * <h4>Copyrights</h4>Copyrights &copy; 2008 - 2012 Soft Assets.
 * All Rights Reserved.
 * <h4>Company</h4>Soft Assets
 * <h4>Created</h4>January 01, 2009
 * @author Ali Ahsan
 * @version 1.0
 */
public class SQLFunctionFactoryImpl extends SQLFunctionFactory {

  /**
   * Does not do anything.
   */
  public SQLFunctionFactoryImpl() {
    super();
  } // end of no-argument constructor
  
  /**
   * {@inheritDoc}
   */
  @Override
  public String getFunctionString(SQLFunction[] functions, boolean includeAlias) {
    if(functions == null || functions.length < 1) {
      return "";
    } // end of if
    StringBuffer sb = new StringBuffer(functions[0].getFunctionString(includeAlias));
    for (int i = 1; i < functions.length; i++) {
      sb.append(", ");
      sb.append(functions[i].getFunctionString(includeAlias));
    } // end of for
    return SQLFieldWrapper.wrapFields(sb.toString(), SQLConstants.FIELD_PREFIX, SQLConstants.FIELD_SUFFIX);
  } // end of method getFunctionString

} // end of class SQLFunctionFactory
