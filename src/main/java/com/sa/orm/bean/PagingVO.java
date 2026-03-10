package com.sa.orm.bean;

import java.util.List;

import com.sa.orm.ORMInfoManager;

/**
 * <h4>Description</h4>Defines set and get methods for instance
 * members to serve as a value object for displaying records in paged
 * manner.
 * <h4>Copyrights</h4>Copyrights &copy; 2008 Soft Assets.
 * All Rights Reserved.
 * <h4>Company</h4>Soft Assets
 * <h4>Created</h4>October 27, 2010
 * @author Ali Ahsan
 * @version 1
 */
public class PagingVO implements java.io.Serializable {

  private static final long serialVersionUID = 4000000000161L;

  private int firstRowNumber;
  private int rowsPerPage = MAX_RECORDS_SHOWN;
  private int totalRows;
  private String sortBy;
  private String sortOrder;
  private int numberOfPagesToShow = MAX_RECORDS_SHOWN;
  
  private long totalPages = 0;
  private long currentPage = 1;
  private long pageFirstRecord = 1;
  private long pageLastRecord = 1;
  private long firstPageFirstRecord = 1;
  private long currentStartPage = 0;
  private long currentEndPage = 0;

  private List<?> results;
  
  public static final int MAX_RECORDS_SHOWN;
  
  /**
   * This static block loads constant values from a file.
   */
  static {
    int maxRecords = 10;
    try {
      maxRecords = ORMInfoManager.MAX_RECORDS_SHOWN;
    } catch(Exception e) {}
    
    MAX_RECORDS_SHOWN = maxRecords;
  } // end of static initializer

  /**
   * Does not do anything.
   */
  public PagingVO() {
    pageFirstRecord = 0;
  } // end of no-argument constructor
  
  /**
   * Sets given values as initial values for the instance members.
   * @param numberOfPagesToShow Number of pages to show on a screen.
   * @param recordsPerPage Records per page.
   * @param totalRecords Total records found.
   */
  public void setInitialValues(int numberOfPagesToShow, int recordsPerPage,
      int totalRecords) {

    this.numberOfPagesToShow = numberOfPagesToShow;
    this.rowsPerPage = recordsPerPage;
    this.totalRows = totalRecords;

    if(recordsPerPage < 1) {
      recordsPerPage = MAX_RECORDS_SHOWN;
    } // end of if
    pageFirstRecord = (totalRecords > 0) ? 1 : 0;
    pageLastRecord = pageFirstRecord + recordsPerPage - 1;
    if(pageLastRecord > totalRecords) {
      pageLastRecord = totalRecords;
    } // end of if
    firstPageFirstRecord = pageFirstRecord;
    currentPage = (totalRecords > 0) ? 1 : 0;
    totalPages = totalRecords / recordsPerPage;
    if(totalRecords % recordsPerPage > 0) {
      totalPages++;
    } // end of if

    currentStartPage = currentPage;

    currentEndPage = currentStartPage + numberOfPagesToShow - 1;
    if(totalPages < currentEndPage) {
      currentEndPage = totalPages;
    } // end of if

  } // end of method setInitialValues

  /**
   * @return the pageFirstRecord
   */
  public long getPageFirstRecord() {
    return pageFirstRecord;
  } // end of method getPageFirstRecord

  /**
   * @param pageFirstRecord the pageFirstRecord to set
   */
  public void setPageFirstRecord(long pageFirstRecord) {
    this.pageFirstRecord = pageFirstRecord;
  } // end of method setPageFirstRecord

  /**
   * @return the pageLastRecord
   */
  public long getPageLastRecord() {
    return pageLastRecord;
  } // end of method getPageLastRecord

  /**
   * @param pageLastRecord the pageLastRecord to set
   */
  public void setPageLastRecord(long pageLastRecord) {
    this.pageLastRecord = pageLastRecord;
  } // end of method setPageLastRecord

  /**
   * @return the numberOfPagesToShow
   */
  public int getNumberOfPagesToShow() {
    return numberOfPagesToShow;
  } // end of method getNumberOfPagesToShow

  /**
   * @param numberOfPagesToShow the numberOfPagesToShow to set
   */
  public void setNumberOfPagesToShow(int numberOfPagesToShow) {
    this.numberOfPagesToShow = numberOfPagesToShow;
  } // end of method setNumberOfPagesToShow

  /**
   * @return the currentEndPage
   */
  public long getCurrentEndPage() {
    return currentEndPage;
  } // end of method getCurrentEndPage

  /**
   * @param currentEndPage the currentEndPage to set
   */
  public void setCurrentEndPage(long currentEndPage) {
    this.currentEndPage = currentEndPage;
  } // end of method setCurrentEndPage

  /**
   * @return the currentStartPage
   */
  public long getCurrentStartPage() {
    return currentStartPage;
  } // end of method getCurrentStartPage

  /**
   * @param currentStartPage the currentStartPage to set
   */
  public void setCurrentStartPage(long currentStartPage) {
    this.currentStartPage = currentStartPage;
  } // end of method setCurrentStartPage

  /**
   * @return the totalPages
   */
  public long getTotalPages() {
    return totalPages;
  } // end of method getTotalPages

  /**
   * @param totalPages the totalPages to set
   */
  public void setTotalPages(long totalPages) {
    this.totalPages = totalPages;
  } // end of method setTotalPages

  /**
   * @return the currentPage
   */
  public long getCurrentPage() {
    return currentPage;
  } // end of method getCurrentPage

  /**
   * @param currentPage the currentPage to set
   */
  public void setCurrentPage(long currentPage) {
    this.currentPage = currentPage;
  } // end of method setCurrentPage

  /**
   * @return the results
   */
  public List<?> getResults() {
    return results;
  } // end of method getResults

  /**
   * @param results the results to set
   */
  public void setResults(List<?> results) {
    this.results = results;
  } // end of method setResults

  /**
   * @return the firstRowNumber
   */
  public int getFirstRowNumber() {
    return firstRowNumber;
  } // end of method getFirstRowNumber
  
  /**
   * @param firstRowNumber the firstRowNumber to set
   */
  public void setFirstRowNumber(int firstRowNumber) {
    this.firstRowNumber = firstRowNumber;
  } // end of method setFirstRowNumber
  
  /**
   * @return the rowsPerPage
   */
  public int getRowsPerPage() {
    return rowsPerPage;
  } // end of method getRowsPerPage
  
  /**
   * @param rowsPerPage the rowsPerPage to set
   */
  public void setRowsPerPage(int rowsPerPage) {
    this.rowsPerPage = rowsPerPage;
  } // end of method setRowsPerPage
  
  /**
   * @return the sortBy
   */
  public String getSortBy() {
    return sortBy;
  } // end of method getSortBy
  
  /**
   * @param sortBy the sortBy to set
   */
  public void setSortBy(String sortBy) {
    this.sortBy = sortBy;
  } // end of method setSortBy
  
  /**
   * @return the sortOrder
   */
  public String getSortOrder() {
    return sortOrder;
  } // end of method getSortOrder
  
  /**
   * @param sortOrder the sortOrder to set
   */
  public void setSortOrder(String sortOrder) {
    this.sortOrder = sortOrder;
  } // end of method setSortOrder
  
  /**
   * @return the totalRows
   */
  public int getTotalRows() {
    return totalRows;
  } // end of method getTotalRows
  
  /**
   * @param totalRows the totalRows to set
   */
  public void setTotalRows(int totalRows) {
    this.totalRows = totalRows;
  } // end of method setTotalRows
  
  /**
   * Sets the current page value to the given new value if valid. Also
   * calculates the rest of the variables.
   * @param number Number of page to be set as current page.
   * @throws java.lang.RuntimeException If the given page number does not exist.
   */
  public void moveToPage(long number)
      throws RuntimeException {

    if(number < 1 || number > totalPages) {
      throw new RuntimeException("Requested page does not exist.");
    } // end of if

    currentPage = number;
    firstRowNumber = (int)(currentPage - 1) * rowsPerPage + 1;
    System.out.println(firstRowNumber + " ========== ");
    pageFirstRecord = ((currentPage - 1) * rowsPerPage) + 1;
    pageLastRecord = pageFirstRecord + rowsPerPage - 1;
    if(pageLastRecord > totalRows) {
      pageLastRecord = totalRows;
    } // end of if

    firstPageFirstRecord = (((currentPage - 1) / numberOfPagesToShow) * numberOfPagesToShow * rowsPerPage) + 1;

    currentStartPage = (((currentPage - 1) / numberOfPagesToShow) * numberOfPagesToShow) + 1;
    currentEndPage = currentStartPage + numberOfPagesToShow - 1;
    if(totalPages < currentEndPage) {
      currentEndPage = totalPages;
    } // end of if
  } // end of  method moveToPage

  /**
   * Sets the current page to next value;
   */
  public void nextPage() {
    moveToPage(currentPage + 1);
  } // end of  method nextPage

  /**
   * Sets the current page to previous value;
   */
  public void previousPage() {
    moveToPage(currentPage - 1);
  } // end of  method previousPage
  
  /**
   * Returns a string containing the current values of the fields.
   * @return String containing the field names and their values.
   */
  public String toString() {
    StringBuffer str = new StringBuffer();

    str.append("firstRowNumber : ");
    str.append(firstRowNumber);
    str.append("\trowsPerPage : ");
    str.append(rowsPerPage);
    str.append("\ttotalRows : ");
    str.append(totalRows);
    str.append("\tsortBy : ");
    str.append(sortBy);
    str.append("\nsortOrder : ");
    str.append(sortOrder);
    str.append("\tnumberOfPagesToShow : ");
    str.append(numberOfPagesToShow);
    str.append("\ttotalPages : ");
    str.append(totalPages);
    str.append("\tcurrentPage : ");
    str.append(currentPage);
    str.append("\npageFirstRecord : ");
    str.append(pageFirstRecord);
    str.append("\tpageLastRecord : ");
    str.append(pageLastRecord);
    str.append("\tfirstPageFirstRecord : ");
    str.append(firstPageFirstRecord);
    str.append("\tcurrentStartPage : ");
    str.append(currentStartPage);
    str.append("\ncurrentEndPage : ");
    str.append(currentEndPage);
    return str.toString();
  } // end of method toString

} // end of class PagingVO
