package com.soco.SoCoClient.view.friends;

//import info.androidhive.tabsswipe.R;

import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.control.database.DataLoader;
import com.soco.SoCoClient.model.conversation.SingleConversation;
import com.soco.SoCoClient.view.common.andtinder.model.CardModel;
import com.soco.SoCoClient.view.common.andtinder.view.CardContainer;
import com.soco.SoCoClient.view.common.andtinder.view.PersonCardStackAdapter;
import com.soco.SoCoClient.view.config.ActivityProfile;
import com.soco.SoCoClient.view.friends.contact.ActivityAllFriends;
import com.soco.SoCoClient.view.messages.ActivityChats;
import com.soco.SoCoClient.view.messages.ActivityNotifications;

import java.util.ArrayList;

public class FragmentSuggestedFriends extends Fragment implements View.OnClickListener {

    static String tag = "SuggestedFriends";


//    int pid;
//    String pid_onserver;
//    SocoApp socoApp;
//    Profile profile;
//    DBManagerSoco dbManagerSoco;
//    ArrayList<Item> contactItems;
//    SectionEntryListAdapter contactsAdapter;

    //new code
    View rootView;
    Context context;
    DataLoader dataLoader;
    ArrayList<SingleConversation> singleConversations;

    CardContainer mCardContainer;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        context = getActivity().getApplicationContext();
        dataLoader = new DataLoader(context);

//        socoApp = (SocoApp)(getActivity().getApplication());
//        profile = socoApp.profile;
//        dbManagerSoco = socoApp.dbManagerSoco;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.v(tag, "create fragment view.....");
        rootView = inflater.inflate(R.layout.fragment_suggested_friends, container, false);

        cardTest(rootView);

//        rootView.findViewById(R.id.personevents).setOnClickListener(this);
//        rootView.findViewById(R.id.persongroups).setOnClickListener(this);
        rootView.findViewById(R.id.detail).setOnClickListener(this);
//        rootView.findViewById(R.id.more).setOnClickListener(this);
        rootView.findViewById(R.id.add).setOnClickListener(this);

//        singleConversations = dataLoader.loadSingleConversations();
//        showConversations(singleConversations);

//        ((ListView)rootView.findViewById(R.id.conversations)).setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @SuppressWarnings("unchecked")
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
////                    EntryItem item = (EntryItem) contactItems.get(position);
////                    Log.d(tag, "You clicked: " + item.title);
////                    String name = item.title;
////                    String email = item.subtitle;
////                    //new fragment-based activity
////                    Intent i = new Intent(view.getContext(), ContactDetailsActivityObs.class);
////                    i.putExtra(GeneralConfigV1.INTENT_KEY_NAME, name);
////                    i.putExtra(GeneralConfigV1.INTENT_KEY_EMAIL, email);
////                    startActivity(i);
//
//                SingleConversation c = singleConversations.get(position);
//                Log.v(tag, "click on conversation: " + c.toString());
//
//                Intent i = new Intent(getActivity(), ConversationDetail.class);
//                i.putExtra(DataConfig.EXTRA_CONVERSATION_SEQ, c.getSeq());
//                startActivity(i);
//            }
//        });

//        rootView.findViewById(R.id.add).setOnClickListener(this);
//        listContacts();

        return rootView;
    }

    void cardTest(View rootView){
        Log.v(tag, "start card test");

        mCardContainer = (CardContainer) rootView.findViewById(R.id.personcards);

//        mCardContainer.setOrientation(Orientations.Orientation.Ordered);
        Resources r = getResources();
//        SimpleCardStackAdapter adapter = new SimpleCardStackAdapter(getActivity());
        PersonCardStackAdapter adapter = new PersonCardStackAdapter(getActivity());
//        adapter.add(new CardModel("Title1", "Description goes here", r.getDrawable(R.drawable.picture1)));
//        adapter.add(new CardModel("Title2", "Description goes here", r.getDrawable(R.drawable.picture2)));
//        adapter.add(new CardModel("Title3", "Description goes here", r.getDrawable(R.drawable.picture3)));
//        CardModel card = new CardModel("Title1", "Description goes here", r.getDrawable(R.drawable.picture1);

        for(int i=1; i<=10; i++) {
            CardModel cardModel = new CardModel(
                    "Person #" + i,
                    "Description goes here",
                    r.getDrawable(R.drawable.user));
            cardModel.setOnClickListener(new CardModel.OnClickListener() {
                @Override
                public void OnClickListener() {
                    Log.i(tag, "I am pressing the card");
                }
            });
            cardModel.setOnCardDismissedListener(new CardModel.OnCardDismissedListener() {
                @Override
                public void onLike() {
                    Log.i(tag, "I like the card");
                }
                @Override
                public void onDislike() {
                    Log.i(tag, "I dislike the card");
                }
            });
            adapter.add(cardModel);
        }
        mCardContainer.setAdapter(adapter);
        //card - end
    }

//    public void updateContactName(final String email) {
//        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
//        alert.setTitle("New contact name");
//        alert.setMessage("So I want to ...");
//        final EditText input = new EditText(getActivity());
//        alert.setView(input);
//
//        alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                String name = input.getText().toString();
//                dbManagerSoco.updateContactName(email, name);
//            }
//        });
//
//        alert.show();
//    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.add:
//                add();
//                break;
//            case R.id.personevents:
//                Log.d(tag, "show all event friends");
//                Intent ipe = new Intent(getActivity().getApplicationContext(), ActivityPersonEvents.class);
//                startActivity(ipe);
//                break;
//            case R.id.persongroups:
//                Log.d(tag, "show all event groups");
//                Intent ipg = new Intent(getActivity().getApplicationContext(), ActivityPersonGroups.class);
//                startActivity(ipg);
//                break;
//            case R.id.detail:
//                Log.d(tag, "pass this event");
//                break;
            case R.id.detail:
                Log.d(tag, "show person details");
                Intent ipd = new Intent(getActivity().getApplicationContext(), ActivityPersonDetails.class);
                startActivity(ipd);
                break;
            case R.id.add:
                Log.d(tag, "add this friend");
                Intent iaf = new Intent(getActivity().getApplicationContext(), ActivityAddFriend.class);
                startActivity(iaf);
                break;
        }
    }


//    public void add(String email, String nickname){
////        String email = ((EditText) rootView.findViewById(R.id.email)).getText().toString();
//        Log.i(tag, "save into db new member " + email);
//        dbManagerSoco.saveContact(email, nickname);
//
//        //todo: send invitation to server and save contact id onserver
//        Log.d(tag, "send add friend request to server: " + email);
//        String url = UrlUtil.getAddFriendUrl(getActivity());
//        AddFriendTaskAsync task = new AddFriendTaskAsync(url, email, getActivity().getApplicationContext());
//        task.execute();
//
//        Toast.makeText(getActivity().getApplicationContext(), "Sent invitation success",
//                Toast.LENGTH_SHORT).show();
//
////        listContacts();
//    }

//    public void listContacts() {
//        Log.d(tag, "List contacts");
//
//        contactItems = new ArrayList<>();
//        HashMap<String, String> map = dbManagerSoco.getContacts();
//
//        for(Map.Entry<String, String> e : map.entrySet()){
//            Log.d(tag, "found contact: " + e.getValue() + ", " + e.getKey());
//            contactItems.add(new EntryItem(e.getValue(), e.getKey()));
//        }
//
//        Log.d(tag, "set contacts adapter");
//        contactsAdapter = new SectionEntryListAdapter(getActivity(), contactItems);
//        ListView lv = (ListView) rootView.findViewById(R.id.listview_contacts);
//        lv.setAdapter(contactsAdapter);
//    }

    @Override
    public void onResume() {
        super.onResume();

//        Log.v(tag, "onResume start, reload conversations");
//        singleConversations = dataLoader.loadSingleConversations();
//        showConversations(singleConversations);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_fragment_suggested_friends, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }


//    public void addContact() {
//        Log.d(tag, "create dialog elements");
//        Context context = getActivity();
//        LinearLayout layout = new LinearLayout(context);
//        layout.setOrientation(LinearLayout.VERTICAL);
//
//        final EditText emailBox = new EditText(context);
//        emailBox.setHint("Email address");
//        emailBox.setInputType(InputType.TYPE_CLASS_TEXT | InputType.TYPE_TEXT_FLAG_NO_SUGGESTIONS
//                | InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS);
//        layout.addView(emailBox);
//
//        final EditText nicknameBox = new EditText(context);
//        nicknameBox.setHint("Nick name");
//        layout.addView(nicknameBox);
//
////        dialog.setView(layout);
//
//        AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
//        alert.setTitle("Add new contact");
////        alert.setMessage("Email address:");
////        final EditText input = new EditText(getActivity());
//        alert.setView(layout);
//
//        alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//                String email = emailBox.getText().toString();
//                String nickname = nicknameBox.getText().toString();
//                add(email, nickname);
////                Toast.makeText(getActivity().getApplicationContext(),
////                        "Invited contact complete.", Toast.LENGTH_SHORT).show();
//            }
//        });
//        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
//            public void onClick(DialogInterface dialog, int whichButton) {
//            }
//        });
//
//        alert.show();
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
//        if (id == R.id.add) {
//            Log.i(tag, "Click on add.");
//            addContact();
//        }
//        if (id == R.id.create) {
//            Log.d(tag, "tap menu item: create");
////            addContact();
//        }

        //primary function
        if (id == R.id.friends) {
            Log.d(tag, "tap menu item: contacts");
            Intent i = new Intent(getActivity(), ActivityAllFriends.class);
            startActivity(i);
        }

        //secondary functions
        else if (id == R.id.profile){
            Log.d(tag, "click on menu: profile");
            Intent i = new Intent(getActivity().getApplicationContext(), ActivityProfile.class);
            startActivity(i);
        }
        else if (id == R.id.notifications){
            Log.d(tag, "click on menu: notifications");
            Intent i = new Intent(getActivity().getApplicationContext(), ActivityNotifications.class);
            startActivity(i);
        }
        else if (id == R.id.chats){
            Log.d(tag, "click on menu: chats");
            Intent i = new Intent(getActivity().getApplicationContext(), ActivityChats.class);
            startActivity(i);
        }

        return super.onOptionsItemSelected(item);
    }


//    void showConversations(ArrayList<SingleConversation> conversations){
//        Log.v(tag, "show conversations, total " + conversations.size());
//        ArrayList<Item>  items = new ArrayList<>();
//
////        items.add(new ConversationListEntryItem("abc", "xyz", "123"));
//        for(SingleConversation c : conversations){
//            items.add(new ConversationListEntryItem(c.getCounterpartyName(), c.getLastMsgContent(), c.getLastMsgTimestamp()));
//        }
//
//        ConversationListAdapter adapter = new ConversationListAdapter(this.getActivity(), items);
//        ListView conversationList = ((ListView) rootView.findViewById(R.id.conversations));
//        if(conversationList == null)
//            Log.e(tag, "cannot find conversation list");
//        else
//            conversationList.setAdapter(adapter);
//
//    }


}