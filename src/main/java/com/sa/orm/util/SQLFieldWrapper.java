package com.sa.orm.util;

import java.util.*;
import java.util.regex.*;

/**
 * Utility class for wrapping database field names with custom prefixes and suffixes
 * in a SQL-aware manner. Supports complex SQL expressions, functions, aliases,
 * and ordering clauses.
 */
public class SQLFieldWrapper {

  private static final Pattern ALIAS_PATTERN = 
      Pattern.compile("^(.*?)\\s+AS\\s+([a-zA-Z_][a-zA-Z0-9_]*)$", Pattern.CASE_INSENSITIVE);
  
  private static final Pattern ORDER_BY_PATTERN = 
      Pattern.compile("^(.*?)\\s+(ASC|DESC)$", Pattern.CASE_INSENSITIVE);
  
  private static final Set<String> SQL_FUNCTIONS = new HashSet<>(Arrays.asList(
      "COUNT", "SUM", "AVG", "MIN", "MAX", "CONCAT", "COALESCE", "NULLIF", 
      "ABS", "ROUND", "CEIL", "FLOOR", "UPPER", "LOWER", "LENGTH", "SUBSTRING",
      "SUBSTR", "TRIM", "DATE", "TIME", "TIMESTAMP", "YEAR", "MONTH", "DAY", 
      "NOW", "CURRENT_DATE", "CURRENT_TIME", "CURRENT_TIMESTAMP", "CAST",
      "EXTRACT", "TO_CHAR", "TO_DATE", "NVL", "IFNULL", "DECODE", "CASE"
  ));
  
  private static final Set<String> SQL_KEYWORDS = new HashSet<>(Arrays.asList(
      "SELECT", "FROM", "WHERE", "GROUP", "ORDER", "BY", "HAVING", "JOIN", 
      "INNER", "LEFT", "RIGHT", "OUTER", "ON", "AS", "AND", "OR", "NOT", 
      "IN", "BETWEEN", "LIKE", "IS", "NULL", "ASC", "DESC", "DISTINCT",
      "TOP", "LIMIT", "OFFSET", "FETCH", "ONLY", "UNION", "ALL", "INTERSECT",
      "EXCEPT", "EXISTS", "SOME", "ANY", "ALL"
  ));

  /**
   * Parses a comma-separated SQL field list into individual field expressions.
   * Handles nested functions, parentheses, quoted strings, and complex expressions.
   * 
   * @param fieldList Comma-separated list of SQL field expressions
   * @return Array of individual field expressions
   */
  public static String[] parseFieldList(String fieldList) {
      if (fieldList == null || fieldList.trim().isEmpty()) {
          return new String[0];
      }
      
      List<String> result = new ArrayList<>();
      StringBuilder currentField = new StringBuilder();
      boolean inSingleQuotes = false;
      boolean inDoubleQuotes = false;
      int parenDepth = 0;
      int bracketDepth = 0; // For square brackets if used as identifiers
      
      String trimmed = fieldList.trim();
      
      for (int i = 0; i < trimmed.length(); i++) {
          char c = trimmed.charAt(i);
          
          // Handle quotes
          if (c == '\'' && !inDoubleQuotes) {
              inSingleQuotes = !inSingleQuotes;
              currentField.append(c);
          }
          else if (c == '"' && !inSingleQuotes) {
              inDoubleQuotes = !inDoubleQuotes;
              currentField.append(c);
          }
          // Handle brackets (often used for identifiers in SQL Server)
          else if (c == '[' && !inSingleQuotes && !inDoubleQuotes) {
              bracketDepth++;
              currentField.append(c);
          }
          else if (c == ']' && !inSingleQuotes && !inDoubleQuotes) {
              bracketDepth--;
              currentField.append(c);
          }
          // Handle parentheses
          else if (c == '(' && !inSingleQuotes && !inDoubleQuotes && bracketDepth == 0) {
              parenDepth++;
              currentField.append(c);
          }
          else if (c == ')' && !inSingleQuotes && !inDoubleQuotes && bracketDepth == 0) {
              parenDepth--;
              currentField.append(c);
          }
          // Handle comma separator
          else if (c == ',' && !inSingleQuotes && !inDoubleQuotes && parenDepth == 0 && bracketDepth == 0) {
              String field = currentField.toString().trim();
              if (!field.isEmpty()) {
                  result.add(field);
              }
              currentField = new StringBuilder();
          }
          else {
              currentField.append(c);
          }
      }
      
      // Add the last field
      if (currentField.length() > 0) {
          String field = currentField.toString().trim();
          if (!field.isEmpty()) {
              result.add(field);
          }
      }
      
      return result.toArray(new String[0]);
  }

  /**
   * Parses an ORDER BY clause into individual field expressions with their sort directions.
   * 
   * @param orderByClause The ORDER BY clause content (without the "ORDER BY" keywords)
   * @return Array of ORDER BY field expressions with sort directions preserved
   */
  public static String[] parseOrderByList(String orderByClause) {
      if (orderByClause == null || orderByClause.trim().isEmpty()) {
          return new String[0];
      }
      
      String[] fields = parseFieldList(orderByClause);
      List<String> result = new ArrayList<>();
      
      for (String field : fields) {
          // Check if the field already has ASC/DESC
          String upperField = field.toUpperCase();
          if (upperField.endsWith(" ASC")) {
              result.add(field.substring(0, field.length() - 4).trim() + " ASC");
          } else if (upperField.endsWith(" DESC")) {
              result.add(field.substring(0, field.length() - 5).trim() + " DESC");
          } else {
              // No direction specified, default to ASC (but don't add it explicitly)
              result.add(field);
          }
      }
      
      return result.toArray(new String[0]);
  }

  /**
   * Parses a SELECT list into individual field expressions.
   * This is an alias for parseFieldList for better readability.
   * 
   * @param selectList The SELECT list content
   * @return Array of individual field expressions
   */
  public static String[] parseSelectList(String selectList) {
      return parseFieldList(selectList);
  }

  /**
   * Wraps an array of SQL field expressions with the specified prefix and suffix.
   * Handles complex SQL constructs including:
   * - Simple and qualified field names
   * - Functions with multiple parameters
   * - Expressions with operators
   * - Aliases (with or without AS keyword)
   * - ORDER BY clauses with ASC/DESC
   * - String literals and numbers
   * 
   * @param fields Array of SQL field expressions to wrap
   * @param prefix Prefix to add to identifiers (e.g., "`", "[", "\"")
   * @param suffix Suffix to add to identifiers (e.g., "`", "]", "\"")
   * @return Array of wrapped SQL expressions
   */
  public static String[] wrapFields(String[] fields, String prefix, String suffix) {
      if (fields == null || fields.length == 0) {
          return new String[0];
      }
      
      // Normalize prefix and suffix
      final String p = prefix == null ? "" : prefix;
      final String s = suffix == null ? "" : suffix;
      
      String[] result = new String[fields.length];
      
      for (int i = 0; i < fields.length; i++) {
          String field = fields[i];
          if (field == null) {
              result[i] = null;
              continue;
          }
          
          field = field.trim();
          if (field.isEmpty()) {
              result[i] = null;
              continue;
          }
          
          result[i] = wrapFieldExpression(field, p, s, false);
      }
      
      return result;
  }
  
  /**
   * Wraps a comma-separated string of SQL field expressions.
   * 
   * @param fields Comma-separated field expressions
   * @param prefix Prefix to add to identifiers
   * @param suffix Suffix to add to identifiers
   * @return String of wrapped expressions joined by commas
   */
  public static String wrapFields(String fields, String prefix, String suffix) {
      if (fields == null || fields.trim().isEmpty()) {
          return fields;
      }
      
      String[] fieldArray = parseFieldList(fields);
      String[] wrappedArray = wrapFields(fieldArray, prefix, suffix);
      return String.join(", ", wrappedArray);
  }
  
  /**
   * Wraps a single SQL field expression, handling all supported syntax.
   */
  private static String wrapFieldExpression(String expr, String prefix, String suffix, boolean inFunctionParam) {
      if (prefix.isEmpty() && suffix.isEmpty()) {
          return expr;
      }
      
      // First, check if the entire expression is just a simple identifier
      if (isSimpleIdentifier(expr) && !isSQLFunction(expr) && !isSQLKeyword(expr)) {
          return wrapIdentifier(expr, prefix, suffix);
      }
      
      // Check for ORDER BY clause first
      Matcher orderByMatcher = ORDER_BY_PATTERN.matcher(expr);
      if (orderByMatcher.matches()) {
          String fieldPart = orderByMatcher.group(1).trim();
          String direction = orderByMatcher.group(2);
          return wrapFieldExpression(fieldPart, prefix, suffix, false) + " " + direction;
      }
      
      // Check for explicit AS alias
      Matcher aliasMatcher = ALIAS_PATTERN.matcher(expr);
      if (aliasMatcher.matches()) {
          String fieldPart = aliasMatcher.group(1).trim();
          String alias = aliasMatcher.group(2);
          return wrapFieldExpression(fieldPart, prefix, suffix, false) + " AS " + wrapIdentifier(alias, prefix, suffix);
      }
      
      // Check for simple alias (without AS)
      // But be careful not to misinterpret function calls or expressions
      if (!hasUnmatchedParens(expr)) {
          String[] words = expr.split("\\s+");
          if (words.length == 2 && isSimpleIdentifier(words[1]) && !isSQLKeyword(words[1].toUpperCase())) {
              // Check if first part is a function call or complex expression
              String firstPart = words[0];
              String alias = words[1];
              
              // If first part ends with ')', it's likely a function call without AS
              if (firstPart.endsWith(")")) {
                  return wrapFieldExpression(firstPart, prefix, suffix, false) + " " + wrapIdentifier(alias, prefix, suffix);
              }
              // If first part contains operators or parentheses, it's a complex expression
              else if (firstPart.matches(".*[+\\-*/%><=!&|^~()].*")) {
                  return wrapFieldExpression(firstPart, prefix, suffix, false) + " " + wrapIdentifier(alias, prefix, suffix);
              }
          }
      }
      
      // Handle complex expressions
      return parseComplexExpression(expr, prefix, suffix);
  }
  
  /**
   * Parses a complex SQL expression containing functions, operators, etc.
   */
  private static String parseComplexExpression(String expr, String prefix, String suffix) {
      StringBuilder result = new StringBuilder();
      int i = 0;
      int length = expr.length();
      
      while (i < length) {
          char c = expr.charAt(i);
          
          // Skip whitespace
          if (Character.isWhitespace(c)) {
              result.append(c);
              i++;
              continue;
          }
          
          // Handle string literals
          if (c == '\'' || c == '"') {
              StringBuilder literal = new StringBuilder();
              literal.append(c);
              char quoteChar = c;
              i++;
              
              while (i < length && expr.charAt(i) != quoteChar) {
                  // Handle escaped quotes
                  if (expr.charAt(i) == '\\' && i + 1 < length && expr.charAt(i + 1) == quoteChar) {
                      literal.append(expr.charAt(i));
                      i++;
                      literal.append(expr.charAt(i));
                      i++;
                  } else {
                      literal.append(expr.charAt(i));
                      i++;
                  }
              }
              
              if (i < length) {
                  literal.append(expr.charAt(i));
                  i++;
              }
              
              result.append(literal.toString());
              continue;
          }
          
          // Handle numbers
          if (Character.isDigit(c) || (c == '-' && i + 1 < length && Character.isDigit(expr.charAt(i + 1)))) {
              StringBuilder number = new StringBuilder();
              boolean hasDecimal = false;
              
              while (i < length) {
                  char current = expr.charAt(i);
                  if (Character.isDigit(current)) {
                      number.append(current);
                  } else if (current == '.' && !hasDecimal) {
                      hasDecimal = true;
                      number.append(current);
                  } else {
                      break;
                  }
                  i++;
              }
              
              result.append(number.toString());
              continue;
          }
          
          // Check for function calls (identifier followed by '(')
          if (Character.isLetter(c)) {
              // Check if this is a function name
              StringBuilder possibleFunction = new StringBuilder();
              int startPos = i;
              
              while (i < length && (Character.isLetterOrDigit(expr.charAt(i)) || expr.charAt(i) == '_')) {
                  possibleFunction.append(expr.charAt(i));
                  i++;
              }
              
              // Skip whitespace to check for '('
              int tempPos = i;
              while (tempPos < length && Character.isWhitespace(expr.charAt(tempPos))) {
                  tempPos++;
              }
              
              if (tempPos < length && expr.charAt(tempPos) == '(') {
                  // This is a function name - don't wrap it
                  String functionName = possibleFunction.toString();
                  result.append(functionName);
                  
                  // Skip whitespace
                  while (i < length && Character.isWhitespace(expr.charAt(i))) {
                      result.append(expr.charAt(i));
                      i++;
                  }
                  
                  // Process the function arguments
                  if (i < length && expr.charAt(i) == '(') {
                      result.append('(');
                      i++;
                      
                      // Parse function arguments
                      StringBuilder args = new StringBuilder();
                      int parenDepth = 1;
                      
                      while (i < length && parenDepth > 0) {
                          char currentChar = expr.charAt(i);
                          
                          if (currentChar == '(') {
                              parenDepth++;
                              args.append(currentChar);
                          } else if (currentChar == ')') {
                              parenDepth--;
                              if (parenDepth > 0) {
                                  args.append(currentChar);
                              }
                          } else {
                              args.append(currentChar);
                          }
                          i++;
                      }
                      
                      // Process the arguments
                      String processedArgs = processFunctionArguments(args.toString(), prefix, suffix);
                      result.append(processedArgs).append(')');
                  }
              } else {
                  // Not a function, could be an identifier or operator
                  i = startPos; // Reset to start position
                  String token = parseToken(expr, i, prefix, suffix);
                  result.append(token);
                  i += countTokenLength(expr, i);
              }
              continue;
          }
          
          // Handle operators and other characters
          if (isOperator(c) || c == ',' || c == '.' || c == '(' || c == ')') {
              result.append(c);
              i++;
              continue;
          }
          
          // Handle any other character
          result.append(c);
          i++;
      }
      
      return result.toString();
  }
  
  /**
   * Counts the length of a token starting at the given position.
   */
  private static int countTokenLength(String expr, int startPos) {
      int i = startPos;
      int length = expr.length();
      
      while (i < length) {
          char c = expr.charAt(i);
          if (Character.isLetterOrDigit(c) || c == '_' || c == '.') {
              i++;
          } else {
              break;
          }
      }
      
      return i - startPos;
  }
  
  /**
   * Parses a token that might be an identifier or qualified identifier.
   */
  private static String parseToken(String expr, int startPos, String prefix, String suffix) {
      StringBuilder token = new StringBuilder();
      int i = startPos;
      int length = expr.length();
      
      while (i < length) {
          char c = expr.charAt(i);
          if (Character.isLetterOrDigit(c) || c == '_' || c == '.') {
              token.append(c);
              i++;
          } else {
              break;
          }
      }
      
      String tokenStr = token.toString();
      return wrapToken(tokenStr, prefix, suffix);
  }
  
  /**
   * Wraps a token if it's an identifier.
   */
  private static String wrapToken(String token, String prefix, String suffix) {
      if (token == null || token.isEmpty()) {
          return token;
      }
      
      // Check if it's a qualified identifier
      if (token.contains(".")) {
          String[] parts = token.split("\\.");
          StringBuilder wrapped = new StringBuilder();
          for (int j = 0; j < parts.length; j++) {
              if (j > 0) wrapped.append('.');
              String part = parts[j];
              if (isSimpleIdentifier(part) && !isSQLFunction(part) && !isSQLKeyword(part)) {
                  wrapped.append(prefix).append(part).append(suffix);
              } else {
                  wrapped.append(part);
              }
          }
          return wrapped.toString();
      }
      
      // Simple identifier
      if (isSimpleIdentifier(token) && !isSQLFunction(token) && !isSQLKeyword(token)) {
          return prefix + token + suffix;
      }
      
      return token;
  }
  
  /**
   * Processes function arguments, wrapping identifiers as needed.
   */
  private static String processFunctionArguments(String args, String prefix, String suffix) {
      if (args.trim().isEmpty()) {
          return "";
      }
      
      StringBuilder result = new StringBuilder();
      int i = 0;
      int length = args.length();
      int parenDepth = 0;
      boolean inSingleQuotes = false;
      boolean inDoubleQuotes = false;
      StringBuilder currentArg = new StringBuilder();
      
      while (i < length) {
          char c = args.charAt(i);
          
          // Handle quotes
          if (c == '\'' && !inDoubleQuotes) {
              inSingleQuotes = !inSingleQuotes;
              currentArg.append(c);
          } else if (c == '"' && !inSingleQuotes) {
              inDoubleQuotes = !inDoubleQuotes;
              currentArg.append(c);
          } else if (!inSingleQuotes && !inDoubleQuotes) {
              if (c == '(') {
                  parenDepth++;
                  currentArg.append(c);
              } else if (c == ')') {
                  parenDepth--;
                  currentArg.append(c);
              } else if (c == ',' && parenDepth == 0) {
                  // End of current argument
                  if (currentArg.length() > 0) {
                      if (result.length() > 0) result.append(", ");
                      result.append(wrapFieldExpression(currentArg.toString().trim(), prefix, suffix, true));
                      currentArg = new StringBuilder();
                  }
              } else {
                  currentArg.append(c);
              }
          } else {
              currentArg.append(c);
          }
          
          i++;
      }
      
      // Add the last argument
      if (currentArg.length() > 0) {
          if (result.length() > 0) result.append(", ");
          result.append(wrapFieldExpression(currentArg.toString().trim(), prefix, suffix, true));
      }
      
      return result.toString();
  }
  
  /**
   * Checks if a string is a simple identifier.
   */
  private static boolean isSimpleIdentifier(String token) {
      return token != null && token.matches("^[a-zA-Z_][a-zA-Z0-9_]*$");
  }
  
  /**
   * Checks if a string is an SQL function name.
   */
  private static boolean isSQLFunction(String token) {
      return token != null && SQL_FUNCTIONS.contains(token.toUpperCase());
  }
  
  /**
   * Checks if a string has unmatched parentheses.
   */
  private static boolean hasUnmatchedParens(String expr) {
      int count = 0;
      for (char c : expr.toCharArray()) {
          if (c == '(') count++;
          if (c == ')') count--;
      }
      return count != 0;
  }
  
  /**
   * Checks if a character is an SQL operator.
   */
  private static boolean isOperator(char c) {
      return "+-*/%=<>!&|^~".indexOf(c) >= 0;
  }
  
  /**
   * Checks if a string is an SQL keyword.
   */
  private static boolean isSQLKeyword(String token) {
      return token != null && SQL_KEYWORDS.contains(token.toUpperCase());
  }
  
  /**
   * Wraps an identifier (column or table name).
   */
  private static String wrapIdentifier(String identifier, String prefix, String suffix) {
      if (identifier == null || identifier.isEmpty()) {
          return "";
      }
      
      // Handle qualified identifiers (table.column)
      if (identifier.contains(".")) {
          String[] parts = identifier.split("\\.");
          StringBuilder wrapped = new StringBuilder();
          for (int i = 0; i < parts.length; i++) {
              if (i > 0) wrapped.append('.');
              String part = parts[i].trim();
              if (isSimpleIdentifier(part) && !isSQLFunction(part) && !isSQLKeyword(part)) {
                  wrapped.append(prefix).append(part).append(suffix);
              } else {
                  wrapped.append(part);
              }
          }
          return wrapped.toString();
      }
      
      // Simple identifier
      if (isSimpleIdentifier(identifier) && !isSQLFunction(identifier) && !isSQLKeyword(identifier)) {
          return prefix + identifier + suffix;
      }
      
      return identifier;
  }
  
  /**
   * Removes empty strings from an array.
   */
  public static String[] removeEmpty(String[] input) {
      return Arrays.stream(input)
          .filter(s -> s != null && !s.trim().isEmpty())
          .toArray(String[]::new);
  }
  
  /**
   * Wraps a single field with optional table qualification.
   * 
   * @param tableName Table name or alias (can be null)
   * @param fieldName Field name or expression
   * @param wrapperPrefix Prefix wrapper
   * @param wrapperSuffix Suffix wrapper
   * @param qualifierSeparator Separator between table and field (usually ".")
   * @return Wrapped field expression
   */
  public static String wrapDBField(String tableName, String fieldName,
          String wrapperPrefix, String wrapperSuffix, String qualifierSeparator) {
      if (fieldName == null || fieldName.trim().isEmpty()) {
          return "";
      }
      
      String wrappedField = wrapFieldExpression(fieldName.trim(), wrapperPrefix, wrapperSuffix, false);
      
      if (tableName != null && !tableName.trim().isEmpty()) {
          String wrappedTable = wrapIdentifier(tableName.trim(), wrapperPrefix, wrapperSuffix);
          return wrappedTable + qualifierSeparator + wrappedField;
      }
      
      return wrappedField;
  }
  
  /**
   * Wraps a single field with optional table qualification using same wrapper for prefix and suffix.
   */
  public static String wrapDBField(String tableName, String fieldName, String wrapper, String qualifierSeparator) {
      return wrapDBField(tableName, fieldName, wrapper, wrapper, qualifierSeparator);
  }
  
  /**
   * Wraps a single field with the specified wrapper (no table qualification).
   */
  public static String wrapDBField(String fieldName, String wrapper) {
      return wrapDBField(null, fieldName, wrapper, wrapper, "");
  }

  
  /**
   * Test method to verify all functionality including the new parse methods.
   */
  public static void main(String[] args) {
      System.out.println("=== Testing SQL Field Wrapper ===\n");
      
      String prefix = "`";
      String suffix = "`";
      
      // Test parseFieldList
      System.out.println("=== Testing parseFieldList ===");
      String[] testLists = {
          "abc, def, xyz",
          "user.abc, userType.def, xyz",
          "concat(firstName, ' ', lastName), age, count(*)",
          "price * quantity, (discount + tax) as total, name asc",
          "user.firstName, user.lastName, count(*) as total",
          "concat(firstName, ' ', lastName) as fullName, age desc",
          "CASE WHEN status = 'ACTIVE' THEN 1 ELSE 0 END as active_flag, created_date"
      };
      
      for (String list : testLists) {
          System.out.println("\nInput: " + list);
          String[] parsed = parseFieldList(list);
          System.out.println("Parsed " + parsed.length + " fields:");
          for (int i = 0; i < parsed.length; i++) {
              System.out.println("  [" + i + "] " + parsed[i]);
          }
      }
      
      // Test parseOrderByList
      System.out.println("\n=== Testing parseOrderByList ===");
      String[] orderByLists = {
          "name asc, age desc, created_date",
          "user.last_name desc, department.name asc",
          "price * quantity desc, category asc",
          "coalesce(sort_order, 999) asc, name"
      };
      
      for (String list : orderByLists) {
          System.out.println("\nInput ORDER BY: " + list);
          String[] parsed = parseOrderByList(list);
          System.out.println("Parsed " + parsed.length + " fields:");
          for (int i = 0; i < parsed.length; i++) {
              System.out.println("  [" + i + "] " + parsed[i]);
          }
      }
      
      // Test parseSelectList
      System.out.println("\n=== Testing parseSelectList ===");
      String[] selectLists = {
          "id, name, email",
          "u.id, u.name, d.name as dept_name",
          "COUNT(*) as total, AVG(salary) as avg_salary, MAX(hire_date) as latest_hire",
          "CONCAT(first_name, ' ', last_name) as full_name, age, salary * 12 as annual_salary"
      };
      
      for (String list : selectLists) {
          System.out.println("\nInput SELECT: " + list);
          String[] parsed = parseSelectList(list);
          System.out.println("Parsed " + parsed.length + " fields:");
          for (int i = 0; i < parsed.length; i++) {
              System.out.println("  [" + i + "] " + parsed[i]);
          }
      }
      
      // Test wrapFields with parsed lists
      System.out.println("\n=== Testing wrapFields with parsed lists ===");
      String complexList = "abc, user.def, concat(firstName, ' ', lastName) as fullName, age desc, price * quantity";
      System.out.println("Original: " + complexList);
      
      String[] parsedFields = parseFieldList(complexList);
      System.out.println("Parsed fields:");
      for (String field : parsedFields) {
          System.out.println("  " + field);
      }
      
      String[] wrappedFields = wrapFields(parsedFields, prefix, suffix);
      System.out.println("\nWrapped fields:");
      for (String field : wrappedFields) {
          System.out.println("  " + field);
      }
      
      String wrappedList = wrapFields(complexList, prefix, suffix);
      System.out.println("\nWrapped list: " + wrappedList);
      
      // Original test cases
      System.out.println("\n=== Original Test Cases ===");
      String[][] testCases = {
          // Simple field names
          {"abc", "`abc`"},
          {"def", "`def`"},
          {"xyz", "`xyz`"},
          
          // Qualified field names
          {"user.abc", "`user`.`abc`"},
          {"userType.def", "`userType`.`def`"},
          
          // Mixed simple and qualified
          {"abc, user.def, xyz", "`abc`, `user`.`def`, `xyz`"},
          
          // Functions
          {"concat(firstName, ' ', lastName)", "concat(`firstName`, ' ', `lastName`)"},
          {"count(*)", "count(*)"},
          {"sum(amount)", "sum(`amount`)"},
          {"avg(price) as average", "avg(`price`) AS `average`"},
          
          // Complex functions
          {"concat(user.firstName, ' ', user.lastName)", "concat(`user`.`firstName`, ' ', `user`.`lastName`)"},
          {"coalesce(null, field1, field2)", "coalesce(null, `field1`, `field2`)"},
          
          // Expressions with operators
          {"price * quantity", "`price` * `quantity`"},
          {"user.firstName + ' ' + lastName", "`user`.`firstName` + ' ' + `lastName`"},
          {"(price - discount) * tax", "(`price` - `discount`) * `tax`"},
          
          // Aliases
          {"abc def", "`abc` `def`"},
          {"user.xyz as xyz1", "`user`.`xyz` AS `xyz1`"},
          {"count(*) total_count", "count(*) `total_count`"},
          
          // ORDER BY clauses
          {"name asc", "`name` asc"},
          {"user.age desc", "`user`.`age` desc"},
          {"price * quantity asc", "`price` * `quantity` asc"},
          
          // Mixed complex cases
          {"user.firstName, user.lastName, count(*) as total", 
           "`user`.`firstName`, `user`.`lastName`, count(*) AS `total`"},
          
          {"concat(firstName, ' ', lastName) AS fullName, age desc", 
           "concat(`firstName`, ' ', `lastName`) AS `fullName`, `age` desc"},
          
          // Edge cases
          {"", null},
          {null, null},
          {"   ", null},
          {"123", "123"}, // Numbers shouldn't be wrapped
          {"'literal'", "'literal'"}, // String literals shouldn't be wrapped
      };
      
      // Run tests
      int passed = 0;
      int total = 0;
      
      for (String[] testCase : testCases) {
          String input = testCase[0];
          String expected = testCase[1];
          
          String[] inputArray = input == null ? null : new String[]{input};
          String[] resultArray = wrapFields(inputArray, prefix, suffix);
          String result = resultArray != null && resultArray.length > 0 ? resultArray[0] : null;
          
          // Handle empty string case
          if (input != null && input.trim().isEmpty()) {
              result = null;
          }
          
          boolean success = (expected == null && result == null) || 
                           (expected != null && expected.equals(result));
          
          System.out.printf("Input:    %s%n", input);
          System.out.printf("Expected: %s%n", expected);
          System.out.printf("Result:   %s%n", result);
          System.out.printf("Status:   %s%n%n", success ? "✓ PASS" : "✗ FAIL");
          
          total++;
          if (success) passed++;
      }
      
      System.out.printf("=== Test Summary: %d/%d passed ===%n", passed, total);
      
      // Test the comma-separated string version
      System.out.println("\n=== Testing comma-separated string version ===");
      String testStr = "abc, user.def, concat(firstName, ' ', lastName) as fullName, age desc";
      String expected = "`abc`, `user`.`def`, concat(`firstName`, ' ', `lastName`) AS `fullName`, `age` desc";
      String result = wrapFields(testStr, prefix, suffix);
      System.out.printf("Input:    %s%n", testStr);
      System.out.printf("Expected: %s%n", expected);
      System.out.printf("Result:   %s%n", result);
      System.out.printf("Status:   %s%n", expected.equals(result) ? "✓ PASS" : "✗ FAIL");
      
      // Test wrapDBField methods
      System.out.println("\n=== Testing wrapDBField methods ===");
      System.out.println("wrapDBField('user', 'name', '`', '`', '.'): " + 
          wrapDBField("user", "name", "`", "`", "."));
      System.out.println("wrapDBField('', 'concat(firstName, lastName)', '`', '`', '.'): " + 
          wrapDBField("", "concat(firstName, lastName)", "`", "`", "."));
      System.out.println("wrapDBField('', 'lower(concat(firstName, ' ', lastName))', '\"', '\"', '.'): " + 
          wrapDBField("", "lower(concat(firstName, ' ', lastName))", "\"", "\"", "."));
      System.out.println("wrapDBField('', 'lower(concat(User.firstName, ' ', User.lastName))C', '\"', '\"', '.'): " + 
          wrapDBField("", "lower(concat(User.firstName, ' ', User.lastName))", "\"", "\"", "."));
      System.out.println("wrapDBField('', 'lower(concat(firstName, ' ', lastName)) DESC', '\"', '\"', '.'): " + 
          wrapDBField("", "lower(concat(firstName, ' ', lastName)) DESC", "\"", "\"", "."));
      
      System.out.println("wrapDBField(LOWER(CONCAT(updatedBy.firstName, ' ', updatedBy.lastName)) DESC', '`')): " + 
          wrapDBField("LOWER(CONCAT(updatedBy.firstName, ' ', updatedBy.lastName)) DESC", "`"));
  }
}