package gl.core.db;

public class DbDetailsValidation
{
  public boolean validDetails(String type, String url, String user, String passwd) {
    if (type == null || (!type.equals("POLYHEDRA") && !type.equals("ORACLE") && !type.equals("MYSQL") && !type.equals("HIVE"))) {
      System.out.println("please specify database type options:POLYHEDRA/ORACLE/MYSQL");
      return false;
    } 
    if (url == null) {
      System.out.println("please specify database connect url ::POLYHEDRA/ORACLE/MYSQL");
      return false;
    } 
    if (user == null || passwd == null) {
      System.out.println("please specify database user/password");
      return false;
    } 
    return true;
  }
}
