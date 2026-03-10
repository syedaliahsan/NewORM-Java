package com.sa.orm;

import java.sql.Connection;
import java.sql.SQLException;

/**
 * <p>Defines method to give and take back live database connection objects so
 * that other classes requiring connection objects need not to manage them.
 * <p>It also provides a way to let ORM classes use the {@link Connection}s
 * from database pool class of the project in which ORM classes are being
 * used.</p>
 * 
 * <p>Copyright &copy; Soft Assets 2008 - 2012. All Rights Reserved.</p>
 * <p><h4>Company</h4></p><p>Soft Assets</p>
 * @author Ali Ahsan
 * @created November 11, 2008
 * @version 1.0
 */
public abstract class AbstractPool {

  /**
   * Returns a live database {@link Connection} object.
   * 
   * @return A live database {@link Connection} object.
   * 
   * @throws SQLException In case of any errors.
   */
  public abstract Connection getConnection()
      throws SQLException;

  /**
   * Closes (or returns to underlying pool class) the given object.
   * 
   * @param con {@link Connection} object to be closed (or returned to
   * underlying pool class).
   */
  public abstract void returnConnection(Connection con);

} // end of AbstractPool class