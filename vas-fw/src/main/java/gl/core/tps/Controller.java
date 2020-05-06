package gl.core.tps;






public class Controller
{
  private int tps = 0;
  private int slotValue = 0;
  private int breakCount = 0;
  private int baseSlotValue = 0;
  private int totalTpsCounter = 0;
  
  private long expectEndtime = 0L;
  private long sleep = 0L;
  private long endtime = 0L;
  private int modulovalue = 0;
  int countter = 0;



  
  public Controller(int tps) {
    if (tps <= 0) {
      System.out.println("Total TPS can not be less than or equals to zero");
      return;
    } 
    this.tps = tps;
  }


  
  public void updateCounter() {
    try {
      if (this.tps < 1)
        return;  sentCounter();
      if (this.totalTpsCounter <= 0) {
        checkWait();
        resetTps();
      }
      else if (this.slotValue <= 0) {
        checkWait();
        this.breakCount--;
        this.slotValue = this.baseSlotValue;
      }
    
    } catch (Exception exp) {
      exp.printStackTrace();
    } 
  }
  private void sentCounter() {
    this.totalTpsCounter--;
    this.slotValue--;
  }
  public void resetTps(int tps) {
    if (tps < 1) {
      return;
    }
    this.tps = tps;
    resetTps();
  }
  private void resetTps() {
    if (this.tps < 1)
      return; 
    this.totalTpsCounter = this.tps;
    this.baseSlotValue = getBreakCounts(this.tps);
    this.slotValue = this.baseSlotValue;
    this.breakCount = this.tps / this.baseSlotValue;
    this.modulovalue = this.tps % this.breakCount;
    this.slotValue += this.modulovalue;
    this.expectEndtime = System.currentTimeMillis() + 1000L;
    this.countter = 1;
  }
  private int getBreakCounts(int tps) {
    int countPerSlot = 1;
    if (tps > 10) {
      countPerSlot = tps / 10;
    } else {
      countPerSlot = tps / 3;
    } 
    if (countPerSlot <= 0)
      countPerSlot = 1; 
    return countPerSlot;
  }
  private void checkWait() {
    try {
      this.endtime = System.currentTimeMillis();
      this.sleep = this.expectEndtime - this.endtime;
      this.sleep /= this.breakCount;
      if (this.sleep > 0L) {
        Thread.sleep(this.sleep);
      }
    } catch (Exception exception) {}
  }



  
  private void setTps(int tps) { this.tps = tps; }
}
