package gl.core.util;

import java.util.concurrent.atomic.AtomicInteger;

public class McQueueBuffer {
  AtomicInteger indexRear = new AtomicInteger(0);
  AtomicInteger indexFrount = new AtomicInteger(0);
  AtomicInteger index = new AtomicInteger(0);
  int size = 1000000;
  Object[] obj = new Object[this.size];
  int max = 0;
  
  public void push(Object obj) {
    this.index.incrementAndGet();
    this.obj[this.indexRear.incrementAndGet()] = obj;
    if (this.indexRear.get() == this.size)
      this.indexRear.set(0); 
    if (this.index.get() == 1) {
      synchronized (this) {
        notify();
      } 
    }
  }

  
  public Object pull() throws Exception {
    if (this.index.get() == 0) {
      try {
        synchronized (this)
        { wait(); } 
      } catch (InterruptedException interruptedException) {}
    }
    Object objj = this.obj[this.indexFrount.getAndIncrement()];
    this.index.getAndDecrement();
    if (this.indexFrount.get() == this.size)
      this.indexFrount.set(0); 
    return objj;
  }
  
  public int getLength() { return this.index.get(); }
}
