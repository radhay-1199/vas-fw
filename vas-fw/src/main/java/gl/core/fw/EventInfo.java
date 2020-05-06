package gl.core.fw;

import gl.core.timer.Timer;

public class EventInfo {
  String serviceId = null;
  String msisdn = null;
  String event = null;
  String timerId = null;
  StateInfo stateInfo = null;
  ServiceIdState stateFlow = null;
  LibraryInfo libraryInfo = null;
  ActiveEvents activeEvents = null;
  Timer timer = null;
  WaitingRequest waitTask = null;
  public EventInfo() {}
  
  public EventInfo(String serviceId, String msisdn, String event) {
    this.serviceId = serviceId;
    this.msisdn = msisdn;
    this.event = event;
  }
  
  public String getServiceId() { return this.serviceId; }

  
  public String getMsisdn() { return this.msisdn; }

  
  public String getEvent() { return this.event; }

  
  public LibraryInfo getLibraryInfo() { return this.libraryInfo; }

  
  public StateInfo getStateInfo() { return this.stateInfo; }

  
  public ServiceIdState getServiceIdState() { return this.stateFlow; }

  
  public ActiveEvents getActiveEvents() { return this.activeEvents; }

  
  public Timer getTimer() { return this.timer; }

  
  public String getTimerId() { return this.timerId; }

  
  public WaitingRequest getWaitingRequest() { return this.waitTask; }

  
  public void setServiceIdState(ServiceIdState stateFlow) { this.stateFlow = stateFlow; }

  
  public void setServiceId(String serviceId) { this.serviceId = serviceId; }

  
  public void setMsisdn(String msisdn) { this.msisdn = msisdn; }

  
  public void setActiveEvents(ActiveEvents activeEvents) { this.activeEvents = activeEvents; }

  
  public void setEvent(String event) { this.event = event; }

  
  public void setLibraryInfo(LibraryInfo libraryInfo) { this.libraryInfo = libraryInfo; }

  
  public void setStateInfo(StateInfo stateInfo) { this.stateInfo = stateInfo; }

  
  public void setTimer(Timer timer) { this.timer = timer; }

  
  public void setTimerId(String timerId) { this.timerId = timerId; }

  
  public void setWaitingRequest(WaitingRequest waitTask) { this.waitTask = waitTask; }
}
