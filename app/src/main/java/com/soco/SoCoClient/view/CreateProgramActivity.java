package com.soco.SoCoClient.view;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.text.InputType;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.soco.SoCoClient.control.Config;
import com.soco.SoCoClient.R;
import com.soco.SoCoClient.control.DBManagerSoco;
import com.soco.SoCoClient.model.Program;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CreateProgramActivity extends ActionBarActivity implements View.OnClickListener {

    // Local views
    private EditText et_pdate = null;
    private EditText et_ptime = null;
    private EditText et_pplace = null;
    private EditText et_pdesc = null;
    private EditText et_pphone = null;
    private EditText et_pemail = null;
    private EditText et_pwechat = null;

    private DatePickerDialog pdatePickerDialog = null;
    private TimePickerDialog ptimePickerDialog = null;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

    String loginEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_program);
        findViewsById();

        Intent intent = getIntent();
        loginEmail = intent.getStringExtra(Config.LOGIN_EMAIL);

        setDateTimeField();
    }

    void gotoPreviousScreen(){
        Intent intent = new Intent(this, ShowActiveProgramsActivity.class);
        intent.putExtra(Config.LOGIN_EMAIL, loginEmail);
        startActivity(intent);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                gotoPreviousScreen();
        }
        return true;
    }

    @Override
    public void onBackPressed() {
        gotoPreviousScreen();
    }

    @Override
    public void onClick(View view) {
        if(view == et_pdate) {
            pdatePickerDialog.show();
        }
        if(view == et_ptime) {
            ptimePickerDialog.show();
        }
    }

    private void findViewsById() {
        et_pdate = (EditText) findViewById(R.id.pdate);
        et_pdate.setInputType(InputType.TYPE_NULL);
        et_pdate.requestFocus();

        et_ptime = (EditText) findViewById(R.id.ptime);
        et_ptime.setInputType(InputType.TYPE_NULL);

        et_pplace = (EditText) findViewById(R.id.pplace);
        et_pdesc = (EditText) findViewById(R.id.pdesc);
        et_pphone = (EditText) findViewById(R.id.pphone);
        et_pemail = (EditText) findViewById(R.id.pemail);
        et_pwechat = (EditText) findViewById(R.id.pwechat);
    }

    private void setDateTimeField() {
        // Date picker
        et_pdate.setOnClickListener(this);
        Calendar newCalendar = Calendar.getInstance();
        int year = newCalendar.get(Calendar.YEAR);
        int month = newCalendar.get(Calendar.MONTH);
        int day = newCalendar.get(Calendar.DAY_OF_MONTH);
        pdatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                et_pdate.setText(dateFormatter.format(newDate.getTime()));
            }
        }, year, month, day);

        // Time picker
        et_ptime.setOnClickListener(this);
        int hour = newCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = newCalendar.get(Calendar.MINUTE);
        ptimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                et_ptime.setText(selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true); //Yes 24 hour time

    }

    public void createProgram(View view) {
        Program p = new Program();
        p.pname = ((EditText) findViewById(R.id.pname)).getText().toString();
        p.pdate = ((EditText) findViewById(R.id.pdate)).getText().toString();
        p.ptime = ((EditText) findViewById(R.id.ptime)).getText().toString();
        p.pplace = ((EditText) findViewById(R.id.pplace)).getText().toString();
        p.pcomplete = 0;
        p.pdesc = ((EditText) findViewById(R.id.pdesc)).getText().toString();
        p.pphone = ((EditText) findViewById(R.id.pphone)).getText().toString();
        p.pemail = ((EditText) findViewById(R.id.pemail)).getText().toString();
        p.pwechat = ((EditText) findViewById(R.id.pwechat)).getText().toString();

        DBManagerSoco dbmgrSoco = new DBManagerSoco(this);
        dbmgrSoco.add(p);

        Toast.makeText(getApplicationContext(), "Program created.", Toast.LENGTH_SHORT).show();
    }

}
