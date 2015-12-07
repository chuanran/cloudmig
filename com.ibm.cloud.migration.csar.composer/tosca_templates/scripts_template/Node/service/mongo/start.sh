#!/bin/bash

ps -ef | grep mongodb | grep -v grep | awk '{print $2}' | xargs kill -9
if [ -f /var/lib/mongodb/mongod.lock ]; then
    rm -f /var/lib/mongodb/mongod.lock
fi
/usr/bin/mongod -f /etc/mongodb.conf &