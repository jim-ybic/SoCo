package com.soco.SoCoClient.view.messages;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.control.config.DataConfig;
import com.soco.SoCoClient.control.database.DataLoader;
import com.soco.SoCoClient.model.conversation.SingleConversation;
import com.soco.SoCoClient.view.common.Item;
import com.soco.SoCoClient.view.friends.ConversationDetail;
import com.soco.SoCoClient.view.friends.ConversationListAdapter;
import com.soco.SoCoClient.view.friends.ConversationListEntryItem;

import java.util.ArrayList;

public class ActivityChats extends ActionBarActivity {

    static String tag = "Chats";

//    View rootView;
    Context context;
    DataLoader dataLoader;
    ArrayList<SingleConversation> singleConversations;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats);

        context = getApplicationContext();
        dataLoader = new DataLoader(context);
        singleConversations = dataLoader.loadSingleConversations();
        showConversations(singleConversations);

        ((ListView)findViewById(R.id.conversations)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//                    EntryItem item = (EntryItem) contactItems.get(position);
//                    Log.d(tag, "You clicked: " + item.title);
//                    String name = item.title;
//                    String email = item.subtitle;
//                    //new fragment-based activity
//                    Intent i = new Intent(view.getContext(), ContactDetailsActivityObs.class);
//                    i.putExtra(GeneralConfigV1.INTENT_KEY_NAME, name);
//                    i.putExtra(GeneralConfigV1.INTENT_KEY_EMAIL, email);
//                    startActivity(i);

                SingleConversation c = singleConversations.get(position);
                Log.v(tag, "click on conversation: " + c.toString());

                Intent i = new Intent(getApplicationContext(), ConversationDetail.class);
                i.putExtra(DataConfig.EXTRA_CONVERSATION_SEQ, c.getSeq());
                startActivity(i);
            }
        });
    }

    void showConversations(ArrayList<SingleConversation> conversations){
        Log.v(tag, "show conversations, total " + conversations.size());
        ArrayList<Item>  items = new ArrayList<>();

//        items.add(new ConversationListEntryItem("abc", "xyz", "123"));
        for(SingleConversation c : conversations){
            items.add(new ConversationListEntryItem(c.getCounterpartyName(), c.getLastMsgContent(), c.getLastMsgTimestamp()));
        }

        ConversationListAdapter adapter = new ConversationListAdapter(this, items);
        ListView conversationList = ((ListView) findViewById(R.id.conversations));
        if(conversationList == null)
            Log.e(tag, "cannot find conversation list");
        else
            conversationList.setAdapter(adapter);

    }

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        getMenuInflater().inflate(R.menu.menu_activity_chats, menu);
//        return true;
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.v(tag, "onResume start, reload conversations");
        singleConversations = dataLoader.loadSingleConversations();
        showConversations(singleConversations);
    }
}
