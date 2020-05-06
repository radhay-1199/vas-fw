package gl.core.tps;

import gl.core.util.Utility;
import java.util.concurrent.ConcurrentHashMap;



public class InboudTps
  extends Thread
{
  private ConcurrentHashMap<Long, Integer> timeTpsMap = null;
  private Utility util = null;
  private int tps = 0;



  
  public InboudTps(int tps) {
    if (tps <= 0) {
      System.out.println("Total TPS can not be less than or equals to zero");
      return;
    } 
    this.tps = tps;
    this.timeTpsMap = new ConcurrentHashMap<>();
    this.util = new Utility();
  }


  
  public boolean updateCounter() {
    Long currentTime = Long.valueOf(System.currentTimeMillis() / 1000L);
    Integer count = this.timeTpsMap.get(currentTime);
    if (count == null) {
      count = Integer.valueOf(1);
    } else {
      if (count.intValue() >= this.tps) {
        return false;
      }
      count = Integer.valueOf(count.intValue() + 1);
    }  this.timeTpsMap.put(currentTime, count);
    return true;
  }



  
  public void setTps(int tps) { this.tps = tps; }
  
  public void run() {
    while (true) {
      try {
        Long chkTime = Long.valueOf(System.currentTimeMillis() / 600000L);
        for (Long i : this.timeTpsMap.keySet()) {
          if (i.longValue() < chkTime.longValue()) {
            System.out.println("Removing for =" + i);
            this.timeTpsMap.remove(i);
          } 
        } 
        Thread.sleep(60000L);
      }
      catch (Exception exception) {}
    } 
  }
}
