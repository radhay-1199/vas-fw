package gl.core.tps;






public class InboudController
  extends Thread
{
  private InboudTps inboudTps = null;
  private int tps = 0;



  
  public InboudController(int tps) {
    if (tps <= 0) {
      System.out.println("Total TPS can not be less than or equals to zero");
      return;
    } 
    this.inboudTps = new InboudTps(tps);
    this.inboudTps.start();
  }



  
  public boolean updateCounter() { return this.inboudTps.updateCounter(); }




  
  private void setTps(int tps) { this.inboudTps.setTps(tps); }
}
