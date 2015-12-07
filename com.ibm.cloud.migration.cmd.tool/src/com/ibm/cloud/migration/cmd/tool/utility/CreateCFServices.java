package com.ibm.cloud.migration.cmd.tool.utility;

import java.util.List;

import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.cloudfoundry.client.lib.domain.CloudService;
import org.cloudfoundry.client.lib.domain.CloudServiceOffering;
import org.cloudfoundry.client.lib.domain.CloudServicePlan;
import org.cloudfoundry.client.lib.domain.CloudSpace;

import com.ibm.cloud.migration.appmodel.model.ServiceNode;

public class CreateCFServices {
	
	public static void createServices(List<ServiceNode> services, CloudFoundryClient cfClient, CloudSpace space){
		
		
		List<CloudServiceOffering> service_offerings = cfClient.getServiceOfferings();
		
		
		CloudService cloudservice = new CloudService();
		
		if(services!=null && services.size()>0){
			for(ServiceNode service : services){
				CloudServiceOffering selServiceOffering = MapToCFService.selectServiceOffering(service, service_offerings);
				cloudservice.setName(service.getName());
				CloudServicePlan service_plan = MapToCFService.selectServicePlan(selServiceOffering);
				cloudservice.setLabel(selServiceOffering.getLabel());
				cloudservice.setMeta(space.getMeta());
				cloudservice.setPlan(service_plan.getName());
				cloudservice.setVersion(selServiceOffering.getVersion());
				System.out.println("Creating service instance: " + service.getName());
				cfClient.createService(cloudservice);
			}
		}
			
	}

}
