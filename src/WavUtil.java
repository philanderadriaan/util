
import java.io.File;
import java.util.AbstractMap.SimpleEntry;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.sound.sampled.AudioSystem;
import javax.sound.sampled.Clip;

public class WavUtil
{
  private static final Map<String, File[]> FILE_MAP = new HashMap<String, File[]>();
  private static final Map<String, String> PREV_FILE_MAP = new HashMap<String, String>();
  private static Entry<String, List<File>> fileEntry =
      new SimpleEntry<String, List<File>>("", new ArrayList<File>());
  private static long cooldown = 1;

  public static void play(File file) throws Exception
  {
    if (file == null)
    {
      Thread.sleep(cooldown);
      cooldown = 1;
    }
    else
    {
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
      while (System.currentTimeMillis() - start < cooldown)
      {
        Thread.sleep(1);
      }
      cooldown = finish - start;
      clip.close();
    }
  }

  public static void playBackground(File... files)
  {
    new Thread(() -> {
      for (File file : files)
      {
        try
        {
          play(file);
        }
        catch (Exception e)
        {
          e.printStackTrace();
        }
      }
    }).start();
  }

  public static void flush() throws Exception
  {
    play(new File("../utility/flush.wav"));
  }

  public static void putDirectory(File dir)
  {
    if (PREV_FILE_MAP.isEmpty())
    {
      PREV_FILE_MAP.put("", "");
    }
    FILE_MAP.put(dir.getName(), dir.listFiles());
  }

  public static void playDirectory(String dirName) throws Exception
  {
    if (dirName == null)
    {
      play(null);
    }
    else
    {
      if (!fileEntry.getKey().equalsIgnoreCase(dirName) ||
          fileEntry.getValue().isEmpty())
      {
        List<File> fileList = new ArrayList<File>();
        fileList.addAll(Arrays.asList(FILE_MAP.get(dirName)));
        Collections.shuffle(fileList);
        if (fileList.get(0).getAbsolutePath()
            .equalsIgnoreCase(PREV_FILE_MAP.get(dirName)))
        {
          fileList.remove(0);
        }
        fileEntry = new SimpleEntry<String, List<File>>(dirName, fileList);
        PREV_FILE_MAP.put(dirName, fileEntry.getValue().get(0).getAbsolutePath());
      }
      play(fileEntry.getValue().remove(0));
    }
  }

  public static void resetCooldown()
  {
    cooldown = 1;
  }
}
