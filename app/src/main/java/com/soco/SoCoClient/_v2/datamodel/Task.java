package com.soco.SoCoClient._v2.datamodel;


import com.soco.SoCoClient._v2.businesslogic.config.DbConfig;

import java.util.ArrayList;

public class Task {

    int taskIdLocal;
    int taskIdServer;
    String taskName;
    String taskPath;
    int isTaskActive;

    public Task(){
        this.taskIdLocal = DbConfig.ENTITIY_ID_NOT_READY;
        this.taskIdServer = DbConfig.ENTITIY_ID_NOT_READY;
    }

    public void save(){}

    public void delete(){}

    public void addMember(Contact contact){}

    public ArrayList<Contact> loadMembers(){
        return null;
    }

    public void updateAttribute(Attribute attr){}

    public void clearAttributes(){}

    public ArrayList<Attribute> loadAttributes(){
        return null;
    }

    public void addComment(Comment comment){}

    public ArrayList<Comment> loadComments(){
        return null;
    }

    public void addAttachment(Attachment attachment){}

    public ArrayList<Attachment> loadAttachments(){
        return null;
    }




}
