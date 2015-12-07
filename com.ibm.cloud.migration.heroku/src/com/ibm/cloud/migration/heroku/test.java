package com.ibm.cloud.migration.heroku;

import java.io.FileNotFoundException;
import java.util.Scanner;

import com.ibm.cloud.migration.appmodel.AppModelException;


public class test {
	
public static void main(String[] args){
		
		System.out.println("Enter heroku credentails: ");
		System.out.println("Email: ");
		Scanner readers=new Scanner(System.in);
		String username = readers.nextLine();
		System.out.println("Password: ");
		String password =readers.nextLine();
		Exporter exporter = new Exporter();
		try {
			exporter.export(username, password, "hello.xml");
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (AppModelException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
}

}
