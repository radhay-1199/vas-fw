package gl.core.timer;

import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;



public class KeyManager
{
  private AtomicInteger intCounter = null;
  private AtomicLong longCounter = null;
  public KeyManager() {
    this.intCounter = new AtomicInteger((int)System.currentTimeMillis() / 10000);
    this.longCounter = new AtomicLong(System.currentTimeMillis());
  }
  
  public long getLongKey() { return this.longCounter.incrementAndGet(); }

  
  public int getKey() { return this.intCounter.incrementAndGet(); }
}
