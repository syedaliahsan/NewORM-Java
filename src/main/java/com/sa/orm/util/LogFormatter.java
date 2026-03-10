package com.sa.orm.util;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.MessageFormat;
import java.util.Date;
import java.util.logging.Formatter;
import java.util.logging.LogRecord;


/**
 * <h4>Description</h4>
 * 
 * <h4>Copyrights</h4>Copyrights &copy; 2008 - 2012 Soft Assets.
 * All Rights Reserved.
 * 
 * <h4>Company</h4>Soft Assets
 * <h4>Created</h4>April 23, 2009
 * @author Ali Ahsan
 * @version 1.0
 */
public class LogFormatter
    extends Formatter {

  private String lineSep = System.getProperty("line.separator");
  Date dat = new Date();
  private final static String format = "{0,date} {0,time}";
  private MessageFormat formatter;

  private Object args[] = new Object[1];

  /**
   * Format the given LogRecord.
   * @param record the log record to be formatted.
   * @return a formatted log record
   */
  public synchronized String format(LogRecord record) {
    StringBuffer sb = new StringBuffer();
    // Minimize memory allocations here.
    dat.setTime(record.getMillis());
    args[0] = dat;
    StringBuffer text = new StringBuffer();
    if (formatter == null) {
      formatter = new MessageFormat(format);
    }
    formatter.format(args, text, null);
    sb.append(text);
    sb.append(" ");
    if (record.getSourceClassName() != null) {  
        sb.append(record.getSourceClassName());
    } else {
      sb.append(record.getLoggerName());
    }
    if (record.getSourceMethodName() != null) { 
      sb.append(".");
      sb.append(record.getSourceMethodName());
    }
    sb.append(" - ");
    String message = formatMessage(record);
    sb.append(record.getLevel().getLocalizedName());
    sb.append(": ");
    sb.append(message);
    sb.append(lineSep);
    if(record.getThrown() != null) {
      try {
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        record.getThrown().printStackTrace(pw);
        pw.close();
        sb.append(sw.toString());
      } catch (Exception ex) {}
    }
    return sb.toString();
  }
} // end of class LogFormatter
