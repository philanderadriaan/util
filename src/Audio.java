
import java.io.File;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class Audio
{

  public static long play(File file, long elapsed) throws Exception
  {
    if (file == null)
    {
      Thread.sleep(elapsed);
      return 1;
    }
    Clip clip = AudioSystem.getClip();
    clip.open(AudioSystem.getAudioInputStream(file));
    clip.start();
    while (!clip.isRunning())
    {
      Thread.sleep(100);
    }
    long start = System.currentTimeMillis();
    while (clip.isRunning())
    {
      Thread.sleep(100);
    }
    long finish = System.currentTimeMillis();
    while (System.currentTimeMillis() - start < elapsed)
    {
      Thread.sleep(100);
    }
    clip.close();
    return finish - start;
  }

  public static void playList(File... files)
  {
    new Thread(() -> {
      long elapsed = 1;
      for (File file : files)
      {
        try
        {
          elapsed = play(file, elapsed);
        }
        catch (Throwable e)
        {
          e.printStackTrace();
        }
      }

    }).start();
  }

  public static void flush()
  {
    playList(new File("../common/flush.wav"));
  }

}
