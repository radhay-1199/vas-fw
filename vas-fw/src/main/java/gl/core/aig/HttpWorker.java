package gl.core.aig;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;













public class HttpWorker
  extends WorkerThread
{
  public void process(Object packet) {
    try {
      AigDataInfo aigDataInfo = (AigDataInfo)packet;
      this.log.info(getThreadIndex() + "-" + aigDataInfo.getData() + "-" + CallURL((String)aigDataInfo.getData()));
    } catch (Exception exception) {}
  }


  
  public String CallURL(String urlString) {
    String inputResponse = "";
    try {
      URL url = new URL(urlString);
      URLConnection urlConn = url.openConnection();
      urlConn.setConnectTimeout(30000);
      urlConn.setReadTimeout(30000);
      BufferedReader inputReader = new BufferedReader(new InputStreamReader(urlConn.getInputStream()));
      String inputLine;
      while ((inputLine = inputReader.readLine()) != null)
        inputResponse = inputResponse + inputLine; 
      inputReader.close();
    }
    catch (Exception exp) {
      inputResponse = exp.getMessage();
    } 
    return inputResponse;
  }
}

