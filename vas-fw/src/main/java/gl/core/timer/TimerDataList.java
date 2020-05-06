package gl.core.timer;

import gl.core.tps.Controller;
import gl.core.util.LogUtil;
import gl.core.util.McQueue;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;



public class TimerDataList
  extends LogUtil
  implements Runnable
{
  ConcurrentHashMap<Integer, DoubleyLinketList> delayTimeStampMap = null;
  ConcurrentHashMap<String, Node> timerIdNodeMap = null;
  McQueue queue = null;
  Controller expireControl = null;
  int expireTps = -1;
  public TimerDataList(McQueue queue, int expireTps) {
    this.queue = queue;
    this.expireTps = expireTps;
    if (expireTps >= 0) {
      this.expireControl = new Controller(expireTps);
    } else {
      this.expireControl = new Controller(1);
    }  this.delayTimeStampMap = new ConcurrentHashMap<>();
    this.timerIdNodeMap = new ConcurrentHashMap<>(110000, 10000.0F);
  }
  
  public void add(String timerId, int time, Data data) throws Exception {
    long time_out_point = System.currentTimeMillis() + (time * 1000);
    
    DoubleyLinketList lst = this.delayTimeStampMap.get(Integer.valueOf(time));
    if (lst == null) {
      lst = new DoubleyLinketList(time);
      this.delayTimeStampMap.put(Integer.valueOf(time), lst);
    } 
    Node node = lst.insertAtEnd(Integer.valueOf(time), time_out_point, data);
    this.timerIdNodeMap.put(timerId, node);
  }
  public void run() {
    Controller control = new Controller(10); while (true) {
      try {
        while (true) {
          checkAndExpire();
          control.updateCounter();
        }  
        //break;
      } catch (Exception ex) {
        printLog(ex);
      } 
    } 
  }
  
  public void checkAndExpire() throws Exception {
    List<DoubleyLinketList> nodeList = new ArrayList<>(this.delayTimeStampMap.values());



    
    for (int index = 0; index < nodeList.size(); index++) {
      DoubleyLinketList lst = nodeList.get(index);
      if (lst != null) {

        
        Node ptr = lst.start;
        
        while (ptr != null && 
          ptr.getTimeStamp() <= System.currentTimeMillis()) {
          
          lst.deleteNode(ptr);
          this.timerIdNodeMap.remove(ptr.getData().getKey());
          this.queue.push(ptr.getData());
          ptr = ptr.getLinkNext();


          
          if (this.expireTps > 0)
            this.expireControl.updateCounter(); 
        } 
      } 
    } 
  }
  public Data get(String timerId) throws Exception {
    Node ptr = this.timerIdNodeMap.get(timerId);
    if (ptr == null)
      return null; 
    return ptr.getData();
  }
  public Data remove(String timerId) throws Exception {
    Node ptr = this.timerIdNodeMap.remove(timerId);
    if (ptr == null)
      return null; 
    DoubleyLinketList nodeList = this.delayTimeStampMap.get(ptr.getDelayTime());
    if (nodeList != null) {
      nodeList.deleteNode(ptr);
    }
    return ptr.getData();
  }
  public boolean reset(String timerId, int delay) throws Exception {
    Data data = null;
    data = remove(timerId);
    if (data == null) {
      return false;
    }
    add(timerId, delay, data);
    return true;
  }
}
