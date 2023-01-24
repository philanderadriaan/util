
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
      return 0;
    }
    Clip clip = AudioSystem.getClip();
    clip.open(AudioSystem.getAudioInputStream(file));
    clip.start();
    while (!clip.isRunning())
    {
      Thread.sleep(1);
    }
    long start = System.currentTimeMillis();
    while (clip.isRunning())
    {
      Thread.sleep(1);
    }
    long finish = System.currentTimeMillis();
    while (System.currentTimeMillis() - start < elapsed)
    {
      Thread.sleep(1);
    }
    clip.close();
    return finish - start;
  }

  public static void play(File... files)
  {
    new Thread(() -> {
      long elapsed = 0;
      for (File file : files)
      {
        try
        {
          elapsed = play(file, elapsed);
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
      }
    }).start();
  }

  public static void flush()
  {
    play(new File("../common/flush.wav"));
  }

}
