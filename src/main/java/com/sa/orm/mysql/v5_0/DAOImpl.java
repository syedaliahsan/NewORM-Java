package com.sa.orm.mysql.v5_0;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Collection;

import com.sa.orm.AbstractDAO;
import com.sa.orm.DbResult;
import com.sa.orm.ORMException;
import com.sa.orm.ORMInfoManager;
import com.sa.orm.SQLCriterion;
import com.sa.orm.SQLCriterion.BOOLEAN_OPERATOR;
import com.sa.orm.bean.PagingVO;
import com.sa.orm.util.DBField;
import com.sa.orm.util.StringUtils;

/**
 * MySQL Database specific implementation of {@link com.sa.orm.DAO}.
 * <h4>Description</h4>
 * Inherits from {@link AbstractDAO} and uses MySQL specific factories.
 * TODO Implement and test methods.
 */
public class DAOImpl extends AbstractDAO {

  /**
   * No argument constructor.
   * Passes MySQL specific implementations of factories to the super class.
   */
  public DAOImpl() {
    super(new SQLCriterionFactoryImpl(), new SQLFunctionFactoryImpl(), new ExceptionUtilImpl(), new SQLConstants());
  }

  /**
   * {@inheritDoc}
   */
  public PagingVO searchPaging(Object pojo, int level, String[] fields,
        SQLCriterion[] criteria, BOOLEAN_OPERATOR booleanOperator, String sortBy,
        int limitStart, int limitSize, Connection con)
    throws ORMException {

    PagingVO pg = new PagingVO();
    pg.setPageFirstRecord(limitStart);
    pg.setRowsPerPage(limitSize);
    pg.setSortBy(sortBy);

    String qry = createSelectQuery(pojo, level, fields, criteria,
        booleanOperator, sortBy, limitStart, limitSize);
    logger.info(qry + ";");
    ArrayList<Object> records = new ArrayList();

    boolean isNew = false;

    Statement stmt = null;
    ResultSet rst = null;
    try {
      if(con == null || con.isClosed()) {
        con = getConnection();
        isNew = true;
      } // end of if
      stmt = con.createStatement();
      rst = stmt.executeQuery(qry);
      Object obj = null;
      while(rst.next()) {
        try {
          obj = ORMInfoManager.instantiate(pojo.getClass().getName());
          fillObject(obj, rst);
          records.add(obj);
        } catch(Exception e){}
      } // end of while
      rst.close();
      if (records.size() > 0) {
        rst = stmt.executeQuery("Select FOUND_ROWS()");
        rst.next();
        pg.setTotalRows(rst.getInt(1));
        logger.finer("Total Records Found : " + pg.getTotalRows());
        rst.close();
      } // end of if
      else {
        pg.setTotalRows(0);
      } // end of if
    } // end of try
    catch(SQLException sqle) {
      throw exceptionUtil.getSelectException(sqle.getMessage(), sqle);
    } // end of catch
    finally {
      try {
        stmt.close();
      } // end of try
      catch(Exception e) {}
      if(isNew) {
        closeConnection(con);
      } // end of if connection was created locally
    } // end of finally
    pg.setResults(records);
    return pg;
  } // end of method searchPaging

  /**
   * Inserts a record in the database corresponding to the given
   * <code>clazz</code> using the field values set in <code>pojo</code>.
   * <p>Primary key, if generated automatically, is set in the given object
   * <code>pojo</code> before returning.</p>
   * 
   * @param clazz {@link Class} to read the fields values from which define the
   * fields whose values are to be inserted in the underlying database.
   * @param pojo Object having the field values to be used to insert a new row
   * in the database table corresponding to given <code>clazz</code>.
   * @param con Connection for transaction control outside this method. If
   * <code>null</code>, a new connection is automatically made.
   * @param insertContainedParentObjects Whether or not to insert the parent
   * objects of the given <code>pojo</code> whose object references are found in
   * it (i.e. they are contained objects) and whose primary key values are not
   * set (i.e. they are zero in case of numeric data types and <code>null</code>
   * in case of objects).
   * @param insertContainedChildObjects Whether or not to insert the child
   * objects of the given <code>pojo</code> whose object references are found in
   * it (i.e. they are contained objects) and whose primary key values are not
   * set (i.e. they are zero in case of numeric data types and <code>null</code>
   * in case of objects).
   * 
   * @return Number of rows inserted. Return number may be greater than one in
   * case super, contained parent and/or contained child objects were also
   * inserted.
   * 
   * @throws ORMException In case of any database or other errors.
   */
  protected <T> DbResult<T> insert(Class clazz, T pojo, Connection con,
      boolean returnAffectedObject, boolean insertContainedParentObjects,
      boolean insertContainedChildObjects) throws ORMException {

    T insertedObj = (T)ORMInfoManager.instantiate(pojo.getClass().getName());
    DbResult<T> resultObj = null;
    Object previousPKValue = ORMInfoManager.getPrimaryKeyValue(pojo);
    Object pkValue = previousPKValue;

    try{
      // contained parent objects
      if(insertContainedParentObjects) {
        insertContainedParentObjects(clazz, pojo, insertedObj, con, insertContainedParentObjects, insertContainedChildObjects);
      } // end of if
      
      // super class objects
      Class superClass = ORMInfoManager.getSuperClass(clazz);
      if(superClass != null && superClass.getName().equals(Object.class.getName()) == false) {
        
        if(ORMInfoManager.isInstanceMemberValueSet(pojo, ORMInfoManager.getPrimaryKeyField(superClass.getName())) == false) {
          logger.finer("About to insert super object of type [" + superClass.getName() + "] for [" + clazz.getName() + "].");
          DbResult<T> insertedParentObj = insert(superClass, pojo, con, true, insertContainedParentObjects, insertContainedChildObjects);
          Object insertedSuperObject = insertedParentObj.getEntities().toArray()[0];
          logger.finer("insertedSuperObject : " + insertedSuperObject.toString());
          Object insertedObjPKValue = ORMInfoManager.getPrimaryKeyValue(superClass, insertedSuperObject);
          DBField pkField = ORMInfoManager.getPrimaryKeyField(pojo.getClass().getName());
          ORMInfoManager.setInstanceMemberValue(pojo, pkField, insertedObjPKValue);
          logger.finer("To be inserted object after setting the super PK: " + pojo.toString());
        }
      } // end of if
      
      logger.finer("About to insert [" + clazz.getName() + "] object with primary key field value [" + pkValue + "].");
      String sql = createInsertQuery(clazz, pojo);
      logger.info(sql + ";");
      int affectedCount = this.executeUpdate(sql, pojo, con);
      if(returnAffectedObject) {
        resultObj = new DbResult<T>(this.executeUpdate(sql, pojo, con));
      }
      else {
        resultObj = new DbResult<T>(affectedCount);
      }
      logger.finer("Inserted [" + clazz.getName() + "] object with primary key field value [" + pkValue + "] successfully.");
            
      // contained Objects
      if(insertContainedChildObjects) {
        insertContainedChildObjects(clazz, pojo, insertedObj, con, insertContainedParentObjects, insertContainedChildObjects);
      }
   
    } catch(SQLException sqle) {
      throw exceptionUtil.getInsertException(sqle.getMessage(), sqle);
    }
    return resultObj;
  } // end of method insertTest

  /**
   * {@inheritDoc}
   */
  public <T> DbResult<T> update(T pojo, String[] fields, SQLCriterion[] criteria,
      BOOLEAN_OPERATOR booleanOperator, Connection con, boolean returnUpdatedObject)
      throws ORMException {
    DbResult<T> resultObj = null;
    try {
      String sql = createUpdateQuery(pojo, fields, criteria, booleanOperator);
      logger.info(sql + ";");
      if(returnUpdatedObject) {
        resultObj = new DbResult<T>((Collection<T>)this.executeQuery(sql, pojo.getClass(), pojo, con));
      }
      else {
        int affectedCount = this.executeUpdate(sql, pojo, con);
        resultObj = new DbResult<T>(affectedCount);
      }
    } // end of try
    catch(SQLException e) {
      throw exceptionUtil.getUpdateException(e.getMessage(), e);
    } // end of catch
    return resultObj;
  } // end of method update
  
  /**
   * {@inheritDoc}
   */
  public <T> DbResult<T> delete(T pojo, Collection ids, Connection con, boolean returnDeletedObject)
      throws ORMException {

    DbResult<T> resultObj = null;
    try {
      String sql = createDeleteQuery(pojo, ids);
      logger.info(sql + ";");
      if(returnDeletedObject) {
        resultObj = new DbResult<T>((Collection<T>)this.executeQuery(sql, pojo.getClass(), pojo, con));
      }
      else {
        int affectedCount = this.executeUpdate(sql, pojo, con);
        resultObj = new DbResult<T>(affectedCount);
      }
    } // end of try
    catch(SQLException e) {
      throw exceptionUtil.getDeleteException(e.getMessage(), e);
    } // end of catch
    return resultObj;
  } // end of method delete

  /**
   * Override this method if the fields (optionally qualified) need to be
   * wrapped in database specific field wrappers.
   * It returns the fields as is.
   * @param fields
   * @return fields without wrapping. 
   */
  @Override
  protected String wrapFields(String fields) {
    return StringUtils.wrapFields(fields, SQLConstants.FIELD_PREFIX, SQLConstants.FIELD_SUFFIX);
  }
  
}
