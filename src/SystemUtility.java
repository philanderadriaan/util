public class SystemUtility
{
  public static void exit() throws Exception
  {
    AudioUtility.flush();
    System.exit(0);
  }
}
