package gl.core.aig;

import gl.core.util.McQueue;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;









public abstract class WorkerThread
  implements Runnable, BaseWorker
{
  protected Logger log = LogManager.getRootLogger();
  private int threadIndex = 0;
  private boolean active = false;
  private Object object = null;
  private McQueue workerThreadPool = null;

  
  public WorkerThread() {}

  
  public WorkerThread(int threadIndex) { this.threadIndex = threadIndex; }


  
  public void setPool(McQueue workerThreadPool) { this.workerThreadPool = workerThreadPool; }







  
  public final void setThreadIndex(int threadIndex) { this.threadIndex = threadIndex; }







  
  public final int getThreadIndex() { return this.threadIndex; }








  
  public final void setObject(Object object) { this.object = object; }







  
  public final Object getObject() { return this.object; }

  
  public final synchronized void awake() {
    this.active = true;
    notify();
  }






  
  public final boolean getActiveState() { return this.active; }







  
  public final void setActiveState(boolean active) { this.active = active; }



  
  public final synchronized void resetState() {
    try {
      setActiveState(false);
      
      setObject(null);
      this.workerThreadPool.push(this);
      
      wait();
    }
    catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  public void run() {
    while (true) {
      if (this.object != null)
      {
        process(this.object);
      }
      resetState();
    } 
  }


  
  public void printLog(String info) { this.log.info("TH-" + this.threadIndex + "|" + info); }
  
  public abstract void process(Object paramObject);
}
