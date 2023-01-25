
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

public class AudioUtility
{
  private static final Map<String, File[]> FILE_MAP = new HashMap<String, File[]>();
  private static final Map<String, String> PREVIOUS_FILE_MAP = new HashMap<String, String>();
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
      clip.close();
      cooldown = finish - start;
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

  public static void flush()
  {
    playBackground(new File("../common/flush.wav"));
  }

  public static void putDirectory(File directory)
  {
    if (PREVIOUS_FILE_MAP.size() == 0)
    {
      PREVIOUS_FILE_MAP.put("", "");
    }
    FILE_MAP.put(directory.getName(), directory.listFiles());
  }

  public static void playDirectory(String directoryName) throws Exception
  {
    if (directoryName == null)
    {
      play(null);
    }
    else
    {
      if (!fileEntry.getKey().equalsIgnoreCase(directoryName) ||
          fileEntry.getValue().size() == 0)
      {
        List<File> fileList = new ArrayList<File>();
        fileList.addAll(Arrays.asList(FILE_MAP.get(directoryName)));
        Collections.shuffle(fileList);
        if (fileList.get(0).getAbsolutePath()
            .equalsIgnoreCase(PREVIOUS_FILE_MAP.get(directoryName)))
        {
          fileList.remove(0);
        }
        fileEntry = new SimpleEntry<String, List<File>>(directoryName, fileList);
        PREVIOUS_FILE_MAP.put(directoryName, fileEntry.getValue().get(0).getAbsolutePath());
      }
      play(fileEntry.getValue().remove(0));
    }
  }

  public static void resetCooldown()
  {
    cooldown = 1;
  }
}
