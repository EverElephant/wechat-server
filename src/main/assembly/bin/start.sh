#!/bin/bash
nohup java -XX:+HeapDumpOnOutOfMemoryError -Xmx2048m -Xms1024m -Xss256k -Duser.timezone=GMT+08 -Dfile.encoding=utf-8 -XX:+PrintGCTimeStamps -XX:+PrintHeapAtGC -Xmn512M -XX:+PrintGCDetails -XX:+UseParNewGC -XX:MaxTenuringThreshold=5 -XX:ParallelGCThreads=8 -XX:+UseCMSCompactAtFullCollection -XX:CMSFullGCsBeforeCompaction=3 -XX:SurvivorRatio=4 -XX:CMSInitiatingOccupancyFraction=80 -XX:PermSize=32m -XX:MaxPermSize=68m -XX:+UseConcMarkSweepGC -server -cp ./wechattools.jar:./resource/ org.springframework.boot.loader.JarLauncher prod >/dev/null 2>&1 &