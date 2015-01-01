package com.soco.SoCoClient.ui;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.soco.SoCoClient.config.Config;
import com.soco.SoCoClient.R;


public class LoginActivity extends ActionBarActivity {


    EditText etLoginEmail;
    EditText etLoginPassword;

    String loginEmail;
    String loginPassword;
    String nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etLoginEmail = (EditText) findViewById(R.id.et_login_email);
        etLoginEmail.setText("jim.ybic@gmail.com");
        etLoginPassword = (EditText) findViewById(R.id.et_login_password);
        etLoginPassword.setText("12345678");
    }

//    void readFile(){
//            File file = new File(Config.PROFILE_FILENAME);
//            if (!file.exists()) {
//                file = new File(getApplicationContext().getFilesDir(), Config.PROFILE_FILENAME);
//
//                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
//                        new FileOutputStream(file), Config.ENCODING));
//                writer.write(Config.PROFILE_EMAIL + ":" + loginEmail);
//                writer.flush();
//                writer.close();
//            }
//
//            BufferedReader reader = new BufferedReader(new InputStreamReader(
//                    new FileInputStream(file), Config.ENCODING));
//            String s = reader.readLine();
//            while (!s.isEmpty()) {
//                s = reader.readLine();
//            }
//    }

    void updateProfile(String loginEmail) {
            Log.i("soco", "Load profile.");
            SharedPreferences settings = getSharedPreferences(Config.PROFILE_FILENAME, 0);
            SharedPreferences.Editor editor = settings.edit();

            String email = settings.getString(Config.PROFILE_EMAIL, "");
            if(email.isEmpty()) {
                Log.i("soco", "Create new profile, " + Config.PROFILE_EMAIL + ":" + loginEmail);
                editor.putString(Config.PROFILE_EMAIL, loginEmail);
                editor.commit();
            } else {
                Log.i("soco", "Load existing profile, " + Config.PROFILE_EMAIL + ":" + email);
                String n = settings.getString(Config.PROFILE_NICKNAME, "");
                if (!n.isEmpty())
                    nickname = n;
            }
    }

    public void login (View view) {
        loginEmail = etLoginEmail.getText().toString();
        loginPassword = etLoginPassword.getText().toString();
        nickname = loginEmail;

        updateProfile(loginEmail);

        Toast.makeText(getApplicationContext(), "Hello, " + nickname,
                Toast.LENGTH_SHORT).show();

        boolean loginSuccess = true;
        // TODO: add login logic

        if(loginSuccess) {
            Intent intent = new Intent(this, ShowActiveProgramsActivity.class);
            intent.putExtra(Config.LOGIN_EMAIL, loginEmail);
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
