package com.sah.seewaves;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sah.seewaves.models.Place;

import java.util.HashMap;
import java.util.Map;

public class TsunamiTrigger {
    private static final String TAG = "TsunamiTrigger";
    private DatabaseReference mListOfPlaceReference;
    private ChildEventListener listOfPlaceListener;
    private ValueEventListener placeListener;
    private DatabaseReference mPlaceReference;
    private Place myPlace;

    public TsunamiTrigger(final Place myPlace) {
        mListOfPlaceReference = FirebaseDatabase.getInstance().getReference("places");
        listOfPlaceListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Place place = dataSnapshot.getValue(Place.class);
                Log.d(TAG, "Added Place: " + place.name + ", " + place.latitude + ", " + place.longitude + ", " + place.status);
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Place place = dataSnapshot.getValue(Place.class);
                Log.d(TAG, "Changed Place: " + place.name + ", " + place.latitude + ", " + place.longitude + ", " + place.status);
                if (place.status.equals("danger")) {
                    Location myLocation = new Location("My Location");
                    myLocation.setLatitude(myPlace.latitude);
                    myLocation.setLongitude(myPlace.longitude);
                    Location location = new Location("Potential Tsunami Location");
                    location.setLatitude(place.latitude);
                    location.setLongitude(place.longitude);
                    float distance = myLocation.distanceTo(location);
                    if (distance > 10000) {
                        Log.d(TAG, "Distance: " + distance);
                    }
                }
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        };
        mListOfPlaceReference.addChildEventListener(listOfPlaceListener);

        this.myPlace = myPlace;
    }

    public void writePlaceWithUpdateChildren(String name, Double latitude, Double longitude, String status) {
        Place place = new Place(name,latitude,longitude,status);
        Map<String, Object> placeValues = place.toMap();
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("/" + place.name, placeValues);
        mListOfPlaceReference.updateChildren(childUpdates);
    }
}
