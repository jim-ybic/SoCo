package com.soco.app.main;

import java.util.Map;

import com.hazelcast.config.Config;
import com.hazelcast.config.MapConfig;
import com.hazelcast.config.MapStoreConfig;
import com.hazelcast.config.XmlConfigBuilder;
import com.hazelcast.core.Hazelcast;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.MapStore;
import com.soco.event.Event;
import com.soco.mapstore.event.EventMapStore;
import com.soco.mapstore.user.RoleMapStore;
import com.soco.mapstore.user.UserMapStore;
import com.soco.table.BaseTable;
import com.soco.user.Role;
import com.soco.user.User;


public class ServerStart {
	

	public static void main(String[] args){
		final Config config = createNewConfig();
		HazelcastInstance hazelcastInstance = Hazelcast.newHazelcastInstance(config);
		
		Map<Long, User> userMap = hazelcastInstance.getMap((new User()).getTableName());
		System.out.println("User Map size is " + userMap.size());
	}
	
	/* generate the config file for hazelcast server */
	private static Config createNewConfig() {
       /* XmlConfigBuilder configBuilder = new XmlConfigBuilder();
        Config config = configBuilder.build();
        
         user map store for user table information 
        MapConfig userMapConfig = config.getMapConfig(User.getTableName());
        MapStoreConfig userMapStoreConfig = new MapStoreConfig();
        userMapStoreConfig.setImplementation(new UserMapStore());
        userMapStoreConfig.setWriteDelaySeconds(0);
        userMapConfig.setMapStoreConfig(userMapStoreConfig);
        */
        /* user map store for user table information
        MapConfig roleMapConfig = config.getMapConfig(Role.getTableName());
        MapStoreConfig roleMapStoreConfig = new MapStoreConfig();
        roleMapStoreConfig.setImplementation(new RoleMapStore());
        roleMapStoreConfig.setWriteDelaySeconds(0);
        roleMapConfig.setMapStoreConfig(roleMapStoreConfig);
         */
		Config config = null;
        ServerStart ss = new ServerStart();
        ConfigFactory cu = ss.new ConfigFactory();
        
        /* user */
        User user = new User();
        config = cu.buildConfig(config, user.getTableCreateSQL(), new UserMapStore());
        /* role */
        Role role = new Role();
        config = cu.buildConfig(config, role.getTableCreateSQL(), new RoleMapStore());
        /* task */
        Event event = new Event();
        config = cu.buildConfig(config, event.getTableCreateSQL(), new EventMapStore());
        
        return config;
    }
	
	private class ConfigFactory {
		public Config buildConfig(Config config, String tableName, MapStore ms){
			if(null == config){
				XmlConfigBuilder configBuilder = new XmlConfigBuilder();
		        config = configBuilder.build();
			}
			
			MapConfig tableMapConfig = config.getMapConfig(tableName);
	        MapStoreConfig tableMapStoreConfig = new MapStoreConfig();
	        tableMapStoreConfig.setImplementation(ms);
	        tableMapStoreConfig.setWriteDelaySeconds(0);
	        tableMapConfig.setMapStoreConfig(tableMapStoreConfig);
			
			return config;
		}
	}
}
