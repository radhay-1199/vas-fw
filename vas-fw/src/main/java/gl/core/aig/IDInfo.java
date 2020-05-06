package gl.core.aig;

import gl.core.util.LogUtil;
import java.util.ArrayList;




class IDInfo
  extends LogUtil
{
  private String id = null;
  private int tps = 0;
  private int factor = 0;
  private int nextIndexForUse = 0;
  private int totolSub = 0;
  private ArrayList<SubIDInfo> subidList = null;



  
  public IDInfo(String id) {
    this.id = id;
    this.subidList = new ArrayList<>();
  }





  
  public void addSubID(String subid, int tps, Object config) {
    try {
      SubIDInfo subId = new SubIDInfo(subid, tps, config);
      this.subidList.add(subId);
      this.tps += tps;
      resetFactor();
      printLog(4, "add new subid for id=" + this.id + ",subid=" + subid + ",tps=" + tps + ",totolSub=" + this.totolSub);
    }
    catch (Exception exception) {}
  }
  
  private void resetFactor() {
    try {
      int[] subList = new int[this.subidList.size()];
      for (int index = 0; index < this.subidList.size(); index++) {
        subList[index] = ((SubIDInfo)this.subidList.get(index)).getTps().intValue();
      }
      if (subList.length > 1) {
        this.factor = getProportion(subList);
      } else {
        this.factor = subList[0];
      }  printLog(5, "get getProportion of id=" + this.id + ",tps list=" + subList + ",factor=" + this.factor);
      this.totolSub = this.subidList.size();
    }
    catch (Exception exception) {}
  }
  
  private int gcd(int a, int b) {
    while (b > 0) {
      int temp = b;
      b = a % b;
      a = temp;
    } 
    return a;
  }
  private int getProportion(int[] input) {
    int result = input[0];
    for (int i = 1; i < input.length; i++)
      result = gcd(result, input[i]); 
    return result;
  }



  
  public SubIDInfo getAvailID() throws Exception {
    int counter = 0;
    if (this.nextIndexForUse >= this.totolSub) {
      this.nextIndexForUse = 0;
    }
    while (!((SubIDInfo)this.subidList.get(this.nextIndexForUse)).getStatus()) {
      this.nextIndexForUse++;
      counter++;
      if (this.nextIndexForUse == this.totolSub) {
        this.nextIndexForUse = 0;
      }
      if (counter == this.totolSub) {
        printLog(4, this.id + " - no connection available");
        return null;
      } 
    } 
    SubIDInfo subId = this.subidList.get(this.nextIndexForUse);
    if (subId.getSentCount() > subId.getTps().intValue() / this.factor) {
      subId.setSentCount(0);
      this.nextIndexForUse++;
      subId = getAvailID();
    } 
    subId.updateSentCounter();
    printLog(5, "IndexDetail =  id=" + this.id + ",totalSub=" + this.totolSub + ",nextIndexForUse=" + this.nextIndexForUse + ",factor=" + this.factor + ",sentCounter=" + subId.getSentCount() + ",subId=" + subId.getId() + ",subIdTps=" + subId.getTps());
    return subId;
  }




  
  public int getTps() { return this.tps; }





  
  public void addTps(String subid, int tps) {
    for (int index = 0; index < this.subidList.size(); index++) {
      SubIDInfo subId = this.subidList.get(index);
      if (subId.getId().equals(subid)) {
        subId.addTps(tps);
        resetFactor();
        break;
      } 
    } 
  }



  
  public void removeId(String subid) {
    for (int index = 0; index < this.subidList.size(); index++) {
      SubIDInfo subId = this.subidList.get(index);
      if (subId.getId().equals(subid)) {
        this.subidList.remove(index);
        resetFactor();
        break;
      } 
    } 
  }
}
