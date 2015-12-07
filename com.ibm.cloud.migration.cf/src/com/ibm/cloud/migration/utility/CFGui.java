package com.ibm.cloud.migration.utility;

import java.awt.Frame;
import java.awt.GridLayout;
import java.awt.Label;
import java.awt.Panel;
import java.awt.TextField;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.MalformedURLException;

import com.ibm.cloud.migration.cf.Exporter;
import com.ibm.cloud.migration.appmodel.AppModelException;

public class CFGui {
	
	final Frame frame=new Frame();
	Panel panel=new Panel();	
	String defaultUrl = "http://api.ng.bluemix.net";
	Label title=new Label("**********Capture the application model from Cloud Foundry**********");
	Label deUrl=new Label("Enter Cloud Foundry Target URL (default: "+defaultUrl+")");
	Label user=new Label("Email: ");
	Label pw=new Label("Password: ");
	Label look=new Label("Would you like to take a look at the application model?(Y/N)");
	Label contents=new Label("The contents of captured application model:\n");
	Label app_model=new Label("");
	
	final TextField txtuser=new	TextField("",20);
	final TextField txtpw=new TextField("",20);
	final TextField txtlook=new TextField("",20);

	public CFGui() {
		// TODO Auto-generated constructor stub
	}
	
	public void init() {
		panel.setLayout(new GridLayout(20,1));
		txtpw.setEchoChar('*');
		panel.add(title);
		panel.add(deUrl);
		panel.add(user);
		panel.add(txtuser);
		panel.add(pw);
		panel.add(txtpw);
		frame.add(panel);		
		frame.setTitle("title");
		frame.pack();
		frame.setVisible(true);
		
		txtpw.addKeyListener(new KeyListener() {  
	        public void keyPressed(KeyEvent e) {  
	            if (e.getKeyCode() == KeyEvent.VK_ENTER) { 
	            	String targetUrl = defaultUrl;
	            	String username = txtuser.getText();
	            	String password=txtpw.getText();
	            	String outfile = "application_model.xml";	    			
	    			try {
	    				Exporter exporter = new Exporter();
						exporter.export(targetUrl, username, password, outfile);
						panel.add(look);
						panel.add(txtlook);
						frame.pack();
		        		frame.setVisible(true);
					} catch (FileNotFoundException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (MalformedURLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					} catch (AppModelException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}	       		
	            }  
	        }  
	        public void keyReleased(KeyEvent e) {  
	        }  
	        public void keyTyped(KeyEvent e) {  
	        }  
	    });  
		
		txtlook.addKeyListener(new KeyListener() {  
	        public void keyPressed(KeyEvent e) {  
	        	String outfile = "application_model.xml";
	            if (e.getKeyCode() == KeyEvent.VK_ENTER) { 
	            	if(txtlook.getText().equalsIgnoreCase("Y")){
	            		panel.add(contents);
	            		frame.pack();
		        		frame.setVisible(true);
	    				//Display the content of the application model
	    				try{
	    					BufferedReader in = new BufferedReader(new FileReader(outfile));
	    					String app_model_str;
	    			        while ((app_model_str = in.readLine()) != null) 
	    			        {
	    			        	//app_model.
	    			              System.out.println(app_model_str);
	    			        }
	    			        in.close();
	    				} catch (IOException ex) {
	    					ex.printStackTrace();
	    				}
	    			}
	        		
	            }  
	        }  
	        public void keyReleased(KeyEvent e) {  
	        }  
	        public void keyTyped(KeyEvent e) {  
	        }  
	    });  
		
		
	}
	

	public static void main(String[] args) {
		CFGui jtt=new CFGui();
		jtt.init();
		
	}
	
}
