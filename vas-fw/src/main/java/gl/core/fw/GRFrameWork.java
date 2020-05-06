package gl.core.fw;
public class GRFrameWork {
  public static void main(String[] args) {
    try {
      String configFile = "config.cfg";
      if (args.length == 1)
        configFile = args[0]; 
      FrameWork frameWork = new FrameWork(configFile);
    }
    catch (Exception exp) {
      exp.printStackTrace();
      System.exit(0);
    } 
  }
}
