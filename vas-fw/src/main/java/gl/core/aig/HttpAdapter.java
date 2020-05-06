package gl.core.aig;

import gl.core.util.LogUtil;
import gl.core.util.McQueue;





public class HttpAdapter
  extends LogUtil
  implements Runnable
{
  private McQueue taskQueue;
  private int index = 0;
  private int poolSize = 10;
  private AigThreadPool aigPool = null;
  private TpsConfig tpsConfig = null;




  
  public HttpAdapter(TpsConfig tpsConfig) {
    this.taskQueue = new McQueue();
    this.tpsConfig = tpsConfig;
    this.aigPool = new AigThreadPool();
    Thread thread = new Thread(this.aigPool, "AigThreadPool");
    thread.start();
  }




  
  public String initPool(int poolSize, Object worker) {
    this.poolSize = poolSize;
    return initPool(worker);
  }




  
  public void addTask(String id, Object object) {
    AigDataInfo aigDataInfo = new AigDataInfo();
    aigDataInfo.setId(id);
    aigDataInfo.setData(object);
    this.taskQueue.push(aigDataInfo);
  }





  
  public String initPool(Object worker) { return this.aigPool.initPool(this.poolSize, worker); }
  
  public void run() {
    while (true) {
      try {
        AigDataInfo aigDataInfo = (AigDataInfo)this.taskQueue.pull();
        SubIDInfo idinfo = this.tpsConfig.getAvailableSubId(aigDataInfo.getId());
        if (idinfo != null) {
          aigDataInfo.setConfig(idinfo.getConfig());
          this.aigPool.addTask(aigDataInfo);
          continue;
        } 
        printLog(5, "no id information available for id=" + aigDataInfo.getId());
      }
      catch (Exception e) {
        e.printStackTrace();
      } 
    } 
  }
  
  public void removeId(String id) {}
}
