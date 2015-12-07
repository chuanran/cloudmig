if(process.env.VCAP_SERVICES){
	var env = JSON.parse(process.env.VCAP_SERVICES);
	if(!env){
		throw new Error("environment VCAP_SERVICES is not a valid JSON object");
	}
	var service_arr = env["mongodb-2.2"];
	if(!(service_arr instanceof Array) || service_arr.length==0){
		throw new Error("cannot find corresponding service in VCAP_SERVICES");
	}
	for(var i in service_arr){
		var service_item = service_arr[i];
		service = service_item.credentials;	
		if(service.url){
			process.env.COUNTLYDB = service.url;
		}else{
			if(service.username && service.password){		
				 process.env.COUNTLYDB = "mongodb://" + service.username + ":" + service.password + "@"
					   + service.hostname + ":" + service.port + "/" + service.db;
		    }else{
				 process.env.COUNTLYDB = "mongodb://" + service.hostname + ":" + service.port + "/" + service.db;
			}
		}
	}
}
