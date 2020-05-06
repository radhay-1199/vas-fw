package gl.core.aig;





class SubIDInfo
{
  private String id = null;
  private int sentCount = 0;
  private int tps = 0;
  private boolean status = true;
  private Object config = null;





  
  public SubIDInfo(String subid, int tps, Object config) {
    this.id = subid;
    this.tps = tps;
    this.config = config;
  }




  
  public void addTps(int tps) { this.tps += tps; }





  
  public boolean getStatus() { return this.status; }





  
  public Integer getTps() { return Integer.valueOf(this.tps); }





  
  public void setSentCount(int value) { this.sentCount = value; }




  
  public void updateSentCounter() { this.sentCount++; }





  
  public Object getConfig() { return this.config; }




  
  public int getSentCount() { return this.sentCount; }





  
  public String getId() { return this.id; }
}
