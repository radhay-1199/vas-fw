package gl.core.fw;

import java.lang.reflect.Method;

public class MethodDetails {
  private Object classInstance = null;
  private String methodName = null;
  private Method methodRef = null;
  public MethodDetails(Object classInstance, String methodName, Method methodRef) {
    this.classInstance = classInstance;
    this.methodName = methodName;
    this.methodRef = methodRef;
  }
  
  public Object getClassRef() { return this.classInstance; }

  
  public Method getMethodRef() { return this.methodRef; }

  
  public String getMethodName() { return this.methodName; }
}
