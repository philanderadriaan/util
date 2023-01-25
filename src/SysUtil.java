public class SysUtil
{
  public static void exit() throws Exception
  {
    WavUtil.flush();
    System.exit(0);
  }
}
