package gl.core.fw;

public class StateInfo {
  private String serviceId = null;
  private int seqNumber = -1;
  private String currentId = null;
  private String eventId = null;
  private String actionId = null;
  private String nextStateId = null;
  private String eventMsg = null;
  private int timeout = 0;
  public StateInfo(String serviceId, int seqNumber, String currentId, String eventId, String actionId, String nextStateId, int timeout, String eventMsg) {
    this.serviceId = serviceId;
    this.seqNumber = seqNumber;
    this.currentId = currentId;
    this.eventId = eventId;
    this.actionId = actionId;
    this.nextStateId = nextStateId;
    this.timeout = timeout;
    this.eventMsg = eventMsg;
  }
  
  public String getServiceId() { return this.serviceId; }

  
  public String getEventId() { return this.eventId; }

  
  public String getCurrentId() { return this.currentId; }

  
  public String getNextStateId() { return this.nextStateId; }

  
  public String getActionId() { return this.actionId; }

  
  public int getTimeOut() { return this.timeout; }

  
  public String getEventMsg() { return this.eventMsg; }

  
  public int getSeqNumber() { return this.seqNumber; }

  
  public void setEventMsg(String eventMsg) { this.eventMsg = eventMsg; }
}
