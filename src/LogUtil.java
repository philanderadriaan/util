import java.util.Date;

public class LogUtil
{
  public static void out(String msg)
  {
    System.out.println(new Date() + "\t" + msg);
  }

  public static void err(String msg)
  {
    System.err.println(new Date() + "\t" + msg);
  }
}
