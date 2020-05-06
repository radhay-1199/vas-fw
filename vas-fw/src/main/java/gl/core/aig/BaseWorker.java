package gl.core.aig;

interface BaseWorker {
  void setThreadIndex(int paramInt);
  
  int getThreadIndex();
  
  void setObject(Object paramObject);
  
  Object getObject();
  
  void awake();
  
  boolean getActiveState();
  
  void setActiveState(boolean paramBoolean);
  
  void resetState();
  
  void process(Object paramObject);
}
