package gl.core.timer;

import gl.core.tps.Controller;
import gl.core.util.LogUtil;
import gl.core.util.McQueue;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TimerDataMap
  extends LogUtil
  implements Runnable {
  ConcurrentHashMap<Integer, ConcurrentHashMap> timeStampData = null;
  ConcurrentHashMap<String, Integer> timerIdTimeStampMap = null;
  McQueue queue = null;
  Controller expireControl = null;
  int expireTps = -1;
  public TimerDataMap(McQueue queue, int expireTps) {
    this.queue = queue;
    this.expireTps = expireTps;
    if (expireTps >= 0) {
      this.expireControl = new Controller(expireTps);
    } else {
      this.expireControl = new Controller(1);
    } 
    this.timerIdTimeStampMap = new ConcurrentHashMap<>();
    this.timeStampData = new ConcurrentHashMap<>();
  }
  
  public void add(String timerId, int time, Data data) throws Exception {
    int time_out_point = (int)(System.currentTimeMillis() / 1000L + time);
    ConcurrentHashMap<Object, Object> lst = this.timeStampData.get(Integer.valueOf(time_out_point));
    if (lst == null) {
      lst = new ConcurrentHashMap<>();
      this.timeStampData.put(Integer.valueOf(time_out_point), lst);
      this.timerIdTimeStampMap.put(timerId, Integer.valueOf(time_out_point));
    } 
    lst.put(timerId, data);
  }
  public void run() {
    Controller control = new Controller(1); while (true) {
      try {
        while (true) {
          checkAndExpire(null);
          control.updateCounter();
        } 
        //break;
      } catch (Exception ex) {
        printLog(ex);
      } 
    } 
  }
  public void expireAllForTimeStamp(int timerstamp) throws Exception {
    ConcurrentHashMap<String, Data> hmap = this.timeStampData.remove(Integer.valueOf(timerstamp));
    if (hmap == null) {
      return;
    }
    this.expireControl.resetTps(hmap.size() * this.timeStampData.size());
    for (Map.Entry<String, Data> entry : hmap.entrySet()) {
      String timerid = entry.getKey();
      Data data = entry.getValue();
      this.timerIdTimeStampMap.remove(timerid);
      this.queue.push(data);
      this.expireControl.updateCounter();
    } 
  }
  public void checkAndExpire(String temp) throws Exception {
    List<Integer> keyList = new ArrayList<>(this.timeStampData.keySet());
    
    Collections.sort(keyList);
    int checkTime = (int)System.currentTimeMillis() / 1000;
    for (Integer timeStamp : keyList) {
      int lTimeStamp = timeStamp.intValue();
      if (lTimeStamp <= checkTime) {
        expireAllForTimeStamp(lTimeStamp);
      }
    } 
  }


  
  public Data remove(String timerId) throws Exception {
    Data data = null;
    Integer timestamp = this.timerIdTimeStampMap.remove(timerId);
    if (timestamp == null)
      return null; 
    ConcurrentHashMap hmap = this.timeStampData.get(timestamp);
    if (hmap != null)
      data = (Data)hmap.remove(timerId); 
    return data;
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
