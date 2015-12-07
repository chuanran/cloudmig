#!/bin/sh
InstallationPackage_MountPoint="/opt"

if [ -f /usr/local/bin/node ]; then
    echo "node is installed successfully"
else
    echo "node failed to be installed, try again to install it"
    #install the c++ compiler needed by making process
    yum -y install gcc-c++
    #make process of Node
    cd ${InstallationPackage_MountPoint}/node*
    make CFLAGS+=-O2 CXXFLAGS+=-O2
    make install CFLAGS+=-O2 CXXFLAGS+=-O2
fi