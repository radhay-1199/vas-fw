package gl.core.oldfw;
import gl.core.util.LogUtil;

public class LogWriter extends LogUtil {
  public void log(int logLevel, String msg) { printLog(logLevel, msg); }
}
