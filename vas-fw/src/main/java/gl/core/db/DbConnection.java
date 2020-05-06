package gl.core.db;

import java.sql.Connection;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;


public class DbConnection
  extends DbDetailsValidation
{
  private Logger log = LogManager.getRootLogger();
  private String dbType = null;
  private String url = null;
  private String user = null;
  private String passwd = null;




















  
  public Connection getConnection() {
    try {
      return DataSource.getConnection();
    } catch (Exception exp) {
      exp.printStackTrace();
      
      return null;
    } 
  }
}
