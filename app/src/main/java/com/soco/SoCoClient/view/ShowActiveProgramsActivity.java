package com.soco.SoCoClient.view;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.content.Intent;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SimpleAdapter;
import android.widget.Toast;

import com.soco.SoCoClient.control.Config;
import com.soco.SoCoClient.R;
import com.soco.SoCoClient.control.DBManagerSoco;
import com.soco.SoCoClient.model.Program;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class ShowActiveProgramsActivity extends ActionBarActivity {

    // Local view
    private ListView lv_active_programs;

    // Local variable
    private DBManagerSoco dbmgrSoco;
    private List<Program> programs;
    private String loginEmail, loginPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_active_programs);
        findViewsById();

        Intent intent = getIntent();
        loginEmail = intent.getStringExtra(LoginActivity.LOGIN_EMAIL);
        loginPassword = intent.getStringExtra(LoginActivity.LOGIN_PASSWORD);

        dbmgrSoco = new DBManagerSoco(this);
        programs = dbmgrSoco.loadPrograms(Config.PROGRAM_ACTIVE);

        listPrograms(null);

        lv_active_programs.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @SuppressWarnings("unchecked")
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListView listView = (ListView) parent;
                HashMap<String, String> map = (HashMap<String, String>)
                        listView.getItemAtPosition(position);
                String name = map.get(Config.PROGRAM_PNAME);

                Intent intent = new Intent(view.getContext(), ShowSingleProgramActivity.class);
                intent.putExtra(Config.PROGRAM_PNAME, name);
                intent.putExtra(LoginActivity.LOGIN_EMAIL, loginEmail);
                intent.putExtra(LoginActivity.LOGIN_PASSWORD, loginPassword);
                startActivity(intent);
            }
        });
    }

    private void findViewsById() {
        lv_active_programs = (ListView) findViewById(R.id.lv_active_programs);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_show_active_programs, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_clean_up) {
            Log.i("setting", "Click on Clean up.");
            new AlertDialog.Builder(this)
                    .setMessage("Delete all programs?")
                    .setCancelable(true)
                    .setPositiveButton("Yes", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dbmgrSoco.cleanDB();
                            Toast.makeText(getApplicationContext(),
                                    "All Programs deleted.", Toast.LENGTH_SHORT).show();
                            Intent i = new Intent(getApplicationContext(),
                                    ShowActiveProgramsActivity.class);
                            i.putExtra(LoginActivity.LOGIN_EMAIL, loginEmail);
                            startActivity(i);;
                        }
                    })
                    .setNegativeButton("No", null)
                    .show();
            return true;
        }
        if (id == R.id.action_profile) {
            Log.i("setting", "Click on Profile.");
            Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
            intent.putExtra(LoginActivity.LOGIN_EMAIL, loginEmail);
            intent.putExtra(LoginActivity.LOGIN_PASSWORD, loginPassword);
            startActivity(intent);
        }

        return super.onOptionsItemSelected(item);
    }

    public void createProgram(View view) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Create new program");
        alert.setMessage("So I want to ...");
        final EditText input = new EditText(this);
        alert.setView(input);

        alert.setPositiveButton("Done", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String n = input.getText().toString();
                Program p = new Program(n);
                dbmgrSoco.add(p);
                Log.i("new", "New program created: " + n);
                Toast.makeText(getApplicationContext(), "Program created.", Toast.LENGTH_SHORT).show();
                programs = dbmgrSoco.loadPrograms(Config.PROGRAM_ACTIVE);
                listPrograms(null);
            }
        });

        alert.setNeutralButton("Details", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
                String n = input.getText().toString();
                Program p = new Program(n);
                dbmgrSoco.add(p);
                Log.i("new", "New program created: " + n);

                Intent intent = new Intent(getApplicationContext(), ShowSingleProgramActivity.class);
                intent.putExtra(Config.PROGRAM_PNAME, n);
                intent.putExtra(LoginActivity.LOGIN_EMAIL, loginEmail);
                intent.putExtra(LoginActivity.LOGIN_PASSWORD, loginPassword);
                Log.i("new", "Start activity to view program details");
                startActivity(intent);
            }
        });

        alert.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int whichButton) {
            }
        });

        alert.show();
    }

    public void listPrograms(View view) {
        ArrayList<Map<String, String>> list = new ArrayList<>();
        for (Program program : programs) {
            HashMap<String, String> map = new HashMap<>();
            map.put(Config.PROGRAM_PNAME, program.pname);
            map.put(Config.PROGRAM_PINFO, program.getMoreInfo());
            list.add(map);
        }
        SimpleAdapter adapter = new SimpleAdapter(this, list,
                android.R.layout.simple_list_item_2,
                new String[]{Config.PROGRAM_PNAME, Config.PROGRAM_PINFO},
                new int[]{android.R.id.text1, android.R.id.text2});
        lv_active_programs.setAdapter(adapter);
    }

    public void showCompletedPrograms(View view) {
        Intent intent = new Intent(this, ShowCompletedProgramsActivity.class);
        intent.putExtra(LoginActivity.LOGIN_EMAIL, loginEmail);
        intent.putExtra(LoginActivity.LOGIN_PASSWORD, loginPassword);
        startActivity(intent);
    }

    public void exit(View view) {
        Intent intent = new Intent(getApplicationContext(), LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        intent.putExtra("EXIT", true);
        startActivity(intent);
    }

}
