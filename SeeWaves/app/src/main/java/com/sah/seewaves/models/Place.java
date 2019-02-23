package com.sah.seewaves.models;
import android.location.Location;

import com.google.firebase.database.IgnoreExtraProperties;

import java.util.HashMap;
import java.util.Map;

@IgnoreExtraProperties
public class Place {
    public Location location;
    public String status;
    public Double latitude;
    public Double longitude;
    public String name;

    public Place()  {

    }

    public Place(String name, Double latitude, Double longitude, String status)    {
        this.name = name;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
    }

    public Map<String, Object> toMap()  {
        HashMap<String, Object> result = new HashMap<>();
        result.put("name", name);
        result.put("latitude", latitude);
        result.put("longitude", longitude);
        result.put("status", status);
        return result;
    }
}