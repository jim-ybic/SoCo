package com.soco.SoCoClient.view;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session;
import com.dropbox.client2.session.TokenPair;
import com.soco.SoCoClient.control.Config;
import com.soco.SoCoClient.R;
import com.soco.SoCoClient.control.DBManagerSoco;
import com.soco.SoCoClient.control.SignatureUtil;
import com.soco.SoCoClient.model.Program;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

import com.soco.SoCoClient.model.UploadFileToDropbox;

public class ShowSingleProgramActivity extends ActionBarActivity implements View.OnClickListener {

    // Local views
    EditText pdateEditText = null;
    EditText ptimeEditText = null;
    EditText et_spname;
    EditText et_spdate;
    EditText et_sptime;
    EditText et_spplace;
    EditText et_spdesc;
    EditText et_spphone;
    EditText et_spemail;
    EditText et_spwechat;
    Button bt_call;
    Button bt_whatsapp;
    Button bt_email;
    Button bt_wechat;
    MenuItem action_add_date;
    MenuItem action_add_time;
    MenuItem action_add_place;
    MenuItem action_add_desc;
    MenuItem action_add_phone;
    MenuItem action_add_email;
    MenuItem action_add_wechat;
    LinearLayout layout_spdate;
    LinearLayout layout_sptime;
    LinearLayout layout_spplace;
    LinearLayout layout_spdesc;
    LinearLayout layout_spphone;
    LinearLayout layout_spemail;
    LinearLayout layout_spwechat;

    // Local variables
    DBManagerSoco dbmgrSoco = null;
    Program program = null;
    String loginEmail;
    String loginPassword;
    String original_pname;

    DatePickerDialog pdatePickerDialog = null;
    TimePickerDialog ptimePickerDialog = null;
    SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

    DropboxAPI<AndroidAuthSession> dropbox;
    String ACCESS_KEY = "7cfm4ur90xw54pv";
    String ACCESS_SECRET = "9rou23wi8t4htkz";
    String accessToken;
    AppKeyPair appKeyPair;
    AccessTokenPair accessTokenPair;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i("show", "Show single activity: onCreate");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_single_program);
        findViewsById();

        Intent intent = getIntent();
        original_pname = intent.getStringExtra(Config.PROGRAM_PNAME);
        loginEmail = intent.getStringExtra(Config.LOGIN_EMAIL);
        loginPassword = intent.getStringExtra(Config.LOGIN_PASSWORD);

        dbmgrSoco = new DBManagerSoco(this);
        program = dbmgrSoco.loadProgram(original_pname);
        showProgram(program);

        setDateTimeField();

        initDropboxApiAuthentication();
    }

    void refreshOptionMenu(){
        if (et_spdate.getText().toString().isEmpty())
            action_add_date.setVisible(true);
        else
            action_add_date.setVisible(false);

        if (et_sptime.getText().toString().isEmpty())
            action_add_time.setVisible(true);
        else
            action_add_time.setVisible(false);

        if (et_spplace.getText().toString().isEmpty())
            action_add_place.setVisible(true);
        else
            action_add_place.setVisible(false);

        if (et_spdesc.getText().toString().isEmpty())
            action_add_desc.setVisible(true);
        else
            action_add_desc.setVisible(false);

        if (et_spphone.getText().toString().isEmpty())
            action_add_phone.setVisible(true);
        else
            action_add_phone.setVisible(false);

        if (et_spemail.getText().toString().isEmpty())
            action_add_email.setVisible(true);
        else
            action_add_email.setVisible(false);

        if (et_spwechat.getText().toString().isEmpty())
            action_add_wechat.setVisible(true);
        else
            action_add_wechat.setVisible(false);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_single_program, menu);

        action_add_date = menu.findItem(R.id.action_add_date);
        action_add_time = menu.findItem(R.id.action_add_time);
        action_add_place = menu.findItem(R.id.action_add_place);
        action_add_desc = menu.findItem(R.id.action_add_desc);
        action_add_phone = menu.findItem(R.id.action_add_phone);
        action_add_email = menu.findItem(R.id.action_add_email);
        action_add_wechat = menu.findItem(R.id.action_add_wechat);

        refreshOptionMenu();

        return true;
    }


    void findViewsById() {
        pdateEditText = (EditText) findViewById(R.id.et_spdate);
        pdateEditText.setInputType(InputType.TYPE_NULL);
        pdateEditText.requestFocus();

        ptimeEditText = (EditText) findViewById(R.id.et_sptime);
        ptimeEditText.setInputType(InputType.TYPE_NULL);

        et_spname = (EditText) findViewById(R.id.et_spname);
        et_spdate = (EditText) findViewById(R.id.et_spdate);
        et_sptime = (EditText) findViewById(R.id.et_sptime);
        et_spplace = (EditText) findViewById(R.id.et_spplace);
        et_spdesc = (EditText) findViewById(R.id.et_spdesc);
        et_spphone = (EditText) findViewById(R.id.et_spphone);
        et_spemail = (EditText) findViewById(R.id.et_spemail);
        et_spwechat = (EditText) findViewById(R.id.et_spwechat);

        bt_call = (Button) findViewById(R.id.bt_call);
        bt_whatsapp = (Button) findViewById(R.id.bt_whatsapp);
        bt_email = (Button) findViewById(R.id.bt_email);
        bt_wechat = (Button) findViewById(R.id.bt_wechat);

        layout_spdate = (LinearLayout) findViewById(R.id.layout_spdate);
        layout_sptime = (LinearLayout) findViewById(R.id.layout_sptime);
        layout_spplace = (LinearLayout) findViewById(R.id.layout_spplace);
        layout_spdesc = (LinearLayout) findViewById(R.id.layout_spdesc);
        layout_spphone = (LinearLayout) findViewById(R.id.layout_spphone);
        layout_spemail = (LinearLayout) findViewById(R.id.layout_spemail);
        layout_spwechat = (LinearLayout) findViewById(R.id.layout_spwechat);

//        action_add_date = (MenuItem) findViewById(R.id.action_add_date);
//        action_add_time = (MenuItem) findViewById(R.id.action_add_time);
//        action_add_place = (MenuItem) findViewById(R.id.action_add_place);
//        action_add_desc = (MenuItem) findViewById(R.id.action_add_desc);
//        action_add_phone = (MenuItem) findViewById(R.id.action_add_phone);
//        action_add_email = (MenuItem) findViewById(R.id.action_add_email);
//        action_add_wechat = (MenuItem) findViewById(R.id.action_add_wechat);
    }


    void gotoPreviousScreen(){
        Intent intent = new Intent(this, ShowActiveProgramsActivity.class);
        intent.putExtra(Config.LOGIN_EMAIL, loginEmail);
        loginEmail = intent.getStringExtra(Config.LOGIN_EMAIL);
        startActivity(intent);
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        refreshOptionMenu();
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Log.i("menu", "Menu click from single program activity.");


        switch (item.getItemId()) {
            case android.R.id.home:
                Log.i("menu", "Menu click: home.");
                gotoPreviousScreen();
                break;
            case R.id.action_add_date:
                Log.i("menu", "Menu click: add date.");
                setVisiblePdate(null);
                break;
            case R.id.action_add_time:
                Log.i("menu", "Menu click: add time.");
                setVisiblePtime(null);
                break;
            case R.id.action_add_place:
                Log.i("menu", "Menu click: add place.");
                setVisiblePplace(null);
                break;
            case R.id.action_add_desc:
                Log.i("menu", "Menu click: add desc.");
                layout_spdesc.setVisibility(View.VISIBLE);
                et_spdesc.requestFocus();
                break;
            case R.id.action_add_phone:
                Log.i("menu", "Menu click: add phone.");
                setVisiblePphone(null);
                break;
            case R.id.action_add_email:
                Log.i("menu", "Menu click: add email.");
                setVisiblePemail(null);
                break;
            case R.id.action_add_wechat:
                Log.i("menu", "Menu click: add wechat.");
                layout_spwechat.setVisibility(View.VISIBLE);
                et_spwechat.requestFocus();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setVisiblePphone(View view) {
        layout_spphone.setVisibility(View.VISIBLE);
        et_spphone.requestFocus();
    }

    public void setVisiblePemail(View view) {
        layout_spemail.setVisibility(View.VISIBLE);
        et_spemail.requestFocus();
    }

    public void setVisiblePplace(View view) {
        layout_spplace.setVisibility(View.VISIBLE);
        et_spplace.requestFocus();
    }

    public void setVisiblePtime(View view) {
        layout_sptime.setVisibility(View.VISIBLE);
        et_sptime.requestFocus();
    }

    public void setVisiblePdate(View view) {
        layout_spdate.setVisibility(View.VISIBLE);
        et_spdate.requestFocus();
    }

    @Override
    public void onBackPressed() {
        gotoPreviousScreen();
    }

    @Override
    public void onClick(View view) {
        if(view == pdateEditText) {
            pdatePickerDialog.show();
        }
        if(view == ptimeEditText) {
            ptimePickerDialog.show();
        }
    }

    void setDateTimeField() {
        // Date picker
        pdateEditText.setOnClickListener(this);
        Calendar newCalendar = Calendar.getInstance();
        int year = newCalendar.get(Calendar.YEAR);
        int month = newCalendar.get(Calendar.MONTH);
        int day = newCalendar.get(Calendar.DAY_OF_MONTH);
        pdatePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                pdateEditText.setText(dateFormatter.format(newDate.getTime()));
            }
        }, year, month, day);

        // Time picker
        ptimeEditText.setOnClickListener(this);
        int hour = newCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = newCalendar.get(Calendar.MINUTE);
        ptimePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                ptimeEditText.setText( selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true); //Yes 24 hour time

    }

    public void showProgram(Program p){
        Log.i("program", "Show program: " + p.toString());
        et_spname.setText(p.pname, TextView.BufferType.EDITABLE);

        if (p.pdate.isEmpty())
            layout_spdate.setVisibility(View.GONE);
        else {
            layout_spdate.setVisibility(View.VISIBLE);
            et_spdate.setText(p.pdate, TextView.BufferType.EDITABLE);
        }

        if (p.ptime.isEmpty())
            layout_sptime.setVisibility(View.GONE);
        else {
            layout_sptime.setVisibility(View.VISIBLE);
            et_sptime.setText(p.ptime, TextView.BufferType.EDITABLE);
        }

        if (p.pplace.isEmpty())
            layout_spplace.setVisibility(View.GONE);
        else{
            layout_spplace.setVisibility(View.VISIBLE);
            et_spplace.setText(p.pplace, TextView.BufferType.EDITABLE);
        }

        if (p.pdesc.isEmpty())
            layout_spdesc.setVisibility(View.GONE);
        else {
            layout_spdesc.setVisibility(View.VISIBLE);
            et_spdesc.setText(p.pdesc, TextView.BufferType.EDITABLE);
        }

        if (p.pphone.isEmpty())
            layout_spphone.setVisibility(View.GONE);
        else {
            layout_spphone.setVisibility(View.VISIBLE);
            et_spphone.setText(p.pphone, TextView.BufferType.EDITABLE);
        }

        if (p.pemail.isEmpty())
            layout_spemail.setVisibility(View.GONE);
        else {
            layout_spemail.setVisibility(View.VISIBLE);
            et_spemail.setText(p.pemail, TextView.BufferType.EDITABLE);
        }

        if (p.pwechat.isEmpty())
            layout_spwechat.setVisibility(View.GONE);
        else {
            layout_spwechat.setVisibility(View.VISIBLE);
            et_spwechat.setText(p.pwechat, TextView.BufferType.EDITABLE);
        }
    }

    void refreshProgram(){
        String pname = et_spname.getText().toString();
        if (!program.pname.equals(pname))
            program.pname = pname;

        String pdate = et_spdate.getText().toString();
        if (!program.pdate.equals(pdate))
            program.pdate = pdate;

        String ptime = et_sptime.getText().toString();
        if (!program.ptime.equals(ptime))
            program.ptime = ptime;

        String pplace = et_spplace.getText().toString();
        if (!program.pplace.equals(pplace))
            program.pplace = pplace;

        String pdesc = et_spdesc.getText().toString();
        if (!program.pdesc.equals(pdesc))
            program.pdesc = pdesc;

        String pphone = et_spphone.getText().toString();
        if (!program.pphone.equals(pphone))
            program.pphone = pphone;

        String pemail = et_spemail.getText().toString();
        if (!program.pemail.equals(pemail))
            program.pemail = pemail;

        String pwechat = et_spwechat.getText().toString();
        if (!program.pwechat.equals(pwechat))
            program.pwechat = pwechat;
    }

    public void saveProgram(View view){

        //TEST
//        finish();

        refreshProgram();
        dbmgrSoco.update(original_pname, program);
        Log.i("db", "Program saved, " + program.toString());
        Toast.makeText(getApplicationContext(), "Program saved.", Toast.LENGTH_SHORT).show();
    }

    public void completeProgram(View view){
        refreshProgram();
        program.pcomplete = 1;
        dbmgrSoco.update(program.pname, program);
        Toast.makeText(getApplicationContext(), "Program complete.", Toast.LENGTH_SHORT).show();
        gotoPreviousScreen();
    }

    public void call(View view){
        Log.i("call", "Make call");
        try{
            String n = et_spphone.getText().toString();
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + n));
            startActivity(intent);
        } catch (Exception e) {
            Log.e("call", "Cannot start intent to call");
            e.printStackTrace();
        }
    }

    public void sms(View view) {
        Log.i("whatsapp", "Send whatsapp");
        String n = et_spphone.getText().toString();
        Uri uri = Uri.parse("smsto:" + n);
        try {
            Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
            intent.putExtra("sms_body", "Hi, ");
            startActivity(intent);
        } catch (Exception e) {
            Log.e("sms", "Cannot start intent to send sms");
            e.printStackTrace();
        }
    }

    public void whatsapp(View view) {
        Log.i("whatsapp", "Send whatsapp");
        String n = et_spphone.getText().toString();
        Uri uri = Uri.parse("smsto:" + n);
        try {
            Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
            intent.setPackage("com.whatsapp");
            intent.putExtra("chat", true);
            startActivity(intent);
        } catch (Exception e) {
            Log.e("whatsapp", "Cannot start intent to send whatsapp");
            e.printStackTrace();
        }
    }

    public void email(View view){
        Log.i("email", "Send email");
        try {
            String e = et_spemail.getText().toString();
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", e, null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "SUBJECT");
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        } catch (Exception e) {
            Log.e("email", "Cannot start intent to send email");
            e.printStackTrace();
        }
    }


    public void wechat(View view) {
        Log.i("wechat", "Send wechat");
        try {
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(
                    "com.tencent.mm", "com.tencent.mm.ui.tools.ShareImgUI"));
            intent.setAction("android.intent.action.SEND");
            intent.setType("image/*");
            intent.putExtra(Intent.EXTRA_TEXT, "Input message below:");
//        intent.putExtra(Intent.EXTRA_STREAM, Uri.fromFile(file));
            startActivity(intent);
        } catch (Exception e){
            Log.e("wechat", "Cannot start intent to send wechat");
            e.printStackTrace();
        }
    }


    public static String hash(String string) {
        long h = 1125899906842597L; // prime
        int len = string.length();

        for (int i = 0; i < len; i++) {
            h = 31*h + string.charAt(i);
        }
        return String.valueOf(h);
    }

    public void upload(View view) {
        Log.i("upload", "ShowSingleProgramActivity:upload");
        saveProgram(view);

//        Log.i("dropbox", "Begin to anthenticate");
//        dropbox.getSession().startAuthentication(ShowSingleProgramActivity.this);

        //TEST
//        String loginEmail = "jim.ybic@gmail.com";
        String sigEmail = SignatureUtil.genSHA1(loginEmail, loginPassword);
        Log.i("hash", "SHA1 signature, " + loginEmail + ", " + sigEmail);
//        String program = "Dinner w Jenny";
        String sigProgram = SignatureUtil.genSHA1(program.pname, loginPassword);
        Log.i("hash", "SHA1 signature, " + program.pname + ", " + sigProgram);

//        Log.i("hash", "Hash code of, " + program + ", " + hash(program));

//        String p = getApplicationContext().getFilesDir().toString();
        String p = "/" + sigEmail + "/" + sigProgram + "/";
        Log.i("dropbox",  "Remote file path: " + p);

//        String p = "/" + hash(loginEmail) + "/" + hash(program);
        UploadFileToDropbox upload = new UploadFileToDropbox(this, dropbox, p);
        upload.key = ACCESS_KEY;
        upload.secret = ACCESS_SECRET;
        upload.accessTokenPair = accessTokenPair;
        upload.sigEmail = sigEmail;
        upload.sigProgram = sigProgram;
        upload.localPath = getApplicationContext().getFilesDir().toString();
        Log.i("dropbox", "Create UploadFileToDropbox with accessTokenPair: " + accessTokenPair);
        upload.execute();
    }

    void initDropboxApiAuthentication(){
        Log.i("dropbox", "Create DropboxAPI object");

        AndroidAuthSession session;

        Log.i("dropbox", "Step 1: Create appKeyPair from Key/Secret: "
                + ACCESS_KEY + "/" + ACCESS_SECRET);
        appKeyPair = new AppKeyPair(ACCESS_KEY, ACCESS_SECRET);
        accessTokenPair = new AccessTokenPair(ACCESS_KEY, ACCESS_SECRET);

        Log.i("dropbox", "Step 2: Create session with appKeyPair: " + appKeyPair
                + ", AccessType: " + Session.AccessType.APP_FOLDER
                + ", accessTokenPair: " + accessTokenPair);
//        session = new AndroidAuthSession(appKeyPair, Session.AccessType.APP_FOLDER, accessTokenPair);
        session = new AndroidAuthSession(appKeyPair, Session.AccessType.APP_FOLDER);

        Log.i("dropbox", "Step 3: Create DropboxAPI from session: " + session);
        dropbox = new DropboxAPI<AndroidAuthSession>(session);

        boolean useSoCoDropboxAccount = true;
        //TODO: choose if use SoCo's dropbox account or user's own dropbox account

        if (useSoCoDropboxAccount) {
            Log.i("dropbox", "Step 4 (approach a): Load SoCo's dropbox account and OA2 token");
            String OA2token = "JWWNa2LgL2UAAAAAAAAANNpl6wfgG5wTX6_OrNik5a_yKGsnySogfHYMK-uxjLJd";
            Log.i("dropbox", "Set DropboxAPI OA2 token: " + OA2token);
            dropbox.getSession().setOAuth2AccessToken(OA2token);
        } else {
            Log.i("dropbox", "Step 4 (approach b): Let user login");
        }

        Log.i("dropbox", "Validate DropboxAPI and Session");
        if (dropbox != null && dropbox.getSession() != null
                && dropbox.getSession().getOAuth2AccessToken() != null)
            Log.i("dropbox", "Validation success, token: " + dropbox.getSession().getOAuth2AccessToken());
        else {
                Log.i("dropbox", "Session authentication failed, create new OA2 validation session");
//                accessTokenPair = new AccessTokenPair(ACCESS_KEY, ACCESS_SECRET);
//                Log.i("dropbox", "Created accessTokenPair: " + accessTokenPair);
//                session = new AndroidAuthSession(appKeyPair, Session.AccessType.APP_FOLDER);
//                Log.i("dropbox", "New session created with accessTokenPair: " + accessTokenPair);
//                dropbox = new DropboxAPI<AndroidAuthSession>(session);
//                Log.i("dropbox", "Start DropboxAPI OAuth2 authentication screen");
                dropbox.getSession().startOAuth2Authentication(ShowSingleProgramActivity.this);
//            }

        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i("dropbox", "ShowSingleProgramActivity:OnResume, check if OA2 authentication success");
        Log.i("dropbox", "Session token: " + dropbox.getSession().getOAuth2AccessToken());

//        AndroidAuthSession session = dropbox.getSession();
        if (dropbox != null && dropbox.getSession() != null
                && dropbox.getSession().getOAuth2AccessToken() != null) {
            Log.i("dropbox", "DropboxAPI and Session created with existing token: "
                    + dropbox.getSession().getOAuth2AccessToken());
            return;
        }

        Log.i("dropbox", "Check OA2 authentication result");
        if (dropbox.getSession().authenticationSuccessful()) {
            Log.i("dropbox", "Dropbox OA2 authentication success");
            try {
                Log.i("dropbox", "Session finish authentication, set OA2 token");
                dropbox.getSession().finishAuthentication();
                Log.i("dropbox", "Session finish authentication complete with token: "
                        + dropbox.getSession().getOAuth2AccessToken());

//                TokenPair accessToken = dropbox.getSession().getAccessTokenPair();
//                Log.i("dropbox", "TokenPair after authentication: " + accessToken);
//                if(accessToken != null) {
//                    ACCESS_KEY = accessToken.key;
//                    ACCESS_SECRET = accessToken.secret;
//                } else {
//                    Log.i("dropbox", "Set session accessTokenPair as: " + accessTokenPair);
//                    dropbox.getSession().setAccessTokenPair(accessTokenPair);
//                }
            } catch (IllegalStateException e) {
                Toast.makeText(this, "Error during Dropbox authentication",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.i("dropbox", "Dropbox OA2 authentication failed (possibly timing issue)");
        }
    }
}
