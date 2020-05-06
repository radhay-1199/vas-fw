package gl.core.timer;

import gl.core.util.LogUtil;
import gl.core.util.McQueue;
import gl.core.util.Utility;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.concurrent.BlockingQueue;

public class TimeoutHandler extends LogUtil implements Runnable {
  private McQueue queue = null;
  private McQueue respQueue = null;
  private BlockingQueue<Object> respBlockQueue = null;
  private Utility util = null;
  private DatagramSocket clientSocket = null;
  private void createSocket() {
    try {
      this.clientSocket = new DatagramSocket();
    }
    catch (Exception e) {
      e.printStackTrace();
    } 
  }
  
  private TimeoutHandler() throws Exception { createSocket(); }
  
  public TimeoutHandler(McQueue queue) {
    createSocket();
    this.util = new Utility();
    this.queue = queue;
  }




  
  public TimeoutHandler(McQueue queue, McQueue respQueue) {
    this(queue);
    createSocket();
    this.respQueue = respQueue;
  }




  
  public TimeoutHandler(McQueue queue, BlockingQueue<Object> respQueue) {
    this(queue);
    createSocket();
    this.respBlockQueue = respQueue;
  }


  
  public void run() {
    while (true) {
      try {
        Data data = (Data)this.queue.pull();
        if (data.getRespOverUdpFlag()) {
          sendToUdp(data); continue;
        } 
        if (this.respQueue != null) {
          sendToMcRespQueue(data); continue;
        } 
        if (this.respBlockQueue != null) {
          sendToRespBlockQueue(data);
          continue;
        } 
        printLog(5, "no response method defined, droping packet for key=" + data.getKey());
      }
      catch (Exception exp) {
        exp.printStackTrace();
      } 
    } 
  }
  private void sendToUdp(Data data) throws Exception {
    InetAddress IPAddress = InetAddress.getByName(data.getRespIp());
    DatagramPacket sendPacket = null;
    if (data.getDataBytes() != null) {
      sendPacket = new DatagramPacket(data.getDataBytes(), (data.getDataBytes()).length, IPAddress, data.getRespPort());
    } else {
      sendPacket = new DatagramPacket(((String)data.getObject()).getBytes(), ((String)data.getObject()).length(), IPAddress, data.getRespPort());
    } 
    System.out.println("sendPacket" + sendPacket + "clientSocket" + this.clientSocket);
    this.clientSocket.send(sendPacket);
  }
  
  private void sendToMcRespQueue(Data data) throws Exception { this.respQueue.push(data.getObject()); }

  
  private void sendToRespBlockQueue(Data data) throws Exception { this.respBlockQueue.add(data.getObject()); }
}