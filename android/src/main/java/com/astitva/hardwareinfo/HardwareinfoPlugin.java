package com.astitva.hardwareinfo;
import java.io.BufferedReader;
import java.io.File;

import android.os.Build;
import android.os.Environment;
import android.os.StatFs;
import android.app.Activity;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.RandomAccessFile;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.microedition.khronos.opengles.GL10;

import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugin.common.MethodChannel.MethodCallHandler;
import io.flutter.plugin.common.MethodChannel.Result;
import io.flutter.plugin.common.PluginRegistry.Registrar;


/** HardwareinfoPlugin */
public class HardwareinfoPlugin extends  Activity implements MethodCallHandler {
  static String a="label",b="label",c="label";

  /** Plugin registration. */
  public static void registerWith(Registrar registrar) {
    final MethodChannel channel = new MethodChannel(registrar.messenger(), "hardwareinfo");
    channel.setMethodCallHandler(new HardwareinfoPlugin());
  }
  @Override
  public void onMethodCall(MethodCall call, Result result) {
    if (call.method.equals("getPlatformVersion")) {
      result.success("Android " + android.os.Build.VERSION.RELEASE);
    }
    else if (call.method.equals("getCpuMaxFreq"))  {
      ProcessBuilder cmd;
      String results="";
      try{
        String[] args = {"/system/bin/cat", "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_max_freq"};
        cmd = new ProcessBuilder(args);
        Process process = cmd.start();
        InputStream in = process.getInputStream();
        byte[] re = new byte[1024];
        while(in.read(re) != -1){
          results = results + new String(re);
        }
        in.close();
      } catch(IOException ex){
        ex.printStackTrace();
      }
      result.success(results);
    }
    else if (call.method.equals("getCpuMinFreq")) {
      ProcessBuilder cmd;
      String results="";
      try{
        String[] args = {"/system/bin/cat", "/sys/devices/system/cpu/cpu0/cpufreq/cpuinfo_min_freq"};
        cmd = new ProcessBuilder(args);
        Process process = cmd.start();
        InputStream in = process.getInputStream();
        byte[] re = new byte[1024];
        while(in.read(re) != -1){
          results = results + new String(re);
        }
        in.close();
      } catch(IOException ex){
        ex.printStackTrace();
      }
      result.success(results.toString());
    }
    else if (call.method.equals("getCpuCurrFreq")) {

      ProcessBuilder cmd;
      String results="";
      try{
        String[] args = {"/system/bin/cat", "/sys/devices/system/cpu/cpu0/cpufreq/scaling_cur_freq"};
        cmd = new ProcessBuilder(args);
        Process process = cmd.start();
        InputStream in = process.getInputStream();
        byte[] re = new byte[1024];
        while(in.read(re) != -1){
          results = results + new String(re);
        }
        in.close();
      } catch(IOException ex){
        ex.printStackTrace();
      }
      result.success(results);
    }
    else if (call.method.equals("getCpuCore")) {
      int  a = Runtime.getRuntime().availableProcessors();
      result.success(a);
    }
    else if (call.method.equals("getCompMenuf")) {
      result.success(Build.MANUFACTURER);
    }
    else if (call.method.equals("getModel")) {
      result.success(Build.MODEL);
    }
    else  if(call.method.equals("getFreeInternalMemory")) {
      String availmem = getAvailableInternalMemorySize();
      result.success(availmem );

    }
    else  if(call.method.equals("getTotalInternalMemory")) {
      String totalmem = getTotalInternalMemorySize();
      result.success(totalmem );

    }
    else  if(call.method.equals("getUsedInternalMemory")) {
      String usedmem = getUsedInternalMemorySize();
      result.success(usedmem );

    }
    else  if(call.method.equals("getFreeExternalMemory")) {
      String availmeme = getAvailableExternalMemorySize();
      result.success(availmeme );

    }
    else  if(call.method.equals("getTotalExternalMemory")) {
      String totalmeme = getTotalExternalMemorySize();
      result.success(totalmeme );

    }
    else  if(call.method.equals("getUsedExternalMemory")) {
      String usedmeme = getUsedExternalMemorySize();
      result.success(usedmeme);

    }
    else  if(call.method.equals("getTotalRam")) {
      RandomAccessFile reader = null;
      String load = null;
      DecimalFormat twoDecimalForm = new DecimalFormat("#.##");
      double totRam = 0;
      String lastValue = "";
      try {
        reader = new RandomAccessFile("/proc/meminfo", "r");
        load = reader.readLine();
        Pattern p = Pattern.compile("(\\d+)");
        Matcher m = p.matcher(load);
        String value = "";
        while (m.find()) {
          value = m.group(1);
        }
        reader.close();
        totRam = Double.parseDouble(value);
        double mb = totRam / 1024.0;
        double gb = totRam / 1048576.0;
        double tb = totRam / 1073741824.0;
        if (tb > 1) {
          lastValue = twoDecimalForm.format(tb).concat(" TB");
        } else if (gb > 1) {
          lastValue = twoDecimalForm.format(gb).concat(" GB");
        } else if (mb > 1) {
          lastValue = twoDecimalForm.format(mb).concat(" MB");
        } else {
          lastValue = twoDecimalForm.format(totRam).concat(" KB");
        }
      } catch (IOException ex) {
        ex.printStackTrace();
      }
      String re = lastValue;
      result.success(re);

    }
    else if(call.method.equals("getCpuTemp")){
      Process process;
      try {
        process = Runtime.getRuntime().exec("cat sys/class/thermal/thermal_zone0/temp");
        process.waitFor();
        BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
        String line = reader.readLine(),temps = "";
        if(line!=null) {
          float temp = Float.parseFloat(line);
          temps = String.valueOf(temp / 1000.0f)+" c";
          result.success(temps);
        }else{
          temps = String.valueOf(51.0f) +"c";
          result.success(temps );
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }


    else {
      result.notImplemented();
    }
  }

  public static boolean externalMemoryAvailable() {
    return android.os.Environment.getExternalStorageState().equals(
            android.os.Environment.MEDIA_MOUNTED);
  }

  public static String getAvailableExternalMemorySize() {
    if (externalMemoryAvailable()) {
      File path = Environment.getExternalStorageDirectory();
      StatFs stat = new StatFs(path.getPath());
      long blockSize = 0;
      if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
        blockSize = stat.getBlockSizeLong();
      }
      long availableBlocks = 0;
      if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
        availableBlocks = stat.getAvailableBlocksLong();
      }
      return formatSize(availableBlocks * blockSize);
    } else {
      return "External Memory Not Available";
    }
  }
  public static String getUsedExternalMemorySize(){
    if (externalMemoryAvailable()) {
      File path = Environment.getExternalStorageDirectory();
      StatFs stat = new StatFs(path.getPath());
      long blockSize = 0;
      if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
        blockSize = stat.getBlockSizeLong();
      }
      long availableBlocks = 0;
      if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
        availableBlocks = stat.getBlockCountLong()-stat.getAvailableBlocksLong();
      }
      return formatSize(availableBlocks * blockSize);
    } else {
      return "External Memory Not Available";
    }
  }
  public static String getTotalExternalMemorySize() {
    if (externalMemoryAvailable()) {
      File path = Environment.getExternalStorageDirectory();
      StatFs stat = new StatFs(path.getPath());
      long blockSize = 0;
      if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
        blockSize = stat.getBlockSizeLong();
      }
      long totalBlocks = 0;
      if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
        totalBlocks = stat.getBlockCountLong();
      }
      return formatSize(totalBlocks * blockSize);
    } else {
      return "External Memory Not Available";
    }
  }
  public static String getAvailableInternalMemorySize() {
    File path = Environment.getDataDirectory();
    StatFs stat = new StatFs(path.getPath());
    long blockSize = 0;
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
      blockSize = stat.getBlockSizeLong();
    }
    long availableBlocks = 0;
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
      availableBlocks = stat.getAvailableBlocksLong();
    }

    return formatSize(availableBlocks * blockSize);
  }
  public static String getTotalInternalMemorySize() {
    File path = Environment.getDataDirectory();
    StatFs stat = new StatFs(path.getPath());
    long blockSize = 0;
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
      blockSize = stat.getBlockSizeLong();
    }
    long totalBlocks = 0;
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
      totalBlocks = stat.getBlockCountLong();
    }
    return formatSize(totalBlocks * blockSize);
  }
  public static String getUsedInternalMemorySize(){
    File path = Environment.getDataDirectory();
    StatFs stat = new StatFs(path.getPath());
    long blockSize = 0;
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
      blockSize = stat.getBlockSizeLong();
    }
    long totalBlocks = 0;
    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR2) {
      totalBlocks = stat.getBlockCountLong()-stat.getAvailableBlocksLong();
    }
    return formatSize(totalBlocks * blockSize);

  }
  public static String formatSize(long size) {
    String suffix = null;

    if (size >= 1024) {
      suffix = "KB";
      size /= 1024;
      if (size >= 1024) {
        suffix = "MB";
        size /= 1024;
      }
    }

    StringBuilder resultBuffer = new StringBuilder(Long.toString(size));

    int commaOffset = resultBuffer.length() - 3;
    while (commaOffset > 0) {
      resultBuffer.insert(commaOffset, ',');
      commaOffset -= 3;
    }

    if (suffix != null) resultBuffer.append(suffix);
    return resultBuffer.toString();
  }

  public static final String[] getGPUInfo(GL10 gl10) {
    String N = "label";
    try {
      String[] result = new String[3];
      result[0] = gl10.glGetString(GL10.GL_RENDERER);
      result[1] = gl10.glGetString(GL10.GL_VENDOR);
      result[2] = gl10.glGetString(GL10.GL_VERSION);
      Log.d(N,result[0]+result[1]+result[2]);
      return result;
    } catch (Exception localException) {
    }

    return new String[0];
  }

}

