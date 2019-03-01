package com.sah.seewaves;

import android.location.Location;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sah.seewaves.models.News;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.Map;

public class NewsTrigger {
    private static final String TAG = "NewsTrigger";
    private DatabaseReference mListOfNewsReference;
    private ChildEventListener listOfNewsListener;
    private LinkedList<News> newsList = new LinkedList<>();
    private RecyclerView mRecyclerView = null;

    public NewsTrigger() {
        mListOfNewsReference = FirebaseDatabase.getInstance().getReference("news");
        listOfNewsListener = new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                Log.d(TAG, "Added News Test");
                News news = dataSnapshot.getValue(News.class);
                Log.d(TAG, "Added News: " + news.name + ", " + news.latitude + ", " + news.longitude + ", " + news.status);
                newsList.addFirst(news);
                if (mRecyclerView != null) {
                    // Notify the adapter, that the data has changed.
                    mRecyclerView.getAdapter().notifyItemInserted(0);
                    mRecyclerView.smoothScrollToPosition(0);
                }
            }
            @Override
            public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                News news = dataSnapshot.getValue(News.class);
                Log.d(TAG, "Changed News: " + news.name + ", " + news.latitude + ", " + news.longitude + ", " + news.status);
            }
            @Override
            public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {}
            @Override
            public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {}
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {}
        };
        mListOfNewsReference.addChildEventListener(listOfNewsListener);
    }

    public void setRecyclerView(RecyclerView mRecyclerView) {
        // Create recycler view.
        this.mRecyclerView = mRecyclerView;
    }

    public LinkedList<News> getNewsList() {
        return newsList;
    }
}
