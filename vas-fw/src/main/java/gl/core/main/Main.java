package gl.core.main;

import gl.core.fw.GRFrameWork;

public class Main
{
  public static void main(String[] args) {
    try {
      GRFrameWork gr = new GRFrameWork();
      GRFrameWork.main(args);
    }
    catch (Exception exp) {
      exp.printStackTrace();
      System.exit(0);
    } 
  }
}
