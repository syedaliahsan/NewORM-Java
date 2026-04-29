package com.sa.orm;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.TreeMap;
import java.util.logging.Logger;

import com.sa.orm.reflect.annotation.ContainedObject;
import com.sa.orm.reflect.annotation.Entity;
import com.sa.orm.reflect.annotation.PrimaryKey;
import com.sa.orm.reflect.annotation.ForeignKey;
import com.sa.orm.reflect.annotation.Unique;
import com.sa.orm.util.ContainedObjectField;
import com.sa.orm.util.DBField;
import com.sa.orm.util.ForeignKeyField;
import com.sa.orm.util.InstanceMember;
import com.sa.orm.util.ReflectionException;
import com.sa.orm.util.StringUtils;
import com.sa.orm.util.Utility;

/**
 * <h4>Description</h4>Maintains the ORM information of the project POJOs.
 * <p>Once ORM information of an object has been generated, it is stored
 * in-memory and it is not generated again.</p>
 * <p>The only object of this class is heavily used by {@link PersistentManager}
 * and {@link AbstractDAO} objects for building queries.</p>
 * <p>When this class is referred first time, it locates the configuration file
 * by either loading it from the path specified in system property named
 * <code>ORM_INFO_FILE_NAME</code> or discovering a file named
 * <code>ORMInfo.xml</code> in the class path.</p>
 * 
 * <p>TODO Instantiates object of this class by either loading it from the file
 * named <code>ORMInfo.object</code> found in either class path or in project
 * home (specified either in configuration file or in a system property named
 * <code>NEWORM_PROJECT_HOME</code> or in file system's temp folder) or by
 * introspecting the POJOs (to be included in introspection).</p>
 * 
 * <h4>Copyrights</h4>Copyrights &copy; 2008 - 2012 Soft Assets.
 * All Rights Reserved.
 * 
 * <h4>Company</h4>Soft Assets
 * <h4>Created</h4>October 27, 2010
 * @author Ali Ahsan
 * @version 1.1
 */
public class ORMInfoManager {

  /**
   * Default number of parent class levels to be considered while
   * creating queries for joining and condition purposes.
   */
  public static final int SUPER_LEVEL;

  /**
   * Default format to convert {@link Timestamp} values
   * to String.
   */
  public static final String TIMESTAMP_FORMAT;
  
  /**
   * Default format to convert {@link java.sql.Date} values to String.
   */
  public static final String DATE_FORMAT;
  
  /**
   * Default format to convert {@link java.sql.Time} values to String.
   */
  public static final String TIME_FORMAT;

  /**
   * Returns the singleton object of this class.
   * 
   * @return Singleton object of this class.
   */
  public static ORMInfoManager getInstance() {
    if(ormInfoMgr == null) {
      ormInfoMgr = new ORMInfoManager();
    } // end of if
    return ormInfoMgr;
  } // end of method getInstance
  
  /**
   * Map containing the ORM information of all the objects, which has been
   * introspected/parsed at least once.
   * <p>{@link Map} objects are stored against values returned by
   * {@link Class#getName()} method.</p>
   * <p>The mapped {@link Map} object, in turn, contains all the information
   * of each object including entity name, primary key name, constraints, list
   * of simple (primitive wrapper classes and dates) instance members, list of
   * contained objects etc.</p>
   */
  private static Map<String, Map<String, Object>> reflectionCache = new TreeMap<String, Map<String, Object>>();

  private static ClassLoader classLoader = null;
  
  private static final String ENUM_ENTITY_NAME = "EName";

  private static final String ENUM_INHERITS = "Inherits";

  private static final String ENUM_INHERITED_ATTRIBUTES = "InheritedAttributes";

  private static final String ENUM_INHERIT_PK = "InheritPK";

  private static final String ENUM_PRIMARY_KEY = "PK";

  private static final String ENUM_QUALIFIED_PRIMARY_KEY = "QualPK";

  private static final String ENUM_QUALIFIED_PRIMARY_KEY_ALIAS = "QualPKAlias";

  private static final String ENUM_PLAIN_DB_FIELD_NAMES = "PDBFNames";

  private static final String ENUM_DB_FIELDS = "Fields";

  private static final String ENUM_INSTANCE_MEMBERS = "InstanceMembers";
  
  private static final String ENUM_FOREIGN_KEY_FIELDS = "ForeignKeyFields";

  private static final String ENUM_WRAPPED_DB_FIELD_NAMES = "DBFNames";

  private static final String ENUM_QUALIFIED_DB_FIELD_NAMES = "QualDBFNames";

  private static final String ENUM_QUALIFIED_ALIASED_DB_FIELD_NAMES = "QualADBFNames";

  private static final String ENUM_DB_FIELD_ALIAS_NAMES = "DBFANames";

  private static final String ENUM_QUALIFIED_DB_FIELD_ALIAS_NAMES = "QualDBFANames";

  private static final String ENUM_INSTANCE_SETTERS = "Setters";

  private static final String ENUM_CONTAINED_OBJECT_FIELD_KEY_PREFIX = "ContainedObjectField_";

  private static final String ENUM_CONTAINED_OBJECT_FIELDS = "ContainedObjectFields";

  /**
   * Prefix to the instance member name to be used as key to store corresponding
   * {@link DBField} object in a reflectionCache containing fields of a POJO.
   */
  private static final String INSTANCE_MEMBER_NAME_KEY_PREFIX = "instanceMember_";

  /**
   * Prefix to the database column name to be used as key to store corresponding
   * {@link DBField} object in a reflectionCache containing fields of a POJO.
   */
  private static final String COLUMN_NAME_KEY_PREFIX = "columnName_";
  
  /**
   * Package of the implementation classes for this ORM tool.
   */
  private static final String databaseImplClassesPackage;
  
  /**
   * Name of the connection pool class.
   */
  public static final String connectionPoolClassName;
  
  public static final String DB_USER_NAME;
  public static final String DB_USER_PASSWORD;
  public static final String DB_DRIVER_CLASS_NAME;
  public static final String DB_URL;
  public static final int DB_MAX_ACTIVE;
  public static final int DB_MAX_IDLE;
  public static final long DB_MAX_WAIT;
  public static final int MAX_RECORDS_SHOWN;

  public static final boolean RETURN_AFFECTED_OBJECTS_INSERT;
  public static final boolean RETURN_AFFECTED_OBJECTS_UPDATE;
  public static final boolean RETURN_AFFECTED_OBJECTS_DELETE;
  
  /**
   * Object providing database specific things etc. entity name wrapper
   * characters, keywords, literal value wrappers etc.
   * 
   * @see SQLConstants
   */
  public static final SQLConstants sqlConstantsObj;
  
  public static final ExceptionUtil exceptionUtil;
  
  public static final SQLCriterionFactory sqlCriterionFactory;
  
  public static final SQLFunctionFactory sqlFunctionFactory;
  
  public static final AbstractDAO daoImpl;
  
  /**
   * Logger object to log messages.
   */
  private static Logger logger = Logger.getLogger(ORMInfoManager.class.getName());
  
  /**
   * This static block loads constant values from a file.
   */
  static {
    Properties configProps = null;
    try {
      configProps = new Properties();
      String ormInfoFilePath = System.getProperty("ORM_INFO_FILE_NAME");
      InputStream ormInfoFileStream = null;
      if(ormInfoFilePath == null || ormInfoFilePath.trim().length() < 1) {
        URL configFileURI = ORMInfoManager.class.getClassLoader().getResource("ORMInfo.xml");
        logger.config("ORM Info file path : " + configFileURI.getPath());
        ormInfoFileStream = ORMInfoManager.class.getClassLoader().getResourceAsStream("ORMInfo.xml");
      }
      else {
        ormInfoFileStream = new BufferedInputStream(new FileInputStream(ormInfoFilePath));
      }
      configProps.loadFromXML(ormInfoFileStream);
      
      SUPER_LEVEL = Integer.parseInt(configProps.getProperty("SUPER_LEVEL", "1"));
      DATE_FORMAT = configProps.getProperty("DATE_FORMAT", "yyyy-MM-dd");
      TIME_FORMAT = configProps.getProperty("TIME_FORMAT", "HH:mm:ss");
      TIMESTAMP_FORMAT = configProps.getProperty("TIMESTAMP_FORMAT", "yyyy-MM-dd HH:mm:ss");
      databaseImplClassesPackage = configProps.getProperty("DATABASE_CLASSES_PACKAGE");
      
      DB_USER_NAME = configProps.getProperty("DB_USER_NAME");
      DB_USER_PASSWORD = configProps.getProperty("DB_USER_PASSWORD");
      DB_DRIVER_CLASS_NAME = configProps.getProperty("DB_DRIVER_CLASS_NAME");
      DB_URL = configProps.getProperty("DB_URL");
      DB_MAX_ACTIVE = Integer.parseInt(configProps.getProperty("DB_MAX_ACTIVE", "5"));
      DB_MAX_IDLE = Integer.parseInt(configProps.getProperty("DB_MAX_IDLE", "5"));
      DB_MAX_WAIT = Long.parseLong(configProps.getProperty("DB_MAX_WAIT", "2"));

      RETURN_AFFECTED_OBJECTS_INSERT = Boolean.parseBoolean(configProps.getProperty("RETURN_AFFECTED_OBJECTS_INSERT", "false"));
      RETURN_AFFECTED_OBJECTS_UPDATE = Boolean.parseBoolean(configProps.getProperty("RETURN_AFFECTED_OBJECTS_UPDATE", "false"));
      RETURN_AFFECTED_OBJECTS_DELETE = Boolean.parseBoolean(configProps.getProperty("RETURN_AFFECTED_OBJECTS_DELETE", "false"));
      
      MAX_RECORDS_SHOWN = Integer.parseInt(configProps.getProperty("MAX_RECORDS_SHOWN", "10"));

      connectionPoolClassName = configProps.getProperty("CONNECTION_POOL_CLASS_NAME");
      daoImpl = (AbstractDAO)Class.forName(databaseImplClassesPackage + ".DAOImpl").getDeclaredConstructor().newInstance();
      sqlConstantsObj = (SQLConstants)Class.forName(databaseImplClassesPackage + ".SQLConstants").getDeclaredConstructor().newInstance();
      exceptionUtil = (ExceptionUtil)Class.forName(databaseImplClassesPackage + ".ExceptionUtilImpl").getDeclaredConstructor().newInstance();
      sqlCriterionFactory = (SQLCriterionFactory)Class.forName(databaseImplClassesPackage + ".SQLCriterionFactoryImpl").getDeclaredConstructor().newInstance();
      sqlFunctionFactory = (SQLFunctionFactory)Class.forName(databaseImplClassesPackage + ".SQLFunctionFactoryImpl").getDeclaredConstructor().newInstance();
      logger.info("Constants class: " + sqlConstantsObj.getClass().getPackageName() + "." + sqlConstantsObj.getClass().getName());
    } // end of try
    catch(Exception e) {
      logger.warning(Utility.getStackTrace(e));
      throw new RuntimeException(e.getMessage(), e);
    } // end of catch
  } // end of static initializer

  private static ORMInfoManager ormInfoMgr;
  
  public static void clearCache() {
    reflectionCache = new TreeMap<String, Map<String, Object>>();
  }
  

  private ORMInfoManager() {
    ;
  } // end of constructor
  
  /**
   * Returns the name of the database entity, the given <code>pojo</code> is
   * associated with.
   * 
   * @param pojo Object whose associated database entity's name is to be
   * returned.
   * 
   * @return Name of the database entity, the given <code>pojo</code> is
   * associated with.
   */
  public static String getEntityName(Object pojo) {
    return (String)get(pojo, ENUM_ENTITY_NAME);
  } // end of method getEntityName

  /**
   * Returns the name of the database entity (table) associated with the pojo
   * whose name is given.
   * 
   * @param pojoName Name of a pojo whose associated database entity's name is
   * to be returned.
   * 
   * @return Name of the database entity (table) associated with the pojo
   * whose name is given.
   */
  public static String getEntityName(String pojoName) {
    return (String)get(pojoName, ENUM_ENTITY_NAME);
  } // end of method getEntityName

  /**
   * Returns the name of the instance member of the given <code>pojo</code>,
   * which is mapped to the primary key of the associated entity.
   * 
   * @param pojo Object whose instance member, mapped to primary key, name is to
   * be returned.
   * 
   * @return Name of the instance member of the given <code>pojo</code>,
   * which is mapped to the primary key of the associated entity.
   */
  public static String getPrimaryKeyName(Object pojo) {
    return (String)((DBField)get(pojo, ENUM_PRIMARY_KEY)).getInstanceMemberName();
  } // end of method getPrimaryKeyName

  /**
   * Returns the name of the instance member of the given <code>className</code>
   * which is mapped to the primary key of the associated entity.
   * 
   * @param className Name of the java class whose instance member's, which is
   * mapped to primary key in the database, name is to be returned.
   * 
   * @return Name of the instance member of the given <code>className</code>,
   * which is mapped to the primary key of the associated entity.
   */
  public static String getPrimaryKeyName(String className) {
    return (String)((DBField)get(className, ENUM_PRIMARY_KEY)).getInstanceMemberName();
  } // end of method getPrimaryKeyName

  /**
   * Returns the {@link DBField} encapsulating the instance member of the given
   * <code>pojo</code>, which is mapped to the primary key of the associated
   * entity.
   * 
   * @param pojo Object whose instance member, mapped to primary key, is to be
   * returned.
   * 
   * @return Instance member of the given <code>pojo</code>, which is mapped to
   * the primary key of the associated entity.
   */
  public static DBField getPrimaryKeyField(Object pojo) {
    return (DBField)get(pojo, ENUM_PRIMARY_KEY);
  } // end of method getPrimaryKeyField

  /**
   * Returns the {@link DBField} encapsulating the instance member of the given
   * <code>className</code>, which is mapped to the primary key of the
   * associated entity.
   * 
   * @param className Name of the java class whose instance member, which is
   * mapped to primary key in the database, is to be returned.
   * 
   * @return Instance member of the given <code>className</code>, which is
   * mapped to the primary key of the associated entity.
   */
  public static DBField getPrimaryKeyField(String className) {
    return (DBField)get(className, ENUM_PRIMARY_KEY);
  } // end of method getPrimaryKeyField

  public static String getQualifiedPrimaryKey(Object pojo) {
    return (String)get(pojo, ENUM_QUALIFIED_PRIMARY_KEY);
  } // end of method getQualifiedPrimaryKey

  public static String getQualifiedPrimaryKeyAlias(Object pojo) {
    return (String)get(pojo, ENUM_QUALIFIED_PRIMARY_KEY_ALIAS);
  } // end of method getQualifiedPrimaryKeyAlias

  public static String getPlainDBFieldNames(String pojoClassName) {
    return (String)get(pojoClassName, ENUM_PLAIN_DB_FIELD_NAMES);
  } // end of method getPlainDBFieldNames

  public static String getPlainDBFieldNames(Object pojo) {
    return (String)get(pojo, ENUM_PLAIN_DB_FIELD_NAMES);
  } // end of method getPlainDBFieldNames

  public static String getDBFieldNames(Object pojo) {
    return (String)get(pojo, ENUM_WRAPPED_DB_FIELD_NAMES);
  } // end of method getDBFieldNames

  public static String getDBFieldAliasNames(Object pojo) {
    return (String)get(pojo, ENUM_DB_FIELD_ALIAS_NAMES);
  } // end of method getDBFieldAliasNames

  public static String getQualifiedDBFieldNames(Object pojo) {
    return (String)get(pojo, ENUM_QUALIFIED_DB_FIELD_NAMES);
  } // end of method getQualifiedDBFieldNames

  public static String getQualifiedDBFieldAliasNames(Object pojo) {
    return (String)get(pojo, ENUM_QUALIFIED_DB_FIELD_ALIAS_NAMES);
  } // end of method getQualifiedDBFieldAliasNames

  public static String getQualifiedAliasedDBFieldNames(Object pojo) {
    return (String)get(pojo, ENUM_QUALIFIED_ALIASED_DB_FIELD_NAMES);
  } // end of method getQualifiedAliasedDBFieldNames

  /**
   * Returns setter methods for all the instance members (having corresponding
   * database fields) of the given <code>pojo</code>.
   * 
   * @param pojo Object whose database mapped instance members' setter methods
   * are to be returned.
   * 
   * @return Setter methods for all the instance members (having corresponding
   * database fields) of the given <code>pojo</code>.
   */
  public static Method[] getInstanceSetters(Object pojo) {
    return (Method[])get(pojo, ENUM_INSTANCE_SETTERS);
  } // end of method getInstanceSetters

  /**
   * Returns super class of the given <code>clazz</code> if it inherits another
   * database entity.
   * <p>It is checked if <code>inherits</code> field of annotation
   * {@link Entity} has a non-empty value.</p>
   *
   * @param clazz {@link Class} whose parent class is to be returned.
   *
   * @return Super class of the given <code>clazz</code> if it inherits another
   * database entity or <code>null</code>.
   */
  public static Class getSuperClass(Class clazz) {
    String inherits = (String)get(clazz.getName(), ENUM_INHERITS);
    if(inherits == null || inherits.trim().length() < 1) {
      return null;
    } // end of if
    try {
      return Class.forName(inherits);
    } catch(ClassNotFoundException e) {
      logger.warning("Super class not found: " + inherits);
      return null;
    }
  } // end of method getSuperClass

  /**
   * Returns super class of the given <code>pojo</code> if it inherits another
   * database object.
   * <p>It is checked if <code>inherits</code> field of annotation
   * {@link Entity} has a non-empty value.</p>
   *
   * @param pojo Object whose parent class is to be returned.
   *
   * @return Super class of the given <code>pojo</code> if it inherits another
   * database entity or <code>null</code>.
   */
  public static Class getSuperClass(Object pojo) {
    String inherits = (String)get(pojo, ENUM_INHERITS);
    if(inherits == null || inherits.trim().length() < 1) {
      return null;
    } // end of if
    try {
      return Class.forName(inherits);
    } catch(ClassNotFoundException e) {
      logger.warning("Super class not found: " + inherits);
      return null;
    }
  } // end of method getSuperClass

  /**
   * Returns super object of the given <code>pojo</code> if it inherits
   * another database object.
   * <p>It is checked if <code>inherits</code> field of annotation
   * {@link Entity} has a non-empty value.</p>
   * <p>NOTE: The returned super class object has no field values set in the
   * given <code>pojo</code> object. If such behavior is intended, please call
   * {@link #getSuperClassObject(Object, boolean)} method instead.</p>
   * 
   * @param pojo Object whose parent super is to be returned.
   * 
   * @return Super object of the given <code>pojo</code> if it inherits
   * another database entity or <code>null</code>.
   */
  public static Object getSuperClassObject(Object pojo) {
    return getSuperClassObject(pojo, false);
  } // end of method getSuperClassObject

  /**
   * Returns super object of the given <code>pojo</code> if it inherits
   * another database object.
   * <p>It is checked if <code>inherits</code> field of annotation
   * {@link Entity} has a non-empty value.</p>
   * <p>If <code>copyFields</code> is <code>true</code>, field values from
   * <code>pojo</code> are copied to the returned super class object before
   * returning.</p>
   *
   * @param pojo Object whose parent super is to be returned.
   * @param copyFields Whether or not to copy field values from given
   * <code>pojo</code> to returned super class object.
   *
   * @return Super object of the given <code>pojo</code> if it inherits
   * another database entity or <code>null</code>.
   */
  public static Object getSuperClassObject(Object pojo, boolean copyFields) {
    Object superClassObj = null;
    String inherits = (String)get(pojo, ENUM_INHERITS);
    if(inherits == null || inherits.trim().length() < 1) {
      return null;
    } // end of if
    try {
      superClassObj = instantiate(inherits);
    } catch (Exception ee) {}
    if(superClassObj != null && superClassObj.getClass().getName().equals(Object.class.getName()) == false) {
      if(copyFields) {
        logger.finer("About to copy [" + pojo.getClass().getName() + "] object's field values to [" + superClassObj.getClass().getName() + "] object.");
        copyFields(superClassObj, pojo);
      }
    } // end of if
    return superClassObj;
  } // end of method getSuperClassObject

  /**
   * Returns the inherited attributes from the parent entity for the given
   * <code>pojo</code>.
   *
   * @param pojo Object whose inherited attributes are to be returned.
   *
   * @return Array of inherited attribute names, or null if none.
   */
  public static String[] getInheritedAttributes(Object pojo) {
    return (String[])get(pojo, ENUM_INHERITED_ATTRIBUTES);
  } // end of method getInheritedAttributes

  /**
   * Returns whether the given <code>pojo</code> inherits the primary key
   * from its parent entity.
   *
   * @param pojo Object whose inheritPK setting is to be returned.
   *
   * @return true if the primary key is inherited, false otherwise.
   */
  public static boolean isInheritPK(Object pojo) {
    Boolean inheritPK = (Boolean)get(pojo, ENUM_INHERIT_PK);
    return inheritPK != null && inheritPK.booleanValue();
  } // end of method isInheritPK

  /**
   * Returns {@link Collection} of {@link ContainedObjectField} objects which
   * represent the contained objects in the given <code>pojo</code>.
   * 
   * @param pojo Bean whose contained objects' {@link ContainedObjectField}s are
   * to be returned.
   * 
   * @return Collection of {@link ContainedObjectField}s of the given
   * <code>pojo</code>.
   */
  public static List<ContainedObjectField> getContainedObjectFields(Object pojo) {
    return (List<ContainedObjectField>)get(pojo, ENUM_CONTAINED_OBJECT_FIELDS);
  } // end of method getContainedObjectGetters

  /**
   * Returns {@link Collection} of {@link ContainedObjectField} objects which
   * represent the contained objects in the given <code>clazz</code>.
   * 
   * @param clazz {@link Class} whose contained objects'
   * {@link ContainedObjectField}s are to be returned.
   * 
   * @return Collection of {@link ContainedObjectField}s of the given
   * <code>clazz</code>.
   */
  public static List<ContainedObjectField> getContainedObjectFields(Class clazz) {
    return (List<ContainedObjectField>)get(clazz.getName(), ENUM_CONTAINED_OBJECT_FIELDS);
  } // end of method getContainedObjectGetters

  /**
   * Returns {@link Collection} of {@link ContainedObjectField} objects which
   * represent the contained objects in the class whose name is given.
   * 
   * @param className Name of the {@link Class} whose contained objects'
   * {@link ContainedObjectField}s are to be returned.
   * 
   * @return Collection of {@link ContainedObjectField}s of the class whose name
   * is given.
   */
  public static List<ContainedObjectField> getContainedObjectFields(String className) {
    return (List<ContainedObjectField>)get(className, ENUM_CONTAINED_OBJECT_FIELDS);
  } // end of method getContainedObjectGetters

  /**
   * Returns {@link Collection} of {@link ContainedObjectField} objects which
   * represent the contained objects in the class whose name is given.
   * 
   * @param className Name of the {@link Class} whose contained objects'
   * {@link ContainedObjectField}s are to be returned.
   * 
   * @return Collection of {@link ContainedObjectField}s of the class whose name
   * is given.
   */
  public static ContainedObjectField getContainedObjectField(Object pojo, String instanceMemberName) {
    return (ContainedObjectField)get(pojo, ENUM_CONTAINED_OBJECT_FIELD_KEY_PREFIX + instanceMemberName);
  } // end of method getContainedObjectGetters

  /**
   * Returns the {@link DBField} object containing information about the
   * instance member whose name <code>instanceMemberName</code> is given.
   * 
   * @param pojo Object whose instance member's associated {@link DBField}
   * object is to be returned.
   * @param instanceMemberName Name of the instance member whose associated
   * {@link DBField} object is to be returned.
   * 
   * @return A {@link DBField} object containing information about the
   * instance member whose name <code>instanceMemberName</code> is given.
   * <p>If the underlying container does not contain the fields of the given
   * <code>pojo</code>, <code>null</code> is returned.</p>
   */
  public static DBField getFieldByInstanceMemberName(Object pojo, String instanceMemberName) {
    return (DBField)get(pojo, INSTANCE_MEMBER_NAME_KEY_PREFIX + instanceMemberName);
  } // end of method getFieldByInstanceMemberName

  /**
   * Returns the {@link DBField} object containing information about the
   * instance member whose name <code>instanceMemberName</code> is given.
   * 
   * @param pojoName Fully qualified name of the object whose instance
   * member's associated {@link DBField} object is to be returned.
   * @param instanceMemberName Name of the instance member whose associated
   * {@link DBField} object is to be returned.
   * 
   * @return A {@link DBField} object containing information about the
   * instance member whose name <code>instanceMemberName</code> is given.
   * <p>If the underlying container does not contain the fields of the POJO
   * whose name <code>pojoName</code> is given, <code>null</code> is
   * returned.</p>
   */
  public static DBField getFieldByInstanceMemberName(String pojoName, String instanceMemberName) {
    Map<String, Object> pojoFields = reflectionCache.get(pojoName);

    if(pojoFields == null) {
      return null;
    } // end of if
    return (DBField)pojoFields.get(INSTANCE_MEMBER_NAME_KEY_PREFIX + instanceMemberName);
  } // end of method getFieldByInstanceMemberName

  /**
   * Returns the {@link DBField} object containing information about the
   * mapped instance member to the database column (in the database entity
   * associated with the given <code>pojo</code>) whose name
   * <code>columnName</code> is given.
   * 
   * @param pojo Object whose instance member's (which is associated to the
   * column whose name is given) information is to be returned.
   * @param columnName Name of the database column in the database entity
   * associated with <code>pojo</code>, whose mapped instance member's
   * associated {@link DBField} object is to be returned.
   * 
   * @return A {@link DBField} object containing information about the
   * given column's mapped instance member.
   */
  public static DBField getFieldByColumnName(Object pojo, String columnName) {
    return (DBField)reflectionCache.get(pojo.getClass().getName()).get(COLUMN_NAME_KEY_PREFIX + columnName);
  } // end of method getFieldByColumnName

  /**
   * Returns the {@link DBField} object containing information about the
   * mapped database column (in the database entity named
   * <code>entityName</code>) whose name <code>columnName</code> is given.
   * 
   * @param entityName Name of the database entity whose given column's, named
   * <code>columnName</code>, information is to be returned.
   * @param columnName Name of the database column in the database entity
   * <code>entityName</code>, whose mapped {@link DBField} object is to be
   * returned.
   * 
   * @return A {@link DBField} object containing information about the
   * given column's mapped instance member.
   */
  public static DBField getDBField(String typeName, String columnName) {
    DBField field = (DBField)reflectionCache.get(typeName).get(COLUMN_NAME_KEY_PREFIX + columnName);
    if(field == null) {
      logger.warning("No keys found against column [" + columnName + "] of type [" + typeName + "]");
    } // end of if
    return field;
  } // end of method getFieldByColumnName

  /**
   * Returns {@link List} of {@link DBField} objects representing instance
   * members of the given <code>pojo</code> mapped to database fields.
   * 
   * @param pojo Object whose database mapped instance members are to be
   * returned.
   * 
   * @return Database mapped instance members of the given <code>pojo</code>.
   */
  public static List<DBField> getFields(Object pojo) {
    return (List<DBField>)Collections.unmodifiableList((List<DBField>)get(pojo, ENUM_DB_FIELDS));
    //return (List<DBField>)reflectionCache.get(pojo.getClass().getName()).get(ENUM_DB_FIELDS);
  } // end of method getFields
  
  /**
   * Returns {@link List} of {@link InstanceMember} objects representing instance
   * members of the given <code>pojo</code>.
   * 
   * @param pojo Object whose instance members are to be returned.
   * 
   * @return Instance members of the given <code>pojo</code>.
   */
  public static List<InstanceMember> getInstanceMembers(Object pojo) {
    return (List<InstanceMember>)Collections.unmodifiableList((List<InstanceMember>)get(pojo, ENUM_INSTANCE_MEMBERS));
  } // end of method getFields
  
  /**
   * Returns {@link List} of {@link InstanceMember} objects representing instance
   * members of the given <code>pojo</code>.
   * 
   * @param pojo Object whose instance members are to be returned.
   * 
   * @return Instance members of the given <code>pojo</code>.
   */
  public static List<InstanceMember> getInstanceMembers(String className) {
    return (List<InstanceMember>)Collections.unmodifiableList((List<InstanceMember>)get(className, ENUM_INSTANCE_MEMBERS));
  } // end of method getFields
  
  /**
   * Returns {@link List} of {@link DBField} objects representing instance
   * members of the given <code>pojo</code> mapped to database fields.
   * 
   * @param pojo Object whose database mapped instance members are to be
   * returned.
   * 
   * @return Database mapped instance members of the given <code>pojo</code>.
   */
  public static List<ForeignKeyField> getForeignKeyFields(Object pojo) {
    return (List<ForeignKeyField>)Collections.unmodifiableList((List<ForeignKeyField>)get(pojo, ENUM_FOREIGN_KEY_FIELDS ));
  } // end of method getFields
  
  /**
   * Returns all the field of the <code>pojo</code> which are foreign keys
   * from the underlying table represented by <code>superObj</code> and have
   * the type as given <code>fieldType</code>.
   * 
   * @param superObj Object representing parent table.
   * @param pojo Object representing child table.
   * @param fieldType Type of the foreign key fields.
   * 
   * @return A {@link List> of {@link DBField} object representing foreign
   * keys in the underlying table represented by <code>pojo</code> from the
   * underlying table represented by <code>superObj</code> having type
   * <code>fieldType</code>.
   */
  public static List<ForeignKeyField> getForiengKeyFields(Object superObj, Object pojo, Class fieldType) {
    List<ForeignKeyField> returnForeignKeyFields = new ArrayList<ForeignKeyField>();
    List<ForeignKeyField> foreignKeysFields = getForeignKeyFields(pojo);
    
    DBField superPKField = getPrimaryKeyField(superObj);
    
    for (ForeignKeyField field : foreignKeysFields) {
      
      if(field.getField().getType().equals(fieldType) &&
          field.getReferencedEntity().equals(superPKField.getEntityName()) &&
          field.getReferencedField().equals(superPKField.getDbFieldName())) {
        returnForeignKeyFields.add(field);
      } // end of if
    } // end of for
    return (List<ForeignKeyField>)Collections.unmodifiableList((List<ForeignKeyField>)returnForeignKeyFields);
  } // end of method getForiengKeyFields

  /**
   * Returns the {@link ContainedObjectField} instance which is reciprocal to
   * the given <code>field</code> object in contained class.
   * <p>For example, if class A contains object(s) of class B (B's corresponding
   * table has A's corresponding table's primary key as foreign key) and class B
   * also contains reference to its super class, then the instance member
   * representing contained object(s) (of class B) in class A and the instance
   * member representing contained object (of class A) in class B are defined as
   * reciprocal contained objects.</p>
   * 
   * @param field {@link ContainedObjectField} whose reciprocal
   * {@link ContainedObjectField} is to be returned.
   * 
   * @return Reciprocal {@link ContainedObjectField} of the given
   * <code>field</code> or <code>null</code> if the reciprocal contained object
   * had not been defined.
   */
  public static ContainedObjectField getReciprocalContainedObjectField(ContainedObjectField field) {
    DBField relatedField = getDBField(field.getContainedObjectType(), field.getReferencedField());
    if(relatedField == null) {
      return null;
    } // end of if
    String relatedFieldName = relatedField.getField().getName();
    List<ContainedObjectField> containedObjFields = getContainedObjectFields(relatedField.getPojo());
    String relatedInstanceMemberName = null;
    for (ContainedObjectField containedObjField : containedObjFields) {
      relatedInstanceMemberName = containedObjField.getRelatedInstanceMemberName();
      if(relatedFieldName.equals(relatedInstanceMemberName)) {
        return containedObjField;
      } // end of if
    } // end of for
    return null;
  } // end of method getReciprocalContainedObjectField
  
  /**
   * Returns the corresponding object for the given key stored in the
   * {@link Map} for the given <code>pojo</code>.
   * <p>It is a general purpose method to give the user access to the
   * underlying {@link #reflectionCache} storing ORM information of the fields.</p>
   * 
   * @param pojo Object whose corresponding object stored against given
   * <code>key</code> is to be returned.
   * @param key Key against which the store object for the given
   * <code>pojo</code> is to be returned.
   * 
   * @return An object for the given key stored in the {@link Map} for the given
   * <code>pojo</code>.
   */
  protected static Object get(Object pojo, String key) {
    String name = pojo.getClass().getName();
    Map<String, Object> pojoMap = reflectionCache.get(name);
    if(pojoMap == null) {
      introspect(pojo);
      pojoMap = reflectionCache.get(name);
    } // end of if
    return pojoMap.get(key);
  } // end of method get

  /**
   * Returns the corresponding object for the given key stored in the
   * {@link Map} against the given <code>pojoName</code>.
   * <p>It is a general purpose method to give the user access to the
   * underlying {@link #reflectionCache} storing ORM information of the fields.</p>
   * 
   * @param pojoName Key in the {@link #reflectionCache} whose corresponding object stored
   * against given <code>key</code> is to be returned.
   * @param key Key against which the store object for the given
   * <code>pojoName</code> is to be returned.
   * 
   * @return An object for the given key stored in the {@link Map} for the given
   * <code>pojoName</code>.
   */
  protected static Object get(String pojoName, String key) {
    Map<String, Object> pojoMap = reflectionCache.get(pojoName);
    if(pojoMap == null) {
      introspect(instantiate(pojoName));
      pojoMap = reflectionCache.get(pojoName);
    } // end of if
    return pojoMap.get(key);
  } // end of method get

  /**
   * Introspects the class, whose fully qualified name is given, to extract and
   * cache metadata about its structure, including database mappings,
   * annotations, and relationships. This method analyzes the class's structure,
   * processes field annotations, and builds metadata for ORM (Object-Relational
   * Mapping) operations.
   *
   * The method performs the following main operations:
   * <ol><li>Checks if the class has already been introspected to avoid
   * redundant processing.</li>
   * <li>Extracts entity name and inheritance information from class-level
   * annotations.</li>
   * <li>Processes all declared fields to identify database mappings and
   * relationships.</li>
   * <li>Builds various SQL field name representations for query construction.</li>
   * <li>Recursively introspects contained objects to build a complete object
   * graph.</li>
   * <li>Caches all extracted metadata in a global reflectionCache for future reference.</li></ol>
   *
   * @param pojo The object to introspect. If null or already processed, the method returns early.
   *
   * Processing Flow:
   * 1. Early Exit: Returns if pojo is null or already cached in the global reflectionCache.
   * 2. Entity Identification: Determines the entity name from @Entity annotation or class name.
   * 3. Field Processing: Iterates through all fields to identify:
   *    - Database fields (@Field, @PrimaryKey, @Unique annotations)
   *    - Foreign key relationships (@ForeignKey annotation)
   *    - Contained objects (@ContainedObject annotation)
   * 4. Metadata Aggregation: Collects processed field information into structured lists.
   * 5. SQL String Building: Constructs various formatted field name strings for SQL queries.
   * 6. Cache Storage: Stores all metadata in a global reflectionCache with multiple lookup keys.
   * 7. Recursive Processing: Introspects contained objects to ensure complete metadata.
   *
   * Metadata Structure:
   * The method builds a TreeMap containing:
   * - Entity name and inheritance information
   * - Lists of database fields, foreign keys, and contained objects
   * - Primary key information
   * - Multiple SQL field name representations
   * - Setter methods for field population
   *
   * Cache Keys:
   * - Class name: Fully qualified class name as primary key
   * - Entity name: "ENUM_ENTITY_NAME-{entityName}" as secondary key
   *
   * Recursive Behavior:
   * For contained objects (via @ContainedObject annotation), recursively introspects
   * the contained type if not already cached. Handles arrays and Collection types.
   *
   * Error Handling:
   * - Logs warnings for missing type information in contained objects
   * - Silently catches exceptions during recursive introspection (InstantiationException,
   *   InvocationTargetException, NoSuchMethodException, ClassNotFoundException,
   *   IllegalAccessException) and continues processing
   *
   * Thread Safety Note:
   * This method modifies a shared static reflectionCache. Concurrent access should be synchronized
   * externally if used in multi-threaded environments.
   *
   * Dependencies:
   * - Relies on various annotation types (Entity, Field, PrimaryKey, Unique, ForeignKey, ContainedObject)
   * - Uses utility classes: StringUtils for SQL wrapping, Utility for class checking
   * - Depends on SQL constants from sqlConstantsObj for query formatting
   *
   * Side Effects:
   * - Modifies the static reflectionCache field by adding new metadata entries
   * - May recursively create and introspect new object instances for contained types
   * - Logs warnings to the logger for problematic configurations
   */
  protected static void introspect(Object pojo) {
    if(pojo == null) return;
    if(reflectionCache.containsKey(pojo.getClass().getName())) return;

    if(classLoader == null) {
      classLoader = pojo.getClass().getClassLoader();
      logger.finer("ClassLoader set to " + classLoader.getName());
    }

    logger.finer("Introspecting class " + pojo.getClass().getName());
    String entityName = pojo.getClass().getSimpleName();
    String inherits = null;
    Class inheritsClass = null;
    String[] inheritedAttributes = null;
    boolean inheritPK = false;
    Entity annoEntity = pojo.getClass().getAnnotation(Entity.class);
    if(annoEntity != null) {
      if(annoEntity.name() != null && annoEntity.name().trim().length() > 0) {
        entityName = annoEntity.name();
      }
      inheritsClass = annoEntity.inherits();
      if(inheritsClass != null && inheritsClass.equals(Object.class) == false) {
        inherits = inheritsClass.getName();
      }
      inheritedAttributes = annoEntity.inheritedAttributes();
      inheritPK = annoEntity.inheritPK();
    } // end of if
    
    
    Field[] fields = pojo.getClass().getDeclaredFields();
    
    // Map to contain all possible information of the given object for later use.
    Map<String, Object> map1 = new TreeMap<String, Object>();
    List<DBField> dbFields = new ArrayList<DBField>();
    List<InstanceMember> instanceMembers = new ArrayList<InstanceMember>();
    List<ContainedObjectField> containedObjects = new ArrayList<ContainedObjectField>();
    List<ForeignKeyField> foreignKeys = new ArrayList<ForeignKeyField>();
    DBField primaryKey = null;
    String columnName = null;
    
    for (int i = 0; i < fields.length; i++) {
      Field fd = fields[i];
      columnName = fd.getName();
      com.sa.orm.reflect.annotation.Field annoField = fd.getAnnotation(com.sa.orm.reflect.annotation.Field.class);
      ContainedObject contained = fd.getAnnotation(ContainedObject.class);
      PrimaryKey annoPK = fd.getAnnotation(PrimaryKey.class);
      Unique annoUnique = fd.getAnnotation(Unique.class);
      ForeignKey annoFK = fd.getAnnotation(ForeignKey.class);

      if(annoField == null && annoPK == null && annoUnique == null && contained == null && annoFK == null) {
        instanceMembers.add(new InstanceMember(pojo, fd));
        continue;
      } // end of if

      if(contained != null) {
        int relationship = contained.relationshipWithContainedObject();
        String containedType = contained.type();
        if(fd.getType().isArray() == false && Utility.isChildClass(fd.getType(), Collection.class) == false && (containedType == null || containedType.trim().length() < 1)) {
          containedType = fd.getType().getName();
        }
        if(containedType == null || containedType.trim().length() < 1) {
          logger.warning("Type of field [] in [] could not known. In case of Collection (any child class of collection classes in java.util) of contained child objects, please specify the type in the annotations.");
        }
        String instanceMemberName = contained.localInstanceMember();
        ContainedObjectField containedObjectField = new ContainedObjectField(pojo, fd, entityName, fd.getName(), contained.referencedEntity(), contained.referencedField(), containedType, instanceMemberName, relationship);
        logger.finer("Contained Field: " + containedObjectField.toString());
        containedObjects.add(containedObjectField);
        map1.put(ENUM_CONTAINED_OBJECT_FIELD_KEY_PREFIX + fd.getName(), containedObjectField);
        continue;
      } // end of if

      DBField dbField = null;
      if(annoField != null) {
        if(annoField.name() != null && annoField.name().trim().length() > 0) {
          columnName = annoField.name().trim();
        }
        String dbFieldName = columnName;
        if(annoField.name() != null && annoField.name().trim().length() > 0) {
          dbFieldName = annoField.name();
        } // end of if
        dbField = new DBField(pojo, fd, entityName, dbFieldName);
      }

      if(annoPK != null) {
        if(dbField == null) {
          dbField = new DBField(pojo, fd, entityName, columnName);
        }
        dbField.setConstraintName(annoPK.constraintName());

        primaryKey = dbField;
      } // end of if
      if(annoUnique != null) {
        if(dbField == null) {
          dbField = new DBField(pojo, fd, entityName, columnName);
        }
        if(annoUnique.constraintName() != null && annoUnique.constraintName().trim().length() > 0) {
          dbField.setConstraintName(annoUnique.constraintName());
        }
      }
      if(annoFK != null) {
        // If dbField already exists (e.g., from @PrimaryKey), convert it to ForeignKeyField
        if(dbField != null) {
          String oldDbFieldName = dbField.getDbFieldName();
          String oldConstraintName = dbField.getConstraintName();
          dbField = new ForeignKeyField(pojo, fd, entityName, oldDbFieldName, annoFK.referenceEntity(), annoFK.referencedField());
          // Preserve constraint name if it was set by @PrimaryKey or @Unique
          if(oldConstraintName != null) {
            dbField.setConstraintName(oldConstraintName);
          }
          // If this field was marked as primary key, update primaryKey reference to the new ForeignKeyField
          if(primaryKey != null && primaryKey.getDbFieldName().equals(oldDbFieldName)) {
            primaryKey = dbField;
          }
        } else {
          dbField = new ForeignKeyField(pojo, fd, entityName, columnName, annoFK.referenceEntity(), annoFK.referencedField());
        }
        if(annoFK.constraintName() != null && annoFK.constraintName().trim().length() > 0) {
          dbField.setConstraintName(annoFK.constraintName());
        } // end of if
        foreignKeys.add((ForeignKeyField)dbField);
      } // end of if
      map1.put(INSTANCE_MEMBER_NAME_KEY_PREFIX + dbField.getInstanceMemberName(), dbField);
      map1.put(COLUMN_NAME_KEY_PREFIX + dbField.getDbFieldName(), dbField);
      instanceMembers.add(dbField);
      dbFields.add(dbField);
    } // end of for

    // Handle inheritance: include inherited PK and inherited attributes
    if(inheritsClass != null && inheritsClass.equals(Object.class) == false) {
      try {
        Object parentObj = instantiate(inheritsClass.getName());
        if(parentObj != null) {
          // Ensure parent is introspected first
          if(reflectionCache.containsKey(inheritsClass.getName()) == false) {
            introspect(parentObj);
          }

          // Include inherited primary key if inheritPK is true
          if(inheritPK) {
            DBField parentPKField = getPrimaryKeyField(parentObj);
            if(parentPKField != null) {
              // Create a ForeignKeyField for the inherited PK
              ForeignKeyField inheritedPKField = new ForeignKeyField(
                  pojo,
                  parentPKField.getField(),
                  entityName,
                  parentPKField.getDbFieldName(),
                  parentPKField.getEntityName(),
                  parentPKField.getDbFieldName()
              );
              // Add to the beginning of the lists
              dbFields.add(0, inheritedPKField);
              instanceMembers.add(0, inheritedPKField);
              map1.put(INSTANCE_MEMBER_NAME_KEY_PREFIX + inheritedPKField.getInstanceMemberName(), inheritedPKField);
              map1.put(COLUMN_NAME_KEY_PREFIX + inheritedPKField.getDbFieldName(), inheritedPKField);
              foreignKeys.add(inheritedPKField);
              // Set the inherited PK as the primary key for this entity
              primaryKey = inheritedPKField;
              logger.finer("Added inherited PK [" + inheritedPKField.getInstanceMemberName() + "] to [" + pojo.getClass().getName() + "]");
            } else {
              logger.warning("Parent class [" + inheritsClass.getName() + "] does not have a primary key defined. inheritPK=true will not work correctly.");
            }
          }

          // Include inherited attributes
          if(inheritedAttributes != null && inheritedAttributes.length > 0) {
            List<DBField> parentFields = getFields(parentObj);
            for(String attrName : inheritedAttributes) {
              for(DBField parentField : parentFields) {
                if(parentField.getInstanceMemberName().equals(attrName)) {
                  // Check if this field is not already in the child's fields
                  boolean alreadyExists = false;
                  for(DBField existingField : dbFields) {
                    if(existingField.getInstanceMemberName().equals(attrName)) {
                      alreadyExists = true;
                      break;
                    }
                  }
                  if(!alreadyExists) {
                    // Create a new DBField for the inherited attribute
                    DBField inheritedField = new DBField(
                        pojo,
                        parentField.getField(),
                        entityName,
                        parentField.getDbFieldName()
                    );
                    dbFields.add(inheritedField);
                    instanceMembers.add(inheritedField);
                    map1.put(INSTANCE_MEMBER_NAME_KEY_PREFIX + inheritedField.getInstanceMemberName(), inheritedField);
                    map1.put(COLUMN_NAME_KEY_PREFIX + inheritedField.getDbFieldName(), inheritedField);
                    logger.finer("Added inherited attribute [" + inheritedField.getInstanceMemberName() + "] to [" + pojo.getClass().getName() + "]");
                  }
                  break;
                }
              }
            }
          }
        }
      } catch(Exception e) {
        logger.warning("Error processing inheritance for [" + pojo.getClass().getName() + "]: " + e.getMessage());
      }
    } // end of if inheritance

    StringBuilder plainFieldNames = new StringBuilder();
    StringBuilder wrappedFieldNames = new StringBuilder();
    StringBuilder aliasNames = new StringBuilder();
    StringBuilder qualifiedAliasNames = new StringBuilder();
    StringBuilder qualifiedFieldNames = new StringBuilder();
    StringBuilder qualifiedAliasedFieldNames = new StringBuilder();
    List<Method> setters = new ArrayList<Method>();
    Method setterMethod = null;
    for (int i = 0; i < dbFields.size(); i++) {
      DBField field = dbFields.get(i);
      if(i > 0) {
        plainFieldNames.append(sqlConstantsObj.getFieldsSeparator());
        wrappedFieldNames.append(sqlConstantsObj.getFieldsSeparator());
        aliasNames.append(sqlConstantsObj.getFieldsSeparator());
        qualifiedAliasNames.append(sqlConstantsObj.getFieldsSeparator());
        qualifiedFieldNames.append(sqlConstantsObj.getFieldsSeparator());
        qualifiedAliasedFieldNames.append(sqlConstantsObj.getFieldsSeparator());
      } // end of if

      setterMethod = field.getSetterMethod();
      setters.add(setterMethod);
      
      String fieldName = field.getDbFieldName();
      plainFieldNames.append(fieldName);
      wrappedFieldNames.append(StringUtils.wrap(fieldName, sqlConstantsObj.getFieldPrefix(), sqlConstantsObj.getFieldSuffix()));
      aliasNames.append(fieldName);
      qualifiedAliasNames.append(getQualifiedAlias(entityName, fieldName));
      qualifiedFieldNames.append(getQualifiedField(entityName, fieldName, true));
      qualifiedAliasedFieldNames.append(getQualifiedAliasedField(entityName, fieldName, true));
    } // end of for
    
    map1.put(ENUM_ENTITY_NAME, entityName);
    map1.put(ENUM_INHERITS, inherits);
    map1.put(ENUM_INHERITED_ATTRIBUTES, inheritedAttributes);
    map1.put(ENUM_INHERIT_PK, inheritPK);
    map1.put(ENUM_PRIMARY_KEY, primaryKey);
    
    // Handle null primaryKey - this should not happen for entities with @PrimaryKey
    if(primaryKey == null && dbFields.isEmpty() == false) {
      logger.warning("Primary key is null for entity [" + entityName + "]. This may cause issues.");
    }
    
    if(primaryKey != null) {
      map1.put(ENUM_QUALIFIED_PRIMARY_KEY, entityName + sqlConstantsObj.getQualifierSeparator() + primaryKey.getDbFieldName());
      map1.put(ENUM_QUALIFIED_PRIMARY_KEY_ALIAS, entityName + sqlConstantsObj.getQualifierSeparatorReplaced() + primaryKey.getDbFieldName());
    }
    map1.put(ENUM_DB_FIELDS, dbFields);
    map1.put(ENUM_INSTANCE_MEMBERS, instanceMembers);
    map1.put(ENUM_FOREIGN_KEY_FIELDS, foreignKeys);
    
    map1.put(ENUM_PLAIN_DB_FIELD_NAMES, plainFieldNames.toString());
    map1.put(ENUM_WRAPPED_DB_FIELD_NAMES, wrappedFieldNames.toString());
    map1.put(ENUM_DB_FIELD_ALIAS_NAMES, aliasNames.toString());
    map1.put(ENUM_QUALIFIED_DB_FIELD_ALIAS_NAMES, qualifiedAliasNames.toString());
    map1.put(ENUM_QUALIFIED_DB_FIELD_NAMES, qualifiedFieldNames.toString());
    map1.put(ENUM_QUALIFIED_ALIASED_DB_FIELD_NAMES, qualifiedAliasedFieldNames.toString());

    map1.put(ENUM_INSTANCE_SETTERS, (Method[])setters.toArray(new Method[0]));
    map1.put(ENUM_CONTAINED_OBJECT_FIELDS, containedObjects);
    
    reflectionCache.put(pojo.getClass().getName(), map1);
    reflectionCache.put(ENUM_ENTITY_NAME + "-" + entityName, map1);
    
    for (ContainedObjectField containedObjField : containedObjects) {
      String containedType = containedObjField.getContainedObjectType();
      Field fd = containedObjField.getField();
      if(fd.getType().isArray() == false && Utility.isChildClass(fd.getType(), Collection.class) == false && (containedType == null || containedType.trim().length() < 1)) {
        containedType = containedObjField.getPojo().getClass().getName();
      }
      if(containedType == null || containedType.trim().length() < 1) {
        continue;
      } // end of if
      if(reflectionCache.containsKey(containedType) == false) {
        introspect(instantiate(containedObjField.getContainedObjectType()));
      } // end of if
    } // end of for
  } // end of method introspect

  /**
   * Copies the values of instance members of <code>src</code> to the same
   * named instance members of <code>dest</code>.
   * <p>Please note that only those instance member values are copied which
   * have mapped database fields.</p>
   * 
   * @param dest Object to which the instance member values are to be copied.
   * @param src Object from which the values of instance members are to be
   * copied.
   */
  public static void copyFields(Object dest, Object src) {
    List<DBField> fields = getFields(dest);
    Object value = null;
    for (DBField dbField : fields) {
      try {
        value = Utility.invokeMethod(src, dbField.getGetterMethod());
        Utility.invokeMethod(dest, dbField.getSetterMethod(), new Object[] {value});
      } catch(Exception eee) {
        logger.warning("Could not copy [" + dbField.getInstanceMemberName() + "] value from [" + src.getClass().getName() + "] object to [" + dest.getClass().getName() + "] object.");
        StackTraceElement rootCause = eee.getStackTrace()[eee.getStackTrace().length - 1];
        logger.warning(rootCause.getClassName() + " thrown with message [" + eee.getMessage() + "].");
        logger.warning(rootCause.toString());
      } // end of catch
    } // end of for
  } // end of method copyFields

  /**
   * Returns {@link List} of primary key values of the objects in given array
   * <code>objs</code>.
   * 
   * @param objs Array whose objects' primary key values are to be returned as
   * a {@link List}.
   * 
   * @return List of primary key values of the given objects.
   */
  public static List getIds(Collection objs) {
    if(objs == null) {
      return null;
    } // end of if
    return getIds(objs.toArray(new Object[0]));
  } // end of method getIds
  
  /**
   * Returns {@link List} of primary key values of the objects in given array
   * <code>objs</code>.
   * 
   * @param objs Array whose objects' primary key values are to be returned as
   * a {@link List}.
   * 
   * @return List of primary key values of the given objects.
   */
  public static List getIds(Object[] objs) {
    if(objs == null) {
      return null;
    } // end of if
    
    List ids = new ArrayList();
    if(objs.length < 1) {
      return ids;
    } // end of if
    
    DBField pkField = (DBField)get(objs[0], ENUM_PRIMARY_KEY);
    for (int i = 0; i < objs.length; i++) {
      ids.add(Utility.invokeMethod(objs[i], pkField.getGetterMethod()));
    } // end of for
    return ids;
  } // end of method getIds
  
  public static List getFieldValues(Object[] objs, DBField field) {
    if(objs == null) {
      return null;
    } // end of if
    
    List ids = new ArrayList();
    if(objs.length < 1) {
      return ids;
    } // end of if
    
    for (int i = 0; i < objs.length; i++) {
      ids.add(Utility.invokeMethod(objs[i], field.getGetterMethod()));
    } // end of for
    return ids;
  } // end of method getIds
  
  public static String getQualifiedField(String entityName, String fieldName, boolean wrap) {
    StringBuffer sb = new StringBuffer();
    String wrapperPrefix = wrap ? sqlConstantsObj.getFieldPrefix() : "";
    String wrapperSuffix = wrap ? sqlConstantsObj.getFieldSuffix() : "";
    sb.append(StringUtils.wrapDBField(entityName, fieldName, wrapperPrefix, wrapperSuffix, sqlConstantsObj.getQualifierSeparator()));
    return sb.toString();
  } // end of method getQualifiedField
  
  private static String getQualifiedAliasedField(String entityName, String fieldName, boolean wrap) {
    StringBuffer sb = new StringBuffer();
    String wrapperPrefix = wrap ? sqlConstantsObj.getFieldPrefix() : "";
    String wrapperSuffix = wrap ? sqlConstantsObj.getFieldSuffix() : "";
    sb.append(StringUtils.wrapDBField(entityName, fieldName, wrapperPrefix, wrapperSuffix, sqlConstantsObj.getQualifierSeparator()));
    sb.append(sqlConstantsObj.getAliasSeparator());
    sb.append(StringUtils.wrapDBField(null, entityName + sqlConstantsObj.getQualifierSeparatorReplaced() + fieldName, wrapperPrefix, wrapperSuffix, sqlConstantsObj.getQualifierSeparator()));
    return sb.toString();
  } // end of method getQualifiedAliasedField
  
  private static String getQualifiedAlias(String entityName, String fieldName) {
    StringBuffer sb = new StringBuffer();
    sb.append(entityName);
    sb.append(sqlConstantsObj.getQualifierSeparatorReplaced());
    sb.append(fieldName);
    return sb.toString();
  } // end of method getQualifiedAlias
  
  /**
   * Returns actual class to resolve the issue of automatic conversion
   * , of JVM, from primitive type to wrapper class.
   * 
   * @param clazz Object whose actual class object is to be returned.
   * 
   * @return Actual class object of the given object in case the given
   * class represents a primitive. Otherwise same is returned.
   */
  public static Class getActualClass(Class clazz) {
    if(clazz.isPrimitive()) {
      char firstChar = clazz.getName().charAt(0);
      switch (firstChar) {
        case 'I':
          return int.class;
        case 'L':
          return long.class;
        case 'S':
          return short.class;
        case 'B':
          return byte.class;
        case 'f':
          return float.class;
        case 'D':
          return double.class;
        case 'C':
          return char.class;
      } // end of switch
    } // end of if primitive
    return clazz;
  } // end of method getActualClass

  /**
   * Returns the value of the primary key of the given object.
   * 
   * @param pojo Object whose primary key value is to be returned.
   * @return Value of the primary key of the given object.
   * 
   * @throws ReflectionException In case the primary key's getter method does
   * not have <code>public</code> access modifier or the given object is
   * <code>null</code>.
   */
  public static Object getPrimaryKeyValue(Object pojo) throws ReflectionException {
    try {
      DBField pkField = (DBField)get(pojo, ENUM_PRIMARY_KEY);
      return pkField.getGetterMethod().invoke(pojo, new Object[] {});
    } catch(InvocationTargetException e) {
      throw new ReflectionException(e);
    } catch(IllegalAccessException e) {
      throw new ReflectionException(e);
    }
  } // end of method getPrimaryKeyValue

  /**
   * Returns the value of the primary key of the given <code>clazz</code> from
   * the given <code>pojo</code>.
   * 
   * @param clazz Class whose instance's primary key is to be returned.
   * @param pojo Object containing the primary key value that is to be returned.
   * @return Value of the primary key of the given object.
   * 
   * @throws ReflectionException In case the primary key's getter method does
   * not have <code>public</code> access modifier or the given object is
   * <code>null</code>.
   */
  public static Object getPrimaryKeyValue(Class clazz, Object pojo) throws ReflectionException {
    try {
      DBField pkField = (DBField)get(clazz.getName(), ENUM_PRIMARY_KEY);
      return pkField.getGetterMethod().invoke(pojo, new Object[] {});
    } catch(InvocationTargetException e) {
      throw new ReflectionException(e);
    } catch(IllegalAccessException e) {
      throw new ReflectionException(e);
    }
  } // end of method getPrimaryKeyValue

  /**
   * Sets the given <code>pkValue</code> in the primary key field of the given
   * object <code>pojo</code>.
   * 
   * @param pojo Object whose primary key value is to be set.
   * @param pkValue Value to be set in the primary key field of the given
   * <code>pojo</code>.
   * 
   * @throws ReflectionException In case the primary key's getter method does
   * not have <code>public</code> access modifier or the given object is
   * <code>null</code>.
   */
  public static void setPrimaryKeyValue(Object pojo, Object pkValue) throws ReflectionException {
    setInstanceMemberValue(pojo, getPrimaryKeyField(pojo), pkValue);
  } // end of method setPrimaryKeyValue

  /**
   * Sets the given <code>value</code> in the instance member encapsulated by
   * the given <code>instanceMember</code> in the given object
   * <code>pojo</code>.
   * 
   * @param pojo Object whose given <code>instanceMember</code>'s value is to
   * be set.
   * @param instanceMember {@link DBField} object encapsulating instance member
   * whose value is to be set to given <code>value</code>.
   * @param value Value to be set in the given instance member of the given
   * object <code>pojo</code>.
   * 
   * @throws ReflectionException In case the instance member's setter method
   * does not exist or does  not have <code>public</code> access modifier.
   */
  public static void setInstanceMemberValue(Object pojo, DBField instanceMember, Object value) throws ReflectionException {
    try {
      instanceMember.getSetterMethod().invoke(pojo, new Object[] {value});
    } catch(InvocationTargetException e) {
      throw new ReflectionException(e);
    } catch(IllegalAccessException e) {
      throw new ReflectionException(e);
    }
  } // end of method setInstanceMemberValue

  /**
   * Sets the value of the all the instance members mapped to foreign keys in
   * the given object <code>pojo</code> from the underlying table of given
   * object <code>parentObj</code> with the given value
   * <code>foreignKeyValue</code>.
   * 
   * @param pojo Object whose instance members', which are mapped to foreign
   * keys from the underlying table of the given <code>parentObj</code>,
   * values are to be set to the given value <code>foreignKeyValues</code>.
   * @param parentObj Object whose underlying table is the parent table of the
   * underlying table of the given object <code>pojo</code>.
   * @param foreignKeyValue Instance members's value to be set in object
   * <code>pojo</code>.
   * 
   * @throws IllegalAccessException In case the setter method is not
   * accessible.
   * @throws InvocationTargetException In case there is no method in the
   * target <code>pojo</code> which is being invoked.
   */
  public static void setForeignKeyValue(Object pojo, Object parentObj, Object foreignKeyValue)
  throws IllegalAccessException, InvocationTargetException {
    List<ForeignKeyField> foreignKeys = getForiengKeyFields(parentObj, pojo, foreignKeyValue.getClass());
    for (ForeignKeyField field : foreignKeys) {
      field.getSetterMethod().invoke(pojo, new Object[] {foreignKeyValue});
    } // end of for
  } // end of method setForiegnKeyValue

  /**
   * Sets the value of the primary key of the given <code>superObj</code>in the
   * given <code>childObj</code>.
   * <p>It is assumed that the <code>childObj</code> has the same name of the
   * foreign key as the name of the primary key in <code>superObj</code>.</p>
   * 
   * @param childObj Object in which the super object's primary key value is
   * to be set.
   * @param superObj Object whose primary key value is to be set in respective
   * field in <code>childObj</code>.
   * 
   * @throws IllegalAccessException In case the setter method is not
   * accessible.
   * @throws InvocationTargetException In case the given <code>childObj</code>
   * does no the contain the setter method.
   * @throws NoSuchMethodException In case the setter method is not defined.
   */
  public static void setSuperObjectPKValue(Object childObj, Object superObj)
  throws IllegalAccessException, InvocationTargetException, NoSuchMethodException {
      //set pk value of super object in child object.
      String superObjectPKName = ORMInfoManager.getPrimaryKeyName(superObj);
      DBField childFKField = ORMInfoManager.getFieldByInstanceMemberName(childObj, superObjectPKName);
      Object superObjPKValue = ORMInfoManager.getPrimaryKeyValue(superObj);
      logger.finer("About to set super object primary key [" + superObjectPKName + ":" + superObjPKValue + "] in extended object of type [" + childObj.getClass().getName() + "] in [" + childFKField.getInstanceMemberName() + "] field.");
      Utility.invokeMethod(childObj, childFKField.getSetterMethod(), new Object[] {superObjPKValue});
      logger.finer("Set [" + superObjPKValue + "] in field [" + childFKField.getInstanceMemberName() + "] of [" + childObj.getClass().getName() + "] object.");
  } // end of method setSuperObjectPKValue

  /**
   * Returns <code>true</code> if the value of primary key in the given
   * <code>pojo</code> is set.
   * <p>In case primary key is an object and its value is <code>null</code> in
   * the given <code>pojo</code>, <code>false</code> is returned.</p>
   * <p>If the type of primary key is any one of numeric primitive types or
   * wrapper classes, and the value is zero, <code>false</code> is returned.</p>
   * <p>In case the instance member is of type {@link String} and contains an
   * empty string or white spaces only, <code>false</code> is returned.</p>
   * 
   * @param pojo Object whose primary key value is to be checked whether set or
   * not.
   * 
   * @return <code>true</code> if the primary key value is set in the given
   * <code>pojo</code>. <code>false</code> otherwise.
   */
  public static boolean isPrimaryKeyValueSet(Object pojo) {
    return isInstanceMemberValueSet(pojo, getPrimaryKeyField(pojo));
  } // end of method isPrimaryKeyValueSet
  
  /**
   * Returns <code>true</code> if the value of the <code>instanceMember</code>
   * in the given <code>pojo</code> is set or not.
   * <p>In case instance member is an object and its value is <code>null</code>
   * in the given <code>pojo</code>, <code>false</code> is returned.</p>
   * <p>If the type of instance member is any one of numeric primitive types or
   * wrapper classes, and the value is zero, <code>false</code> is returned.</p>
   * <p>In case the instance member is of type {@link String} and contains an
   * empty string or white spaces only, <code>false</code> is returned.</p>
   * 
   * @param pojo Object whose instance member's value is to be checked whether
   * set or not.
   * @param instanceMember Name of the instance member whose value is to be
   * checked whether set or not in the given object <code>pojo</code>.
   * 
   * @return <code>true</code> if the instance member value is set in the given
   * <code>pojo</code>. <code>false</code> otherwise.
   */
  public static boolean isInstanceMemberValueSet(Object pojo, String instanceMember) {
    DBField dbField = getFieldByInstanceMemberName(pojo, instanceMember);
    return isInstanceMemberValueSet(pojo, dbField);
  } // end of method isInstanceMemberValueSet
  
  /**
   * Returns <code>true</code> if the value of the instance member, whose name
   * is given as <code>fieldName</code>, in the given <code>pojo</code> is set
   * or not.
   * <p>In case instance member is an object and its value is <code>null</code>
   * in the given <code>pojo</code>, <code>false</code> is returned.</p>
   * <p>If the type of instance member is any one of numeric primitive types or
   * wrapper classes, and the value is zero, <code>false</code> is returned.</p>
   * <p>In case the instance member is of type {@link String} and contains an
   * empty string or white spaces only, <code>false</code> is returned.</p>
   * 
   * @param pojo Object whose instance member's value is to be checked whether
   * set or not.
   * @param dbField {@link DBField} encapsulating instance member whose value is
   * to be checked whether set or not in the given object <code>pojo</code>.
   * 
   * @return <code>true</code> if the instance member value is set in the given
   * <code>pojo</code>. <code>false</code> otherwise.
   */
  public static boolean isInstanceMemberValueSet(Object pojo, DBField dbField) {
    if(pojo == null || dbField == null) {
      return false;
    }
    Object fieldValue = Utility.invokeMethod(pojo, dbField.getGetterMethod());
    if(fieldValue == null) {
      return false;
    } // end of if
    if(Double.parseDouble(fieldValue.toString()) == 0.0d) {
      return false;
    }
    if(fieldValue instanceof String && ((String)fieldValue).trim().length() < 1) {
      return false;
    }
    return true;
  } // end of method isInstanceMemberValueSet
  
  public static Object instantiate(String className) {
    Object instance = null;
    try {
      logger.finer("About to instantiate an object of " + className);

      // Get the appropriate classloader - use the stored one if available
      ClassLoader targetClassLoader = Thread.currentThread().getContextClassLoader();
      //ClassLoader targetClassLoader = classLoader != null ? classLoader : Thread.currentThread().getContextClassLoader();

      // Handle wrapper classes specially
      if (isWrapperClass(className)) {
        instance = createWrapperInstance(className);
      }
      else {
        // Load the class using the target classloader
        Class<?> clazz = Class.forName(className, true, targetClassLoader);

        try {
          // Try default constructor first
          Constructor<?> constructor = clazz.getDeclaredConstructor();
          constructor.setAccessible(true); // Make it accessible even if private
          instance = constructor.newInstance();
        }
        catch (NoSuchMethodException e) {
          // If no default constructor, try alternative constructors
          instance = createInstanceWithAlternativeConstructor(clazz,
              targetClassLoader);
        }
      }

      // Verify that the instance was created and return it
      if (instance != null) {
        logger
            .finer("Successfully instantiated: " + instance.getClass().getName()
                + " with classloader: " + instance.getClass().getClassLoader());
      }

    }
    catch (ClassNotFoundException e) {
      // Try to find the class in the current thread's context classloader
      try {
        ClassLoader contextClassLoader = Thread.currentThread()
            .getContextClassLoader();
        if (contextClassLoader != null && contextClassLoader != classLoader) {
          logger.finer("Attempting to load with context classloader: "
              + contextClassLoader);
          Class<?> clazz = Class.forName(className, true, contextClassLoader);
          instance = clazz.getDeclaredConstructor().newInstance();
        }
      }
      catch (Exception ex) {
        e.printStackTrace();
        ex.printStackTrace();
      }
    }
    catch (IllegalAccessException | InstantiationException
        | InvocationTargetException | NoSuchMethodException e) {
      logger
          .severe("Failed to instantiate " + className + ": " + e.getMessage());
      e.printStackTrace();
    }

    return instance;
  }

  private static Object createInstanceWithAlternativeConstructor(Class<?> clazz,
      ClassLoader classLoader) throws InstantiationException,
      IllegalAccessException, InvocationTargetException, NoSuchMethodException {

    // First, try to find a constructor with the most common patterns
    Exception lastException = null;

    // Try String constructor
    try {
      Constructor<?> stringConstructor = clazz.getConstructor(String.class);
      stringConstructor.setAccessible(true);
      return stringConstructor.newInstance("");
    }
    catch (NoSuchMethodException e) {
      lastException = e;
    }

    // Try primitive int constructor
    try {
      Constructor<?> intConstructor = clazz.getConstructor(int.class);
      intConstructor.setAccessible(true);
      return intConstructor.newInstance(0);
    }
    catch (NoSuchMethodException e) {
      lastException = e;
    }

    // Try primitive long constructor
    try {
      Constructor<?> longConstructor = clazz.getConstructor(long.class);
      longConstructor.setAccessible(true);
      return longConstructor.newInstance(0L);
    }
    catch (NoSuchMethodException e) {
      lastException = e;
    }

    // Try primitive double constructor
    try {
      Constructor<?> doubleConstructor = clazz.getConstructor(double.class);
      doubleConstructor.setAccessible(true);
      return doubleConstructor.newInstance(0.0);
    }
    catch (NoSuchMethodException e) {
      lastException = e;
    }

    // Try primitive boolean constructor
    try {
      Constructor<?> booleanConstructor = clazz.getConstructor(boolean.class);
      booleanConstructor.setAccessible(true);
      return booleanConstructor.newInstance(false);
    }
    catch (NoSuchMethodException e) {
      lastException = e;
    }

    // As a last resort, try to find ANY constructor and provide null/default
    // values
    Constructor<?>[] constructors = clazz.getDeclaredConstructors();
    for (Constructor<?> constructor : constructors) {
      // Skip private constructors if we can't access them
      if (!Modifier.isPublic(constructor.getModifiers())
          && !Modifier.isProtected(constructor.getModifiers())) {
        try {
          constructor.setAccessible(true);
        }
        catch (SecurityException se) {
          continue; // Skip if we can't access it
        }
      }

      Class<?>[] paramTypes = constructor.getParameterTypes();
      Object[] args = createDefaultArguments(paramTypes);

      try {
        return constructor.newInstance(args);
      }
      catch (Exception e) {
        lastException = e;
        // Continue trying other constructors
      }
    }

    // If we get here, no constructor worked
    throw new NoSuchMethodException("No suitable constructor found for class: "
        + clazz.getName() + ". Last error: "
        + (lastException != null ? lastException.getMessage() : "unknown"));
  }

  private static boolean isWrapperClass(String className) {
    // Handle both fully qualified names and simple names
    String simpleName = className.contains(".")
        ? className.substring(className.lastIndexOf('.') + 1)
        : className;

    return simpleName.equals("Integer")
        || simpleName.equals("Long")
        || simpleName.equals("Double")
        || simpleName.equals("Float")
        || simpleName.equals("Boolean")
        || simpleName.equals("Byte")
        || simpleName.equals("Short") 
        || simpleName.equals("Character");
  }

  private static Object createWrapperInstance(String className) {
    // Extract simple name if fully qualified
    String simpleName = className.contains(".")
        ? className.substring(className.lastIndexOf('.') + 1)
        : className;

    switch (simpleName) {
      case "Integer":
        return Integer.valueOf(0);
      case "Long":
        return Long.valueOf(0L);
      case "Double":
        return Double.valueOf(0.0);
      case "Float":
        return Float.valueOf(0.0f);
      case "Boolean":
        return Boolean.valueOf(false);
      case "Byte":
        return Byte.valueOf((byte) 0);
      case "Short":
        return Short.valueOf((short) 0);
      case "Character":
        return Character.valueOf('\u0000');
      default:
        throw new IllegalArgumentException(
            "Unknown wrapper class: " + className);
    }
  }

  private static Object[] createDefaultArguments(Class<?>[] paramTypes) {
    Object[] args = new Object[paramTypes.length];

    for (int i = 0; i < paramTypes.length; i++) {
      Class<?> type = paramTypes[i];

      if (type == boolean.class || type == Boolean.class) {
        args[i] = false;
      }
      else if (type == byte.class || type == Byte.class) {
        args[i] = (byte) 0;
      }
      else if (type == short.class || type == Short.class) {
        args[i] = (short) 0;
      }
      else if (type == int.class || type == Integer.class) {
        args[i] = 0;
      }
      else if (type == long.class || type == Long.class) {
        args[i] = 0L;
      }
      else if (type == float.class || type == Float.class) {
        args[i] = 0.0f;
      }
      else if (type == double.class || type == Double.class) {
        args[i] = 0.0;
      }
      else if (type == char.class || type == Character.class) {
        args[i] = '\u0000';
      }
      else if (type == String.class) {
        args[i] = "";
      }
      else {
        // For complex types, try to create an instance using the same
        // classloader
        try {
          args[i] = instantiate(type.getName());
        }
        catch (Exception e) {
          // If we can't create it, set to null
          args[i] = null;
        }
      }
    }

    return args;
  }

// Add this method to help debug classloader issues
  public static void debugClassloaderInfo(String className, Object instance) {
    if (instance != null) {
      ClassLoader instanceLoader = instance.getClass().getClassLoader();
      ClassLoader contextLoader = Thread.currentThread()
          .getContextClassLoader();
      ClassLoader systemLoader = ClassLoader.getSystemClassLoader();

      logger.info("=== Classloader Debug Info for " + className + " ===");
      logger.info("Instance class: " + instance.getClass().getName());
      logger.info("Instance classloader: " + instanceLoader);
      logger.info("Context classloader: " + contextLoader);
      logger.info("System classloader: " + systemLoader);
      logger.info("Stored classloader: " + classLoader);
      logger.info(
          "Instance and stored are same: " + (instanceLoader == classLoader));
      logger.info("========================================");
    }
  }

public static Object instantiate_old(String className) {
    Object instance = null;
    try {
      logger.finer("About to instantiate an object of " + className);
      if(classLoader == null) {
        instance = Class.forName(className).getDeclaredConstructor().newInstance();
      }
      else {
        instance = classLoader.loadClass(className).getDeclaredConstructor().newInstance();
      }
    }
    catch(ClassNotFoundException e) {
      e.printStackTrace();
    }
    catch(IllegalAccessException e) {
      e.printStackTrace();
    }
    catch(InstantiationException e) {
      e.printStackTrace();
    }
    catch(InvocationTargetException e) {
      e.printStackTrace();
    }
    catch(NoSuchMethodException e) {
      e.printStackTrace();
    }
    return instance;
  }
} // end of class ORMInfoManager
