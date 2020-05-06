package gl.core.fw;

import gl.core.util.McQueue;

public class ProcessRequest
  extends ProcessEvents
  implements Runnable {
  private int serviceIdIndex = -1;
  private int msisdnIndex = -1;
  public ProcessRequest(McQueue mainQueue, String InputFormat) {
    super(mainQueue);
    setInputFormat(InputFormat);
  }
  
  public void run() {
    while (true) {
      try {
        String data = (String)this.mainQueue.pull();
        printLog(data);
        
        String[] info = data.split("#");
        String serviceId = info[this.serviceIdIndex];
        
        String msisdn = info[this.msisdnIndex];

        
        String event = data.substring(data.indexOf(serviceId + "#" + msisdn) + (serviceId + "#" + msisdn).length() + 1, data.length() - 1);


        
        if (msisdn.equals("RELOAD")) {
          if (event.equals("LIB")) {
            printLog(0, "Reload Library Request");
            reloadLibrary();
            continue;
          } 
          if (event.equals("FLOW")) {
            printLog(0, "Reload State Flow Request for  serviceId=" + serviceId);
            reloadStateFlow(serviceId);
            
            continue;
          } 
        } 
        
        boolean isWorking = this.activeEvents.isInWorkingState(serviceId, msisdn);
        if (isWorking) {
          printLog(5, "Already in working state, ServiceId=" + serviceId + ",Msisdn=" + msisdn);
          this.waitTask.addInWaitQueue(serviceId, msisdn, event);

          
          continue;
        } 
        
        ServiceIdState serviceIdState = getStateFlow().getServiceIdState(serviceId);
        if (serviceIdState == null) {
          printLog(4, "Unknown service id =" + serviceId + ",drop request=" + data);
          
          continue;
        } 
        EventInfo eventInfo = this.activeEvents.getActiveState(serviceId, msisdn);
        if (eventInfo == null) {
          eventInfo = new EventInfo();
          eventInfo.setServiceId(serviceId);
          eventInfo.setMsisdn(msisdn);
          eventInfo.setEvent(event);
          eventInfo.setTimer(this.timer);
          eventInfo.setActiveEvents(this.activeEvents);
          eventInfo.setServiceIdState(serviceIdState);
          eventInfo.setWaitingRequest(this.waitTask);
          eventInfo.setLibraryInfo(getLibrary());
        }
        else {
          
          eventInfo.setEvent(event);
          if (eventInfo.getTimerId() != null) {
            eventInfo.getTimer().remove(eventInfo.getTimerId());
            eventInfo.setTimerId(null);
          } 
        } 

        
        this.activeEvents.addInWorkingState(serviceId, msisdn);
        printLog("Added Task");
        this.aigThreadPool.addTask(eventInfo);
      
      }
      catch (Exception exp) {
        exp.printStackTrace();
      } 
    } 
  }
  public void setInputFormat(String inputFormat) {
    if (inputFormat != null) {
      String[] inputLoc = inputFormat.split("#");
      for (int loc = 0; loc < inputLoc.length; loc++) {
        if (inputLoc[loc].equalsIgnoreCase("SERVICEID")) {
          this.serviceIdIndex = loc;
        }
        else if (inputLoc[loc].equalsIgnoreCase("MSISDN")) {
          this.msisdnIndex = loc;
        } 
      } 
    } 
    if (this.serviceIdIndex == -1 || this.msisdnIndex == -1) {
      this.serviceIdIndex = 2;
      this.msisdnIndex = 3;
    } 
  }
}