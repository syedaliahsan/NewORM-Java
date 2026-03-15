package com.sa.orm.demo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Iterator;
import java.util.List;

import com.sa.orm.AbstractDAO;
import com.sa.orm.DbResult;
import com.sa.orm.NameValueVO;
import com.sa.orm.ORMException;
import com.sa.orm.ORMInfoManager;
import com.sa.orm.SQLCriterion;
import com.sa.orm.SQLCriterionFactory;
import com.sa.orm.SQLFunction;
import com.sa.orm.SQLFunctionFactory;
import com.sa.orm.SQLCriterion.BOOLEAN_OPERATOR;
import com.sa.orm.bean.PagingVO;
import com.sa.orm.bean.SearchRequest;
import com.sa.orm.util.SearchRequestFactory;
import com.sa.orm.util.StringUtils;
import com.sa.orm.Join;
import com.sa.orm.FromElement;

public class DAOTest {

  private static String fieldSeparator = ORMInfoManager.sqlConstantsObj.getFieldPrefix();
  private static String qulifierSeparator = ORMInfoManager.sqlConstantsObj.getQualifierSeparator();
  private static SQLCriterionFactory sqlCriterionFactory = ORMInfoManager.sqlCriterionFactory;
  private static SQLFunctionFactory sqlFunctionFactory = ORMInfoManager.sqlFunctionFactory;
  private static final AbstractDAO daoObj;
  static {
    daoObj = ORMInfoManager.daoImpl;
  }

  public static void testInsertSimple() throws Exception {
    User obj1 = new User();
    obj1.setFirstName("Amir");
    //obj1.setMiddleName("A");
    obj1.setLastName("Syed");
    //obj1.setGender("M");
    //obj1.setDateOfBirth(DateUtils.getStringToTimestamp("1970-04-29", "yyyy-MM-dd"));
    obj1.setEmail("amir@hotmail.com");
    obj1.setUserTypeId(3);
    DbResult<User> insertResult = daoObj.insert(obj1, null, true);
    Collection<User> insertedCollection = insertResult.hasEntities() ? insertResult.getEntities() : new ArrayList<User>();
    insertedCollection.stream()
        .forEach(obj -> System.out.println(obj));
    User insertedUser1 = (User)insertedCollection.toArray()[0];

    DbResult<User> deleteResult = daoObj.delete(new User(), (Collection)Arrays.asList(new Integer[] {insertedUser1.getId()}), null, true);
    Collection<User> deletedCollection = deleteResult.hasEntities() ? deleteResult.getEntities() : new ArrayList<User>();
    deletedCollection.stream()
        .forEach(obj -> System.out.println(obj));
  } // end of method testInsertSimple
  
  public static void testInsertContained() throws Exception {
    User user = new User();
    user.setFirstName("Amir");
    //user.setMiddleName("A");
    user.setLastName("Syed");
    //user.setDateOfBirth(DateUtils.getStringToTimestamp("1970-04-29", "yyyy-MM-dd"));
    user.setEmail("amir@hotmail.com");
    user.setUserTypeId(3);
    UserCredentials userLogin = new UserCredentials();
    //userLogin.setId(user.getId());
    userLogin.setUsername("amirabbas");
    userLogin.setPassword("amirabbas");

    daoObj.insert(user, null, true);
  } // end of method testInsertContained
  
  public static void testInsertSuper() throws Exception {
    FirmUser firmUser = new FirmUser();
    firmUser.setFirstName("Super");
    firmUser.setLastName("User Test");
    firmUser.setEmail("super.user@gmail.com");
    firmUser.setFirmUserTypeId(1);
    firmUser.setUserStatusId(1);
    firmUser.setFirmId(1);
    firmUser.setUserTypeId(1);
    UserCredentials uc = new UserCredentials();
    uc.setUsername("super.user.test");
    uc.setPassword("123456");
    firmUser.setUserLogin(uc);
    daoObj.insert(firmUser, null, true);
  } // end of method testInsertSuper
  
  /**
   * Tests insertion of an object which has a parent object to be inserted as
   * well. It also has contained parent object which in turn has a contained
   * child object and the super object also has a contained child object.
   * 
   * @throws Exception In case of any errors.
   */
  public static void testInsertComprehensive() throws Exception {
    FirmUser firmUser = new FirmUser();
    firmUser.setFirstName("Super");
    firmUser.setLastName("User Test");
    firmUser.setEmail("super.user@gmail.com");
    firmUser.setUserTypeId(3);
    firmUser.setFirmUserTypeId(1);
    firmUser.setUserStatusId(1);
    firmUser.setFirmId(1);
    UserCredentials uc = new UserCredentials();
    uc.setUsername("ali.athar");
    uc.setPassword("123456");
    firmUser.setUserLogin(uc);
    DbResult<User> insertResult = daoObj.insert(firmUser, null, true, false, true);
    Collection<User> insertedCollection = insertResult.hasEntities() ? insertResult.getEntities() : new ArrayList<User>();
    insertedCollection.stream()
        .forEach(obj -> System.out.println(obj));

    FirmUser insertedFirmUser1 = (FirmUser)insertedCollection.toArray()[0];
    DbResult<FirmUser> deleteFirmUserResult = daoObj.delete(new FirmUser(), (Collection)Arrays.asList(new Integer[] {insertedFirmUser1.getUserId()}), null, true);
    Collection<FirmUser> deletedFirmUserCollection = deleteFirmUserResult.getEntities();
    deletedFirmUserCollection.stream().forEach(obj -> System.out.println(obj));

    DbResult<UserCredentials> deleteUCResult = daoObj.delete(new UserCredentials(), (Collection)Arrays.asList(new Integer[] {insertedFirmUser1.getUserId()}), null, true);
    Collection<UserCredentials> deletedUCCollection = deleteUCResult.hasEntities() ? deleteUCResult.getEntities() : new ArrayList<UserCredentials>();
    deletedUCCollection.stream().forEach(obj -> System.out.println(obj));

    DbResult<User> deleteUserResult = daoObj.delete(new User(), (Collection)Arrays.asList(new Integer[] {insertedFirmUser1.getUserId()}), null, true);
    Collection<User> deletedUserCollection = deleteUserResult.getEntities();
    deletedUserCollection.stream().forEach(obj -> System.out.println(obj));
  } // end of method testInsertComprehensive

  /**
   * Tests insertion of an object which has a parent object to be inserted as
   * well. It also has contained parent object which in turn has a contained
   * child object and the super object also has a contained child object.
   * 
   * @throws Exception In case of any errors.
   */
  public static void testUpdate() throws Exception {
    User user = daoObj.getFirstByAttribute(new User(), new NameValueVO("email", "john.doe@example.com"));
    user.setFirstName("John 1");
    user.setUserLogin(null);
    DbResult<User> updateResult = daoObj.update(user, null, true);
    Collection<User> updatedCollection = updateResult.hasEntities() ? updateResult.getEntities() : new ArrayList<User>();
    updatedCollection.stream()
        .forEach(obj -> System.out.println(obj));

  } // end of method testInsertComprehensive

  /**
   * Tests cascade delete of the given objects.
   * 
   * @throws Exception In case of any errors
   */
  public static void testCascadeDelete() throws Exception {
    String[] fields = null;
    //fields = new String[] {StringUtils.wrapDBField("User", "firstName", fieldSeparator, qulifierSeparator)};
    Collection<User> users = daoObj.search(new User(), fields, null, BOOLEAN_OPERATOR.AND, "\"User\".\"id\" DESC", -1, 1, null);
    System.out.println(users.size() + " user objects found.");
    daoObj.fillContainedObjects(users.toArray(new Object[0]), 3, null);
    daoObj.cascadeDelete(users.toArray(new Object[0]), null, true);
  } // end of method testCascadeDelete
  
  /**
   * Tests delete of the given object(s).
   * 
   * @throws Exception In case of any errors
   */
  public static void testDelete() throws Exception {
    Collection<User> users = daoObj.search(new User(), null, null, BOOLEAN_OPERATOR.AND, "\"User\".\"id\" DESC", -1, 1, null);
    System.out.println(users.size() + " user objects found.");
    daoObj.delete(users.toArray(new Object[0]), null, true);
  } // end of method testDelete
  
  public static void fillObject() throws Exception {
    UserCredentials obj = new UserCredentials();
    obj.setUserId(1);
    System.out.println(daoObj.fillObject(obj, null).toString());
  } // end of method fillObject
  
  public static void search() throws Exception {
    User obj1 = new User();
    obj1.setId(1);

    SQLCriterion[] criteria = new SQLCriterion[3];
    criteria[0] = sqlCriterionFactory.createEqualTo("id", obj1, obj1.getId());
    criteria[1] = sqlCriterionFactory.createEqualTo("firstName", obj1, "Ali");
    criteria[2] = sqlCriterionFactory.createEqualTo("userId", new UserCredentials(), obj1.getId());
    
    String[] fields = null;
//    fields = new String[] {
//        StringUtils.wrapDBField("User", "id", fieldSeparator, qulifierSeparator), 
//        StringUtils.wrapDBField("User", "firstName", fieldSeparator, qulifierSeparator), 
//        StringUtils.wrapDBField("User", "lastName", fieldSeparator, qulifierSeparator), 
//        StringUtils.wrapDBField("User", "email", fieldSeparator, qulifierSeparator)
//        };
    fields = new String[] {"User.id", "User.firstName", "User.lastName", "User.email", "UserCredentials.username"};

    List<Join> joins = new ArrayList<Join>();
    joins.add(getSimpleInnerJoin("User", "UserCredentials", "UserCredentials", "id", "userId"));
    Collection<User> records = daoObj.search(obj1, fields, criteria, BOOLEAN_OPERATOR.OR, null, -1, -1, joins, null);
    /*
    for (int i = 0; i < records.size(); i++) {
      System.out.println(((DefaultVO)records.elementAt(i)).toXML("", true));
    } // end of for
    */
    User[] users = (User[])records.toArray(new User[0]);
    for (int i = 0; i < users.length; i++) {
      System.out.println(users[i].toXML("", true));
    } // end of for
  } // end of method search
  
  public static void fillContainedSimple() throws Exception {
    String[] fields = null;
    fields = new String[] {StringUtils.wrapDBField("User", "id", fieldSeparator, qulifierSeparator), StringUtils.wrapDBField("User", "firstName", fieldSeparator, qulifierSeparator), StringUtils.wrapDBField("User", "lastName", fieldSeparator, qulifierSeparator), StringUtils.wrapDBField("User", "email", fieldSeparator, qulifierSeparator)};
    Collection<User> records = daoObj.search(new User(), fields, null, BOOLEAN_OPERATOR.OR, null, -1, -1, null);
    User[] users = (User[])records.toArray(new User[0]);
    daoObj.fillContainedObjects(users, 3, null);
    for (int i = 0; i < users.length; i++) {
      System.out.println(users[i].toXML("", true));
    } // end of for
  } // end of method fillContainedSimple
  
  public static void fillContained() throws Exception {
    User obj1 = new User();
    obj1.setId(1);

    SQLCriterion[] criteria = new SQLCriterion[2];
    criteria[0] = sqlCriterionFactory.createEqualTo(StringUtils.wrapDBField("User", "id", fieldSeparator, qulifierSeparator), null, obj1.getId(), SQLCriterionFactory.INT);
    criteria[1] = sqlCriterionFactory.createEqualTo(StringUtils.wrapDBField("User", "firstName", fieldSeparator, qulifierSeparator), null, "Ali", SQLCriterionFactory.STRING);
    
    String[] fields = null;
    fields = new String[] {StringUtils.wrapDBField("User", "id", fieldSeparator, qulifierSeparator), StringUtils.wrapDBField("User", "firstName", fieldSeparator, qulifierSeparator), StringUtils.wrapDBField("User", "lastName", fieldSeparator, qulifierSeparator), StringUtils.wrapDBField("User", "email", fieldSeparator, qulifierSeparator)};
    Collection<User> records = daoObj.search(obj1, fields, null, BOOLEAN_OPERATOR.OR, null, -1, -1, null);
    User[] users = (User[])records.toArray(new User[0]);
    daoObj.fillContainedObjects(users, 3, null);
    for (int i = 0; i < users.length; i++) {
      System.out.println(users[i].toXML("", true));
    } // end of for
  } // end of method fillContained
  
  public static void searchPaging() throws Exception {
    User obj1 = new User();
    obj1.setId(0);

    SQLCriterion[] criteria = new SQLCriterion[0];
    //criteria[0] = SQLCriterionFactory.createGreaterThan(StringUtils.wrapDBField("User", "id", fieldSeparator, qulifierSeparator), null, obj1.getId(), SQLCriterionFactory.INT, null, null);
    //criteria[1] = SQLCriterionFactory.createEqualTo(StringUtils.wrapDBField("User", "firstName", fieldSeparator, qulifierSeparator), null, "Ali", SQLCriterionFactory.STRING);
    
    String[] fields = null;
    fields = new String[] {
        StringUtils.wrapDBField("User", "id", fieldSeparator, qulifierSeparator),
        StringUtils.wrapDBField("User", "firstName", fieldSeparator, qulifierSeparator),
        StringUtils.wrapDBField("User", "lastName", fieldSeparator, qulifierSeparator),
        StringUtils.wrapDBField("User", "email", fieldSeparator, qulifierSeparator)
    };
    PagingVO pagingVO = daoObj.searchPaging(obj1, fields, criteria, SQLCriterion.BOOLEAN_OPERATOR.AND, null, 5, 10, null);
    User[] users = (User[])pagingVO.getResults().toArray(new User[0]);
    System.out.println("Total records found : " + pagingVO.getTotalRows());
    for (int i = 0; i < users.length; i++) {
      System.out.println(users[i].toXML("", true));
    } // end of for
  } // end of method searchPaging
  
  public static void searchGroupBy() throws Exception {
    User obj1 = new User();
    obj1.setId(1);

    SQLCriterion[] criteria = new SQLCriterion[2];
    criteria[0] = sqlCriterionFactory.createEqualTo(StringUtils.wrapDBField("User", "id", fieldSeparator, qulifierSeparator), null, obj1.getId(), SQLCriterionFactory.INT);
    criteria[1] = sqlCriterionFactory.createEqualTo(StringUtils.wrapDBField("User", "firstName", fieldSeparator, qulifierSeparator), null, "Amir", SQLCriterionFactory.STRING);
    
    String[] fields = null;
    fields = new String[] {StringUtils.wrapDBField("User", "firstName", fieldSeparator, qulifierSeparator)};
    SQLFunction[] functions = {sqlFunctionFactory.createCount(StringUtils.wrapDBField("User", "id", fieldSeparator, qulifierSeparator), "UserCount")};
    Collection vec = daoObj.searchGroupBy(obj1, fields, functions, null, BOOLEAN_OPERATOR.OR, null, 0, 3, null);
    for (Object user : vec) {
      System.out.println(StringUtils.stringArrayToString((String[])user, ", "));
    } // end of for
  } // end of method searchGroupBy
  
  public static void searchGeneric() throws Exception {
    // JSON simulating: (Age > 25) OR ((Status = 'Active') AND (Name starts with 'J'))
    String jsonInput = "{\n" +
            "  \"firmId\": 101,\n" +
            "  \"pageSize\": 5,\n" +
            "  \"orderBy\": \"\\\"lastName\\\"\",\n" +
            "  \"sortDirection\": \"asc\",\n" + 
            "  \"group\": {\n" +
            "    \"operator\": \"OR\",\n" +
            "    \"criteria\": [\n" +
            "      { \"fieldName\": \"email\", \"operator\": \"<>\", \"value\": \"someone@gmail.com\" }\n" +
            "    ],\n" +
            "    \"groups\": [\n" +
            "      {\n" +
            "        \"operator\": \"AND\",\n" +
            "        \"criteria\": [\n" +
            //"          { \"fieldName\": \"status\", \"operator\": \"=\", \"value\": \"Active\" },\n" +
            "          { \"fieldName\": \"firstName\", \"operator\": \"startswith\", \"value\": \"Ali\" },\n" +
            "          { \"fieldName\": \"lastName\", \"operator\": \"=\", \"value\": \"Ahsan\" },\n" +
            "          { \"fieldName\": \"id\", \"operator\": \"=\", \"value\": \"2\" }\n" +
            "        ]\n" +
            "      }\n" +
            "    ]\n" +
            "  }\n" +
            "}";

    // 1. Parse JSON to Java Object
    SearchRequest request = SearchRequestFactory.createFromJson(jsonInput);
    PagingVO pagedResults = daoObj.genericSearch(request, new User());
    User[] users = (User[])pagedResults.getResults().toArray(new User[0]);
    System.out.println("Total records found : " + pagedResults.getTotalRows());
    for (int i = 0; i < users.length; i++) {
      System.out.println(users[i].toXML("", true));
    } // end of for
    if (request != null) {
        System.out.println("Firm ID: " + request.getFirmId());
        System.out.println("Sort Ascending: " + request.isSortOrderAscending()); // Should be false because "desc"
        
        System.out.println("Root Operator: " + request.getGroup().getOperator());
        System.out.println("Root Criteria Count: " + request.getGroup().getCriteria().size()); // 1
        System.out.println("Nested Groups Count: " + request.getGroup().getGroups().size());   // 1
        
        // Access nested data
        String nestedOp = request.getGroup().getGroups().get(0).getOperator();
        System.out.println("Nested Operator: " + nestedOp); // AND
    }
  }

  public static void join() throws Exception {
    FromElement rhs = new FromElement(); rhs.setTableName("User");
    //List<Join> joins = new ArrayList<Join>();
//    SQLCriterion[] joinCriteria = new SQLCriterion[1];
//    joinCriteria[0] = ORMInfoManager.sqlCriterionFactory.createColumnComparison("userId", "FirmUser", 1, "id", "User");
    //joins.add(new Join(null, rhs, Join.JOIN_TYPE_INNER, joinCriteria, BOOLEAN_OPERATOR.AND));
    //joins.add(getSimpleInnerJoin("FirmUser", "User", "User", "userId", "id"));
    String[] fields = new String[] {"User.firstName", "FirmUser.userId", "FirmUser.title"};

    List<SQLCriterion> criteria = new ArrayList<SQLCriterion>();
    criteria.add(ORMInfoManager.sqlCriterionFactory.createEqualTo("FirmUser.userId", 1));
    PagingVO result = daoObj.searchPaging(new FirmUser(), fields, criteria.toArray(new SQLCriterion[0]), BOOLEAN_OPERATOR.AND, "User.firstName", -1, -1, null, null);

    FirmUser[] users = (FirmUser[])result.getResults().toArray(new FirmUser[0]);
    for (int i = 0; i < users.length; i++) {
      System.out.println(users[i].toXML("", true));
    } // end of for
  } // end of method search
  
  
  public static void populateSingle() throws Exception {
    User obj1 = new User();
    obj1.setId(1);
    obj1.setFirstName("Amir");
    obj1.setMiddleName("A");
    obj1.setLastName("Syed");
    //obj1.setDateOfBirth(DateUtils.getStringToTimestamp("1970-04-29", "yyyy-MM-dd"));
    obj1.setEmail("amir@hotmail.com");
    //obj1.setStatus("Active");
    SQLCriterion[] criteria = new SQLCriterion[2];
    criteria[0] = sqlCriterionFactory.createEqualTo(StringUtils.wrapDBField("User", "id", fieldSeparator, qulifierSeparator), null, obj1.getId(), SQLCriterionFactory.INT);
    criteria[1] = sqlCriterionFactory.createEqualTo(StringUtils.wrapDBField("User", "firstName", fieldSeparator, qulifierSeparator), null, "Amir", SQLCriterionFactory.STRING);
    
    String[] fields = null;
    fields = new String[] {StringUtils.wrapDBField("User", "id", fieldSeparator, qulifierSeparator), StringUtils.wrapDBField("User", "firstName", fieldSeparator, qulifierSeparator), StringUtils.wrapDBField("User", "lastName", fieldSeparator, qulifierSeparator), StringUtils.wrapDBField("User", "email", fieldSeparator, qulifierSeparator)};
    Collection<User> records = daoObj.search(obj1, fields, null, BOOLEAN_OPERATOR.OR, null, -1, -1, null);
    User[] recordsArray = (User[])records.toArray(new User[0]);
    daoObj.populateContainedObjects(recordsArray, "userLogin", null);
    for (User user : records) {
      System.out.println(user.toXML("", true));
    } // end of for
  } // end of method populateSingle
  
  public static void populateMultiple() throws Exception {
    User obj1 = new User();
    UserCredentials obj2 = new UserCredentials();
    /*
    obj1.setId(7);
    obj1.setFirstName("Amir");
    //obj1.setMiddleName("A");
    obj1.setLastName("Syed");
    //obj1.setDateOfBirth(DateUtils.getStringToTimestamp("1970-04-29", "yyyy-MM-dd"));
    obj1.setEmail("amir@hotmail.com");
    //obj1.setStatus("Active");
    SQLCriterion[] criteria = new SQLCriterion[2];
    criteria[0] = sqlCriterionFactory.createEqualTo("id", "User", obj1.getId(), SQLCriterionFactory.INT);
    criteria[1] = sqlCriterionFactory.createEqualTo("firstName", "User", "Amir", SQLCriterionFactory.STRING);
    */
    /*
    String[] fields = null;
    fields = new String[] {"User.id", "User.firstName", "User.lastName", "User.email"};
    Collection<User> records = daoObj.search(obj1, fields, null, BOOLEAN_OPERATOR.OR, null, -1, -1, null);
    User[] recordsArray = (User[])records.toArray(new User[0]);
    daoObj.populateContainedObjects(recordsArray, "userLogin", null);
    for (User user: records) {
      System.out.println(user.toXML("", true));
    } // end of for
    */
    SQLCriterion[] criteria = new SQLCriterion[1];
    criteria[0] = sqlCriterionFactory.createEqualTo(StringUtils.wrapDBField("UserCredentials", "username", fieldSeparator, qulifierSeparator), null, "johndoe", SQLCriterionFactory.STRING);
    List<UserCredentials> records = (List<UserCredentials>)daoObj.search(obj2, null, criteria, BOOLEAN_OPERATOR.AND);
    UserCredentials[] recordsArray = (UserCredentials[])records.toArray(new UserCredentials[0]);
    daoObj.populateContainedObjects(records.toArray(new UserCredentials[0]), "user", null);
    for (UserCredentials userCredentials: records) {
      System.out.println(userCredentials.toXML("", true));
    } // end of for
  } // end of method populateMultiple
  
  public static void misc() throws Exception {
    User obj1 = new User();
    obj1.setId(1);
    obj1.setFirstName("Amir");
    obj1.setMiddleName("A");
    obj1.setLastName("Syed");
    //obj1.setDateOfBirth(DateUtils.getStringToTimestamp("1970-04-29", "yyyy-MM-dd"));
    obj1.setEmail("amir@hotmail.com");
    //obj1.setStatus("Active");
    System.out.println(daoObj.createInsertQuery(obj1));
    System.out.println(daoObj.createInsertQueryShort(obj1));
    System.out.println(daoObj.createUpdateQuery(obj1));

    FirmUser obj = new FirmUser();
    obj.setId(1);
    SQLCriterion[] criteria = new SQLCriterion[2];
    criteria[0] = sqlCriterionFactory.createEqualTo(StringUtils.wrapDBField("User", "id", fieldSeparator, qulifierSeparator), null, obj1.getId(), SQLCriterionFactory.INT);
    criteria[1] = sqlCriterionFactory.createEqualTo(StringUtils.wrapDBField("User", "firstName", fieldSeparator, qulifierSeparator), null, "Amir", SQLCriterionFactory.STRING);
    
    System.out.println(daoObj.createUpdateQuery(obj1, new String[] {"status"}, criteria, BOOLEAN_OPERATOR.AND));
    System.out.println(daoObj.createSelectQuery(obj, 2));
    System.out.println(daoObj.createDeleteQuery(obj));
    System.out.println(daoObj.createDeleteQuery(obj, Arrays.asList(new Integer[] {12,23,34,54,10,9})));
    String[] fields = null;
    fields = new String[] {StringUtils.wrapDBField("User", "id", fieldSeparator, qulifierSeparator), StringUtils.wrapDBField("User", "firstName", fieldSeparator, qulifierSeparator), StringUtils.wrapDBField("User", "lastName", fieldSeparator, qulifierSeparator), StringUtils.wrapDBField("User", "email", fieldSeparator, qulifierSeparator)};
    Collection<User> records = daoObj.search(obj1, fields, null, BOOLEAN_OPERATOR.OR, null, -1, -1, null);

    for (User user : records) {
      System.out.println(user.toXML("", true));
    } // end of for

    User[] recordsArray = (User[])records.toArray(new User[0]);
    //daoObj.populateContainedObjects(recordsArray, "userLogin", null);
    //daoObj.fillContainedObjects(recordsArray, 3, null);
    for (User user : records) {
      System.out.println(user.toXML("", true));
    } // end of for
  } // end of method misc
  
  public static void testSearchWithJoins() throws Exception {
    User user = new User();
    
    // Join with UserCredentials
    Join join = new Join();
    join.setJoinType(Join.JOIN_TYPE_INNER);
    
    FromElement rightSide = new FromElement();
    rightSide.setTableName("UserCredentials");
    //rightSide.setAlias("uc");
    join.setRightSide(rightSide);
    
    SQLCriterion[] onCriteria = new SQLCriterion[1];
    // "User"."id" = uc."userId"
    onCriteria[0] = sqlCriterionFactory.createColumnComparison(
        "id", "User",
        SQLCriterion.EQUAL_TO,
        "userId", "UserCredentials");
    
    join.setOnCriteria(onCriteria);
    join.setOnCriteriaOperator(BOOLEAN_OPERATOR.AND);
    
    List<Join> joins = new ArrayList<>();
    joins.add(join);
    
    // Add a filter on the joined table
    // uc."username" = 'amirabbas'
    SQLCriterion[] criteria = new SQLCriterion[1];
    criteria[0] = sqlCriterionFactory.createEqualTo(
        StringUtils.wrapDBField("UserCredentials", "username", fieldSeparator, qulifierSeparator),
        null, 
        "amirabbas", 
        SQLCriterionFactory.STRING);
        
    String[] fields = new String[] {
        StringUtils.wrapDBField("UserCredentials", "username", fieldSeparator, qulifierSeparator),
        StringUtils.wrapDBField("User", "firstName", fieldSeparator, qulifierSeparator),
        StringUtils.wrapDBField("User", "lastName", fieldSeparator, qulifierSeparator)
    };

    System.out.println("Testing Search With Joins...");
    PagingVO result = daoObj.searchPaging(user, fields, null, BOOLEAN_OPERATOR.AND, null, 1, 10, joins, null);
    
    System.out.println("Total Rows: " + result.getTotalRows());
    List<User> users = (List<User>) result.getResults();
    for (User u : users) {
      System.out.println("Found User: " + u.getUsername() + " " + u.getFirstName() + " " + u.getLastName());
    }
  }

  
  public static void testGetMaxUpdatedAt() throws Exception {
    User user = new User();

    String[] fields = new String[] {"User.updatedAt"};
    
    // Optional: Add criteria (example: filter by userTypeId)
    SQLCriterion[] criteria = null;
    // Uncomment below to add a filter:
//     criteria = new SQLCriterion[1];
//     criteria[0] = sqlCriterionFactory.createEqualTo("userTypeId", user, 3);

    // Optional: Add joins (example: join with UserCredentials)
    List<Join> joins = null;
    // Uncomment below to add a join:
    /*
    joins = new ArrayList<Join>();
    Join join = new Join();
    join.setJoinType(Join.JOIN_TYPE_INNER);
    FromElement rightSide = new FromElement();
    rightSide.setTableName("UserCredentials");
    join.setRightSide(rightSide);
    SQLCriterion[] onCriteria = new SQLCriterion[1];
    onCriteria[0] = sqlCriterionFactory.createColumnComparison(
        "id", "User",
        SQLCriterion.EQUAL_TO,
        "userId", "UserCredentials");
    join.setOnCriteria(onCriteria);
    join.setOnCriteriaOperator(BOOLEAN_OPERATOR.AND);
    joins.add(join);
    */

    // Add a filter on the joined table (example: username = 'amirabbas')
    // Uncomment below to add a filter on joined table:
//     criteria = new SQLCriterion[1];
//     criteria[0] = sqlCriterionFactory.createEqualTo("username", "UserCredentials", "amirabbas", SQLCriterionFactory.STRING);

    // Create MAX function for updatedAt field
    SQLFunction[] functions = {
        sqlFunctionFactory.createCount("User.id", "IdCount")
        //, sqlFunctionFactory.createMax("User.updatedAt", "MaxUpdatedAt")
    };

    Collection<Object[]> results = daoObj.searchGroupBy(user, fields, functions, 
        criteria, BOOLEAN_OPERATOR.AND, null, -1, -1, joins, null);

    if (results != null && !results.isEmpty()) {
      Iterator iterator = results.iterator();
      while(iterator.hasNext()) {
        Object[] objects = (Object[]) iterator.next();
        System.out.println(Arrays.toString(objects));
      }
    }
    else {
      System.out.println("No results returned.");
    }
  } // end of method testGetMaxUpdatedAt

  public static void testColumnComparison() {
    try {
      System.out.println("Testing Column Comparison...");
      User user = new User();

      SQLCriterion criterion = sqlCriterionFactory.createColumnComparison(
          "id",
          "User",
          SQLCriterion.EQUAL_TO,
          "userTypeId",
          "User"
      );

      System.out.println("Criterion String: " + criterion.getCriterionString());

      // Execute a search with this criterion
      Collection<User> results = daoObj.search(user, null, new SQLCriterion[] { criterion }, BOOLEAN_OPERATOR.AND, null, -1, 10, null);
      System.out.println("Column Comparison Search executed successfully. Found " + results.size() + " records.");

    } catch (Exception e) {
       System.out.println("Column Comparison Test Failed:");
       e.printStackTrace();
    }
  }

  public static void testExecuteUnionQuery() throws Exception {
    try {
      System.out.println("Testing Execute Union Query...");

      // Create two separate queries to union
      String[] queries = new String[2];

      // Query 1: Get users with id = 1
      User user1 = new User();
      SQLCriterion[] criteria1 = new SQLCriterion[1];
      criteria1[0] = sqlCriterionFactory.createEqualTo("id", user1, 1);
      String[] fields1 = new String[] {"User.id", "User.firstName", "User.lastName", "User.email"};
      queries[0] = daoObj.createSelectQuery(user1, ORMInfoManager.SUPER_LEVEL, fields1, null, criteria1, BOOLEAN_OPERATOR.AND, null, -1, -1, null);

      // Query 2: Get users with id = 2
      User user2 = new User();
      SQLCriterion[] criteria2 = new SQLCriterion[1];
      criteria2[0] = sqlCriterionFactory.createEqualTo("id", user2, 2);
      String[] fields2 = new String[] {"User.id", "User.firstName", "User.lastName", "User.email"};
      queries[1] = daoObj.createSelectQuery(user2, ORMInfoManager.SUPER_LEVEL, fields2, null, criteria2, BOOLEAN_OPERATOR.AND, null, -1, -1, null);

      System.out.println("Query 1: " + queries[0]);
      System.out.println("Query 2: " + queries[1]);

      // Execute the union query
      Collection<User> results = daoObj.executeUnionQuery(new User(), queries, null);

      System.out.println("Union Query executed successfully. Found " + results.size() + " records.");
      for (User user : results) {
        System.out.println("User: " + user.getId() + " - " + user.getFirstName() + " " + user.getLastName() + " (" + user.getEmail() + ")");
      }

    } catch (Exception e) {
      System.out.println("Execute Union Query Test Failed:");
      e.printStackTrace();
      throw e;
    }
  }

  private static Join getSimpleJoin(String lhsTableAlias, String rhsTableName, String rhsTableAlias, String lhsFieldName, String rhsFieldName, int joinType) {
    FromElement rhs = new FromElement();
    rhs.setTableName(rhsTableName);
    rhs.setAlias(rhsTableAlias);
    SQLCriterion[] joinCriteria = new SQLCriterion[1];
    joinCriteria[0] = sqlCriterionFactory.createColumnComparison(lhsFieldName, lhsTableAlias, SQLCriterion.EQUAL_TO, rhsFieldName, rhsTableAlias);
    return new Join(null, rhs, joinType, joinCriteria, BOOLEAN_OPERATOR.AND);
  }
  
  private static Join getSimpleInnerJoin(String lhsTableAlias, String rhsTableName, String rhsTableAlias, String lhsFieldName, String rhsFieldName) {
    return getSimpleJoin(lhsTableAlias, rhsTableName, rhsTableAlias, lhsFieldName, rhsFieldName, Join.JOIN_TYPE_INNER);
  }
  
  private static Join getSimpleLeftJoin(String lhsTableAlias, String rhsTableName, String rhsTableAlias, String lhsFieldName, String rhsFieldName) {
    return getSimpleJoin(lhsTableAlias, rhsTableName, rhsTableAlias, lhsFieldName, rhsFieldName, Join.JOIN_TYPE_LEFT_OUTER);
  }
  
  /**
   * Test dao classes.
   * @param args Command line arguments
   */
  public static void main(String[] args) throws Exception {
    try {
//      testInsertSimple();
//      testInsertSuper();
//      testInsertContained();
//      testInsertComprehensive();
//      testUpdate();
//      
//      search();
//      searchPaging();
//      searchGroupBy();
//      searchGeneric();
//      
//      fillContainedSimple();
//      fillContained();
//      fillObject();
//      
//      populateMultiple();
//      misc();
//      
//      testDelete();
//      
//      testCascadeDelete();
//
//      testSearchWithJoins();
//      testColumnComparison();
      testExecuteUnionQuery();
      // testGetMaxUpdatedAt();
//
      //join();
    }
    catch(Exception e) {
      if(e instanceof ORMException) {
        System.err.println(((ORMException)e).getStandardizedMessage());
      }
      e.printStackTrace();
    }
  } // end of method main
} // end of class DAOTest
