package com.poosdseventeen.targetguys;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
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
import com.parse.ParseGeoPoint;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.ParseException;
import java.util.List;

/**
 * Created by Colin on 11/13/2015.
 */



public class SearchMap extends AppCompatActivity {
    static final LatLng testPoint = new LatLng(21, 57);
    private GoogleMap googleMap;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.map_search);

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

        setUpMap();
    }

    private void setUpMap() {

        // Enable MyLocation Layer of Google Map
        googleMap.setMyLocationEnabled(true);

        LatLng yourLocation = getLocation();
        //get Your Current Location

        ParseUser currentUser = ParseUser.getCurrentUser();


        if(currentUser == null) {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
            finish();
        }

        if(yourLocation == null) {
            ParseGeoPoint point = currentUser.getParseGeoPoint("location");
            if(point == null){
                AlertDialog.Builder dlgAlert  = new AlertDialog.Builder(this);

                dlgAlert.setMessage("No location can be found, please open up Google Maps to get your position then try again.");
                dlgAlert.setTitle("Error Message...");
                dlgAlert.setPositiveButton("OK", null);
                dlgAlert.setCancelable(true);
                dlgAlert.create().show();

                finish();
            }
            yourLocation = new LatLng(point.getLatitude(), point.getLongitude());

            /////USE THE PREVIOUSLY STORED LOCATION FROM THE DATABASE, IF ONE EXISTS
        }

        Marker TP = googleMap.addMarker(new MarkerOptions().
                position(yourLocation).title(currentUser.getString("name")));
        CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(yourLocation, 10);
        googleMap.animateCamera(cameraUpdate);

        Circle circle = googleMap.addCircle(new CircleOptions()
                .center(yourLocation)
                .radius(16093)
                .strokeColor(Color.RED));
        circle.setCenter(yourLocation);


        currentUser.put("location", new ParseGeoPoint(yourLocation.latitude, yourLocation.longitude));


        ParseQuery<ParseUser> query = ParseUser.getQuery();
        query.whereExists("location");
        query.whereNotEqualTo("objectId", currentUser.getString("objectId"));
        query.findInBackground(new FindCallback<ParseUser>() {
            public void done(List<ParseUser> userList, com.parse.ParseException e) {
                if (e == null) {
                    for (int i = 0; i < userList.size(); i++) {
                        googleMap.addMarker(new MarkerOptions().position(new LatLng(userList.get(i).getParseGeoPoint("location").getLatitude(), userList.get(i).getParseGeoPoint("location").getLongitude())).title(userList.get(i).getString("name")));
                    }

                } else {
                    Toast.makeText(getApplicationContext(),
                            "Error loading user locations",
                            Toast.LENGTH_LONG).show();
                }
            }
        });


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
}
