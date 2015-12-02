package com.poosdseventeen.targetguys;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseRelation;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by Colin on 11/13/2015.
 */



public class SearchMap extends AppCompatActivity {
    static final LatLng testPoint = new LatLng(21, 57);
    private GoogleMap googleMap;

    ArrayList showUsers = new ArrayList<String>();

    LatLng yourLocation;
    double searchDistance;
    ParseUser currentUser = ParseUser.getCurrentUser();
    ParseGeoPoint parseLocation;
    ListView usersList;

    private EditText distanceEditText;
    Circle circle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_search);

        usersList = (ListView) findViewById(R.id.users_listview);


        try {
            if (googleMap == null) {
                googleMap = ((MapFragment) getFragmentManager().
                        findFragmentById(R.id.map)).getMap();
            }
            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            // Marker TP = googleMap.addMarker(new MarkerOptions().
            //        position(testPoint).title("testPoint"));
        } catch (Exception e) {
            e.printStackTrace();
        }

//        Intent intent = new Intent(this, CategoryActivity.class);
//        Bundle extras = getIntent().getExtras();
//        String distanceString = extras.getString("distance");



        setUpMap();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_profile, menu);
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

    private void setUpMap() {

        // Enable MyLocation Layer of Google Map
        googleMap.setMyLocationEnabled(true);

        yourLocation = getLocation();
        //get Your Current Location

        currentUser = ParseUser.getCurrentUser();


        if (currentUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        /////USE THE PREVIOUSLY STORED LOCATION FROM THE DATABASE, IF ONE EXISTS
        if (yourLocation == null) {
            ParseGeoPoint point = currentUser.getParseGeoPoint("location");
            if (point == null) {
                AlertDialog.Builder dlgAlert = new AlertDialog.Builder(this);

                dlgAlert.setMessage("No location can be found, please open up Google Maps to get your position then try again.");
                dlgAlert.setTitle("Error Message...");
                dlgAlert.setPositiveButton("OK", null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();

                finish();
            }
            yourLocation = new LatLng(point.getLatitude(), point.getLongitude());

        }

        //create a point on the map at your location
        Marker TP = googleMap.addMarker(new MarkerOptions().
                position(yourLocation).title(currentUser.getString("name")));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(yourLocation, 10);
        googleMap.animateCamera(cameraUpdate);


        parseLocation = new ParseGeoPoint(yourLocation.latitude, yourLocation.longitude);
        currentUser.put("location", parseLocation);

    }

    public LatLng getLocation() {
        // Get the location manager
        LocationManager locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        Criteria criteria = new Criteria();
        String bestProvider = locationManager.getBestProvider(criteria, false);
        Location location = locationManager.getLastKnownLocation(bestProvider);
        Double lat, lon;
        try {
            lat = location.getLatitude();
            lon = location.getLongitude();
            return new LatLng(lat, lon);
        } catch (NullPointerException e) {
            e.printStackTrace();
            return null;
        }
    }

    private void setUpUsersList() {
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, showUsers);
        // Set The Adapter
        usersList.setAdapter(arrayAdapter);

        // register onClickListener to handle click events on each item
        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // argument position gives the index of item which is clicked
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {

                final String selectedUser = showUsers.get(position).toString();
                final String fromUser = currentUser.getUsername();

                AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(SearchMap.this);
                alertDialogBuilder.setMessage("Choose an option");

                // get an existing photo form the gallery
                alertDialogBuilder.setPositiveButton("View Profile", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface arg0, int arg1) {
                        Intent intent = new Intent(getApplicationContext(), ProfileActivity.class);
                        intent.putExtra("selectedUser", selectedUser);

                        PendingIntent pendingIntent =
                                TaskStackBuilder.create(SearchMap.this)
                                        .addNextIntentWithParentStack(intent).getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(SearchMap.this);
                        builder.setContentIntent(pendingIntent);

                        startActivity(intent);
                    }
                });

                // get new photo from camera
                alertDialogBuilder.setNegativeButton("Send Message", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                        Intent intent = new Intent(getApplicationContext(), ChatActivity.class);
                        intent.putExtra("selectedUser", selectedUser);
                        intent.putExtra("fromUser", fromUser);
                        //maybe put extra here for username and profile pic
                        PendingIntent pendingIntent =
                                TaskStackBuilder.create(SearchMap.this)
                                        .addNextIntentWithParentStack(intent).getPendingIntent(0, PendingIntent.FLAG_UPDATE_CURRENT);
                        NotificationCompat.Builder builder = new NotificationCompat.Builder(SearchMap.this);
                        builder.setContentIntent(pendingIntent);

                        startActivity(intent);


                    }
                });
                AlertDialog alertDialog = alertDialogBuilder.create();
                alertDialog.show();

            }
        });
    }


    // query users from database based on current location and other users interests, location, and current activity
    private void findUsersWithinSearchDistance() {
        final ArrayList<String> currentUserInterests = new ArrayList<String>();


        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereExists("location");
        query.whereNotEqualTo("objectId", currentUser.getString("objectId"));
        query.whereWithinMiles("location", parseLocation, searchDistance);
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(final List<ParseUser> userList, com.parse.ParseException e) {
                if (e == null) {
                    //first set up interest lists
                    ParseRelation relation = currentUser.getRelation("interests");
                    ParseQuery interestQuery = relation.getQuery();
                    interestQuery.findInBackground(new FindCallback<ParseObject>() {
                        public void done(List<ParseObject> interestsList, ParseException e) {
                            if (e == null) {
                                for (int i = 0; i < interestsList.size(); i++) {
                                    currentUserInterests.add(interestsList.get(i).getString("name"));
                                    Log.d("OTHER USERES", currentUserInterests.get(i));
                                }

                            } else {
                                Log.d("interests", "Error: " + e.getMessage());
                            }
                        }
                    });

                    for (int i = 0; i < userList.size(); i++) {
                        final int k = i;
                        final ArrayList<String> otherUsers = new ArrayList<String>();
                        //first set up interest lists
                        ParseRelation otherUserRelation = userList.get(i).getRelation("interests");
                        ParseQuery otherUserInterestQuery = otherUserRelation.getQuery();
                        otherUserInterestQuery.findInBackground(new FindCallback<ParseObject>() {
                            public void done(List<ParseObject> interestsList, ParseException e) {
                                if (e == null) {

                                    for (int j = 0; j < interestsList.size(); j++) {
                                        otherUsers.add(interestsList.get(j).getString("name"));
                                    }
                                    if (!Collections.disjoint(currentUserInterests, otherUsers) && userList.get(k).getString("username") != currentUser.getString("username")) {
                                        showUsers.add(userList.get(k).getString("username"));
                                        googleMap.addMarker(new MarkerOptions().position(new LatLng(userList.get(k).getParseGeoPoint("location").getLatitude(), userList.get(k).getParseGeoPoint("location").getLongitude())).title(userList.get(k).getString("name")));
                                    }

                                } else {
                                    Log.d("interests", "Error: " + e.getMessage());
                                }
                            }
                        });


                    }

                } else {
                    Toast.makeText(getApplicationContext(),
                            "Error loading user locations",
                            Toast.LENGTH_LONG).show();
                }
            }
        });
    }

    //when a user updates the distance this function is called which
    public void reloadMap(View v){

        showUsers.clear();
        googleMap.clear();
        Marker TP = googleMap.addMarker(new MarkerOptions().
                position(yourLocation).title(currentUser.getString("name")));

        distanceEditText = (EditText) findViewById(R.id.distanceEditText);
        String distanceString = distanceEditText.getText().toString();
        searchDistance = Double.parseDouble(distanceString);

        double circleRadius = searchDistance * 1609.34;

        circle = googleMap.addCircle(new CircleOptions()
                .center(yourLocation)
                .radius(circleRadius)
                .strokeColor(Color.RED));
        circle.setCenter(yourLocation);

        currentUser.put("searchDistance", searchDistance);
        currentUser.put("currentlyUsing", true);

        findUsersWithinSearchDistance();
        setUpUsersList();
    }
}