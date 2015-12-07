package com.ibm.cloud.migration.utility;

import java.io.Console;

public class Passwd {	
	
	public Passwd() {
		// TODO Auto-generated constructor stub
	}

	public String hidePasswd() {
		String hpw=null;
		Console con=System.console();
	    if(con!=null){
	    	hpw=new String(con.readPassword("Password: "));
	        System.out.println(hpw);
	    }else{
	        System.out.println("Console is unavaliable");
	    }
	    return hpw;
	}
	
	
	public static void main(String argv[]) {
	    Passwd pw=new Passwd();
	    String pa=pw.hidePasswd();
	    System.out.println(pa);
	}
	
}
