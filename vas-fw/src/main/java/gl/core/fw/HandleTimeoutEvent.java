package gl.core.fw;

import gl.core.aig.AigThreadPool;
import gl.core.util.LogUtil;
import gl.core.util.McQueue;

public class HandleTimeoutEvent extends LogUtil implements Runnable {
  McQueue timeoutQueue = null;
  AigThreadPool aigThreadPool = null;
  McQueue mainQueue = null;
  public HandleTimeoutEvent(McQueue timeoutQueue, AigThreadPool aigThreadPool, McQueue mainQueue) {
    this.timeoutQueue = timeoutQueue;
    this.aigThreadPool = aigThreadPool;
    this.mainQueue = mainQueue;
  } public void run() {
    while (true) {
      try {
        while (true) {
          EventInfo eventInfo = (EventInfo)this.timeoutQueue.pull();
          eventInfo.setEvent("EVT_TIMEOUT");
          this.mainQueue.push("12345#12345#" + eventInfo.getServiceId() + "#" + eventInfo.getMsisdn() + "#EVT_TIMEOUT#");
        }  
      //  break;
      } catch (Exception exp) {
        exp.printStackTrace();
      } 
    } 
  }
}
