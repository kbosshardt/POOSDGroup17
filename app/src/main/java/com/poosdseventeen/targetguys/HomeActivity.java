package com.poosdseventeen.targetguys;

import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import com.parse.ParseUser;

import java.util.ArrayList;


/**
 * Created by Kourtney Bosshardt on 10/23/2015.
 */

public class HomeActivity extends AppCompatActivity {

    ArrayList allUsers =  new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        Intent intent = new Intent(this, LoginActivity.class);
        // Check if user is logged in
        ParseUser currentUser = ParseUser.getCurrentUser();
        if(currentUser == null) {
            startActivity(intent);
            finish();
        }

        currentUser.put("currentlyUsing", false);
        //populateUsers();
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



    // log user out of the app by using parse method and going back to the main screen
    public void logout(final View v){
        ParseUser currentUser = ParseUser.getCurrentUser();
        currentUser.logOut();
        //Putting this here for now since not sure where else to put it
        stopService(new Intent(this, ChatActivity.class));

        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(intent);

        finish();
    }


    // go to the view interests screen
    public void viewInterests(View v){
        Intent intent = new Intent(HomeActivity.this, CategoryActivity.class);
        // Use TaskStackBuilder to build the back stack and get the PendingIntent
        PendingIntent pendingIntent =
                TaskStackBuilder.create(this)
                        // add all of DetailsActivity's parents to the stack,
                        // followed by DetailsActivity itself
                        .addNextIntentWithParentStack(intent).getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentIntent(pendingIntent);


        startActivity(intent);
    }


    // go to the view profule screen
    public void viewProfile(View v){
        Intent intent = new Intent(HomeActivity.this, UserDetailsActivity.class);

        // Use TaskStackBuilder to build the back stack and get the PendingIntent
        PendingIntent pendingIntent =
                TaskStackBuilder.create(this)
                        // add all of DetailsActivity's parents to the stack,
                        // followed by DetailsActivity itself
                        .addNextIntentWithParentStack(intent).getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentIntent(pendingIntent);


        startActivity(intent);

    }

    // go to the view messages screen
    public void viewMessages(View v){
        Intent intent = new Intent(HomeActivity.this, ViewMessages.class);

        // Use TaskStackBuilder to build the back stack and get the PendingIntent
        PendingIntent pendingIntent =
                TaskStackBuilder.create(this)
                        // add all of DetailsActivity's parents to the stack,
                        // followed by DetailsActivity itself
                        .addNextIntentWithParentStack(intent).getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentIntent(pendingIntent);


        startActivity(intent);
    }

    // go to the search map screen
    public void goToMap(View v){

        Intent intent = new Intent(HomeActivity.this, SearchMap.class);

        // Use TaskStackBuilder to build the back stack and get the PendingIntent
        PendingIntent pendingIntent =
                TaskStackBuilder.create(this)
                        // add all of DetailsActivity's parents to the stack,
                        // followed by DetailsActivity itself
                        .addNextIntentWithParentStack(intent).getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentIntent(pendingIntent);


        startActivity(intent);

    }


}
