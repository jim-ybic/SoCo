package com.soco.SoCoClient.events._ref;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TimePicker;
import android.widget.Toast;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.database.Config;
import com.soco.SoCoClient.common.database.DataLoader;
import com.soco.SoCoClient.events.model.EventV1;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

@Deprecated
public class ActivityEventDetailV1 extends ActionBarActivity {

    static String tag = "EventDetail";

    Context context;
    DataLoader dataLoader;
    EventV1 eventV1;
    View rootView;

    //local view items
    EditText et_name, et_desc, et_date, et_time, et_location;
    DatePickerDialog pdatePickerDialog = null;
    TimePickerDialog ptimePickerDialog = null;
    SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_details_v1);

        context = getApplicationContext();
        dataLoader = new DataLoader(context);

        Bundle extras = getIntent().getExtras();
        if (extras != null) {
            int seq = extras.getInt(Config.EXTRA_EVENT_SEQ);
            Log.d(tag, "extra has seq " + seq);
            eventV1 = dataLoader.loadEvent(seq);
//            event.addContext(context);
            Log.d(tag, "loaded event: " + eventV1.toString());
        }

        findViewItems();
        setTimedatePicker();
        show(eventV1);
    }

    void findViewItems() {
        et_name = (EditText) findViewById(R.id.name);
        et_desc = (EditText) findViewById(R.id.test_view);
        et_date = (EditText) findViewById(R.id.date);
        et_time = (EditText) findViewById(R.id.time);
        et_location = (EditText) findViewById(R.id.location);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test_event_detail, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.refresh) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    void show(EventV1 e){
        if(e == null){
            Log.e(tag, "event is null");
            return;
        }

        et_name.setText(e.getName());
        et_desc.setText(e.getDesc());
        et_date.setText(e.getDate());
        et_time.setText(e.getTime());
        et_location.setText(e.getLocation());
    }

    public void save(View view){
        Log.d(tag, "save changes");

        eventV1.setName(et_name.getText().toString());
        eventV1.setDesc(et_desc.getText().toString());
        eventV1.setDate((et_date.getText().toString()));
        eventV1.setTime(et_time.getText().toString());
        eventV1.setLocation(et_location.getText().toString());

//        event.addContext(context);
        eventV1.save();
        Toast.makeText(getApplicationContext(), "Event saved", Toast.LENGTH_SHORT).show();

        return;
    }

    public void date(View view){
        pdatePickerDialog.show();
    }

    public void time(View view){
        ptimePickerDialog.show();
    }

    void setTimedatePicker() {
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
                        et_date.setText(dateFormatter.format(newDate.getTime()));
                    }
                }, year, month, day);
        // Time picker
        int hour = newCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = newCalendar.get(Calendar.MINUTE);
        ptimePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        et_time.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true); //Yes 24 hour time

    }

}
