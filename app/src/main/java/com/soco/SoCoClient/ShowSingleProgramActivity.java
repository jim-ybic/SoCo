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
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.soco.SoCoClient.db.DBManagerSoco;
import com.soco.SoCoClient.db.Program;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class ShowSingleProgramActivity extends ActionBarActivity implements View.OnClickListener {

    private EditText pdateEditText = null;
    private EditText ptimeEditText = null;
    private DatePickerDialog pdatePickerDialog = null;
    private TimePickerDialog ptimePickerDialog = null;
    private SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

    DBManagerSoco dbmgrSoco = null;
    Program program = null;
    String username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_single_program);

        Intent intent = getIntent();
        String pname = intent.getStringExtra(Config.PROGRAM_PNAME);
        username = intent.getStringExtra(LoginActivity.LOGIN_USERNAME);

        dbmgrSoco = new DBManagerSoco(this);
        program = dbmgrSoco.loadProgram(pname);
        showProgram(program);

        findViewsById();
        setDateTimeField();
    }

    void gotoPreviousScreen(){
        Intent intent = new Intent(this, ShowActiveProgramsActivity.class);
        intent.putExtra(LoginActivity.LOGIN_USERNAME, username);
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
        pdateEditText = (EditText) findViewById(R.id.spdate);
        pdateEditText.setInputType(InputType.TYPE_NULL);
        pdateEditText.requestFocus();
        ptimeEditText = (EditText) findViewById(R.id.sptime);
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
        pdatePickerDialog = new DatePickerDialog(this,
                new DatePickerDialog.OnDateSetListener() {
            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                pdateEditText.setText(dateFormatter.format(newDate.getTime()));
            }
        }, year, month, day);

        // Time picker
        int hour = newCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = newCalendar.get(Calendar.MINUTE);
        ptimePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
            public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                ptimeEditText.setText( selectedHour + ":" + selectedMinute);
            }
        }, hour, minute, true); //Yes 24 hour time

    }

    public void showProgram(Program program){
        ((TextView) findViewById(R.id.spname)).setText(program.pname,
                TextView.BufferType.EDITABLE);
        ((EditText) findViewById(R.id.spdate)).setText(program.pdate,
                TextView.BufferType.EDITABLE);
        ((EditText) findViewById(R.id.sptime)).setText(program.ptime,
                TextView.BufferType.EDITABLE);
        ((EditText) findViewById(R.id.spplace)).setText(program.pplace,
                TextView.BufferType.EDITABLE);
        ((EditText) findViewById(R.id.spdesc)).setText(program.pdesc,
                TextView.BufferType.EDITABLE);
    }

    Program getProgram(){
        String pname = ((TextView) findViewById(R.id.spname)).getText().toString();
        if (!program.pname.equals(pname))
            program.pname = pname;

        String pdate = ((EditText) findViewById(R.id.spdate)).getText().toString();
        if (!program.pdate.equals(pdate))
            program.pdate = pdate;

        String ptime = ((EditText) findViewById(R.id.sptime)).getText().toString();
        if (!program.ptime.equals(ptime))
            program.ptime = ptime;

        String pplace = ((EditText) findViewById(R.id.spplace)).getText().toString();
        if (!program.pplace.equals(pplace))
            program.pplace = pplace;

        String pdesc = ((EditText) findViewById(R.id.spdesc)).getText().toString();
        if (!program.pdesc.equals(pdesc))
            program.pdesc = pdesc;

        return program;
    }

    public void saveProgram(View view){
        Program p = getProgram();
        dbmgrSoco.update(p);
        Toast.makeText(getApplicationContext(), "Program saved.", Toast.LENGTH_SHORT).show();
    }

    public void completeProgram(View view){
        Program p = getProgram();
        p.pcomplete = 1;
        dbmgrSoco.update(p);
        Toast.makeText(getApplicationContext(), "Program complete.", Toast.LENGTH_SHORT).show();
    }

}
