package gl.core.fw;

import gl.core.util.LogUtil;
import java.util.ArrayList;

public class RemoveOldLibrary
  extends LogUtil
  implements Runnable {
  private long libCheckId = -1L;
  private ActiveEvents activeEvents = null;
  private ArrayList libraryList = null;
  public RemoveOldLibrary(long libCheckId, ActiveEvents activeEvents, ArrayList libraryList) {
    this.libCheckId = libCheckId;
    this.activeEvents = activeEvents;
    this.libraryList = libraryList;
  }
  public void run() {
    while (true) {
      try {
        boolean libActive = false;
        Thread.sleep(1000L);
        ArrayList<EventInfo> activeEventList = (ArrayList<EventInfo>)this.activeEvents.getActiveEventList();
        if (activeEventList.size() <= 0) {
          printLog(5, "No more active event for Library id = " + this.libCheckId);
          removeLibrary();
          break;
        } 
        printLog(5, "Remove old library , current active events states =" + activeEventList.size());
        for (int index = 0; index < activeEventList.size(); index++) {
          EventInfo eventInfo = activeEventList.get(index);
          if (eventInfo.getLibraryInfo().getLibId() == this.libCheckId) {
            libActive = true;
            printLog(5, "Can't remove old library , already used in active events");
            break;
          } 
        } 
        if (!libActive) {
          removeLibrary();
        }
      }
      catch (Exception exp) {
        exp.printStackTrace();
      } 
    } 
  }
  public boolean removeLibrary() throws Exception {
    int removeLoc = -1;
    for (int loc = 0; loc < this.libraryList.size(); loc++) {
      LibraryInfo libraryInfo = (LibraryInfo) this.libraryList.get(loc);
      if (libraryInfo.getLibId() == this.libCheckId) {
        removeLoc = loc;
        break;
      } 
    } 
    if (removeLoc > 0) {
      printLog(5, "Old LibraryInfo found at loc=" + removeLoc + " for libCheckId=" + this.libCheckId + ",going to remove");
      this.libraryList.remove(removeLoc);
      return true;
    } 
    printLog(5, "Old LibraryInfo not found at CheckId=" + this.libCheckId);
    return false;
  }
}
