package com.soco.app.test;

import java.util.Collection;
import java.util.Iterator;

import com.hazelcast.client.HazelcastClient;
import com.hazelcast.client.config.ClientConfig;
import com.hazelcast.core.EntryEvent;
import com.hazelcast.core.HazelcastInstance;
import com.hazelcast.core.IMap;
import com.hazelcast.core.MapEvent;
import com.hazelcast.map.listener.EntryAddedListener;
import com.hazelcast.map.listener.EntryEvictedListener;
import com.hazelcast.map.listener.EntryRemovedListener;
import com.hazelcast.map.listener.EntryUpdatedListener;
import com.hazelcast.map.listener.MapClearedListener;
import com.hazelcast.map.listener.MapEvictedListener;
import com.hazelcast.query.SqlPredicate;
import com.soco.user.User;

public class TestClient {

	public static void main(String[] args){
        ClientConfig clientConfig = new ClientConfig();
        HazelcastInstance client = HazelcastClient.newHazelcastClient(clientConfig);
        IMap<Long, User> map = client.getMap((new User()).getTableName());
        map.addEntryListener(new MyEntryListener(), true);
        System.out.println("Map size:" + map.size());
        map.put((long) 1, new User());
        
        for(int i = 1; i <= map.size(); i++){
            User c = (User)map.get((long)i);
            if(c != null){
                System.out.println("--- Name Card ---");
                System.out.println(c.getEmail());
                System.out.println("");
            }
            System.out.println(i+" Map size:" + map.size());
        }
        
        //
        //IMap<Customer> mapCustomer = client.getMap("customers");
       /*
        Collection<User> customers = map.values(new SqlPredicate("_age < 23"));
        System.out.println("Set size:" + customers.size());
        Iterator<User> ite = customers.iterator();
        while(ite.hasNext()){
        	User c = ite.next();
            if(c != null){
                System.out.println("--- Name Card in set---");
                System.out.println(c.getEmail());
                System.out.println("");
            }
        }
        */
        /*
        map.put((long) (map.size() + 1), new Customer());
        System.out.println("Map size:" + map.size());
        for(int i = 1; i <= map.size(); i++){
            Customer c = (Customer)map.get((long)i);
            if(c != NULL){
                System.out.println("--- Name Card ---");
                System.out.println(c.getNameCard());
                System.out.println("");
            }
        }
        */
    }
    
    
    static class MyEntryListener implements EntryAddedListener<String, String>, 
                                            EntryRemovedListener<String, String>, 
                                            EntryUpdatedListener<String, String>, 
                                            EntryEvictedListener<String, String> , 
                                            MapEvictedListener, 
                                            MapClearedListener   {
        @Override
        public void entryAdded( EntryEvent<String, String> event ) {
            System.out.println( "Entry Added:" + event );
        }
        
        @Override
        public void entryRemoved( EntryEvent<String, String> event ) {
            System.out.println( "Entry Removed:" + event );
        }
        
        @Override
        public void entryUpdated( EntryEvent<String, String> event ) {
            System.out.println( "Entry Updated:" + event );
        }
        
        @Override
        public void entryEvicted( EntryEvent<String, String> event ) {
            System.out.println( "Entry Evicted:" + event );
        }
        
        @Override
        public void mapEvicted( MapEvent event ) {
            System.out.println( "Map Evicted:" + event );
        }
        
        @Override
        public void mapCleared( MapEvent event ) {
            System.out.println( "Map Cleared:" + event );
        }

    }
}
