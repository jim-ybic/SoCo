package com.soco.SoCoClient.v2.view.contacts;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.obsolete.v1.control.config.DataConfigObs;
import com.soco.SoCoClient.v2.control.config.SocoApp;
import com.soco.SoCoClient.obsolete.v1.control.config.GeneralConfig;
import com.soco.SoCoClient.obsolete.v1.control.config.HttpConfig;
import com.soco.SoCoClient.obsolete.v1.control.db.DBManagerSoco;
import com.soco.SoCoClient.obsolete.v1.control.http.task.SendMessageTaskAsync;
import com.soco.SoCoClient.v2.control.util.SignatureUtil;
import com.soco.SoCoClient.v2.view.sectionlist.EntryItem;
import com.soco.SoCoClient.v2.view.sectionlist.Item;
import com.soco.SoCoClient.v2.view.sectionlist.SectionEntryListAdapter;
import com.soco.SoCoClient.v2.control.http.UrlUtil;
import com.soco.SoCoClient.v2.model.Profile;

import java.util.ArrayList;


public class ContactDetailsActivity extends ActionBarActivity {

    String tag = "ContactDetails";
    String name, email;
    SocoApp socoApp;
    DBManagerSoco dbManagerSoco;
    Profile profile;
    SectionEntryListAdapter adapter_chat;
    int contactId, contactIdOnserver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.v1_activity_contact_details);

        socoApp = (SocoApp)getApplication();
        dbManagerSoco = socoApp.dbManagerSoco;
        profile = socoApp.profile;

        Intent i = getIntent();
        name = i.getStringExtra(GeneralConfig.INTENT_KEY_NAME);
        email = i.getStringExtra(GeneralConfig.INTENT_KEY_EMAIL);
        contactId = dbManagerSoco.getContactIdByEmail(email);
        contactIdOnserver = dbManagerSoco.getContactIdOnserverByEmail(email);
        Log.d(tag, "get extra on name: " + name);

        //todo: show contact details
        showContactDetails(name, email);
        showChatHistory(email);
    }

    void showContactDetails(String name, String email){
        Log.d(tag, "show contact details: " + name + ", " + email);
        ((TextView)findViewById(R.id.name)).setText(name);
        ((TextView)findViewById(R.id.email)).setText(email);

        String phone = dbManagerSoco.getPhoneByContactEmail(email);
        Log.d(tag, "get phone " + phone);
        ((EditText)findViewById(R.id.phone)).setText(phone);
    }

    void showChatHistory(String email){
        ArrayList<ArrayList<String>> chatHistory = new ArrayList<ArrayList<String>>();
        chatHistory = dbManagerSoco.getChatHistoryByContactId(contactId);

        ArrayList<Item> chatItems = new ArrayList<Item>();
        for(ArrayList<String> u : chatHistory){
            String content = u.get(DataConfigObs.CHAT_INDEX_CONTENT);
            String timestamp = u.get(DataConfigObs.CHAT_INDEX_TIMESTAMP);
            String type = u.get(DataConfigObs.CHAT_INDEX_TYPE);
            Log.d(tag, "get a chat: " + content + ", " + timestamp + ", " + type);

            String sender;
            if(type.equals(String.valueOf(DataConfigObs.CHAT_TYPE_SEND)))
                sender = socoApp.loginEmail;
            else
                sender = email;

            chatItems.add(new EntryItem(sender, content + " " + timestamp));
        }

        adapter_chat = new SectionEntryListAdapter(this, chatItems);
        ((ListView)findViewById(R.id.chat)).setAdapter(adapter_chat);

        scrollMyListViewToBottom();

    }

    private void scrollMyListViewToBottom() {
        ((ListView)findViewById(R.id.chat)).post(new Runnable() {
            @Override
            public void run() {
                // Select the last row so it will scroll into view...
                ((ListView) findViewById(R.id.chat)).setSelection(adapter_chat.getCount() - 1);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_contact_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.save) {
            //todo: save contact deails
            return true;
        }
        else if (id == R.id.delete) {
            //todo: delete contact deails
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void send(View view){
        EditText et_message = (EditText)findViewById(R.id.message);
        String message = et_message.getText().toString();
        Log.i(tag, "add message into database: " + message);
        dbManagerSoco.addMessage(contactId, message, DataConfigObs.CHAT_TYPE_SEND);
        Toast.makeText(getApplicationContext(), "Message sent",
                Toast.LENGTH_SHORT).show();

        String url = UrlUtil.getSendMessageUrl(this);
        String ownEmail = socoApp.loginEmail;
        Log.i(tag, "send message to server: " + "from " + ownEmail + ", to " + email);
        Log.i(tag, "send message to server: " + message);

        SendMessageTaskAsync task = new SendMessageTaskAsync(url,
                HttpConfig.MESSAGE_FROM_TYPE_1,     //from type 1: individual
                ownEmail,                           //individual email
                HttpConfig.MESSAGE_TO_TYPE_1,       //to type 1: individual
                String.valueOf(contactIdOnserver),          //individual id
                SignatureUtil.now(),                //timestamp
                GeneralConfig.TEST_DEVICE_SAMSUNG,  //device name
                HttpConfig.MESSAGE_CONTENT_TYPE_1,  //content type
                message                             //message
        );
        task.execute();

        Log.d(tag, "refresh UI");
        et_message.setText("");
        showChatHistory(email);
    }
}
