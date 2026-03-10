package com.sa.orm.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true) // Safer: ignores extra JSON fields
public class SearchRequest {
    private Long firmId;
    private FilterGroup group;
    private String orderBy;
    private boolean sortOrderAscending = true; 
    private Integer pageSize = 25;
    private Integer pageNumber = 1;
    private boolean includeCount = true;
    private boolean distinct = false;
    private List<String> columns;
    private boolean ignoreLimit = false;

    // --- Getters and Setters ---

    public Long getFirmId() { return firmId; }
    public void setFirmId(Long firmId) { this.firmId = firmId; }

    public FilterGroup getGroup() { return group; }
    public void setGroup(FilterGroup group) { this.group = group; }

    public String getOrderBy() {
      if(orderBy == null || orderBy.trim().length() < 1) return orderBy;
      return orderBy + (this.sortOrderAscending ? " ASC" : " DESC");
    }
    public void setOrderBy(String orderBy) { this.orderBy = orderBy; }

    public boolean isSortOrderAscending() { return sortOrderAscending; }
    public void setSortOrderAscending(boolean sortOrderAscending) { this.sortOrderAscending = sortOrderAscending; }

    // Special setter to handle JSON "sortDirection": "desc"
    @JsonProperty("sortDirection")
    public void setSortDirection(String direction) {
        this.sortOrderAscending = !"desc".equalsIgnoreCase(direction);
    }

    public Integer getPageSize() { return pageSize; }
    public void setPageSize(Integer pageSize) { 
      this.pageSize = pageSize;
      if(this.pageSize < 2) this.pageSize = 25;
    }

    public Integer getPageNumber() { return pageNumber; }
    public void setPageNumber(Integer pageNumber) {
      this.pageNumber = pageNumber;
      if(this.pageNumber < 1) this.pageNumber = 1;
    }

    public boolean isIncludeCount() { return includeCount; }
    public void setIncludeCount(boolean includeCount) { this.includeCount = includeCount; }

    public boolean isDistinct() { return distinct; }
    public void setDistinct(boolean distinct) { this.distinct = distinct; }

    public List<String> getColumns() { return columns; }
    public void setColumns(List<String> columns) { this.columns = columns; }

    public boolean isIgnoreLimit() { return ignoreLimit; }
    public void setIgnoreLimit(boolean ignoreLimit) { this.ignoreLimit = ignoreLimit; }
}