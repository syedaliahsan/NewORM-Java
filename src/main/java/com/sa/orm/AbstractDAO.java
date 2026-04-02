package com.sa.orm;

import java.sql.*;
import java.util.*;
import java.util.logging.Logger;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

import com.sa.orm.bean.PagingVO;
import com.sa.orm.bean.SearchRequest;
import com.sa.orm.reflect.annotation.AnnotationConstants;
import com.sa.orm.util.*;
import com.sa.orm.SQLCriterion.BOOLEAN_OPERATOR;

/**
 * <h4>Description</h4>MySQL Database specific implementation of
 * {@link DAOInterface}. It has some constants as well.
 * <h4>Copyrights</h4>Copyrights &copy; 2008 - 2012 Soft Assets.
 * All Rights Reserved.
 * <h4>Company</h4>Soft Assets
 * <h4>Created</h4>October 27, 2010
 * @author Ali Ahsan
 * @version 1.1
 */
public abstract class AbstractDAO implements DAO {

  /**
   * Holds next id for each table.
   */
  /**
   * Holds next id for each table.
   */
  protected static Map<String, Integer> ids = new TreeMap<String, Integer>();
  
  /**
   * Database pool class object to get database connections from.
   */
  protected static final AbstractPool pool;
  
  /**
   * Database specific factory class to create {@link SQLCriterion} objects.
   */
  protected SQLCriterionFactory criteronFactory;
  
  /**
   * Database specific factory class to create {@link SQLFunction} objects to
   * be used in <code>Group By</code> clause of the query.
   */
  protected SQLFunctionFactory functionFactory;
  
  protected ExceptionUtil exceptionUtil;
  
  /**
   * Database specific constants class to get SQL constants while making
   * queries.
   */
  protected SQLConstants sqlConstants;
  
  /**
   * Logger object to log messages.
   */
  protected static Logger logger = Logger.getLogger(AbstractDAO.class.getName());
  
  /**
   * Static initializer to loads constant values from properties file.
   */
  static {
    try {
      if(StringUtils.getNull(ORMInfoManager.connectionPoolClassName) == null) {
        pool = ConnectionPool.getInstance();
      } // end of if
      else {
        Class poolClass = Class.forName(ORMInfoManager.connectionPoolClassName);
        pool = (AbstractPool)poolClass.getMethod("getInstance", new Class[0]).invoke(poolClass, new Object[0]);
      }
    } // end of try
    catch(Exception e) {
      logger.warning(Utility.getStackTrace(e));
      throw new RuntimeException(e.getMessage(), e);
    } // end of catch
  } // end of static initializer

  /**
   * No arugment constructor does not do anything
   */
  public AbstractDAO(SQLCriterionFactory criterionFactoryImpl,
      SQLFunctionFactory functionFactoryImpl, ExceptionUtil exceptionUtilImpl,
      SQLConstants sqlConstants) {
    this.criteronFactory = criterionFactoryImpl;
    this.functionFactory = functionFactoryImpl;
    this.exceptionUtil = exceptionUtilImpl;
    this.sqlConstants = sqlConstants;
  } // end of no-argument constructor

  /**
   * Returns a filled object of the type of the given <code>pojo</code> object
   * based on the value of primary key set in it.
   * 
   * @param pojo Object having primary key value set and whose clone filled with
   * the database record is to be returned.
   * 
   * @return Object of the given type, filled with a database record, found
   * based on the value of the primary key set in the given object.
   * 
   * @throws ORMException In case no record is found or any other database
   * related errors.
   */
  public <T> T getById(T pojo)
      throws ORMException {
    return getById(pojo, (Connection)null);
  } // end of method get

  /**
   * Returns a filled object of the type of the given <code>pojo</code> object
   * based on the value of primary key set in it.
   * 
   * @param pojo Object having primary key value set and whose clone filled with
   * the database record is to be returned.
   * @param con Connection object to be used to run the query. If
   * <code>null</code>, a new connection is created internally.
   * 
   * @return Object of the given type, filled with a database record, found
   * based on the value of the primary key set in the given object.
   * 
   * @throws ORMException In case no record is found or any other database
   * related errors.
   */
  public <T> T getById(T pojo, Connection con)
      throws ORMException {
    Object pkValue = ORMInfoManager.getPrimaryKeyValue(pojo);
    if (pkValue == null || (pkValue.getClass() == Integer.class && ((Integer)pkValue).intValue() < 1) || (pkValue.getClass() == String.class && ((String)pkValue).trim().equals(""))) {
      throw new ORMException("Instance member " + ORMInfoManager.getPrimaryKeyName(pojo) + " must be set in order to get the object.");
    }
    T newObj = (T)ORMInfoManager.instantiate(pojo.getClass().getName());
    ORMInfoManager.setPrimaryKeyValue(newObj, pkValue);
    return fillObject(newObj, con);
  } // end of method get

  /**
   * Returns a filled object of the type of the given <code>pojo</code> object
   * based on the value of primary key set in it.
   * 
   * @param pojo Object having primary key value set and whose clone filled with
   * the database record is to be returned.
   * @param pkValue Value of the primary key field.
   * 
   * @return Object of the given type, filled with a database record, found
   * based on the value of the primary key set in the given object.
   * 
   * @throws ORMException In case no record is found or any other database
   * related errors.
   */
  public <T> T getById(T pojo, Object pkValue)
      throws ORMException {
    return getById(pojo, pkValue, null);
  } // end of method get

  /**
   * Returns a filled object of the type of the given <code>pojo</code> object
   * based on the value of primary key set in it.
   * 
   * @param pojo Object having primary key value set and whose clone filled with
   * the database record is to be returned.
   * @param pkValue Value of the primary key field.
   * @param con Connection object to be used to run the query. If
   * <code>null</code>, a new connection is created internally.
   * 
   * @return Object of the given type, filled with a database record, found
   * based on the value of the primary key set in the given object.
   * 
   * @throws ORMException In case no record is found or any other database
   * related errors.
   */
  public <T> T getById(T pojo, Object pkValue, Connection con)
      throws ORMException {
    T newObj = (T)ORMInfoManager.instantiate(pojo.getClass().getName());
    ORMInfoManager.setPrimaryKeyValue(newObj, pkValue);
    return fillObject(newObj, con);
  } // end of method get

  /**
   * Returns a filled object of the type of the given <code>pojo</code> object
   * based on the value of primary key set in it.
   * 
   * @param pojo Object having primary key value set and whose clone filled with
   * the database record is to be returned.
   * @param fields Fields to be populated in the returned object.
   * 
   * @return Object of the given type, filled with a database record, found
   * based on the value of the primary key set in the given object.
   * 
   * @throws ORMException In case no record is found or any other database
   * related errors.
   */
  public <T> T getById(T pojo, String[] fields)
      throws ORMException {
    SQLCriterion[] criteria = new SQLCriterion[1];
    criteria[0] = criteronFactory.createEqualTo(ORMInfoManager.getQualifiedPrimaryKey(pojo), null, 
        ORMInfoManager.getPrimaryKeyValue(pojo), SQLCriterionFactory.INT);
    
    return getById(pojo, fields, null);
  } // end of method get

  /**
   * Returns a filled object of the type of the given <code>pojo</code> object
   * based on the value of primary key set in it.
   * 
   * @param pojo Object having primary key value set and whose clone filled with
   * the database record is to be returned.
   * @param fields Fields to be populated in the returned object.
   * @param con Connection object to be used to run the query. If
   * <code>null</code>, a new connection is created internally.
   * 
   * @return Object of the given type, filled with a database record, found
   * based on the value of the primary key set in the given object.
   * 
   * @throws ORMException In case no record is found or any other database
   * related errors.
   */
  public <T> T getById(T pojo, String[] fields, Connection con)
      throws ORMException {
    SQLCriterion[] criteria = new SQLCriterion[1];
    criteria[0] = criteronFactory.createEqualTo(ORMInfoManager.getQualifiedPrimaryKey(pojo), null, 
        ORMInfoManager.getPrimaryKeyValue(pojo), SQLCriterionFactory.INT);
    
    return search(pojo, fields, criteria, BOOLEAN_OPERATOR.AND, null, 0, 1, con).iterator().next();
  } // end of method get

  /**
   * Searches the table, represented by given <code>pojo</code> object against
   * the given <code>criteria</code>, using the <code>AND</code> operator
   * between the <code>criteria</code>, and returns records, in the form of
   * representative objects in a {@link Collection} object.
   * <p>If <code>fields</code> is <code>null</code> or zero-length, all the
   * table fields are retrieved and filled in the returned objects. Otherwise,
   * only the given fields are retrieved and set in the returned objects.</p>
   * 
   * @param pojo Object representing a database table which is to be searched.
   * @param fields Fields to be retrieved from database and their values to be
   * set in the returned objects.
   * @param criteria Criteria to make <code>Where</code> clause of the database
   * query to search specific results. If <code>null</code>, all the table
   * records are searched.
   * 
   * @return Collection having objects of type <code>pojo</code> which matched
   * the given <code>criteria</code> with given or all field values filled.
   * 
   * @throws ORMException In case of any database or other errors.
   */
  public <T> Collection<T> search(T pojo, String[] fields,
      SQLCriterion[] criteria)
      throws ORMException {
    return search(pojo, fields, criteria, BOOLEAN_OPERATOR.AND);
  }

  /**
   * Searches the table, represented by given <code>pojo</code> object against
   * the given <code>criteria</code> and returns records, in the form of
   * representative objects in a {@link Collection} object.
   * <p>If <code>fields</code> is <code>null</code> or zero-length, all the
   * table fields are retrieved and filled in the returned objects. Otherwise,
   * only the given fields are retrieved and set.</p>
   * 
   * @param pojo Object representing a database table which is to be searched.
   * @param fields Fields to be retrieved from database and their values to be
   * set in the returned objects.
   * @param criteria Criteria to make <code>Where</code> clause of the database
   * query to search specific results. If <code>null</code>, all the table
   * records are searched.
   * @param booleanOperator Database <code>AND</code> or <code>OR</code> operator to be
   * used between the criteria.
   * 
   * @return Collection having objects of type <code>pojo</code> which matched
   * the given <code>criteria</code> with given or all field values filled.
   * 
   * @throws ORMException In case of any database or other errors.
   */
  public <T> Collection<T> search(T pojo, String[] fields,
      SQLCriterion[] criteria, BOOLEAN_OPERATOR booleanOperator)
      throws ORMException {
    return search(pojo, fields, criteria, booleanOperator, null);
  }

  /**
   * Searches the table, represented by given <code>pojo</code> object against
   * the given <code>criteria</code> and returns records, in the form of
   * representative objects in a {@link Collection} object.
   * <p>If <code>fields</code> is <code>null</code> or zero-length, all the
   * table fields are retrieved and filled in the returned objects. Otherwise,
   * only the given fields are retrieved and set.</p>
   * 
   * @param pojo Object representing a database table which is to be searched.
   * @param fields Fields to be retrieved from database and their values to be
   * set in the returned objects.
   * @param criteria Criteria to make <code>Where</code> clause of the database
   * query to search specific results. If <code>null</code>, all the table
   * records are searched.
   * @param booleanOperator Database <code>AND</code> or <code>OR</code> operator to be
   * used between the criteria.
   * @param sortBy Field(s) to sort the returned records with.
   * 
   * @return Collection having objects of type <code>pojo</code> which matched
   * the given <code>criteria</code> with given or all field values filled.
   * 
   * @throws ORMException In case of any database or other errors.
   */
  public <T> Collection<T> search(T pojo, String[] fields,
      SQLCriterion[] criteria, BOOLEAN_OPERATOR booleanOperator, String sortBy)
      throws ORMException {
    return search(pojo, fields, criteria, booleanOperator, sortBy, 0, 0);
  }

  /**
   * Searches the table, represented by given <code>pojo</code> object against
   * the given <code>criteria</code> and returns records, in the form of
   * representative objects in a {@link Collection} object.
   * <p>If <code>fields</code> is <code>null</code> or zero-length, all the
   * table fields are retrieved and filled in the returned objects. Otherwise,
   * only the given fields are retrieved and set.</p>
   * 
   * @param pojo Object representing a database table which is to be searched.
   * @param fields Fields to be retrieved from database and their values to be
   * set in the returned objects.
   * @param criteria Criteria to make <code>Where</code> clause of the database
   * query to search specific results. If <code>null</code>, all the table
   * records are searched.
   * @param booleanOperator Database <code>AND</code> or <code>OR</code> operator to be
   * used between the criteria.
   * @param sortBy Field(s) to sort the returned records with.
   * @param limitStart First record to be fetched out of the matched records. If
   * less than 1, it is ignored.
   * @param limitSize Number of records to be returned, optionally starting from
   * <code>limitStart</code>.
   * 
   * @return Collection having objects of type <code>pojo</code> which matched
   * the given <code>criteria</code> with given or all field values filled.
   * 
   * @throws ORMException In case of any database or other errors.
   */
  public <T> Collection<T> search(T pojo, String[] fields,
      SQLCriterion[] criteria, BOOLEAN_OPERATOR booleanOperator, String sortBy, 
      int limitStart, int limitSize)
      throws ORMException {
    return search(pojo, fields, criteria, booleanOperator, sortBy, limitStart, limitSize, null);
  }

  /**
   * Searches the table, represented by given <code>pojo</code> object against
   * the given <code>criteria</code> and returns records, in the form of
   * representative objects in a {@link Collection} object.
   * <p>If <code>fields</code> is <code>null</code> or zero-length, all the
   * table fields are retrieved and filled in the returned objects. Otherwise,
   * only the given fields are retrieved and set.</p>
   * 
   * @param pojo Object representing a database table which is to be searched.
   * @param fields Fields to be retrieved from database and their values to be
   * set in the returned objects.
   * @param criteria Criteria to make <code>Where</code> clause of the database
   * query to search specific results. If <code>null</code>, all the table
   * records are searched.
   * @param booleanOperator Database <code>AND</code> or <code>OR</code> operator to be
   * used between the criteria.
   * @param sortBy Field(s) to sort the returned records with.
   * @param limitStart First record to be fetched out of the matched records. If
   * less than 1, it is ignored.
   * @param limitSize Number of records to be returned, optionally starting from
   * <code>limitStart</code>.
   * @param con Connection object to be used to run the query. If
   * <code>null</code>, a new connection is created internally.
   * 
   * @return Collection having objects of type <code>pojo</code> which matched
   * the given <code>criteria</code> with given or all field values filled.
   * 
   * @throws ORMException In case of any database or other errors.
   */
  public <T> Collection<T> search(T pojo, String[] fields,
      SQLCriterion[] criteria, BOOLEAN_OPERATOR booleanOperator, String sortBy, 
      int limitStart, int limitSize, Connection con)
      throws ORMException {
    return search(pojo, fields, criteria, booleanOperator, sortBy, limitStart, limitSize, null, con);
  }

  /**
   * Searches the table, represented by given <code>pojo</code> object against
   * the given <code>criteria</code> and returns records, in the form of
   * representative objects in a {@link Collection} object.
   * <p>If <code>fields</code> is <code>null</code> or zero-length, all the
   * table fields are retrieved and filled in the returned objects. Otherwise,
   * only the given fields are retrieved and set.</p>
   * 
   * @param pojo Object representing a database table which is to be searched.
   * @param fields Fields to be retrieved from database and their values to be
   * set in the returned objects.
   * @param criteria Criteria to make <code>Where</code> clause of the database
   * query to search specific results. If <code>null</code>, all the table
   * records are searched.
   * @param booleanOperator Database <code>AND</code> or <code>OR</code> operator to be
   * used between the criteria.
   * @param sortBy Field(s) to sort the returned records with.
   * @param limitStart First record to be fetched out of the matched records. If
   * less than 1, it is ignored.
   * @param limitSize Number of records to be returned, optionally starting from
   * <code>limitStart</code>.
   * @param joins List of {@link Join} objects to be used in the query.
   * @param con Connection object to be used to run the query. If
   * <code>null</code>, a new connection is created internally.
   * 
   * @return Collection having objects of type <code>pojo</code> which matched
   * the given <code>criteria</code> with given or all field values filled.
   * 
   * @throws ORMException In case of any database or other errors.
   */
  public <T> Collection<T> search(T pojo, String[] fields,
      SQLCriterion[] criteria, BOOLEAN_OPERATOR booleanOperator, String sortBy, 
      int limitStart, int limitSize, List<Join> joins, Connection con)
      throws ORMException {
    logger.finest("Entering - Time : " + System.currentTimeMillis());

    String qry = createSelectQuery(pojo, ORMInfoManager.SUPER_LEVEL, fields, null, criteria, booleanOperator, sortBy, limitStart, limitSize, joins);
    logger.finest("Select query built - Time : " + System.currentTimeMillis());
    
    logger.info(qry + ";");
    Vector<T> records = new Vector<T>();

    boolean isNewConnection = false;

    Statement stmt = null;
    ResultSet rst = null;
    try {
      if(con == null || con.isClosed()) {
        con = getConnection();
        isNewConnection = true;
      } // end of if
      stmt = con.createStatement();
      rst = stmt.executeQuery(qry);
      Object obj = null;
      while(rst.next()) {
        try {
          obj = ORMInfoManager.instantiate(pojo.getClass().getName());
          fillObject(obj, rst);
          records.addElement((T)obj);
        } catch(Exception e) {}
      } // end of while
    } // end of try
    catch(SQLException sqle) {
      throw this.exceptionUtil.getSelectException(sqle.getMessage(), sqle);
    } // end of catch
    finally {
      try {
        stmt.close();
      } // end of try
      catch(Exception e) {}
      if(isNewConnection) {
        closeConnection(con);
      } // end of if connection was created locally
    } // end of finally

    logger.finest("Leaving - Time : " + System.currentTimeMillis());
    return records;
  } // end of method search
  
  /**
   * {@inheritDoc}
   */
  public Collection searchGroupBy(Object pojo, String[] fields,
      SQLFunction[] functions, SQLCriterion[] criteria, BOOLEAN_OPERATOR booleanOperator,
      String sortBy, Connection con)
    throws ORMException {
    
    return searchGroupBy(pojo, fields, functions, criteria, booleanOperator,
        sortBy, -1, -1, con);
  } // end of method searchGroupBy

  /**
   * {@inheritDoc}
   */
  public Collection searchGroupBy(Object pojo, String[] fields,
      SQLFunction[] functions, SQLCriterion[] criteria, BOOLEAN_OPERATOR booleanOperator,
      String sortBy, List<Join> joins, Connection con)
    throws ORMException {
    
    return searchGroupBy(pojo, fields, functions, criteria, booleanOperator,
        sortBy, -1, -1, joins, con);
  } 

  /**
   * {@inheritDoc}
   */
  public Collection searchGroupBy(Object pojo, String[] fields,
      SQLFunction[] functions, SQLCriterion[] criteria, BOOLEAN_OPERATOR booleanOperator,
      String sortBy, int limitStart, int limitSize, Connection con)
    throws ORMException {
      return searchGroupBy(pojo, fields, functions, criteria, booleanOperator, sortBy, limitStart, limitSize, null, con);
  }

  /**
   * {@inheritDoc}
   */
  public Collection searchGroupBy(Object pojo, String[] fields,
      SQLFunction[] functions, SQLCriterion[] criteria, BOOLEAN_OPERATOR booleanOperator,
      String sortBy, int limitStart, int limitSize, List<Join> joins, Connection con)
    throws ORMException {

    long currentMillis = 0L;
    currentMillis = System.currentTimeMillis();
    
    String qry = createGroupByQuery(pojo, fields, functions,
        criteria, booleanOperator, sortBy, limitStart, limitSize, joins);

    logger.finest("Group by query built in " + (System.currentTimeMillis() - currentMillis) + " milliseconds"); 
    
    logger.info(qry + ";");
    
    currentMillis = System.currentTimeMillis();

    Collection<Object[]> result = executeSelectQuery(qry, con);

    logger.finest("Group by query executed and data populated as Collection of arrays of strings in " + (System.currentTimeMillis() - currentMillis) + " milliseconds"); 

    return result;
  } // end of method searchGroupBy

  /**
   * {@inheritDoc}
   */
  public PagingVO searchPaging(Object pojo, String[] fields,
        SQLCriterion[] criteria, BOOLEAN_OPERATOR booleanOperator, String sortBy,
        int limitStart, int limitSize, Connection con)
    throws ORMException {
    
    return searchPaging(pojo, ORMInfoManager.SUPER_LEVEL, fields, criteria, booleanOperator, sortBy, limitStart, limitSize, con);
  }
  
  /**
   * {@inheritDoc}
   */
  public PagingVO searchPaging(Object pojo, String[] fields,
        SQLCriterion[] criteria, BOOLEAN_OPERATOR booleanOperator, String sortBy,
        int limitStart, int limitSize, List<Join> joins, Connection con)
    throws ORMException {
    
    return searchPaging(pojo, ORMInfoManager.SUPER_LEVEL, fields, criteria, booleanOperator, sortBy, limitStart, limitSize, joins, con);
  }

  /**
   * {@inheritDoc}
   */
  public PagingVO searchPaging(Object pojo, int level, String[] fields,
        SQLCriterion[] criteria, BOOLEAN_OPERATOR booleanOperator, String sortBy,
        int limitStart, int limitSize, Connection con)
    throws ORMException {
    throw new RuntimeException("Must be implemented by the database specific impl class");
  } 

  /**
   * {@inheritDoc}
   */
  public PagingVO searchPaging(Object pojo, int level, String[] fields,
        SQLCriterion[] criteria, BOOLEAN_OPERATOR booleanOperator, String sortBy,
        int limitStart, int limitSize, List<Join> joins, Connection con)
    throws ORMException {
    throw new RuntimeException("Must be implemented by the database specific impl class");
  } // end of method searchPaging

  public PagingVO genericSearch(SearchRequest searchRequest, Object pojo)
      throws ORMException,  IllegalAccessException, InstantiationException,
      InvocationTargetException, NoSuchMethodException {
    // 1. Build Criteria using the new utility
    SQLCriterion[] criteria = SearchUtil.buildCriteria(searchRequest, pojo);

    // 2. Handle Columns
    String[] fields = null;
    if (searchRequest.getColumns() != null && !searchRequest.getColumns().isEmpty()) {
      fields = searchRequest.getColumns().toArray(new String[0]);
      fields = StringUtils.wrapFields(fields, sqlConstants.getFieldPrefix(), sqlConstants.getFieldSuffix());
    }

    // 3. Handle Pagination limits
    int limitSize = searchRequest.getPageSize();
    // Convert pageNumber to limitStart (offset + 1 logic depending on your DAO implementation)
    // AbstractDAO.searchPaging expects limitStart as record index (1-based or 0-based check DAO).
    // Based on provided AbstractDAO: limitStart is "First record to be fetched".
    int limitStart = ((searchRequest.getPageNumber() - 1) * limitSize) + 1;

    if (searchRequest.isIgnoreLimit()) {
      limitSize = -1;
      limitStart = -1;
    }

    // 4. Handle Sorting
    String sortBy = searchRequest.getOrderBy(); 
    // Note: You might need to qualify sortBy with table name if ambiguous, 
    // or check if sortOrderAscending needs to append " ASC" or " DESC".
    if (!searchRequest.isSortOrderAscending()) {
      sortBy += " DESC";
    }

    // 5. Execute Search via PersistenceManager (which delegates to AbstractDAO)
    // We use AND as the root operator for the criteria array, 
    // though our SearchUtil usually returns a single Group node anyway.
    return searchPaging(
        pojo,
        fields,
        criteria,
        BOOLEAN_OPERATOR.AND, 
        sortBy,
        limitStart,
        limitSize,
        null // Connection (null creates new)
    );
  }

  public PagingVO genericSearch(SearchRequest searchRequest, Object pojo,
      List<Join> joins) throws ORMException,  IllegalAccessException,
  InstantiationException, InvocationTargetException, NoSuchMethodException {

    // 1. Build Criteria using the new utility
    SQLCriterion[] criteria = SearchUtil.buildCriteria(searchRequest, pojo);

    // 2. Handle Columns
    String[] fields = null;
    if (searchRequest.getColumns() != null && !searchRequest.getColumns().isEmpty()) {
      fields = searchRequest.getColumns().toArray(new String[0]);
      fields = StringUtils.wrapFields(fields, sqlConstants.getFieldPrefix(), sqlConstants.getFieldSuffix());
    }

    // 3. Handle Pagination limits
    int limitSize = searchRequest.getPageSize();
    // Convert pageNumber to limitStart (offset + 1 logic depending on your DAO implementation)
    // AbstractDAO.searchPaging expects limitStart as record index (1-based or 0-based check DAO).
    // Based on provided AbstractDAO: limitStart is "First record to be fetched".
    int limitStart = ((searchRequest.getPageNumber() - 1) * limitSize) + 1;

    if (searchRequest.isIgnoreLimit()) {
      limitSize = -1;
      limitStart = -1;
    }

    // 4. Handle Sorting
    String sortBy = searchRequest.getOrderBy(); 
    // Note: You might need to qualify sortBy with table name if ambiguous, 
    // or check if sortOrderAscending needs to append " ASC" or " DESC".
    if (!searchRequest.isSortOrderAscending()) {
      sortBy += " DESC";
    }

    // 5. Execute Search via PersistenceManager (which delegates to AbstractDAO)
    // We use AND as the root operator for the criteria array, 
    // though our SearchUtil usually returns a single Group node anyway.
    return searchPaging(
        pojo,
        fields,
        criteria,
        BOOLEAN_OPERATOR.AND, 
        sortBy,
        limitStart,
        limitSize,
        joins,
        null // Connection (null creates new)
    );
  }
  
  public <T> T getFirstByAttributes(T pojo, String[] fields,
      Collection<NameValueVO> attributeValues, Connection con) throws ORMException {
    return getFirstByAttributes(pojo, fields, attributeValues, null, -1, -1, con);
  } // end of method searchPaging

  public <T> T getFirstByAttributes(T pojo, String[] fields,
      Collection<NameValueVO> attributeValues, String sortBy,
      int limitStart, int limitSize, Connection con) throws ORMException {
    PagingVO pagingVO = searchByAttributes(pojo, fields, attributeValues, sortBy, limitStart, limitSize, con);
    return (T)pagingVO.getResults().get(0);
  } // end of method searchPaging

  public <T> T getFirstByAttribute(T pojo, NameValueVO attributeValue,
      String[] fields, Connection con) throws ORMException {
    Collection<NameValueVO> attributeValues = Arrays.asList(new NameValueVO[] {attributeValue});
    return getFirstByAttributes(pojo, fields, attributeValues, null, 0, 0, con);
  } // end of method searchPaging

  public <T> T getFirstByAttribute(T pojo, NameValueVO attributeValue,
      String[] fields) throws ORMException {
    return getFirstByAttribute(pojo, attributeValue, fields, null);
  } // end of method searchPaging

  public <T> T getFirstByAttribute(T pojo, NameValueVO attributeValue) throws ORMException {
    return getFirstByAttribute(pojo, attributeValue, null);
  } // end of method searchPaging

  public PagingVO searchByAttributes(Object pojo, String[] fields,
      Collection<NameValueVO> attributeValues, String sortBy,
      int limitStart, int limitSize, Connection con) throws ORMException {
    
    SQLCriterion[] criteria = (SQLCriterion[])attributeValues
        .stream()
        .map(value -> ORMInfoManager.sqlCriterionFactory.createEqualTo(value.getAttributeName(),  value.getAttributeValue()))
        .toArray(SQLCriterion[]::new);
    return searchPaging(pojo, ORMInfoManager.SUPER_LEVEL, fields, criteria, BOOLEAN_OPERATOR.AND, sortBy, limitStart, limitSize, con);
  } // end of method searchPaging

  public PagingVO searchByAttribute(Object pojo, String[] fields,
      NameValueVO attributeValue, String sortBy,
      int limitStart, int limitSize, Connection con) throws ORMException {
    Collection<NameValueVO> attributeValues = Arrays.asList(new NameValueVO[] {attributeValue});
    return searchByAttributes(pojo, fields, attributeValues, sortBy, limitStart, limitSize, con);
  } // end of method searchPaging

  /**
   * {@inheritDoc}
   */
  public  <T> DbResult<T> insert(T pojo, Connection con) throws ORMException {
    boolean returnInsertedObject = ORMInfoManager.RETURN_AFFECTED_OBJECTS_INSERT;
    return insert(pojo, con, returnInsertedObject);
  }
  
  /**
   * {@inheritDoc}
   */
  public  <T> DbResult<T> insert(T pojo, Connection con, boolean returnInsertedObject) throws ORMException {
    return insert(pojo, con, returnInsertedObject, true, true);
  }
  
  /**
   * {@inheritDoc}
   */
  public  <T> DbResult<T> insert(T pojo, Connection con, boolean returnInsertedObject, boolean insertContainedParentObjects, boolean insertContainedChildObjects) throws ORMException {
    return insert(pojo.getClass(), pojo, con, returnInsertedObject, insertContainedParentObjects, insertContainedChildObjects);
  } // end of method insert

  /**
   * Inserts the contained parent objects of the given <code>clazz</code> by
   * calling {@link #insert(Class, Object, Connection, boolean, boolean, boolean)}
   * when the primary key value of the parent object is not set.
   * 
   * @param parentClass {@link Class} to get the structure from. Its structure
   * is used to make the <code>INSERT</code> query. It has been separated from
   * <code>pojo</code> to avoid copying fields from the given <code>pojo</code>
   * to its super class object and then copying primary key value from the super 
   * class object back to the given <code>pojo</code>.
   * @param pojo Object to get the field values from.
   * @param con Required {@link Connection} object to be used to insert the
   * record.
   * @param insertContainedParentObjects See
   * {@link #insert(Object, Connection, boolean)} for details.
   * @param insertContainedChildObjects See
   * {@link #insert(Object, Connection, boolean)} for details.
   * 
   * @return Number of rows inserted whether parents of the given
   * <code>pojo</code> or any other parent/child objects in the hierarchy.
   * 
   * @throws ORMException In case of any underlying database error.
   */
  protected <T> void insertContainedParentObjects(Class parentClass, T pojo,
      T resultObj, Connection con, boolean insertContainedParentObjects,
      boolean insertContainedChildObjects) throws ORMException {
    
    Collection<ContainedObjectField> containedObjFields = ORMInfoManager.getContainedObjectFields(parentClass);
    if(containedObjFields == null) return;
    
    for (ContainedObjectField containedObjField : containedObjFields) {
      if(containedObjField.getRelationshipWithContainedObject() != AnnotationConstants.RELATIONSHIP_PARENT) {
        continue;
      } // end of if
      Object parentObj = Utility.invokeMethod(pojo, containedObjField.getGetterMethod());
      if(parentObj != null && ORMInfoManager.isPrimaryKeyValueSet(parentObj) == false) {
        logger.finer("Inserting parent object [" + parentObj.getClass().getName() + "] of [" + parentClass.getName() + "].");
        DbResult<T> insertedObj = insert(parentObj.getClass(), (T)parentObj, con, false, insertContainedParentObjects, insertContainedChildObjects);
        
        DBField relatedInstanceMemberField = ORMInfoManager.getFieldByInstanceMemberName(parentClass, containedObjField.getRelatedInstanceMemberName());
        DBField parentField = ORMInfoManager.getDBField(containedObjField.getContainedObjectType(), containedObjField.getReferencedField());
        Object parentFieldValue = Utility.invokeMethod(parentObj, parentField.getGetterMethod());
        logger.finer("Setting foreign key value [" + parentFieldValue + "] in [" + relatedInstanceMemberField.getInstanceMemberName() + "] field of [" + parentClass.getName() + "] object.");
        Utility.invokeMethod(pojo, relatedInstanceMemberField.getSetterMethod(), new Object[] {parentFieldValue});
      } // end of if
    } // end of for
  } // end of method insertContainedParentObjects
  
  /**
   * Inserts the contained child objects of the given <code>clazz</code> by
   * calling {@link #insert(Class, Object, Connection, boolean, boolean, boolean)}.
   * <p>The contained objects are read from <code>pojo</code>.</p>
   * 
   * @param clazz {@link Class} to get the structure from. Its structure is used
   * to make the <code>INSERT</code> query. It has been separated from
   * <code>pojo</code> to avoid copying fields from given <code>pojo</code>  to
   * it super class object and then copying primary key value from the super 
   * class object back to the given <code>pojo</code>.
   * @param pojo Object to get the field values from.
   * @param resultObj The result object to fill with inserted child objects
   * @param con Required {@link Connection} object to be used to insert the
   * record.
   * @param insertContainedParentObjects See
   * {@link #insert(Object, Connection, boolean)} for details.
   * @param insertContainedChildObjects See
   * {@link #insert(Object, Connection, boolean)} for details.
   * 
   * @return Number of rows inserted whether children of the given
   * <code>pojo</code> or any other parent/children objects in the hierarchy.
   * 
   * @throws ORMException In case of any underlying database error.
   */
  protected <T> void insertContainedChildObjects(Class clazz, T pojo,
      T resultObj, Connection con, boolean insertContainedParentObjects,
      boolean insertContainedChildObjects) throws ORMException {

    Object containedObj = null;

    Object parentPKValue = ORMInfoManager.getPrimaryKeyValue(clazz, resultObj);

    logger.finer("About to insert contained objects for [" + clazz.getName()
        + "] object with primary key value [" + parentPKValue + "].");
    Collection<ContainedObjectField> containedObjFields = ORMInfoManager.getContainedObjectFields(clazz);
    if(containedObjFields == null) return;
    
    for (ContainedObjectField containedObjField : containedObjFields) {
      if (containedObjField.getRelationshipWithContainedObject() != AnnotationConstants.RELATIONSHIP_CHILD) {
        continue;
      }
      try {
        containedObj = Utility.invokeMethod(pojo, containedObjField.getGetterMethod());
        if (containedObj == null) {
          logger.finer("Contained object of type ["
              + containedObjField.getContainedObjectType()
              + "] is null. Hence skipping.");
          continue;
        } // end of if

        DBField childField = ORMInfoManager.getDBField(containedObjField.getContainedObjectType(), containedObjField.getReferencedField());
        logger.finer("Contained child's related field name is [" + childField.getInstanceMemberName() + "].");
        Method childFieldSetterMethod = childField.getSetterMethod();

        if (containedObj instanceof Collection) {
          logger.finer("Contained object type is ["
              + containedObj.getClass().getName() + "] of ["
              + containedObjField.getContainedObjectType() + "].");
          Collection objs = (Collection) containedObj;
          Collection<Object> insertedChildren = new ArrayList<>();
          for (Object obj : objs) {
            Utility.invokeMethod(obj, childFieldSetterMethod,
                new Object[] { parentPKValue });
            DbResult<Object> childResult = insert(obj.getClass(), obj, con,
                true, insertContainedParentObjects,
                insertContainedChildObjects);
            if (childResult.hasEntities()) {
              insertedChildren.addAll(childResult.getEntities());
            }
          } // end of for
          // Set the inserted children in the result object
          Method setterMethod = containedObjField.getSetterMethod();
          setterMethod.invoke(resultObj, new Object[] { insertedChildren });
        } // end of if
        else if (containedObj.getClass().isArray()) {
          logger.finer("Contained object type is array of ["
              + containedObjField.getContainedObjectType() + "].");
          Object[] objs = (Object[]) containedObj;
          List<Object> insertedChildren = new ArrayList<>();
          for (Object obj : objs) {
            Utility.invokeMethod(obj, childFieldSetterMethod,
                new Object[] { parentPKValue });
            DbResult<Object> childResult = insert(obj.getClass(), obj, con,
                true, insertContainedParentObjects,
                insertContainedChildObjects);
            if (childResult.hasEntities()) {
              insertedChildren.add(childResult.getEntities());
            }
          } // end of for
          // Set the inserted children in the result object
          Method setterMethod = containedObjField.getSetterMethod();
          setterMethod.invoke(resultObj,
              new Object[] { insertedChildren.toArray() });
        } // end of if
        else {
          logger.finer("Contained object type is of type ["
              + containedObjField.getContainedObjectType() + "].");
          Utility.invokeMethod(containedObj, childFieldSetterMethod,
              new Object[] { parentPKValue });
          DbResult<Object> childResult = insert(containedObj.getClass(),
              containedObj, con, true, insertContainedParentObjects,
              insertContainedChildObjects);
          if (childResult.hasEntities()) {
            // Set the inserted child in the result object
            Method setterMethod = containedObjField.getSetterMethod();
            setterMethod.invoke(resultObj, childResult.getEntities());
          }
        } // end of if
      }
      catch (Exception eee) {
        logger.warning(
            "Error inserting contained child object: " + eee.getMessage());
      }
    } // end of for
  } // end of method insertContainedChildObjects
  
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
      boolean returnInsertedObject, boolean insertContainedParentObjects,
      boolean insertContainedChildObjects) throws ORMException {
    throw new RuntimeException("Must be implemented by the database specific impl class");
  } // end of method insert

  /**
   * {@inheritDoc}
   */
  public <T> DbResult<T> update(T pojo, Connection con) throws ORMException {
    boolean returnUpdatedObject = ORMInfoManager.RETURN_AFFECTED_OBJECTS_UPDATE;
    return update(pojo, con, returnUpdatedObject);
  }
  
  /**
   * {@inheritDoc}
   */
  public <T> DbResult<T> update(T pojo, Connection con, boolean returnUpdatedObject) throws ORMException {
    DbResult<T> resultObj = null;
    boolean isNewConnection = false;
    try {
      if(con == null || con.isClosed() == true) {
        con = getConnection();
        con.setAutoCommit(false);
        isNewConnection = true;
      } // end of if

      // super class objects
      Object superObj = ORMInfoManager.getSuperClassObject(pojo, true);
      if(superObj != null && superObj.getClass().getName().equals(Object.class.getName()) == false) {
        update(superObj, con, returnUpdatedObject);

        try {
          ORMInfoManager.setSuperObjectPKValue(pojo, superObj);
        } catch(Exception eee) {}
      } // end of if
      
      String[] fields = StringUtils.splitString(ORMInfoManager.getPlainDBFieldNames(pojo), sqlConstants.getFieldsSeparator());
      logger.finest("object fields: " + Arrays.toString(fields));
      resultObj = update(pojo, fields, con, returnUpdatedObject);
      if(isNewConnection) {
        con.commit();
      } // end of if
    } // end of try
    catch(SQLException e) {
      try {
        if(isNewConnection) {
          con.rollback();
        } // end of if
      } catch(Exception ee) {}
      throw exceptionUtil.getUpdateException(e.getMessage(), e);
    } // end of catch
    finally {
      if(isNewConnection) {
        try {
          con.setAutoCommit(true);
        } catch(Exception ee) {}
        closeConnection(con);
      } // end of if
    } // end of finally
    
    return resultObj;
  } // end of method update
  
  /**
   * {@inheritDoc}
   */
  public <T> DbResult<T> update(T pojo, String[] fields, Connection con, boolean returnUpdatedObject) throws ORMException {
    SQLCriterion[] criteria = new SQLCriterion[1];
    criteria[0] = criteronFactory.createEqualTo(ORMInfoManager.getQualifiedPrimaryKey(pojo), null, 
        ORMInfoManager.getPrimaryKeyValue(pojo), SQLCriterionFactory.INT);
    return update(pojo, fields, criteria, BOOLEAN_OPERATOR.AND, con, returnUpdatedObject);
  } // end of method update
  
  /**
   * {@inheritDoc}
   */
  public <T> DbResult<T> update(T pojo, String[] fields, SQLCriterion[] criteria,
      BOOLEAN_OPERATOR booleanOperator, Connection con, boolean returnUpdatedObject)
      throws ORMException {
    throw new RuntimeException("Must be implemented by the database specific impl class");
//    DbResult<T> resultObj = null;
//    try {
//      String sql = createUpdateQuery(pojo, fields, criteria, booleanOperator);
//      T updatedObj = (T)executeUpdateQuery(sql, pojo.getClass(), con);
//      resultObj = new DbResult<T>(updatedObj);
//    } // end of try
//    catch(SQLException e) {
//      throw exceptionUtil.getUpdateException(e.getMessage(), e);
//    } // end of catch
//    return resultObj;
  } // end of method update
  
  /**
   * {@inheritDoc}
   */
  public <T> DbResult<T> delete(T pojo, Connection con)
      throws ORMException {
    boolean returnDeletedObject = ORMInfoManager.RETURN_AFFECTED_OBJECTS_DELETE;
    return delete(pojo, ORMInfoManager.getPrimaryKeyValue(pojo), con, returnDeletedObject);
  } // end of method delete
  
  /**
   * {@inheritDoc}
   */
  public <T> DbResult<T> delete(T pojo, Connection con, boolean returnDeletedObject)
      throws ORMException {
    return delete(pojo, ORMInfoManager.getPrimaryKeyValue(pojo), con, returnDeletedObject);
  } // end of method delete
  
  /**
   * {@inheritDoc}
   */
  public <T> DbResult<T> delete(T pojo, Object pkValue, Connection con, boolean returnDeletedObject) 
      throws ORMException {
    Collection ids = Arrays.asList(new Object[] {pkValue});
    return delete(pojo, ids, con, returnDeletedObject);
  } // end of method delete
  
  /**
   * {@inheritDoc}
   */
  public <T> DbResult<T> delete(Collection<T> pojos, Connection con, boolean returnDeletedObject)
      throws ORMException {
    if(pojos == null || pojos.size() < 1) {
      return new DbResult<T>(0);
    }
    List ids = ORMInfoManager.getIds(pojos);
    return delete((T)pojos.toArray(new Object[0])[0], (Collection)ids, con, returnDeletedObject);
  } // end of method delete
  
  /**
   * {@inheritDoc}
   */
  public <T> DbResult<T> delete(T pojo, SQLCriterion[] criteria,
      BOOLEAN_OPERATOR booleanOperator, Connection con,
      boolean returnDeletedObject) throws ORMException {
    throw new RuntimeException("Must be implemented by the database specific impl class");
  } // end of method delete
  
  /**
   * {@inheritDoc}
   */
  public <T> DbResult<T> delete(T pojo, Collection ids,
      Connection con, boolean returnDeletedObject) throws ORMException {
    throw new RuntimeException("Must be implemented by the database specific impl class");
  } // end of method delete

  /**
   * {@inheritDoc}
   */
  public <T> DbResult<T> cascadeDelete(T pojo, Connection con,
      boolean returnDeletedObject) throws ORMException {
    Collection<T> collection = new ArrayList<T>();
    collection.add(pojo);
    return (DbResult<T>)cascadeDelete(collection, con, returnDeletedObject);
  } // end of method cascadeDelete
  
  /**
   * {@inheritDoc}
   * 
   * TODO This method needs to be refactored for better performance. Each level objects can be grouped to delete in a single query.
   */
  public <T> DbResult<T> cascadeDelete(Collection<T> pojos,
      Connection con, boolean returnDeletedObject) throws ORMException {
    DbResult<T> result = null;
    if(pojos == null || pojos.size() < 1) {
      return new DbResult<T>(0);
    } // end of if
    boolean isNewConnection = false;
    try {
      if(con == null || con.isClosed() == true) {
        con = getConnection();
        con.setAutoCommit(false);
        isNewConnection = true;
      } // end of if

      // contained Objects
      Object containedObj = null;
      for(Object pojo : pojos) {
        Collection<ContainedObjectField> containedObjFields = ORMInfoManager.getContainedObjectFields(pojo);
        for (ContainedObjectField containedObjField : containedObjFields) {
          if(containedObjField.getRelationshipWithContainedObject() != AnnotationConstants.RELATIONSHIP_CHILD) {
            continue;
          }
          logger.finer("Contained child object [" + containedObjField.getField().getName() + "]");
          containedObj = Utility.invokeMethod(pojo, containedObjField.getGetterMethod());
          if(containedObj == null) {
            continue;
          } // end of if

          if(containedObj.getClass().isArray()) {
            if(((Object[])containedObj).length < 1) {
              continue;
            } // end of if
            Collection<T> collection = (Collection<T>)(Arrays.asList((T[])containedObj));
            DbResult<T> deletedChild = cascadeDelete(collection, con, returnDeletedObject);
            logger.finer("Contained child objects [" + containedObj + "] are about to be deleted.");
          }
          if(containedObj instanceof Collection) {
            Collection<T> objs = (Collection<T>)containedObj;
            if(objs.size() < 1) {
              continue;
            } // end of if
            DbResult<T> deletedChildren = cascadeDelete(objs, con, returnDeletedObject);
            logger.finer("Contained child objects [" + containedObj + "] are about to be deleted.");
          } // end of if
          else {
            Collection<T> collection = (Collection<T>)(Arrays.asList((T[])new Object[] {containedObj}));
            result = cascadeDelete(collection, con, returnDeletedObject);
            logger.finer("Contained child object [" + containedObj.toString() + "] is about to be deleted.");
          } // end of if
        } // end of for contained objects
      } // end of for pojos
      
      // actual object
      result = delete((T)pojos.toArray()[0], (Collection)ORMInfoManager.getIds(pojos), con, returnDeletedObject);
      
      if(isNewConnection) {
        con.commit();
      } // end of if
    } // end of try
    catch(SQLException e) {
      try {
        if(isNewConnection) {
          con.rollback();
        } // end of if
      } catch(Exception ee) {}
      throw exceptionUtil.getDeleteException(e.getMessage(), e);
    } // end of catch
    finally {
      if(isNewConnection) {
        try {
          con.setAutoCommit(true);
        } catch(Exception ee) {}
        closeConnection(con);
      } // end of if
    } // end of finally
    
    return result;
  } // end of method cascadeDelete
    
  /**
   * {@inheritDoc}
   */
  public void populateContainedObjects(Object[] containers,
      String containedObjectInstanceMemeber, Connection con)
      throws ORMException {
    if(containers == null || containers.length < 1 || containedObjectInstanceMemeber == null || containedObjectInstanceMemeber.trim().length() < 1) {
      return;
    }
    logger.info("containedObjectInstanceMemeber: " + containers[0].getClass().getName() + "." + containedObjectInstanceMemeber);
    ContainedObjectField containedObjField = ORMInfoManager.getContainedObjectField(containers[0], containedObjectInstanceMemeber);
    if(containedObjField == null) {
      ORMInfoManager.getContainedObjectFields(containers[0]).forEach(System.out::println);
      logger.warning("No instance member found with the given name [" + containedObjectInstanceMemeber + "] in class [" + containers[0].getClass().getName() + "].");
      return;
    } // end of if
    populateContainedObjects(containers, containedObjField, con);
  } // end of method populateContainedObjects
  
  /**
   * {@inheritDoc}
   */
  public void populateContainedObjects(Object[] containers,
      ContainedObjectField containedObjField, Connection con)
      throws ORMException {
    if (containers == null || containers.length < 1 || containedObjField == null) {
      return;
    } // end of if
    Object containerObj = containers[0];

    DBField containerObjRelatedField = ORMInfoManager.getFieldByInstanceMemberName(containerObj, containedObjField.getRelatedInstanceMemberName());
    Object pkValue = Utility.invokeMethod(containerObj, containerObjRelatedField.getGetterMethod());
    List ids = ORMInfoManager.getIds(containers);
    Collections.sort(ids);
    DBField containedObjPKField = ORMInfoManager.getDBField(containedObjField.getContainedObjectType(), containedObjField.getReferencedField());
    SQLCriterion[] criteria = new SQLCriterion[1];
    criteria[0] = criteronFactory.createIn(containedObjField.getReferencedField(), containedObjField.getReferencedEntity(), ids, criteronFactory.getType(pkValue), null, null);
    
    Collection records = search(containedObjPKField.getPojo(), null, criteria, BOOLEAN_OPERATOR.AND, ORMInfoManager.getQualifiedField(containedObjField.getReferencedEntity(), containedObjPKField.getDbFieldName(), true), -1, -1, con);
    if (records == null || records.size() < 1) {
      return;
    } // end of if
    Object[] recordsArray = (Object[])records.toArray(new Object[0]);
    Method setterMethod = containedObjField.getSetterMethod();
    Class setterParamType = setterMethod.getParameterTypes()[0];
    if(Utility.isChildClass(setterParamType, Collection.class) || setterParamType.isArray()) {
      fillContainersMulti(containers, recordsArray, containerObjRelatedField, containedObjPKField, containedObjField);
    } // end of if
    else {
      fillContainersSingle(containers, recordsArray, containerObjRelatedField, containedObjPKField, containedObjField);
    } // end of else
  } // end of method populateContainedObjects
  
  /**
   * {@inheritDoc}
   */
  public java.sql.Connection getConnection() throws SQLException {

    Connection con = null;

    con = pool.getConnection();

    return con;
  } // end of method getConnection

  /**
   * {@inheritDoc}
   */
  public void closeConnection(java.sql.Connection con) {
    try {
      if (con != null) {
        pool.returnConnection(con);
      } // end of if
    } // end of try
    catch (Exception e) {
      logger.warning(Utility.getStackTrace(e));
    } // end of catch
  } // end of method closeConnection
  
  /**
   * {@inheritDoc}
   */
  public synchronized int getNewId(String tableName, String primaryKeyDBField)
      throws ORMException {

    // FIXME Based on a configuration flag, either do as following or get the next value from sequence in the database. In case more than one applications can insert into a table, the following approach wont work.
    if(ids.containsKey(tableName) == true) {
      Integer id = ids.get(tableName);
      ids.put(tableName, id + 1);
      return id.intValue();
    } // end of if
    
    Statement stmt = null;
    java.sql.Connection con = null;
    ResultSet rst = null;
    int newId = 0;
    StringBuffer sql = new StringBuffer("Select max(");
    sql.append(StringUtils.wrapDBField(primaryKeyDBField, sqlConstants.getFieldPrefix()));
    sql.append(") From ");
    sql.append(StringUtils.wrap(tableName, sqlConstants.getFieldPrefix(), sqlConstants.getFieldSuffix()));
    logger.info(sql.toString() + ";");

    try {
      con = getConnection();

      stmt = con.createStatement();
      rst = stmt.executeQuery(sql.toString());

      if (rst.next()) {
        newId = rst.getInt(1);
      } // end of if
      rst.close();
    } // end of try
    catch (SQLException e) {
      throw exceptionUtil.getSelectException(e.getMessage(), e);
    } // end of catch
    finally {
      try {
        if (stmt != null) {
          stmt.close();
        } // end of if
      } catch (SQLException ex) {}

      closeConnection(con);
    } // end of finally
    
    ids.put(tableName, newId);
    return (newId + 1);
  } // end of method getNewId

  /**
   * {@inheritDoc}
   */
  public String createSelectQuery(Object pojo) {
    return createSelectQuery(pojo, ORMInfoManager.SUPER_LEVEL);
  } // end of method createSelectQuery
  
  /**
   * {@inheritDoc}
   */
  public String createSelectQuery(Object pojo, int level) {
    SQLCriterion[] criteria = new SQLCriterion[1];
    Object pkValue = ORMInfoManager.getPrimaryKeyValue(pojo);
    criteria[0] = criteronFactory.createEqualTo(ORMInfoManager.getQualifiedPrimaryKey(pojo), pkValue);
    return createSelectQuery(pojo, level, criteria, BOOLEAN_OPERATOR.AND);
  } // end of method createSelectQuery
  
  /**
   * {@inheritDoc}
   */
  public String createSelectQuery(Object pojo, int level, SQLCriterion[] criteria, BOOLEAN_OPERATOR booleanOperator) {
    return createSelectQuery(pojo, level, null, criteria, booleanOperator);
  } // end of method createSelectQuery
  
  /**
   * {@inheritDoc}
   */
  public String createSelectQuery(Object pojo, int level, String[] fields, SQLCriterion[] criteria, BOOLEAN_OPERATOR booleanOperator) {
    return createSelectQuery(pojo, level, fields, criteria, booleanOperator, null, -1, -1);
  } // end of method createSelectQuery
  
  /**
   * {@inheritDoc}
   */
  public String createGroupByQuery(Object pojo, String[] fields, 
      SQLFunction[] functions, SQLCriterion[] criteria, BOOLEAN_OPERATOR booleanOperator,
      String sortBy, int limitStart, int limitSize) {
    return createSelectQuery(pojo, 0, fields, true, functions, criteria, booleanOperator, sortBy, limitStart, limitSize, null);
  } 

  /**
   * {@inheritDoc}
   */
  public String createGroupByQuery(Object pojo, String[] fields, 
      SQLFunction[] functions, SQLCriterion[] criteria, BOOLEAN_OPERATOR booleanOperator,
      String sortBy, int limitStart, int limitSize, List<Join> joins) {
    return createSelectQuery(pojo, 0, fields, true, functions, criteria, booleanOperator, sortBy, limitStart, limitSize, joins);
  }
  
  /**
   * {@inheritDoc}
   */
  public String createGroupByQuery(String[] fields, String fromClause, 
      SQLFunction[] functions, SQLCriterion[] criteria, BOOLEAN_OPERATOR booleanOperator,
      String sortBy, int limitStart, int limitSize) {

    return createSelectQuery(fields, fromClause, functions, criteria, booleanOperator, sortBy, limitStart, limitSize);
  } // end of method createGroupByQuery
  
  /**
   * {@inheritDoc}
   */
  public String createSelectQuery(Object pojo, int level, String[] fields, 
      SQLCriterion[] criteria, BOOLEAN_OPERATOR booleanOperator, String sortBy, int limitStart, int limitSize) {
    return createSelectQuery(pojo, level, fields, null, criteria, booleanOperator,
        sortBy, limitStart, limitSize);
  } // end of method createSelectQuery
  
  /**
   * Override this method if the fields (optionally qualified) need to be
   * wrapped in database specific field wrappers.
   * It returns the fields as is.
   * @param fields
   * @return fields without wrapping. 
   */
  protected String wrapFields(String fields) {
    return fields;
  }
  
  protected String wrapSortBy(String sortBy) {
    return sortBy;
  }
  
  /**
   * {@inheritDoc}
   */
  public String createSelectQuery(Object pojo, int level, String[] fields,
      SQLFunction[] functions, SQLCriterion[] criteria, BOOLEAN_OPERATOR booleanOperator,
      String sortBy, int limitStart, int limitSize) {
    return createSelectQuery(pojo, level, fields, functions, criteria, booleanOperator, sortBy, limitStart, limitSize, null);
  }

  /**
   * {@inheritDoc}
   */
  public String createSelectQuery(Object pojo, int level, String[] fields, 
      SQLFunction[] functions, SQLCriterion[] criteria, BOOLEAN_OPERATOR booleanOperator,
      String sortBy, int limitStart, int limitSize, List<Join> joins) {
    return createSelectQuery(pojo, level, fields, false, functions, criteria,
        booleanOperator, sortBy, limitStart, limitSize, joins);
  }
  
  /**
   * {@inheritDoc}
   */
  public String createSelectQuery(Object pojo, int level, String[] fields, boolean isForGroupByQuery,
      SQLFunction[] functions, SQLCriterion[] criteria, BOOLEAN_OPERATOR booleanOperator,
      String sortBy, int limitStart, int limitSize, List<Join> joins) {
    StringBuffer strFields = new StringBuffer("");
    StringBuffer from = new StringBuffer("");
    StringBuffer where = new StringBuffer("");
    StringBuffer groupByFunctions = new StringBuffer("");
    StringBuffer groupByClause = new StringBuffer("");

    if(fields != null && fields.length > 0) {
      strFields.append(wrapFields(StringUtils.stringArrayToString(fields, sqlConstants.getFieldsSeparator())));
    } // end of if
    else if(isForGroupByQuery == false) {
      strFields.append(wrapFields(ORMInfoManager.getQualifiedDBFieldNames(pojo)));
    } // end of if
    String entityName = ORMInfoManager.getEntityName(pojo);
    String fromEntity = StringUtils.wrap(entityName, sqlConstants.getFieldPrefix(), sqlConstants.getFieldSuffix());
    String fromEntityWithAlias = fromEntity + sqlConstants.getAliasSeparator() + fromEntity;
    from.append(fromEntityWithAlias);
    try {
      Object childObj = pojo;
      Class superClass = ORMInfoManager.getSuperClass(pojo.getClass());
      if(superClass != null && !Modifier.isAbstract(superClass.getModifiers())) {
        Object parentObj = ORMInfoManager.instantiate(superClass.getName());
        String parentEntityName = ORMInfoManager.getEntityName(parentObj);
        String parentPK = ORMInfoManager.getPrimaryKeyField(parentObj).getDbFieldName();
        for (int i = 0; i < level; i++) {
          if(parentObj == null || Object.class.getName().equals(parentObj.getClass().getName())) {
            break;
          } // end of for
          if(fields == null || fields.length < 1) {
            strFields.append(sqlConstants.getFieldsSeparator());
            strFields.append(wrapFields(ORMInfoManager.getQualifiedDBFieldNames(parentObj)));
          } // end of if
          String parentEntity = StringUtils.wrap(parentEntityName, sqlConstants.getFieldPrefix(), sqlConstants.getFieldSuffix());
          String parentTableReferenceField = ORMInfoManager.getQualifiedField(null, parentPK, true);
          String childTableReferenceField = ORMInfoManager.getQualifiedField(null, ORMInfoManager.getPrimaryKeyField(childObj).getDbFieldName(), true);
          from.append(getSimpleInnerJoin(entityName, parentEntity, parentEntity, childTableReferenceField, parentTableReferenceField).getJoinClause(criteronFactory));
          childObj = parentObj;
          Class superClass1 = ORMInfoManager.getSuperClass(childObj.getClass());
          if(superClass1 == null || Modifier.isAbstract(superClass.getModifiers()) || Object.class.getName().equals(parentObj.getClass().getName())) {
            break;
          }
          parentObj = ORMInfoManager.instantiate(superClass.getName());
          parentPK = ORMInfoManager.getPrimaryKeyField(parentObj).getDbFieldName();
        } // end of for
      }
    } catch(Exception ee) {}

    if (joins != null) {
      for (Join join : joins) {
        try {
          FromElement wrappedFromElement = new FromElement();
          wrappedFromElement.setTableName(wrapFields(join.getRightSide().getTableName()));
          wrappedFromElement.setAlias(wrapFields(join.getRightSide().getAlias()));
          join.setRightSide(wrappedFromElement);
          from.append(join.getJoinClause(criteronFactory));
        }
        catch (SQLSyntaxErrorException e) {
           throw new RuntimeException(e);
        }
      }
    }

    if(criteria != null && criteria.length > 0) {
      String criteriaStr = criteronFactory.createCriteriaString(criteria, booleanOperator);
      if(criteriaStr != null && criteriaStr.trim().length() > 0) {
        if(where.toString().trim().length() > 0) {
          where.append(" ");
          where.append(sqlConstants.getOpAnd());
          where.append(" ");
        } // end of if
        where.append("(");
        where.append(criteriaStr);
        where.append(")");
      }
    } // end of if
    String whereClause = (where.toString().length() > 0) ? sqlConstants.getQryWhere().format(new Object[] {where.toString()}) : "";
    String sortByClause = "";
    if(sortBy != null && sortBy.trim().length() > 0) {
      sortByClause = sqlConstants.getQrySortBy().format(new Object[] {wrapSortBy(sortBy)});
    } // end of if
    
    String limitClause = "";
    if(limitSize > 0) {
      if(limitStart > 1) {
        limitClause = sqlConstants.getQryLimit2().format(new Object[] {"" + (limitStart - 1), "" + limitSize});
      } // end of if
      else {
        limitClause = sqlConstants.getQryLimit1().format(new Object[] {"" + limitSize});
      } // end of else
    } // end of if
    
    if(functions == null || functions.length < 1) {
      return sqlConstants.getQrySelectQuery().format(new Object[] {sqlConstants.getQrySelect().format(new Object[] {sqlConstants.getDistinct() + strFields.toString()}), 
          sqlConstants.getQryFrom().format(new Object[] {from.toString()}), whereClause, sortByClause, limitClause}).trim();
    } // end of if
    String groupByClauseStr = "";
    groupByFunctions.append(functionFactory.getFunctionString(functions, true));
    if(strFields.length() > 0) {
      groupByClause.append(strFields.toString());
      groupByClauseStr = sqlConstants.getQryGroupBy().format(new Object[] {groupByClause.toString()});
      strFields.append(sqlConstants.getFieldsSeparator());
    }
    strFields.append(groupByFunctions.toString());
    return sqlConstants.getQryGroupByQuery().format(new Object[] {sqlConstants.getQrySelect().format(new Object[] {strFields.toString()}), 
        sqlConstants.getQryFrom().format(new Object[] {from.toString()}), whereClause,
        groupByClauseStr, sortByClause, limitClause}).trim();
  } // end of method createSelectQuery
  
  /**
   * {@inheritDoc}
   */
  public String createSelectQuery(String[] fields, String fromClause,
      SQLFunction[] functions, SQLCriterion[] criteria, BOOLEAN_OPERATOR booleanOperator,
      String sortBy, int limitStart, int limitSize) {

      StringBuffer strFields = new StringBuffer("");
      StringBuffer from = new StringBuffer(fromClause);
      StringBuffer where = new StringBuffer("");
      StringBuffer groupByFunctions = new StringBuffer("");
      StringBuffer groupByClause = new StringBuffer("");

      if(fields != null && fields.length > 0) {
        strFields.append(wrapFields(StringUtils.stringArrayToString(fields, sqlConstants.getFieldsSeparator())));
      } // end of if
      else {
        strFields.append(sqlConstants.getOpAll());
      } // end of if
      if(criteria != null && criteria.length > 0) {
        if(where.toString().trim().length() > 0) {
          where.append(" ");
          where.append(sqlConstants.getOpAnd());
          where.append(" ");
        } // end of if
        where.append("(");
        where.append(criteronFactory.createCriteriaString(criteria, booleanOperator));
        where.append(")");
      } // end of if
      String whereClause = (where.toString().length() > 0) ? sqlConstants.getQryWhere().format(new Object[] {where.toString()}) : "";
      String sortByClause = "";
      if(sortBy != null && sortBy.trim().length() > 0) {
        sortByClause = sqlConstants.getQrySortBy().format(new Object[] {wrapSortBy(sortBy)});
      } // end of if
      
      String limitClause = "";
      if(limitSize > 0) {
        if(limitStart > 1) {
          limitClause = sqlConstants.getQryLimit2().format(new Object[] {"" + (limitStart - 1), "" + limitSize});
        } // end of if
        else {
          limitClause = sqlConstants.getQryLimit1().format(new Object[] {"" + limitSize});
        } // end of else
      } // end of if
      
      if(functions == null || functions.length < 1) {
        return sqlConstants.getQrySelectQuery().format(new Object[] {sqlConstants.getQrySelect().format(new Object[] {sqlConstants.getDistinct() + strFields.toString()}), 
            from.toString(), whereClause, sortByClause, limitClause}).trim();
      } // end of if
      groupByClause.append(strFields.toString());
      groupByFunctions.append(functionFactory.getFunctionString(functions, true));
      strFields.append(sqlConstants.getFieldsSeparator());
      strFields.append(groupByFunctions.toString());
      return sqlConstants.getQryGroupByQuery().format(new Object[] {sqlConstants.getQrySelect().format(new Object[] {strFields.toString()}), 
          sqlConstants.getQryFrom().format(new Object[] {from.toString()}), whereClause,
          sqlConstants.getQryGroupBy().format(new Object[] {groupByClause.toString()}), sortByClause, limitClause}).trim();
  } // end of method createSelectQuery
  
  /**
   * {@inheritDoc}
   */
  public String createInsertQuery(Object pojo) 
      throws ORMException {
    return createInsertQuery(pojo.getClass(), pojo);
  } // end of method createInsertQuery

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
  public String createInsertQuery(Class clazz, Object pojo) 
      throws ORMException {
    StringBuffer cols = new StringBuffer("");
    StringBuffer values = new StringBuffer("");
    String entityName = StringUtils.wrapDBField(null, ORMInfoManager.getEntityName(clazz.getName()), sqlConstants.getFieldPrefix(), sqlConstants.getFieldSuffix(), sqlConstants.getQualifierSeparator());
    serialize(clazz, pojo, cols, values, null, true);
    return sqlConstants.getQryInsertQuery().format(new Object[] {entityName, StringUtils.wrapFields(cols.toString().trim(), sqlConstants.getFieldPrefix(), sqlConstants.getFieldSuffix()), values.toString().trim()}).trim();
  } // end of method createInsertQuery

  /**
   * {@inheritDoc}
   */
  public String createInsertQueryShort(Object pojo) {
    StringBuffer cols = new StringBuffer("");
    StringBuffer values = new StringBuffer("");
    serialize(pojo, cols, values, null, false);
    return sqlConstants.getQryInsertQuery().format(new Object[] {ORMInfoManager.getEntityName(pojo),
        wrapFields(cols.toString().trim()), values.toString().trim()}).trim();
  } // end of method createInsertQueryShort
  
  /**
   * {@inheritDoc}
   */
  public String createUpdateQuery(Object pojo) {
    String[] fields = StringUtils.splitString(ORMInfoManager.getPlainDBFieldNames(pojo), sqlConstants.getFieldsSeparator());
    SQLCriterion[] criteria = new SQLCriterion[1];
    Object pkValue = ORMInfoManager.getPrimaryKeyValue(pojo);
    criteria[0] = criteronFactory.createEqualTo(ORMInfoManager.getPrimaryKeyField(pojo).getDbFieldName(), pkValue);
    
    return createUpdateQuery(pojo, fields, criteria, BOOLEAN_OPERATOR.AND);
  } // end of method createUpdateQuery
  
  /**
   * {@inheritDoc}
   */
  public String createUpdateQuery(Object pojo, String[] fields, SQLCriterion[] criteria, BOOLEAN_OPERATOR booleanOperator) {
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
      str.append(wrapFields((String)colsVec.elementAt(i)));
      str.append(sqlConstants.getOpEqual());
      str.append(valuesVec.elementAt(i));
    } // end of for

    String whereClause = criteronFactory.createCriteriaString(criteria, booleanOperator);
    if(StringUtils.getNull(whereClause) != null) {
      whereClause = sqlConstants.getQryWhere().format(new Object[] {whereClause});
    } // end of if
    return sqlConstants.getQryUpdateQuery().format(new Object[] {ORMInfoManager.getEntityName(pojo), str.toString().trim(), whereClause.trim()}).trim();
  } // end of method createUpdateQuery
  
  /**
   * {@inheritDoc}
   */
  public String createDeleteQuery(Object pojo) {
    SQLCriterion[] criteria = new SQLCriterion[1];
    criteria[0] = criteronFactory.createEqualTo(ORMInfoManager.getPrimaryKeyField(pojo).getDbFieldName(), null, ORMInfoManager.getPrimaryKeyValue(pojo));
    return createDeleteQuery(pojo, criteria, BOOLEAN_OPERATOR.AND);
  } // end of method createDeleteQuery
  
  /**
   * {@inheritDoc}
   */
  public String createDeleteQuery(Object pojo, Collection ids) {
    SQLCriterion[] criteria = new SQLCriterion[1];
    Object pkValue = ids != null && ids.size() > 0 ? ids.toArray()[0] : null;
    criteria[0] = criteronFactory.createIn(ORMInfoManager.getQualifiedPrimaryKey(pojo), null, ids, criteronFactory.getType(pkValue), null, null);
    return createDeleteQuery(pojo, criteria, BOOLEAN_OPERATOR.AND);
  } // end of method createDeleteQuery
  
  /**
   * {@inheritDoc}
   */
  public String createDeleteQuery(Object pojo, SQLCriterion[] criteria, BOOLEAN_OPERATOR booleanOperator) {
    String criteriaStr = "";
    if(criteria != null && criteria.length > 0) {
      criteriaStr = criteronFactory.createCriteriaString(criteria, booleanOperator);
    } // end of if
    if(StringUtils.getNull(criteriaStr) != null) {
      criteriaStr = sqlConstants.getQryWhere().format(new Object[] {criteriaStr});
    } // end of if
    return sqlConstants.getQryDeleteQuery().format(new Object[] {sqlConstants.getQryDelete().format(new Object[] {StringUtils.wrapDBField(ORMInfoManager.getEntityName(pojo), sqlConstants.getFieldPrefix())}), criteriaStr}).trim();
  } // end of method createDeleteQuery
  
  /**
   * {@inheritDoc}
   */
  public void serialize(Object pojo, StringBuffer cols, StringBuffer values, String[] fields, boolean includeEmpty) {
    serialize(pojo.getClass(), pojo, cols, values, fields, includeEmpty);
  }
  
  /**
   * Executes the getter methods on <code>pojo</code> as per the database fields
   * declared in <code>clazz</code> and fills their values in the given
   * <code>values</code>.
   * <p>Values are separated by 
   * <code>sqlConstantsObj.getFieldsSeparator()</code>. If <code>all</code> is
   * <code>false</code>, then <code>null</code> values returned by getter
   * methods, or zero if the object is of numeric ({@link Integer},
   * {@link Long}, {@link Float} or {@link Double}) type, or empty string, are
   * not appended to <code>values</code>.</p>
   * <p>If <code>cols</code> is not <code>null</code>, field names are also
   * appended in <code>cols</code>, separated by
   * <code>sqlConstantsObj.getFieldsSeparator()</code>.</p>
   * 
   * @param clazz {@link Class} whose getters are to be executed to get the
   * respective instance member values.
   * @param pojo Object on which the getter methods, taken from
   * <code>clazz</code>, are to be executed and values, and optionally field
   * names, are to be serialized in the given <code>values</code> buffer.
   * @param cols Buffer to serialize field names in.
   * @param values Buffer to serialize values in.
   * @param includeEmpty Whether to included a <code>null</code> or empty value
   * or not.
   */
  protected void serialize(Class clazz, Object pojo, StringBuffer cols, StringBuffer values, String[] fields, boolean includeEmpty) {
    Vector colsVec = null;
    if(fields == null || fields.length < 1) {
      colsVec = new Vector();
    } // end of if
    else {
      colsVec = new Vector(Arrays.asList(fields));
    } // end of else
    Vector valuesVec = new Vector();
    extractValues(clazz, pojo, colsVec, valuesVec, includeEmpty);
    values.append(StringUtils.stringArrayToString((String[])valuesVec.toArray(new String[0]), sqlConstants.getFieldsSeparator()));
    if(cols != null) {
      cols.append(StringUtils.stringArrayToString((String[])colsVec.toArray(new String[0]), sqlConstants.getFieldsSeparator()));
    } // end of if
  } // end of method serialize
  
  /**
   * {@inheritDoc}
   */
  public void extractValues(Object pojo, Collection cols, Collection values, boolean includeEmpty) {
    extractValues(pojo.getClass(), pojo, cols, values, includeEmpty);
  }
  
  /**
   * Executes the getter methods, on <code>pojo</code>, as per the database
   * fields declared in <code>clazz</code> and fills their values in the given
   * <code>values</code>.
   * <p>Values are separated by 
   * <code>sqlConstantsObj.getFieldsSeparator()</code>. If <code>all</code> is
   * <code>false</code>, then <code>null</code> values returned by
   * getter methods, or zero if the object is of numeric (Integer,
   * Long, Float or Double) type, or empty string, are not appended to
   * <code>values</code>.</p>
   * <p>If <code>cols</code> is not <code>null</code>, field names are also
   * appended in <code>cols</code>, separated by
   * <code>sqlConstantsObj.getFieldsSeparator()</code>.</p>
   * 
   * @param clazz {@link Class} whose getters are to be executed to get the
   * respective instance member values.
   * @param pojo Object on which, getters, taken from <code>clazz</code>, are to
   * be executed and values, and optionally field names, are to be serialized in
   * the given <code>values</code> buffer.
   * @param cols Buffer to serialize field names in.
   * @param values Buffer to serialize values in.
   * @param all Whether to included a <code>null</code> or empty value or not.
   */
  public void extractValues(Class clazz, Object pojo, Collection cols, Collection values, boolean includeEmpty) {
    String[] fieldNames = null;
    boolean addCols = false;
    if(cols != null && cols.size() > 0) {
      fieldNames = (String[]) cols.toArray(new String[0]);
    } // end of if
    else {
      fieldNames = StringUtils.splitString(ORMInfoManager.getPlainDBFieldNames(clazz.getName()), sqlConstants.getFieldsSeparator());
      addCols = true;
    } // end of else
    Object value = null;
    Method getter = null;
    String wrapperPrefix = null;
    String wrapperSuffix = null;
    try {
      for (int i = 0; i < fieldNames.length; i++) {
        DBField dbField = ORMInfoManager.getDBField(clazz.getName(), fieldNames[i]);
        getter = dbField.getGetterMethod();
        value = Utility.invokeMethod(pojo, getter);
        wrapperPrefix = wrapperSuffix = "";
        if(value instanceof String) {
          wrapperPrefix = wrapperSuffix = sqlConstants.getStringWrapper();
          if(value != null && "".equals(value.toString())) {
            value = null;
          } // end of if
          else {
            value = StringUtils.replaceCharacter((String)value, '\'', '`');
          } // end of else
        } // end of if
        else if(value instanceof Integer || value instanceof Long) {
          wrapperPrefix = wrapperSuffix = sqlConstants.getNumberWrapper();
          if(value != null && "0".equals(value.toString())) {
            value = null;
          } // end of if
        } // end of if
        else if(value instanceof Timestamp) {
          wrapperPrefix = sqlConstants.getDateWrapperPrefix();
          wrapperSuffix = sqlConstants.getDateWrapperSuffix();
          value = DateUtils.getDateToString((Timestamp)value, ORMInfoManager.TIMESTAMP_FORMAT);
        } // end of if
        else if(value instanceof Float || value instanceof Double) {
          wrapperPrefix = wrapperSuffix = sqlConstants.getNumberWrapper();
          if(value != null && "0.0".equals(value.toString())) {
            value = null;
          } // end of if
        } // end of if
        else if(value instanceof java.sql.Time) {
          wrapperPrefix = sqlConstants.getDateWrapperPrefix();
          wrapperSuffix = sqlConstants.getDateWrapperSuffix();
          value = DateUtils.getDateToString((java.sql.Time)value, ORMInfoManager.TIME_FORMAT);
        } // end of if
        else if(value instanceof java.sql.Date || value instanceof java.util.Date) {
          wrapperPrefix = sqlConstants.getDateWrapperPrefix();
          wrapperSuffix = sqlConstants.getDateWrapperSuffix();
          value = DateUtils.getDateToString((java.util.Date)value, ORMInfoManager.DATE_FORMAT);
        } // end of if
        if(value != null || includeEmpty == true) {
          if(value == null) {
            value = sqlConstants.getOpNull();
            wrapperPrefix = wrapperSuffix = "";
          } // end of if
          values.add(wrapperPrefix + value + wrapperSuffix);
          if(addCols) {
            cols.add(fieldNames[i]);
          } // end of if
        } // end of if
      } // end of for
    } // end of try
    catch(Exception ee) {
      logger.warning(Utility.getStackTrace(ee));
      values.clear();
    } // end of catch
  } // end of method extractValues
  
  /**
   * Returns <code>Object</code> array having <code>num</code> objects in it.
   * <p>It appends empty <code>String</code> objects in the array if length of
   * given array is less than <code>num</code>.</p>
   * 
   * @param args Object array whose length is to be increased to
   * <code>num</code> with empty string objects.
   * @param num Required length of the given array.
   * 
   * @return Array of length <code>num</code> with empty string objects appended
   * to given array.
   */
  protected Object[] getEmptyStrings(Object[] args, int num) {
    int arraySize = (args == null) ? 0 : args.length;
    if(num < 1) {
      return null;
    } // end of if
    if(num >= args.length) {
      return args;
    } // end of if
    Vector vec = new Vector(java.util.Arrays.asList(args));
    int i = arraySize;
    for (; i < num; i++) {
        vec.addElement("");
    } // end of for
    return (Object[])vec.toArray(new Object[0]);
  } // end of method getEmptyStrings

  /**
   * Executes the given <code>qry</code> on the connection object taken from
   * {@link AbstractPool} class and returns a {@link Vector} object
   * containing records.
   * <p>Each record is an array of {@link Object} class. First element in the
   * returned object is array of {@link String} containing names of the columns.
   * </p>
   * 
   * @param qry Select query to be executed
   * 
   * @return Records found against the given query in the form of array of
   * {@link String} class stored in a {@link Collection} object.
   * 
   * @throws ORMException In case of incorrect query or any other errors.
   */
  public Vector<Object[]> executeSelectQuery(String qry, Connection con)
      throws ORMException {
    
    logger.finest("Entering - Time : " + System.currentTimeMillis());
    Vector<Object[]> results = new Vector<Object[]>();
    boolean isNewConnection = false;
    Statement stmt = null;
    try {
      if(con == null || con.isClosed()) {
        con = getConnection();
        isNewConnection = true;
      } // end of if
      stmt = con.createStatement();
      ResultSet rst = stmt.executeQuery(qry);
      ResultSetMetaData rsmd = rst.getMetaData();
      int columnCount = rsmd.getColumnCount();
      String[] fields = new String[columnCount];
      for(int i = 0; i < columnCount; i++) {
        fields[i] = rsmd.getColumnName(i + 1);
      } // end of for
      results.add(fields);
      Object[] values = null;
      while(rst.next()) {
        values = new Object[columnCount];
        for(int i = 0; i < columnCount; i++) {
          values[i] = rst.getObject(i + 1);
        } // end of for
        results.add(values);
      } // end of while
      rst.close();
    } // end of try
    catch(SQLException sqle) {
      throw exceptionUtil.getSelectException(sqle.getMessage(), sqle);
    } // end of catch
    finally {
      try {
        stmt.close();
      } // end of try
      catch(Exception e) {}
      if(isNewConnection) {
        closeConnection(con);
      } // end of if
    } // end of finally
    logger.finest("Leaving - Time : " + System.currentTimeMillis());
    return results;
  } // end of  method executeSelectQuery
  
  /**
   * {@inheritDoc}
   */
  public <T> T fillObject(T pojo, Connection con)
      throws ORMException {

    boolean isNewConnection = false;
    Statement stmt = null;
    try {
      if(con == null || con.isClosed()) {
        con = getConnection();
        isNewConnection = true;
      } // end of if
      stmt = con.createStatement();
      String qry = createSelectQuery(pojo, ORMInfoManager.SUPER_LEVEL);
      logger.info(qry + ";");
      ResultSet rst = stmt.executeQuery(qry);
      if(rst.next()) {
        fillObject(pojo, rst);
      } // end of if
      else {
        pojo = null;
      } // end of else
      rst.close();
    } // end of try
    catch(SQLException sqle) {
      throw ExceptionUtil.getFillObjectException(sqle.getMessage(), sqle);
    } // end of catch
    finally {
      try {
        stmt.close();
      } // end of try
      catch(Exception e) {}
      if(isNewConnection) {
        closeConnection(con);
      } // end of if
    } // end of finally
    return pojo;
  } // end of  method fillObject
  
  protected <T> void fillObjects(Collection<T> results, T pojo, ResultSet rst) throws SQLException {
    logger.finest("Entering - Time : " + System.currentTimeMillis());
    List<InstanceMember> instanceMembers = ORMInfoManager.getInstanceMembers(pojo);
    logger.finest("Retrieved fields of " + pojo.getClass().getName() + " - Time : " + System.currentTimeMillis());
    if(instanceMembers == null || instanceMembers.size() < 1) {
      return;
    } // end of if
    // TODO write code to fill super object instance members
    instanceMembers = getMatchingInstanceMembers(rst.getMetaData(), pojo.getClass().getName());
    while(rst.next()) {
      T result = (T)ORMInfoManager.instantiate(pojo.getClass().getName());
      fillObject(result, instanceMembers, rst);
      results.add(result);
    }
    logger.finest("Leaving - Time : " + System.currentTimeMillis());
  }
  /**
   * {@inheritDoc}
   */
  protected void fillObject(Object pojo, ResultSet rst) throws SQLException {
    logger.finest("Entering - Time : " + System.currentTimeMillis());
    List<InstanceMember> instanceMembers = null; //ORMInfoManager.getInstanceMembers(pojo);
//    if(instanceMembers == null || instanceMembers.size() < 1) {
//      return;
//    } // end of if
    Class pojoClass = pojo.getClass();
    for (int i = 0; i < ORMInfoManager.SUPER_LEVEL; i++) {
      Class superClass = ORMInfoManager.getSuperClass(pojoClass);
      if(superClass == null || Modifier.isAbstract(superClass.getModifiers())) {
        break;
      }
      instanceMembers = getMatchingInstanceMembers(rst.getMetaData(), superClass.getName());
      logger.finest("Retrieved instance members of " + superClass.getName() + " - Time : " + System.currentTimeMillis());
      logger.finest("About to fill " + superClass.getName() + " members. - Time : " + System.currentTimeMillis());
      fillObject(pojo, instanceMembers, rst);
      pojoClass = superClass;
    }
    instanceMembers = getMatchingInstanceMembers(rst.getMetaData(), pojo.getClass().getName());
    logger.finest("Retrieved instance members of " + pojo.getClass().getName() + " - Time : " + System.currentTimeMillis());
    logger.finest("About to fill " + pojo.getClass().getName() + " members. - Time : " + System.currentTimeMillis());
    fillObject(pojo, instanceMembers, rst);
    logger.finest("Leaving - Time : " + System.currentTimeMillis());
  } // end of method fillObject
  
  private void fillObject(Object pojo, List<InstanceMember> instanceMembers, ResultSet rst) throws SQLException {
    Object value = null;
    for (InstanceMember instanceMember : instanceMembers) {
      value = null;
      try {
        value = rst.getObject(instanceMember.getInstanceMemberName());
        Utility.invokeMethod(pojo, instanceMember.getSetterMethod(), new Object[] {value});
      } catch(Exception eee) {
        logger.warning("Could not set value [" + instanceMember.getInstanceMemberName() + " : " + value + "] in [" + instanceMember.getSetterMethod().getName() + "] method in [" + pojo.getClass().getName() + "] object.");
        StackTraceElement rootCause = eee.getStackTrace()[eee.getStackTrace().length - 1];
        logger.warning(rootCause.getClassName() + " thrown with message [" + eee.getMessage() + "].");
        logger.warning(rootCause.toString());
        ;
      } // end of catch
    } // end of for
  } // end of method fillObject
  
  /**
   * Returns the {@link List| of {@link DBField} objects whose name is found in
   * the columns list in the given <code>metaData</code> object.
   * <p>It improves efficiency in {@link #fillObject(Object, ResultSet)} method
   * by avoiding unnecessary exceptions instantiation.</p>
   * 
   * @param metaData {@link ResultSetMetaData} object containing the column
   * names.
   * @param dbFields {@link List} of {@link DBField} objects representing all
   * the mapped (to database) fields on a particular class.
   * 
   * @return List of {@link DBField} objects whose corresponding columns are
   * found in the given <code>metaData</code> object.
   * 
   * @throws SQLException In case of any underlying errors.
   */
  private List<InstanceMember> getMatchingInstanceMembers(ResultSetMetaData metaData,
      String className) throws SQLException {

    List<InstanceMember> instanceMembers = ORMInfoManager.getInstanceMembers(className);

    if(instanceMembers == null || instanceMembers.size() < 1) {
      return instanceMembers;
    } // end of if
    if(metaData == null || metaData.getColumnCount() < 1) {
      return new ArrayList<InstanceMember>();
    } // end of if
    
    Map<String, String> columnNames = new TreeMap<String, String>();
    for (int i = 0; i < metaData.getColumnCount(); i++) {
      columnNames.put(metaData.getColumnLabel(i + 1), "");
    } // end of for
    
    List<InstanceMember> returnedInstanceMembers = new ArrayList<InstanceMember>();
    for (InstanceMember instanceMember : instanceMembers) {
      if(columnNames.containsKey(instanceMember.getInstanceMemberName())) {
        returnedInstanceMembers.add(instanceMember);
      } // end of if
    } // end of for
    return returnedInstanceMembers;
  } // end of method getMatchingInstanceMembers
  
  protected <T> Collection<T> executeQuery(String qry, Class<T> clazz, Object pojo, Connection con)
      throws SQLException {

    boolean isNewConnection = false;
    Collection<T> affectedObjects;
    Statement stmt = null;
    try {
      if(con == null || con.isClosed()) {
        con = getConnection();
        isNewConnection = true;
      } // end of if
      stmt = con.createStatement();
      ResultSet rst = stmt.executeQuery(qry);
      affectedObjects = new ArrayList<T>();
      T resultObject = (T)ORMInfoManager.instantiate(clazz.getName());
      fillObjects(affectedObjects, resultObject, rst);
    } // end of try
    catch(SQLException sqle) {
      throw sqle;
    } // end of catch
    finally {
      try {
        stmt.close();
      } // end of try
      catch(Exception e) {}
      if(isNewConnection) {
        closeConnection(con);
      } // end of if connection was created locally
    } // end of finally

    return affectedObjects;
  } // end of  method executeUpdateQuery
  
  public String createUnionQuery(String[] queries) {
    StringBuffer qry = new StringBuffer();
    for (int i = 0; i < queries.length; i++) {
      if(qry.toString().trim().length() > 0) {
        qry.append(sqlConstants.getClauseUnion());
      } // end of if
      qry.append(queries[i]);
    } // end of for

    return qry.toString().trim();
  }

  public <T> Collection<T> executeUnionQuery(Object pojo, String[] queries, Connection con) throws SQLException {
    String sql = createUnionQuery(queries);
    logger.info(sql.toString() + ";");
    return (Collection<T>)this.executeQuery(sql, pojo.getClass(), pojo, con);
  }

  protected int executeUpdate(String qry, Object pojo, Connection con)
      throws SQLException {

    boolean isNewConnection = false;
    Statement stmt = null;
    int affectedCount = 0;
    try {
      if(con == null || con.isClosed()) {
        con = getConnection();
        isNewConnection = true;
      } // end of if
      stmt = con.createStatement();
      affectedCount = stmt.executeUpdate(qry);
    } // end of try
    catch(SQLException sqle) {
      throw sqle;
    } // end of catch
    finally {
      try {
        stmt.close();
      } // end of try
      catch(Exception e) {}
      if(isNewConnection) {
        closeConnection(con);
      } // end of if connection was created locally
    } // end of finally

    return affectedCount;
  } // end of  method executeUpdateQuery
  
  /**
   * {@inheritDoc}
   */
  public void fillContainersMulti(Object[] containerObjs, 
      Object[] containedObjs, DBField containerRelatedField,
      DBField containedRelatedField, ContainedObjectField containedObjField) {
    fillContainersMulti(containerObjs, containedObjs, containerRelatedField,
        containedRelatedField, containedObjField, false);
  } // end of method fillContainersMulti

  /**
   * Fills the given <code>containerObjs</code> with the relevant
   * <code>containedObjs</code> using the setter method of
   * <code>containedObjField</code> object in each of the
   * <code>containerObjs</code>.
   * 
   * @param containerObjs Objects whose <code>containerSetterMethod</code> is to
   * be invoked to set corresponding contained objects.
   * @param containedObjs Object to be contained in relevant container objects.
   * @param containerRelatedField {@link DBField} object whose getter method is
   * to be called to get the value of the related field of every object in
   * <code>containerObjs</code> to be matched against related field value
   * obtained by calling getter method on <code>containedRelatedField</code>
   * to decide which contained object(s) belong to which container object.
   * @param containedRelatedField {@link DBField} object whose getter method is
   * to be called to get the value of the related field of every object in
   * <code>containedObjs</code> to be matched against corresponding related
   * field value in container objects.
   * @param containedObjField {@link DBField} object whose setter method is to
   * be invoked for each of the <code>containerObjs</code> to populate
   * corresponding child records in the <code>containerObjs</code>.
   * @param exclusive If <code>true</code>, an object added to one
   * container shall not be added to another. Otherwise, one contained
   * object would be added to all the matching containers.
   */
  public void fillContainersMulti(Object[] containerObjs, 
      Object[] containedObjs, DBField containerRelatedField,
      DBField containedRelatedField, ContainedObjectField containedObjField, boolean exclusive) {
    if (containerObjs == null || containerObjs.length < 1) {
      return;
    } // end of if
    
    Method containerRelatedFieldGetter = containerRelatedField.getGetterMethod();
    Method containedRelatedFieldGetter = containedRelatedField.getGetterMethod();
    Method containerSetterMethod = containedObjField.getSetterMethod();
    
    if (containedObjs == null || containedObjs.length < 1) {
      for (int i = 0; i < containerObjs.length; i++) {
        Utility.invokeMethod(containerObjs[i], containerSetterMethod, new Object[] {null});
      } // end of for;
      return;
    } // end of if

    Class setterParamType = containerSetterMethod.getParameterTypes()[0];
    
    // set method in the contained class for containing class if exists.
    ContainedObjectField containedParentObjectInChildObjectField = containedObjField.getReciprocalContainedObjectField();
    //ContainedObjectField containedParentObjectInChildObjectField = ORMInfoManager.getReciprocalContainedObjectField(containedObjField);
    Method containedObjSetMethod = null;
    if(containedParentObjectInChildObjectField != null) {
      containedObjSetMethod = containedParentObjectInChildObjectField.getSetterMethod();
    } // end of if
    
    Object containerObjValue = null;
    Object containedObjValue = null;
    Vector objectVec = new Vector(Arrays.asList(containedObjs));
    ArrayList matchedVec = null;
    for (int i = 0; i < containerObjs.length; i++) {
      if(containerObjs[i] == null) {
        continue;
      } // end of if
      matchedVec = new ArrayList();
      for (int j = 0; j < objectVec.size(); j++) {
        try {
          containedObjValue = Utility.invokeMethod(objectVec.elementAt(j), containedRelatedFieldGetter);
          containerObjValue = Utility.invokeMethod(containerObjs[i], containerRelatedFieldGetter);
          if (containerObjValue.equals(containedObjValue) == true) {
            matchedVec.add(objectVec.elementAt(j));
            if(containedObjSetMethod != null) {
              Utility.invokeMethod(objectVec.elementAt(j), containedObjSetMethod, new Object[] {containerObjs[i]});
            } // end of if
            if(exclusive) {
              objectVec.remove(j);
              j--;
            } // end of if
          } // end of if
        } // end of try
        catch (Exception ex) {
          ex.printStackTrace();
        } // end of catch
      } // end of for j
      if (matchedVec != null && matchedVec.size() > 0) {
        try {
          if(Utility.isChildClass(setterParamType, List.class)) {
            containerSetterMethod.invoke(containerObjs[i], new Object[] {matchedVec});
          } // end of if set method takes List type
          else if(setterParamType.equals(Vector.class)) {
            containerSetterMethod.invoke(containerObjs[i], new Object[] {new Vector(matchedVec)});
          } // end of if set method takes Vector type
          else if(setterParamType.equals(Set.class)) {
            containerSetterMethod.invoke(containerObjs[i], new Object[] {new HashSet(matchedVec)});
          } // end of if set method takes Vector type
          else if(Utility.isChildClass(setterParamType, Collection.class)) {
            containerSetterMethod.invoke(containerObjs[i], new Object[] {matchedVec});
          } // end of if set method takes Vector type
          else if(setterParamType.isArray()) {
            containerSetterMethod.invoke(containerObjs[i], new Object[] {matchedVec.toArray(new Object[0])});
          } // end of if
        } catch (Exception ee) {
          logger.warning(Utility.getStackTrace(ee));
        } // end of catch
      } // end of if
    } // end of for i
  } // end of method fillContainersMulti

  /**
   * {@inheritDoc}
   */
  public void fillContainersSingle(Object[] containerObjs,  Object[] containedObjs,
      DBField containerRelatedField, DBField containedRelatedField,
      ContainedObjectField containedObjField) {
    
    fillContainersSingle(containerObjs, containedObjs, containerRelatedField,
        containedRelatedField, containedObjField, false);
  } // end of method fillContainersSingle
  
  /**
   * Fills the given <code>containedObjs</code> in the corresponding (where the
   * value of the related instance members match) <code>containerObjs</code>
   * using the setter method of <code>containedObjField</code> object.
   * 
   * @param containerObjs Objects whose <code>containerSetterMethod</code> is to
   * be invoked to set corresponding contained objects in them.
   * @param containedObjs Object to be contained in the relevant
   * <code>containerObjs</code>.
   * @param containerRelatedField {@link DBField} object whose getter method is
   * to be called to get the value of the related field of every object in
   * <code>containerObjs</code> to be matched against related field value
   * obtained by calling getter method on <code>containedRelatedField</code>
   * to decide which contained object belong to which container object.
   * @param containedRelatedField {@link DBField} object whose getter method is
   * to be called to get the value of the related field of every object in
   * <code>containedObjs</code> to be matched against corresponding related
   * field value in container objects.
   * @param containedObjField {@link DBField} object whose setter method is to
   * be invoked for each of the <code>containerObjs</code> to populate
   * corresponding child record in the <code>containerObjs</code>.
   * @param exclusive If <code>true</code>, an object added to one container
   * shall not be added to another. Otherwise, one contained object would be
   * added to all the matching containers.
   */
  public void fillContainersSingle(Object[] containerObjs, 
      Object[] containedObjs,
      DBField containerRelatedField, DBField containedRelatedField, 
      ContainedObjectField containedObjField, boolean exclusive) {
    if (containerObjs == null || containerObjs.length < 1) {
      return;
    } // end of if
    
    Method containerRelatedFieldGetter = containerRelatedField.getGetterMethod();
    Method containedRelatedFieldGetter = containedRelatedField.getGetterMethod();
    Method setMethod = containedObjField.getSetterMethod();
    if (containedObjs == null || containedObjs.length < 1) {
      for (int i = 0; i < containerObjs.length; i++) {
        try {
          setMethod.invoke(containerObjs[i], new Object[] {null});
        } catch (Exception ee) {
          logger.fine(Utility.getStackTrace(ee));
        } // end of catch
      } // end of for;
      return;
    } // end of if

    Object containerObjValue = null;
    Object containedObjValue = null;
    Vector objectVec = new Vector(Arrays.asList(containedObjs));
    for (int i = 0; i < containerObjs.length; i++) {
      if(containerObjs[i] == null) {
        continue;
      } // end of if
      for (int j = 0; j < objectVec.size(); j++) {
        try {
          containedObjValue = containedRelatedFieldGetter.invoke(objectVec.elementAt(j), new Object[0]);
          containerObjValue = containerRelatedFieldGetter.invoke(containerObjs[i], new Object[0]);
          if (containerObjValue.equals(containedObjValue) == true) {
            try {
              // set contained object in container object.
              logger.info(containerObjs[i].getClass().getName() + "." + setMethod.getName() + " > " + objectVec.elementAt(j).getClass().getName());
              logger.info(objectVec.elementAt(j).toString());
              Method setMethodRT = containerObjs[i].getClass().getMethod(setMethod.getName(), new Class[] {objectVec.elementAt(j).getClass()});
              setMethodRT.invoke(containerObjs[i], objectVec.elementAt(j));

              // set container object in contained object if corresponding setter method exists.
              ContainedObjectField reciprocalContainedObjectField = containedObjField.getReciprocalContainedObjectField();
              if(reciprocalContainedObjectField != null) {
                reciprocalContainedObjectField.getSetterMethod().invoke(objectVec.elementAt(j), new Object[] {containerObjs[i]});
              } // end of if
              
            } catch (Exception ee) {
              logger.fine(Utility.getStackTrace(ee));
            } // end of catch
            
            
            if(exclusive) {
              objectVec.remove(j);
              j--;
            } // end of if
            break;
          } // end of if
        } // end of try
        catch (Exception ex) {
          logger.fine(Utility.getStackTrace(ex));
        } // end of catch
      } // end of for j
    } // end of for i
  } // end of method fillContainersSingle

  /**
   * {@inheritDoc}
   */
  public void fillContainedObjectsDepthFirst(Object[] containers, int level, Connection con)
      throws ORMException {
    fillContainedObjectsDepthFirst(containers, null, level, con);
  } // end of method fillContainedObjectsDepthFirst
  
  /**
   * {@inheritDoc}
   */
  public void fillContainedObjectsDepthFirst(Object[] containers,
      String[] includes, int level, Connection con)
      throws ORMException {

    Map inclusions = null;
    if(includes != null && includes.length > 0) {
      inclusions = new TreeMap();
      for (int i = 0; i < includes.length; i++) {
        inclusions.put(includes[i], "");
      } // end of for
    } // end of if
    fillContainedObjects(containers, inclusions, level, con, new TreeMap());
  } // end of method fillContainedObjectsDepthFirst
  
  /**
   * {@inheritDoc}
   */
  public void fillContainedObjects(Object[] containers, int level, Connection con)
      throws ORMException {
    
    fillContainedObjects(containers, null, level, con);
  } // end of method fillContainedObjects
  
  /**
   * {@inheritDoc}
   */
  public void fillContainedObjects(Object[] containers, String[] includes, int level, Connection con)
      throws ORMException {
    if(containers == null || containers.length < 1) {
      return;
    } // end of if
    Map inclusions = null;
    if(includes != null && includes.length > 0) {
      inclusions = new TreeMap();
      for (int i = 0; i < includes.length; i++) {
        inclusions.put(includes[i], "");
      } // end of for
    } // end of if
    Map done = new TreeMap();
    Vector pojoVec = new Vector();
    pojoVec.add(containers);
    Collection tmpDvoVec = null;
    Vector containedDvosVec = new Vector();
    while(level > 0) {
      for (int i = 0; i < pojoVec.size(); i++) {
        tmpDvoVec = fillContainedObjects((Object[])pojoVec.get(i), inclusions, 1, con, done);
        for (Object object : tmpDvoVec) {
          containedDvosVec.add((Object[])object);
        }
      } // end of for
      pojoVec = containedDvosVec;
      containedDvosVec = new Vector();
      level--;
    } // end of while
  } // end of method fillContainedObjects
  
  /**
   * Fills the objects of types contained by class of given
   * <code>containers</code> to the given <code>level</code> using the given
   * connection in depth first manner.
   * <p>Note: All the objects in the given <code>containers</code> shall be of
   * the same specific class because this method does not execute query for each
   * object. Rather it executes one query for all the objects in the given
   * array.</p>
   * 
   * @param containers Array of objects whose contained objects are to be filled
   * in.
   * @param includes Names of getter methods, of contained beans, in the
   * containing classes, only whose respective contained objects are to be
   * filled.
   * @param level Level to which objects are to be filled. Zero means do not
   * fill contained objects. 1 mean fill contained objects in the given
   * <code>containers</code> and return.
   * @param con Connection object to be used for querying. If the given object
   * is <code>null</code> or closed, a new connection object is created and
   * queries are executed through that object.
   * @param fillMap Map object to contain method names which have been processed
   * to avoid circular execution.
   * 
   * @throws ORMException In case of any database related errors.
   */
  protected Collection fillContainedObjects(Object[] containers, Map inclusions, int level, Connection con, Map fillMap)
      throws ORMException {

    logger.finest("Entering - Time : " + System.currentTimeMillis());
    /**
     * 1. Get contained object names whether single or multiple
     * 2. For each type, call fillContainers<Multi/Single> methods.
     * 3. Call this method recursively for the objects in turn with
     * level value decremented.
     */
    if(level < 1) {
      fillMap = new TreeMap();
      return new Vector();
    } // end of if
    if(containers == null || containers.length < 1) {
      fillMap = new TreeMap();
      return new Vector();
    } // end of if

    List<ContainedObjectField> containerObjFields = ORMInfoManager.getContainedObjectFields(containers[0]);
    logger.finest("Count of containerObjGetters is : " + containerObjFields.size());
    if(containerObjFields == null || containerObjFields.size() < 1) {
      fillMap = new TreeMap();
      return new Vector();
    } // end of if
    String containerClassName = containers[0].getClass().getName();
    String sortBy = null;
    
    Collection containedObjs = null;
    Vector containedObjArrays = new Vector();
    List fieldValues = null;
    Object containerRelatedFieldValue = null;
    
    Object pojo = null;
    SQLCriterion[] criteria = new SQLCriterion[1];
    Integer storedLevel = null;
    DBField containerField;
    DBField containedField;

    /**
     * True when the container object is parent of the contained object.
     * False otherwise.
     */
    boolean isParent = true;
    
    for (ContainedObjectField containedObjField : containerObjFields) {
      logger.finer("Retrieving contained field for reference entity [" + containedObjField.getReferencedEntity() + "] and reference field [" + containedObjField.getReferencedField() + "] in class [" + containedObjField.getClassName() + "]");
      containedField = ORMInfoManager.getDBField(containedObjField.getContainedObjectType(), containedObjField.getReferencedField());
      if(containedField == null) {
        ORMInfoManager.getPrimaryKeyName(ORMInfoManager.instantiate(containedObjField.getContainedObjectType()));
        containedField = ORMInfoManager.getDBField(containedObjField.getContainedObjectType(), containedObjField.getReferencedField());
      } // end of if
      logger.finer("Contained field for reference entity [" + containedObjField.getReferencedEntity() + "] and reference field [" + containedObjField.getReferencedField() + "] is [" + containedField.getInstanceMemberName() + "]");
      sortBy = ORMInfoManager.getQualifiedField(containedField.getEntityName(), containedField.getDbFieldName(), true);
      containerField = ORMInfoManager.getFieldByInstanceMemberName(containers[0], containedObjField.getRelatedInstanceMemberName());
      if(containerField != null) {
        isParent = false;
      }
      else {
        containerField = ORMInfoManager.getPrimaryKeyField(containers[0]);
        isParent = true;
      }
      containerRelatedFieldValue = Utility.invokeMethod(containers[0], containerField.getGetterMethod());

      if(inclusions != null && inclusions.containsKey(containedObjField.getGetterMethod().getName()) == false) {
        continue;
      } // end of if
      Class setterParamType = containedObjField.getSetterMethod().getParameterTypes()[0];
      try {
        pojo = containedField.getPojo();
        
        storedLevel = (Integer)fillMap.get(containerClassName + "." + containedObjField.getGetterMethod().getName() + "-Level");
        logger.finest(containerClassName + "." + containedObjField.getGetterMethod().getName() + "-Level : " + storedLevel);
        if(fillMap.containsKey(containerClassName + "." + containedObjField.getGetterMethod().getName()) && storedLevel != null && level >= storedLevel.intValue()) {
          logger.finest("As the " + containerClassName + "." + containedObjField.getGetterMethod().getName() + " has been found at level : " + storedLevel + ", hence skipping.");
          continue;
        } // end of if

        fieldValues = Utility.getFieldValues(containers, containerField.getGetterMethod());
        
        criteria[0] = criteronFactory.createIn(containedField.getDbFieldName(), containedField.getEntityName(), fieldValues, criteronFactory.getType(containerRelatedFieldValue), null, null);

        logger.finest("About to search contained objects - Time : " + System.currentTimeMillis());
        containedObjs = search(pojo, null, criteria, BOOLEAN_OPERATOR.OR, sortBy, -1, -1, con);
        logger.finest("Searched contained objects - Time : " + System.currentTimeMillis());
        containedObjArrays.add((Object[])containedObjs.toArray(new Object[0]));
        
        if(Utility.isChildClass(setterParamType, Collection.class) || setterParamType.isArray()) {
          fillContainersMulti(containers, (Object[])containedObjs.toArray(new Object[0]), containerField, containedField, containedObjField);
        } // end of if
        else {
          fillContainersSingle(containers, (Object[])containedObjs.toArray(new Object[0]), containerField, containedField, containedObjField);
        } // end of if
        
        logger.finest("Filled contained objects in objects of type [" + containers[0].getClass().getName() + "] - Time : " + System.currentTimeMillis());
        
        fillMap.put(containerClassName + "." + containedObjField.getGetterMethod().getName(), "true");
        fillMap.put(containerClassName + "." + containedObjField.getGetterMethod().getName() + "-Level", level);
        logger.finest("putting " + containerClassName + "." + containedObjField.getGetterMethod().getName() + " in fillMap.");
        logger.finest("putting " + containerClassName + "." + containedObjField.getGetterMethod().getName() + "-Level : " + level + " in fillMap.");
        
        // makes sure that query is not executed for reciprocal contained objects.
        ContainedObjectField reciprocalContainedObjectField = containedObjField.getReciprocalContainedObjectField();
        if(reciprocalContainedObjectField != null) {
          fillMap.put(reciprocalContainedObjectField.getClassName() + "." + reciprocalContainedObjectField.getGetterMethod().getName(), "true");
          fillMap.put(reciprocalContainedObjectField.getClassName() + "." + reciprocalContainedObjectField.getGetterMethod().getName() + "-Level", level - 1);
          
          logger.finest("putting " + reciprocalContainedObjectField.getClassName() + "." + reciprocalContainedObjectField.getGetterMethod().getName() + " in fillMap.");
          logger.finest("putting " + reciprocalContainedObjectField.getClassName() + "." + reciprocalContainedObjectField.getGetterMethod().getName() + "-Level : " + (level - 1) + " in fillMap.");
        } // end of if
        
        
        fillContainedObjects((Object[])containedObjs.toArray(new Object[0]), inclusions, level - 1, con, fillMap);
      } catch(Exception ee) {
        logger.warning(Utility.getStackTrace(ee));
      } // end of catch
    } // end of for
    logger.finest("Leaving - Time : " + System.currentTimeMillis());
    return containedObjArrays;
  } // end of  method fillContainedObject
  
  
  protected Join getSimpleJoin(String lhsTableAlias, String rhsTableName, String rhsTableAlias, String lhsFieldName, String rhsFieldName, int joinType) {
    FromElement rhs = new FromElement();
    rhs.setTableName(rhsTableName);
    rhs.setAlias(rhsTableAlias);
    SQLCriterion[] joinCriteria = new SQLCriterion[1];
    joinCriteria[0] = criteronFactory.createColumnComparison(lhsFieldName, lhsTableAlias, SQLCriterion.EQUAL_TO, rhsFieldName, rhsTableAlias);
    return new Join(null, rhs, joinType, joinCriteria, BOOLEAN_OPERATOR.AND);
  }
  
  protected Join getSimpleInnerJoin(String lhsTableAlias, String rhsTableName, String rhsTableAlias, String lhsFieldName, String rhsFieldName) {
    return getSimpleJoin(lhsTableAlias, rhsTableName, rhsTableAlias, lhsFieldName, rhsFieldName, Join.JOIN_TYPE_INNER);
  }
  
  protected Join getSimpleLeftJoin(String lhsTableAlias, String rhsTableName, String rhsTableAlias, String lhsFieldName, String rhsFieldName) {
    return getSimpleJoin(lhsTableAlias, rhsTableName, rhsTableAlias, lhsFieldName, rhsFieldName, Join.JOIN_TYPE_LEFT_OUTER);
  }
  
} // end of class AbstractDAO
