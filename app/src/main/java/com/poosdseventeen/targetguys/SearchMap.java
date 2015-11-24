package com.poosdseventeen.targetguys;

import android.app.AlertDialog;
import android.app.PendingIntent;
import android.app.TaskStackBuilder;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.NotificationCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Colin on 11/13/2015.
 */



public class SearchMap extends AppCompatActivity {
    static final LatLng testPoint = new LatLng(21, 57);
    private GoogleMap googleMap;


    ArrayList showUsers =  new ArrayList<String>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_search);

//        try {
//            if (googleMap == null) {
//                googleMap = ((MapFragment) getFragmentManager().
//                        findFragmentById(R.id.map)).getMap();
//            }
//            googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
//            // Marker TP = googleMap.addMarker(new MarkerOptions().
//            //        position(testPoint).title("testPoint"));
//        } catch (Exception e) {
//            e.printStackTrace();
//        }

        // Do a null check to confirm that we have not already instantiated the map.
        if (googleMap == null) {
            // Try to obtain the map from the SupportMapFragment.
//            googleMap = ((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
            googleMap = ((MapFragment) getFragmentManager().
                        findFragmentById(R.id.map)).getMap();
            //googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
            googleMap.setMyLocationEnabled(true);
            // Check if we were successful in obtaining the map.
            if (googleMap != null) {


                googleMap.setOnMyLocationChangeListener(new GoogleMap.OnMyLocationChangeListener() {

                    @Override
                    public void onMyLocationChange(Location arg0) {
                        // TODO Auto-generated method stub

                        googleMap.addMarker(new MarkerOptions().position(new LatLng(arg0.getLatitude(), arg0.getLongitude())).title("It's Me!"));
                    }
                });

            }
        }


        setUpUsersList();
    }

//    private void setUpMap() {
//
//        // Enable MyLocation Layer of Google Map
//        googleMap.setMyLocationEnabled(true);
//
//        LatLng yourLocation = getLocation();
//        //get Your Current Location
//
//        if(yourLocation != null) {
//            Marker TP = googleMap.addMarker(new MarkerOptions().
//                    position(yourLocation).title("Your location!"));
//        }
//        else{
//            /////USE THE PREVIOUSLY STORED LOCATION FROM THE DATABASE, IF ONE EXISTS
//        }
//
//        ///////////////////////////////////////////////////////////////////////////////////////////
//        //ADD CODE HERE TO INSERT yourLocation INTO THE DATABASE FOR THE CURRENT USER////////////
//        //////////////////////////////////////////////////////////////////////////////////////////
//
//        //////////////////////////////////////////////////////////////////////////////////////////
//        ///ADD CODE HERE TO ALSO DISPLAY OTHER USERS' LOCATION NEARBY////////////////////////////
//        //////////////////////////////////////////////////////////////////////////////////////////
//
//    }

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

    private void setUpUsersList(){

        //NEED TO CHANGE THIS QUERY!
        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereEqualTo("username", "user2");
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> objects, ParseException e) {

                ParseUser user;
                if (e == null) {
                    for (int i = 0; i < objects.size(); i++) {
                        user = objects.get(i);
                        showUsers.add(user.getString("name"));
                    }

                } else {
                    // Something went wrong.
                }
            }
        });

        ListView usersList =(ListView)findViewById(R.id.users_listview);
        ArrayAdapter<String> arrayAdapter =
                new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1, showUsers);
        // Set The Adapter
        usersList.setAdapter(arrayAdapter);

        // register onClickListener to handle click events on each item
        usersList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            // argument position gives the index of item which is clicked
            public void onItemClick(AdapterView<?> arg0, View v, int position, long arg3) {

                final String selectedUser = showUsers.get(position).toString();

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
                                        .addNextIntentWithParentStack(intent).getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
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

                        PendingIntent pendingIntent =
                                TaskStackBuilder.create(SearchMap.this)
                                        .addNextIntentWithParentStack(intent).getPendingIntent(0,PendingIntent.FLAG_UPDATE_CURRENT);
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
}
