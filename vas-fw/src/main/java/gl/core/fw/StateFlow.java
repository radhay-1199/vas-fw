package gl.core.fw;

import gl.core.db.DataSource;
import gl.core.util.LogUtil;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;

public class StateFlow
  extends LogUtil {
  private LibraryInfo libraryInfo = null;
  private ConcurrentHashMap<String, ServiceIdState> serviceIdMap = null;
  public StateFlow(LibraryInfo libraryInfo) {
    this.libraryInfo = libraryInfo;
    this.serviceIdMap = new ConcurrentHashMap<>();
  }


  
  public void loadFlowFromDB(String serviceId, String db_conn, String db_user, String db_pass) {
    try {
      Connection connection = DataSource.getConnection();
      Statement stmt = connection.createStatement();
      String query = "select serviceid , sequencenumber , currentstateid , eventid , actionid , nextstate , timeout from state_info where serviceid ='" + serviceId + "'";
      ResultSet rs = stmt.executeQuery(query);
      while (rs.next()) {
        
        StateInfo stateInfo = new StateInfo(rs.getString("serviceid"), rs.getInt("sequencenumber"), rs.getString("currentstateid"), rs.getString("eventid"), rs.getString("actionid"), rs.getString("nextstate"), rs.getInt("timeout"), getEventMessage(connection, rs.getString("serviceid"), rs.getInt("sequencenumber")));
        
        printLog(4, "state added,serviceid=" + stateInfo.getServiceId() + ",seqNumber=" + stateInfo.getSeqNumber() + ",currentstateid=" + stateInfo.getCurrentId() + ",eventid=" + stateInfo.getEventId() + ",actionid=" + stateInfo.getActionId() + ",nextstate=" + stateInfo.getNextStateId() + ",timeout=" + stateInfo.getTimeOut() + ",eventMsg=" + stateInfo.getEventMsg());
        
        System.out.println("state added,serviceid=" + stateInfo.getServiceId() + ",seqNumber=" + stateInfo.getSeqNumber() + ",currentstateid=" + stateInfo.getCurrentId() + ",eventid=" + stateInfo.getEventId() + ",actionid=" + stateInfo.getActionId() + ",nextstate=" + stateInfo.getNextStateId() + ",timeout=" + stateInfo.getTimeOut() + ",eventMsg=" + stateInfo.getEventMsg());
        
        this.libraryInfo.loadMethod(stateInfo.getActionId());
        addState(stateInfo);
      } 
      rs.close();
      stmt.close();
      connection.close();
    }
    catch (Exception exp) {
      exp.printStackTrace();
    } 
  }
  
  public void loadFlowFromDB(String db_conn, String db_user, String db_pass) {
    try {
      Connection connection = DataSource.getConnection();
      
      Statement stmt = connection.createStatement();
      String query = "select serviceid , sequencenumber , currentstateid , eventid , actionid , nextstate , timeout from state_info";
      ResultSet rs = stmt.executeQuery(query);
      while (rs.next()) {
        
        String eventId = rs.getString("eventid");
        if (eventId == null) {
          eventId = "Start";
        }
        StateInfo stateInfo = new StateInfo(rs.getString("serviceid"), rs.getInt("sequencenumber"), rs.getString("currentstateid"), eventId, rs.getString("actionid"), rs.getString("nextstate"), rs.getInt("timeout"), getEventMessage(connection, rs.getString("serviceid"), rs.getInt("sequencenumber")));
        printLog(4, "state added,serviceid=" + stateInfo.getServiceId() + ",seqNumber=" + stateInfo.getSeqNumber() + ",currentstateid=" + stateInfo.getCurrentId() + ",eventid=" + stateInfo.getEventId() + ",actionid=" + stateInfo.getActionId() + ",nextstate=" + stateInfo.getNextStateId() + ",timeout=" + stateInfo.getTimeOut() + ",eventMsg=" + stateInfo.getEventMsg());
        System.out.println("state added,serviceid=" + stateInfo.getServiceId() + ",seqNumber=" + stateInfo.getSeqNumber() + ",currentstateid=" + stateInfo.getCurrentId() + ",eventid=" + stateInfo.getEventId() + ",actionid=" + stateInfo.getActionId() + ",nextstate=" + stateInfo.getNextStateId() + ",timeout=" + stateInfo.getTimeOut() + ",eventMsg=" + stateInfo.getEventMsg());
        this.libraryInfo.loadMethod(stateInfo.getActionId());
        addState(stateInfo);
      } 
      
      rs.close();
      stmt.close();
      connection.close();
    }
    catch (Exception exp) {
      exp.printStackTrace();
    } 
  }
  public void addState(StateInfo stateInfo) throws Exception {
    ServiceIdState serivceIdState = this.serviceIdMap.get(stateInfo.getServiceId());
    if (serivceIdState == null) {
      serivceIdState = new ServiceIdState(stateInfo.getServiceId());
      this.serviceIdMap.put(stateInfo.getServiceId(), serivceIdState);
    } 
    serivceIdState.addState(stateInfo);
  }
  
  public String getEventMessage(Connection connection, String serviceId, int seqNumber) throws Exception {
    String eventMsg = null;
    Statement stmt = connection.createStatement();
    String query = "select parametervalue from action_relation where serviceid='" + serviceId + "' and sequencenumber=" + seqNumber;
    ResultSet rs = stmt.executeQuery(query);
    while (rs.next()) {
      eventMsg = rs.getString("parametervalue");
    }
    rs.close();
    stmt.close();
    return eventMsg;
  }
  
  public LibraryInfo getLibrary() { return this.libraryInfo; }

  
  public ServiceIdState getServiceIdState(String serviceId) { return this.serviceIdMap.get(serviceId); }
  
  public List getListOfServiceId() throws Exception {
    List list = new ArrayList(this.serviceIdMap.keySet());
    return list;
  }


  
  public void reloadStateFlow(String serviceId) throws Exception {}

  
  public void getFlowFromDB(String serviceId) {
    try {
      Connection connection = DataSource.getConnection();
      
      Statement stmt = connection.createStatement();
      String query = "select serviceid , sequencenumber , currentstateid , eventid , actionid , nextstate , timeout from state_info where serviceid ='" + serviceId + "'";
      ResultSet rs = stmt.executeQuery(query);
      while (rs.next()) {
        StateInfo stateInfo = new StateInfo(rs.getString("serviceid"), rs.getInt("sequencenumber"), rs.getString("currentstateid"), rs.getString("eventid"), rs.getString("actionid"), rs.getString("nextstate"), rs.getInt("timeout"), getEventMessage(connection, rs.getString("serviceid"), rs.getInt("sequencenumber")));
        printLog(4, "state added,serviceid=" + stateInfo.getServiceId() + ",seqNumber=" + stateInfo.getSeqNumber() + ",currentstateid=" + stateInfo.getCurrentId() + ",eventid=" + stateInfo.getEventId() + ",actionid=" + stateInfo.getActionId() + ",nextstate=" + stateInfo.getNextStateId() + ",timeout=" + stateInfo.getTimeOut() + ",eventMsg=" + stateInfo.getEventMsg());
        this.libraryInfo.loadMethod(stateInfo.getActionId());
        addState(stateInfo);
      } 
      rs.close();
      stmt.close();
      connection.close();
    }
    catch (Exception exp) {
      exp.printStackTrace();
    } 
  }
}
