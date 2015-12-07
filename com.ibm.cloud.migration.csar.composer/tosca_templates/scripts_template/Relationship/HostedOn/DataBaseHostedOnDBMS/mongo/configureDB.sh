#!/bin/sh

iptables='/etc/sysconfig/iptables'

databaseport=$Source_DBPort

#By default, the mongo port is 27017, will be re-defined if mongodb.conf file has the new value
grep -qi ^"port =" /etc/mongodb.conf

if [ $? -eq 0 ]; then
    sed -i "s/port =.*/port = ${databaseport}/g" /etc/mongodb.conf
    #mongodport=$(grep ^"port =" /etc/mongodb.conf | awk -F "= " '{ print $2 }')
else
    echo "port = ${databaseport}" >> /etc/mongodb.conf
fi

#restart the mongo

ps -ef | grep mongodb | grep -v grep | awk '{print $2}' | xargs kill -9
#After killing the process, need to remove the mongod.lock file to avoid failing to start
if [ -f /var/lib/mongodb/mongod.lock ]; then
    rm -f /var/lib/mongodb/mongod.lock
fi
/usr/bin/mongod -f /etc/mongodb.conf &

iptables-save | grep -q "OUTPUT -p tcp --sport $databaseport -j ACCEPT"
if [ $? -eq 1 ];then
	iptables -I OUTPUT -p tcp --sport $databaseport -j ACCEPT
fi

iptables-save | grep -q "INPUT -p tcp --dport $databaseport -j ACCEPT"
if [ $? -eq 1 ];then
	iptables -I INPUT -p tcp --dport $databaseport -j ACCEPT
fi
	
iptables-save > $iptables
iptables-restore < $iptables
