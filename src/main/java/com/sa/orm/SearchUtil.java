package com.sa.orm;

import com.sa.orm.bean.FilterCriterion;
import com.sa.orm.bean.FilterGroup;
import com.sa.orm.bean.SearchRequest;
import com.sa.orm.util.DBField;
import com.sa.orm.SQLCriterion.BOOLEAN_OPERATOR;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.logging.Logger;

public class SearchUtil {

  private static final String OPERATOR_OR = "OR";

  /**
   * Converts a generic SearchRequest into an array of SQLCriterion acceptable by AbstractDAO.
   * The result will usually contain a single SQLCriterionGroup if the request has structure,
   * or be empty if no criteria provided.
   */
  public static SQLCriterion[] buildCriteria(SearchRequest request, Object pojo)
          throws IllegalAccessException, InstantiationException,
          InvocationTargetException, NoSuchMethodException {
    
    if (request == null || request.getGroup() == null) {
      return new SQLCriterion[0];
    }

    SQLCriterion rootCriterion = buildGroupCriterion(request.getGroup(), pojo);
    
    if (rootCriterion == null) {
      return new SQLCriterion[0];
    }
    return new SQLCriterion[] { rootCriterion };
  }

  /**
   * Recursive function to build SQLCriterionGroup from FilterGroup.
   */
  private static SQLCriterion buildGroupCriterion(FilterGroup group, Object pojo)
    throws IllegalAccessException, InstantiationException,
    InvocationTargetException, NoSuchMethodException {
    if (group == null) return null;

    // Determine operator for this group
    BOOLEAN_OPERATOR dbOperator = OPERATOR_OR.equalsIgnoreCase(group.getOperator())
        ? BOOLEAN_OPERATOR.OR : BOOLEAN_OPERATOR.AND;

    // Use SQLCriterionGroup to hold nested logic
    SQLCriterionGroup sqlGroup = new SQLCriterionGroup(dbOperator);
    boolean hasContent = false;

    // 1. Process direct criteria in this group
    if (group.getCriteria() != null) {
      for (FilterCriterion fc : group.getCriteria()) {
        SQLCriterion criterion = createSingleCriterion(fc, pojo);
        if (criterion != null) {
          sqlGroup.addCriterion(criterion);
          hasContent = true;
        }
      }
    }

    // 2. Process nested groups (Recursion)
    if (group.getGroups() != null) {
      for (FilterGroup nestedGroup : group.getGroups()) {
        SQLCriterion nestedCriterion = buildGroupCriterion(nestedGroup, pojo);
        if (nestedCriterion != null) {
          sqlGroup.addCriterion(nestedCriterion);
          hasContent = true;
        }
      }
    }

    return hasContent ? sqlGroup : null;
  }

  /**
   * Logger object to log messages.
   */
  private static Logger logger = Logger.getLogger(SearchUtil.class.getName());
  
  /**
   * Factory method to map a single FilterCriterion (JSON) to SQLCriterion (Java).
   */
  private static SQLCriterion createSingleCriterion(FilterCriterion fc, Object pojo)
      throws IllegalAccessException, InstantiationException,
      InvocationTargetException, NoSuchMethodException {

    logger.finer(fc == null ? "FilterCriterion object is null" : "FilterCriterion object is not null");
    if (fc == null || fc.getFieldName() == null || fc.getOperator() == null) return null;

    logger.finer(fc.toString());
    // Resolve DBField to get the correct type and table name
    DBField fieldInfo = ORMInfoManager.getFieldByInstanceMemberName(pojo, fc.getFieldName());
    
    if (fieldInfo == null) {
      logger.finer("No field " + fc.getFieldName() + " found in class " + pojo.getClass().getName());
      // Log warning: Field not found in ORM mapping
      return null;
    }
    SQLCriterionFactory sqlCriterionFactory = ORMInfoManager.sqlCriterionFactory;
    
    String dbFieldName = fieldInfo.getDbFieldName();
    String tableName = fieldInfo.getEntityName();
    // Determine type from ORM info (fallback to String)
    int fieldType = sqlCriterionFactory.getType(fieldInfo.getField().getType().getName());
    //int fieldType = sqlCriterionFactory.getType(ORMInfoManager.instantiate(fieldInfo.getField().getType().getName()));
    
    Object value = convertValue(fc.getValue(), fieldInfo);
    logger.finer("fieldType: " + fieldInfo.getField().getType().getName() + " > " + fieldType + ", value: " + value + ", valueType: " +  value.getClass().getName());

    // Map Node.js operators to SQLCriterionFactory calls
    switch (fc.getOperator().toLowerCase()) {
      case "=":
        logger.finer("About to return SQLCriterion object for field " + fc.getFieldName());
        return sqlCriterionFactory.createEqualTo(dbFieldName, tableName, value, fieldType);
      case "!=":
      case "<>":
       //String fieldName, String tableName, Object value, int fieldType, String dbMask, String javaMask
        return sqlCriterionFactory.createNotEqualTo(dbFieldName, tableName, value, fieldType);
      case ">":
        return sqlCriterionFactory.createGreaterThan(dbFieldName, tableName, value, fieldType);
      case ">=":
        return sqlCriterionFactory.createGreaterThanEqualTo(dbFieldName, tableName, value, fieldType);
      case "<":
        return sqlCriterionFactory.createLessThan(dbFieldName, tableName, value, fieldType);
      case "<=":
        return sqlCriterionFactory.createLessThanEqualTo(dbFieldName, tableName, value, fieldType);
      case "like":
      case "contains":
        return sqlCriterionFactory.createContains(dbFieldName, tableName, value, fieldType);
      case "startswith":
        return sqlCriterionFactory.createStartsWith(dbFieldName, tableName, value, fieldType);
      case "endswith":
        return sqlCriterionFactory.createEndsWith(dbFieldName, tableName, value, fieldType);
      case "null":
        return sqlCriterionFactory.createNull(dbFieldName, tableName, fieldType);
      case "notnull":
        return sqlCriterionFactory.createNotNull(dbFieldName, tableName, fieldType);
      case "in":
        if (value instanceof java.util.Collection) {
          return sqlCriterionFactory.createIn(dbFieldName, tableName, (java.util.Collection) value, fieldType, null, null);
        }
        break;
      case "notin":
        if (value instanceof java.util.Collection) {
          return sqlCriterionFactory.createNotIn(dbFieldName, tableName, (java.util.Collection) value, fieldType, null, null);
        }
        break;
      case "between":
        if (value instanceof List && ((List) value).size() >= 2) {
          List list = (List) value;
          return sqlCriterionFactory.createBetween(dbFieldName, tableName, list.get(0), list.get(1), fieldType, null, null);
        }
        break;
      case "notbetween":
        if (value instanceof List && ((List) value).size() >= 2) {
          List list = (List) value;
          return sqlCriterionFactory.createNotBetween(dbFieldName, tableName, list.get(0), list.get(1), fieldType, null, null);
        }
        break;
      default:
        return null;
    }
    return null;
  }

  /**
   * Helper to cast/convert values based on field type (Date, Integer, etc).
   * Corresponds to `getCriterionValue` in Node.js.
   */
  private static Object convertValue(Object rawValue, DBField fieldInfo) {
    if (rawValue == null) return null;
    
    Class<?> javaType = fieldInfo.getField().getType();

    // TODO Handle Date conversion if the incoming value is String/Long but field is Date
    if (java.util.Date.class.isAssignableFrom(javaType) || java.sql.Timestamp.class.isAssignableFrom(javaType)) {
       // Use DateUtils or simple parsing logic here
       // Example: if rawValue is String iso8601, parse it.
       // implementation depends on how your UI sends dates.
    }
    
    // Handle Lists for IN/BETWEEN operators
    if (rawValue instanceof List) {
      // Deep conversion logic for list elements could go here
      return rawValue;
    }
    String typeName = javaType.getName();
    if (rawValue instanceof String) {
      if("java.lang.Integer".equals(typeName) || "int".equals(typeName)) return Integer.parseInt((String)rawValue);
      if("java.lang.Byte".equals(typeName) || "byte".equals(typeName)) return Byte.parseByte((String)rawValue);
      if("java.lang.Short".equals(typeName) || "short".equals(typeName)) return Short.parseShort((String)rawValue);
      if("java.lang.Long".equals(typeName) || "long".equals(typeName)) return Long.parseLong((String)rawValue);
      if("java.lang.Float".equals(typeName) || "float".equals(typeName)) return Float.parseFloat((String)rawValue);
      if("java.lang.Double".equals(typeName) || "double".equals(typeName)) return Double.parseDouble((String)rawValue);
      // Deep conversion logic for list elements could go here
      return rawValue;
    }

    return rawValue;
  }
}