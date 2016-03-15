package com.soco.SoCoClient.events.service;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.braintreepayments.api.BraintreePaymentActivity;
import com.braintreepayments.api.PaymentRequest;
import com.braintreepayments.api.models.PaymentMethodNonce;
import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.TaskCallBack;
import com.soco.SoCoClient.common.http.UrlUtil;
import com.soco.SoCoClient.common.service.HttpRequestTask;
import com.soco.SoCoClient.common.util.IconUrlUtil;
import com.soco.SoCoClient.config.ClientConfig;
import com.soco.SoCoClient.events.details.AddPostActivity;
import com.soco.SoCoClient.events.model.Event;

import android.support.v7.app.NotificationCompat;
import android.app.TaskStackBuilder;
import android.app.PendingIntent;
import android.app.NotificationManager;
import android.widget.Toast;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by JohnWU on 2/3/2016.
 */
public class EventPaypalActivity extends AppCompatActivity implements TaskCallBack {

    static int REQUEST_CODE = 100;
    static final String tag = "EventPaypalActivity";
    private long currentEventId = 0;
    private String eventTitle = "";
    private String eventAddress = "";
    private String eventStartDate = "";
    private String eventStartTime = "";
    private String eventEndDate = "";
    private String eventEndTime = "";
    private String eventBannerUrl = "";

    private Context context;
    private String clientToken = "";
    ProgressDialog pd;

    private static final String SERVER_URL = "http://"+ ClientConfig.SERVER_IP+":"+ UrlUtil.SERVER_PORT;

    public static final String EVENT_ID = "EVENT_ID";
    public static String EVENT_TITLE = "EVENT TITLE";
    public static String EVENT_BANNER = "EVENT ICON";
    public static String EVENT_ADDRESS = "EVENT ADDRESS";
    public static String EVENT_START_DATE = "EVENT START DATE";
    public static String EVENT_START_TIME = "EVENT START TIME";
    public static String EVENT_END_DATE = "EVENT START DATE";
    public static String EVENT_END_TIME = "EVENT START TIME";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_paypal_v1);

        Log.v(tag, "oncreate");

        Intent i = getIntent();
        getSupportActionBar().hide();

        context = getApplicationContext();
        currentEventId = i.getLongExtra(EVENT_ID, 0);
        eventTitle = i.getStringExtra(EVENT_TITLE);
        eventAddress = i.getStringExtra(EVENT_ADDRESS);
        eventStartDate = i.getStringExtra(EVENT_START_DATE);
        eventStartTime = i.getStringExtra(EVENT_START_TIME);
        eventEndDate = i.getStringExtra(EVENT_END_DATE);
        eventEndTime = i.getStringExtra(EVENT_END_TIME);
        eventBannerUrl = i.getStringExtra(EVENT_BANNER);

        Log.v(tag, "current event id: " + currentEventId);
        Log.v(tag, "event details: " + eventTitle);

        init();

    }

    private void init(){
        ((TextView)this.findViewById(R.id.eventTitle)).setText(eventTitle);
        ((TextView)this.findViewById(R.id.eventAddress)).setText(eventAddress);
        ((TextView)this.findViewById(R.id.eventStartDate)).setText(eventStartDate);
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
            Date date = sdf.parse(eventStartDate);
            String week = (new SimpleDateFormat("EE")).format(date);
            String strDateWeek = eventStartDate + " " + week;
            if(eventStartDate.equals(eventEndDate)){
                ((TextView)this.findViewById(R.id.eventStartTime)).setText(eventStartTime + " ~ " + eventEndTime);
            } else {
                ((TextView)this.findViewById(R.id.eventStartTime)).setText(eventStartTime);
            }
            ((TextView)this.findViewById(R.id.eventStartDate)).setText(strDateWeek);
            calculateTotal();
            getClientTokenForPaypal();
        }catch (ParseException e){
            Log.e(tag, e.getMessage());
        }catch(IOException e){
            Log.e(tag, e.getMessage());
        }
        ////
        IconUrlUtil.setImageForViewWithSize(
                this.getResources(), (ImageView) this.findViewById(R.id.paypalEventBanner), eventBannerUrl);

    }

    private void calculateTotal(){
        Double price = Double.valueOf(((TextView)this.findViewById(R.id.eventPrice)).getText().toString());
        Integer amount = Integer.valueOf(((TextView)this.findViewById(R.id.ticketAmount)).getText().toString());
        Double total = price * amount;
        ((TextView)this.findViewById(R.id.totalSummary)).setText(String.valueOf(total) + " HKD ");
    }

    private void getClientTokenForPaypal() throws IOException {
//        String tokenURL = "https://localhost:8443/v1/client_token";
        String tokenURL = SERVER_URL+"/v1/client_token";
        HttpRequestTask getTask = new HttpRequestTask(this);
        getTask.execute("GET", tokenURL);
    }

    private PaymentRequest getPaymentRequest() {
        String amount = ((TextView)this.findViewById(R.id.ticketAmount)).getText().toString();
        PaymentRequest paymentRequest = new PaymentRequest()
                .clientToken(clientToken)
                .secondaryDescription("1 Item")
                .amount(amount)
                .submitButtonText("Buy");


        return paymentRequest;
    }


    public void increaseAmount(View view){
        Integer amount = Integer.valueOf(((TextView)this.findViewById(R.id.ticketAmount)).getText().toString());

        amount = amount + 1;
        ((TextView) this.findViewById(R.id.ticketAmount)).setText(String.valueOf(amount));
        calculateTotal();
    }

    public void decreaseAmount(View view){
        Integer amount = Integer.valueOf(((TextView)this.findViewById(R.id.ticketAmount)).getText().toString());
        if(amount > 0) {
            amount = amount - 1;
            ((TextView) this.findViewById(R.id.ticketAmount)).setText(String.valueOf(amount));
            calculateTotal();
        }
    }

    public void doneTask(Object o){
        if(o==null){
            Log.e(tag, "EventDetailsTask returns null");
            Toast.makeText(getApplicationContext(), R.string.msg_network_error, Toast.LENGTH_SHORT).show();
            return;
        }
        if(o instanceof String){
            clientToken = (String)o;
            Log.v(tag, "Get client token: "+clientToken);
        } else {
            Log.v(tag, "default: " + o);
        }
    }


    public void onPaypalSubmit(View view){
        Log.v(tag,"submit click");

        startActivityForResult(getPaymentRequest().getIntent(this), REQUEST_CODE);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.v(tag, "In onActivityResult : " + requestCode);
        if (requestCode == REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                PaymentMethodNonce paymentMethodNonce = data.getParcelableExtra(
                        BraintreePaymentActivity.EXTRA_PAYMENT_METHOD_NONCE
                );
                String nonce = paymentMethodNonce.getNonce();
                // Send the nonce to your server.
                Log.v(tag, "nonce: " + nonce);
                String tokenURL = SERVER_URL+"/v1/checkout";
                HttpRequestTask getTask = new HttpRequestTask(this);
                getTask.execute("POST", tokenURL, "payment_method_nonce", nonce, "payment_amount", ((TextView)this.findViewById(R.id.ticketAmount)).getText().toString());
            }
        }
    }

    public void testForNotification(){
        NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(this);
        mBuilder.setSmallIcon(R.drawable.logo);
        mBuilder.setContentTitle("APA");
        mBuilder.setContentText("Events received");
        mBuilder.setAutoCancel(true);

        NotificationCompat.InboxStyle inboxStyle = new NotificationCompat.InboxStyle();
        String[] events = {"event 1","event 2","event 3","event 4","event 5","event 6"};
        inboxStyle.setBigContentTitle("Event tracker details:");

        for(int i=0;i<events.length; i++){
            inboxStyle.addLine(events[i]);
        }
        mBuilder.setStyle(inboxStyle);
// Creates an explicit intent for an Activity in your app
        Intent resultIntent = new Intent(this, AddPostActivity.class);

// The stack builder object will contain an artificial back stack for the
// started Activity.
// This ensures that navigating backward from the Activity leads out of
// your application to the Home screen.
        TaskStackBuilder stackBuilder = TaskStackBuilder.create(this);
// Adds the back stack for the Intent (but not the Intent itself)
        stackBuilder.addParentStack(AddPostActivity.class);
// Adds the Intent that starts the Activity to the top of the stack
        stackBuilder.addNextIntent(resultIntent);
        PendingIntent resultPendingIntent =
                stackBuilder.getPendingIntent(
                        0,
                        PendingIntent.FLAG_UPDATE_CURRENT
                );
        mBuilder.setContentIntent(resultPendingIntent);
        NotificationManager mNotificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
// mId allows you to update the notification later on.
        mNotificationManager.notify(1, mBuilder.build());
    }


    public void close(View view){
        finish();
    }
}
