package com.ibm.cloud.migration.csar.composer.utility;


//import org.apache.log4j.Logger;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class LinuxCmd {

	//private static final Logger logger = Logger.getLogger(LinuxCmd.class);

	public static int runLinuxCmd(String cmd)  throws InterruptedException,
	IOException{
		System.out.println("runLinuxCmd(String) - start");
		System.out.println("runLinuxCmd(String) -  " + cmd);
		Runtime run = Runtime.getRuntime();
		Process pr;
		int rc = 0;
		pr = run.exec(cmd);
		rc = pr.waitFor();
		BufferedReader buf = new BufferedReader(new InputStreamReader(pr
				.getInputStream()));
		String line = "";
		while ((line = buf.readLine()) != null) {
			System.out.println(line);
		}
		
		System.out.println("runLinuxCmd(String) - end");

		return rc;
	}
}
