package gl.core.net;

import gl.core.util.LogUtil;
import gl.core.util.McQueue;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.Arrays;
import java.util.concurrent.BlockingQueue;

public class UdpTLVServer
  extends LogUtil
  implements Runnable
{
  private int udpPort = 0;
  private DatagramSocket serverSocket;
  private DatagramPacket recivePacket;
  McQueue queue = null;
  BlockingQueue bQueue = null;
  private int bufferSize = 2048;
  private int queueAlertSize = 100;
  
  public UdpTLVServer(int udpPort, McQueue main_queue) {
    this.udpPort = udpPort;
    this.queue = main_queue;
  }
  
  public UdpTLVServer(int udpPort, McQueue main_queue, int bufferSize) {
    this(udpPort, main_queue);
    this.bufferSize = bufferSize;
  }
  
  public UdpTLVServer(int udpPort, McQueue main_queue, int bufferSize, int queueAlertSize) {
    this(udpPort, main_queue, bufferSize);
    this.queueAlertSize = queueAlertSize;
  }
  
  public UdpTLVServer(int udpPort, BlockingQueue main_queue) {
    this.udpPort = udpPort;
    this.bQueue = main_queue;
  }


  
  public void createsocket() {
    try {
      this.serverSocket = new DatagramSocket(this.udpPort);
    } catch (Exception e) {
      e.printStackTrace();
      System.exit(1);
    } 
  }
  
  public void run() {
    try {
      createsocket();
      this.serverSocket.setSoTimeout(3000000);
      while (true) {
        try {
          UdpBuffer udpBuffer = new UdpBuffer(this.bufferSize);
          this.serverSocket.receive(udpBuffer.packet);
          
          if (this.queue != null) {
            this.queue.push(udpBuffer);
            if (this.queue.getLength() > this.queueAlertSize) {
              printLog("UdpTLVServer|Queue Size=" + this.queue.getLength());
            }
          } 
          if (this.bQueue != null) {
            this.bQueue.put(udpBuffer);
            if (this.bQueue.size() > this.queueAlertSize) {
              printLog("UdpTLVServer|Queue Size=" + this.bQueue.size());
            }
          } 
        } catch (Exception exception) {}
      }
    
    } catch (Exception exp) {
      exp.printStackTrace();
      return;
    } 
  }
  
  public byte[] getBuffer(DatagramPacket packet) { return Arrays.copyOfRange(packet.getData(), 0, packet.getLength()); }
}
