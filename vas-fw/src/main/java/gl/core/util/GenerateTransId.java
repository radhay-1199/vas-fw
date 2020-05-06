package gl.core.util;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;

public class GenerateTransId {
  private static AtomicLong tid = new AtomicLong(System.currentTimeMillis());
  private static AtomicInteger endCounter = new AtomicInteger(0);
  public static String getTid() {
    if (endCounter.get() >= 9999) {
      tid.set(System.currentTimeMillis());
      endCounter.set(0);
    } 
    return tid.get() + "" + endCounter.incrementAndGet();
  }
}
