package com.ibm.cloud.migration.cmd.tool;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.security.oauth2.common.OAuth2AccessToken;

public class Command
{
  private static Log log = LogFactory.getLog(Command.class);
  public static OAuth2AccessToken token;
  public static void main(String[] args)
  {
	  
    //log.info("Command Line Tool starting...");
	  //,"-url","http://api.labs.w3.bluemix.net"
    //String[] arg={"mig","-action","login","-target","cf","-url","http://api.labs.w3.bluemix.net","-org", "netflix","-space","dev"};
	  //String[] arg={"mig","-action","login"};
	  //String[] arg ={"mig","-action","capture"};
	  //String[] arg ={"mig","-action","deploy"};
	  //String[] arg={"mig","-action","cf-apps","-h"};
	 // String[] arg={"mig","-action","capture","-h"};
	 // String[] arg={"mig","-action","deploy","-h"};
	 // String[] arg={"mig","-action","tosca","-h"};
    //String[] arg={"mig","-action","login","-target","cf","-url","http://api.cloudoe.one","-username","micro@vcap.me","-password","micr0@micr0","-org","micro_org","-space","dev"};
    //String[] arg={"mig","-action","login","-target","cf","-url","http://api.ng.w3.bluemix.net","-username","ranchuan@cn.ibm.com","-password","chuanran_072309","-org","ranchuan@cn.ibm.com","-space","dev"};
	  //String[] arg={"mig","-action","cf-apps"};
    //String[] arg={"mig","-action","cf-apps","-org","netflix","-space","dev"};
    //String[] arg={"mig","-action","cf-service-offerings"};
    //String[] arg={"mig","-action","capture","-target","cf","-org","micro_org","-space","dev","-app","countlyui,countlyapi","-appmodel","application_model_test_cf.xml"};
    //String[] arg={"mig","-action","capture","-target","cf","-app","countly-ui-2013,countly-api-2013","-am","application_model_test_cf.xml","-dep","countly-ui-2013->countly-api-2013"};
    //String[] arg={"mig","-action","capture","-target","cf","-app","countlyui,countlyapi","-am","application_model_test_cf.xml","-dep","countlyui->countlyapi"};
    //String[] arg={"mig","-action","deploy","-target","cf","-ad","D:\\WorkSpace\\com.ibm.cloud.migration.cmd.tool","-org","micro_org","-space","dev"};
    //"-ad","F:/material/material/cloudmig/com.ibm.cloud.migration.cmd.tool/countly-heroku"
	// String[] arg={"mig","-action","deploy","-target","cf","-ad","F:/material/material/cloudmig/com.ibm.cloud.migration.cmd.tool/countly-heroku","-am","application_model_heroku.xml"};
    //[] arg={"mig","-action","tosca","-appmodel","application_model_heroku.xml","-csardir","F:"};
   //String[] arg={"mig","-action","login","-target","heroku", "-username","majunc@cn.ibm.com","-password","iikk2156"};
   // String[] arg={"mig","-action","heroku-apps"};
    //String[] arg={"mig","-action","heroku-app-addons"};
	  
	  //,"-appmodel","application_model_test_heroku.xml"
	  //,"-dep","countly-ui-2013->countly-api-2013"
    //String[] arg={"mig","-action","capture","-target","heroku","-app","countly-ui-2013,countly-api-2013","-dep","countly-ui-2013->countly-api-2013"};
    //String[] arg={"mig","-action","help"};
	//String[] arg={"mig","-action","logout","-t","heroku"};
	//String[] arg ={"mig","-action",""};
    CommandLineHandler handler = new CommandLineHandler();    
    handler.checkArgs(args);
    handler.execute();
  }
}
