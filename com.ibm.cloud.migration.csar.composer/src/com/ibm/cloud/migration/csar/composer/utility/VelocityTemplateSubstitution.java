package com.ibm.cloud.migration.csar.composer.utility;

import java.io.StringWriter;
import java.util.Properties;

import org.apache.velocity.app.Velocity;
import org.apache.velocity.Template;
import org.apache.velocity.VelocityContext;

public class VelocityTemplateSubstitution {
	
	private Properties p;
	private String template_file_name;
	private String sourcestr;
	private String targetstr;
	
	//instantiate this class
	public VelocityTemplateSubstitution(Properties p, String template_file_name, String sourcestr, String targetstr){
		this.p = p;
		this.template_file_name = template_file_name;
		this.sourcestr = sourcestr;
		this.targetstr = targetstr;
	}
	
	public String substituteTemplate() throws Exception{
		Velocity.init(p);
		Template t = Velocity.getTemplate(template_file_name);
		/*  create a context and add data */
		VelocityContext context = new VelocityContext();
		context.put(sourcestr, targetstr);
		/* now render the template into a StringWriter */
		StringWriter writer = new StringWriter();
		t.merge(context, writer);
		return writer.toString();
	}
}
