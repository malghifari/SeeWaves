package com.sah.seewaves;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.sah.seewaves.models.News;

import java.util.LinkedList;

public class NewsListAdapter extends RecyclerView.Adapter<NewsListAdapter.NewsViewHolder> {

    private final LinkedList<News> mNewsList;
    private final LayoutInflater mInflater;
    private Context context;

    class NewsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public final TextView newsText;
        public final TextView locationDetail;
        public final TextView timestamp;
        public final Button openLocationButton;
        public final Button shareButton;
        public Double latitude = -6.893346;
        public Double longitude= 107.610039;
        private Context context;

        final NewsListAdapter mAdapter;

        /**
         * Creates a new custom view holder to hold the view to display in the RecyclerView.
         *
         * @param itemView The view in which to display the data.
         * @param adapter The adapter that manages the the data and views for the RecyclerView.
         */
        public NewsViewHolder(Context context, View itemView, NewsListAdapter adapter) {
            super(itemView);
            newsText = (TextView) itemView.findViewById(R.id.news_text);
            locationDetail = (TextView) itemView.findViewById(R.id.location_detail);
            timestamp = (TextView) itemView.findViewById(R.id.timestamp);
            openLocationButton = (Button) itemView.findViewById(R.id.open_location_button);
            shareButton = (Button) itemView.findViewById(R.id.share_button1);

            this.mAdapter = adapter;
            this.context = context;
            Log.d("maps open", latitude + " " + longitude);
            openLocationButton.setOnClickListener(this);
        }

        public void setLatitude(Double latitude) {
            this.latitude = latitude;
        }

        public void setLongitude(Double longitude) {
            this.longitude = longitude;
        }

        @Override
        public void onClick(View v) {
            // All we do here is prepend "Clicked! " to the text in the view, to verify that
            // the correct item was clicked. The underlying data does not change.
            if (v.getId() == R.id.open_location_button) {
                Uri addressUri = Uri.parse("geo:" + latitude + "," + longitude);
                Intent intent = new Intent(Intent.ACTION_VIEW, addressUri);
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                } else {
                    Log.d("ImplicitIntents", "Can't handle this intent!");
                }
            }
        }
    }

    public NewsListAdapter(Context context, LinkedList<News> mNewsList) {
        mInflater = LayoutInflater.from(context);
        this.mNewsList = mNewsList;
        this.context = context;
    }

    /**
     * Inflates an item view and returns a new view holder that contains it.
     * Called when the RecyclerView needs a new view holder to represent an item.
     *
     * @param parent The view group that holds the item views.
     * @param viewType Used to distinguish views, if more than one type of item view is used.
     * @return a view holder.
     */
    @Override
    public NewsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate an item view.
        View mItemView = mInflater.inflate(R.layout.feed_item, parent, false);
        return new NewsViewHolder(context, mItemView, this);
    }

    /**
     * Sets the contents of an item at a given position in the RecyclerView.
     * Called by RecyclerView to display the data at a specificed position.
     *
     * @param holder The view holder for that position in the RecyclerView.
     * @param position The position of the item in the RecycerView.
     */
    @Override
    public void onBindViewHolder(NewsViewHolder holder, int position) {
        // Retrieve the data for that position.
        News mCurrent = mNewsList.get(position);
        // Add the data to the view holder.
        Double latitude = mCurrent.latitude;
        Double longitude = mCurrent.longitude;
        String newsText = "Telah terdeteksi potensi tsunami di daerah sekitaran <b>" + mCurrent.name + "</b> dengan detail sebagai berikut.\n";
        String locationDetail = "Latitude: " + latitude + ", Longitude: " + longitude;
        String timestamp = "Waktu: " + mCurrent.date;
        holder.setLatitude(latitude);
        holder.setLongitude(longitude);
        holder.newsText.setText(Html.fromHtml(newsText));
        holder.locationDetail.setText(locationDetail);
        holder.timestamp.setText(timestamp);
        Log.d("maps open", latitude + " " + longitude);
    }

    /**
     * Returns the size of the container that holds the data.
     *
     * @return Size of the list of data.
     */
    @Override
    public int getItemCount() {
        return mNewsList.size();
    }
}


