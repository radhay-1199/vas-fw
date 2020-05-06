package gl.core.aig;

import gl.core.util.LogUtil;
import java.util.concurrent.ConcurrentHashMap;



public class TpsConfig
  extends LogUtil
{
  private ConcurrentHashMap<String, IDInfo> idMap = null;



  
  public TpsConfig() { this.idMap = new ConcurrentHashMap<>(); }







  
  public void addId(String id, String subid, int tps, Object config) {
    try {
      IDInfo idInfo = this.idMap.get(id);
      if (idInfo == null) {
        idInfo = new IDInfo(id);
      }
      idInfo.addSubID(subid, tps, config);
      this.idMap.put(id, idInfo);
    }
    catch (Exception e) {
      e.printStackTrace();
    } 
  }




  
  public void removeId(String id, String subid) {
    try {
      IDInfo idInfo = this.idMap.get(id);
      if (idInfo != null) {
        idInfo.removeId(subid);
      }
      if (idInfo.getTps() <= 0) {
        this.idMap.remove(id);
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    } 
  }



  
  public void removeId(String id) {
    try {
      this.idMap.remove(id);
    }
    catch (Exception e) {
      e.printStackTrace();
    } 
  }




  
  public int getTps(String id) {
    int tps = -1;
    try {
      IDInfo idInfo = this.idMap.get(id);
      if (idInfo != null) {
        tps = idInfo.getTps();
        if (tps <= 0) {
          removeId(id);
        }
      }
    
    } catch (Exception e) {
      e.printStackTrace();
    } 
    return tps;
  }





  
  public void addTps(String id, String subid, int tps) {
    try {
      IDInfo idInfo = this.idMap.get(id);
      if (idInfo == null) {
        printLog(0, "Unregistered ID =" + id);
        return;
      } 
      idInfo.addTps(subid, tps);
    }
    catch (Exception e) {
      e.printStackTrace();
    } 
  }




  
  public SubIDInfo getAvailableSubId(String id) {
    try {
      IDInfo idInfo = this.idMap.get(id);
      if (idInfo != null) {
        return idInfo.getAvailID();
      }
    } catch (Exception e) {
      e.printStackTrace();
    } 
    printLog(0, "Unregistered ID =" + id);
    return null;
  }
}
