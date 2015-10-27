package com.soco.SoCoClient.common.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.soco.SoCoClient.common.model.Contact;
import com.soco.SoCoClient.secondary.chat.model.SingleConversation;
import com.soco.SoCoClient.events.model.EventV1;
import com.soco.SoCoClient.secondary.chat.model.Message;
import com.soco.SoCoClient.common.model.Person;
import com.soco.SoCoClient.common.model.Task;

import java.util.ArrayList;
import java.util.HashSet;

public class DataLoader {

    String tag = "DataLoader";

    Context context;
    SQLiteDatabase db;
    DbHelper dbHelper;

    public DataLoader(Context context){
        this.context = context;
        this.dbHelper = new DbHelper(context);
//        this.db = dbHelper.getWritableDatabase();
    }

    public ArrayList<Task> loadActiveTasks(){
        Log.v(tag, "load active tasks from database");
        db = dbHelper.getWritableDatabase();
        
        ArrayList<Task> tasks = new ArrayList<>();
        Cursor cursor = db.rawQuery(
                "select * from " + Config.TABLE_TASK
                        + " where " + Config.COLUMN_TASK_ISTASKACTIVE + " = ?",
                new String[]{String.valueOf(Config.TASK_IS_ACTIVE)});

        while(cursor.moveToNext()){
            Task task = new Task(cursor);
            Log.d(tag, "load task from database: " + task.toString());
            tasks.add(task);
        }

        Log.d(tag, tasks.size() + " tasks loaded from database");
        db.close();
        return tasks;
    }

    public ArrayList<EventV1> loadEvents(){
        Log.v(tag, "load all events from db");
        db = dbHelper.getWritableDatabase();

        String query = "select * from " + Config.TABLE_EVENT;
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<EventV1> eventV1s = new ArrayList<>();
        while(cursor.moveToNext()){
            EventV1 e = new EventV1(this.context, cursor);
            Log.v(tag, "loaded event from db: " + e.toString());
            eventV1s.add(e);
        }

        Log.d(tag, eventV1s.size() + " events loaded from db");
        db.close();
        return eventV1s;
    }

    public EventV1 loadEvent(int seq){
        Log.v(tag, "load event from db for seq " + seq);
        db = dbHelper.getWritableDatabase();

        String query = "select * from " + Config.TABLE_EVENT
                + " where " + Config.COLUMN_EVENT_SEQ + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(seq)});

        while(cursor.moveToNext()){
            EventV1 e = new EventV1(context, cursor);
            Log.v(tag, "loaded event from db: " + e.toString());
            return e;
        }

        Log.d(tag, "event not found");
        db.close();
        return null;
    }

    public ArrayList<Person> loadPersons(){
        Log.v(tag, "load all persons from db");
        db = dbHelper.getWritableDatabase();

        String query = "select * from " + Config.TABLE_PERSON;
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<Person> persons = new ArrayList<>();
        while(cursor.moveToNext()){
            Person p = new Person(context, cursor);
            Log.v(tag, "loaded person from db: " + p.toString());
            persons.add(p);
        }

        Log.d(tag, persons.size() + " persons loaded from db");
        db.close();
        return persons;
    }

    public ArrayList<Person> loadFriends(){
        Log.v(tag, "load all friends from db");
        db = dbHelper.getWritableDatabase();
        
        String query = "select * from " + Config.TABLE_PERSON
                + " where " + Config.COLUMN_PERSON_CATEGORY + " = ?";
        Cursor cursor = db.rawQuery(query,
                new String[]{Config.CONTACT_LIST_SECTION_MYFRIENDS});

        ArrayList<Person> persons = new ArrayList<>();
        while(cursor.moveToNext()){
            Person p = new Person(context, cursor);
            Log.v(tag, "loaded friends from db: " + p.toString());
            persons.add(p);
        }
//        db.close();

        Log.d(tag, persons.size() + " friends loaded from db");
        db.close();
        return persons;
    }

    public ArrayList<SingleConversation> loadSingleConversations(){
        Log.v(tag, "load conversation from db ");
        db = dbHelper.getWritableDatabase();

        ArrayList<SingleConversation> conversations = new ArrayList<>();

        String query = "select * from " + Config.TABLE_SINGLE_CONVERSATION;
        Log.d(tag, "loadSingleConversations: " + query);
        Cursor cursor = db.rawQuery(query, null);

        while(cursor.moveToNext()){
            SingleConversation c = new SingleConversation(context, cursor);
            Log.v(tag, "loaded conversation from db: " + c.toString());
            conversations.add(c);
        }

        Log.d(tag, conversations.size() + " conversations found");
        db.close();
        return conversations;
    }

    public SingleConversation loadSingleConversationByCtpySeq(int counterpartySeq){
        Log.v(tag, "load conversation from db for counterpartySeq " + counterpartySeq);
        db = dbHelper.getWritableDatabase();

        String query = "select * from " + Config.TABLE_SINGLE_CONVERSATION
                + " where " + Config.COLUMN_SINGLE_CONVERSATION_COUNTERPARTYID + " = ?";
        Log.d(tag, "loadSingleConversationByCtpySeq: " + query + ", " + counterpartySeq);
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(counterpartySeq)});

        while(cursor.moveToNext()){
            SingleConversation c = new SingleConversation(context, cursor);
            Log.v(tag, "loaded conversation from db: " + c.toString());
            return c;
        }

        Log.d(tag, "conversation not found");
        db.close();
        return null;
    }

    public SingleConversation loadSingleConversationBySeq(int seq){
        Log.v(tag, "load conversation from db for seq " + seq);
        db = dbHelper.getWritableDatabase();

        String query = "select * from " + Config.TABLE_SINGLE_CONVERSATION
                + " where " + Config.COLUMN_SINGLE_CONVERSATION_SEQ + " = ?";
        Log.d(tag, "query db: " + query + ", " + seq);
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(seq)});

        while(cursor.moveToNext()){
            SingleConversation c = new SingleConversation(context, cursor);
            Log.v(tag, "loaded conversation from db: " + c.toString());
            return c;
        }

        Log.d(tag, "conversation not found");
        db.close();
        return null;
    }

    public ArrayList<Person> loadPhoneContacts(){
        Log.v(tag, "load all phone contacts from db");
        db = dbHelper.getWritableDatabase();

        String query = "select * from " + Config.TABLE_PERSON
                + " where " + Config.COLUMN_PERSON_CATEGORY + " = ?";
        Cursor cursor = db.rawQuery(query,
                new String[]{Config.CONTACT_LIST_SECTION_MYPHONECONTACTS});
        Log.v(tag, "loadPhoneContacts: " + query);

        ArrayList<Person> persons = new ArrayList<>();
        while(cursor.moveToNext()){
            Person p = new Person(context, cursor);
            Log.v(tag, "loaded phone contact from db: " + p.toString());
            persons.add(p);
        }

        Log.d(tag, persons.size() + " phone contact loaded from db");
        db.close();
        return persons;
    }


    public ArrayList<Task> loadInactiveTasks(){
        return null;
    }

    public Task loadTaskByIdLocal(int idLocal){
        return null;
    }

    public Task loadTaskByIdServer(int idServer){
        return null;
    }

    public Task loadTaskByPath(String path){
        return null;
    }

    public ArrayList<Contact> loadContacts(){
        Log.v(tag, "load contacts from database");
        db = dbHelper.getWritableDatabase();

        ArrayList<Contact> contacts = new ArrayList<>();
        String query =  "select * from " + Config.TABLE_CONTACT;
        Cursor cursor = db.rawQuery(query, null);

        while(cursor.moveToNext()){
            Contact contact = new Contact(cursor);
            Log.d(tag, "load contact from database: " + contact.toString());
            contacts.add(contact);
        }

        Log.d(tag, contacts.size() + " contacts loaded from database");
        db.close();
        return contacts;
    }

    public ArrayList<Contact> loadContacts(HashSet<Integer> contactIdLocalSet){
        Log.v(tag, "load contacts from database for a given set of id local");
        db = dbHelper.getWritableDatabase();

        ArrayList<Contact> contacts = new ArrayList<>();
        String query =  "select * from " + Config.TABLE_CONTACT;
        Cursor cursor = db.rawQuery(query, null);

        while(cursor.moveToNext()){
            Contact contact = new Contact(cursor);
            Log.v(tag, "current contact: " + contact.toString());
            if(contactIdLocalSet.contains(contact.getContactIdLocal())) {
                Log.d(tag, "load contact from database: " + contact.toString());
                contacts.add(contact);
            }
        }

        Log.d(tag, contacts.size() + " contacts loaded from database for id local set: " + contactIdLocalSet);
        db.close();
        return contacts;
    }

    public ArrayList<Message> loadMessages(){
        Log.v(tag, "load messages from database");
        db = dbHelper.getWritableDatabase();

        ArrayList<Message> messages = new ArrayList<>();
        String query =  "select * from " + Config.TABLE_MESSAGE;
        Cursor cursor = db.rawQuery(query, null);

        while(cursor.moveToNext()){
            Message message = new Message(context, cursor);
            Log.d(tag, "load message from database: " + message.toString());
            messages.add(message);
        }

        Log.d(tag, messages.size() + " message loaded from database");
        db.close();
        return messages;
    }



    public ArrayList<Message> loadMessagesForSingleConversation(int singleConversationSeq){
        Log.v(tag, "load messages from database for single conversation " + singleConversationSeq);
        db = dbHelper.getWritableDatabase();

        ArrayList<Message> messages = new ArrayList<>();

        Log.v(tag, "get associated message id list");
        String queryMsgId = "select " + Config.COLUMN_SINGLE_CONVERSATION_MESSAGE_MSGSEQ
                        + " from " + Config.TABLE_SINGLE_CONVERSATION_MESSAGE
                        + " where " + Config.COLUMN_SINGLE_CONVERSATION_MESSAGE_CONSEQ + " =?";
        Cursor cursorMsgId = db.rawQuery(queryMsgId,
                new String[]{String.valueOf(singleConversationSeq)});
        Log.d(tag, "queryMsgId: " + queryMsgId + ", " + singleConversationSeq);
        ArrayList<String> msgIds = new ArrayList<>();
        while (cursorMsgId.moveToNext()) {
            int id = cursorMsgId.getInt(0);
            Log.v(tag, "get message " + id);
            msgIds.add(String.valueOf(id));
        }
        String [] msgIdsStr = msgIds.toArray(new String[msgIds.size()]);
        Log.d(tag, msgIds.size() + " messages found for single conversation " + singleConversationSeq);

        if(msgIds.size() == 0)
            return messages;

        Log.v(tag, "load messages");
        String queryMsg =  "select * from " + Config.TABLE_MESSAGE
                            + " where " + Config.COLUMN_MESSAGE_SEQ + " in ("
                            + makePlaceholders(msgIdsStr.length) + ")";
        Cursor cursorMsg = db.rawQuery(queryMsg, msgIdsStr);
        Log.d(tag, "queryMsg: " + queryMsg + ", " + msgIdsStr.toString());

        while(cursorMsg.moveToNext()){
            Message message = new Message(context, cursorMsg);
            Log.d(tag, "load message from database: " + message.toString());
            messages.add(message);
        }

        Log.d(tag, messages.size() + " message loaded from database");
        db.close();
        return messages;
    }

    String makePlaceholders(int len) {
        if (len < 1) {
            // It will lead to an invalid query anyway ..
            throw new RuntimeException("No placeholders");
        } else {
            StringBuilder sb = new StringBuilder(len * 2 - 1);
            sb.append("?");
            for (int i = 1; i < len; i++) {
                sb.append(",?");
            }
            return sb.toString();
        }
    }

}
