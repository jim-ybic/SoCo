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
    public EditText pdateEditText = null;
    public EditText ptimeEditText = null;

    public EditText et_spname;
    public EditText et_spdate;
    public EditText et_sptime;
    public EditText et_spplace;
    public EditText et_spdesc;
    public EditText et_spphone;
    public EditText et_spemail;
    public EditText et_spwechat;
    public Button bt_call;
    public Button bt_whatsapp;
    public Button bt_email;
    public Button bt_wechat;

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
                et_spdate.setVisibility(View.VISIBLE);
                et_spdate.requestFocus();
                break;
            case R.id.action_add_time:
                Log.i("menu", "Menu click: add time.");
                et_sptime.setVisibility(View.VISIBLE);
                et_sptime.requestFocus();
                break;
            case R.id.action_add_place:
                Log.i("menu", "Menu click: add place.");
                et_spplace.setVisibility(View.VISIBLE);
                et_spplace.requestFocus();
                break;
            case R.id.action_add_desc:
                Log.i("menu", "Menu click: add desc.");
                et_spdesc.setVisibility(View.VISIBLE);
                et_spdesc.requestFocus();
                break;
            case R.id.action_add_phone:
                Log.i("menu", "Menu click: add phone.");
                et_spphone.setVisibility(View.VISIBLE);
                et_spphone.requestFocus();
                break;
            case R.id.action_add_email:
                Log.i("menu", "Menu click: add email.");
                et_spemail.setVisibility(View.VISIBLE);
                et_spemail.requestFocus();
                break;
            case R.id.action_add_wechat:
                Log.i("menu", "Menu click: add wechat.");
                et_spwechat.setVisibility(View.VISIBLE);
                et_spwechat.requestFocus();
                break;
        }

        return super.onOptionsItemSelected(item);
    }

//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                gotoPreviousScreen();
//        }
//        return true;
//    }

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
            et_spdate.setVisibility(View.GONE);
        else {
            et_spdate.setVisibility(View.VISIBLE);
            et_spdate.setText(p.pdate, TextView.BufferType.EDITABLE);
        }

        if (p.ptime.isEmpty())
            et_sptime.setVisibility(View.GONE);
        else {
            et_sptime.setVisibility(View.VISIBLE);
            et_sptime.setText(p.ptime, TextView.BufferType.EDITABLE);
        }

        if (p.pplace.isEmpty())
            et_spplace.setVisibility(View.GONE);
        else{
            et_spplace.setVisibility(View.VISIBLE);
            et_spplace.setText(p.pplace, TextView.BufferType.EDITABLE);
        }

        if (p.pdesc.isEmpty())
            et_spdesc.setVisibility(View.GONE);
        else {
            et_spdesc.setVisibility(View.VISIBLE);
            et_spdesc.setText(p.pdesc, TextView.BufferType.EDITABLE);
        }

        if (p.pphone.isEmpty()) {
            et_spphone.setVisibility(View.GONE);
            bt_call.setVisibility(View.GONE);
            bt_whatsapp.setVisibility(View.GONE);
        } else {
            et_spphone.setVisibility(View.VISIBLE);
            et_spphone.setText(p.pphone, TextView.BufferType.EDITABLE);
        }

        if (p.pemail.isEmpty()) {
            et_spemail.setVisibility(View.GONE);
            bt_email.setVisibility(View.GONE);
        } else {
            et_spemail.setVisibility(View.VISIBLE);
            et_spemail.setText(p.pemail, TextView.BufferType.EDITABLE);
        }

        if (p.pwechat.isEmpty()) {
            et_spwechat.setVisibility(View.GONE);
            bt_wechat.setVisibility(View.GONE);
        } else {
            et_spwechat.setVisibility(View.VISIBLE);
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
            Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + "92713146"));
            startActivity(intent);
        } catch (Exception e) {
            Log.e("call", "Cannot start intent to call");
            e.printStackTrace();
        }
}

    public void email(View view){
        Log.i("email", "Send email");
        try {
            Intent emailIntent = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                    "mailto", "abc@gmail.com", null));
            emailIntent.putExtra(Intent.EXTRA_SUBJECT, "SUBJECT");
            startActivity(Intent.createChooser(emailIntent, "Send email..."));
        } catch (Exception e) {
            Log.e("email", "Cannot start intent to send email");
            e.printStackTrace();
        }
    }

    public void whatsapp(View view) {
        Log.i("email", "Send whatsapp");
        Uri uri = Uri.parse("smsto:92713146");
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

    public void wechat(View view) {
        Log.i("wechat", "Send wechat");
        // TODO: send wechat message
    }



}
