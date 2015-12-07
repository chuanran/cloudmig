#!/bin/sh

#install the NodeJS from source code


#change mode to be executable 

InstallationPackage_MountPoint="/opt"

chmod +x ${InstallationPackage_MountPoint}/node*/configure

if [ -e ${InstallationPackage_MountPoint}/node* ]; then
    echo "node source code can be found here"
else
    echo "node source code cannot be found here"
    exit 1
fi

#install the c++ compiler needed by making process
yum -y install gcc-c++

sleep 10

#make process of Node
cd ${InstallationPackage_MountPoint}/node*
make CFLAGS+=-O2 CXXFLAGS+=-O2
sleep 30
make install CFLAGS+=-O2 CXXFLAGS+=-O2
sleep 5

if [ -f /usr/local/bin/node ]; then
    echo "node is installed successfully"
else
    echo "node failed to be installed"
    exit 1
fi