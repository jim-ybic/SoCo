package com.soco.SoCoClient;

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

import com.soco.SoCoClient.datamodel.DBManagerSoco;
import com.soco.SoCoClient.datamodel.Program;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class CreateProgramActivity extends ActionBarActivity implements View.OnClickListener {

    private EditText pdateEditText = null;
    private EditText ptimeEditText = null;
    private DatePickerDialog pdatePickerDialog = null;
    private TimePickerDialog ptimePickerDialog = null;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

    String loginEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_program);

        Intent intent = getIntent();
        loginEmail = intent.getStringExtra(Config.LOGIN_EMAIL);

        findViewsById();
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
        if(view == pdateEditText) {
            pdatePickerDialog.show();
        }
        if(view == ptimeEditText) {
            ptimePickerDialog.show();
        }
    }

    private void findViewsById() {
        pdateEditText = (EditText) findViewById(R.id.pdate);
        pdateEditText.setInputType(InputType.TYPE_NULL);
        pdateEditText.requestFocus();

        ptimeEditText = (EditText) findViewById(R.id.ptime);
        ptimeEditText.setInputType(InputType.TYPE_NULL);
    }

    private void setDateTimeField() {
        pdateEditText.setOnClickListener(this);
        ptimeEditText.setOnClickListener(this);

        // Date picker
        Calendar newCalendar = Calendar.getInstance();
        int year = newCalendar.get(Calendar.YEAR);
        int month = newCalendar.get(Calendar.MONTH);
        int day = newCalendar.get(Calendar.DAY_OF_MONTH);
        pdatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                pdateEditText.setText(dateFormatter.format(newDate.getTime()));
            }
        }, year, month, day);

        // Time picker
        int hour = newCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = newCalendar.get(Calendar.MINUTE);
        ptimePickerDialog = new TimePickerDialog(this, new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                ptimeEditText.setText( selectedHour + ":" + selectedMinute);
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
