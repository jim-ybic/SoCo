package com.soco.SoCoClient._v2.datamodel;


import java.util.ArrayList;

public class Contact {

    int contactIdLocal;
    int contactIdServer;
    String contactEmail;
    String contactUsername;

    public Contact(){
        this.contactIdLocal = -1;
    }

    public void save(){};

    public void addMessage(Message message){}

    public ArrayList<Message> loadMessages(){
        return null;
    }

}
