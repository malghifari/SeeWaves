package com.sah.seewaves;

import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SafeLocation extends AppCompatActivity implements LocationListener, SensorEventListener {
    private static final String TAG = "SafeLocation";
    private float currentDegree = 0f;
    private float bearing;
    public static String Longi;
    public static String Lati;
    ImageView hand;
    TextView Latitude;
    TextView Longitude;
    LocationManager locationManager;
    Location targetLocation = new Location("");//provider name is unnecessary

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.safe_location);
        CheckPermission();
        targetLocation.setLatitude(-6.891172);//your coords of course
        targetLocation.setLongitude(107.609687);
    }

    public void openmaps(View view) {
        String loc = "Institut Teknologi Bandung";
        Uri addressUri = Uri.parse("geo:0,0?q=" + loc);
        Intent intent = new Intent(Intent.ACTION_VIEW, addressUri);

        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Log.d("ImplicitIntents", "Can't handle this intent!");
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        getLocation();
    }

    /* Remove the locationlistener updates when Activity is paused */
    @Override
    protected void onPause() {
        super.onPause();
        locationManager.removeUpdates(this);
    }

    public void getLocation() {
        try {
            locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            locationManager.requestLocationUpdates(LocationManager.NETWORK_PROVIDER, 5000, 5, this);
        } catch (SecurityException e) {
            e.printStackTrace();
        }
    }

    public void CheckPermission() {
        if (ContextCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(getApplicationContext(), android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, android.Manifest.permission.ACCESS_COARSE_LOCATION}, 101);
        }
    }

    @Override
    public void onLocationChanged(Location location) {
        Longitude = (TextView) findViewById(R.id.longitude);
        Latitude = (TextView) findViewById(R.id.latitude);
        hand = (ImageView) findViewById(R.id.hand);

        Longi = String.valueOf(location.getLongitude());
        Lati = String.valueOf(location.getLatitude());

        Longitude.setText("Longitude: " + Longi);
        Latitude.setText("Latitude:" + Lati);

        bearing = location.bearingTo(targetLocation);
        float degree = 0;
        degree = (bearing - degree) * -1;
        degree = normalizeDegree(degree);
        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        // how long the animation will take place
        ra.setDuration(1000);

        // set the animation after the end of the reservation status
        ra.setFillAfter(true);

        // Start the animation
        hand.startAnimation(ra);
        currentDegree = -degree;
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {
        Toast.makeText(this, "Enabled new provider!" + provider,
                Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onProviderDisabled(String provider) {
        Toast.makeText(SafeLocation.this, "Please Enable GPS and Internet", Toast.LENGTH_SHORT).show();
    }

    private float normalizeDegree(float value) {
        if (value >= 0.0f && value <= 180.0f) {
            return value;
        } else {
            return 180 + (180 + value);
        }
    }

    @Override
    public void onSensorChanged(SensorEvent event) {

        float degree = Math.round(event.values[0]);
        Log.d(TAG, "will set rotation " + degree +" degree");
        degree = (bearing - degree) * -1;
        degree = normalizeDegree(degree);
        RotateAnimation ra = new RotateAnimation(
                currentDegree,
                -degree,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF,
                0.5f);

        // how long the animation will take place
        ra.setDuration(500);
        ra.setRepeatCount(0);
        // set the animation after the end of the reservation status
        ra.setFillAfter(true);

        // Start the animation
        hand.startAnimation(ra);
        currentDegree = -degree;
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy) {

    }
}
