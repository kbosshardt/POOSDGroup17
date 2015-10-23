
package com.poosdseventeen.targetguys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.Parse;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

public class SignUpActivity extends Activity {

    private EditText nameText;
    private EditText emailText;
    private EditText usernameText;
    private EditText passwordText;
    private TextView mErrorField;
    private Button SignUpButton;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        nameText = (EditText) findViewById(R.id.nameField);
        emailText = (EditText) findViewById(R.id.emailField);
        usernameText = (EditText) findViewById(R.id.usernameField);
        passwordText = (EditText) findViewById(R.id.passwordField);
        mErrorField = (TextView) findViewById(R.id.error_messages);
        SignUpButton = (Button) findViewById(R.id.signUpButton);


        ParseAnalytics.trackAppOpenedInBackground(getIntent());

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_signup, menu);
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

    public void register(final View v){
        if(usernameText.getText().length() == 0 || passwordText.getText().length() == 0)
            return;

        v.setEnabled(false);

        // delete current user
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.logOut();

        // create new user
        ParseUser user = new ParseUser();
        user.setUsername(usernameText.getText().toString());
        user.setPassword(passwordText.getText().toString());
        user.setEmail(emailText.getText().toString());
        mErrorField.setText("");

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(getApplicationContext(), "Sign Up Error.", Toast.LENGTH_LONG).show();
                    // Sign up didn't succeed. Look at the ParseException
                    // to figure out what went wrong
                    switch(e.getCode()){
                        case ParseException.USERNAME_TAKEN:
                            mErrorField.setText("Sorry, this username has already been taken.");
                            break;
                        case ParseException.USERNAME_MISSING:
                            mErrorField.setText("Sorry, you must supply a username to register.");
                            break;
                        case ParseException.PASSWORD_MISSING:
                            mErrorField.setText("Sorry, you must supply a password to register.");
                            break;
                        default:
                            mErrorField.setText(e.getLocalizedMessage());
                    }
                    v.setEnabled(true);
                }
            }
        });
    }

    public void showLogin(View v) {
        Intent intent = new Intent(this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
