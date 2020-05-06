package gl.core.fw;
import java.util.ArrayList;

public class WaitEventInfo {
  String serviceId = null;
  String msisdn = null;
  ArrayList<String> events = null;
  public WaitEventInfo(String serviceId, String msisdn, String event) {
    this.serviceId = serviceId;
    this.msisdn = msisdn;
    this.events = new ArrayList<>();
    this.events.add(event);
  }
  
  public String getServiceId() { return this.serviceId; }

  
  public String getMsisdn() { return this.msisdn; }

  
  public void addEvent(String event) { this.events.add(event); }
  
  public String getNextEvent(String serviceId, String msisdn) {
    if (this.events == null || this.events.size() == 0)
      return null; 
    return this.events.remove(0);
  }
}
