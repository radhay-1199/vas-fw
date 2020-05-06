package gl.core.util;

import java.util.LinkedList;
import java.util.concurrent.atomic.AtomicInteger;





public class McQueue
{
  private AtomicInteger length = new AtomicInteger(0);
  private LinkedList<Object> linklist = null;



  
  public McQueue() { this.linklist = new LinkedList(); }




  
  public synchronized void push(Object object) {
    this.linklist.addLast(object);
    this.length.incrementAndGet();
    if (this.length.get() == 1) {
      notify();
    }
  }



  
  public synchronized Object pull() {
    while (this.length.get() == 0) {
      try {
        wait();
      }
      catch (InterruptedException interruptedException) {}
    } 
    
    this.length.getAndDecrement();
    return pull0();
  }

  
  private Object pull0() { return this.linklist.removeFirst(); }





  
  public int getLength() { return this.length.get(); }
}
