package com.soco.SoCoClient._test;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.view.login.ActivityLogin;
import com.soco.SoCoClient.view.events.ActivityEventDetail;

public class TestEntryScreen extends ActionBarActivity {

    static String tag = "TestEntry";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test_entry_screen);

        dev(null);  //quick dev mode
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_test_entry_screen, menu);
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

    public void dev (View view) {
        Log.i(tag, "dev");
        Intent intent = new Intent(this, ActivityLogin.class);
        startActivity(intent);
    }

    public void test (View view) {
        Log.i(tag, "test");
        Intent intent = new Intent(this, ActivityEventDetail.class);
        startActivity(intent);
    }

}