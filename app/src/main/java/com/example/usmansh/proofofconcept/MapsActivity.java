package com.example.usmansh.proofofconcept;

import android.icu.text.DecimalFormat;
import android.location.Location;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

      Toast.makeText(this, "lat "+getIntent().getStringExtra("lat"), Toast.LENGTH_SHORT).show();
        Toast.makeText(this, "lang " +getIntent().getStringExtra("lang"), Toast.LENGTH_SHORT).show();

    //loadLocationForThisUser();



    }

    private void loadLocationForThisUser() {


        //double mylat  = Double.parseDouble(getIntent().getStringExtra("myLat"));
        //double mylang = Double.parseDouble(getIntent().getStringExtra("myLang"));

        String frndLat  =  getIntent().getStringExtra("lat");
        String frndLan  =  getIntent().getStringExtra("lang");

        if(!frndLat.equals(" //") || !frndLan.equals(" //")) {

            double frndlat = Double.parseDouble(getIntent().getStringExtra("lat"));
            double frndlang = Double.parseDouble(getIntent().getStringExtra("lang"));


            //Add marker for friend location
            LatLng friendLocation = new LatLng(frndlat, frndlang);


            //Create Location from user coordinates
        /*Location currentUser = new Location("");
        currentUser.setLatitude(mylat);
        currentUser.setLongitude(mylang);
*/
            //Create Location from Friends coordinates
            Location friend = new Location("");
            friend.setLatitude(frndlat);
            friend.setLongitude(frndlang);

            //Create Function which will calculate the distance between friend and user
            //distance(currentUser,friend);


            //Clear All  old Marker
            mMap.clear();

            //Add friend marker on map
            mMap.addMarker(new MarkerOptions()
                    .position(friendLocation)
                    .title(getIntent().getStringExtra("ProName"))
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE)));

            mMap.animateCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(frndlat, frndlang), 16.0f));


            //Create Marker for Current User
        /*LatLng current = new LatLng(mylat,mylang);
        mMap.addMarker(new MarkerOptions().position(current).title("My Location"));*/

        }
        else
        {
            Toast.makeText(this, "Location is Empty..!", Toast.LENGTH_SHORT).show();
        }
    }




    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        loadLocationForThisUser();
    }
}
