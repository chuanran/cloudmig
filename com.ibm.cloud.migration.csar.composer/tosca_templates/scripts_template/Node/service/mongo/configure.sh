#!/bin/sh
#By default, the mongo port is 27017, will be re-defined if mongodb.conf file has the new value
#mongodport=27017

InstallationPackage_MountPoint="/opt"

#change mode to be executable for mongo script

chmod +x ${InstallationPackage_MountPoint}/mongodb*/bin/*

#create a softlink of mongod for convenience
if [ -f ${InstallationPackage_MountPoint}/mongodb*/bin/mongod ]; then
    ln -s ${InstallationPackage_MountPoint}/mongodb*/bin/mongod /usr/bin/mongod
fi

#move the mongodb.conf file to /etc directory
mv ${InstallationPackage_MountPoint}/mongodb.conf /etc

#According to the mongodb.conf, mongod writes data to the /data/db/ directory
if [ ! -d /var/lib/mongodb ]; then
    mkdir -p /var/lib/mongodb
fi

#logging file for mongodb
if [ ! -d /var/log/mongodb ]; then
    mkdir -p /var/log/mongodb
    touch /var/log/mongodb/mongodb.log
fi
