package com.poosdseventeen.targetguys;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.parse.ParseUser;

public class HomeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);


        // Check if user is logged in
        ParseUser currentUser = ParseUser.getCurrentUser();
        if(currentUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_home, menu);
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


    public void logout(final View v){
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.logOut();

        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    public void viewInterests(View v){
        Intent intent = new Intent(HomeActivity.this, CategoryActivity.class);
        startActivity(intent);
        finish();
    }

    public void viewProfile(View v){
        Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
        startActivity(intent);
        finish();
    }
}
