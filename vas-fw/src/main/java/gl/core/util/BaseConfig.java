package gl.core.util;

import java.io.FileInputStream;
import java.util.Properties;

public class BaseConfig {
  Properties properties = new CaselessProperties();
  
  public BaseConfig(String configFile) throws Exception {
    FileInputStream propsFile = new FileInputStream(configFile);
    this.properties.load(propsFile);
    propsFile.close();
  }
  public String getParamValue(String param_name) {
    String param_value = null;
    param_value = this.properties.getProperty(param_name);
    if (param_value != null)
      param_value = param_value.trim(); 
    return param_value;
  }
  public String getParamValue(String param_name, String defautValue) {
    String param_value = null;
    param_value = this.properties.getProperty(param_name);
    if (param_value != null) {
      return param_value.trim();
    }
    return defautValue;
  }
  
  public boolean getBooleanProperty(String propName) {
    String value = getParamValue(propName);
    if (value == null) {
      return false;
    }
    value = value.trim();
    if (value.equalsIgnoreCase("true")) {
      return true;
    }
    return false;
  }
  
  public boolean getBooleanProperty(String propName, boolean defaultValue) {
    String value = getParamValue(propName);
    System.out.println("boolean Property [" + propName + "] " + value);
    if (value == null) {
      return defaultValue;
    }
    value = value.trim();
    if (value.equalsIgnoreCase("true")) {
      return true;
    }
    return false;
  }
  public int getIntProperty(String propName, int defaultValue) {
    String value = this.properties.getProperty(propName, Integer.toString(defaultValue)).trim();
    return Integer.parseInt(value);
  }
  public byte getByteProperty(String propName, byte defaultValue) {
    String temp = getParamValue(propName);
    if (temp == null) {
      return defaultValue;
    }
    return Byte.parseByte(temp);
  }
  
  public char getCharProperty(String propName, char defaultValue) {
    char value = this.properties.getProperty(propName, Integer.toString(defaultValue)).trim().toCharArray()[0];
    return value;
  }
}
