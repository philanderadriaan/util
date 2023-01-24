import java.util.Date;

public class Log
{
  public static void log(String message)
  {
    System.out.println(new Date() + "\t" + message);
  }
}
