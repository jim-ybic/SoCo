package com.soco.SoCoClient.events._ref;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ScrollView;
import android.widget.SimpleAdapter;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

//import com.dropbox.client2.DropboxAPI;
//import com.dropbox.client2.android.AndroidAuthSession;
import com.soco.SoCoClient._ref.DataConfigV1;
import com.soco.SoCoClient._ref.GeneralConfigV1;
import com.soco.SoCoClient.R;
import com.soco.SoCoClient.common.database._ref.DBManagerSoco;
import com.soco.SoCoClient.common.util.SocoApp;
import com.soco.SoCoClient.common.dropbox._ref.DropboxUtilV1;
import com.soco.SoCoClient.common.util.ActivityUtil;
import com.soco.SoCoClient.common.util.SignatureUtil;
import com.soco.SoCoClient.common.model._ref.Activity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.soco.SoCoClient._ref.DataConfigV1.*;

@Deprecated
public class ActivityDetailsFragment extends Fragment implements View.OnClickListener {

    public static String tag="ActivityDetailsFragment";

    View rootView;

    // Local views
    EditText pdateEditText, ptimeEditText;
    EditText et_spname, et_spdate, et_sptime, et_spdesc;
    TextView tv_spdate, tv_sptime, tv_splocname, tv_spdesc, tv_sptag;
    EditText et_splocation, et_sptag;
    AutoCompleteTextView et_spphone_auto, et_spemail_auto;
    TableRow tr_spdatetime, tr_splocation, tr_spdesc, tr_spphone, tr_spemail, tr_sptag;
    Button bt_clear_pdatetime, bt_splocname;
    ScrollView sv_sproject;

    DBManagerSoco dbmgrSoco = null;
//    String original_pname;

    Activity activity;
    ArrayList<HashMap<String, String>> attrMap;

    DatePickerDialog pdatePickerDialog = null;
    TimePickerDialog ptimePickerDialog = null;
    SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

//    DropboxAPI<AndroidAuthSession> dropboxApi;
    int pid, pid_onserver;
//    String pid_onserver;

    private ArrayList<Map<String, String>> listNamePhone, listNameEmail;
    private SimpleAdapter mAdapterPhone, mAdapterEmail;

    SocoApp socoApp = (SocoApp)(Context)getActivity();

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(tag, "on create view");
        rootView = inflater.inflate(R.layout.fragment_activity_details, container, false);
        Log.d(tag, "Found root view: " + rootView);

        findViewsById();
        showProjectToScreen(activity, attrMap);
        setDateTimeField();
        PopulatePhoneEmailList();

        //set button listeners
//        rootView.findViewById(R.id.bt_save_project).setOnClickListener(this);
//        rootView.findViewById(R.id.bt_finish_project).setOnClickListener(this);
        rootView.findViewById(R.id.et_spdate).setOnClickListener(this);
        rootView.findViewById(R.id.et_sptime).setOnClickListener(this);
        rootView.findViewById(R.id.bt_splocation).setOnClickListener(this);
//        rootView.findViewById(R.id.bt_shared_file_details).setOnClickListener(this);
        rootView.findViewById(R.id.bt_call).setOnClickListener(this);
        rootView.findViewById(R.id.bt_email).setOnClickListener(this);

        return rootView;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.bt_save_project:
//                saveProjectToDb();
//                break;
//            case R.id.bt_finish_project:
//                setProjectCompleted();
//                break;
            case R.id.et_spdate:
                pdatePickerDialog.show();
                break;
            case R.id.et_sptime:
                ptimePickerDialog.show();
                break;
            case R.id.bt_splocation:
                showLocationDetails();
                break;
//            case R.id.bt_shared_file_details:
//                sharedFileDetails();
//                break;
            case R.id.bt_call:
                call();
                break;
            case R.id.bt_email:
                email();
                break;
        }
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);

        if(socoApp == null){
            Log.i(tag, "socoApp object is null, creating new");
            socoApp = (SocoApp)(getActivity().getApplication());
            Log.i(tag, "socoApp object created: " + socoApp);
        }

        pid = socoApp.pid;
        pid_onserver = socoApp.pid_onserver;
        Log.d(tag, "pid is " + pid + ", pid_onserver is " + pid_onserver);

//        Log.i(tag, "onCreate, get project properties: "
//                        + GeneralConfigV1.PROJECT_PID + ":" + pid + ","
//                        + GeneralConfigV1.PROJECT_PID_ONSERVER + ":" + pid_onserver
//        );

        dbmgrSoco = new DBManagerSoco(getActivity().getApplication());
        activity = dbmgrSoco.loadActivityByAid(pid);
        attrMap = dbmgrSoco.loadActivityAttributesByPid(pid);

//        dropboxApi = DropboxUtilV1.initDropboxApiAuthentication(
//                GeneralConfigV1.ACCESS_KEY, GeneralConfigV1.ACCESS_SECRET, GeneralConfigV1.OA2_TOKEN,
//                getActivity().getApplication());
//        socoApp.dropboxApi = dropboxApi;
    }

    public void PopulatePhoneEmailList(){
        Log.i(tag, "Populate phone list");
        listNamePhone = socoApp.loadNamePhoneList();
        mAdapterPhone = new SimpleAdapter(getActivity(), listNamePhone, R.layout.custcontview,
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
        listNameEmail = socoApp.loadNameEmailList();
        mAdapterEmail = new SimpleAdapter(getActivity(), listNameEmail, R.layout.custcontview,
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.menu_activity_details, menu);
        super.onCreateOptionsMenu(menu, inflater);
        return;
    }

    void findViewsById() {
        if(rootView == null)
            Log.e(tag, "Cannot get View object");
        Log.i(tag, "root view is: " + rootView);

        pdateEditText = (EditText) rootView.findViewById(R.id.et_spdate);
        pdateEditText.setInputType(InputType.TYPE_NULL);
        ptimeEditText = (EditText) rootView.findViewById(R.id.et_sptime);
        ptimeEditText.setInputType(InputType.TYPE_NULL);

        et_spname = (EditText) rootView.findViewById(R.id.et_spname);
        if(et_spname == null){
            Log.e(tag, "et_spname object cannot be found");
            return;
        }
        else
            Log.i(tag, "Found et_spname object: " + et_spdate);

        et_spdate = (EditText) rootView.findViewById(R.id.et_spdate);
        et_sptime = (EditText) rootView.findViewById(R.id.et_sptime);
        et_splocation = (EditText) rootView.findViewById(R.id.et_splocation);
        et_spdesc = (EditText) rootView.findViewById(R.id.et_spdesc);
        et_spphone_auto = (AutoCompleteTextView) rootView.findViewById(R.id.et_spphone_auto);
        et_spemail_auto = (AutoCompleteTextView) rootView.findViewById(R.id.et_spemail_auto);

        tr_spdatetime = (TableRow) rootView.findViewById(R.id.tr_spdatetime);
        tr_splocation = (TableRow) rootView.findViewById(R.id.tr_splocation);
        tr_spdesc = (TableRow) rootView.findViewById(R.id.tr_spdesc);
        tr_spphone = (TableRow) rootView.findViewById(R.id.tr_spphone);
        tr_spemail = (TableRow) rootView.findViewById(R.id.tr_spemail);

        tv_spdate = (TextView) rootView.findViewById(R.id.tv_spdate);
        tv_sptime = (TextView) rootView.findViewById(R.id.tv_sptime);
        tv_splocname = (TextView) rootView.findViewById(R.id.tv_splocname);
        tv_spdesc = (TextView) rootView.findViewById(R.id.tv_spdesc);

        bt_splocname = (Button) rootView.findViewById(R.id.bt_splocation);

        tr_sptag = (TableRow) rootView.findViewById(R.id.tr_sptag);
        tv_sptag = (TextView) rootView.findViewById(R.id.tv_sptag);
        et_sptag = (EditText) rootView.findViewById(R.id.et_sptag);

        sv_sproject = (ScrollView) rootView.findViewById(R.id.sv_sproject);
    }

    void gotoPreviousScreen(){
        getActivity().finish();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        Log.i(tag, "onOptionsItemSelected:" + item.getItemId());

        switch (item.getItemId()) {
            case android.R.id.home:
                getActivity().finish();
                break;
            case R.id.mn_datetime:
                if (et_spdate.getText().toString().isEmpty()
                        || et_sptime.getText().toString().isEmpty()){
                    tr_spdatetime.setVisibility(View.VISIBLE);
                    tv_spdate.setVisibility(View.VISIBLE);
                    et_spdate.setVisibility(View.VISIBLE);
                    tv_sptime.setVisibility(View.VISIBLE);
                    et_sptime.setVisibility(View.VISIBLE);
                } else {
                    et_spdate.setText("");
                    et_sptime.setText("");
                    tr_spdatetime.setVisibility(View.GONE);
                }
                break;
            case R.id.mn_location:
                if (et_splocation.getText().toString().isEmpty()){
                    tr_splocation.setVisibility(View.VISIBLE);
                } else {
                    et_splocation.setText("");
                    tr_splocation.setVisibility(View.GONE);
                }
                break;
            case R.id.mn_desc:
                if (et_spdesc.getText().toString().isEmpty()){
                    tr_spdesc.setVisibility(View.VISIBLE);
                } else {
                    et_spdesc.setText("");
                    tr_spdesc.setVisibility(View.GONE);
                }
                break;
            case R.id.mn_phone:
                if (et_spphone_auto.getText().toString().isEmpty()){
                    tr_spphone.setVisibility(View.VISIBLE);
                } else {
                    et_spphone_auto.setText("");
                    tr_spphone.setVisibility(View.GONE);
                }
                break;
            case R.id.mn_email:
                if (et_spemail_auto.getText().toString().isEmpty()){
                    tr_spemail.setVisibility(View.VISIBLE);
                } else {
                    et_spemail_auto.setText("");
                    tr_spemail.setVisibility(View.GONE);
                }
                break;
            case R.id.mn_tag:
                if (et_sptag.getText().toString().isEmpty()){
                    tr_sptag.setVisibility(View.VISIBLE);
                } else {
                    et_sptag.setText("");
                    tr_sptag.setVisibility(View.GONE);
                }
                break;
            case R.id.save:
                saveProjectToDb();
                break;
            case R.id.delete_activity:
                setProjectCompleted();
                break;
        }

        return super.onOptionsItemSelected(item);
    }


    void setDateTimeField() {
        // Date picker
        Calendar newCalendar = Calendar.getInstance();
        int year = newCalendar.get(Calendar.YEAR);
        int month = newCalendar.get(Calendar.MONTH);
        int day = newCalendar.get(Calendar.DAY_OF_MONTH);
        pdatePickerDialog = new DatePickerDialog(getActivity(),
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
        ptimePickerDialog = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        ptimeEditText.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true); //Yes 24 hour time

    }

    public void showProjectToScreen(Activity p, ArrayList<HashMap<String, String>> attrMap){
        if(p == null) {
            Log.e(tag, "Project is null");
            return;
        }
        Log.i(tag, "Show project: " + p.pid + ", " + p.pname + ", "
                + " total attributes: " + attrMap.size());

        //name
        et_spname.setText(p.pname);

        //hide all attributes (before showing available attributes)
        tr_spdatetime.setVisibility(View.GONE);
        tv_spdate.setVisibility(View.GONE);
        et_spdate.setVisibility(View.GONE);
        tv_sptime.setVisibility(View.GONE);
        et_sptime.setVisibility(View.GONE);
        tr_splocation.setVisibility(View.GONE);
        tr_spdesc.setVisibility(View.GONE);
        tr_spphone.setVisibility(View.GONE);
        tr_spemail.setVisibility(View.GONE);
        tr_sptag.setVisibility(View.GONE);

        //show available attributes
        for(HashMap<String, String> map : attrMap) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String attr_name = entry.getKey();
                String attr_value = entry.getValue();
                Log.d(tag, "Current attr: " + attr_name + ", " + attr_value);

                if (attr_name.equals(DataConfigV1.ATTRIBUTE_NAME_DATE)) {    //date
                    tr_spdatetime.setVisibility(View.VISIBLE);
                    tv_spdate.setVisibility(View.VISIBLE);
                    et_spdate.setVisibility(View.VISIBLE);
                    et_spdate.setText(attr_value, TextView.BufferType.EDITABLE);
                } else if (attr_name.equals(DataConfigV1.ATTRIBUTE_NAME_TIME)) {   //time
                    tr_spdatetime.setVisibility(View.VISIBLE);
                    tv_sptime.setVisibility(View.VISIBLE);
                    et_sptime.setVisibility(View.VISIBLE);
                    et_sptime.setText(attr_value, TextView.BufferType.EDITABLE);
                } else if (attr_name.equals(DataConfigV1.ATTRIBUTE_NAME_LOCNAME)) {   //locname
                    tr_splocation.setVisibility(View.VISIBLE);
                    et_splocation.setText(attr_value, TextView.BufferType.EDITABLE);
                } else if (attr_name.equals(DataConfigV1.ATTRIBUTE_NAME_DESC)) {   //description
                    tr_spdesc.setVisibility(View.VISIBLE);
                    et_spdesc.setText(attr_value, TextView.BufferType.EDITABLE);
                } else if (attr_name.equals(DataConfigV1.ATTRIBUTE_NAME_PHONE)) {   //phone
                    tr_spphone.setVisibility(View.VISIBLE);
                    et_spphone_auto.setText(attr_value, TextView.BufferType.EDITABLE);
                } else if (attr_name.equals(DataConfigV1.ATTRIBUTE_NAME_EMAIL)) {   //email
                    tr_spemail.setVisibility(View.VISIBLE);
                    et_spemail_auto.setText(attr_value, TextView.BufferType.EDITABLE);
                }
            }
        }

        //show tag
        String ptag = p.ptag;
        if(ptag != null && !ptag.isEmpty()){
            tr_sptag.setVisibility(View.VISIBLE);
            et_sptag.setText(ptag, TextView.BufferType.EDITABLE);
        }

        //show shared file summary
        ArrayList<String> sharedFileDisplayName = dbmgrSoco.getSharedFilesDisplayName(pid);
        String summary = SignatureUtil.genSharedFileSummary(sharedFileDisplayName);
        Log.i(tag, "Shared file summary: " + summary);
//        ((TextView) rootView.findViewById(R.id.tv_shared_file_summary)).setText(summary,
//                TextView.BufferType.EDITABLE);
    }

    HashMap<String, String> collectProjectAttributesFromScreen(){
        HashMap<String, String> attrMap = new HashMap<String, String>();

        //date, time, and place
        String date = et_spdate.getText().toString();
        if (date != null && !date.isEmpty())
            attrMap.put(ATTRIBUTE_NAME_DATE, date);

        String time = et_sptime.getText().toString();
        if (time != null && !time.isEmpty())
            attrMap.put(ATTRIBUTE_NAME_TIME, time);

        String locname = et_splocation.getText().toString();
        if (locname != null && !locname.isEmpty())
            attrMap.put(ATTRIBUTE_NAME_LOCNAME, locname);

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

        //tag
//        String tag = et_sptag.getText().toString();
//        if (tag != null && !tag.isEmpty())
//            attrMap.put(ATTRIBUTE_NAME_TAG, tag);

        return attrMap;
    }

    public void saveProjectToDb(){
        Log.i(tag, "Save to db the project: " + activity.pid + ", " + activity.pid_onserver + ", "
                + activity.pname);
        String pname = et_spname.getText().toString();
        dbmgrSoco.updateActivityName(activity.pid, pname);
        ActivityUtil.serverUpdateActivityName(String.valueOf(pid), getActivity().getApplication(),
                pname, String.valueOf(pid_onserver));

        String ptag = et_sptag.getText().toString();
        dbmgrSoco.updateAcivityTag(activity.pid, ptag);
        //TODO: server interface to update project tag

        HashMap<String, String> attrMap2 = collectProjectAttributesFromScreen();
        if(attrMap2.size() > 0) {
            dbmgrSoco.updateDbActivityAttributes(pid, attrMap2);
            ActivityUtil.serverSetActivityAttribute(String.valueOf(pid),
                    getActivity().getApplication(),
                    pname, String.valueOf(pid_onserver), attrMap2);
        }

        Toast.makeText(getActivity(), "Project saved.", Toast.LENGTH_SHORT).show();
        attrMap = dbmgrSoco.loadActivityAttributesByPid(pid);
    }

    public void setProjectCompleted(){
        Log.i(tag, "Set project complete start");
        saveProjectToDb();
        dbmgrSoco.updateActivityActiveness(pid, DataConfigV1.VALUE_ACTIVITY_INACTIVE);
        Toast.makeText(getActivity(), "Project complete, well done.",
                Toast.LENGTH_SHORT).show();
        ActivityUtil.serverArchiveActivity(String.valueOf(pid), getActivity().getApplication(), String.valueOf(pid_onserver));
        gotoPreviousScreen();
    }

    public void call(){
        Log.i(tag, "Make a call");
        try{
            String n = et_spphone_auto.getText().toString();
            if (n.isEmpty()){
                AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
                alert.setTitle("Phone number is not set");
                alert.setMessage("Input number");
                final AutoCompleteTextView input = getAutoCompleteTextViewPhone();
                alert.setView(input);

                alert.setPositiveButton("Call", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String s = input.getText().toString();
                        et_spphone_auto.setText(s);
                        saveProjectToDb();
                        Log.i(tag, "New phone number saved and call: " + s);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(DataConfigV1.ATTRIBUTE_NAME_PHONE, s);
                        attrMap.add(map);
                        showProjectToScreen(activity, attrMap);
                        Intent intent = new Intent(Intent.ACTION_CALL, Uri.parse("tel:" + s));
                        startActivity(intent);
                    }
                });
                alert.setNeutralButton("Save", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int whichButton) {
                        String s = input.getText().toString();
                        et_spphone_auto.setText(s);
                        saveProjectToDb();
                        Log.i(tag, "New phone number saved: " + s);
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put(DataConfigV1.ATTRIBUTE_NAME_PHONE, s);
                        attrMap.add(map);
                        showProjectToScreen(activity, attrMap);
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
        final AutoCompleteTextView input = new AutoCompleteTextView(getActivity());
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
        final AutoCompleteTextView input = new AutoCompleteTextView(getActivity());
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
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setTitle("Phone number is not set");
            alert.setMessage("Input number");
            final AutoCompleteTextView input = getAutoCompleteTextViewPhone();
            alert.setView(input);
            alert.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String s = input.getText().toString();
                    //todo: save phone number to project
//                    program.pphone = s;
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
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setTitle("Phone number is not set");
            alert.setMessage("Input number");
            final AutoCompleteTextView input = getAutoCompleteTextViewPhone();
            alert.setView(input);
            alert.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String s = input.getText().toString();
                    //todo: save phone number to project
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

    public void email(){
        Log.i(tag, "Send email");
        String n = et_spemail_auto.getText().toString();
        if (n.isEmpty()){
            AlertDialog.Builder alert = new AlertDialog.Builder(getActivity());
            alert.setTitle("Email is not set");
            alert.setMessage("Input email");
            final AutoCompleteTextView input = getAutoCompleteTextViewEmail();
            alert.setView(input);
            alert.setPositiveButton("Send", new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int whichButton) {
                    String s = input.getText().toString();
                    et_spemail_auto.setText(s);
                    saveProjectToDb();
                    Log.i(tag, "New email saved and send: " + s);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(DataConfigV1.ATTRIBUTE_NAME_EMAIL, s);
                    attrMap.add(map);
                    showProjectToScreen(activity, attrMap);
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
                    saveProjectToDb();
                    Log.i(tag, "New email saved and send: " + s);
                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put(DataConfigV1.ATTRIBUTE_NAME_EMAIL, s);
                    attrMap.add(map);
                    showProjectToScreen(activity, attrMap);
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
            startActivity(intent);
        } catch (Exception e){
            Log.e("wechat", "Cannot start intent to send wechat");
            e.printStackTrace();
        }
    }

    @Override
    public void onResume() {
        super.onResume();

        Log.i(tag, "onResume start, reload project attribute for pid: " + pid);
        attrMap = dbmgrSoco.loadActivityAttributesByPid(pid);
        Log.i(tag, "onResume, total attributes loaded: " + attrMap.size());
        showProjectToScreen(activity, attrMap);

//        if (dropboxApi != null && dropboxApi.getSession() != null
//                && dropboxApi.getSession().getOAuth2AccessToken() != null) {
//            Log.d(tag, "DropboxAPI and Session created with existing token: "
//                    + dropboxApi.getSession().getOAuth2AccessToken());
//            return;
//        }
//
//        Log.v(tag, "Check OA2 authentication result");
//        if (dropboxApi.getSession().authenticationSuccessful()) {
//            Log.v(tag, "Dropbox OA2 authentication success");
//            try {
//                Log.v(tag, "Session finish authentication, set OA2 token");
//                dropboxApi.getSession().finishAuthentication();
//                Log.v(tag, "Session finish authentication complete with token: "
//                        + dropboxApi.getSession().getOAuth2AccessToken());
//            } catch (IllegalStateException e) {
//                Toast.makeText(getActivity(), "Error during Dropbox authentication",
//                        Toast.LENGTH_SHORT).show();
//            }
//        } else {
//            Log.i(tag, "Dropbox OA2 authentication failed (possibly timing issue)");
//        }
    }

//    public void more(View view) {
//        Log.i(tag, "ShowSingleProgramActivity:more");
//        saveProjectToDb(view);;
//
//        Intent i = new Intent(this, ShowMoreActivity.class);
//        i.putExtra(Config.LOGIN_EMAIL, loginEmail);
//        i.putExtra(Config.LOGIN_PASSWORD, loginPassword);
//        i.putExtra(Config.PROJECT_PID, project.pid);
//        Log.i(tag, Config.LOGIN_EMAIL + ":" + loginEmail + ", "
//                + Config.LOGIN_PASSWORD + ":" + loginPassword + ", "
//                + Config.PROJECT_PID + ":" + project.pid);
//
//        startActivityForResult(i, Config.ACTIVITY_SHOW_MORE);
//    }

//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        switch(requestCode) {
//            case (100) : {  //show more
//                if (resultCode == Activity.RESULT_OK) {
//                    original_pname = data.getStringExtra(GeneralConfigV1.ACTIVITY_NAME);
//                }
//                break;
//            }
//        }
//    }

//    public void sharedFileDetails(){
//        Log.i(tag, "Show shared file details start, set attrMap for pid=" + pid);
//
//        socoApp.setAttrMap(attrMap);
//        socoApp.setPid(pid);
//        socoApp.dbManagerSoco = dbmgrSoco;
//
//        Intent i = new Intent(getActivity(), ShowSharedFilesActivity.class);
//        startActivityForResult(i, -1);
//    }

    public void showLocationDetails(){
        socoApp.setAttrMap(attrMap);
        socoApp.dbManagerSoco = dbmgrSoco;
        Intent i = new Intent(getActivity(), ActivityLocationActivity.class);
        startActivity(i);
    }


}
