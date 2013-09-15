package com.example.shoppingapp;

/**This class is used for GoogleMap*/

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.Menu;
import android.support.v4.app.FragmentActivity;


public class MyMap extends FragmentActivity {
 private GoogleMap googleMap;

 @Override
 protected void onCreate(Bundle savedInstanceState) {
  super.onCreate(savedInstanceState);
  setContentView(R.layout.google_map);
  setUpMapIfNeeded();
  
 }

 private void setUpMapIfNeeded() {
   if (googleMap == null){
   googleMap=((SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map)).getMap();
   
   if(googleMap!=null){
    setUpMap();
   }
  }
 }

 private void setUpMap() {
  googleMap.setMyLocationEnabled(true);
  
  LocationManager locationManager = (LocationManager)  getSystemService(LOCATION_SERVICE);
  Criteria criteria= new Criteria();
  String provider = locationManager.getBestProvider(criteria, true);
  Location myLocation = locationManager.getLastKnownLocation(provider);
  googleMap.setMapType(GoogleMap.MAP_TYPE_HYBRID);
  double latitude = myLocation.getLatitude();
  double longtitude = myLocation.getLongitude();
  LatLng latlng = new LatLng (latitude, longtitude);
  googleMap.moveCamera(CameraUpdateFactory.newLatLng(latlng));
  googleMap.animateCamera(CameraUpdateFactory.zoomTo(20));
  googleMap.addMarker(new MarkerOptions().position(new LatLng(latitude, longtitude)).title("You are here!"));
 }

 @Override
 public boolean onCreateOptionsMenu(Menu menu) {
    getMenuInflater().inflate(R.menu.main, menu);
  return true;
 }

}