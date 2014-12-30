package com.soco.SoCoClient;

import android.content.Intent;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;


public class LoginActivity extends ActionBarActivity {

    EditText etUsername;
    EditText etPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = (EditText) findViewById(R.id.et_login_username);
        etUsername.setText("Jim");
        etPassword = (EditText) findViewById(R.id.et_login_password);
        etPassword.setText("12345678");
    }

    public void login (View view) {
        String user = etUsername.getText().toString();
        String password = etPassword.getText().toString();
        Toast.makeText(getApplicationContext(), "Login: " + user + "/" + password,
                Toast.LENGTH_SHORT).show();

        boolean loginSuccess = true;
        // TODO: add login logic

        if(loginSuccess) {
            Intent intent = new Intent(this, ShowActiveProgramsActivity.class);
            startActivity(intent);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_login, menu);
        return true;
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
}
