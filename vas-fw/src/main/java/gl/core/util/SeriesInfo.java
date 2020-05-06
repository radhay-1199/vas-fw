package gl.core.util;

public class SeriesInfo {
  private String startRange = null;
  private String endRange = null;
  private String circle = null;
  private Object info = null;
  public SeriesInfo(String startRange, String endRange, String circle, Object info) {
    this.startRange = startRange;
    this.endRange = endRange;
    this.circle = circle;
    this.info = info;
  }
  
  public String getStartRange() { return this.startRange; }

  
  public String getEndRange() { return this.endRange; }

  
  public String getCircle() { return this.circle; }


  
  public Object getInfo() { return this.info; }
}
