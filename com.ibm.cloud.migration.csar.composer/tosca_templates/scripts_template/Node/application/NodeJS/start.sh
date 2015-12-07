#!/bin/sh

iptables='/etc/sysconfig/iptables'
App_Start_Dir="${Start_Point_Dir}"
if [ -f /usr/local/bin/node ]; then
    echo "There is node runtime for the Application"
else
    echo "There is no runtime for the Application"
    #install the c++ compiler needed by making process
    yum -y install gcc-c++
    sleep 1
    #make process of Node
    cd /opt/node*
    make CFLAGS+=-O2 CXXFLAGS+=-O2
    sleep 2
    make install CFLAGS+=-O2 CXXFLAGS+=-O2
    sleep 1
fi

if [ -d ${App_Start_Dir} ]; then
    echo "try to setup some environment variables such as application port, application host, etc"
    App_Port=${App_Port_Value}
    echo "export AppPort=${App_Port}" >> /etc/profile
    App_Host=${PublicIP}
    echo "export AppHost=\"${App_Host}\"" >> /etc/profile
    
     echo "try to source the /etc/profile to make the environment variable which will be used by the application"
    . /etc/profile
    
    echo "setup the firewall policy on port 5984, which is the npm source's port"
    iptables -I OUTPUT -p tcp --dport 5984 -j ACCEPT
    iptables -I INPUT -p tcp --sport 5984 -j ACCEPT
    echo "package.json can be found, trying to execute npm install and npm start"
    cd ${App_Start_Dir}
    
    #Currently comment out the npm install as the time.node issue...
    #/usr/local/bin/npm --registry http://9.115.210.2:5984/registry/_design/scratch/_rewrite/ install > log_install_deps.out 2>&1 &
    
    /usr/local/bin/npm start > log_app.out 2>&1 &
    sleep 1
else
    echo "package.json cannot be found, will exit"
    exit 1
fi


## open firewall for the web port
Source_AppPort=${App_Port_Value}
iptables-save | grep -q "OUTPUT -p tcp --sport ${Source_AppPort} -j ACCEPT"
if [ $? -eq 1 ];then
	iptables -I OUTPUT -p tcp --sport ${Source_AppPort} -j ACCEPT
fi

iptables-save | grep -q "INPUT -p tcp --dport ${Source_AppPort} -j ACCEPT"
if [ $? -eq 1 ];then
	iptables -I INPUT -p tcp --dport ${Source_AppPort} -j ACCEPT
fi

iptables-save > $iptables
iptables-restore < $iptables