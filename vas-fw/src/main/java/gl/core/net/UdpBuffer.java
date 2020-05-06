package gl.core.net;

import java.net.DatagramPacket;
import java.util.Arrays;

public class UdpBuffer {
  public int receiveBufferSize = 2048;
  public byte[] buffer;
  public DatagramPacket packet = null;
  public int status = 0;
  public int startLength = 0;
  public int endLength = 0;
  public int len = 0;
  
  public int getLen() { return this.len; }
  
  public UdpBuffer(int receiveBufferSize) {
    this.receiveBufferSize = receiveBufferSize;
    this.buffer = new byte[receiveBufferSize];
    this.packet = new DatagramPacket(this.buffer, this.buffer.length);
  }
  
  public byte[] getBuffer() { return Arrays.copyOfRange(this.packet.getData(), 0, this.packet.getLength()); }
  
  public byte[] getBufferedData() {
    this.len = this.packet.getLength();
    return this.packet.getData();
  }
}
