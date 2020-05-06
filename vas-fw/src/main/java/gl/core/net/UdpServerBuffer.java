package gl.core.net;

import gl.core.util.McQueue;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class UdpServerBuffer
  implements Runnable
{
  private DatagramSocket serverSocket;
  private DatagramPacket recivePacket;
  private byte[] buffer = new byte[4096];
  private McQueue queue;
  private int port;
  private UdpBuffer[] arrayOfUdpBuffer;
  private int udpBufferArraySize = 2048;
  private int checkUdpBufferArraySize = 2047;
  private int receiveBufferSize = 2048;
  private int checkCounter; private int lastUsed;
  private UdpServerBuffer() { this.checkCounter = 0;
    this.lastUsed = 0; } public UdpServerBuffer(int port, McQueue queue, int udpBufferArraySize, int receiveBufferSize) { this.checkCounter = 0; this.lastUsed = 0;

    
    this.queue = queue;
    this.port = port;
    this.udpBufferArraySize = udpBufferArraySize;
    this.receiveBufferSize = receiveBufferSize;
    this.checkUdpBufferArraySize = udpBufferArraySize - 1;
    createsocket(); }



  
  public UdpServerBuffer(int port, McQueue queue) {
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
        this.serverSocket.setReceiveBufferSize(25000000);
        this.recivePacket = new DatagramPacket(this.buffer, this.buffer.length);
      
      }
    
    }
    catch (Exception e) {
      e.printStackTrace();
      try {
        Thread.sleep(5000L);
      } catch (Exception exception) {}
    } 
  }
  
  public void run() {
    String msg = null;
    this.arrayOfUdpBuffer = new UdpBuffer[this.udpBufferArraySize];
    
    for (int loc = 0; loc < this.udpBufferArraySize; loc++) {
      this.arrayOfUdpBuffer[loc] = new UdpBuffer(this.receiveBufferSize);
    }
    
    while (true) {
      try {
        getAvailableIndex();
        if (this.lastUsed == -1) {
          
          UdpBuffer udpBuffer = new UdpBuffer(this.receiveBufferSize);
          this.serverSocket.receive(udpBuffer.packet);
          this.queue.push(udpBuffer);
          this.lastUsed = 0;
          continue;
        } 
        (this.arrayOfUdpBuffer[this.lastUsed]).packet.setLength((this.arrayOfUdpBuffer[this.lastUsed]).receiveBufferSize);
        this.serverSocket.receive((this.arrayOfUdpBuffer[this.lastUsed]).packet);
        (this.arrayOfUdpBuffer[this.lastUsed]).status = 1;
        this.queue.push(this.arrayOfUdpBuffer[this.lastUsed]);
        this.lastUsed++;
      
      }
      catch (Exception e) {
        e.printStackTrace();
        createsocket();
      } 
    } 
  }
  
  public void getAvailableIndex() throws Exception {
    while ((this.arrayOfUdpBuffer[this.lastUsed]).status != 0) {
      this.lastUsed++;
    }
    if (this.lastUsed >= this.checkUdpBufferArraySize)
      this.lastUsed = -1; 
  }
}
