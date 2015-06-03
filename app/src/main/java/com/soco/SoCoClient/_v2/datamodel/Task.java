package com.soco.SoCoClient._v2.datamodel;


import java.util.ArrayList;

public class Task {

    int taskIdLocal;
    int taskIdServer;
    String taskName;
    String taskPath;
    int isTaskActive;

    public Task(){
        this.taskIdLocal = -1;
    }

    public void save(){}

    public void delete(){}

    public ArrayList<Attribute> loadAttributes(){
        return null;
    }

    public void updateAttribute(Attribute attr){}

    public void clearAttributes(){}

    public ArrayList<Comment> loadComments(){
        return null;
    }

    public void addComment(Comment comment){}

    public ArrayList<Attachment> loadAttachments(){
        return null;
    }

    public void addAttachment(Attachment attachment){}



}
