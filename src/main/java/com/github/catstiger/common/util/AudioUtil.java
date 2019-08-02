package com.github.catstiger.common.util;

import java.io.BufferedReader;
import java.io.File;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;

import com.google.common.base.Splitter;

/**
 * 使用ffmpeg转换音频文件格式，因此首先要安装ffmepg
 * <ul>
 * <li>在CentOS中：https://www.cnblogs.com/wpjamer/p/ffmpeg.html</li>
 * <li>在Ubuntu中：https://blog.csdn.net/lwgkzl/article/details/77836207</li> MAC
 * 系统安装FFmpeg: https://www.jianshu.com/p/73441acf7815
 * </ul>
 * <p>
 * <strong> 要在用户目录下创建软连接 ln -s path_to_ffmpeg ~/ffmpeg </strong>
 * </p>
 * 
 * @author samlee
 *
 */
public final class AudioUtil {

  public static final int DEFAULT_SAMPLE_RATE = 16000;

  /**
   * 转换音频文件格式
   * 
   * @param in         输入文件
   * @param out        输出文件
   * @param sampleRate 采样率，16000， 8000，44100等
   */
  public static void convert(File in, File out, int sampleRate) {
    ExecUtil exec = new ExecUtil();
    exec.execute(new File(System.getProperty("user.home")), System.getProperty("user.home") + "/ffmpeg", "-y", "-i",
        in.getAbsolutePath(), "-ar", String.valueOf(sampleRate), out.getAbsolutePath());
  }

  /**
   * 获取音频文件持续时长
   * 
   * @param path 音频文件为准，可以是本地文件，也可以是网络文件
   * @return 持续时长，例如00:00:04.32
   */
  public static String duration(String path) {
    ExecUtil exec = new ExecUtil();
    List<String> cmds = Arrays.asList(System.getProperty("user.home") + "/ffmpeg", "-i", path);
    String lines = exec.exeAndReadAllOutput(new File(System.getProperty("user.home")), cmds);
    
    try (BufferedReader reader = new BufferedReader(new StringReader(lines))) {
      String line = null;
      while((line = reader.readLine()) != null) {
        line = StringUtils.trimToNull(line);
        if (StringUtils.startsWith(line, "Duration")) {
          List<String> spls = Splitter.on(",").splitToList(line);
          if (!CollectionUtils.isEmpty(spls)) {
            String duration = StringUtils.trimToNull(spls.get(0));
            return StringUtils.trimToNull(duration.substring(duration.indexOf(":") + 1));
          }
        }
      }
    } catch (Exception e) {
      e.printStackTrace();
      return null;
    }
    return null;
  }
  
  /**
   * 格式化为X′Y″的形式，忽略小时
   * @param duration
   * @return
   */
  public static String formatDuration(String duration) {
    if (StringUtils.isBlank(duration)) {
      return null;
    }
    String[] durs = duration.split(":");
    if(durs == null || durs.length != 3) {
      return null;
    }
    
    if (Integer.valueOf(durs[1]).equals(0)) {
      return Float.valueOf(durs[2]) + "″";
    } else {
      return Integer.valueOf(durs[1]) + "′" + Float.valueOf(durs[2]) + "″";
    }
  }

  /**
   * 转换音频文件
   * 
   * @param in  输入文件
   * @param out 输出文件
   */
  public static void convert(File in, File out) {
    AudioUtil.convert(in, out, DEFAULT_SAMPLE_RATE);
  }

  private AudioUtil() {

  }

  public static void main(String[] args) {
    String s = "https://tigercdn.oss-cn-beijing.aliyuncs.com/f/haohao.m4a";
    String d = duration(s);
    System.out.println(d);
    System.out.println(formatDuration(d));
  }

}
