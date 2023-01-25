
import java.io.File;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Random;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class AudioUtility
{

  private static final Map<String, File[]> FILE_MAP = new HashMap<String, File[]>();
  private static final Map<String, String> LAST_FILE_MAP = new HashMap<String, String>();
  private static Entry<String, List<File>> fileEntry =
      new SimpleEntry<String, List<File>>("", new ArrayList<File>());

  private static final Random RANDOM = new Random();

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

  public static void put(String key, File directory)
  {
    FILE_MAP.put(key, directory.listFiles());
  }
}
