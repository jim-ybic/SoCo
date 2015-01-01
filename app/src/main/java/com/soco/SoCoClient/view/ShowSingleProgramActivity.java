package com.soco.SoCoClient.view;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
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

import com.soco.SoCoClient.control.Config;
import com.soco.SoCoClient.R;
import com.soco.SoCoClient.control.DBManagerSoco;
import com.soco.SoCoClient.model.Program;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

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
    String original_pname;

    private DatePickerDialog pdatePickerDialog = null;
    private TimePickerDialog ptimePickerDialog = null;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_single_program);
        findViewsById();

        Intent intent = getIntent();
        original_pname = intent.getStringExtra(Config.PROGRAM_PNAME);
        loginEmail = intent.getStringExtra(Config.LOGIN_EMAIL);

        dbmgrSoco = new DBManagerSoco(this);
        program = dbmgrSoco.loadProgram(original_pname);
        showProgram(program);

        setDateTimeField();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_single_program, menu);

        action_add_date = menu.findItem(R.id.action_add_date);
        if (program.pdate.isEmpty())
            action_add_date.setVisible(true);
        else
            action_add_date.setVisible(false);

        action_add_time = menu.findItem(R.id.action_add_time);
        if (program.ptime.isEmpty())
            action_add_time.setVisible(true);
        else
            action_add_time.setVisible(false);

        action_add_place = menu.findItem(R.id.action_add_place);
        if (program.pplace.isEmpty())
            action_add_place.setVisible(true);
        else
            action_add_place.setVisible(false);

        action_add_desc = menu.findItem(R.id.action_add_desc);
        if (program.pdesc.isEmpty())
            action_add_desc.setVisible(true);
        else
            action_add_desc.setVisible(false);

        action_add_phone = menu.findItem(R.id.action_add_phone);
        if (program.pphone.isEmpty())
            action_add_phone.setVisible(true);
        else
            action_add_phone.setVisible(false);

        action_add_email = menu.findItem(R.id.action_add_email);
        if (program.pemail.isEmpty())
            action_add_email.setVisible(true);
        else
            action_add_email.setVisible(false);

        action_add_wechat = menu.findItem(R.id.action_add_wechat);
        if (program.pwechat.isEmpty())
            action_add_wechat.setVisible(true);
        else
            action_add_wechat.setVisible(false);

        return true;
    }


    private void findViewsById() {
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
        startActivity(intent);
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
                layout_spdate.setVisibility(View.VISIBLE);
                et_spdate.requestFocus();
                break;
            case R.id.action_add_time:
                Log.i("menu", "Menu click: add time.");
                layout_sptime.setVisibility(View.VISIBLE);
                et_sptime.requestFocus();
                break;
            case R.id.action_add_place:
                Log.i("menu", "Menu click: add place.");
                layout_spplace.setVisibility(View.VISIBLE);
                et_spplace.requestFocus();
                break;
            case R.id.action_add_desc:
                Log.i("menu", "Menu click: add desc.");
                layout_spdesc.setVisibility(View.VISIBLE);
                et_spdesc.requestFocus();
                break;
            case R.id.action_add_phone:
                Log.i("menu", "Menu click: add phone.");
                layout_spphone.setVisibility(View.VISIBLE);
                et_spphone.requestFocus();
                break;
            case R.id.action_add_email:
                Log.i("menu", "Menu click: add email.");
                layout_spemail.setVisibility(View.VISIBLE);
                et_spemail.requestFocus();
                break;
            case R.id.action_add_wechat:
                Log.i("menu", "Menu click: add wechat.");
                layout_spwechat.setVisibility(View.VISIBLE);
                et_spwechat.requestFocus();
                break;
        }

        return super.onOptionsItemSelected(item);
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

    private void setDateTimeField() {
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
        // TODO: send wechat message
    }



}
