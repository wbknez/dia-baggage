package unmla.airport.simul;

/**
 * 
 */
public class TimeStamp {
    public static final double  SecondsPerMinute    = 60.0;
    
    public static final double  SecondsPerHour      = 3600;
    
    public static String convertTimeToString(final double time, char delim) {
        /// calculate the various time formats
          final double modMinutes = time % SecondsPerHour;
          final double modSeconds = modMinutes % SecondsPerMinute;
                  
          final double hours = (int)((time - modMinutes) / SecondsPerHour);
          final double minutes = 
                              (int)((modMinutes - modSeconds) / SecondsPerMinute);
          final double seconds = (int)modSeconds;
          
          final StringBuilder buffer = new StringBuilder();
          
          if(hours < 10) {
              buffer.append("0");
          }
          buffer.append((int)hours);
          buffer.append(delim);
          
          if(minutes < 10) {
              buffer.append("0");
          }
          buffer.append((int)minutes);
          buffer.append(delim);
          
          if(seconds < 10) {
              buffer.append("0");
          }
          buffer.append((int)seconds);
          
          return buffer.toString();
      }
}
