package com.sa.orm;

import java.lang.reflect.InvocationTargetException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Collection;
import java.util.List;

import com.sa.orm.bean.PagingVO;
import com.sa.orm.bean.SearchRequest;
import com.sa.orm.util.ContainedObjectField;
import com.sa.orm.util.DBField;
import com.sa.orm.SQLCriterion.BOOLEAN_OPERATOR;

/**
 * <h4>Description</h4>Provides template for ORM DAO implementations.
 * <p>Business logic classes use object of this class do all the database
 * interaction including inserting, updating, deleting and retrieving data from
 * the database.</p>
 * 
 * <h4>Copyrights</h4>Copyrights &copy; 2008 - 2012 Soft Assets.
 * All Rights Reserved.
 * 
 * <h4>Company</h4>Soft Assets
 * <h4>Created</h4>October 27, 2010
 * @author Ali Ahsan
 * @version 1.1
 */
public interface DAO {

  /**
   * Returns a filled object of the type of the given <code>pojo</code>
   * object based on the value of primary key set in it.
   * 
   * @param <T> Type of the POJO
   * @param pojo Object having primary key value set and whose clone
   * filled with the database record is to be returned.
   * @param con Connection object to be used to run the query. If
   * <code>null</code>, a new connection is created internally.
   * @return Object of the given type, filled with a database record,
   * found based on the value of the primary key set in the given
   * object.
   * @throws ORMException In case no record is found or
   * any other database related errors.
   */
  public <T> T getById(T pojo, Connection con)
      throws ORMException;

  /**
   * Returns a filled object of the type of the given <code>pojo</code>
   * object based on the specified primary key value.
   * 
   * @param <T> Type of the POJO
   * @param pojo Object representing the database table
   * @param pkValue Value of the primary key field.
   * @param con Connection object to be used to run the query. If
   * <code>null</code>, a new connection is created internally.
   * @return Object of the given type, filled with a database record,
   * found based on the specified primary key value.
   * @throws ORMException In case no record is found or
   * any other database related errors.
   */
  public <T> T getById(T pojo, Object pkValue, Connection con)
      throws ORMException;

  /**
   * Returns a filled object of the type of the given <code>pojo</code>
   * object based on the value of primary key set in it, with only specified
   * fields populated.
   * 
   * @param <T> Type of the POJO
   * @param pojo Object having primary key value set and whose clone
   * filled with the database record is to be returned.
   * @param fields Fields to be populated in the returned object.
   * @param con Connection object to be used to run the query. If
   * <code>null</code>, a new connection is created internally.
   * @return Object of the given type, filled with a database record,
   * found based on the value of the primary key set in the given
   * object with only the specified fields populated.
   * @throws ORMException In case no record is found or any other
   * database related errors.
   */
  public <T> T getById(T pojo, String[] fields, Connection con)
      throws ORMException;

  /**
   * Searches the table, represented by given <code>pojo</code> object
   * against the given <code>criteria</code> and returns records, in
   * the form of representative objects in a {@link Collection}
   * object.
   * If <code>fields</code> is <code>null</code> or zero-length, all
   * the table fields are retrieved and filled in the returned
   * objects. Otherwise, only the given fields are retrieved and set.
   * 
   * @param <T> Type of the POJO
   * @param pojo Object representing a database table which is to be
   * searched.
   * @param fields Fields to be retrieved from database and their
   * values to be set in the returned objects.
   * @param criteria Criteria to make <code>WHERE</code> clause of
   * the database query to search specific results. If
   * <code>null</code>, all the table records are searched.
   * @param booleanOperator Database <code>AND</code> or <code>OR</code>
   * operator to be used between the criteria.
   * @param sortBy Field(s) to sort the returned records with.
   * @param limitStart First record to be fetched out of the matched
   * records. If less than 1, it is ignored.
   * @param limitSize Number of records to be returned, optionally
   * starting from limitStart.
   * @param con Connection object to be used to run the query. If
   * <code>null</code>, a new connection is created internally.
   * @return Collection having objects of type <code>pojo</code> which
   * matched the given <code>criteria</code> with given or all field
   * values filled.
   * @throws ORMException In case of any database or other errors.
   */
  public <T> Collection<T> search(T pojo, String[] fields,
      SQLCriterion[] criteria, BOOLEAN_OPERATOR booleanOperator, String sortBy, 
      int limitStart, int limitSize, Connection con)
      throws ORMException;

  /**
   * Searches the table, represented by given <code>pojo</code> object
   * against the given <code>criteria</code> and returns records, in
   * the form of representative objects in a {@link Collection}
   * object.
   * If <code>fields</code> is <code>null</code> or zero-length, all
   * the table fields are retrieved and filled in the returned
   * objects. Otherwise, only the given fields are retrieved and set.
   * 
   * @param <T> Type of the POJO
   * @param pojo Object representing a database table which is to be
   * searched.
   * @param fields Fields to be retrieved from database and their
   * values to be set in the returned objects.
   * @param criteria Criteria to make <code>WHERE</code> clause of
   * the database query to search specific results. If
   * <code>null</code>, all the table records are searched.
   * @param booleanOperator Database <code>AND</code> or <code>OR</code>
   * operator to be used between the criteria.
   * @param sortBy Field(s) to sort the returned records with.
   * @param limitStart First record to be fetched out of the matched
   * records. If less than 1, it is ignored.
   * @param limitSize Number of records to be returned, optionally
   * starting from limitStart.
   * @param joins List of {@link Join} objects to be used in the query.
   * @param con Connection object to be used to run the query. If
   * <code>null</code>, a new connection is created internally.
   * @return Collection having objects of type <code>pojo</code> which
   * matched the given <code>criteria</code> with given or all field
   * values filled.
   * @throws ORMException In case of any database or other errors.
   */
  public <T> Collection<T> search(T pojo, String[] fields,
      SQLCriterion[] criteria, BOOLEAN_OPERATOR booleanOperator, String sortBy, 
      int limitStart, int limitSize, List<Join> joins, Connection con)
      throws ORMException;
  
  /**
   * Returns a {@link Collection} object containing records retrieved by
   * executing a <code>GROUP BY</code> SQL query on the entity represented by
   * <code>pojo</code>.
   * <p>Each element in the object is an array of strings. First row contains
   * column names.</p>
   * 
   * @param pojo A POJO object representing a database entity.
   * @param fields Array of strings representing database fields which are to be
   * included in <code>SELECT</code> clause of the query.
   * @param functions SQL cumulative functions which make the query a
   * <code>GROUP BY</code> query.
   * @param criteria Array of {@link SQLCriterion} child classes to define
   * fields and their values to be compared to make the <code>WHERE</code>
   * clause portion of the query.
   * @param booleanOperator Database AND / OR operator used between the
   * <code>criteria</code>.
   * @param sortBy Name of the table field(s) by which the records are to be
   * sorted.
   * @param con Connection object to be used to run the query. If
   * <code>null</code>, a new connection is created internally.
   * @return Collection of string arrays each having a column value as per the
   * given <code>fields</code>. First element contains the captions, as given in
   * fields, of the columns.
   * @throws ORMException In case of any database errors.
   */
  public Collection<String[]> searchGroupBy(Object pojo, String[] fields,
      SQLFunction[] functions, SQLCriterion[] criteria, BOOLEAN_OPERATOR booleanOperator,
      String sortBy, Connection con)
    throws ORMException;

  /**
   * Returns a {@link Collection} object containing records retrieved by
   * executing a <code>GROUP BY</code> SQL query on the entity represented by
   * <code>pojo</code>.
   * <p>Each element in the object is an array of strings. First row contains
   * column names.</p>
   * 
   * @param pojo A POJO object representing a database entity.
   * @param fields Array of strings representing database fields which are to be
   * included in <code>SELECT</code> clause of the query.
   * @param functions SQL cumulative functions which make the query a
   * <code>GROUP BY</code> query.
   * @param criteria Array of {@link SQLCriterion} child classes to define
   * fields and their values to be compared to make the <code>WHERE</code>
   * clause portion of the query.
   * @param booleanOperator Database AND / OR operator used between the
   * <code>criteria</code>.
   * @param sortBy Name of the table field(s) by which the records are to be
   * sorted.
   * @param joins List of {@link Join} objects to be used in the query.
   * @param con Connection object to be used to run the query. If
   * <code>null</code>, a new connection is created internally.
   * @return Collection of string arrays each having a column value as per the
   * given <code>fields</code>. First element contains the captions, as given in
   * fields, of the columns.
   * @throws ORMException In case of any database errors.
   */
  public Collection<String[]> searchGroupBy(Object pojo, String[] fields,
      SQLFunction[] functions, SQLCriterion[] criteria, BOOLEAN_OPERATOR booleanOperator,
      String sortBy, List<Join> joins, Connection con)
    throws ORMException;

  /**
   * Returns a {@link Collection} object containing records retrieved by
   * executing a <code>GROUP BY</code> SQL query on the entity represented by
   * <code>pojo</code> with pagination support.
   * <p>Each element in the object is an array of strings. First row contains
   * column names.</p>
   * 
   * @param pojo A POJO object representing a database entity.
   * @param fields Array of strings representing database fields which are to be
   * included in <code>SELECT</code> clause of the query.
   * @param functions SQL cumulative functions which make the query a
   * <code>GROUP BY</code> query.
   * @param criteria Array of {@link SQLCriterion} child classes to define
   * fields and their values to be compared to make the <code>WHERE</code>
   * clause portion of the query.
   * @param booleanOperator Database AND / OR operator used between the
   * <code>criteria</code>.
   * @param sortBy Name of the table field(s) by which the records are to be
   * sorted.
   * @param limitStart First record number that is to be retrieved. If this
   * number is less than 1, it is not included in the SQL statement.
   * @param limitSize Number of records to be retrieved. Number less than one
   * means all records.
   * @param con Connection object to be used to run the query. If
   * <code>null</code>, a new connection is created internally.
   * @return Collection of string arrays each having a column value as per the
   * given <code>fields</code>. First element contains the captions, as given in
   * fields, of the columns.
   * @throws ORMException In case of any database errors.
   */
  public Collection<String[]> searchGroupBy(Object pojo, String[] fields,
      SQLFunction[] functions, SQLCriterion[] criteria, BOOLEAN_OPERATOR booleanOperator,
      String sortBy, int limitStart, int limitSize, Connection con)
    throws ORMException;

  /**
   * Returns a {@link Collection} object containing records retrieved by
   * executing a <code>GROUP BY</code> SQL query on the entity represented by
   * <code>pojo</code> with pagination support.
   * <p>Each element in the object is an array of strings. First row contains
   * column names.</p>
   * 
   * @param pojo A POJO object representing a database entity.
   * @param fields Array of strings representing database fields which are to be
   * included in <code>SELECT</code> clause of the query.
   * @param functions SQL cumulative functions which make the query a
   * <code>GROUP BY</code> query.
   * @param criteria Array of {@link SQLCriterion} child classes to define
   * fields and their values to be compared to make the <code>WHERE</code>
   * clause portion of the query.
   * @param booleanOperator Database AND / OR operator used between the
   * <code>criteria</code>.
   * @param sortBy Name of the table field(s) by which the records are to be
   * sorted.
   * @param limitStart First record number that is to be retrieved. If this
   * number is less than 1, it is not included in the SQL statement.
   * @param limitSize Number of records to be retrieved. Number less than one
   * means all records.
   * @param joins List of {@link Join} objects to be used in the query.
   * @param con Connection object to be used to run the query. If
   * <code>null</code>, a new connection is created internally.
   * @return Collection of string arrays each having a column value as per the
   * given <code>fields</code>. First element contains the captions, as given in
   * fields, of the columns.
   * @throws ORMException In case of any database errors.
   */
  public Collection<String[]> searchGroupBy(Object pojo, String[] fields,
      SQLFunction[] functions, SQLCriterion[] criteria, BOOLEAN_OPERATOR booleanOperator,
      String sortBy, int limitStart, int limitSize, List<Join> joins, Connection con)
    throws ORMException;

  /**
   * Returns a {@link Collection} object containing records retrieved by
   * executing a <code>GROUP BY</code> SQL query on the entity represented by
   * <code>pojo</code> including parent entities.
   * <p>Each element in the object is an array of strings. First row contains
   * column names.</p>
   * 
   * @param pojo A POJO object representing a database entity.
   * @param level Number of parent entities to be included in
   * <code>SELECT</code> and <code>FROM</code> clause of the SQL statement.
   * @param fields Array of strings representing database fields which are to be
   * included in <code>SELECT</code> clause of the query.
   * @param functions SQL cumulative functions which make the query a
   * <code>GROUP BY</code> query.
   * @param criteria Array of {@link SQLCriterion} child classes to define
   * fields and their values to be compared to make the <code>WHERE</code>
   * clause portion of the query.
   * @param booleanOperator Database AND / OR operator used between the
   * <code>criteria</code>.
   * @param sortBy Name of the table field(s) by which the records are to be
   * sorted.
   * @param limitStart First record number that is to be retrieved. If this
   * number is less than 1, it is not included in the SQL statement.
   * @param limitSize Number of records to be retrieved. Number less than one
   * means all records.
   * @param con Connection object to be used to run the query. If
   * <code>null</code>, a new connection is created internally.
   * @return Collection of string arrays each having a column value as per the
   * given <code>fields</code>. First element contains the captions, as given in
   * fields, of the columns.
   * @throws ORMException In case of any database errors.
   */
  public Collection<String[]> searchGroupBy(Object pojo, int level, String[] fields,
      SQLFunction[] functions, SQLCriterion[] criteria, BOOLEAN_OPERATOR booleanOperator,
      String sortBy, int limitStart, int limitSize, Connection con)
    throws ORMException;

  /**
   * Returns a {@link Collection} object containing records retrieved by
   * executing a <code>GROUP BY</code> SQL query on the entity represented by
   * <code>pojo</code> including parent entities.
   * <p>Each element in the object is an array of strings. First row contains
   * column names.</p>
   * 
   * @param pojo A POJO object representing a database entity.
   * @param level Number of parent entities to be included in
   * <code>SELECT</code> and <code>FROM</code> clause of the SQL statement.
   * @param fields Array of strings representing database fields which are to be
   * included in <code>SELECT</code> clause of the query.
   * @param functions SQL cumulative functions which make the query a
   * <code>GROUP BY</code> query.
   * @param criteria Array of {@link SQLCriterion} child classes to define
   * fields and their values to be compared to make the <code>WHERE</code>
   * clause portion of the query.
   * @param booleanOperator Database AND / OR operator used between the
   * <code>criteria</code>.
   * @param sortBy Name of the table field(s) by which the records are to be
   * sorted.
   * @param limitStart First record number that is to be retrieved. If this
   * number is less than 1, it is not included in the SQL statement.
   * @param limitSize Number of records to be retrieved. Number less than one
   * means all records.
   * @param joins List of {@link Join} objects to be used in the query.
   * @param con Connection object to be used to run the query. If
   * <code>null</code>, a new connection is created internally.
   * @return Collection of string arrays each having a column value as per the
   * given <code>fields</code>. First element contains the captions, as given in
   * fields, of the columns.
   * @throws ORMException In case of any database errors.
   */
  public Collection<String[]> searchGroupBy(Object pojo, int level, String[] fields,
      SQLFunction[] functions, SQLCriterion[] criteria, BOOLEAN_OPERATOR booleanOperator,
      String sortBy, int limitStart, int limitSize, List<Join> joins, Connection con)
    throws ORMException;

  /**
   * Returns an object containing required results, if found, along with total
   * number of records which satisfy the given <code>criteria</code>.
   * 
   * @param pojo Object representing a database entity, which is to be queried
   * to retrieve desired records.
   * @param fields Array of strings representing database fields which are to be
   * included in <code>SELECT</code> clause of the query.
   * @param criteria Array of {@link SQLCriterion} child classes to define
   * fields and their values to be compared to make the <code>WHERE</code>
   * clause portion of the query.
   * @param booleanOperator Database AND / OR operator used between the
   * <code>criteria</code>.
   * @param sortBy Name of the table field(s) by which the records are to be
   * sorted.
   * @param limitStart Number of first record to be returned.
   * @param limitSize Number of records to be returned.
   * @param con Connection object to be used to run the query. If
   * <code>null</code>, a new connection is created internally.
   * @return {@link PagingVO} object containing the required records as well as
   * total number of
   * records available in the database.
   * @throws ORMException In case of any database errors.
   */
  public PagingVO searchPaging(Object pojo, String[] fields,
        SQLCriterion[] criteria, BOOLEAN_OPERATOR booleanOperator, String sortBy,
        int limitStart, int limitSize, Connection con)
    throws ORMException;

  /**
   * Returns an object containing required results, if found, along with total
   * number of records which satisfy the given <code>criteria</code>.
   * 
   * @param pojo Object representing a database entity, which is to be queried
   * to retrieve desired records.
   * @param fields Array of strings representing database fields which are to be
   * included in <code>SELECT</code> clause of the query.
   * @param criteria Array of {@link SQLCriterion} child classes to define
   * fields and their values to be compared to make the <code>WHERE</code>
   * clause portion of the query.
   * @param booleanOperator Database AND / OR operator used between the
   * <code>criteria</code>.
   * @param sortBy Name of the table field(s) by which the records are to be
   * sorted.
   * @param limitStart Number of first record to be returned.
   * @param limitSize Number of records to be returned.
   * @param joins List of {@link Join} objects to be used in the query.
   * @param con Connection object to be used to run the query. If
   * <code>null</code>, a new connection is created internally.
   * @return {@link PagingVO} object containing the required records as well as
   * total number of
   * records available in the database.
   * @throws ORMException In case of any database errors.
   */
  public PagingVO searchPaging(Object pojo, String[] fields,
        SQLCriterion[] criteria, BOOLEAN_OPERATOR booleanOperator, String sortBy,
        int limitStart, int limitSize, List<Join> joins, Connection con)
    throws ORMException;

  /**
   * Returns an object containing required results, if found, along with total
   * number of records which satisfy the given <code>criteria</code> including
   * parent entities.
   * 
   * @param pojo Object representing a database entity, which is to be queried
   * to retrieve desired records.
   * @param level Number of parent entities to be included in
   * <code>SELECT</code> and <code>FROM</code> clause of the SQL statement.
   * @param fields Array of strings representing database fields which are to be
   * included in <code>SELECT</code> clause of the query.
   * @param criteria Array of {@link SQLCriterion} child classes to define
   * fields and their values to be compared to make the <code>WHERE</code>
   * clause portion of the query.
   * @param booleanOperator Database AND / OR operator used between the
   * <code>criteria</code>.
   * @param sortBy Name of the table field(s) by which the records are to be
   * sorted.
   * @param limitStart Number of first record to be returned.
   * @param limitSize Number of records to be returned.
   * @param con Connection object to be used to run the query. If
   * <code>null</code>, a new connection is created internally.
   * @return {@link PagingVO} object containing the required records as well as
   * total number of records available in the database.
   * @throws ORMException In case of any database errors.
   */
  public PagingVO searchPaging(Object pojo, int level, String[] fields,
        SQLCriterion[] criteria, BOOLEAN_OPERATOR booleanOperator, String sortBy,
        int limitStart, int limitSize, Connection con)
    throws ORMException;

  /**
   * Returns an object containing required results, if found, along with total
   * number of records which satisfy the given <code>criteria</code> including
   * parent entities.
   * 
   * @param pojo Object representing a database entity, which is to be queried
   * to retrieve desired records.
   * @param level Number of parent entities to be included in
   * <code>SELECT</code> and <code>FROM</code> clause of the SQL statement.
   * @param fields Array of strings representing database fields which are to be
   * included in <code>SELECT</code> clause of the query.
   * @param criteria Array of {@link SQLCriterion} child classes to define
   * fields and their values to be compared to make the <code>WHERE</code>
   * clause portion of the query.
   * @param booleanOperator Database AND / OR operator used between the
   * <code>criteria</code>.
   * @param sortBy Name of the table field(s) by which the records are to be
   * sorted.
   * @param limitStart Number of first record to be returned.
   * @param limitSize Number of records to be returned.
   * @param joins List of {@link Join} objects to be used in the query.
   * @param con Connection object to be used to run the query. If
   * <code>null</code>, a new connection is created internally.
   * @return {@link PagingVO} object containing the required records as well as
   * total number of records available in the database.
   * @throws ORMException In case of any database errors.
   */
  public PagingVO searchPaging(Object pojo, int level, String[] fields,
        SQLCriterion[] criteria, BOOLEAN_OPERATOR booleanOperator, String sortBy,
        int limitStart, int limitSize, List<Join> joins, Connection con)
    throws ORMException;

  /**
   * Performs a generic search based on the provided search request.
   * 
   * @param searchRequest The search request containing criteria, pagination,
   * and sorting information.
   * @param pojo Object representing a database entity.
   * @return {@link PagingVO} object containing search results.
   * @throws ORMException In case of any database errors.
   * @throws IllegalAccessException If field access is denied.
   * @throws InstantiationException If class instantiation fails.
   * @throws InvocationTargetException If method invocation fails.
   * @throws NoSuchMethodException If method is not found.
   */
  public PagingVO genericSearch(SearchRequest searchRequest, Object pojo)
      throws ORMException, IllegalAccessException, InstantiationException,
      InvocationTargetException, NoSuchMethodException;
      
  /**
   * Performs a generic search based on the provided search request.
   * 
   * @param searchRequest The search request containing criteria, pagination,
   * and sorting information.
   * @param pojo Object representing a database entity.
   * @param joins List of {@link Join} objects to be used in the query.
   * @return {@link PagingVO} object containing search results.
   * @throws ORMException In case of any database errors.
   * @throws IllegalAccessException If field access is denied.
   * @throws InstantiationException If class instantiation fails.
   * @throws InvocationTargetException If method invocation fails.
   * @throws NoSuchMethodException If method is not found.
   */
  public PagingVO genericSearch(SearchRequest searchRequest, Object pojo, List<Join> joins)
      throws ORMException, IllegalAccessException, InstantiationException,
      InvocationTargetException, NoSuchMethodException;
  
  /**
   * Inserts a record in the database corresponding to the given object in the
   * respective table the <code>pojo</code> is associated with.
   * <p>Fields values for the record are set in the given <code>pojo</code>.</p>
   * <p>Primary key, if generated automatically, is set in the object after
   * successful insertion.</p>
   * <p>NOTE: If any of the super, contained parent and contained children
   * objects in the given <code>pojo</code> have their primary keys not set
   * (i.e. they are less than 1 in case of numeric data types and null in case
   * of objects), and are configured to be associated with some table(s), they
   * are also inserted.</p>
   * 
   * @param <T> Type of the POJO
   * @param pojo Object representing a database row to be inserted in the table
   * given <code>pojo</code> is associated with.
   * @param con Connection for transaction control outside this method. If
   * <code>null</code>, a new connection is automatically made.
   * @return {@link DbResult} containing the number of rows inserted and the
   * inserted object if configured.
   * @throws ORMException In case of any database or other errors.
   */
  public <T> DbResult<T> insert(T pojo, Connection con) throws ORMException;
  
  /**
   * Inserts a record in the database corresponding to the given object with
   * control over returning the inserted object.
   * 
   * @param <T> Type of the POJO
   * @param pojo Object representing a database row to be inserted.
   * @param con Connection for transaction control outside this method.
   * @param returnInsertedObject Whether to return the inserted object in the
   * result.
   * @return {@link DbResult} containing the number of rows inserted and
   * optionally the inserted object.
   * @throws ORMException In case of any database or other errors.
   */
  public <T> DbResult<T> insert(T pojo, Connection con, 
      boolean returnInsertedObject) throws ORMException;
  
  /**
   * Inserts a record in the database corresponding to the given object with
   * fine-grained control over contained objects insertion.
   * 
   * @param <T> Type of the POJO
   * @param pojo Object representing a database row to be inserted.
   * @param con Connection for transaction control outside this method.
   * @param returnInsertedObject Whether to return the inserted object in the
   * result.
   * @param insertContainedParentObjects Whether to insert contained parent
   * objects.
   * @param insertContainedChildObjects Whether to insert contained child
   * objects.
   * @return {@link DbResult} containing the number of rows inserted and
   * optionally the inserted object.
   * @throws ORMException In case of any database or other errors.
   */
  public <T> DbResult<T> insert(T pojo, Connection con, 
      boolean returnInsertedObject, boolean insertContainedParentObjects,
      boolean insertContainedChildObjects) throws ORMException;
  
  /**
   * Updates a record, represented by the given <code>pojo</code>, in the
   * associated database table with fields values set in it.
   * 
   * @param <T> Type of the POJO
   * @param pojo Object contained field values to be updated in the
   * corresponding database row in the associated table.
   * @param con Connection for transaction control outside this method. If
   * <code>null</code>, a new connection is automatically made.
   * @return {@link DbResult} containing the number of rows updated and the
   * updated object if configured.
   * @throws ORMException In case of any database or other errors.
   */
  public <T> DbResult<T> update(T pojo, Connection con) throws ORMException;
  
  /**
   * Updates a record, represented by the given <code>pojo</code>, in the
   * associated database table with control over returning the updated object.
   * 
   * @param <T> Type of the POJO
   * @param pojo Object contained field values to be updated.
   * @param con Connection for transaction control outside this method.
   * @param returnUpdatedObject Whether to return the updated object in the
   * result.
   * @return {@link DbResult} containing the number of rows updated and
   * optionally the updated object.
   * @throws ORMException In case of any database or other errors.
   */
  public <T> DbResult<T> update(T pojo, Connection con, 
      boolean returnUpdatedObject) throws ORMException;
  
  /**
   * Updates specified fields of the record matching the primary key
   * of the given <code>pojo</code>.
   * 
   * @param <T> Type of the POJO
   * @param pojo Object representing a database table and has some field values
   * that are to be updated.
   * @param fields Table fields that are to be updated.
   * @param con Connection for transaction control outside this method.
   * @param returnUpdatedObject Whether to return the updated object in the
   * result.
   * @return {@link DbResult} containing the number of rows updated and
   * optionally the updated object.
   * @throws ORMException In case of any database or other errors.
   */
  public <T> DbResult<T> update(T pojo, String[] fields, Connection con, 
      boolean returnUpdatedObject)
      throws ORMException;
  
  /**
   * Updates specified fields of the records matching the given criteria.
   * 
   * @param <T> Type of the POJO
   * @param pojo Object representing a database table and has some field values
   * that are to be updated.
   * @param fields Table fields that are to be updated.
   * @param criteria Criteria to make <code>WHERE</code> clause of the
   * <code>UPDATE</code> query.
   * @param booleanOperator Database AND / OR operator used between the
   * <code>criteria</code>.
   * @param con Connection for transaction control outside this method.
   * @param returnUpdatedObject Whether to return the updated object in the
   * result.
   * @return {@link DbResult} containing the number of rows updated and
   * optionally the updated object.
   * @throws ORMException In case of any database or other errors.
   */
  public <T> DbResult<T> update(T pojo, String[] fields,
      SQLCriterion[] criteria, BOOLEAN_OPERATOR booleanOperator, Connection con,
      boolean returnUpdatedObject)
      throws ORMException;
  
  /**
   * Deletes a record represented by the given <code>pojo</code> object using
   * its primary key.
   * 
   * @param <T> Type of the POJO
   * @param pojo Object representing a database table and a unique row in that
   * table (through its primary key value).
   * @param con Connection object for transaction control outside this method.
   * @return {@link DbResult} containing the number of rows deleted and the
   * deleted object if configured.
   * @throws ORMException In case of any database or other errors.
   */
  public <T> DbResult<T> delete(T pojo, Connection con) throws ORMException;
  
  /**
   * Deletes a record represented by the given <code>pojo</code> object using
   * its primary key with control over returning the deleted object.
   * 
   * @param <T> Type of the POJO
   * @param pojo Object representing a database table and a unique row in that
   * table.
   * @param con Connection object for transaction control outside this method.
   * @param returnDeletedObject Whether to return the deleted object in the
   * result.
   * @return {@link DbResult} containing the number of rows deleted and 
   * optionally the deleted object.
   * @throws ORMException In case of any database or other errors.
   */
  public <T> DbResult<T> delete(T pojo, Connection con,
      boolean returnDeletedObject) throws ORMException;
  
  /**
   * Deletes a record using a specific primary key value.
   * 
   * @param <T> Type of the POJO
   * @param pojo Object representing a database table.
   * @param pkValue Value of the primary key field.
   * @param con Connection object for transaction control outside this method.
   * @param returnDeletedObject Whether to return the deleted object in the
   * result.
   * @return {@link DbResult} containing the number of rows deleted and
   * optionally the deleted object.
   * @throws ORMException In case of any database or other errors.
   */
  public <T> DbResult<T> delete(T pojo, Object pkValue, Connection con,
      boolean returnDeletedObject) throws ORMException;
  
  /**
   * Deletes multiple records represented by the given collection of objects.
   * 
   * @param <T> Type of the POJO
   * @param pojos Objects representing database rows in the corresponding table.
   * @param con Connection object for transaction control outside this method.
   * @param returnDeletedObject Whether to return the deleted objects in the
   * result.
   * @return {@link DbResult} containing the number of rows deleted and
   * optionally the deleted objects.
   * @throws ORMException In case of any database or other errors.
   */
  public <T> DbResult<T> delete(Collection<T> pojos, Connection con,
      boolean returnDeletedObject) throws ORMException;
  
  /**
   * Deletes rows matching the given criteria.
   * 
   * @param <T> Type of the POJO
   * @param pojo Object representing a database table whose rows matching the
   * <code>criteria</code> are to be deleted.
   * @param criteria Criteria to delete specific rows. If <code>null</code>, all
   * the rows shall be deleted.
   * @param booleanOperator Database AND / OR operator used between the
   * <code>criteria</code>.
   * @param con Connection object for transaction control outside this method.
   * @param returnDeletedObject Whether to return the deleted objects in the
   * result.
   * @return {@link DbResult} containing the number of rows deleted and
   * optionally the deleted objects.
   * @throws ORMException In case of any database or other errors.
   */
  public <T> DbResult<T> delete(T pojo, SQLCriterion[] criteria,
      BOOLEAN_OPERATOR booleanOperator, Connection con,
      boolean returnDeletedObject) throws ORMException;
  
  /**
   * Deletes rows having primary key values in the given collection.
   * 
   * @param <T> Type of the POJO
   * @param pojo Object representing a database table whose rows are to be
   * deleted.
   * @param ids Values of primary key of the rows that are to be deleted.
   * @param con Connection object for transaction control outside this method.
   * @param returnDeletedObject Whether to return the deleted objects in the
   * result.
   * @return {@link DbResult} containing the number of rows deleted and
   * optionally the deleted objects.
   * @throws ORMException In case of any database or other errors.
   */
  public <T> DbResult<T> delete(T pojo, Collection ids,
      Connection con, boolean returnDeletedObject) throws ORMException;

  /**
   * Deletes a record represented by the given <code>pojo</code> object and all
   * its child records.
   * 
   * @param <T> Type of the POJO
   * @param pojo Object representing a database table and a unique row in that
   * table.
   * @param con Connection object for transaction control outside this method.
   * @param returnDeletedObject Whether to return the deleted object in the
   * result.
   * @return {@link DbResult} containing the number of rows deleted and
   * optionally the deleted object.
   * @throws ORMException In case of any database or other errors.
   */
  public <T> DbResult<T> cascadeDelete(T pojo, Connection con,
      boolean returnDeletedObject) throws ORMException;
    
  /**
   * Deletes multiple records and all their child records.
   * 
   * @param <T> Type of the POJO
   * @param pojos Objects representing database rows in the associated table
   * which are to be cascade deleted.
   * @param con Connection object for transaction control outside this method.
   * @param returnDeletedObject Whether to return the deleted objects in the
   * result.
   * @return {@link DbResult} containing the number of rows deleted including
   * the child rows in the hierarchy.
   * @throws ORMException In case of any database or other errors.
   */
  public <T> DbResult<T> cascadeDelete(Collection<T> pojos,
      Connection con, boolean returnDeletedObject) throws ORMException;
    
  /**
   * Populates contained objects in the given container objects based on related
   * field values.
   * 
   * @param containers Objects which contain other objects and need to be filled.
   * @param containedObjectInstanceMember Name of the instance member which is a
   * contained object (or collection).
   * @param con Optional database {@link Connection} object to be used for query
   * execution.
   * @throws ORMException In case of any errors.
   */
  public void populateContainedObjects(Object[] containers,
      String containedObjectInstanceMember, Connection con)
      throws ORMException;
  
  /**
   * Populates contained objects in the given container objects using a
   * {@link ContainedObjectField} specification.
   * 
   * @param containers Objects which contain other objects and need to be
   * filled.
   * @param containedObjField {@link ContainedObjectField} object representing
   * the related and referenced field in the parent/child relationship.
   * @param con Optional database connection object to be used for query
   * execution.
   * @throws ORMException In case of any errors.
   */
  public void populateContainedObjects(Object[] containers,
      ContainedObjectField containedObjField, Connection con)
      throws ORMException;
  
  /**
   * Gets a live database connection from connection pool class.
   * 
   * @return A live database connection.
   * @throws SQLException In case of any errors.
   */
  public java.sql.Connection getConnection() throws SQLException;

  /**
   * Returns the given <code>con</code> to the connection pool class.
   * 
   * @param con Database connection to be returned to pool.
   */
  public void closeConnection(java.sql.Connection con);
  
  /**
   * Returns a number that can be used as database unique id for a record that
   * is to be inserted in the given table.
   * 
   * @param tableName Table in which the record is to be inserted.
   * @param primaryKeyField Name of the table column for which a unique id is
   * required.
   * @return A number that can be used as unique database id for a record to be
   * inserted in the given table.
   * @throws ORMException In case of database connection errors or any other
   * errors.
   */
  public int getNewId(String tableName, String primaryKeyField)
      throws ORMException;

  /**
   * Creates SQL select statement for the given <code>pojo</code> object.
   * 
   * @param pojo Object for which <code>SELECT</code> SQL query is to be
   * generated.
   * @return SQL <code>SELECT</code> query.
   */
  public String createSelectQuery(Object pojo);
  
  /**
   * Creates SQL select statement for the given <code>pojo</code> object
   * including super classes.
   * 
   * @param pojo Object for which <code>SELECT</code> SQL query is to be
   * generated.
   * @param level Number of super classes to be included.
   * @return SQL <code>SELECT</code> query.
   */
  public String createSelectQuery(Object pojo, int level);
  
  /**
   * Creates SQL select statement for the given <code>pojo</code> object with
   * criteria.
   * 
   * @param pojo Object for which <code>SELECT</code> SQL query is to be
   * generated.
   * @param level Number of super classes to be included.
   * @param criteria <code>WHERE</code> clause criteria if any.
   * @param booleanOperator Database AND / OR operator used between the
   * <code>criteria</code>.
   * @return SQL <code>SELECT</code> query.
   */
  public String createSelectQuery(Object pojo, int level, 
      SQLCriterion[] criteria, BOOLEAN_OPERATOR booleanOperator);
  
  /**
   * Creates SQL select statement for the given <code>pojo</code> object with
   * specific fields and criteria.
   * 
   * @param pojo Object for which <code>SELECT</code> SQL query is to be 
   * generated.
   * @param level Number of super classes to be included.
   * @param fields Fields to be included in the <code>SELECT</code> clause.
   * @param criteria <code>WHERE</code> clause criteria if any.
   * @param booleanOperator Database AND / OR operator used between the 
   * <code>criteria</code>.
   * @return SQL <code>SELECT</code> query.
   */
  public String createSelectQuery(Object pojo, int level, String[] fields, 
      SQLCriterion[] criteria, BOOLEAN_OPERATOR booleanOperator);
  
  /**
   * Creates SQL GROUP BY query for the given <code>pojo</code> object.
   * 
   * @param pojo Object for which <code>GROUP BY</code> SQL query is to be
   * generated.
   * @param level Number of super classes to be included.
   * @param fields Fields to be included in the <code>SELECT</code> clause.
   * @param functions SQL aggregate functions.
   * @param criteria <code>WHERE</code> clause criteria if any.
   * @param booleanOperator Database AND / OR operator used between the 
   * <code>criteria</code>.
   * @param sortBy Sorting criteria.
   * @param limitStart Starting record number.
   * @param limitSize Maximum number of records.
   * @return SQL <code>GROUP BY</code> query.
   */
  public String createGroupByQuery(Object pojo, int level, String[] fields, 
      SQLFunction[] functions, SQLCriterion[] criteria,
      BOOLEAN_OPERATOR booleanOperator, String sortBy, int limitStart, int limitSize, List<Join> joins);
  
  /**
   * Creates SQL GROUP BY query for custom from clause.
   * 
   * @param fields Fields to be included in the <code>SELECT</code> clause.
   * @param fromClause From clause to be used in the select statement.
   * @param functions SQL aggregate functions.
   * @param criteria <code>WHERE</code> clause criteria if any.
   * @param booleanOperator Database AND / OR operator used between the 
   * <code>criteria</code>.
   * @param sortBy Sorting criteria.
   * @param limitStart Starting record number.
   * @param limitSize Maximum number of records.
   * @return SQL <code>GROUP BY</code> query.
   */
  public String createGroupByQuery(String[] fields, String fromClause, 
      SQLFunction[] functions, SQLCriterion[] criteria, BOOLEAN_OPERATOR booleanOperator,
      String sortBy, int limitStart, int limitSize);
  
  /**
   * Creates SQL select statement with pagination support.
   * 
   * @param pojo Object for which <code>SELECT</code> SQL query is to be
   * generated.
   * @param level Number of super classes to be included.
   * @param fields Fields to be included in the <code>SELECT</code> clause.
   * @param criteria <code>WHERE</code> clause criteria if any.
   * @param booleanOperator Database AND / OR operator used between the 
   * <code>criteria</code>.
   * @param sortBy Sorting criteria.
   * @param limitStart Starting record number.
   * @param limitSize Maximum number of records.
   * @return SQL <code>SELECT</code> query.
   */
  public String createSelectQuery(Object pojo, int level, String[] fields,
      SQLCriterion[] criteria, BOOLEAN_OPERATOR booleanOperator, String sortBy,
      int limitStart, int limitSize);

  /**
   * Creates SQL select statement with aggregate functions.
   * 
   * @param pojo Object for which <code>SELECT</code> SQL query is to be
   * generated.
   * @param level Number of super classes to be included.
   * @param fields Fields to be included in the <code>SELECT</code> clause.
   * @param functions SQL aggregate functions.
   * @param criteria <code>WHERE</code> clause criteria if any.
   * @param booleanOperator Database AND / OR operator used between the 
   * <code>criteria</code>.
   * @param sortBy Sorting criteria.
   * @param limitStart Starting record number.
   * @param limitSize Maximum number of records.
   * @return SQL <code>SELECT</code> query.
   */
  public String createSelectQuery(Object pojo, int level, String[] fields, 
      SQLFunction[] functions, SQLCriterion[] criteria, BOOLEAN_OPERATOR booleanOperator,
      String sortBy, int limitStart, int limitSize, List<Join> joins);
  
  /**
   * Creates SQL select statement for custom from clause with aggregate
   * functions.
   * 
   * @param fields Fields to be included in the <code>SELECT</code> clause.
   * @param fromClause From clause to be used in the select statement.
   * @param functions SQL aggregate functions.
   * @param criteria <code>WHERE</code> clause criteria if any.
   * @param booleanOperator Database AND / OR operator used between the 
   * <code>criteria</code>.
   * @param sortBy Sorting criteria.
   * @param limitStart Starting record number.
   * @param limitSize Maximum number of records.
   * @return SQL <code>SELECT</code> query.
   */
  public String createSelectQuery(String[] fields, String fromClause,
      SQLFunction[] functions, SQLCriterion[] criteria, BOOLEAN_OPERATOR booleanOperator,
      String sortBy, int limitStart, int limitSize);

  /**
   * Creates SQL <code>INSERT</code> statement for the given <code>pojo</code>
   * object.
   * 
   * @param pojo Object for which <code>INSERT</code> SQL query is to be
   * generated.
   * @return SQL <code>INSERT</code> query.
   * @throws ORMException In case of any database interaction or reflection 
   * problems.
   */
  public String createInsertQuery(Object pojo) throws ORMException;

  /**
   * Creates SQL <code>INSERT</code> statement for the given <code>pojo</code>
   * object including only non-empty fields.
   * 
   * @param pojo Object for which <code>INSERT</code> SQL query is to be 
   * generated.
   * @return SQL <code>INSERT</code> query including only non-empty fields.
   */
  public String createInsertQueryShort(Object pojo);
  
  /**
   * Creates SQL <code>UPDATE</code> statement for the given object using its
   * primary key as criteria.
   * 
   * @param pojo Object having field values set to generate an SQL
   * <code>UPDATE</code> statement.
   * @return SQL <code>UPDATE</code> statement that can be run in the database.
   */
  public String createUpdateQuery(Object pojo);
  
  /**
   * Creates SQL <code>UPDATE</code> statement for the given object for specific
   * fields and criteria.
   * 
   * @param pojo Object having field values set to generate an SQL 
   * <code>UPDATE</code> statement.
   * @param fields Fields to be included in the <code>UPDATE</code> statement.
   * @param criteria Criteria to construct <code>WHERE</code> clause of the 
   * statement.
   * @param booleanOperator Database AND / OR operator used between the 
   * <code>criteria</code>.
   * @return SQL <code>UPDATE</code> statement that can be run in the database.
   */
  public String createUpdateQuery(Object pojo, String[] fields,
      SQLCriterion[] criteria, BOOLEAN_OPERATOR booleanOperator);
  
  /**
   * Creates SQL <code>DELETE</code> statement for the row represented by the
   * given object.
   * 
   * @param pojo Object representing a database table for which an SQL 
   * <code>DELETE</code> statement is to be created.
   * @return SQL <code>DELETE</code> statement that can be run in the underlying
   * DBMS.
   */
  public String createDeleteQuery(Object pojo);
  
  /**
   * Creates SQL <code>DELETE</code> statement for multiple records with given
   * primary key values.
   * 
   * @param dvo Object representing a database table for which an SQL
   * <code>DELETE</code> statement is to be created.
   * @param ids Values of the primary key field of the database table.
   * @return SQL <code>DELETE</code> statement that can be run in the underlying
   * DBMS.
   */
  public String createDeleteQuery(Object dvo, Collection<?> ids);
  
  /**
   * Creates SQL <code>DELETE</code> statement for records matching the given 
   * criteria.
   * 
   * @param pojo Object representing a database table for which an SQL 
   * <code>DELETE</code> statement is to be created.
   * @param criteria Criteria to match records that are to be deleted.
   * @param booleanOperator Database AND / OR operator used between the 
   * <code>criteria</code>.
   * @return SQL <code>DELETE</code> statement that can be run in the underlying 
   * DBMS.
   */
  public String createDeleteQuery(Object pojo, SQLCriterion[] criteria, 
      BOOLEAN_OPERATOR booleanOperator);
  
  /**
   * Serializes object fields and values to string buffers.
   * 
   * @param pojo Object whose getters are executed and values, and optionally
   * field names, are to be serialized.
   * @param cols Buffer to serialize field names in.
   * @param values Buffer to serialize values in.
   * @param fields Specific fields to serialize.
   * @param includeEmpty Whether to include null or empty values.
   */
  public void serialize(Object pojo, StringBuffer cols, StringBuffer values, 
      String[] fields, boolean includeEmpty);
  
  /**
   * Extracts field values from object into collections.
   * 
   * @param pojo Object whose getters are executed and values, and optionally
   * field names, are to be extracted.
   * @param cols Collection to store field names in.
   * @param values Collection to store values in.
   * @param includeEmpty Whether to include null or empty values.
   */
  public void extractValues(Object pojo, Collection<String> cols, 
      Collection<Object> values, boolean includeEmpty);
  
  /**
   * Executes a select query and returns results as a collection of string 
   * arrays.
   * 
   * @param qry Select query to be executed.
   * @param con Connection to use for the query.
   * @return Collection of string arrays containing query results.
   * @throws ORMException In case of incorrect query or any other errors.
   */
  public Collection<String[]> selectQueryVec(String qry, Connection con)
      throws ORMException;
  
  /**
   * Fills the given POJO object with data from the database.
   * 
   * @param <T> Type of the POJO
   * @param pojo Object to be filled with data.
   * @param con Connection to use for the query.
   * @return The filled object.
   * @throws ORMException In case of any database errors.
   */
  public <T> T fillObject(T pojo, Connection con)
      throws ORMException;
  
  /**
   * Fills multiple container objects with corresponding contained objects.
   * 
   * @param containerObjs Objects whose setter method is to be invoked to set
   * corresponding contained objects.
   * @param containedObjs Objects to be contained in relevant container objects.
   * @param containerRelatedField {@link DBField} object for getting related
   * field values from container objects.
   * @param containedRelatedField {@link DBField} object for getting related
   * field values from contained objects.
   * @param containedObjField {@link ContainedObjectField} object whose setter
   * method is to be invoked.
   */
  public void fillContainersMulti(Object[] containerObjs, 
      Object[] containedObjs, DBField containerRelatedField,
      DBField containedRelatedField, ContainedObjectField containedObjField);

  /**
   * Fills container objects with single corresponding contained objects.
   * 
   * @param containerObjs Objects whose setter method is to be invoked to set 
   * corresponding contained objects.
   * @param containedObjs Objects to be contained in the relevant container 
   * objects.
   * @param containerRelatedField {@link DBField} object for getting related
   * field values from container objects.
   * @param containedRelatedField {@link DBField} object for getting related
   * field values from contained objects.
   * @param containedObjField {@link ContainedObjectField} object whose setter
   * method is to be invoked.
   */
  public void fillContainersSingle(Object[] containerObjs, Object[] containedObjs,
      DBField containerRelatedField, DBField containedRelatedField,
      ContainedObjectField containedObjField);

  /**
   * Fills contained objects in the given containers to the specified level 
   * using depth-first traversal.
   * 
   * @param containers Array of objects whose contained objects are to be filled
   * in.
   * @param level Level to which objects are to be filled.
   * @param con {@link Connection} object to be used to query database.
   * @throws ORMException In case of any database related errors.
   */
  public void fillContainedObjectsDepthFirst(Object[] containers, int level, 
      Connection con) throws ORMException;
  
  /**
   * Fills specific contained objects in the given containers to the specified
   * level using depth-first traversal.
   * 
   * @param containers Array of objects whose contained objects are to be filled
   * in.
   * @param includes Names of getter methods of contained beans to be filled.
   * @param level Level to which objects are to be filled.
   * @param con {@link Connection} object to be used to query database.
   * @throws ORMException In case of any database related errors.
   */
  public void fillContainedObjectsDepthFirst(Object[] containers,
      String[] includes, int level, Connection con)
      throws ORMException;

  /**
   * Fills contained objects in the given containers to the specified level
   * using breadth-first traversal.
   * 
   * @param containers Array of objects whose contained objects are to be filled 
   * in.
   * @param level Level to which objects are to be filled.
   * @param con {@link Connection} object to be used to query database.
   * @throws ORMException In case of any database related errors.
   */
  public void fillContainedObjects(Object[] containers, int level, Connection con)
      throws ORMException;

  /**
   * Fills specific contained objects in the given containers to the specified
   * level using breadth-first traversal.
   * 
   * @param containers Array of objects whose contained objects are to be filled 
   * in.
   * @param includes Names of getter methods of contained beans to be filled.
   * @param level Level to which objects are to be filled.
   * @param con {@link Connection} object to be used to query database.
   * @throws ORMException In case of any database related errors.
   */
  public void fillContainedObjects(Object[] containers,
      String[] includes, int level, Connection con)
      throws ORMException;
  
} // end of interface DAO