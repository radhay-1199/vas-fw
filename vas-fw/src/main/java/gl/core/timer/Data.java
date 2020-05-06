package gl.core.timer;




public class Data
{
  private long origin;
  private long delay;
  private Object object;
  private byte[] byteObj = null;
  private boolean strOnUdp = false;
  private String respIp = null;
  private int respPort = -1;
  private String key = null;
  
  public Data(String key) {
    this.origin = System.currentTimeMillis();
    this.delay = 0L;
    this.object = null;
    this.key = key;
  }


  
  private Data() {}

  
  public Data(Object object, long delay) {
    this.origin = System.currentTimeMillis();
    this.object = object;
    this.delay = delay;
  }







  
  public Data(byte[] object, long delay, String ip, int port) {
    this.origin = System.currentTimeMillis();
    this.byteObj = object;
    this.delay = delay;
    this.respIp = ip;
    this.respPort = port;
    this.strOnUdp = true;
  }







  
  public Data(String object, long delay, String ip, int port) {
    this.origin = System.currentTimeMillis();
    this.object = object;
    this.delay = delay;
    this.respIp = ip;
    this.respPort = port;
    this.strOnUdp = true;
  }




  
  public int getRespPort() { return this.respPort; }





  
  public String getRespIp() { return this.respIp; }





  
  public boolean getRespOverUdpFlag() { return this.strOnUdp; }





  
  public Object getObject() { return this.object; }





  
  public boolean equals(Object object) {
    try {
      if (object == null || getClass() != object.getClass()) {
        return false;
      }
      if (this.key.equals(((Data)object).getKey())) {
        return true;
      }
      return false;
    }
    catch (Exception exp) {
      exp.printStackTrace();
      
      return false;
    } 
  }


  
  public String getKey() { return this.key; }





  
  public void setKey(String key) { this.key = key; }






  
  public byte[] getDataBytes() { return this.byteObj; }
}
