package com.soco.SoCoClient.view;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.SimpleAdapter;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.dropbox.client2.session.AccessTokenPair;
import com.dropbox.client2.session.AppKeyPair;
import com.dropbox.client2.session.Session;
import com.soco.SoCoClient.control.config.Config;
import com.soco.SoCoClient.R;
import com.soco.SoCoClient.control.config.DataConfig;
import com.soco.SoCoClient.control.db.DBManagerSoco;
import com.soco.SoCoClient.control.SocoApp;
import com.soco.SoCoClient.control.dropbox.DropboxUtl;
import com.soco.SoCoClient.control.util.FileUtils;
import com.soco.SoCoClient.control.util.SignatureUtil;
import com.soco.SoCoClient.model.Program;
import com.soco.SoCoClient.model.Project;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.soco.SoCoClient.control.config.DataConfig.*;

public class ShowSingleProjectActivity extends ActionBarActivity implements View.OnClickListener {

    public static String PROGRAM = "programName";
    public static String tag="ShowSingleProgram";

    // Local views
    EditText pdateEditText, ptimeEditText;
    EditText et_spname, et_spdate, et_sptime, et_spplace, et_spdesc;
    AutoCompleteTextView et_spphone_auto, et_spemail_auto;
    Button bt_call, bt_whatsapp, bt_email, bt_wechat;
//    LinearLayout layout_spdate, layout_sptime, layout_spplace, layout_spdesc;
    LinearLayout layout_spphone, layout_spemail;
    TableRow tr_spdate, tr_sptime, tr_spplace, tr_spdesc, tr_spphone, tr_spemail;

    // Local variables
    DBManagerSoco dbmgrSoco = null;
    Program program = null;
    String loginEmail;
    String loginPassword;
    String original_pname;

    Project project;
    ArrayList<HashMap<String, String>> attrMap;

    DatePickerDialog pdatePickerDialog = null;
    TimePickerDialog ptimePickerDialog = null;
    SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

    DropboxAPI<AndroidAuthSession> dropbox;
    String ACCESS_KEY = "7cfm4ur90xw54pv";
    String ACCESS_SECRET = "9rou23wi8t4htkz";
    String accessToken;
    AppKeyPair appKeyPair;
    AccessTokenPair accessTokenPair;
    int pid;
    ArrayList<String> sharedFileNames = new ArrayList<>();

    private ArrayList<Map<String, String>> listNamePhone, listNameEmail;
    private SimpleAdapter mAdapterPhone, mAdapterEmail;
//    private AutoCompleteTextView mTxtPhoneNo;

    static int SHOW_MORE_ACTIVITY = 100;
    static int OPEN_FILE_ACTIVITY = 101;

    public static int UPLOAD_RETRY = 30;
    public static int UPLOAD_WAIT = 1000;    //ms

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.i(tag, "onCreate, start");
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_single_project);
        findViewsById();

        //TEST
        SocoApp app = (SocoApp) getApplicationContext();
        Log.i(tag, "SocoApp get state: " + app.getState());
        app.setState("showSingle");

        Log.i(tag, "onCreate, original values: " +
                loginEmail + ", " + loginPassword + ", " + original_pname);

        if (loginEmail == null || loginEmail.isEmpty()) {
            Intent intent = getIntent();
            Log.i(tag, "onCreate: intent package, " + intent.getPackage());
            loginEmail = intent.getStringExtra(Config.LOGIN_EMAIL);
            loginPassword = intent.getStringExtra(Config.LOGIN_PASSWORD);
            pid = intent.getIntExtra(Config.PROJECT_PID, -1);
            Log.i(tag, "onCreate, get String extra: "
                    + Config.LOGIN_EMAIL + ":" + loginEmail + ", "
                    + Config.LOGIN_PASSWORD + ":" + loginPassword + ", "
                    + Config.PROJECT_PID + ":" + pid);
        }

        dbmgrSoco = new DBManagerSoco(this);
        project = dbmgrSoco.loadProjectByPid(pid);
        attrMap = dbmgrSoco.loadProjectAttributesByPid(pid);
        showProjectToScreen(project, attrMap);

        setDateTimeField();
        initDropboxApiAuthentication();
        PopulatePhoneEmailList();
    }

    public void PopulatePhoneEmailList(){
        SocoApp app = (SocoApp) getApplicationContext();

        Log.i(tag, "Populate phone list");
        listNamePhone = app.loadNamePhoneList();
        mAdapterPhone = new SimpleAdapter(this, listNamePhone, R.layout.custcontview ,
                new String[] { "Key", "Value" },
                new int[] { R.id.auto_key, R.id.auto_value});
        et_spphone_auto.setAdapter(mAdapterPhone);
        et_spphone_auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View arg1, int index, long arg3) {
                Map<String, String> map = (Map<String, String>) av.getItemAtPosition(index);
                String value = map.get("Value");
                et_spphone_auto.setText(value);
            }
        });

        Log.i(tag, "Populate email list");
        listNameEmail = app.loadNameEmailList();
        mAdapterEmail = new SimpleAdapter(this, listNameEmail, R.layout.custcontview ,
                new String[] { "Key", "Value" },
                new int[] { R.id.auto_key, R.id.auto_value});
        et_spemail_auto.setAdapter(mAdapterEmail);
        et_spemail_auto.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View arg1, int index, long arg3) {
                Map<String, String> map = (Map<String, String>) av.getItemAtPosition(index);
                String value = map.get("Value");
                et_spemail_auto.setText(value);
            }
        });
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_single_program, menu);
        return true;
    }


    void findViewsById() {
        pdateEditText = (EditText) findViewById(R.id.et_spdate);
        pdateEditText.setInputType(InputType.TYPE_NULL);
        ptimeEditText = (EditText) findViewById(R.id.et_sptime);
        ptimeEditText.setInputType(InputType.TYPE_NULL);

        et_spname = (EditText) findViewById(R.id.et_spname);
        et_spdate = (EditText) findViewById(R.id.et_spdate);
        et_sptime = (EditText) findViewById(R.id.et_sptime);
        et_spplace = (EditText) findViewById(R.id.et_spplace);
        et_spdesc = (EditText) findViewById(R.id.et_spdesc);
        et_spphone_auto = (AutoCompleteTextView) findViewById(R.id.et_spphone_auto);
        et_spemail_auto = (AutoCompleteTextView) findViewById(R.id.et_spemail_auto);

//        bt_call = (Button) findViewById(R.id.bt_call);
//        bt_whatsapp = (Button) findViewById(R.id.bt_whatsapp);
//        bt_email = (Button) findViewById(R.id.bt_email);
//        bt_wechat = (Button) findViewById(R.id.bt_wechat);

//        layout_spphone = (LinearLayout) findViewById(R.id.layout_spphone);
//        layout_spemail = (LinearLayout) findViewById(R.id.layout_spemail);

        tr_spdate = (TableRow) findViewById(R.id.tr_spdate);
        tr_sptime = (TableRow) findViewById(R.id.tr_sptime);
        tr_spplace = (TableRow) findViewById(R.id.tr_spplace);
        tr_spdesc = (TableRow) findViewById(R.id.tr_spdesc);
        tr_spphone = (TableRow) findViewById(R.id.tr_spphone);
        tr_spemail = (TableRow) findViewById(R.id.tr_spemail);
    }

    void gotoPreviousScreen(){
        Log.i(tag, "gotoPreviousScreen");
//        Intent intent = new Intent();
//        intent.putExtra(Config.LOGIN_EMAIL, loginEmail);
//        intent.putExtra(Config.LOGIN_PASSWORD, loginPassword);
//        Log.i(tag, "put extra, "
//                + Config.LOGIN_EMAIL + ":" + loginEmail + ", "
//                + Config.LOGIN_PASSWORD + ":" + loginPassword);
//        setResult(Activity.RESULT_OK, intent);
        finish();
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
//        refreshOptionMenu();
        return super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Log.i(tag, "onOptionsItemSelected");

        switch (item.getItemId()) {
            case android.R.id.home:
                Log.i(tag, "Menu click: home.");
                gotoPreviousScreen();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

    public void setVisiblePdesc(View view) {
        tr_spdesc.setVisibility(View.VISIBLE);
        et_spdesc.requestFocus();
    }

//    public void setVisiblePplace(View view) {
//        tr_spplace.setVisibility(View.VISIBLE);
//        et_spplace.requestFocus();
//    }
//
//    public void setVisiblePtime(View view) {
//        tr_sptime.setVisibility(View.VISIBLE);
//        et_sptime.requestFocus();
//    }
//
//    public void setVisiblePdate(View view) {
//        tr_spdate.setVisibility(View.VISIBLE);
//        et_spdate.requestFocus();
//    }

    public void setVisibleDateTimePlace(View view) {
        tr_spdate.setVisibility(View.VISIBLE);
        tr_sptime.setVisibility(View.VISIBLE);
        tr_spplace.setVisibility(View.VISIBLE);
    }

    public void setVisiblePhoneEmail(View view) {
        tr_spphone.setVisibility(View.VISIBLE);
        tr_spemail.setVisibility(View.VISIBLE);
    }

    public void clearPdate(View view) {
        et_spdate.setText("");
        tr_spdate.setVisibility(View.GONE);
    }

    public void clearPtime(View view) {
        et_sptime.setText("");
        tr_sptime.setVisibility(View.GONE);
    }

    public void clearPplace(View view) {
        et_spplace.setText("");
        tr_spplace.setVisibility(View.GONE);
    }

    public void clearPdesc(View view) {
        et_spdesc.setText("");
        tr_spdesc.setVisibility(View.GONE);
    }

    public void clearPphone(View view) {
        et_spphone_auto.setText("");
        tr_spphone.setVisibility(View.GONE);
    }

    public void clearPemail(View view) {
        et_spemail_auto.setText("");
        tr_spemail.setVisibility(View.GONE);
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

    public void showProjectToScreen(Project p, ArrayList<HashMap<String, String>> attrMap){
        Log.i(tag, "Show project: " + p.pid + ", " + p.pname + ", "
                + " total attributes: " + attrMap.size());

        //name
        et_spname.setText(p.pname);
//        et_spname.setText(p.pname, TextView.BufferType.EDITABLE);

        //hide all attributes (before showing available attributes)
        tr_spdate.setVisibility(View.GONE);
        tr_sptime.setVisibility(View.GONE);
        tr_spplace.setVisibility(View.GONE);
        tr_spdesc.setVisibility(View.GONE);
        tr_spphone.setVisibility(View.GONE);
        tr_spemail.setVisibility(View.GONE);

        //show available attributes
        for(HashMap<String, String> map : attrMap) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String attr_name = entry.getKey();
                String attr_value = entry.getValue();

                if (attr_name.equals(DataConfig.ATTRIBUTE_NAME_DATE)) {    //date
                    tr_spdate.setVisibility(View.VISIBLE);
                    et_spdate.setText(attr_value, TextView.BufferType.EDITABLE);
                } else if (attr_name.equals(DataConfig.ATTRIBUTE_NAME_TIME)) {   //time
                    tr_sptime.setVisibility(View.VISIBLE);
                    et_sptime.setText(attr_value, TextView.BufferType.EDITABLE);
                } else if (attr_name.equals(DataConfig.ATTRIBUTE_NAME_PLACE)) {   //place
                    tr_spplace.setVisibility(View.VISIBLE);
                    et_spplace.setText(attr_value, TextView.BufferType.EDITABLE);
                } else if (attr_name.equals(DataConfig.ATTRIBUTE_NAME_DESC)) {   //description
                    tr_spdesc.setVisibility(View.VISIBLE);
                    et_spdesc.setText(attr_value, TextView.BufferType.EDITABLE);
                } else if (attr_name.equals(DataConfig.ATTRIBUTE_NAME_PHONE)) {   //phone
                    tr_spphone.setVisibility(View.VISIBLE);
                    et_spphone_auto.setText(attr_value, TextView.BufferType.EDITABLE);
                } else if (attr_name.equals(DataConfig.ATTRIBUTE_NAME_EMAIL)) {   //email
                    tr_spemail.setVisibility(View.VISIBLE);
                    et_spemail_auto.setText(attr_value, TextView.BufferType.EDITABLE);
                }
            }
        }

        //show shared file summary
        String summary = SignatureUtil.genSharedFileSummary(attrMap);
        Log.i(tag, "Shared file summary: " + summary);
        ((TextView) findViewById(R.id.tv_shared_file_summary)).setText(summary,
                TextView.BufferType.EDITABLE);

    }

    HashMap<String, String> collectProjectAttributes(){
        HashMap<String, String> attrMap = new HashMap<String, String>();

        //date, time, and place
        String date = et_spdate.getText().toString();
        if (date != null && !date.isEmpty())
            attrMap.put(ATTRIBUTE_NAME_DATE, date);

        String time = et_sptime.getText().toString();
        if (time != null && !time.isEmpty())
            attrMap.put(ATTRIBUTE_NAME_TIME, time);

        String place = et_spplace.getText().toString();
        if (place != null && !place.isEmpty())
            attrMap.put(ATTRIBUTE_NAME_PLACE, place);

        //description
        String desc = et_spdesc.getText().toString();
        if(desc != null && !desc.isEmpty())
            attrMap.put(ATTRIBUTE_NAME_DESC, desc);

        //contact
        String phone = et_spphone_auto.getText().toString();
        if(phone != null && !phone.isEmpty())
            attrMap.put(ATTRIBUTE_NAME_PHONE, phone);

        String email = et_spemail_auto.getText().toString();
        if(email != null && !email.isEmpty())
            attrMap.put(ATTRIBUTE_NAME_EMAIL, email);

        return attrMap;
    }

    String collectProjectNameFromScreen(){
        String pname = et_spname.getText().toString();
        Log.i(tag, "Get project name from screen: " + pname);
        return pname;
    }

    public void saveProjectToDb(View view){
        Log.i(tag, "Save to db the project: " + project.pid + ", " + project.pname);
        String pname = collectProjectNameFromScreen();
        dbmgrSoco.updateProjectName(project.pid, pname);
        HashMap<String, String> attrMap = collectProjectAttributes();
        dbmgrSoco.updateDbProjectAttributes(pid, attrMap);
        Toast.makeText(getApplicationContext(), "Project saved.", Toast.LENGTH_SHORT).show();
    }

    public void setProjectCompleted(View view){
        Log.i(tag, "Set project complete start");
        saveProjectToDb(view);
        dbmgrSoco.updateProjectActiveness(pid, DataConfig.VALUE_PROJECT_INACTIVE);
        Toast.makeText(getApplicationContext(), "Project complete, well done.",
                Toast.LENGTH_SHORT).show();
        gotoPreviousScreen();
    }

    public void call(final View view){
        Log.i(tag, "Make a call");
        try{
            String n = et_spphone_auto.getText().toString();
            if (n.isEmpty()){
                AlertDialog.Builder alert = new AlertDialog.Builder(this);
                alert.setTitle("Phone number is not set");
                alert.setMessage("Input number");
                final AutoCompleteTextView input = getAutoCompleteTextViewPhone();
                alert.setView(input);

                alert.setPositiveButton("Call", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String s = input.getText().toString();
                        et_spphone_auto.setText(s);
                        saveProjectToDb(view);
                        Log.i(tag, "New phone number saved and call: " + s);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(DataConfig.ATTRIBUTE_NAME_PHONE, s);
                        attrMap.add(map);
                        showProjectToScreen(project, attrMap);
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + s));
                        startActivity(intent);
                    }
                });
                alert.setNeutralButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String s = input.getText().toString();
                        et_spphone_auto.setText(s);
                        saveProjectToDb(view);
                        Log.i(tag, "New phone number saved: " + s);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(DataConfig.ATTRIBUTE_NAME_PHONE, s);
                        attrMap.add(map);
                        showProjectToScreen(project, attrMap);
                    }
                });
                alert.setNegativeButton("Cancel", null);
                alert.show();
            } else {
                Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + n));
                startActivity(intent);
            }
        } catch (Exception e) {
            Log.e(tag, "Cannot start intent to call");
            e.printStackTrace();
        }
    }

    public AutoCompleteTextView getAutoCompleteTextViewPhone() {
        final AutoCompleteTextView input = new AutoCompleteTextView(this);
        input.setInputType(InputType.TYPE_CLASS_PHONE);
        input.setAdapter(mAdapterPhone);
        input.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View arg1, int index, long arg3) {
                Map<String, String> map = (Map<String, String>) av.getItemAtPosition(index);
                String key  = map.get("Key");
                String value = map.get("Value");
                input.setText(value);
            }
        });
        return input;
    }

    public AutoCompleteTextView getAutoCompleteTextViewEmail() {
        final AutoCompleteTextView input = new AutoCompleteTextView(this);
        input.setInputType(InputType.TYPE_CLASS_TEXT);
        input.setAdapter(mAdapterEmail);
        input.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> av, View arg1, int index, long arg3) {
                Map<String, String> map = (Map<String, String>) av.getItemAtPosition(index);
                String key  = map.get("Key");
                String value = map.get("Value");
                input.setText(value);
            }
        });
        return input;
    }

    public void sms(final View view) {
        Log.i("sms", "Send sms");
        String n = et_spphone_auto.getText().toString();
        if (n.isEmpty()){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Phone number is not set");
            alert.setMessage("Input number");
            final AutoCompleteTextView input = getAutoCompleteTextViewPhone();
            alert.setView(input);
            alert.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String s = input.getText().toString();
                    //todo: save phone number to project
                    program.pphone = s;
//                    et_spphone_auto.setText(s);
//                    saveProgramToDb(view);
//                    Log.i("new", "New phone number saved and send sms: " + s);
//                    showProgramToScreen(program);
                    Log.i("new", "Send sms");
                    Uri uri = Uri.parse("smsto:" + s);
                    try {
                        Intent intent = new Intent(Intent.ACTION_SENDTO, uri);
                        intent.putExtra("sms_body", "Hi, ");
                        startActivity(intent);
                    } catch (Exception e) {
                        Log.e("sms", "Cannot start intent to send sms");
                        e.printStackTrace();
                    }
                }
            });
            alert.setNeutralButton("Save", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String s = input.getText().toString();
                    //todo: save phone number to project
//                    program.pphone = s;
//                    et_spphone_auto.setText(s);
//                    saveProgramToDb(view);
//                    Log.i("new", "New phone number saved: " + s);
//                    showProgramToScreen(program);
                }
            });
            alert.setNegativeButton("Cancel", null);
            alert.show();
        } else {
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
    }

    public void whatsapp(final View view) {
        Log.i("whatsapp", "Send whatsapp");
        String n = et_spphone_auto.getText().toString();

        if (n.isEmpty()){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Phone number is not set");
            alert.setMessage("Input number");
            final AutoCompleteTextView input = getAutoCompleteTextViewPhone();
            alert.setView(input);
            alert.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String s = input.getText().toString();
                    //todo: save phone number to project
//                    program.pphone = s;
//                    et_spphone_auto.setText(s);
//                    saveProgramToDb(view);
//                    Log.i("new", "New phone number saved and send whatsapp: " + s);
//                    showProgramToScreen(program);
                    Uri uri = Uri.parse("smsto:" + s);
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
            });
            alert.setNeutralButton("Save", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String s = input.getText().toString();
                    //todo: save phone number to project
//                    program.pphone = s;
//                    et_spphone_auto.setText(s);
//                    saveProgramToDb(view);
//                    Log.i("new", "New phone number saved: " + s);
//                    showProgramToScreen(program);
                }
            });
            alert.setNegativeButton("Cancel", null);
            alert.show();
        } else {
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
    }

    public void email(final View view){
        Log.i(tag, "Send email");
        String n = et_spemail_auto.getText().toString();
        if (n.isEmpty()){
            AlertDialog.Builder alert = new AlertDialog.Builder(this);
            alert.setTitle("Email is not set");
            alert.setMessage("Input email");
            final AutoCompleteTextView input = getAutoCompleteTextViewEmail();
            alert.setView(input);
            alert.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String s = input.getText().toString();
                    et_spemail_auto.setText(s);
                    saveProjectToDb(view);
                    Log.i(tag, "New email saved and send: " + s);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(DataConfig.ATTRIBUTE_NAME_EMAIL, s);
                    attrMap.add(map);
                    showProjectToScreen(project, attrMap);
                    try {
                        String e = et_spemail_auto.getText().toString();
                        Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                                "mailto", e, null));
                        emailIntent.putExtra(Intent.EXTRA_SUBJECT, "SUBJECT");
                        startActivity(Intent.createChooser(emailIntent, "Send email..."));
                    } catch (Exception e) {
                        Log.e("email", "Cannot start intent to send email");
                        e.printStackTrace();
                    }
                }
            });
            alert.setNeutralButton("Save", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String s = input.getText().toString();
                    et_spemail_auto.setText(s);
                    saveProjectToDb(view);
                    Log.i(tag, "New email saved and send: " + s);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(DataConfig.ATTRIBUTE_NAME_EMAIL, s);
                    attrMap.add(map);
                    showProjectToScreen(project, attrMap);
                }
            });
            alert.setNegativeButton("Cancel", null);
            alert.show();
        } else {
            try {
                Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", n, null));
                emailIntent.putExtra(Intent.EXTRA_SUBJECT, "SUBJECT");
                startActivity(Intent.createChooser(emailIntent, "Send email..."));
            } catch (Exception e) {
                Log.e(tag, "Cannot start intent to send email");
                e.printStackTrace();
            }
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

//
//    public void upload(View view) {
//        Log.i("upload", "ShowSingleProgramActivity:upload");
//        saveProgramToDb(view);
//
//        String sigEmail = SignatureUtil.genSHA1(loginEmail, loginPassword);
//        Log.i("hash", "SHA1 signature, " + loginEmail + ", " + sigEmail);
//        String sigProgram = SignatureUtil.genSHA1(program.pname, loginPassword);
//        Log.i("hash", "SHA1 signature, " + program.pname + ", " + sigProgram);
//
//        String p = "/" + sigEmail + "/" + sigProgram + "/";
//        Log.i("dropboxApi",  "Remote file path: " + p);
//
//        DropboxUploaderUtil upload = new DropboxUploaderUtil(this, dropboxApi,sigEmail, sigProgram);
//        upload.key = ACCESS_KEY;
//        upload.secret = ACCESS_SECRET;
//        upload.accessTokenPair = accessTokenPair;
////        upload.sigEmail = sigEmail;
////        upload.sigProgram = sigProgram;
//        upload.localPath = getApplicationContext().getFilesDir().toString();
//        Log.i("dropboxApi", "Create UploadFileToDropbox with accessTokenPair: " + accessTokenPair);
//        upload.execute();
//    }



    void initDropboxApiAuthentication(){
        Log.d(tag, "initDropboxApiAuthentication: start");

        AndroidAuthSession session;

//        Log.i(tag, "Step 1: Create appKeyPair from Key/Secret: "
//                + ACCESS_KEY + "/" + ACCESS_SECRET);
        appKeyPair = new AppKeyPair(ACCESS_KEY, ACCESS_SECRET);
        accessTokenPair = new AccessTokenPair(ACCESS_KEY, ACCESS_SECRET);

//        Log.i(tag, "Step 2: Create session with appKeyPair: " + appKeyPair
//                + ", AccessType: " + Session.AccessType.APP_FOLDER
//                + ", accessTokenPair: " + accessTokenPair);
        session = new AndroidAuthSession(appKeyPair, Session.AccessType.APP_FOLDER);

//        Log.i(tag, "Step 3: Create DropboxAPI from session: " + session);
        dropbox = new DropboxAPI<AndroidAuthSession>(session);

        boolean useSoCoDropboxAccount = true;
        //TODO: choose if use SoCo's dropboxApi account or user's own dropboxApi account

        if (useSoCoDropboxAccount) {
//            Log.i(tag, "Step 4 (approach a): Load SoCo's dropboxApi account and OA2 token");
            String OA2token = "JWWNa2LgL2UAAAAAAAAANNpl6wfgG5wTX6_OrNik5a_yKGsnySogfHYMK-uxjLJd";
//            Log.i(tag, "Set DropboxAPI OA2 token: " + OA2token);
            dropbox.getSession().setOAuth2AccessToken(OA2token);
        } else {
//            Log.i("dropboxApi", "Step 4 (approach b): Let user login");
        }

//        Log.d(tag, "Validate DropboxAPI and Session");
        if (dropbox != null && dropbox.getSession() != null
                && dropbox.getSession().getOAuth2AccessToken() != null) {
//            Log.d("dropboxApi", "Validation success, token: " + dropboxApi.getSession().getOAuth2AccessToken());
        }
        else {
//                Log.d("dropboxApi", "Session authentication failed, create new OA2 validation session");
            dropbox.getSession().startOAuth2Authentication(ShowSingleProjectActivity.this);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

//        Log.i(tag, "onResume: Show programName to screen: " + program.pname + ", " + program.pphone);
//        showProgramToScreen(program);
        Log.i(tag, "onResume start, reload project attribute for pid: " + pid);
        attrMap = dbmgrSoco.loadProjectAttributesByPid(pid);
        Log.i(tag, "onResume, total attributes loaded: " + attrMap.size());
        showProjectToScreen(project, attrMap);

        if (dropbox != null && dropbox.getSession() != null
                && dropbox.getSession().getOAuth2AccessToken() != null) {
            Log.d(tag, "DropboxAPI and Session created with existing token: "
                    + dropbox.getSession().getOAuth2AccessToken());
            return;
        }

//        Log.i(tag, "Check OA2 authentication result");
        if (dropbox.getSession().authenticationSuccessful()) {
//            Log.i(tag, "Dropbox OA2 authentication success");
            try {
//                Log.i(tag, "Session finish authentication, set OA2 token");
                dropbox.getSession().finishAuthentication();
//                Log.i(tag, "Session finish authentication complete with token: "
//                        + dropboxApi.getSession().getOAuth2AccessToken());
            } catch (IllegalStateException e) {
                Toast.makeText(this, "Error during Dropbox authentication",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.i(tag, "Dropbox OA2 authentication failed (possibly timing issue)");
        }
    }

    public void more(View view) {
        Log.i(tag, "ShowSingleProgramActivity:more");
//        saveProgramToDb(view);
        saveProjectToDb(view);;

        Intent i = new Intent(this, ShowMoreActivity.class);
        i.putExtra(Config.LOGIN_EMAIL, loginEmail);
        i.putExtra(Config.LOGIN_PASSWORD, loginPassword);
        i.putExtra(Config.PROJECT_PID, project.pid);
        Log.i(tag, Config.LOGIN_EMAIL + ":" + loginEmail + ", "
                + Config.LOGIN_PASSWORD + ":" + loginPassword + ", "
                + Config.PROJECT_PID + ":" + project.pid);

        startActivityForResult(i, SHOW_MORE_ACTIVITY);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (100) : {  //show more
                if (resultCode == Activity.RESULT_OK) {
                    Log.i(tag, "onActivityResult, original values: " + loginEmail + ", " + loginPassword);
                    loginEmail = data.getStringExtra(Config.LOGIN_EMAIL);
                    loginPassword = data.getStringExtra(Config.LOGIN_PASSWORD);
                    original_pname = data.getStringExtra(Config.PROGRAM_PNAME);
                    Log.i(tag, "get string extra: "
                            + loginEmail + ", " + loginPassword + ", " + original_pname);

                }
                break;
            }
            case (101) : {  //add file
                if (resultCode == Activity.RESULT_OK) {
                    Uri uri = null;
                    if (data != null) {
                        uri = data.getData();
                        Log.i(tag, "File selected with uri: " + uri.toString());
                        FileUtils.checkUriMeta(getContentResolver(), uri);
                        DropboxUtl.uploadToDropbox(uri, loginEmail, loginPassword, pid, dropbox,
                                getContentResolver(), getApplicationContext());
                        SocoApp app = (SocoApp) getApplicationContext();
                        app.setUploadStatus(SocoApp.UPLOAD_STATUS_START);
                        // check result
                        boolean isSuccess = false;
                        for (int i=1; i<= UPLOAD_RETRY; i++) {
                            Log.d(tag, "Wait for upload response: " + i + "/" + UPLOAD_RETRY);
                            SystemClock.sleep(UPLOAD_WAIT);;
                            Log.d(tag, "Current upload status is: " + app.getUploadStatus());
                            if(app.getUploadStatus().equals(SocoApp.UPLOAD_STATUS_SUCCESS)) {
                                isSuccess = true;
                                break;
                            }
                            else if (app.getUploadStatus().equals(SocoApp.UPLOAD_STATUS_FAIL)){
                                isSuccess = false;
                                break;
                            }
                        }
                        if(isSuccess) {
                            Log.i(tag, "File upload success");
//                            String filename = FileUtils.getDisplayName(getContentResolver(), uri);
//                            sharedFileNames.add(filename);
//                            ((SocoApp) getApplicationContext()).setSharedFileNames(sharedFileNames);
                            String remotePath = DropboxUtl.getRemotePath(uri,
                                    loginEmail, loginPassword, pid, getContentResolver());
                            saveSharedFileToDb(pid, remotePath);
                            new AlertDialog.Builder(this)
                                    .setTitle("File upload success")
                                    .setMessage("File has been saved in the cloud")
                                    .setPositiveButton("OK", null)
                                    .show();
                        }
                        else {
                            Log.i(tag, "File upload failed");
                            new AlertDialog.Builder(this)
                                    .setTitle("File upload failed")
                                    .setMessage("Review upload details and try again")
                                    .setPositiveButton("OK", null)
                                    .show();
                        }
                    }
                }
                break;
            }
        }
    }

    public void saveSharedFileToDb(int pid, String remotePath){
        Log.i(tag, "Save shared file to db start: " + pid + ", " + remotePath);
        dbmgrSoco.addSharedFileProjectAttribute(pid, remotePath);
    }

    public void addFile(View view){
        Log.i(tag, "add file start");
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("*/*");
        startActivityForResult(intent, OPEN_FILE_ACTIVITY);
    }

    public void sharedFileDetails(View view){
        Log.i(tag, "Show shared file details start, set attrMap for pid=" + pid);
//        ((SocoApp) getApplicationContext()).setPid(pid);

        ((SocoApp) getApplicationContext()).setAttrMap(attrMap);

        Intent i = new Intent(this, ShowSharedFilesActivity.class);
        startActivityForResult(i, -1);
    }


}