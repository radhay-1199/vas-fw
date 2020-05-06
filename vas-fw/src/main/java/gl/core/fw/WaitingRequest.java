package gl.core.fw;
import gl.core.aig.AigThreadPool;
import gl.core.timer.Timer;
import java.util.concurrent.ConcurrentHashMap;

public class WaitingRequest extends FWRef {
  Timer waitTimer = null;
  private ConcurrentHashMap<String, WaitEventInfo> serviceIdEventMap = null;
  public WaitingRequest(AigThreadPool aigThreadPool) {
    this.aigThreadPool = aigThreadPool;
    this.serviceIdEventMap = new ConcurrentHashMap<>();
  }
  public void addInWaitQueue(String serviceId, String msisdn, String event) throws Exception {
    WaitEventInfo waitInfo = this.serviceIdEventMap.get(serviceId + msisdn);
    if (waitInfo == null) {
      waitInfo = new WaitEventInfo(serviceId, msisdn, event);
      this.serviceIdEventMap.put(serviceId + msisdn, waitInfo);
    } else {
      
      waitInfo.addEvent(event);
    } 
  } public String getNextEventData(String serviceId, String msisdn) throws Exception {
    WaitEventInfo waitInfo = this.serviceIdEventMap.get(serviceId + msisdn);
    if (waitInfo == null) {
      return null;
    }
    
    String temp = waitInfo.getNextEvent(serviceId, msisdn);
    if (temp == null) {
      this.serviceIdEventMap.remove(serviceId + msisdn);
      waitInfo = null;
    } 
    return temp;
  }
  
  public boolean processNextEventData(EventInfo eventInfo) throws Exception {
    WaitEventInfo waitInfo = this.serviceIdEventMap.get(eventInfo.getServiceId() + eventInfo.getMsisdn());
    if (waitInfo == null) {
      return false;
    }
    
    String temp = waitInfo.getNextEvent(eventInfo.getServiceId(), eventInfo.getMsisdn());
    if (temp == null) {
      this.serviceIdEventMap.remove(eventInfo.getServiceId() + eventInfo.getMsisdn());
      waitInfo = null;
      return false;
    } 
    eventInfo.setEvent(temp);
    this.aigThreadPool.addTask(eventInfo);
    return true;
  }
}
