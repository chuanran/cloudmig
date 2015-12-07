package com.ibm.cloud.migration.heroku;

import java.io.FileWriter;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import com.heroku.api.Addon;
import com.heroku.api.App;
import com.heroku.api.Domain;
import com.heroku.api.HerokuAPI;
import com.heroku.api.Proc;
import com.heroku.api.Release;

public class HerokuAPIExample {

	HerokuAPI api = null;
	public HerokuAPIExample(){
		String username = "chuanran0723@gmail.com";
		String password = "sand_p1o9i8u7";
//		String apiKey = HerokuAPI.obtainApiKey(username, password);
		
		String apiKey = "3ce152a6-559a-43d3-81f3-b61a231deee2";
		api = new HerokuAPI(apiKey);
	}
	
	private JSONObject toJSON(Domain domain) throws JSONException{
		JSONObject obj = new JSONObject();
	
		if(domain!=null){
			obj.put("id", domain.getId());
			obj.put("baseDomain", domain.getBaseDomain());
			obj.put("default", domain.getDefault());
			obj.put("domain", domain.getDomain());
			obj.put("appId", domain.getAppId());		
		}			
		return obj;
	}
	
	private JSONObject toJSON(App app) throws JSONException{
		JSONObject json = new JSONObject();
		
		json.put("name", app.getName());
		json.put("id", app.getId());			
		json.put("buildpack_provided_description", app.getBuildpackProvidedDescription());
		json.put("webUrl", app.getWebUrl());
		json.put("workers", app.getWorkers());			
		json.put("dynos", app.getDynos());
		
		json.put("gitUrl", app.getGitUrl());			
		json.put("repo_size",app.getRepoSize()+" bytes");
		json.put("slug_size", app.getSlugSize()+" bytes");
		
		json.put("domain", toJSON(app.getDomain()));
		
		json.put("requestedStack", app.getRequestedStack());
		json.put("stack", app.getStack());
		
		json.put("createdAt", app.getCreatedAt());
		json.put("createStatus", app.getCreateStatus());
		json.put("ownerEmail", app.getOwnerEmail());
		json.put("releasedAt", app.getReleasedAt());
		json.put("repoMigrateStatus", app.getRepoMigrateStatus());
		return json;
	}
	

	
	private JSONObject toJSON(Addon addon) throws JSONException{
		JSONObject obj = new JSONObject();
		obj.put("beta", addon.getBeta());
		obj.put("configured", addon.getConfigured());
		obj.put("description", addon.getDescription());
		obj.put("id", addon.getId());
		obj.put("name",addon.getName());
		obj.put("priceCents", addon.getPriceCents());
		obj.put("priceUnit", addon.getPriceUnit());
		obj.put("state", addon.getState());
		obj.put("url", addon.getUrl());
		
		return obj;
	}
	
	private JSONArray toJSON(List<Addon> addons) throws JSONException{
		JSONArray arr = new JSONArray();
		for(Addon addon : addons){
			
			arr.put(toJSON(addon));
		}
		
		return arr;
	}
	
	private JSONObject toJSON(Map<String, String> map) throws JSONException{
		JSONObject obj = new JSONObject();
		for(Entry<String, String> entry : map.entrySet()){
			obj.put(entry.getKey(), entry.getValue());
		}
		
		return obj;
	}
	
	private JSONObject toJSON(Proc proc) throws JSONException{
		JSONObject obj = new JSONObject();
		
		
		obj.put("action", proc.getAction());
		obj.put("appName", proc.getAppName());
		obj.put("command",  proc.getCommand());
		obj.put("elapsed", proc.getElapsed());
		obj.put("prettyState",  proc.getPrettyState());
		obj.put("process",  proc.getProcess());
		obj.put("rendezvousUrl",  proc.getRendezvousUrl());
		obj.put("slug",  proc.getSlug());
		obj.put("state", proc.getState());
		obj.put("transitionedAt",  proc.getTransitionedAt());
		obj.put("type",  proc.getType());
		obj.put("upid", proc.getUpid());
		obj.put("isAttached",  proc.isAttached());
		
		return obj;
	}
	
	
	private JSONArray procList2JSON(List<Proc> procs) throws JSONException{
		JSONArray arr = new JSONArray();
		for(Proc proc : procs){
			arr.put(toJSON(proc));
		}
		
		return arr;
	}
	
	private JSONObject toJSON(Release release) throws JSONException{
		JSONObject obj = new JSONObject();
		
		JSONArray addons = new JSONArray();
		for(String addon : release.getAddons()){
			addons.put(addon);
		}
		obj.put("addons",  addons);		
		obj.put("commit", release.getCommit());
		obj.put("created",  release.getCreatedAt());
		obj.put("description", release.getDescription());
		obj.put("env", toJSON(release.getEnv()));
		obj.put("name", release.getName());
		obj.put("user", release.getUser());
		obj.put("pstable",  toJSON(release.getPSTable()));
		
		return obj;
	}
	private JSONArray releaseList2JSON(List<Release> releases) throws JSONException{
		JSONArray arr = new JSONArray();
		for(Release release : releases){
			arr.put(toJSON(release));
		}
		
		return arr;
	}
	
	public void listApps(){
		
		List<App> apps = api.listApps();
		for(App app : apps){
			
			
			try {
				JSONObject obj = toJSON(app);
				List<Addon> addons = api.listAppAddons(app.getName());
				obj.put("addons", toJSON(addons));
				
				Map<String, String> config = api.listConfig(app.getName());
				obj.put("configs", toJSON(config));
				
				List<Proc> procs = api.listProcesses(app.getName());
				obj.put("procs", procList2JSON(procs));
				
				List<Release> releases = api.listReleases(app.getName());
				obj.put("releases", releaseList2JSON(releases));
				
				
				System.out.println(obj.toString(4));
				
			} catch (JSONException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		
	
	}
	
	public void listAddons(String fileName) throws IOException{
		try {
			JSONArray obj = toJSON(api.listAllAddons());
			FileWriter writer = new FileWriter(fileName);
			writer.write(obj.toString(4));
			writer.close();
//			System.out.println(obj.toString(4));
		} catch (JSONException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	 
	/**
	 * @param args
	 */
	public static void main(String[] args) {
		HerokuAPIExample example = new HerokuAPIExample();
		example.listApps();

//		try {
////			example.listAddons("G:\\iCAP\\heroku\\data\\all_addons.json");
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
	}

}
