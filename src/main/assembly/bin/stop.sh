#!/bin/bash
kill -s 9 `ps aux | grep wechattools.jar | awk '{print $2}'`