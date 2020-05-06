package gl.core.util;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentHashMap;

public class SeriesDetails extends LogUtil {
  ConcurrentHashMap<String, ArrayList> seriesMap = null;
  
  public SeriesDetails() { this.seriesMap = new ConcurrentHashMap<>(); }

  
  public String addSeries(String startRange, String endRange, String circle, Object info) {
    try {
      if (startRange == null || startRange.length() < 4) {
        printLog(4, "Invalid start range please check.Range can't be null and length should be more than 4 char,start range=" + startRange);
        return "Invalid Start Range";
      } 
      if (endRange == null || endRange.length() < 4) {
        printLog(4, "Invalid end range please check.Range can't be null and length should be more than 4 char, End range=" + endRange);
        return "Invalid End Range";
      } 
      String subStartRange = startRange.substring(0, 4);
      ArrayList<SeriesInfo> seriesList = this.seriesMap.get(subStartRange);
      if (seriesList == null) {
        seriesList = new ArrayList();
      }
      SeriesInfo seriesInfo = new SeriesInfo(startRange, endRange, circle, info);
      seriesList.add(seriesInfo);
      this.seriesMap.put(subStartRange, seriesList);
    }
    catch (Exception exp) {
      exp.printStackTrace();
      return exp.getMessage();
    } 
    return "success";
  }
  public String getRegion(String number) {
    try {
      long min = 0L;
      long max = 0L;
      long Series = 0L;
      if (number == null || number.length() < 4) {
        printLog(5, "invalid msisdn, mssidn =" + number);
        return null;
      } 
      String startRange = number.substring(0, 4);
      ArrayList<SeriesInfo> seriesList = this.seriesMap.get(startRange);
      if (seriesList == null) {
        
        printLog(5, "Series not defined for start range =" + startRange);
        return null;
      } 
      for (int i = 0; i < seriesList.size(); i++) {
        SeriesInfo seriesInfo = seriesList.get(i);
        int startRangelen = seriesInfo.getStartRange().length();
        if (number.length() > startRangelen) {
          String subseries = number.substring(0, startRangelen);
          Series = Long.parseLong(subseries);
          min = Long.parseLong(seriesInfo.getStartRange());
          max = Long.parseLong(seriesInfo.getEndRange());
          if (Series >= min && Series <= max) {
            printLog(4, "msisdn [" + number + "] min [" + min + "] max [" + max + "] Region [" + seriesInfo.getCircle() + "]");
            return seriesInfo.getCircle();
          }
        
        } 
      } 
    } catch (Exception e) {
      printLog(0, e.getMessage());
    } 
    return null;
  }
  public Object getInfo(String number) {
    try {
      long min = 0L;
      long max = 0L;
      long Series = 0L;
      if (number == null || number.length() < 4) {
        printLog(5, "invalid msisdn, mssidn =" + number);
        return null;
      } 
      String startRange = number.substring(0, 4);
      ArrayList<SeriesInfo> seriesList = this.seriesMap.get(startRange);
      if (seriesList == null) {
        
        printLog(5, "Series not defined for start range =" + startRange);
        return null;
      } 
      for (int i = 0; i < seriesList.size(); i++) {
        SeriesInfo seriesInfo = seriesList.get(i);
        int startRangelen = seriesInfo.getStartRange().length();
        if (number.length() > startRangelen) {
          String subseries = number.substring(0, startRangelen);
          Series = Long.parseLong(subseries);
          min = Long.parseLong(seriesInfo.getStartRange());
          max = Long.parseLong(seriesInfo.getEndRange());
          if (Series >= min && Series <= max) {
            return seriesInfo.getInfo();
          }
        }
      
      } 
    } catch (Exception e) {
      printLog(0, e.getMessage());
    } 
    return null;
  }
}
