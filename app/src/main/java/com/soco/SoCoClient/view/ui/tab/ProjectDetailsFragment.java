package com.soco.SoCoClient.view.ui.tab;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.Fragment;
import android.support.v7.app.ActionBarActivity;
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

import com.dropbox.client2.DropboxAPI;
import com.dropbox.client2.android.AndroidAuthSession;
import com.soco.SoCoClient.control.config.Config;
import com.soco.SoCoClient.R;
import com.soco.SoCoClient.control.config.DataConfig;
import com.soco.SoCoClient.control.db.DBManagerSoco;
import com.soco.SoCoClient.control.SocoApp;
import com.soco.SoCoClient.control.dropbox.DropboxUtil;
import com.soco.SoCoClient.control.util.ProjectUtil;
import com.soco.SoCoClient.control.util.SignatureUtil;
import com.soco.SoCoClient.model.Program;
import com.soco.SoCoClient.model.Project;
import com.soco.SoCoClient.view.ProjectLocationActivity;
import com.soco.SoCoClient.view.ShowSharedFilesActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;
import java.util.Map;

import static com.soco.SoCoClient.control.config.DataConfig.*;

public class ProjectDetailsFragment extends Fragment implements View.OnClickListener {

    public static String tag="ShowSingleProgram";

    View rootView;

    // Local views
    EditText pdateEditText, ptimeEditText;
    EditText et_spname, et_spdate, et_sptime, et_spdesc;
    TextView tv_spdate, tv_sptime, tv_splocname, tv_spdesc, tv_sptag;
    EditText et_splocation, et_sptag;
    AutoCompleteTextView et_spphone_auto, et_spemail_auto;
    TableRow tr_spdatetime, tr_splocation, tr_spdesc, tr_spphone, tr_spemail, tr_sptag;
    Button bt_clear_pdatetime, bt_splocname;    //, bt_spdetails, bt_spupdates;
    ScrollView sv_sproject, sv_supdates;

    // Local variables
    DBManagerSoco dbmgrSoco = null;
    Program program = null;
//    String loginEmail;
//    String loginPassword;
    String original_pname;

    Project project;
    ArrayList<HashMap<String, String>> attrMap;

    DatePickerDialog pdatePickerDialog = null;
    TimePickerDialog ptimePickerDialog = null;
    SimpleDateFormat dateFormatter = new SimpleDateFormat("dd-MM-yyyy", Locale.US);

    DropboxAPI<AndroidAuthSession> dropboxApi;
    int pid;
    String pid_onserver;

    private ArrayList<Map<String, String>> listNamePhone, listNameEmail;
    private SimpleAdapter mAdapterPhone, mAdapterEmail;

    SocoApp socoApp = (SocoApp)(Context)getActivity();
    //TODO: remove below test script

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        Log.d(tag, "on create view");
        rootView = inflater.inflate(R.layout.fragment_project_details, container, false);
        Log.d(tag, "Found root view: " + rootView);

        //moved from onCreate
        findViewsById();
        showProjectToScreen(project, attrMap);
        setDateTimeField();
        PopulatePhoneEmailList();

        //set button listeners
        rootView.findViewById(R.id.bt_save_project).setOnClickListener(this);
        rootView.findViewById(R.id.bt_finish_project).setOnClickListener(this);
        rootView.findViewById(R.id.et_spdate).setOnClickListener(this);
        rootView.findViewById(R.id.et_sptime).setOnClickListener(this);
        rootView.findViewById(R.id.bt_splocation).setOnClickListener(this);
        rootView.findViewById(R.id.bt_shared_file_details).setOnClickListener(this);
        rootView.findViewById(R.id.bt_call).setOnClickListener(this);
        rootView.findViewById(R.id.bt_email).setOnClickListener(this);

        return rootView;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.bt_save_project:
                saveProjectToDb();
                break;
            case R.id.bt_finish_project:
                setProjectCompleted();
                break;
            case R.id.et_spdate:
                clickDate();
                break;
            case R.id.et_sptime:
                clickTime();
                break;
            case R.id.bt_splocation:
                showLocationDetails();
                break;
            case R.id.bt_shared_file_details:
                sharedFileDetails();
                break;
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
//        setContentView(R.layout.activity_show_single_project);
//        findViewsById();

//        loginEmail = ((SocoApp)getActivity()).loginEmail;
//        loginPassword = ((SocoApp)getActivity()).loginPassword;
//        pid = ((SocoApp)getActivity()).pid;

        if(socoApp == null){
            Log.i(tag, "socoApp object is null, creating new");
            socoApp = (SocoApp)(getActivity().getApplication());
            Log.i(tag, "socoApp object created: " + socoApp);
        }
        //todo: remove below testing script
//        socoApp.pid = 15;
//        socoApp.pid_onserver = "15";

        pid = socoApp.pid;
//        pid_onserver = ((SocoApp)getActivity()).pid_onserver;
        pid_onserver = socoApp.pid_onserver;
        Log.d(tag, "pid is " + pid + ", pid_onserver is " + pid_onserver);

        Log.i(tag, "onCreate, get project properties: "
//                        + Config.LOGIN_EMAIL + ":" + loginEmail + ", "
//                        + Config.LOGIN_PASSWORD + ":" + loginPassword + ", "
                        + Config.PROJECT_PID + ":" + pid + ","
                        + Config.PROJECT_PID_ONSERVER + ":" + pid_onserver
        );

        dbmgrSoco = new DBManagerSoco(getActivity().getApplication());
        project = dbmgrSoco.loadProjectByPid(pid);
        attrMap = dbmgrSoco.loadProjectAttributesByPid(pid);
//        showProjectToScreen(project, attrMap);

        dropboxApi = DropboxUtil.initDropboxApiAuthentication(
                Config.ACCESS_KEY, Config.ACCESS_SECRET, Config.OA2_TOKEN,
                getActivity().getApplication());
        socoApp.dropboxApi = dropboxApi;

//        setDateTimeField();
//        PopulatePhoneEmailList();
    }

    public void PopulatePhoneEmailList(){
        SocoApp app = socoApp;

        Log.i(tag, "Populate phone list");
        listNamePhone = app.loadNamePhoneList();
        mAdapterPhone = new SimpleAdapter(getActivity(), listNamePhone, R.layout.custcontview ,
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
        mAdapterEmail = new SimpleAdapter(getActivity(), listNameEmail, R.layout.custcontview ,
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


//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        // Inflate the menu; this adds items to the action bar if it is present.
//        getSupportMenuInflater().inflate(R.menu.menu_show_single_program, menu);
//        return true;
//    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
//        getMenuInflater().inflate(R.menu.menu_show_single_program, menu);
        inflater.inflate(R.menu.menu_show_single_program, menu);
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
//        tr_sptime = (TableRow) rootView.findViewById(R.id.tr_sptime);
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
//        bt_spdetails = (Button) rootView.findViewById(R.id.bt_spdetails);
//        bt_spupdates = (Button) rootView.findViewById(R.id.bt_spupdates);

//        sv_supdates = (ScrollView) rootView.findViewById(R.id.sv_supdates);

//        bt_clear_pdatetime = (Button) rootView.findViewById(R.id.bt_clear_pdatetime);
    }

    void gotoPreviousScreen(){
        getActivity().finish();
    }

//    @Override
//    public boolean onPrepareOptionsMenu(Menu menu) {
//        return super.onPrepareOptionsMenu(menu);
//    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        Log.i(tag, "onOptionsItemSelected");

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
//                    tv_spdate.setVisibility(View.GONE);
//                    et_spdate.setVisibility(View.GONE);
                    et_sptime.setText("");
                    tr_spdatetime.setVisibility(View.GONE);
//                    et_sptime.setVisibility(View.GONE);
                }
                break;
            case R.id.mn_location:
                if (et_splocation.getText().toString().isEmpty()){
                    tr_splocation.setVisibility(View.VISIBLE);
//                    et_splocation.setVisibility(View.VISIBLE);
//                    bt_splocname.setVisibility(View.VISIBLE);
                } else {
                    et_splocation.setText("");
                    tr_splocation.setVisibility(View.GONE);
//                    et_splocation.setVisibility(View.GONE);
//                    bt_splocname.setVisibility(View.GONE);
                }
                break;
            case R.id.mn_desc:
                if (et_spdesc.getText().toString().isEmpty()){
                    tr_spdesc.setVisibility(View.VISIBLE);
//                    et_spdesc.setVisibility(View.VISIBLE);
                } else {
                    et_spdesc.setText("");
                    tr_spdesc.setVisibility(View.GONE);
//                    et_spdesc.setVisibility(View.GONE);
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
        }

        return super.onOptionsItemSelected(item);
    }

    public void setVisiblePdesc(View view) {
        tr_spdesc.setVisibility(View.VISIBLE);
        et_spdesc.requestFocus();
    }

    public void setVisibleDateTimePlace(View view) {
        tr_spdatetime.setVisibility(View.VISIBLE);
//        tr_sptime.setVisibility(View.VISIBLE);
        tr_splocation.setVisibility(View.VISIBLE);
    }

    public void setVisiblePhoneEmail(View view) {
        tr_spphone.setVisibility(View.VISIBLE);
        tr_spemail.setVisibility(View.VISIBLE);
    }

//    public void clearPdate(View view) {
//        et_spdate.setText("");
//        tr_spdate.setVisibility(View.GONE);
//    }

    public void clearPdatetime(View view) {
        et_spdate.setText("");
//        tv_spdate.setVisibility(View.GONE);
//        et_spdate.setVisibility(View.GONE);
        et_sptime.setText("");
//        tv_sptime.setVisibility(View.GONE);
//        et_sptime.setVisibility(View.GONE);
    }

    public void clearPplace(View view) {
        et_splocation.setText("");
        tr_splocation.setVisibility(View.GONE);
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

//    @Override
//    public void onBackPressed() {
//        gotoPreviousScreen();
//    }

//    @Override
//    public void onClick(View view) {
//        if(view == pdateEditText) {
//            pdatePickerDialog.show();
//        }
//        if(view == ptimeEditText) {
//            ptimePickerDialog.show();
//        }
//    }

    public void clickDate(){
        pdatePickerDialog.show();
    }

    public void clickTime(){
        ptimePickerDialog.show();
    }

    void setDateTimeField() {
        // Date picker
//        pdateEditText.setOnClickListener(this);
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
//        ptimeEditText.setOnClickListener(this);
        int hour = newCalendar.get(Calendar.HOUR_OF_DAY);
        int minute = newCalendar.get(Calendar.MINUTE);
        ptimePickerDialog = new TimePickerDialog(getActivity(),
                new TimePickerDialog.OnTimeSetListener() {
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        ptimeEditText.setText( selectedHour + ":" + selectedMinute);
                    }
                }, hour, minute, true); //Yes 24 hour time

    }

    public void showProjectToScreen(Project p, ArrayList<HashMap<String, String>> attrMap){

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
//        bt_clear_pdatetime.setVisibility(View.GONE);

        tr_splocation.setVisibility(View.GONE);
//        tv_splocname.setVisibility(View.GONE);
//        et_splocation.setVisibility(View.GONE);
//        bt_splocname.setVisibility(View.GONE);

        tr_spdesc.setVisibility(View.GONE);
//        tv_spdesc.setVisibility(View.GONE);
//        et_spdesc.setVisibility(View.GONE);

        tr_spphone.setVisibility(View.GONE);
        tr_spemail.setVisibility(View.GONE);

        tr_sptag.setVisibility(View.GONE);

        //show available attributes
        for(HashMap<String, String> map : attrMap) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                String attr_name = entry.getKey();
                String attr_value = entry.getValue();
                Log.d(tag, "Current attr: " + attr_name + ", " + attr_value);

                if (attr_name.equals(DataConfig.ATTRIBUTE_NAME_DATE)) {    //date
                    tr_spdatetime.setVisibility(View.VISIBLE);
                    tv_spdate.setVisibility(View.VISIBLE);
                    et_spdate.setVisibility(View.VISIBLE);
                    et_spdate.setText(attr_value, TextView.BufferType.EDITABLE);
//                    bt_clear_pdatetime.setVisibility(View.VISIBLE);
                } else if (attr_name.equals(DataConfig.ATTRIBUTE_NAME_TIME)) {   //time
                    tr_spdatetime.setVisibility(View.VISIBLE);
                    tv_sptime.setVisibility(View.VISIBLE);
                    et_sptime.setVisibility(View.VISIBLE);
                    et_sptime.setText(attr_value, TextView.BufferType.EDITABLE);
//                    bt_clear_pdatetime.setVisibility(View.VISIBLE);
                } else if (attr_name.equals(DataConfig.ATTRIBUTE_NAME_LOCNAME)) {   //locname
                    tr_splocation.setVisibility(View.VISIBLE);
//                    tv_splocname.setVisibility(View.VISIBLE);
//                    et_splocation.setVisibility(View.VISIBLE);
                    et_splocation.setText(attr_value, TextView.BufferType.EDITABLE);
//                    bt_splocname.setVisibility(View.VISIBLE);
                } else if (attr_name.equals(DataConfig.ATTRIBUTE_NAME_DESC)) {   //description
                    tr_spdesc.setVisibility(View.VISIBLE);
//                    et_spdesc.setVisibility(View.VISIBLE);
                    et_spdesc.setText(attr_value, TextView.BufferType.EDITABLE);
                } else if (attr_name.equals(DataConfig.ATTRIBUTE_NAME_PHONE)) {   //phone
                    tr_spphone.setVisibility(View.VISIBLE);
                    et_spphone_auto.setText(attr_value, TextView.BufferType.EDITABLE);
                } else if (attr_name.equals(DataConfig.ATTRIBUTE_NAME_EMAIL)) {   //email
                    tr_spemail.setVisibility(View.VISIBLE);
                    et_spemail_auto.setText(attr_value, TextView.BufferType.EDITABLE);
                }
//                else if (attr_name.equals(DataConfig.ATTRIBUTE_NAME_TAG)) {    //tag
//                    tr_sptag.setVisibility(View.VISIBLE);
//                    et_sptag.setText(attr_value, TextView.BufferType.EDITABLE);
//                }
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
        ((TextView) rootView.findViewById(R.id.tv_shared_file_summary)).setText(summary,
                TextView.BufferType.EDITABLE);


    }

//    void refreshAttrMap(){
//        Log.i(tag, "refresh attr map");
//        attrMap = dbmgrSoco.loadProjectAttributesByPid(pid);
//    }

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
        Log.i(tag, "Save to db the project: " + project.pid + ", " + project.pname);
        String pname = et_spname.getText().toString();
        dbmgrSoco.updateProjectName(project.pid, pname);
        ProjectUtil.serverUpdateProjectName(String.valueOf(pid), getActivity().getApplication(),
                pname, pid_onserver);

        String ptag = et_sptag.getText().toString();
        dbmgrSoco.updateProjectTag(project.pid, ptag);
        //TODO: server interface to update project tag

        HashMap<String, String> attrMap2 = collectProjectAttributesFromScreen();
        if(attrMap2.size() > 0) {
            dbmgrSoco.updateDbProjectAttributes(pid, attrMap2);
            ProjectUtil.serverSetProjectAttribute(String.valueOf(pid),
                    getActivity().getApplication(),
                    pname, pid_onserver, attrMap2);
        }

        Toast.makeText(getActivity(), "Project saved.", Toast.LENGTH_SHORT).show();
        attrMap = dbmgrSoco.loadProjectAttributesByPid(pid);
    }

    public void setProjectCompleted(){
        Log.i(tag, "Set project complete start");
        saveProjectToDb();
        dbmgrSoco.updateProjectActiveness(pid, DataConfig.VALUE_PROJECT_INACTIVE);
        Toast.makeText(getActivity(), "Project complete, well done.",
                Toast.LENGTH_SHORT).show();
        ProjectUtil.serverArchiveProject(String.valueOf(pid), getActivity().getApplication(), pid_onserver);
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
                        saveProjectToDb();
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
                    program.pphone = s;
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
                    saveProjectToDb();
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
        attrMap = dbmgrSoco.loadProjectAttributesByPid(pid);
        Log.i(tag, "onResume, total attributes loaded: " + attrMap.size());
        showProjectToScreen(project, attrMap);

        if (dropboxApi != null && dropboxApi.getSession() != null
                && dropboxApi.getSession().getOAuth2AccessToken() != null) {
            Log.d(tag, "DropboxAPI and Session created with existing token: "
                    + dropboxApi.getSession().getOAuth2AccessToken());
            return;
        }

        Log.v(tag, "Check OA2 authentication result");
        if (dropboxApi.getSession().authenticationSuccessful()) {
            Log.v(tag, "Dropbox OA2 authentication success");
            try {
                Log.v(tag, "Session finish authentication, set OA2 token");
                dropboxApi.getSession().finishAuthentication();
                Log.v(tag, "Session finish authentication complete with token: "
                        + dropboxApi.getSession().getOAuth2AccessToken());
            } catch (IllegalStateException e) {
                Toast.makeText(getActivity(), "Error during Dropbox authentication",
                        Toast.LENGTH_SHORT).show();
            }
        } else {
            Log.i(tag, "Dropbox OA2 authentication failed (possibly timing issue)");
        }
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

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch(requestCode) {
            case (100) : {  //show more
                if (resultCode == Activity.RESULT_OK) {
//                    Log.i(tag, "onActivityResult, original values: " + loginEmail + ", " + loginPassword);
//                    loginEmail = data.getStringExtra(Config.LOGIN_EMAIL);
//                    loginPassword = data.getStringExtra(Config.LOGIN_PASSWORD);
                    original_pname = data.getStringExtra(Config.PROGRAM_PNAME);
//                    Log.i(tag, "get string extra: "
//                            + loginEmail + ", " + loginPassword + ", " + original_pname);

                }
                break;
            }
//            case (101) : {  //add file
//                if (resultCode == Activity.RESULT_OK) {
//                    Uri uri = null;
//                    if (data != null) {
//                        uri = data.getData();
//                        Log.i(tag, "File selected with uri: " + uri.toString());
//                        FileUtils.checkUriMeta(getContentResolver(), uri);
//                        DropboxUtil.uploadToDropbox(uri, loginEmail, loginPassword, pid, dropboxApi,
//                                getContentResolver(), getActivity());
//                        SocoApp app = (SocoApp) getActivity();
//                        app.setUploadStatus(SocoApp.UPLOAD_STATUS_START);
//                        // check result
//                        boolean isSuccess = false;
//                        for (int i=1; i<= Config.UPLOAD_RETRY; i++) {
//                            Log.d(tag, "Wait for upload parse: " + i + "/" + Config.UPLOAD_RETRY);
//                            SystemClock.sleep(Config.UPLOAD_WAIT);
//                            Log.d(tag, "Current upload status is: " + app.getUploadStatus());
//                            if(app.getUploadStatus().equals(SocoApp.UPLOAD_STATUS_SUCCESS)) {
//                                isSuccess = true;
//                                break;
//                            }
//                            else if (app.getUploadStatus().equals(SocoApp.UPLOAD_STATUS_FAIL)){
//                                isSuccess = false;
//                                break;
//                            }
//                        }
//                        if(isSuccess) {
//                            Log.i(tag, "File upload success");
//                            new AlertDialog.Builder(this)
//                                    .setTitle("File upload success")
//                                    .setMessage("File has been saved in the cloud")
//                                    .setPositiveButton("OK", null)
//                                    .show();
//                            ProjectUtil.addSharedFileToDb(uri, loginEmail, loginPassword, pid,
//                                    getContentResolver(), dbmgrSoco);
//                        }
//                        else {
//                            Log.i(tag, "File upload failed");
//                            new AlertDialog.Builder(this)
//                                    .setTitle("File upload failed")
//                                    .setMessage("Review upload details and try again")
//                                    .setPositiveButton("OK", null)
//                                    .show();
//                        }
//                    }
//                }
//                break;
//            }
        }
    }

    public void sharedFileDetails(){
        Log.i(tag, "Show shared file details start, set attrMap for pid=" + pid);

        socoApp.setAttrMap(attrMap);
        socoApp.setPid(pid);
        socoApp.dbManagerSoco = dbmgrSoco;

        Intent i = new Intent(getActivity(), ShowSharedFilesActivity.class);
        startActivityForResult(i, -1);
    }

    public void showLocationDetails(){
        socoApp.setAttrMap(attrMap);
        socoApp.dbManagerSoco = dbmgrSoco;
        Intent i = new Intent(getActivity(), ProjectLocationActivity.class);
        startActivity(i);
    }


}
