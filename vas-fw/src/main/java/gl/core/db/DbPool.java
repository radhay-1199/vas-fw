package gl.core.db;

import gl.core.util.LogUtil;

public class DbPool extends LogUtil
{
  private String dbUrl = null;
  private String dbUser = null;
  private String dbPasswd = null;
  private int maxConn = 1;
  private int minConn = 1;
  
  public DbPool(String dbUrl, String dbUser, String dbPasswd, int minConn, int maxConn) {
    this.dbUrl = dbUrl;
    this.dbPasswd = dbPasswd;
    this.dbUser = dbUser;
    this.minConn = minConn;
    this.maxConn = maxConn;
  }

  
  public DbPool(String dbUrl, String dbUser, String dbPasswd, int maxConn) { this(dbUrl, dbUser, dbPasswd, maxConn, maxConn); }

  
  private String getDriverString(String url) {
    if (url != null) {
      if (url.indexOf("jdbc:oracle") != -1)
        return "oracle.jdbc.driver.OracleDriver"; 
      if (url.indexOf("jdbc:polyhedra") != -1)
        return "com.polyhedra.jdbc.JdbcDriver"; 
      if (url.indexOf("jdbc:mysql") != -1)
        return "com.mysql.jdbc.Driver"; 
      if (url.indexOf("jdbc:hive") != -1) {
        return "org.apache.hive.jdbc.HiveDriver";
      }
    } 
    return null;
  }
}
