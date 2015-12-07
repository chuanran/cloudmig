var getMongoUrl = function(){
      if(process.env.COUNTLYDB){
           return process.env.COUNTLYDB;
       } else if(process.env.VCAP_SERVICES){
			var env = JSON.parse(process.env.VCAP_SERVICES);
			if(!env){
				throw new Error("environment VCAP_SERVICES is not a valid JSON object");
			}
			var mongo_arr = env["mongodb-2.2"];
			if(!(mongo_arr instanceof Array) || mongo_arr.length==0){
				throw new Error("cannot find MongoDB service in VCAP_SERVICES");
			}
			for(var i in mongo_arr){
				var mongo_item = mongo_arr[i];
				mongo = mongo_item.credentials;	
				if(mongo.url){
					return mongo.url;
				}else{
					if(mongo.username && mongo.password){		
				 		return "mongodb://" + mongo.username + ":" + mongo.password + "@"
					    	+ mongo.hostname + ":" + mongo.port + "/" + mongo.db;
				 	}else{
				 		return "mongodb://" + mongo.hostname + ":" + mongo.port + "/" + mongo.db;
				 	}
				}
			}
		}
}


var countlyConfig = {
    mongodb: getMongoUrl(),
    api: {
        workers: 0,
        port: process.env.PORT,
        safe: false,
        session_duration_limit: 120,
        max_sockets: 1024,
        city_data: true
    },
    apps: {
        country: "US",
        timezone: "America/Los_Angeles",
        category: "6"
    }
};

// Set your host IP or domain to be used in the emails sent
countlyConfig.host = process.env.EMAIL_DOMAIN;

module.exports = countlyConfig;
