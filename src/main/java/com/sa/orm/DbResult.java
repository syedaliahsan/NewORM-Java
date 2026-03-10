package com.sa.orm;

import java.util.ArrayList;
import java.util.Collection;

public class DbResult<T> {
  private final Collection<T> entities;
  private final int affectedRows;
  private final boolean hasEntities;
  
  // Constructor for entity return
  public DbResult(T entity) {
    this.entities = new ArrayList<T>();
    entities.add(entity);
    this.affectedRows = (entities != null) ? entities.size() : 0;
    this.hasEntities = (entities != null && entities.size() > 0);
}

  public DbResult(Collection<T> entities) {
      this.entities = entities;
      this.affectedRows = (entities != null) ? entities.size() : 0;
      this.hasEntities = (entities != null && entities.size() > 0);
  }
  
  // Constructor for count-only return
  public DbResult(int affectedRows) {
      this.entities = null;
      this.affectedRows = affectedRows;
      this.hasEntities = false;
  }
  
  // Getters
  public Collection<T> getEntities() {
      return entities;
  }
  
  public int getAffectedRows() {
      return affectedRows;
  }
  
  public boolean hasEntities() {
      return hasEntities;
  }
  
  public boolean wasSuccessful() {
      return affectedRows > 0 || hasEntities;
  }
}