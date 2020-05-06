package gl.core.fw;

import gl.core.aig.AigThreadPool;
import gl.core.timer.Timer;
import gl.core.util.McQueue;
import java.util.ArrayList;

public class ProcessEvents extends FWRef {
  public ProcessEvents(McQueue mainQueue) {
    this.mainQueue = mainQueue;

    
    this.libraryList = new ArrayList<>();
    LibraryInfo libraryInfo = new LibraryInfo();
    this.libraryList.add(0, libraryInfo);

    
    this.flowList = new ArrayList<>();
    this.stateFlow = new StateFlow(libraryInfo);
    this.flowList.add(0, this.stateFlow);
    this.stateFlow.loadFlowFromDB(Config.db_conn, Config.db_user, Config.db_pass);

    
    this.aigThreadPool = new AigThreadPool();
    (new Thread((Runnable)this.aigThreadPool)).start();
    AdapterWorker worker = new AdapterWorker();
    this.aigThreadPool.initPool(Config.workerThread, worker);

    
    this.timeOutQueue = new McQueue();
    this.timer = new Timer(this.timeOutQueue, 2500);

    
    HandleTimeoutEvent handleTimeOut = new HandleTimeoutEvent(this.timeOutQueue, this.aigThreadPool, mainQueue);
    (new Thread(handleTimeOut)).start();

    
    this.activeEvents = new ActiveEvents();





    
    this.waitTask = new WaitingRequest(this.aigThreadPool);
  }
  
  public LibraryInfo getLibrary() { return this.libraryList.get(0); }

  
  public StateFlow getStateFlow() { return this.flowList.get(0); }
  
  public void reloadStateFlow(String serviceId) throws Exception {
    StateFlow stateFlow = getStateFlow();
    stateFlow.reloadStateFlow(serviceId);
  }
  public void reloadStateFlow() throws Exception {
    StateFlow stateFlow = new StateFlow(getLibrary());
    stateFlow.loadFlowFromDB(Config.db_conn, Config.db_user, Config.db_pass);
    this.flowList.add(0, stateFlow);
  }
  public void reloadLibrary() throws Exception {
    ArrayList<String> serviceIdList = (ArrayList)this.stateFlow.getListOfServiceId();
    for (int index = 0; index < serviceIdList.size(); index++) {
      String serviceId = serviceIdList.get(index);
      printLog(0, "Reload Library for serviceid =" + serviceId);
      ServiceIdState serviceIdState = this.stateFlow.getServiceIdState(serviceId);
      ArrayList<StateInfo> flows = (ArrayList<StateInfo>)serviceIdState.getStateFlowList();
      LibraryInfo oldLib = getLibrary();
      LibraryInfo libraryInfo = new LibraryInfo();
      for (int loc = 0; loc < flows.size(); loc++) {
        StateInfo state = flows.get(loc);
        printLog(0, "Reload Library for serviceid =" + serviceId + " ,action id=" + state.getActionId());
        libraryInfo.loadMethod(state.getActionId());
      } 
      this.libraryList.add(0, libraryInfo);
      RemoveOldLibrary removeOld = new RemoveOldLibrary(oldLib.getLibId(), this.activeEvents, this.libraryList);
      (new Thread(removeOld)).start();
    } 
  }
}
