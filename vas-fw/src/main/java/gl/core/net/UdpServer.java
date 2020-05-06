package gl.core.net;

import gl.core.util.McQueue;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetSocketAddress;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;





public class UdpServer
  extends Thread
{
  private Logger log = LogManager.getRootLogger();
  private DatagramSocket serverSocket;
  private DatagramPacket recivePacket;
  private byte[] buffer = new byte[4096];


  
  private McQueue queue;


  
  private int port;


  
  private UdpServer() {}


  
  public UdpServer(int port, McQueue queue) {
    this.queue = queue;
    this.port = port;
    createsocket();
  }


  
  private void createsocket() {
    try {
      if (this.serverSocket == null || !this.serverSocket.isBound()) {
        this.serverSocket = new DatagramSocket(null);
        this.serverSocket.setReceiveBufferSize(10000000);
        this.recivePacket = new DatagramPacket(this.buffer, this.buffer.length);
        
        this.serverSocket.bind(new InetSocketAddress(this.port));
        this.log.info("Udp Buffer Size " + this.serverSocket.getReceiveBufferSize());
        System.out.println("Udp Buffer Size " + this.serverSocket.getReceiveBufferSize());
      } 
    } catch (Exception e) {
      this.log.error(this.port + ":" + e.getMessage());
      try {
        Thread.sleep(5000L);
      } catch (Exception exception) {}
    } 
  }








  
  public void run() {
    String msg = null;
    
    while (true) {
      try {
        this.serverSocket.receive(this.recivePacket);
        msg = new String(this.buffer, 0, this.recivePacket.getLength());
        this.log.info("UdpReciever|" + msg + ",Packet Length=" + this.recivePacket.getLength() + ",Queue Length|" + this.queue
            .getLength());
        if (this.queue != null) {
          this.queue.push(msg);
        } else {
          this.log.info("Queue not avaialble");
        } 
        this.recivePacket.setLength(this.buffer.length);
      }
      catch (Exception e) {
        createsocket();
      } 
    } 
  }
}
