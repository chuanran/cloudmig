package com.ibm.cloud.migration.csar.composer.utility;

import java.io.File;

public class SedCommandObtainer {
	private static String OS = System.getProperty("os.name").toLowerCase();
	private static String SedCommand;
	private static boolean isWindows() {
	    return (OS.indexOf("win") >= 0);
	}

	private static boolean isMac() {
	    return (OS.indexOf("mac") >= 0);
	}

	private static boolean isUnix() {
	    return (OS.indexOf("nux") >= 0);
	}
	
	public static String ObtainSedCommand(){
		if(isWindows()){
			SedCommand = WindowsSedPath();
		} else{
			SedCommand = "sed";
		}
		return SedCommand;
	}
	
	public static String WindowsSedPath(){
		return System.getProperty("user.dir") + File.separator + "sed.exe";
	}
	
//	public static void main(String[] args){
//		System.out.println(SedCommandObtainer.ObtainSedCommand());
//	}

}
