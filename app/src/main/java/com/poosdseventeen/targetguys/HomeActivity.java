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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

public class HomeActivity extends AppCompatActivity {

    ArrayList allUsers =  new ArrayList<String>();

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

        populateUsers();
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
        //Putting this here for now since not sure where else to put it
        stopService(new Intent(this, ChatActivity.class));

        Intent intent = new Intent(HomeActivity.this, MainActivity.class);
        startActivity(intent);

        finish();
    }


    public void searchMap(View v){
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

    public void viewChat(View v){
        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);

        PendingIntent pendingIntent =
                TaskStackBuilder.create(this)
                            .addNextIntentWithParentStack(intent).getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentIntent(pendingIntent);

        startActivity(intent);
    }

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

    public void populateUsers(){


        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {

                ParseUser user;
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        user = objects.get(i);
                        allUsers.add(user.getString("name"));
                    }

                    setUpAdapter();

                } else {
                    // Something went wrong.
                }
            }
        });
    }

    private void setUpAdapter(){

        ListView usersList =(ListView)findViewById(R.id.listView);
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, allUsers);
        // Set The Adapter
        usersList.setAdapter(arrayAdapter);

        // register onClickListener to handle click events on each item
        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // argument position gives the index of item which is clicked
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {

                String selectedUser = allUsers.get(position).toString();
                goToProfileActivity(selectedUser);
            }
        });
    }

    private void goToProfileActivity(String selectedUser){
        Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
        intent.putExtra("selectedUser", selectedUser);

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

    public void viewMatches(View v){
        Intent intent = new Intent(getApplicationContext(), ListMatchActivity.class);

        PendingIntent pendingIntent =
                TaskStackBuilder.create(this)
                        .addNextIntentWithParentStack(intent).getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
        NotificationCompat.Builder builder = new NotificationCompat.Builder(this);
        builder.setContentIntent(pendingIntent);

        startActivity(intent);
    }

}
