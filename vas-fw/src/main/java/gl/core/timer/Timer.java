package gl.core.timer;

import gl.core.util.LogUtil;
import gl.core.util.McQueue;
import java.util.concurrent.BlockingQueue;




public class Timer
  extends LogUtil
{
  private TimeoutHandler timeoutHandler = null;
  private TimerDataList timerData = null;
  
  private KeyManager keyManager = null;
  public McQueue expiredQueue = null;
  public McQueue respQueue = null;
  public BlockingQueue<Object> respBlockQueue = null;
  public String respIp = null;
  public int respPort = 0;


  
  private Timer() {
    this.expiredQueue = new McQueue();
    this.timerData = new TimerDataList(this.expiredQueue, -1);
    (new Thread(this.timerData)).start();
    this.keyManager = new KeyManager();
  }


  
  private Timer(int expireTps) {
    this.expiredQueue = new McQueue();
    this.timerData = new TimerDataList(this.expiredQueue, expireTps);
    (new Thread(this.timerData)).start();
    this.keyManager = new KeyManager();
  }



  
  public Timer(String ip, int port, int expireTps) {
    this(expireTps);
    this.respIp = ip;
    this.respPort = port;
    this.timeoutHandler = new TimeoutHandler(this.expiredQueue);
    (new Thread(this.timeoutHandler)).start();
  }



  
  public Timer(BlockingQueue<Object> respqueue) {
    this();
    this.respBlockQueue = respqueue;
    this.timeoutHandler = new TimeoutHandler(this.expiredQueue, this.respBlockQueue);
    (new Thread(this.timeoutHandler)).start();
  }



  
  public Timer(McQueue respqueue, int expireTps) {
    this(expireTps);
    this.respQueue = respqueue;
    this.timeoutHandler = new TimeoutHandler(this.expiredQueue, this.respQueue);
    (new Thread(this.timeoutHandler)).start();
  }




  
  public Timer(McQueue respqueue) { this(respqueue, -1); }














  
  public String add(Object object, int delay) throws Exception {
    Data data = new Data(object, delay);
    String key = "" + this.keyManager.getKey();
    data.setKey(key);
    this.timerData.add(key, delay, data);
    return key;
  }










  
  public String add(String key, String strData, int delay, String ip, int port) throws Exception {
    if (ip == null) {
      printLog(3, "Error invalid ip");
      return null;
    } 
    if (port < 100 || port > 99999) {
      printLog(3, "Error invalid port");
      return null;
    } 
    Data data = new Data(strData, delay, ip, port);
    if (key == null)
      key = "" + this.keyManager.getKey(); 
    data.setKey(key);
    this.timerData.add(key, delay, data);
    return key;
  }









  
  public String add(String key, byte[] bdata, int delay, String ip, int port) throws Exception {
    if (ip == null) {
      printLog(3, "Error invalid ip");
      return null;
    } 
    if (port < 100 || port > 99999) {
      printLog(3, "Error invalid port");
      return null;
    } 
    Data data = new Data(bdata, delay, ip, port);
    if (key == null)
      key = "" + this.keyManager.getKey(); 
    data.setKey(key);
    this.timerData.add(key, delay, data);
    return key;
  }








  
  public String add(String key, Object strData, int delay) throws Exception {
    Data data = new Data(strData, delay);
    if (key == null)
      key = "" + this.keyManager.getKey(); 
    data.setKey(key);
    this.timerData.add(key, delay, data);
    return key;
  }









  
  public String add(String key, String strData, int delay) throws Exception {
    Data data = new Data(strData, delay);
    if (key == null)
      key = "" + this.keyManager.getKey(); 
    data.setKey(key);
    this.timerData.add(key, delay, data);
    return key;
  }

  
  public boolean reset(String key, int delay) throws Exception { return this.timerData.reset(key, delay); }








  
  public String add(String strData, int delay, String ip, int port) throws Exception {
    if (ip == null) {
      printLog(3, "Error invalid ip");
      return null;
    } 
    if (port < 100 || port > 99999) {
      printLog(3, "Error invalid port");
      return null;
    } 
    Data data = new Data(strData, delay, ip, port);
    String key = "" + this.keyManager.getKey();
    data.setKey(key);
    this.timerData.add(key, delay, data);
    return key;
  }



  
  public Object get(String key) throws Exception {
    if (key == null) {
      printLog(5, "error invalid key value=" + key);
      return Boolean.valueOf(false);
    } 
    printLog(5, "get timer event for key=" + key);
    Data data = this.timerData.get(key);
    if (data == null) {
      return null;
    }
    return data.getObject();
  }



  
  public Object remove(String key) throws Exception {
    if (key == null) {
      printLog(5, "error invalid key value=" + key);
      return Boolean.valueOf(false);
    } 
    
    Data data = this.timerData.remove(key);
    if (data == null) {
      return null;
    }
    return data.getObject();
  }




  
  public Object remove(Data data) throws Exception {
    String key = data.getKey();
    return remove(key);
  }
}
