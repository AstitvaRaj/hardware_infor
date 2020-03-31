import 'dart:async';
import 'package:flutter/services.dart';
const MethodChannel _channel = const MethodChannel('hardwareinfo');
class Hardwareinfo {
  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
  static Future<String>get cpuMaxFreq async {
    final String version = await _channel.invokeMethod('getCpuMaxFreq');
    return version;
  }
  static Future<String>get cpuMinFreq async {
    final String version = await _channel.invokeMethod('getCpuMinFreq');
    return version;
  }
  static Future<String>get cpuCurrFreq async {
    final String version = await _channel.invokeMethod('getCpuCurrFreq');
    return version;
  }
  static Future get cpuCore async {
    int version = await _channel.invokeMethod('getCpuCore');
    return version;
  }
  static Future<String>get company async {
    final String version = await _channel.invokeMethod('getCompMenuf');
    return version;
  }
  static Future<String>get model async {
    final String version = await _channel.invokeMethod('getModel');
    return version;
  }
  static Future<String> get totalInternalMemory async{
    final String totalmem = await _channel.invokeMethod('getTotalInternalMemory');
    return totalmem;
  }
  static Future<String> get usedInternalMemory async{
    final String usedmem = await _channel.invokeMethod('getUsedInternalMemory');
    return usedmem;
  }
  static Future<String> get freeInternalMemory async{
    final String freemem = await _channel.invokeMethod('getFreeInternalMemory');
    return freemem;
  }
  static Future<String> get totalExternalMemory async{
    final String totalmem = await _channel.invokeMethod('getTotalExternalMemory');
    return totalmem;
  }
  static Future<String> get usedExternalMemory async{
    final String usedmem = await _channel.invokeMethod('getUsedExternalMemory');
    return usedmem;
  }
  static Future<String> get freeExternalMemory async{
    final String freemem = await _channel.invokeMethod('getFreeExternalMemory');
    return freemem;
  }
  static Future<String> get totalRAM async {
    final String Total = await _channel.invokeMethod('getTotalRam');
    return Total;
  }
  static Future<String> get cpuTemp async  {
    final String temp = await _channel.invokeMethod("getCpuTemp");
    return temp;
  }

}

