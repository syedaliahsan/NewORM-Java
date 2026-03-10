# HowTo - NewORM

A guide for setting up and using the NewORM framework.

## Table of Contents

1. [Setup](#setup)
2. [Configuration](#configuration)
3. [Creating Entity Classes](#creating-entity-class)
4. [Using the DAO](#using-the-dao)
5. [Running Tests](#running-tests)

---

## Setup

### 1. Add Required Dependencies

See [ReadMe.md](ReadMe.md) for dependency information.

### 2. Configure Database Connection

Edit `src/main/resources/ORMInfo.xml` with your database credentials:

```xml
<entry key="DB_USER_NAME">your_db_user</entry>
<entry key="DB_USER_PASSWORD">your_db_password</entry>
<entry key="DB_DRIVER_CLASS_NAME">org.postgresql.Driver</entry>
<entry key="DB_URL">jdbc:postgresql://localhost:5432/your_database</entry>
```

For MySQL, change the package and driver:

```xml
<entry key="DATABASE_CLASSES_PACKAGE">com.sa.orm.mysql.v5_0</entry>
<entry key="DB_DRIVER_CLASS_NAME">com.mysql.jdbc.Driver</entry>
<entry key="DB_URL">jdbc:mysql://localhost:3306/your_database</entry>
```

### 3. Apply Database Schema

Run the Liquibase migration file to create the database schema:

```bash
liquibase --changeLogFile=src/main/resources/db/changelog/001-initial-schema.sql update
```

Or execute the SQL file directly against your database.

---

## Configuration

### ORMInfo.xml Location

The ORM configuration file can be located in:
- `src/main/resources/ORMInfo.xml` (default)
- Specified via system property: `-DORM_INFO_FILE_NAME=/path/to/ORMInfo.xml`

### Key Configuration Options

| Property | Description | Default |
|----------|-------------|---------|
| `DATABASE_CLASSES_PACKAGE` | Database implementation package | `com.sa.orm.pgsql.v4` |
| `DB_USER_NAME` | Database username | - |
| `DB_USER_PASSWORD` | Database password | - |
| `DB_URL` | JDBC connection URL | - |
| `DB_MAX_ACTIVE` | Max active connections | 5 |
| `DB_MAX_IDLE` | Max idle connections | 5 |
| `DB_MAX_WAIT` | Max wait time (ms) | 2 |
| `SUPER_LEVEL` | Inheritance levels to consider | 2 |

---

## Creating Entity Classes

### 1. Create POJO with Annotations

Entity classes use annotations to map to database tables:

```java
package com.example;

import com.sa.orm.reflect.annotation.*;

@Entity
public class User {

    @Field(minValue = 1, required = true)
    @PrimaryKey
    private int id;

    @Field(required = true, type = Field.Type.VARIABLE_LENGTH_TEXT, maxLength = 50)
    private String firstName;

    @Field(required = true, type = Field.Type.VARIABLE_LENGTH_TEXT, maxLength = 100)
    private String email;

    @Field(required = true)
    private Integer userTypeId;

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getFirstName() { return firstName; }
    public void setFirstName(String firstName) { this.firstName = firstName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public Integer getUserTypeId() { return userTypeId; }
    public void setUserTypeId(Integer userTypeId) { this.userTypeId = userTypeId; }
}
```

### 2. Annotation Reference

| Annotation | Purpose |
|------------|---------|
| `@Entity` | Marks class as ORM entity. Use `name` for custom table name, `inherits` for parent entity |
| `@Field` | Maps field to database column |
| `@PrimaryKey` | Marks primary key field |
| `@ForeignKey` | Defines foreign key relationship |
| `@Unique` | Adds unique constraint |
| `@ContainedObject` | Defines relationship with another entity |

### 3. Inheritance Support

```java
@Entity(name = "FirmUser", inherits = "User")
public class FirmUser extends User {

    @Field(minValue = 1, required = true)
    @PrimaryKey
    @ForeignKey(referenceEntity = "User", referencedField = "id")
    protected Integer userId;

    @Field(minValue = 1, required = true)
    @ForeignKey(referenceEntity = "Firm", referencedField = "id")
    protected Integer firmId;

    // Getters and Setters
}
```

### 4. One-to-One Relationships

```java
@Entity(name = "UserCredentials")
public class UserCredentials {

    @Field(minValue = 1, required = true)
    @PrimaryKey
    @ForeignKey(referenceEntity = "User", referencedField = "id")
    private Integer userId;

    @Field(required = true, type = Field.Type.VARIABLE_LENGTH_TEXT, maxLength = 20)
    @Unique
    private String username;

    @ContainedObject(
        localInstanceMember = "userId",
        referencedEntity = "User",
        referencedField = "id",
        relationshipWithContainedObject = AnnotationConstants.RELATIONSHIP_PARENT
    )
    private User user;

    // Getters and Setters
}
```

---

## Using the DAO

### 1. Get DAO Instance

```java
import com.sa.orm.ORMInfoManager;
import com.sa.orm.AbstractDAO;

AbstractDAO dao = ORMInfoManager.daoImpl;
```

### 2. Insert Operation

```java
User user = new User();
user.setFirstName("John");
user.setLastName("Doe");
user.setEmail("john.doe@example.com");
user.setUserTypeId(3);

DbResult<User> result = dao.insert(user, null, true);
User insertedUser = result.getEntities().iterator().next();
System.out.println("Inserted ID: " + insertedUser.getId());
```

### 3. Update Operation

```java
User user = dao.getFirstByAttribute(new User(), new NameValueVO("email", "john.doe@example.com"));
user.setFirstName("Jane");
DbResult<User> result = dao.update(user, null, true);
```

### 4. Delete Operation

```java
// Single delete
dao.delete(user, null, true);

// Batch delete
Collection<Integer> ids = Arrays.asList(1, 2, 3);
DbResult<User> result = dao.delete(new User(), ids, null, true);
```

### 5. Search Operations

#### Basic Search

```java
Collection<User> users = dao.search(
    new User(),           // Entity template
    null,                 // Fields (null = all)
    null,                 // Criteria
    BOOLEAN_OPERATOR.AND, // Operator
    "\"User\".\"id\" DESC", // Order by
    -1,                   // Limit (-1 = no limit)
    1,                    // Offset
    null                  // Joins
);
```

#### Search with Criteria

```java
SQLCriterionFactory factory = ORMInfoManager.sqlCriterionFactory;

SQLCriterion[] criteria = new SQLCriterion[2];
criteria[0] = factory.createEqualTo("id", user, 1);
criteria[1] = factory.createEqualTo("firstName", user, "John");

Collection<User> users = dao.search(
    new User(),
    null,
    criteria,
    BOOLEAN_OPERATOR.OR,
    null,
    -1,
    -1,
    null
);
```

#### Search with Paging

```java
PagingVO pagingVO = dao.searchPaging(
    new User(),
    null,
    null,
    BOOLEAN_OPERATOR.AND,
    null,
    5,   // Page size
    10,  // Page number
    null
);
List<User> users = (List<User>) pagingVO.getResults();
int totalRows = pagingVO.getTotalRows();
```

#### Search with Joins

```java
List<Join> joins = new ArrayList<>();
Join join = new Join();
join.setJoinType(Join.JOIN_TYPE_INNER);

FromElement rhs = new FromElement();
rhs.setTableName("UserCredentials");
join.setRightSide(rhs);

SQLCriterion[] onCriteria = new SQLCriterion[1];
onCriteria[0] = factory.createColumnComparison(
    "id", "User",
    SQLCriterion.EQUAL_TO,
    "userId", "UserCredentials"
);
join.setOnCriteria(onCriteria);
joins.add(join);

String[] fields = {"User.firstName", "UserCredentials.username"};
PagingVO result = dao.searchPaging(new User(), fields, null, BOOLEAN_OPERATOR.AND, null, 1, 10, joins, null);
```

#### Generic Search (JSON-based)

```java
String jsonInput = "{\n" +
    "  \"firmId\": 101,\n" +
    "  \"pageSize\": 5,\n" +
    "  \"orderBy\": \"\\\"lastName\\\"\",\n" +
    "  \"sortDirection\": \"asc\",\n" +
    "  \"group\": {\n" +
    "    \"operator\": \"OR\",\n" +
    "    \"criteria\": [\n" +
    "      { \"fieldName\": \"email\", \"operator\": \"<>\", \"value\": \"test@example.com\" }\n" +
    "    ]\n" +
    "  }\n" +
    "}";

SearchRequest request = SearchRequestFactory.createFromJson(jsonInput);
PagingVO result = dao.genericSearch(request, new User());
```

### 6. Fill Contained Objects

```java
User[] users = dao.search(new User(), null, null, BOOLEAN_OPERATOR.AND, null, -1, -1, null)
    .toArray(new User[0]);

// Load related UserCredentials
dao.fillContainedObjects(users, 3, null);

for (User user : users) {
    System.out.println(user.toXML("", true));
}
```

---

## Running Tests

### 1. Configure Test Database

Ensure your `ORMInfo.xml` points to a test database with the schema applied.

### 2. Run DAOTest

```java
package com.sa.orm.demo;

public class DAOTest {
    public static void main(String[] args) throws Exception {
        // Uncomment the test you want to run
        testInsertSimple();
        // testInsertContained();
        // testInsertSuper();
        // testUpdate();
        // search();
        // searchPaging();
        // testDelete();
        // testCascadeDelete();
    }
}
```

### 3. Available Test Methods

| Method | Description |
|--------|-------------|
| `testInsertSimple()` | Basic insert of a User entity |
| `testInsertContained()` | Insert with contained child objects |
| `testInsertSuper()` | Insert inherited entity (FirmUser) |
| `testInsertComprehensive()` | Complex insert with multiple relationships |
| `testUpdate()` | Update existing entity |
| `testDelete()` | Delete entity |
| `testCascadeDelete()` | Cascade delete with related objects |
| `search()` | Basic search with criteria |
| `searchPaging()` | Search with pagination |
| `searchGroupBy()` | Search with GROUP BY |
| `searchGeneric()` | JSON-based generic search |
| `fillObject()` | Fill object from database |
| `fillContainedSimple()` | Load contained objects |
| `populateSingle()` | Populate single contained object |
| `populateMultiple()` | Populate multiple contained objects |
| `testSearchWithJoins()` | Search with JOIN operations |
| `testColumnComparison()` | Column-to-column comparison |

---

## Troubleshooting

### Common Issues

1. **ORMInfo.xml not found**
   - Ensure file is in `src/main/resources/` or specify path via `-DORM_INFO_FILE_NAME`

2. **Database connection failed**
   - Verify JDBC driver is in classpath
   - Check database credentials in ORMInfo.xml
   - Ensure database server is running

3. **Entity not recognized**
   - Verify `@Entity` annotation is present
   - Check package is scanned by ORM

4. **Foreign key constraint violation**
   - Ensure referenced records exist before insert
   - Use cascade operations for related entities

---

## Additional Resources

- [ORMInfo.xml Sample Files](src/main/resources/ORMInfo*.xml)
- [Demo Entity Classes](src/demo/java/com/sa/orm/demo/)
- [Database Schema](src/main/resources/db/changelog/001-initial-schema.sql)
