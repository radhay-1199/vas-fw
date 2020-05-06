package gl.core.util;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class LogUtil extends Utility {
  private Logger log = LogManager.getRootLogger();
  
  public void printLog(int level, String logs) {
    switch (level) {
      case 0:
        this.log.info(logs); break;
    } 
    this.log.debug(logs);
  }


  
  public void printLog(Exception exp) { printLog(0, getPrintStackTrace(exp)); }


  
  public void printLog(int level, Exception exp) { printLog(level, getPrintStackTrace(exp)); }


  
  public void printLog(String log) { printLog(0, log); }
}
