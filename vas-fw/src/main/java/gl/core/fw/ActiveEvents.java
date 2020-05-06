package gl.core.fw;
import gl.core.util.LogUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class ActiveEvents extends LogUtil {
  private ConcurrentHashMap<String, EventInfo> activeEventState = null;
  private ConcurrentHashMap<String, Boolean> inWorkingState = null;
  public ActiveEvents() {
    this.activeEventState = new ConcurrentHashMap<>();
    this.inWorkingState = new ConcurrentHashMap<>();
  }

  
  public void registerCurrentState(String serviceId, String msisdn, EventInfo eventInfo) throws Exception { this.activeEventState.put(serviceId + msisdn, eventInfo); }


  
  public EventInfo getActiveState(String serviceId, String msisdn) throws Exception { return this.activeEventState.get(serviceId + msisdn); }


  
  public void addInWorkingState(String serviceId, String msisdn) throws Exception { this.inWorkingState.put(serviceId + msisdn, Boolean.valueOf(true)); }

  
  public boolean isInWorkingState(String serviceId, String msisdn) throws Exception {
    Boolean status = this.inWorkingState.get(serviceId + msisdn);
    if (status == null) {
      return false;
    }
    return status.booleanValue();
  }

  
  public void removeFromWorkingState(String serviceId, String msisdn) throws Exception { this.inWorkingState.remove(serviceId + msisdn); }


  
  public void removeState(String serviceId, String msisdn) throws Exception { this.activeEventState.remove(serviceId + msisdn); }
  
  public List getActiveEventList() throws Exception {
    List list = new ArrayList(this.activeEventState.values());
    return list;
  }
}
