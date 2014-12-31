package com.soco.SoCoClient;

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


public class LoginActivity extends ActionBarActivity {

    public static String LOGIN_USERNAME = "LOGIN_USER_NAME";

    EditText etUsername;
    EditText etPassword;

    String username;
    String password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        etUsername = (EditText) findViewById(R.id.et_login_username);
        etUsername.setText("Jim");
        etPassword = (EditText) findViewById(R.id.et_login_password);
        etPassword.setText("12345678");

    }

//    void readFile(){
//            File file = new File(Config.PREFERENCE_FILENAME);
//            if (!file.exists()) {
//                file = new File(getApplicationContext().getFilesDir(), Config.PREFERENCE_FILENAME);
//
//                BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(
//                        new FileOutputStream(file), Config.ENCODING));
//                writer.write(Config.PREFERENCE_USERNAME + ":" + username);
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

    void updatePreference(String username) {
//        try {
            Log.i("soco", "Loading preference file.");
            SharedPreferences settings = getSharedPreferences(Config.PREFERENCE_FILENAME, 0);
            SharedPreferences.Editor editor = settings.edit();

            String user = settings.getString(Config.PREFERENCE_USERNAME,"");
            if(user.isEmpty()) {
                Log.i("soco", "Update preference, " + Config.PREFERENCE_USERNAME + ":" + username);
                editor.putString(Config.PREFERENCE_USERNAME, username);
                editor.commit();
            } else {
                Log.i("soco", "Load preference, " + Config.PREFERENCE_USERNAME + ":" + user);
            }

//        }
//        catch (Exception e){
//            System.out.println("File not found.");
//            e.printStackTrace();
//            return;
//        }
    }

    public void login (View view) {
        username = etUsername.getText().toString();
        password = etPassword.getText().toString();
        Toast.makeText(getApplicationContext(), "Hello, " + username,
                Toast.LENGTH_SHORT).show();

        updatePreference(username);

        boolean loginSuccess = true;
        // TODO: add login logic

        if(loginSuccess) {
            Intent intent = new Intent(this, ShowActiveProgramsActivity.class);
            intent.putExtra(LOGIN_USERNAME, username);
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
