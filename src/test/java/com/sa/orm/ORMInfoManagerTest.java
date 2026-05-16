package com.sa.orm;

import com.sa.orm.demo.FirmUser;
import com.sa.orm.demo.User;

public class ORMInfoManagerTest {
  
  public static void main(String[] args) {
    System.out.println("=== Testing copyFields ===\n");
    
    testInheritance();
    testPartialInheritance();
    testNullValues();
    
    System.out.println("\n=== All tests completed ===");
  }
  
  private static void testInheritance() {
    System.out.println("Test 1: Copy from subclass (FirmUser) to parent class (User)");
    
    User user = new User();
    user.setId(1);
    user.setFirstName("John");
    user.setLastName("Doe");
    user.setEmail("john@test.com");
    user.setUserTypeId(1);
    user.setUsername("johnd");
    user.setActive(true);
    
    FirmUser firmUser = new FirmUser();
    firmUser.setId(2);
    firmUser.setFirstName("Jane");
    firmUser.setLastName("Smith");
    firmUser.setEmail("jane@test.com");
    firmUser.setUserTypeId(2);
    firmUser.setUsername("janes");
    firmUser.setActive(false);
    firmUser.setTitle("Manager");
    firmUser.setSSN("123-45-6789");
    firmUser.setOwner(true);
    firmUser.setLoginCount(5);
    
    System.out.println("Before copy - User: " + user);
    ORMInfoManager.copyFields(user, firmUser);
    System.out.println("After copy - User: " + user);
    
    printResult("id", 1, user.getId());
    printResult("firstName", "Jane", user.getFirstName());
    printResult("lastName", "Smith", user.getLastName());
    printResult("email", "jane@test.com", user.getEmail());
    printResult("userTypeId", 2, user.getUserTypeId());
    printResult("username", "janes", user.getUsername());
    printResult("isActive", false, user.isActive());
    System.out.println();
  }
  
  private static void testPartialInheritance() {
    System.out.println("Test 2: Copy User to FirmUser (more fields in dest)");
    
    FirmUser destFirmUser = new FirmUser();
    destFirmUser.setId(100);
    destFirmUser.setTitle("CEO");
    destFirmUser.setLoginCount(999);
    
    User srcUser = new User();
    srcUser.setId(50);
    srcUser.setFirstName("Alice");
    srcUser.setLastName("Bob");
    srcUser.setEmail("alice@test.com");
    srcUser.setUserTypeId(5);
    srcUser.setUsername("aliceb");
    srcUser.setActive(true);
    
    System.out.println("Before copy - FirmUser: " + destFirmUser);
    ORMInfoManager.copyFields(destFirmUser, srcUser);
    System.out.println("After copy - FirmUser: " + destFirmUser);
    
    printResult("id should be unchanged (100)", 100, destFirmUser.getId());
    printResult("firstName", "Alice", destFirmUser.getFirstName());
    printResult("lastName", "Bob", destFirmUser.getLastName());
    printResult("email", "alice@test.com", destFirmUser.getEmail());
    printResult("userTypeId", 5, destFirmUser.getUserTypeId());
    printResult("username", "aliceb", destFirmUser.getUsername());
    printResult("isActive", true, destFirmUser.isActive());
    printResult("title should be unchanged (CEO)", "CEO", destFirmUser.getTitle());
    printResult("loginCount should be unchanged (999)", 999, destFirmUser.getLoginCount());
    System.out.println();
  }
  
  private static void testNullValues() {
    System.out.println("Test 3: Copy with null values in source");
    
    User user = new User();
    user.setId(1);
    user.setFirstName("Test");
    user.setLastName("User");
    user.setEmail("test@test.com");
    user.setUserTypeId(null);  // null value
    user.setUsername("testuser");
    user.setActive(true);
    
    FirmUser firmUser = new FirmUser();
    firmUser.setId(2);
    firmUser.setFirstName("New");
    firmUser.setLastName("Name");
    firmUser.setEmail("new@test.com");
    firmUser.setUserTypeId(3);
    firmUser.setUsername("newuser");
    firmUser.setActive(false);
    
    System.out.println("Before copy - User id=" + user.getId() + ", userTypeId=" + user.getUserTypeId());
    ORMInfoManager.copyFields(user, firmUser);
    System.out.println("After copy - User id=" + user.getId() + ", userTypeId=" + user.getUserTypeId());
    
    printResult("id should remain 1", 1, user.getId());
    printResult("userTypeId should be 3", 3, user.getUserTypeId());
    printResult("firstName should be New", "New", user.getFirstName());
    System.out.println();
  }
  
  private static void printResult(String fieldName, Object expected, Object actual) {
    boolean pass = (expected == null && actual == null) || (expected != null && expected.equals(actual));
    String status = pass ? "PASS" : "FAIL";
    System.out.println("  " + status + ": " + fieldName + " - expected: " + expected + ", actual: " + actual);
  }
}