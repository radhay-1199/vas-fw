package gl.core.oldfw;

import gl.core.db.DataSource;
import java.sql.Connection;




public class DumpLib
{
  public static Connection Connection = DataSource.getConnection();
  public static LogWriter LWA = null;
}
