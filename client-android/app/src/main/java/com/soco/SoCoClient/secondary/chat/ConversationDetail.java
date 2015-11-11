package com.soco.SoCoClient.secondary.chat;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.secondary.chat.common.MessageListAdapter;
import com.soco.SoCoClient.secondary.chat.common.MessageListEntryItem;
import com.soco.SoCoClient.common.database._ref.DBManagerSoco;
import com.soco.SoCoClient.common.database.Config;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.common.database.DataLoader;
import com.soco.SoCoClient.secondary.chat.model.Message;
import com.soco.SoCoClient.common.model.Profile;
import com.soco.SoCoClient.secondary.chat.model.SingleConversation;
import com.soco.SoCoClient.events.ui.Item;

import java.util.ArrayList;


public class ConversationDetail extends ActionBarActivity {

    String tag = "ConversationDetail";
    String name, email;
    SocoApp socoApp;
    DBManagerSoco dbManagerSoco;
    Profile profile;

    int contactId, contactIdOnserver;

    //new variables
    Context context;
    DataLoader dataLoader;
    int seq;
    SingleConversation conversation;
    ArrayList<Message> messages;
    MessageListAdapter adapter_chat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.fragment_conversations);

        context = getApplicationContext();
        dataLoader = new DataLoader(context);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {   //load conversation
            seq = extras.getInt(Config.EXTRA_CONVERSATION_SEQ);
            Log.d(tag, "extra has seq " + seq);
            conversation = dataLoader.loadSingleConversationBySeq(seq);
        }

//        int counterpartyId = conversation.getCounterpartySeq();

        if(conversation != null){   //load messages
            ((TextView) findViewById(R.id.name)).setText(conversation.getCounterpartyName());
            messages = dataLoader.loadMessagesForSingleConversation(seq);
            show(messages);
        }


//        socoApp = (SocoApp)getApplication();
//        dbManagerSoco = socoApp.dbManagerSoco;
//        profile = socoApp.profile;

//        Intent i = getIntent();
//        name = i.getStringExtra(GeneralConfigV1.INTENT_KEY_NAME);
//        email = i.getStringExtra(GeneralConfigV1.INTENT_KEY_EMAIL);
//        contactId = dbManagerSoco.getContactIdByEmail(email);
//        contactIdOnserver = dbManagerSoco.getContactIdOnserverByEmail(email);
//        Log.d(tag, "get extra on name: " + name);

        //todo: show contact details
//        showContactDetails(name, email);
//        showChatHistory(email);
    }

//    void showContactDetails(String name, String email){
//        Log.d(tag, "show contact details: " + name + ", " + email);
//        ((TextView)findViewById(R.id.name)).setText(name);
//        ((TextView)findViewById(R.id.email)).setText(email);
//
//        String phone = dbManagerSoco.getPhoneByContactEmail(email);
//        Log.d(tag, "get phone " + phone);
//        ((EditText)findViewById(R.id.phone)).setText(phone);
//    }

    void show(ArrayList<Message> messages){

        ArrayList<Item> items = new ArrayList<>();
        for(Message m : messages){
            items.add(new MessageListEntryItem(m.getContent(), m.getCreateTimestamp()));
        }


//        ArrayList<ArrayList<String>> chatHistory = new ArrayList<ArrayList<String>>();
//        chatHistory = dbManagerSoco.getChatHistoryByContactId(contactId);
//
//        ArrayList<Item> chatItems = new ArrayList<Item>();
//        for(ArrayList<String> u : chatHistory){
//            String content = u.get(DataConfigV1.CHAT_INDEX_CONTENT);
//            String timestamp = u.get(DataConfigV1.CHAT_INDEX_TIMESTAMP);
//            String type = u.get(DataConfigV1.CHAT_INDEX_TYPE);
//            Log.d(tag, "get a chat: " + content + ", " + timestamp + ", " + type);
//
//            String sender;
//            if(type.equals(String.valueOf(DataConfigV1.CHAT_TYPE_SEND)))
//                sender = socoApp.loginEmail;
//            else
//                sender = email;
//
//            chatItems.add(new EntryItem(sender, content + " " + timestamp));
//        }

        adapter_chat = new MessageListAdapter(this, items);
        ((ListView)findViewById(R.id.messages)).setAdapter(adapter_chat);

        scrollMyListViewToBottom();

    }

//    void showChatHistory(String email){
//        ArrayList<ArrayList<String>> chatHistory = new ArrayList<ArrayList<String>>();
//        chatHistory = dbManagerSoco.getChatHistoryByContactId(contactId);
//
//        ArrayList<Item> chatItems = new ArrayList<Item>();
//        for(ArrayList<String> u : chatHistory){
//            String content = u.get(DataConfigV1.CHAT_INDEX_CONTENT);
//            String timestamp = u.get(DataConfigV1.CHAT_INDEX_TIMESTAMP);
//            String type = u.get(DataConfigV1.CHAT_INDEX_TYPE);
//            Log.d(tag, "get a chat: " + content + ", " + timestamp + ", " + type);
//
//            String sender;
//            if(type.equals(String.valueOf(DataConfigV1.CHAT_TYPE_SEND)))
//                sender = socoApp.loginEmail;
//            else
//                sender = email;
//
//            chatItems.add(new EntryItem(sender, content + " " + timestamp));
//        }
//
//        adapter_chat = new SectionEntryListAdapter(this, chatItems);
//        ((ListView)findViewById(R.id.chat)).setAdapter(adapter_chat);
//
//        scrollMyListViewToBottom();
//
//    }

    private void scrollMyListViewToBottom() {
        ((ListView)findViewById(R.id.messages)).post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                ((ListView) findViewById(R.id.messages)).setSelection(adapter_chat.getCount() - 1);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_conversation, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.attach) {
            //todo: attach something...
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void send(View view){
        EditText et_message = (EditText)findViewById(R.id.message);
        String text = et_message.getText().toString();
        Log.d(tag, "adding message: " + text);

        Message m = new Message(context);
        m.setContent(text);
        m.save();
        Log.v(tag, "new message created: " + m.toString());

//        conversation.addContext(context);
        conversation.addMessage(m);
        Log.d(tag, "message added to conversation");


//        dbManagerSoco.addMessage(contactId, message, DataConfigV1.CHAT_TYPE_SEND);
//        Toast.makeText(getApplicationContext(), "Message sent",
//                Toast.LENGTH_SHORT).show();
//
//        String url = UrlUtil.getSendMessageUrl(this);
//        String ownEmail = socoApp.loginEmail;
//        Log.i(tag, "send message to server: " + "from " + ownEmail + ", to " + email);
//        Log.i(tag, "send message to server: " + message);
//
//        SendMessageTaskAsync task = new SendMessageTaskAsync(url,
//                HttpConfigV1.MESSAGE_FROM_TYPE_1,     //from type 1: individual
//                ownEmail,                           //individual email
//                HttpConfigV1.MESSAGE_TO_TYPE_1,       //to type 1: individual
//                String.valueOf(contactIdOnserver),          //individual id
//                SignatureUtil.now(),                //timestamp
//                GeneralConfigV1.TEST_DEVICE_SAMSUNG,  //device name
//                HttpConfigV1.MESSAGE_CONTENT_TYPE_1,  //content type
//                message                             //message
//        );
//        task.execute();

        Log.d(tag, "refresh message list");
        et_message.setText("");
//        showChatHistory(email);

        messages.add(m);
        show(messages);
    }
}
