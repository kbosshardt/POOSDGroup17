
package com.poosdseventeen.targetguys;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.parse.LogInCallback;
import com.parse.ParseAnalytics;
import com.parse.ParseException;
import com.parse.ParseFacebookUtils;
import com.parse.ParseUser;
import com.parse.SignUpCallback;

import java.util.Arrays;
import java.util.List;


/**
 * Created by Kourtney Bosshardt on 10/23/2015.
 */

public class SignUpActivity extends Activity{

    private EditText nameText;
    private EditText emailText;
    private EditText usernameText;
    private EditText passwordText;
    private TextView mErrorField;
    private Button SignUpButton;
    List<String> permissions;
    private String gender;




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
        permissions = Arrays.asList("public_profile", "email");


        ParseAnalytics.trackAppOpenedInBackground(getIntent());



        //Gender spinner initialization
        Spinner spinner = (Spinner) findViewById(R.id.gender_spinner);
        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.gender_array, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view,
                                       int position, long id) {
                gender = (String) parent.getItemAtPosition(position);
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                // TODO Auto-generated method stub
            }
        });

        // Finished Gender Spinner

    }

    // For Single-Sign On Facebook call
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        ParseFacebookUtils.onActivityResult(requestCode, resultCode, data);
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

    // register the user into the parse database
    public void register(final View v){

        checkFields();
        v.setEnabled(false);

        // delete current user
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.logOut();

        // create new user
        ParseUser user = new ParseUser();
        user.put("name", nameText.getText().toString());
        user.setUsername(usernameText.getText().toString());
        user.setPassword(passwordText.getText().toString());
        user.setEmail(emailText.getText().toString());
        user.put("gender", gender);
        mErrorField.setText("");

        user.signUpInBackground(new SignUpCallback() {
            @Override
            public void done(ParseException e) {
                if (e == null) {
                    showHome(v);
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
                            mErrorField.setText("Oops! Looks like something went wrong.");
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

    public void showHome(View v){
        Intent intent = new Intent(SignUpActivity.this, HomeActivity.class);
        startActivity(intent);
        finish();
    }


    public void signInFacebook(final View v){
        ParseFacebookUtils.logInWithReadPermissionsInBackground(this, permissions, new LogInCallback() {
            @Override
            public void done(ParseUser user, ParseException err) {
                if (user == null) {
                    Log.d("MyApp", "Uh oh. The user cancelled the Facebook login.");
                } else if (user.isNew()) {
                    Log.d("MyApp", "User signed up and logged in through Facebook!");
                } else {
                    Log.d("MyApp", "User logged in through Facebook!");
                    showHome(v);
                }
            }
        });
    }


//    private void registerFacebook(final View v, final ParseUser user){
//        user.setUsername(usernameText.getText().toString());
//        user.setPassword(passwordText.getText().toString());
//        user.setEmail(emailText.getText().toString());
//        mErrorField.setText("");
//
//        user.signUpInBackground(new SignUpCallback() {
//            @Override
//            public void done(ParseException e) {
//                if (e == null) {
//                    showHome(v);
//                } else {
//                    Toast.makeText(getApplicationContext(), "Sign Up Error.", Toast.LENGTH_LONG).show();
//                    // Sign up didn't succeed. Look at the ParseException
//                    // to figure out what went wrong
//                    switch (e.getCode()) {
//                        case ParseException.USERNAME_TAKEN:
//                            mErrorField.setText("Sorry, this username has already been taken.");
//                            break;
//                        case ParseException.USERNAME_MISSING:
//                            mErrorField.setText("Sorry, you must supply a username to register.");
//                            break;
//                        case ParseException.PASSWORD_MISSING:
//                            mErrorField.setText("Sorry, you must supply a password to register.");
//                            break;
//                        default:
//                            mErrorField.setText(e.getLocalizedMessage());
//                    }
//                    v.setEnabled(true);
//                }
//            }
//        });
//    }
//
//    public void linkFacebook(final View v, final ParseUser user){
//        if (!ParseFacebookUtils.isLinked(user)) {
//            ParseFacebookUtils.linkWithReadPermissionsInBackground(user, this, permissions, new SaveCallback() {
//                @Override
//                public void done(ParseException ex) {
//                    if (ParseFacebookUtils.isLinked(user)) {
//                        Log.d("MyApp", "Woohoo, user logged in with Facebook!");
//                    }
//                }
//            });
//        }
//    }

    private boolean checkFields(){
        if(nameText.getText().length() == 0){
            mErrorField.setText("Please enter a name.");
            return true;
        }
        if(usernameText.getText().length() == 0) {
            mErrorField.setText("Please enter a username.");
            return true;
        }
        if(passwordText.getText().length() == 0){
            mErrorField.setText("Please enter a password.");
            return true;
        }
        else
            return false;
    }



}
