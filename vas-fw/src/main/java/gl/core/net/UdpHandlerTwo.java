package gl.core.net;

import gl.core.util.McQueue;
import java.net.DatagramSocket;

public class UdpHandlerTwo implements Runnable {
  private UdpBuffer[] arrayOfUdpBuffer;
  private McQueue queue;
  private int checkUdpBufferArraySize = 2047;
  private int receiveBufferSize = 2048;
  private DatagramSocket serverSocket;
  private int lastUsed = 0;

  
  public UdpHandlerTwo(McQueue queue, UdpBuffer[] arrayOfUdpBuffer, int receiveBufferSize, int checkUdpBufferArraySize, DatagramSocket serverSocket) {
    this.arrayOfUdpBuffer = arrayOfUdpBuffer;
    this.receiveBufferSize = receiveBufferSize;
    this.checkUdpBufferArraySize = checkUdpBufferArraySize;
    this.serverSocket = serverSocket;
    this.queue = queue;
  }
  
  public void run() {
    System.out.println("starting UdpHandlerTwo ");
    while (true) {
      try {
        getAvailableIndex();
        System.out.println("lastUsed = " + this.lastUsed);
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
