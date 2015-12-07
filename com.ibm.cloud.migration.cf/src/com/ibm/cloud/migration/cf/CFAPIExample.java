

package com.ibm.cloud.migration.cf;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.cloudfoundry.client.lib.CloudCredentials;
import org.cloudfoundry.client.lib.CloudFoundryClient;
import org.cloudfoundry.client.lib.domain.CloudApplication;
import org.cloudfoundry.client.lib.domain.CloudService;
import org.springframework.security.oauth2.common.OAuth2AccessToken;


public class CFAPIExample {

	private Map<String, CloudService> services = null;

	private CloudService getCloudService(CloudFoundryClient client,
			String serviceName) {
		if (services == null) {
			services = new HashMap<String, CloudService>();
			List<CloudService> list = client.getServices();
			for (CloudService cs : list) {
				services.put(cs.getName(), cs);
			}
		}
		return services.get(serviceName);
	}

	public void listApps() {
		String email = "ranchuan@cn.ibm.com";
		String password = "sand070923";
		String url = "http://api.ng.bluemix.net";
		CloudCredentials cred = new CloudCredentials(email, password);
		try {
			CloudFoundryClient client = new CloudFoundryClient(cred, new URL(
					url));
			OAuth2AccessToken token = client.login();
			// String content = client.getFile("hellomongo", 0, "app/app.js");
			 //System.out.println(token);
			client.getApplications();
			 System.out.println(client.getCloudInfo().getVersion());
			 List<CloudApplication> apps = client.getApplications();
			 for(CloudApplication app : apps){
			 System.out.println(app.getName()+","+app.getUris().get(0));
			
			 List<String> services = app.getServices();
			 for(String name : services){
			 CloudService cloudService = getCloudService(client, name);
			 System.out.println("==" +
			 cloudService.getName()+","+cloudService.getLabel()+","+cloudService.getProvider());
			 }
			 }
		} catch (MalformedURLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		CFAPIExample api = new CFAPIExample();
		api.listApps();

	}

}

