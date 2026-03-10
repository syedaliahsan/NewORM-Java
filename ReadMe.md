Required Libraries

To use this ORM, your project must include the following dependencies. You can either add them to your pom.xml if you are using Maven, or add the individual JAR files to your project's classpath.
1. Maven Managed Project

Add the following dependencies to the <dependencies> section of your pom.xml file.
XML

<dependencies>
    <dependency>
        <groupId>com.fasterxml.jackson.core</groupId>
        <artifactId>jackson-databind</artifactId>
        <version>2.15.2</version>
    </dependency>

    <dependency>
        <groupId>commons-dbcp</groupId>
        <artifactId>commons-dbcp</artifactId>
        <version>1.4</version>
    </dependency>
    <dependency>
        <groupId>commons-pool</groupId>
        <artifactId>commons-pool</artifactId>
        <version>1.6</version>
    </dependency>

    <dependency>
        <groupId>commons-collections</groupId>
        <artifactId>commons-collections</artifactId>
        <version>2.1</version>
    </dependency>

    <dependency>
        <groupId>org.postgresql</groupId>
        <artifactId>postgresql</artifactId>
        <version>42.7.8</version>
    </dependency>

    <dependency>
        <groupId>mysql</groupId>
        <artifactId>mysql-connector-java</artifactId>
        <version>8.0.33</version> 
    </dependency>
</dependencies>

2. Simple Java Project (Manual JAR Management)

If you are not using a build tool, you must download the following JAR files and add them to your project's Classpath or Build Path.

Core Utility & Pooling:

    jackson-databind-2.15.2.jar (and its dependencies: jackson-core and jackson-annotations)

    commons-collections-2.1.jar

    commons-dbcp.jar (Recommended version: 1.4)

    commons-pool.jar (Recommended version: 1.6)

Database Drivers (Choose at least one):

    PostgreSQL: postgresql-42.7.8.jar

    MySQL: mysql-connector-java.jar (Version 8.0.x recommended)

    Note: The jackson-databind library typically requires jackson-core and jackson-annotations to function. If you are downloading JARs manually, ensure you download those two as well.