import 'package:flutter/material.dart';
import 'dart:async';

import 'package:flutter/services.dart';
import 'package:hardwareinfo/hardwareinfo.dart';

void main() => runApp(mains());
class mains extends StatelessWidget {
  @override
  Widget build(BuildContext context) {
    return MaterialApp(
      home: MyApp(),
    );
  }
}


class MyApp extends StatefulWidget {
  @override
  _MyAppState createState() => _MyAppState();
}

class _MyAppState extends State<MyApp> {
  String _platformVersion="unknown",_maxcpuFreq="unknown",_mincpuFreq="unknown",_currcpuFreq="unknown",_model,_company,_batterylevel;
  String _totalinternalmemory,_usedinternalmemory,_freeinternalmemory,_totalexternalmemory,_usedexternalmemory,_freeexternalmemory;
  String _totalRam,_cputemp;
  int _noOfCores;

  @override
  void initState() {
    super.initState();
    initPlatformState();
  }
  Future<void> initPlatformState() async {
    String platformVersion,maxfreq,minfreq,currfreq,model,company,batterylevel,cputemp;
    String usedinternal,freeinternal,totalinternal;
    String usedexternal,freeexternal,totalexternal;
    String totalram,availRam;
    int cores;
    try {
      platformVersion = await Hardwareinfo.platformVersion;
      maxfreq = await Hardwareinfo.cpuMaxFreq;
      minfreq = await Hardwareinfo.cpuMinFreq;
      currfreq = await Hardwareinfo.cpuCurrFreq;
      cores = await Hardwareinfo.cpuCore;
      company = await Hardwareinfo.company;
      model = await Hardwareinfo.model;
      batterylevel =null;
      totalinternal = await Hardwareinfo.totalInternalMemory;
      freeinternal = await Hardwareinfo.freeInternalMemory;
      usedinternal = await Hardwareinfo.usedInternalMemory;
      totalexternal = await Hardwareinfo.totalExternalMemory;
      freeexternal = await Hardwareinfo.freeExternalMemory;
      usedexternal = await Hardwareinfo.usedExternalMemory;
      totalram = await Hardwareinfo.totalRAM;
      cputemp = await Hardwareinfo.cpuTemp;
    } on PlatformException {
      platformVersion = 'Failed to get platform version.';
    }
    if (!mounted) return;

    setState(() {
      _platformVersion = platformVersion;
      _maxcpuFreq = maxfreq;
      _mincpuFreq = minfreq;
      _noOfCores = cores;
      _currcpuFreq = currfreq;
      _model = model;
      _company = company;
      _batterylevel = batterylevel;
      _usedinternalmemory = usedinternal;
      _freeinternalmemory = freeinternal;
      _totalinternalmemory = totalinternal;
      _totalexternalmemory = totalexternal;
      _freeexternalmemory = freeexternal;
      _usedexternalmemory = usedexternal;
      _totalRam = totalram;
      _cputemp = cputemp;

    });
  }

  @override
  Widget build(BuildContext context) {
    final double width = MediaQuery.of(context).size.width;
    return MaterialApp(
      home: Scaffold(
        appBar: AppBar(
          title: const Text('System info app'),
        ),
        body: SingleChildScrollView(
          child: Padding(
            padding: EdgeInsets.only(left: 20,top:  10),
            child: Container(
              width: width-20,
              child:  Column(
                crossAxisAlignment: CrossAxisAlignment.start,
                mainAxisAlignment: MainAxisAlignment.spaceAround,
                children: <Widget>[
                  Card(
                    child:Padding(
                      padding: EdgeInsets.all(10),
                      child: Container(
                      width: width-20,
                      child: Column(
                        mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                        crossAxisAlignment: CrossAxisAlignment.start,
                        children: <Widget>[
                          Text('CPU\n',style: TextStyle(fontSize: 23),),
                          Text('Maximum frequency (in Hz): ${_maxcpuFreq }'),
                          Text('Minimum frequency (in Hz): ${_mincpuFreq}'),
                          Text('Current frequency (in Hz): ${_currcpuFreq}'),
                          Text('No of Cores: ${_noOfCores}\n'),
                          Text('CPU Temp: ${_cputemp}\n'),
                        ],
                      ),
                    ),
                    )
                  ),
                  Card(
                      child:Padding(
                        padding: EdgeInsets.all(10),
                        child: Container(
                          width: width-20,
                          child: Column(
                            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: <Widget>[
                              Text('Device Info \n',style: TextStyle(fontSize: 23),),
                              Text('Android Version: $_platformVersion \n'),
                              Text('Menufacturer: $_company\n'),
                              Text('Model: $_model\n'),
                            ],
                          ),
                        ),
                      )
                  ),
                  Card(
                      child:Padding(
                        padding: EdgeInsets.all(10),
                        child: Container(
                          width: width-20,
                          child: Column(
                            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: <Widget>[
                              Text('Memory Info \n',style: TextStyle(fontSize: 23),),
                              Text('Total Internal Memory: $_totalinternalmemory \n'),
                              Text('Available Internal Memory: $_freeinternalmemory\n'),
                              Text('Used Internal Memory: $_usedinternalmemory\n'),
                              Text('Total External Memory: $_totalexternalmemory \n'),
                              Text('Available External Memory: $_freeexternalmemory\n'),
                              Text('Used External Memory: $_usedexternalmemory\n'),
                            ],
                          ),
                        ),
                      )
                  ),
                  Card(
                      child:Padding(
                        padding: EdgeInsets.all(10),
                        child: Container(
                          width: width-20,
                          child: Column(
                            mainAxisAlignment: MainAxisAlignment.spaceEvenly,
                            crossAxisAlignment: CrossAxisAlignment.start,
                            children: <Widget>[
                              Text('RAM Info \n',style: TextStyle(fontSize: 23),),
                              Text('Total RAM: $_totalRam \n'),
                            ],
                          ),
                        ),
                      )
                  ),
                ],
              ),
            ),
          )
        ),
        floatingActionButton: FloatingActionButton(
          child: Icon(Icons.refresh),
          onPressed: (){
            initPlatformState();
          },
        ),
      ),
    );
  }
}
