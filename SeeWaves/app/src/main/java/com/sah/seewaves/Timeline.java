package com.sah.seewaves;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ShareCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Calendar;
import java.util.Date;

public class Timeline extends AppCompatActivity {
    private LinearLayout parentLinearLayout;
    private TextView time;
    private TextView location;
    private String longitude = "107.609687";
    private String latitude = "-6.891172";
    String share;
    private Date currentTime = Calendar.getInstance().getTime();
    private DrawerLayout mDrawerLayout;
    private ActionBarDrawerToggle mToogle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.timeline_layout);
        parentLinearLayout = (LinearLayout) findViewById(R.id.parent_timeline_layout);
        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer);
        mToogle = new ActionBarDrawerToggle(this,mDrawerLayout, R.string.open, R.string.close);
        mDrawerLayout.addDrawerListener(mToogle);
        mToogle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        if (mToogle.onOptionsItemSelected(item)){
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    public void onAddField(View v) {
        LayoutInflater inflater = (LayoutInflater) getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View rowView = inflater.inflate(R.layout.field, null);

        time = (TextView) rowView.findViewById(R.id.time);
        time.setText("Time: " + String.valueOf(currentTime));

        location = (TextView) rowView.findViewById(R.id.location);
        location.setText("Longitude: " + longitude + ", Latitude: " + latitude);

        // Add the new row before the add field button.
        parentLinearLayout.addView(rowView, parentLinearLayout.getChildCount() - 1);
    }

    public void onDelete(View v) {
        parentLinearLayout.removeView((View) v.getParent());
    }

    public void share_news(View view) {
        share = "Telah terdeteksi potensi tsunami dengan detail sebagai berikut.\n" + "Longitude: " + longitude + ", Latitude: " + latitude + "\n" + "Time: " + String.valueOf(currentTime);
        String mimeType = "text/plain";

        ShareCompat.IntentBuilder
                .from(this)
                .setType(mimeType)
                .setChooserTitle("Share this news with: ")
                .setText(share)
                .startChooser();
    }

    public void open_location(View view) {
        Uri addressUri = Uri.parse("geo:" + latitude + "," + longitude);
        Intent intent = new Intent(Intent.ACTION_VIEW, addressUri);
        if (intent.resolveActivity(getPackageManager()) != null) {
            startActivity(intent);
        } else {
            Log.d("ImplicitIntents", "Can't handle this intent!");
        }
    }
}
