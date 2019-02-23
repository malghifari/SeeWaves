package com.sah.seewaves.models;
import com.google.firebase.database.IgnoreExtraProperties;

@IgnoreExtraProperties
public class Place {
    public String location;
    public String status;

    public Place()  {

    }

    public Place(String location, String status)    {
        this.location = location;
        this.status = status;
    }
}