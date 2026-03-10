package com.sa.orm;

import java.util.ArrayList;
import java.util.Arrays;

@SuppressWarnings({ "serial" })
public class SQLCriteria extends ArrayList {

  /**
   * Does not do anything
   */
  public SQLCriteria() {
    ;
  } // end of no-argument constructor
  
  /**
   * Adds all the {@link SQLCriterion} objects from <code>criteria</code> to
   * this object.
   */
  public SQLCriteria(SQLCriteria criteria) {
    this.addAll(criteria);
  } // end of constructor

  /**
   * Adds the given {@link SQLCriterion} objects to this object.
   */
  public SQLCriteria(SQLCriterion[] criteria) {
    this.addAll(Arrays.asList(criteria));
  } // end of constructor

  public boolean add(Object obj) {
    if(obj == null || obj instanceof SQLCriterion)
      throw new IllegalArgumentException("Please use overloaded method add(SQLCriterion) instead.");
    return this.add(obj);
  } // end of method add
  
  public boolean add(SQLCriterion criterion) {
    if(criterion == null)
      throw new NullPointerException("input argument must not be null.");
    return this.add(criterion);
  }
  
} // end of class SQLCriteria
