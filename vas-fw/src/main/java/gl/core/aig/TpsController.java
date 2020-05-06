package gl.core.aig;

import gl.core.tps.Controller;
import gl.core.util.LogUtil;
import gl.core.util.McQueue;





public class TpsController
  extends LogUtil
  implements Runnable
{
  private McQueue taskQueue;
  private HttpAdapter httpAdapter = null;
  private Controller tpsCounter = null;
  private String id = null;
  private boolean keepWorking = true;
  private TpsConfig tpsConfig = null;





  
  public TpsController(String id, HttpAdapter httpAdapter, TpsConfig tpsConfig) {
    this.taskQueue = new McQueue();
    this.httpAdapter = httpAdapter;
    this.tpsConfig = tpsConfig;
    this.id = id;
    printLog(4, "Goign to start TPS controller for id =" + id + ",tps=" + tpsConfig.getTps(id));
    this.tpsCounter = new Controller(tpsConfig.getTps(id));
  }




  
  public void addTask(Object object) { this.taskQueue.push(object); }
  
  public void run() {
    while (true) {
      try {
        if (!this.keepWorking)
          break; 
        Object objectInfo = this.taskQueue.pull();
        doAction(objectInfo);
        this.tpsCounter.updateCounter();
      }
      catch (Exception e) {
        e.printStackTrace();
      } 
    } 
  }
  private void doAction(Object objectInfo) {
    try {
      this.httpAdapter.addTask(this.id, objectInfo);
    }
    catch (Exception e) {
      e.printStackTrace();
    } 
  }



  
  public void stopWorking() { this.keepWorking = false; }




  
  public void resetTps() {
    int tps = this.tpsConfig.getTps(this.id);
    if (tps <= 0) {
      this.keepWorking = false;
    } else {
      this.tpsCounter.resetTps(tps);
    }  printLog(4, "TPS controller for id =" + this.id + ",tps=" + tps);
  }
}
