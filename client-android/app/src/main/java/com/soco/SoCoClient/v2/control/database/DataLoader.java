package com.soco.SoCoClient.v2.control.database;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.soco.SoCoClient.v2.control.config.DataConfig;
import com.soco.SoCoClient.v2.model.Contact;
import com.soco.SoCoClient.v2.model.conversation.SingleConversation;
import com.soco.SoCoClient.v2.model.Event;
import com.soco.SoCoClient.v2.model.Message;
import com.soco.SoCoClient.v2.model.Person;
import com.soco.SoCoClient.v2.model.Task;
import com.soco.SoCoClient.v2.model.conversation.SingleConversation;

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
        this.db = dbHelper.getWritableDatabase();
    }

    public ArrayList<Task> loadActiveTasks(){
        Log.v(tag, "load active tasks from database");
        ArrayList<Task> tasks = new ArrayList<>();
        Cursor cursor = db.rawQuery(
                "select * from " + DataConfig.TABLE_TASK
                        + " where " + DataConfig.COLUMN_TASK_ISTASKACTIVE + " = ?",
                new String[]{String.valueOf(DataConfig.TASK_IS_ACTIVE)});

        while(cursor.moveToNext()){
            Task task = new Task(cursor);
            Log.d(tag, "load task from database: " + task.toString());
            tasks.add(task);
        }

        Log.d(tag, tasks.size() + " tasks loaded from database");
        return tasks;
    }

    public ArrayList<Event> loadEvents(){
        Log.v(tag, "load all events from db");
        String query = "select * from " + DataConfig.TABLE_EVENT;
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<Event> events = new ArrayList<>();
        while(cursor.moveToNext()){
            Event e = new Event(this.context, cursor);
            Log.v(tag, "loaded event from db: " + e.toString());
            events.add(e);
        }

        Log.d(tag, events.size() + " events loaded from db");
        return events;
    }

    public Event loadEvent(int seq){
        Log.v(tag, "load event from db for seq " + seq);
        String query = "select * from " + DataConfig.TABLE_EVENT
                + " where " + DataConfig.COLUMN_EVENT_SEQ + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(seq)});

        while(cursor.moveToNext()){
            Event e = new Event(context, cursor);
            Log.v(tag, "loaded event from db: " + e.toString());
            return e;
        }

        Log.d(tag, "event not found");
        return null;
    }

    public ArrayList<Person> loadPersons(){
        Log.v(tag, "load all persons from db");
        String query = "select * from " + DataConfig.TABLE_PERSON;
        Cursor cursor = db.rawQuery(query, null);

        ArrayList<Person> persons = new ArrayList<>();
        while(cursor.moveToNext()){
            Person p = new Person(context, cursor);
            Log.v(tag, "loaded person from db: " + p.toString());
            persons.add(p);
        }

        Log.d(tag, persons.size() + " persons loaded from db");
        return persons;
    }

    public ArrayList<Person> loadFriends(){
        Log.v(tag, "load all friends from db");
        String query = "select * from " + DataConfig.TABLE_PERSON
                + " where " + DataConfig.COLUMN_PERSON_CATEGORY + " = ?";
        Cursor cursor = db.rawQuery(query,
                new String[]{DataConfig.CONTACT_LIST_SECTION_MYFRIENDS});

        ArrayList<Person> persons = new ArrayList<>();
        while(cursor.moveToNext()){
            Person p = new Person(context, cursor);
            Log.v(tag, "loaded friends from db: " + p.toString());
            persons.add(p);
        }
//        db.close();

        Log.d(tag, persons.size() + " friends loaded from db");
        return persons;
    }

    public SingleConversation loadSingleConversationByCtpyId(int counterpartyId){
        Log.v(tag, "load conversation from db for counterpartyId " + counterpartyId);
        String query = "select * from " + DataConfig.TABLE_SINGLE_CONVERSATION
                + " where " + DataConfig.COLUMN_SINGLE_CONVERSATION_COUNTERPARTYID + " = ?";
        Log.d(tag, "query db: " + query + ", " + counterpartyId);
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(counterpartyId)});

        while(cursor.moveToNext()){
            SingleConversation c = new SingleConversation(context, cursor);
            Log.v(tag, "loaded conversation from db: " + c.toString());
            return c;
        }

        Log.d(tag, "conversation not found");
        return null;
    }

    public SingleConversation loadSingleConversationBySeq(int seq){
        Log.v(tag, "load conversation from db for seq " + seq);
        String query = "select * from " + DataConfig.TABLE_SINGLE_CONVERSATION
                + " where " + DataConfig.COLUMN_SINGLE_CONVERSATION_SEQ + " = ?";
        Log.d(tag, "query db: " + query + ", " + seq);
        Cursor cursor = db.rawQuery(query, new String[]{String.valueOf(seq)});

        while(cursor.moveToNext()){
            SingleConversation c = new SingleConversation(context, cursor);
            Log.v(tag, "loaded conversation from db: " + c.toString());
            return c;
        }

        Log.d(tag, "conversation not found");
        return null;
    }

    public ArrayList<Person> loadPhoneContacts(){
        Log.v(tag, "load all phone contacts from db");
//        if(db == null)
            db = dbHelper.getWritableDatabase();

        String query = "select * from " + DataConfig.TABLE_PERSON
                + " where " + DataConfig.COLUMN_PERSON_CATEGORY + " = ?";
        Cursor cursor = db.rawQuery(query,
                new String[]{DataConfig.CONTACT_LIST_SECTION_MYPHONECONTACTS});

        ArrayList<Person> persons = new ArrayList<>();
        while(cursor.moveToNext()){
            Person p = new Person(context, cursor);
            Log.v(tag, "loaded phone contact from db: " + p.toString());
            persons.add(p);
        }

        Log.d(tag, persons.size() + " phone contact loaded from db");
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
        ArrayList<Contact> contacts = new ArrayList<>();
        String query =  "select * from " + DataConfig.TABLE_CONTACT;
        Cursor cursor = db.rawQuery(query, null);

        while(cursor.moveToNext()){
            Contact contact = new Contact(cursor);
            Log.d(tag, "load contact from database: " + contact.toString());
            contacts.add(contact);
        }

        Log.d(tag, contacts.size() + " contacts loaded from database");
        return contacts;
    }

    public ArrayList<Contact> loadContacts(HashSet<Integer> contactIdLocalSet){
        Log.v(tag, "load contacts from database for a given set of id local");
        ArrayList<Contact> contacts = new ArrayList<>();
        String query =  "select * from " + DataConfig.TABLE_CONTACT;
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
        return contacts;
    }

    public ArrayList<Message> loadMessages(){
        Log.v(tag, "load messages from database");
        ArrayList<Message> messages = new ArrayList<>();
        String query =  "select * from " + DataConfig.TABLE_MESSAGE;
        Cursor cursor = db.rawQuery(query, null);

        while(cursor.moveToNext()){
            Message message = new Message(context, cursor);
            Log.d(tag, "load message from database: " + message.toString());
            messages.add(message);
        }

        Log.d(tag, messages.size() + " message loaded from database");
        return messages;
    }



    public ArrayList<Message> loadMessagesForSingleConversation(int singleConversationSeq){
        Log.v(tag, "load messages from database for single conversation " + singleConversationSeq);
        ArrayList<Message> messages = new ArrayList<>();

        Log.v(tag, "get associated message id list");
        String queryMsgId = "select " + DataConfig.COLUMN_SINGLE_CONVERSATION_MESSAGE_MSGSEQ
                        + " from " + DataConfig.TABLE_SINGLE_CONVERSATION_MESSAGE
                        + " where " + DataConfig.COLUMN_SINGLE_CONVERSATION_MESSAGE_CONSEQ + " =?";
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
        String queryMsg =  "select * from " + DataConfig.TABLE_MESSAGE
                            + " where " + DataConfig.COLUMN_MESSAGE_SEQ + " in ("
                            + makePlaceholders(msgIdsStr.length) + ")";
        Cursor cursorMsg = db.rawQuery(queryMsg, msgIdsStr);
        Log.d(tag, "queryMsg: " + queryMsg + ", " + msgIdsStr.toString());

        while(cursorMsg.moveToNext()){
            Message message = new Message(context, cursorMsg);
            Log.d(tag, "load message from database: " + message.toString());
            messages.add(message);
        }

        Log.d(tag, messages.size() + " message loaded from database");
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

    public ArrayList<Message> loadMessages(int singleConversationSeq){
        //todo
        return null;
    }

}
