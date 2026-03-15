package com.sa.orm.pgsql.v4;

import java.sql.*;
import java.text.MessageFormat;
import java.util.*;
import java.util.logging.Logger;

import com.sa.orm.Join;
import com.sa.orm.AbstractDAO;
import com.sa.orm.DbResult;
import com.sa.orm.ORMException;
import com.sa.orm.ORMInfoManager;
import com.sa.orm.SQLCriterion;
import com.sa.orm.SQLCriterion.BOOLEAN_OPERATOR;
import com.sa.orm.bean.PagingVO;
import com.sa.orm.util.DBField;
import com.sa.orm.util.SQLFieldWrapper;
import com.sa.orm.util.StringUtils;

/**
 * PostgreSQL Database specific implementation of DAO.
 */
public class DAOImpl extends AbstractDAO {

  private static Logger logger = Logger.getLogger(DAOImpl.class.getName());

  public DAOImpl() {
    super(new SQLCriterionFactoryImpl(), new SQLFunctionFactoryImpl(), new ExceptionUtilImpl(), new SQLConstants());
  }

  @Override
  public PagingVO searchPaging(Object pojo, int level, String[] fields,
        SQLCriterion[] criteria, BOOLEAN_OPERATOR booleanOperator, String sortBy,
        int limitStart, int limitSize, Connection con)
    throws ORMException {
    return searchPaging(pojo, level, fields, criteria, booleanOperator, sortBy, limitStart, limitSize, null, con);
  }

  @Override
  public PagingVO searchPaging(Object pojo, int level, String[] fields,
        SQLCriterion[] criteria, BOOLEAN_OPERATOR booleanOperator, String sortBy,
        int limitStart, int limitSize, List<Join> joins, Connection con)
    throws ORMException {

    PagingVO pg = new PagingVO();
    pg.setPageFirstRecord(limitStart);
    pg.setRowsPerPage(limitSize);
    pg.setSortBy(sortBy);

    String qry = createSelectQuery(pojo, level, fields, null, criteria,
        booleanOperator, sortBy, -1, 0, joins);
    logger.info(qry + ";");
    
    // For PostgreSQL, we need to modify the query to include window function for total count
    String countQuery = "SELECT COUNT(*) FROM (" +
                       qry.replaceFirst("SELECT\\s+", "SELECT") + 
                       ") AS subquery";
    
    String pagedQuery = qry;
    String limitClause = "";
    if(limitSize > 0) {
      if(limitStart > 1) {
        limitClause = sqlConstants.getQryLimit2().format(new Object[] {"" + (limitStart - 1), "" + limitSize});
      }
      else {
        limitClause = sqlConstants.getQryLimit1().format(new Object[] {"" + limitSize});
      }
    }
    if (limitSize > 0 || limitStart > 1) {
      pagedQuery += " " + limitClause;
    }
    
    logger.info(pagedQuery + ";");
    logger.info(countQuery + ";");
    ArrayList<Object> records = new ArrayList();

    boolean isNew = false;

    Statement stmt = null;
    ResultSet rst = null;
    try {
      if(con == null || con.isClosed()) {
        con = getConnection();
        isNew = true;
      }
      stmt = con.createStatement();
      rst = stmt.executeQuery(pagedQuery);
      Object obj = null;
      while(rst.next()) {
        try {
          obj = pojo.getClass().getDeclaredConstructor().newInstance();
          fillObject(obj, rst);
          records.add(obj);
        } catch(Exception e){}
      }
      rst.close();
      if (limitSize > 0 || limitStart > 1) {
        rst = stmt.executeQuery(countQuery);
        if (rst.next()) {
          pg.setTotalRows(rst.getInt(1));
          logger.finer("Total Records Found : " + pg.getTotalRows());
        }
        rst.close();
      }
      else {
        pg.setTotalRows(0);
      }
    }
    catch(SQLException sqle) {
      throw exceptionUtil.getSelectException(sqle.getMessage(), sqle);
    }
    finally {
      try {
        stmt.close();
      }
      catch(Exception e) {}
      if(isNew) {
        closeConnection(con);
      }
    }
    pg.setResults(records);
    return pg;
  }
  
  /**
   * Creates SQL <code>INSERT</code> statement for the given <code>clazz</code>
   * reading the field values from <code>pojo</code>, through introspection
   * (reflection) on <code>clazz</code> object.
   * <p>All the fields, whether empty or <code>null</code>, but have a
   * corresponding mapping in the database, are included.</p>
   * 
   * @param clazz {@link Class} whose corresponding database row is to be
   * inserted.
   * @param pojo Object to read the field values, for <code>INSERT</code> query,
   * are to be read.
   * 
   * @return SQL <code>INSERT</code> query.
   * 
   * @throws ORMException In case of any database interaction or reflection
   * problems.
   */
  public String createInsertQuery(Class clazz, Object pojo, boolean returnAffectedObject) 
      throws ORMException {
    StringBuffer cols = new StringBuffer("");
    StringBuffer values = new StringBuffer("");
    String entityName = SQLFieldWrapper.wrapFields(ORMInfoManager.getEntityName(clazz.getName()), sqlConstants.getFieldPrefix(), sqlConstants.getFieldSuffix());
    serialize(clazz, pojo, cols, values, null, false);

    MessageFormat sqlStatement = SQLConstants.QRY_INSERT_QUERY;
    if(returnAffectedObject) {
      sqlStatement = SQLConstants.QRY_INSERT_QUERY_WITH_RETURNING;
    }

    return sqlStatement.format(new Object[] {entityName, SQLFieldWrapper.wrapFields(cols.toString().trim(), sqlConstants.getFieldPrefix(), sqlConstants.getFieldSuffix()), values.toString().trim()}).trim();
  } // end of method createInsertQuery

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

    boolean isNewConnection = false;
    T insertedObj = (T)ORMInfoManager.instantiate(pojo.getClass().getName());
    DbResult<T> resultObj = null;
    Object previousPKValue = ORMInfoManager.getPrimaryKeyValue(pojo);
    Object pkValue = previousPKValue;

    try {
      if(con == null || con.isClosed()) {
        con = getConnection();
        con.setAutoCommit(false);
        isNewConnection = true;
      }
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
          Object insertedObjPKValue = ORMInfoManager.getPrimaryKeyValue(insertedSuperObject);
          DBField pkField = ORMInfoManager.getPrimaryKeyField(pojo.getClass().getName());
          ORMInfoManager.setInstanceMemberValue(pojo, pkField, insertedObjPKValue);
          logger.finer("To be inserted object after setting the super PK: " + pojo.toString());
        }
      } // end of if
      
      logger.finer("About to insert [" + clazz.getName() + "] object with primary key field value [" + pkValue + "].");
      String sql = createInsertQuery(clazz, pojo, returnAffectedObject);
      logger.info(sql + ";");
      if(returnAffectedObject || insertContainedChildObjects) {
        resultObj = new DbResult<T>((Collection<T>)this.executeQuery(sql, clazz, pojo, con));
        logger.info("ResultObject has " + resultObj.getEntities().size() + " records");
        insertedObj = resultObj.hasEntities() ? (T)resultObj.getEntities().toArray()[0] : insertedObj;
        pkValue = ORMInfoManager.getPrimaryKeyValue(insertedObj);
      }
      else {
        int affectedCount = this.executeUpdate(sql, pojo, con);
        resultObj = new DbResult<T>(affectedCount);
      }
      logger.finer("Inserted [" + clazz.getName() + "] object with primary key field value [" + pkValue + "] successfully.");
            
      // contained Objects
      if(insertContainedChildObjects) {
        insertContainedChildObjects(clazz, pojo, insertedObj, con, insertContainedParentObjects, insertContainedChildObjects);
      }
   
      if(isNewConnection) {
        try {
          con.commit();
        } catch(Exception ee) {}
      }
    }
    catch(SQLException sqle) {
      if(isNewConnection) {
        try {
          con.rollback();
        } catch(Exception ee) {}
      }
      throw exceptionUtil.getInsertException(sqle.getMessage(), sqle);
    }
    finally {
      if(isNewConnection) {
        try {
          con.close();
        } catch(Exception ee) {}
      }
    }
    return resultObj;
  } // end of method insertTest

  public String createUpdateQuery(Object pojo, boolean returnUpdatedObject) {
    String[] fields = StringUtils.splitString(ORMInfoManager.getPlainDBFieldNames(pojo), sqlConstants.getFieldsSeparator());
    SQLCriterion[] criteria = new SQLCriterion[1];
    Object pkValue = ORMInfoManager.getPrimaryKeyValue(pojo);
    criteria[0] = criteronFactory.createEqualTo(ORMInfoManager.getPrimaryKeyField(pojo).getDbFieldName(), pkValue);
    
    return createUpdateQuery(pojo, fields, criteria, SQLCriterion.BOOLEAN_OPERATOR.AND, returnUpdatedObject);
  } // end of method createUpdateQuery
  
  /**
   * {@inheritDoc}
   */
  public String createUpdateQuery(Object pojo, String[] fields,
      SQLCriterion[] criteria, BOOLEAN_OPERATOR booleanOperator,
      boolean returnUpdatedObject) {
    Vector colsVec = null;
    if(fields == null || fields.length < 1) {
      colsVec = new Vector();
    } // end of if
    else {
      colsVec = new Vector(Arrays.asList(fields));
    } // end of else
    Vector valuesVec = new Vector();
    extractValues(pojo, colsVec, valuesVec, true);
    String pkFieldName = ORMInfoManager.getPrimaryKeyName(pojo);
    StringBuffer str = new StringBuffer("");
    for (int i = 0; i < colsVec.size(); i++) {
      if(pkFieldName.equals(colsVec.elementAt(i))) continue; // skip the pk in SET ... clause
      if(str.length() > 0) {
        str.append(sqlConstants.getFieldsSeparator());
      } // end of if
      str.append(StringUtils.wrapDBField((String)colsVec.elementAt(i), SQLConstants.FIELD_PREFIX));
      str.append(sqlConstants.getOpEqual());
      str.append(valuesVec.elementAt(i));
    } // end of for

    String whereClause = criteronFactory.createCriteriaString(criteria, booleanOperator);
    if(StringUtils.getNull(whereClause) != null) {
      whereClause = sqlConstants.getQryWhere().format(new Object[] {whereClause});
    } // end of if
    MessageFormat sqlStatement = SQLConstants.QRY_UPDATE_QUERY;
    if(returnUpdatedObject) {
      sqlStatement = SQLConstants.QRY_UPDATE_QUERY_WITH_RETURNING;
    }
    String wrappedEntityName = SQLFieldWrapper.wrapFields(ORMInfoManager.getEntityName(pojo), SQLConstants.FIELD_PREFIX, SQLConstants.FIELD_PREFIX);
    return sqlStatement.format(new Object[] {wrappedEntityName, str.toString().trim(), whereClause.trim()}).trim();
  } // end of method createUpdateQuery
  
  /**
   * {@inheritDoc}
   */
  public <T> DbResult<T> update(T pojo, String[] fields, SQLCriterion[] criteria,
      BOOLEAN_OPERATOR booleanOperator, Connection con, boolean returnUpdatedObject)
      throws ORMException {
    DbResult<T> resultObj = null;
    try {
      String sql = createUpdateQuery(pojo, fields, criteria, booleanOperator, returnUpdatedObject);
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
  
  public String createDeleteQuery(Object pojo, boolean returnDeletedObjects) {
    SQLCriterion[] criteria = new SQLCriterion[1];
    criteria[0] = criteronFactory.createEqualTo(ORMInfoManager.getPrimaryKeyField(pojo).getDbFieldName(), null, ORMInfoManager.getPrimaryKeyValue(pojo));
    return createDeleteQuery(pojo, criteria, SQLCriterion.BOOLEAN_OPERATOR.AND, returnDeletedObjects);
  } // end of method createDeleteQuery
  
  public String createDeleteQuery(Object pojo, Collection ids, boolean returnDeletedObjects) {
    SQLCriterion[] criteria = new SQLCriterion[1];
    Object pkValue = ids != null && ids.size() > 0 ? ids.toArray()[0] : null;
    criteria[0] = criteronFactory.createIn(ORMInfoManager.getQualifiedPrimaryKey(pojo), null, ids, criteronFactory.getType(pkValue), null, null);
    return createDeleteQuery(pojo, criteria, SQLCriterion.BOOLEAN_OPERATOR.AND, returnDeletedObjects);
  } // end of method createDeleteQuery
  
  public String createDeleteQuery(Object pojo, SQLCriterion[] criteria,
      BOOLEAN_OPERATOR booleanOperator, boolean returnDeletedObjects) {
    String criteriaStr = "";
    if(criteria == null || criteria.length < 1) throw new RuntimeException("Criteria must be given for DELETE SQL statement.");
    criteriaStr = criteronFactory.createCriteriaString(criteria, booleanOperator);
    if(StringUtils.getNull(criteriaStr) != null) {
      criteriaStr = sqlConstants.getQryWhere().format(new Object[] {criteriaStr});
    } // end of if
    MessageFormat sqlStatement = SQLConstants.QRY_DELETE_QUERY;
    if(returnDeletedObjects) {
      sqlStatement = SQLConstants.QRY_DELETE_QUERY_WITH_RETURNING;
    }

    return sqlStatement.format(new Object[] {SQLConstants.QRY_DELETE.format(new Object[] {wrapFields(ORMInfoManager.getEntityName(pojo))}), criteriaStr}).trim();
  } // end of method createDeleteQuery
  
  /**
   * {@inheritDoc}
   */
  public <T> DbResult<T> delete(T pojo, Collection ids, Connection con, boolean returnDeletedObject)
      throws ORMException {

    DbResult<T> resultObj = null;
    try {
      String sql = createDeleteQuery(pojo, ids, returnDeletedObject);
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
    return SQLFieldWrapper.wrapFields(fields, SQLConstants.FIELD_PREFIX, SQLConstants.FIELD_SUFFIX);
  }
  
  protected String wrapSortBy(String orderByClause) {
    if(orderByClause == null || orderByClause.trim().length() < 1) return orderByClause;

    List<String> wrappedOrderByComponents = new ArrayList<String>();
    String [] orderByComponents = orderByClause.split(",");
    for (int i = 0; i < orderByComponents.length; i++) {
      String[] fieldAndDir = orderByComponents[i].trim().split(" ");
      String sortDirection = (fieldAndDir.length > 1 && isValidSortDirection(fieldAndDir[1]) ?  " " + fieldAndDir[1].trim() : "");
      wrappedOrderByComponents.add(wrapFields(fieldAndDir[0]) + sortDirection);
    }
    return String.join(",", wrappedOrderByComponents);
  }
  
  private static boolean isValidSortDirection(String sortDirection) {
    if(sortDirection == null) return false;
    sortDirection = sortDirection.trim();
    if(sortDirection.length() < 1 || "ASC".equalsIgnoreCase(sortDirection) || "DESC".equalsIgnoreCase(sortDirection))
      return true;
    return false;
  }

}
