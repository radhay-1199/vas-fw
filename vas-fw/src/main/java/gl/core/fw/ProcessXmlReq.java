package gl.core.fw;

import gl.core.timer.Timer;
import gl.core.util.McQueue;

public class ProcessXmlReq implements Runnable {
  McQueue mainQueue = null;
  Timer timer = null;
  StateFlow stateFlow = null;
  public ProcessXmlReq(McQueue mainQueue, Timer timer, StateFlow stateFlow) {
    this.mainQueue = mainQueue;
    this.timer = timer;
    this.stateFlow = stateFlow;
  } public void run() {
    while (true) {
      try {
        while (true) {
          String str = (String)this.mainQueue.pull();
        }
      //  break;
      } catch (Exception exception) {}
    } 
  }
}
