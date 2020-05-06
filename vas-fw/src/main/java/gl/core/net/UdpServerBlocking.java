package gl.core.net;

import gl.core.util.LogUtil;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.concurrent.LinkedBlockingQueue;


public class UdpServerBlocking
  extends LogUtil
  implements Runnable
{
  private DatagramSocket serverSocket;
  private DatagramPacket recivePacket;
  private byte[] buffer = new byte[4096];
  private LinkedBlockingQueue queue;
  private int port;
  private UdpBuffer[] arrayOfUdpBuffer;
  private int udpBufferArraySize = 2048;
  private int checkUdpBufferArraySize = 2047;
  private int receiveBufferSize = 2048;
  private int checkCounter; private int lastUsed;
  private UdpServerBlocking() { this.checkCounter = 0;
    this.lastUsed = 0; } public UdpServerBlocking(int port, LinkedBlockingQueue queue, int udpBufferArraySize, int receiveBufferSize) { this.checkCounter = 0; this.lastUsed = 0;
    
    this.queue = queue;
    this.port = port;
    this.udpBufferArraySize = udpBufferArraySize;
    this.receiveBufferSize = receiveBufferSize;
    this.checkUdpBufferArraySize = udpBufferArraySize - 1;
    createsocket(); }



  
  public UdpServerBlocking(int port, LinkedBlockingQueue queue) {
    this.checkCounter = 0;
    this.lastUsed = 0;
    this.queue = queue;
    this.port = port;
    createsocket();
  }
  
  private void createsocket() {
    try {
      if (this.serverSocket == null || !this.serverSocket.isBound()) {
        this.serverSocket = new DatagramSocket(this.port);
        this.recivePacket = new DatagramPacket(this.buffer, this.buffer.length);
      }
    
    }
    catch (Exception e) {
      printLog(1, this.port + ":" + e.getMessage());
      try {
        Thread.sleep(5000L);
      } catch (Exception exception) {}
    } 
  }
  
  public void run() {
    String msg = null;
    this.arrayOfUdpBuffer = new UdpBuffer[this.udpBufferArraySize];
    System.out.println("Start initiating Udp Buffers , udpBufferArraySize=" + this.udpBufferArraySize);
    for (int loc = 0; loc < this.udpBufferArraySize; loc++) {
      this.arrayOfUdpBuffer[loc] = new UdpBuffer(this.receiveBufferSize);
    }
    System.out.println("End initiating Udp Buffers , udpBufferArraySize=" + this.udpBufferArraySize);
    while (true) {
      try {
        getAvailableIndex();
        if (this.lastUsed == -1) {
          UdpBuffer udpBuffer = new UdpBuffer(this.receiveBufferSize);
          this.serverSocket.receive(udpBuffer.packet);
          this.queue.put(udpBuffer);
          System.out.println("new Created lastUsed=" + this.lastUsed + ",udpBufferArraySize=" + this.udpBufferArraySize);
          this.lastUsed = 0;
          continue;
        } 
        (this.arrayOfUdpBuffer[this.lastUsed]).packet.setLength((this.arrayOfUdpBuffer[this.lastUsed]).receiveBufferSize);
        this.serverSocket.receive((this.arrayOfUdpBuffer[this.lastUsed]).packet);
        (this.arrayOfUdpBuffer[this.lastUsed]).status = 1;
        this.queue.put(this.arrayOfUdpBuffer[this.lastUsed]);
        this.lastUsed++;

      
      }
      catch (Exception e) {
        e.printStackTrace();
        createsocket();
      } 
    } 
  }
  public void getAvailableIndex() throws Exception {
    this.checkCounter = 0;
    while ((this.arrayOfUdpBuffer[this.lastUsed]).status != 0) {
      this.lastUsed++;
      this.checkCounter++;
      System.out.println("checkCounter=" + this.checkCounter);
      if (this.lastUsed == this.udpBufferArraySize) {
        this.lastUsed = 0;
      }
      if (this.checkCounter == this.udpBufferArraySize) {
        this.lastUsed = -1;
        break;
      } 
    } 
    if (this.lastUsed == this.checkUdpBufferArraySize)
      this.lastUsed = 0; 
  }
}
