package gl.core.fw;

import gl.core.util.LogUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


public class ServiceIdState
  extends LogUtil
{
  private String serviceId = null;
  private StateInfo startState = null;
  private ConcurrentHashMap<String, StateInfo> serviceIdCurrentIdEventMap = null;
  private ConcurrentHashMap<String, StateInfo> eventBasedMap = null;
  
  public ServiceIdState(String serviceId) {
    this.serviceId = serviceId;
    this.serviceIdCurrentIdEventMap = new ConcurrentHashMap<>();
    this.eventBasedMap = new ConcurrentHashMap<>();
  }
  
  public void addState(StateInfo stateInfo) throws Exception {
    printLog(5, "add state , serviceId=" + this.serviceId + ",CurrentId=" + stateInfo.getCurrentId() + ",Event=" + stateInfo.getEventId() + ",ActionId=" + stateInfo.getActionId());
    this.serviceIdCurrentIdEventMap.put(stateInfo.getCurrentId() + stateInfo.getEventId(), stateInfo);
    this.eventBasedMap.put(stateInfo.getEventId(), stateInfo);
  }


  
  public StateInfo getNextState(String event, StateInfo previousState) throws Exception {
    StateInfo nextState = null;
    if (previousState == null) {
      printLog(5, "No previous state ,going to get state based on event=" + event);
      return getEventState(event);
    } 


    
    nextState = this.serviceIdCurrentIdEventMap.get(previousState.getNextStateId() + event);
    if (nextState == null) {

      
      printLog(5, "No state found for serviceId=" + this.serviceId + ", CurrentState=" + previousState.getCurrentId() + ",event=" + event);
      nextState = this.serviceIdCurrentIdEventMap.get(previousState.getNextStateId() + "EVT_ERROR");
      if (nextState == null) {
        printLog(5, "No state found for serviceId=" + this.serviceId + ", CurrentState=" + previousState.getCurrentId() + ",event=EVT_ERROR");
        return getEventState(event);
      } 
    } 
    
    return nextState;
  }
  
  public StateInfo getEventState(String event) throws Exception {
    if (event.equals("EVT_TIMEOUT")) {
      return null;
    }
    printLog("eventBasedMap Size=" + this.eventBasedMap.size());
    printMap();
    StateInfo nextState = this.eventBasedMap.get(event);
    
    if (nextState == null) {
      printLog(5, "No defined state found for serviceId=" + this.serviceId + ",event=" + event + ",going for Start");
      return this.eventBasedMap.get("Start");
    } 
    
    return nextState;
  }
  public List getStateFlowList() throws Exception {
    List list = new ArrayList(this.eventBasedMap.values());
    return list;
  }
  public void printMap() {
    for (Map.Entry<String, StateInfo> entry : this.eventBasedMap.entrySet())
      printLog("Key=" + (String)entry.getKey()); 
  }
}