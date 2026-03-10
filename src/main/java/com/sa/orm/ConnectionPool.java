package com.sa.orm;

import java.sql.*;
import java.util.logging.Logger;

import javax.sql.DataSource;

import org.apache.commons.dbcp.BasicDataSource;
import org.apache.commons.dbcp.DbcpException;

import com.sa.orm.util.Utility;

/**
 * <p>This class gets a connection from <code>DataSource</code> object and
 * gives to the calling method. This class also has method to take a connection
 * back. Although this class is not pooling database connection itself, but
 * it is behaving as if it is a database connection pooler.</p>
 * 
 * <p>Copyright (c) Soft Assets 2004 - 2012. All Rights Reserved.</p>
 * <p><h4>Company</h4></p><p>Soft Assets</p>
 * @author Ali Ahsan
 * @created August 07, 2004
 * @version 1.0
 */
public class ConnectionPool extends AbstractPool {

  private static Logger logger = Logger.getLogger(ConnectionPool.class.getName());
  
  private static boolean init = false;

  /**
   * Object which actually is a {@link Connection} pooler.
   */
  private static DataSource ds = null;

  /**
   * Static intializer to intialize {@link javax.sql.DataSource object.
   */
  static {
    try {
      BasicDataSource bds = new BasicDataSource();
      bds.setUsername(ORMInfoManager.DB_USER_NAME);
      bds.setPassword(ORMInfoManager.DB_USER_PASSWORD);
      bds.setDriverClassName(ORMInfoManager.DB_DRIVER_CLASS_NAME);
      bds.setUrl(ORMInfoManager.DB_URL);
      bds.setMaxActive(ORMInfoManager.DB_MAX_ACTIVE);
      bds.setMaxIdle(ORMInfoManager.DB_MAX_IDLE);
      bds.setMaxWait(ORMInfoManager.DB_MAX_WAIT);

      ds = bds;
      logger.fine("Data Source : " + ds);
      logger.config("Success In getting data source");
      init = true;
    } // end of try
    catch(Exception e) {
      logger.warning(Utility.getStackTrace(e));
      init = false;
    } // end of catch

  } // end of static initializer

  /**
   * Does not do anything.
   */
  private ConnectionPool() {
    ;
  } // end of no-argument constructor
  
  private static ConnectionPool poolObj;
  
  public static ConnectionPool getInstance() {
    if(poolObj == null) {
      poolObj = new ConnectionPool();
    } // end of if
    return poolObj;
  } // end of method getInstance
  
  /**
   * Sets the value of <code>init</code> data member.
   * 
   * @param value boolean value to be set as <code>init</code>.
   */
  public void setInit(boolean value) {
    init = value;
  } // end of method setInit

  /**
   * Returns value of <code>init</code> data member.
   * 
   * @return value of <code>init</code>
   */
  public boolean getInit(){
    return init;
  } // end of method getInit

  /**
   * Returns a live database {@link Connection}.
   * 
   * @return A live database {@link Connection}.
   * 
   * @throws SQLException In case database host is not found, database is not
   * found in database server or user credentials are not correct.
   */
  public Connection getConnection()
      throws SQLException {
    Connection con = null;
    try {
    con = ds.getConnection();
    } catch(DbcpException dbcpe) {
      if(dbcpe.getCause() instanceof SQLException) {
        throw (SQLException)dbcpe.getCause();
      }
      throw new SQLException(dbcpe);
    } // end of catch
    return con;
  }    // end of getConnection

  /**
   * Closes (or returns to underlying {@link Connection} pool class) the given
   * database connection.
   * @param con {@link Connection} to be closed (or returned to underlying pool
   * class).
   */
  public void returnConnection(Connection con) {
    try {
      con.close();
    } // end of try
    catch (SQLException se) {
    } // end of catch
  } // end of method returnConnection

}    // end of ConnectionPool class