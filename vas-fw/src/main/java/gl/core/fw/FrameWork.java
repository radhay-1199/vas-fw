package gl.core.fw;

import gl.core.net.UdpServer;
import gl.core.util.LogUtil;
import gl.core.util.McQueue;

public class FrameWork extends LogUtil {
  public FrameWork(String configFile) throws Exception {
    Config config = new Config(configFile);
    config.loadProperties();
    
    McQueue mainQueue = new McQueue();
    UdpServer udpserver = new UdpServer(Config.udp_port, mainQueue);
    udpserver.start();
    
    ProcessRequest processReq = new ProcessRequest(mainQueue, Config.inputType);
    Thread thread = new Thread(processReq);
    thread.start();
  }
}
