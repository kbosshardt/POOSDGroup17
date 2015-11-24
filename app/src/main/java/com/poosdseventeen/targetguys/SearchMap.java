package com.poosdseventeen.targetguys;

import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
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
}
