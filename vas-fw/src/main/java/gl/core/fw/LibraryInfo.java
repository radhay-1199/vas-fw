package gl.core.fw;

import gl.core.oldfw.DumpLib;
import java.io.File;
import java.lang.reflect.Method;
import java.net.URL;
import java.net.URLClassLoader;
import java.util.concurrent.ConcurrentHashMap;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;




public class LibraryInfo
{
  private Logger log = LogManager.getRootLogger();
  private long libId = 0L;
  private ConcurrentHashMap<String, MethodDetails> libMap = null;
  private String currentPath = null;
  private DumpLib dumpLib = null;
  private ConcurrentHashMap<String, Class> loadedClassMap = null;
  private ConcurrentHashMap<String, Object> loadedClassObj = null;

  
  public LibraryInfo() {
    this.libMap = new ConcurrentHashMap<>();
    this.loadedClassObj = new ConcurrentHashMap<>();
    this.loadedClassMap = new ConcurrentHashMap<>();
    
    this.currentPath = System.getProperty("user.dir");
    this.log.debug("currentPath=" + this.currentPath);
    this.libId = System.currentTimeMillis();
    this.dumpLib = new DumpLib();
  }

  
  public void loadLibrary(String className, String methodName) throws Exception {
    MethodDetails methodRef = this.libMap.get(className + "." + methodName);
    if (methodRef == null) {
      loadMethod(className, methodName);
    }
  }
  
  public void loadMethod(String actionId) throws Exception {
    System.out.println("actionId" + actionId);
    if (actionId == null) {
      System.out.println("Invalid Method  name");


      
      return;
    } 


    
    if (actionId.indexOf(".") != -1) {
      String className = actionId.substring(0, actionId.lastIndexOf("."));
      String methodName = actionId.substring(actionId.lastIndexOf(".") + 1);
      loadMethod(className, methodName);
    } else {
      
      System.out.println("Invalid Method  name=" + actionId);
    } 
  }
  
  public URLClassLoader getClassLoader() {
    URLClassLoader classLoader = null;
    try {
      File folder = new File(this.currentPath);
      File[] listOfFiles = folder.listFiles();
      
      for (int i = 0; i < listOfFiles.length; i++)
      {
        if (listOfFiles[i].isFile() && listOfFiles[i].getName().endsWith(".jar") && !listOfFiles[i].getName().equals("fw.jar")) {
          System.out.println("Jar File " + listOfFiles[i].getName() + "|full =" + listOfFiles[i].getAbsolutePath());
          
          classLoader = new URLClassLoader(new URL[] { new URL("file:" + listOfFiles[i].getAbsolutePath()) }, getClass().getClassLoader());
        }
      
      }
    
    }
    catch (Exception exp) {
      exp.printStackTrace();
    } 
    return classLoader;
  }

  
  public void loadMethod(String className, String methodName) throws Exception {
    try {
      this.log.fatal("Load Method :Class=" + className + ",methodName=" + methodName);
      
      Class<?> classRef = this.loadedClassMap.get(className);
      
      if (classRef == null) {
        
        URLClassLoader classLoader = getClassLoader();
        classRef = Class.forName(className, true, classLoader);








        
        if (classRef == null) {
          this.log.fatal("ERROR|Unable to load Class=" + className);
          
          return;
        } 
        this.loadedClassMap.put(className, classRef);
        
        for (Method method : classRef.getDeclaredMethods()) {
          String logs = "Class=" + classRef.getName() + ",Method=" + method.getName() + ",Param=";
          for (Class<?> type : method.getParameterTypes()) {
            logs = logs + "," + type.getName();
          }
          this.log.info("SECOND" + logs);
        } 
      } 


      
      Class[] cArgNew = { String.class, String.class, StateInfo.class };
      
      Method method = getMethod(classRef, methodName, cArgNew);
      
      MethodDetails methodDetails = null;
      
      if (method == null) {
        Class[] cArgOld = { String.class, String.class, DumpLib.class };
        
        method = getMethod(classRef, methodName, cArgOld);
      } 
      
      if (method != null) {
        Object classObj = this.loadedClassObj.get(className);
        if (classObj == null) {

          
          classObj = classRef.newInstance();
          
          this.loadedClassObj.put(className, classObj);
        } 
        
        methodDetails = new MethodDetails(classObj, methodName, method);
        this.libMap.put(className + "." + methodName, methodDetails);
        this.log.fatal("Load Method :Class=" + className + ",methodName=" + methodName + ", Loaded SuccessFully");
      } else {
        
        this.log.fatal("ERROR::-Method=" + methodName + " not found in Class =" + className);
      } 
    } catch (ClassNotFoundException notFound) {
      notFound.printStackTrace();
      this.log.fatal("Class file unavailable::" + notFound.getMessage());
      System.out.println("Class file unavailable::" + notFound.getMessage());
    } 
  }

  
  private Method getMethod(Class classRef, String methodName, Class[] cArg) {
    Method method = null;
    try {
      method = classRef.getDeclaredMethod(methodName, cArg);

























    
    }
    catch (Exception insException) {
      insException.printStackTrace();
      System.out.println("Exception in getting method" + cArg[0] + " " + cArg[1] + " " + cArg[2]);
      return null;
    } 
    return method;
  }












  
  public synchronized String execute(String msisdn, String event, StateInfo stateInfo) throws Exception {
    String status = "success";
    
    MethodDetails methodDetails = this.libMap.get(stateInfo.getActionId());
    Class[] parameterTypes = null;
    
    if (methodDetails == null) {
      return "method not avaliable for serviceid=" + stateInfo.getServiceId() + ",actionId=" + stateInfo
        .getActionId();
    }
    
    parameterTypes = methodDetails.getMethodRef().getParameterTypes();
    
    if (parameterTypes.length == 3 && parameterTypes[2].getName().equalsIgnoreCase("gl.core.fw.StateInfo")) {
      methodDetails.getMethodRef().invoke(methodDetails.getClassRef(), new Object[] { msisdn, event, stateInfo });
      return status;
    } 
    
    if (parameterTypes.length == 3 && parameterTypes[2]
      .getName().equalsIgnoreCase("gl.core.oldfw.DumpLib")) {
      methodDetails.getMethodRef().invoke(methodDetails.getClassRef(), new Object[] { msisdn, event, this.dumpLib });
    }
    
    return status;
  }

  
  public long getLibId() { return this.libId; }
}
