package com.soco.SoCoClient.events.details;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.TaskCallBack;
import com.soco.SoCoClient.common.http.UrlUtil;
import com.soco.SoCoClient.common.ui.SwipeRefreshLayoutBottom;
import com.soco.SoCoClient.common.util.IconUrlUtil;
import com.soco.SoCoClient.common.util.LikeUtil;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.common.util.StringUtil;
import com.soco.SoCoClient.common.util.TimeUtil;
import com.soco.SoCoClient.events._ref.EventOrganizersActivity;
import com.soco.SoCoClient.events.comments.EventCommentsActivity;
import com.soco.SoCoClient.events.model.Event;
import com.soco.SoCoClient.events.photos.EventPhotosActivity;
import com.soco.SoCoClient.events.service.EventDetailsTask;
import com.soco.SoCoClient.events.service.LikeEventTask;
import com.soco.SoCoClient.events.ui.ViewElementHelper;
import com.soco.SoCoClient.groups.GroupDetailsActivity;
import com.soco.SoCoClient.posts.Post;
import com.soco.SoCoClient.posts.PostCardAdapter;

import java.util.ArrayList;


public class EventDetailsActivity extends ActionBarActivity implements TaskCallBack {

    static final String tag = "EventDetailsActivity";

    static int REQUESTCODE_EVENTPOSTS = 101;
    private long currentEventId = 0;

    private Context context;
    public static final String EVENT_ID = "EVENT_ID";
    private Event event;
    static final int MAX_NUMBER_BUDDIES_SHOW_ON_CARD = 6;
    ProgressDialog pd;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details);

        Log.v(tag, "oncreate");

        Intent i = getIntent();
        getSupportActionBar().hide();

        context = getApplicationContext();
        currentEventId = i.getLongExtra(EVENT_ID, 0);
        Log.v(tag, "current event id: " + currentEventId);

        Log.v(tag, "show progress dialog, start downloading event details");
        pd = ProgressDialog.show(this,
                context.getString(R.string.msg_downloading_event),
                context.getString(R.string.msg_pls_wait));
        new Thread(new Runnable(){
            public void run(){
                downloadEventDetails();
            }
        }).start();
    }


    private void downloadEventDetails(){
        Log.v(tag, "download event details");
        EventDetailsTask edt = new EventDetailsTask(SocoApp.user_id,SocoApp.token,this);
        edt.execute(Long.toString(currentEventId));
    }

    public void doneTask(Object o){
        if(o==null){
            Log.e(tag, "EventDetailsTask returns null");
            pd.dismiss();
            Toast.makeText(getApplicationContext(), R.string.msg_network_error, Toast.LENGTH_SHORT).show();
            return;
        }
        event = (Event) o;
        showDetails(event);
        pd.dismiss();
    }

    public void close(View view){
        finish();
    }

    public void eventphotos (View view){
        Log.d(tag, "show all event photos");
        Intent i = new Intent(getApplicationContext(), EventPhotosActivity.class);
        startActivity(i);
    }

    public void eventcomments (View view){
        Log.d(tag, "show all event comments");
        Intent i = new Intent(getApplicationContext(), EventCommentsActivity.class);
        startActivity(i);
    }

    public void eventorganizers (View view){
        Log.d(tag, "show all event organizers");
        Intent i = new Intent(getApplicationContext(), EventOrganizersActivity.class);
        startActivity(i);

    }

    public void joinevent(View view){
        Log.v(tag, "tap join event");
        Intent i = new Intent(getApplicationContext(), JoinEventActivity.class);
        i.putExtra(Event.EVENT_ID,Long.toString(currentEventId));
        startActivity(i);
    }
    public void groupdetails (View view){
        Log.v(tag, "tap on group details");
        Intent i = new Intent(this, GroupDetailsActivity.class);

        //todo: pass group id as parameters

        startActivity(i);
    }

    private void showDetails(Event event){
        if(event==null){
            return;
        }
        Log.v(tag, "show event details: " + event.toString());

        Log.v(tag, "set address: " + event.getAddress());
        ((TextView)this.findViewById(R.id.address)).setText(event.getAddress());

        Log.v(tag, "set intro: " + event.getIntroduction());
        ((TextView)this.findViewById(R.id.intro)).setText(event.getIntroduction());

        Log.v(tag, "set title: " + event.getTitle());
        ((TextView)this.findViewById(R.id.textTitle)).setText(event.getTitle());

        Log.v(tag, "set datetime");
        if(!StringUtil.isEmptyString(event.getStart_date())) {
            ((TextView) this.findViewById(R.id.textStartDate)).setText(TimeUtil.getTextDate(event.getStart_date(), "dd-MMM"));
            ((TextView) this.findViewById(R.id.textStartDayOfWeek)).setText(TimeUtil.getDayOfStartDate(event.getStart_date()));
        }
        if(!StringUtil.isEmptyString(event.getStart_time())||StringUtil.isEmptyString(event.getEnd_time())) {
            ((TextView) this.findViewById(R.id.textStartEndTime)).setText(TimeUtil.getTextStartEndTime(event));
        }

        //event details screen not show "like" status #30
        ((TextView) this.findViewById(R.id.likeevent)).setText(Integer.toString(event.getNumber_of_likes()));
        LikeUtil.initialLikeButton(((Button) this.findViewById(R.id.likeevent)), event.isLikedEvent());

        if(event.getCategories() != null && !event.getCategories().isEmpty()) {
            LinearLayout layout = (LinearLayout) findViewById(R.id.categories);
            ViewElementHelper.showCategories(event, layout, context);
        }
        else
            Log.v(tag, "no category for event: " + event.getTitle());

        if(event.getJoinedBuddies().size()>0 || event.getLikedBuddies().size()>0) {
            LinearLayout layout = (LinearLayout) findViewById(R.id.eventbuddies);
            ViewElementHelper.showBuddies(event, layout, context);
        }
        else
            Log.v(tag, "no buddies for event: " + event.getTitle());

        if(!StringUtil.isEmptyString(event.getCreator_id())) {
            ImageButton ib = (ImageButton) this.findViewById(R.id.creator_icon);
            IconUrlUtil.setImageForButtonNormal(getResources(), ib, UrlUtil.getUserIconUrl(event.getCreator_id()));
            ib.setTag(event.getCreator_id());
        }
        if(!StringUtil.isEmptyString(event.getCreator_name())){
            ((TextView) this.findViewById(R.id.creator_name)).setText(event.getCreator_name());
            this.findViewById(R.id.creator_name).setTag(event.getCreator_id());
        }
    }


    public void share(View view){
        if(event!=null) {
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, event.toString());
            sendIntent.setType("text/plain");
            sendIntent.setPackage("com.whatsapp");
            startActivity(sendIntent);
        }
    }

    public void likeevent(View view){
        Log.v(tag, "tap like event button");
        Button button = (Button) view.findViewById(R.id.likeevent);
        boolean isLiked = button.isActivated();
        LikeEventTask let = new LikeEventTask(SocoApp.user_id, SocoApp.token, event, button,isLiked);
        let.execute();
    }

    public void post(View view){
        Log.v(tag, "tap post");
        Intent i = new Intent(this, AddPostActivity.class);
        i.putExtra(AddPostActivity.EVENT_ID, String.valueOf(event.getId()));
        startActivity(i);
    }

    public void eventdetails(View view){
        Log.v(tag, "show event full intro message");
        String text = ((TextView) findViewById(R.id.intro)).getText().toString();

        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(R.string.msg_info);
        builder.setMessage(text);
        builder.setPositiveButton(
                getApplicationContext().getString(R.string.msg_ok),
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        Log.d(tag, "tap OK");
                    }
                });
        builder.show();
    }

    public void eventposts(View view){
        Log.v(tag, "show event posts");
        Intent i = new Intent(this, EventPostsActivity.class);
        i.putExtra(EventPostsActivity.EVENTID, String.valueOf(event.getId()));
        startActivity(i);
    }



}
