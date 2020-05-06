package gl.core.fw;
import gl.core.aig.WorkerThread;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class AdapterWorker extends WorkerThread {
  private Logger log = LogManager.getRootLogger();
  public void process(Object packet) {
    try {
      EventInfo eventInfo = (EventInfo)packet;
      String serviceId = eventInfo.getServiceId();
      String event = eventInfo.getEvent();
      
      this.log.info(getThreadIndex() + "| serviceId=" + serviceId + ", msisdn=" + eventInfo.getMsisdn() + ", eventid=" + event);
      
      StateInfo stateInfo = eventInfo.getServiceIdState().getNextState(event, eventInfo.getStateInfo());
      
      if (stateInfo != null) {
        
        eventInfo.setStateInfo(stateInfo);
        if (stateInfo.getTimeOut() > 0) {
          
          String timerId = eventInfo.getTimer().add(eventInfo, stateInfo.getTimeOut());
          eventInfo.setTimerId(timerId);
          this.log.info(getThreadIndex() + "|msisdn=" + eventInfo.getMsisdn() + ",timeout=" + stateInfo.getTimeOut() + ",Timer Id=" + timerId);
        } 


        
        String status = null;
        
        try { printLog("Going to execute");
          status = eventInfo.getLibraryInfo().execute(eventInfo.getMsisdn(), event, stateInfo); }
        catch (Exception exp) { exp.printStackTrace(); }

        
        this.log.info(getThreadIndex() + "|serviceId=" + serviceId + ",msisdn=" + eventInfo.getMsisdn() + ",Event=" + event + ",Function=" + stateInfo.getActionId() + ",SequenceNumber=" + stateInfo.getSeqNumber() + ",CurrentState=" + stateInfo.getCurrentId() + ",NextState=" + stateInfo.getNextStateId() + ",Execute=" + status);
        
        if (stateInfo.getTimeOut() > 0) {
          eventInfo.getActiveEvents().registerCurrentState(serviceId, eventInfo.getMsisdn(), eventInfo);
          this.log.info("Register Active State");
        } else {
          
          eventInfo.getActiveEvents().removeState(serviceId, eventInfo.getMsisdn());
          this.log.info("Remove Active State, As no timeout configured");
        
        }
      
      }
      else {
        
        eventInfo.getActiveEvents().removeState(serviceId, eventInfo.getMsisdn());
      } 
      
      eventInfo.getActiveEvents().removeFromWorkingState(serviceId, eventInfo.getMsisdn());
      eventInfo.getWaitingRequest().processNextEventData(eventInfo);

    
    }
    catch (Exception exp) {
      exp.printStackTrace();
    } 
  }
}

