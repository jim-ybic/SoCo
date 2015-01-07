package com.soco.SoCoClient.view;

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

import com.soco.SoCoClient.R;
import com.soco.SoCoClient.control.LoginUtil;
import com.soco.SoCoClient.control.ProfileUtil;


public class LoginActivity extends ActionBarActivity {

    // Local constants
    public static String LOGIN_EMAIL = "email";
    public static String LOGIN_PASSWORD = "password";
    public static String FLAG_EXIT = "exit";

    // Local views
    EditText et_login_email;
    EditText et_login_password;

    // Local variables
    String loginEmail;
    String loginPassword;
    String nickname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        findViewsById();

        if (getIntent().getBooleanExtra(FLAG_EXIT, false))
            finish();

        // Testing login
        et_login_email.setText("jim.ybic@gmail.com");
        et_login_password.setText("12345678");
    }

    private void findViewsById() {
        et_login_email = (EditText) findViewById(R.id.et_login_email);
        et_login_password = (EditText) findViewById(R.id.et_login_password);
    }

    void updateProfile(String loginEmail) {
        ProfileUtil.ready(getApplicationContext(), loginEmail);
        nickname = ProfileUtil.getNickname(getApplicationContext(), loginEmail);
    }

    public void login (View view) {
        loginEmail = et_login_email.getText().toString();
        loginPassword = et_login_password.getText().toString();
        updateProfile(loginEmail);

        boolean loginSuccess = LoginUtil.validateLogin(loginEmail, loginPassword);
        if(loginSuccess) {
            Toast.makeText(getApplicationContext(), "Hello, " + nickname,
                    Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(this, ShowActiveProgramsActivity.class);
            intent.putExtra(LOGIN_EMAIL, loginEmail);
            intent.putExtra(LOGIN_PASSWORD, loginPassword);
            startActivity(intent);
        } else {
            Toast.makeText(getApplicationContext(), "Oops, login failed.",
                    Toast.LENGTH_SHORT).show();
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

}
