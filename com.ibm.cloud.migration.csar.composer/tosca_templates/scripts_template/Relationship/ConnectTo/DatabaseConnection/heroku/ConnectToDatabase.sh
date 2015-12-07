#!/bin/sh
iptables='/etc/sysconfig/iptables'

${Service_Firewall_Setting}

echo "Configuration of the firewall is completed"	
iptables-save > $iptables
iptables-restore < $iptables

#For heroku, set the environment variable
${Export_Env_Variables}
