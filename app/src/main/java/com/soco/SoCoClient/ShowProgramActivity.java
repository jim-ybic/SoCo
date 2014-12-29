package com.soco.SoCoClient;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.soco.SoCoClient.db.DBManagerSoco;
import com.soco.SoCoClient.db.Program;

public class ShowProgramActivity extends ActionBarActivity {

    DBManagerSoco dbmgrSoco = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_program);

        Intent intent = getIntent();
        String pname = intent.getStringExtra(Program.PROGRAM_PNAME);

        dbmgrSoco = new DBManagerSoco(this);
        Program program = dbmgrSoco.loadProgram(pname);
        showProgram(program);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
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
    }

    Program getProgram(View view){
        Program program = new Program();
        program.pname = ((TextView) findViewById(R.id.spname)).getText().toString();
        program.pdate = ((EditText) findViewById(R.id.spdate)).getText().toString();
        program.ptime = ((EditText) findViewById(R.id.sptime)).getText().toString();
        program.pplace = ((EditText) findViewById(R.id.spplace)).getText().toString();
        return program;
    }

    public void saveProgram(View view){
        Program p = getProgram(view);
        dbmgrSoco.update(p);
        Toast.makeText(getApplicationContext(), "Program saved.", Toast.LENGTH_SHORT).show();
    }

    public void completeProgram(View view){
        Program p = getProgram(view);
        p.pcomplete = 1;
        dbmgrSoco.update(p);
        Toast.makeText(getApplicationContext(), "Program complete.", Toast.LENGTH_SHORT).show();
    }

}
