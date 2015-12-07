#!/bin/sh
iptables='/etc/sysconfig/iptables'

${Service_Firewall_Setting}

echo "Configuration of the firewall is completed"	
iptables-save > $iptables
iptables-restore < $iptables

# Set the required environment variable on service credential
${Export_VCAP_SERVICES}
