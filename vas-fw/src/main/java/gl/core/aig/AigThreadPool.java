package gl.core.aig;

import gl.core.util.LogUtil;
import gl.core.util.McQueue;
import java.util.Vector;






public class AigThreadPool
  extends LogUtil
  implements Runnable
{
  McQueue taskQueue;
  Vector<WorkerThread> workerObjects;
  McQueue workerObjectsPool;
  private int index = 0;
  private int poolSize = 10;



  
  public AigThreadPool() {
    this.taskQueue = new McQueue();
    this.workerObjectsPool = new McQueue();
  }









  
  public String initPool(int poolSize, Object worker) {
    this.poolSize = poolSize;
    return initPoolQueue(worker);
  }









  
  public void addTask(Object data) { this.taskQueue.push(data); }









  
  public String initPool(Object worker) {
    String response = "success";
    if (worker == null) {
      System.out.println("ThreadPool - Please initialize worker object");
      printLog(5, "ThreadPool - Please initialize worker object");
      return "Please initialize object";
    } 
    
    try {
      Class<?> baseClass = worker.getClass();
      this.workerObjects = new Vector<>(this.poolSize);
      for (int count = 1; count <= this.poolSize; count++)
      {
        WorkerThread obj = (WorkerThread)baseClass.newInstance();
        obj.setThreadIndex(count);
        Thread thread = new Thread(obj, "WorkerThread-" + count);
        thread.start();
        this.workerObjects.add(obj);
      }
    
    } catch (Exception exp) {
      response = exp.getMessage();
    } 
    return response;
  }

  
  public String initPoolQueue(Object worker) {
    String response = "success";
    if (worker == null) {
      System.out.println("ThreadPool - Please initialize object");
      printLog(5, "ThreadPool - Please initialize worker object");
      return "Please initialize object";
    } 
    
    try {
      Class<?> baseClass = worker.getClass();
      
      for (int count = 1; count <= this.poolSize; count++)
      {
        WorkerThread obj = (WorkerThread)baseClass.newInstance();
        obj.setThreadIndex(count);
        obj.setPool(this.workerObjectsPool);
        Thread thread = new Thread(obj, "WorkerThread-" + count);
        thread.start();
      }
    
    }
    catch (Exception exp) {
      printLog(6, exp);
      response = exp.getMessage();
    } 
    return response;
  }
  public void run() {
    while (true) {
      try {
        while (true)
        { 
          Object data = this.taskQueue.pull();
          doAction(data); 
         }
   //     break;
      } catch (Exception e) {
        printLog(6, e);
        e.printStackTrace();
      } 
    } 
  }
  
  private WorkerThread getAvailableThread() {
    try {
      int loc = this.index;
      if (loc >= this.poolSize)
        loc = 0; 
      while (loc < this.poolSize) {
        WorkerThread worker = this.workerObjects.elementAt(loc);
        if (!worker.getActiveState()) {
          this.index = ++loc;
          return worker;
        } 
        loc++;
      } 
      this.index = this.poolSize;
    } catch (Exception exp) {
      exp.printStackTrace();
    } 
    return null;
  }


  
  private WorkerThread getAvailableThreadQueue() {
    try {
      return (WorkerThread)this.workerObjectsPool.pull();
    } catch (Exception exp) {
      exp.printStackTrace();
      
      return null;
    } 
  }
  private void doAction(Object data) {
    try {
      WorkerThread worker = null;
      
      do {
        worker = getAvailableThreadQueue();
      
      }
      while (worker == null);


      
      synchronized (worker) {
        worker.setObject(data);
        worker.awake();
      
      }
    
    }
    catch (Exception e) {
      printLog(6, e);
      e.printStackTrace();
    } 
  }
}
