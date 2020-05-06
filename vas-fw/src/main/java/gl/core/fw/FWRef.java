package gl.core.fw;

import gl.core.aig.AigThreadPool;
import gl.core.timer.Timer;
import gl.core.util.LogUtil;
import gl.core.util.McQueue;
import java.util.ArrayList;

public class FWRef extends LogUtil {
  McQueue timeOutQueue;
  
  McQueue mainQueue;
  
  WaitingRequest waitTask;
  
  Timer timer;
  
  StateFlow stateFlow;
  
  AigThreadPool aigThreadPool;
  
  ActiveEvents activeEvents;
  
  ArrayList<LibraryInfo> libraryList;
  
  ArrayList<StateFlow> flowList;
}
