package gl.core.fw;
import gl.core.util.BaseConfig;

public class Config extends BaseConfig {
  public static int udp_port = 0;
  public static String db_conn = null;
  public static String db_user = null;
  public static String db_pass = null;
  public static String inputType = null;
  public static int workerThread = 1;

  
  public Config(String fileName) throws Exception { super(fileName); }

  
  public void loadProperties() {
    this.udp_port = getIntProperty("udp-port", udp_port);
    this.db_user = getParamValue("dbuser");
    this.db_pass = getParamValue("dbpassword");
    this.db_conn = getParamValue("db-conn");
    this.workerThread = getIntProperty("workerthread", workerThread);
  }
}
