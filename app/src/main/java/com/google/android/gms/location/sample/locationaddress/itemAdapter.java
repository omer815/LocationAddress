package com.google.android.gms.location.sample.locationaddress;

/**
 * Created by omer on 12/21/2017.
 */

import android.content.Context;
import android.database.Cursor;
import android.location.Location;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.sample.locationaddress.MainActivity;
import com.google.android.gms.location.sample.locationaddress.distance;

import com.google.android.gms.location.sample.locationaddress.data.AlarmClockDbConfiguration;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

import java.util.Locale;

public class itemAdapter extends RecyclerView.Adapter<itemAdapter.itemViewHolder>{
    // Holds on to the cursor to display the waitlist
    private Cursor mCursor;
    private Context mContext;
    protected Location mlocation;

    private FusedLocationProviderClient mFusedLocationClient;

    /**
     * Constructor using the context and the db cursor
     * @param context the calling context/activity
     * @param cursor the db cursor with waitlist data to display
     */
    public itemAdapter(Context context, Cursor cursor) {
        this.mContext = context;
        this.mCursor = cursor;
    }

    @SuppressWarnings("MissingPermission")


    @Override
    public itemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Get the RecyclerView item layout
        LayoutInflater inflater = LayoutInflater.from(mContext);
        View view = inflater.inflate(R.layout.alarm_item, parent, false);
        return new itemViewHolder(view);
    }

    @Override
    public void onBindViewHolder(itemViewHolder holder, int position) {
        // Move the mCursor to the position of the item to be displayed
        if (!mCursor.moveToPosition(position))
            return; // bail if returned null

        // Update the view holder with the information needed to display
        Float range = mCursor.getFloat(mCursor.getColumnIndex(AlarmClockDbConfiguration.alarmclockEntry.COLUMN_RANGE_SIZE));
        String adrress = mCursor.getString(mCursor.getColumnIndex(AlarmClockDbConfiguration.alarmclockEntry.COLUMN_LOCATION_ADDRESS));


        Location loc = mlocation;
        // Display the guest name
        holder.wonted_location.setText(adrress);
        // Display the party count
        holder.radius.setText(String.valueOf(range));
        if(loc != null)
        {
            boolean isinside = isInside(mlocation,loc,7.0);
            holder.is_inside.setText(String.valueOf(isinside));

        }
    }

    public boolean isInside(Location currct , Location fromDB , double radius)
    {
        distance dis = new distance();
        double klimater = 0;
        klimater =  dis.distanceInKmBetweenEarthCoordinates(currct.getLatitude(),currct.getLongitude(),fromDB.getLatitude(),fromDB.getLongitude());
        if(klimater > radius)
        {
            return false;
        }
        return true;
    }
    @Override
    public int getItemCount() {
        return mCursor.getCount();
    }

    /**
     * Swaps the Cursor currently held in the adapter with a new one
     * and triggers a UI refresh
     *
     * @param newCursor the new cursor that will replace the existing one
     */
    public void swapCursor(Cursor newCursor) {
        // Always close the previous mCursor first
        if (mCursor != null) mCursor.close();
        mCursor = newCursor;
        if (newCursor != null) {
            // Force the RecyclerView to refresh
            this.notifyDataSetChanged();
        }
    }

    /**
     * Inner class to hold the views needed to display a single item in the recycler-view
     */
    class itemViewHolder extends RecyclerView.ViewHolder {

        // Will display the guest name
        TextView wonted_location;
        // Will display the party size number
        TextView radius;

        TextView is_inside;

        /**
         * Constructor for our ViewHolder. Within this constructor, we get a reference to our
         * TextViews
         *
         * @param itemView The View that you inflated in
         */
        public itemViewHolder(View itemView) {
            super(itemView);
            wonted_location = (TextView) itemView.findViewById(R.id.wonted_location);
            radius = (TextView) itemView.findViewById(R.id.radius);
            is_inside = (TextView) itemView.findViewById(R.id.is_inside);
        }

    }

    public void setWeatherData(Location location) {
        mlocation = location;
        notifyDataSetChanged();
    }

}
