package com.google.android.gms.location.sample.locationaddress;

import android.app.DialogFragment;
import android.app.PendingIntent;
import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.places.AutocompletePrediction;
import com.google.android.gms.location.places.GeoDataClient;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.PlaceBufferResponse;
import com.google.android.gms.location.places.Places;
import com.google.android.gms.location.places.ui.PlaceSelectionListener;
import com.google.android.gms.location.sample.locationaddress.alarmmanager.AlarmReceiver;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.RuntimeRemoteException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import android.graphics.Color;
import android.location.Location;
import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;
import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Geocoder;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.annotation.NonNull;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.sample.locationaddress.data.AlarmClockDbConnection;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.location.sample.locationaddress.data.*;
import com.google.android.gms.maps.GoogleMap.OnMyLocationButtonClickListener;
import com.google.android.gms.maps.GoogleMap.OnMyLocationClickListener;
import android.support.v4.content.ContextCompat;
import android.app.Activity;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;
import java.util.Calendar;
import java.util.Calendar;


public class AddActivity extends AppCompatActivity  implements
        OnMyLocationButtonClickListener,
        OnMyLocationClickListener,
        OnMapReadyCallback,
        PlaceSelectionListener,
        ActivityCompat.OnRequestPermissionsResultCallback{

    private PendingIntent pendingIntent;

    private TextView DAY1;
    private TextView DAY2;
    private TextView DAY3;
    private TextView DAY4;
    private TextView DAY5;
    private TextView DAY6;
    private TextView DAY7;


    private TextView hour1;
    private TextView minute1;

    private PlaceAutocompleteAdapter mAdapter;
    private FusedLocationProviderClient mFusedLocationClient;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;

    private boolean mPermissionDenied = false;

    private AddressResultReceiver2 mResultReceiver;
    public Place place = null;

    public Location mLastLocation;

    public Location getmLastLocation()
    {
        return mLastLocation;
    }

    private static final int REQUEST_PERMISSIONS_REQUEST_CODE = 34;

    private static final String ADDRESS_REQUESTED_KEY = "address-request-pending";
    private static final String LOCATION_ADDRESS_KEY = "location-address";

    private GoogleMap mMap;
    SupportMapFragment mapFragment;
    private SQLiteDatabase mDb;

    private boolean mAddressRequested;

    public String mAddressOutput;

    private AutoCompleteTextView mAutocompleteView;

    private TextView mPlaceDetailsText;

    private TextView mPlaceDetailsAttribution;

    private TextView place_details;
    Marker  mBrisbane = null;
    private SeekBar seekBarradius;
    private ToggleButton mToggleButton;
    private Button  piketime;

    protected GeoDataClient mGeoDataClient;

    private Button AddButton;
    private TextView radiussummary;
    private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
            new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));


    private void enableMyLocation() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {
            // Permission to access the location is missing.
            PermissionUtils.requestPermission(this, LOCATION_PERMISSION_REQUEST_CODE,
                    Manifest.permission.ACCESS_FINE_LOCATION, true);
        } else if (mMap != null) {
            // Access to the location has been granted to the app.
            mMap.setMyLocationEnabled(true);
        }
    }

    @Override
    public boolean onMyLocationButtonClick() {
        Toast.makeText(this, "MyLocation button clicked", Toast.LENGTH_SHORT).show();
        // Return false so that we don't consume the event and the default behavior still occurs
        // (the camera animates to the user's current position).
        return false;
    }

    @Override
    public void onMyLocationClick(@NonNull Location location) {
        Toast.makeText(this, "Current location:\n" + location, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        if (requestCode != LOCATION_PERMISSION_REQUEST_CODE) {
            return;
        }

        if (PermissionUtils.isPermissionGranted(permissions, grantResults,
                Manifest.permission.ACCESS_FINE_LOCATION)) {
            // Enable the my location layer if the permission has been granted.
            enableMyLocation();
        } else {
            // Display the missing permission error dialog when the fragments resume.
            mPermissionDenied = true;
        }
    }

    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        if (mPermissionDenied) {
            // Permission was not granted, display error dialog.
            showMissingPermissionError();
            mPermissionDenied = false;
        }
    }

    /**
     * Displays a dialog with error message explaining that the location permission is missing.
     */
    private void showMissingPermissionError() {
        PermissionUtils.PermissionDeniedDialog
                .newInstance(true).show(getSupportFragmentManager(), "dialog");
    }
    Circle mCircle = null;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add);

        Intent alarmIntent = new Intent(AddActivity.this, AlarmReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(AddActivity.this ,0 ,alarmIntent , 0);

        DAY1 = (TextView) findViewById(R.id.Day1);
        DAY2 = (TextView) findViewById(R.id.Day2);
        DAY3 = (TextView) findViewById(R.id.Day3);
        DAY4 = (TextView) findViewById(R.id.Day4);
        DAY5 = (TextView) findViewById(R.id.Day5);
        DAY6 = (TextView) findViewById(R.id.Day6);
         DAY7 = (TextView) findViewById(R.id.Day7);


        hour1 = (TextView) findViewById(R.id.hour);
        minute1 = (TextView) findViewById(R.id.minute);

        AddButton = (Button) findViewById(R.id.action_ADD);

        AlarmClockDbConnection dbHelper = new AlarmClockDbConnection(this);

        mDb = dbHelper.getWritableDatabase();


        mResultReceiver = new AddressResultReceiver2(new Handler());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        // Construct a GeoDataClient for the Google Places API for Android.
        mGeoDataClient = Places.getGeoDataClient(this, null);

        // Retrieve the AutoCompleteTextView that will display Place suggestions.
        mAutocompleteView = (AutoCompleteTextView)
                findViewById(R.id.autocomplete_places);

        // Register a listener that receives callbacks when a suggestion has been selected
        mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);

        // Retrieve the TextViews that will display details and attributions of the selected place.
        mPlaceDetailsText = (TextView) findViewById(R.id.place_details);

        // Set up the adapter that will retrieve suggestions from the Places Geo Data Client.
        mAdapter = new PlaceAutocompleteAdapter(this, mGeoDataClient, BOUNDS_GREATER_SYDNEY, null);
        mAutocompleteView.setAdapter(mAdapter);

        place_details= (TextView) findViewById(R.id.place_details);
        mToggleButton = (ToggleButton) findViewById(R.id.toggleButton);

        mToggleButton.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    String text ="omer";
                    Toast.makeText(AddActivity.this, text, Toast.LENGTH_SHORT).show();
                    place_details.setText(getResources().getText(R.string.AllPlaces));
                    place_details.setTextColor(getResources().getColor(R.color.green));
                    mAutocompleteView.setText(getResources().getText(R.string.AllPlaces));
                    mAutocompleteView.setEnabled(false);
                } else {
                    mAutocompleteView.setEnabled(true);
                    mAutocompleteView.setText((""));
                    mAutocompleteView.setHint(getResources().getText(R.string.autocomplete_hint));
                    place_details.setTextColor(getResources().getColor(R.color.common_google_signin_btn_text_dark_focused));
                }
            }
        });

        seekBarradius =(SeekBar) findViewById(R.id.seekBarradius);
        radiussummary = (TextView) findViewById(R.id.radiussummary);

        radiussummary.setText("0 M");

        seekBarradius.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onProgressChanged(SeekBar seekBar, int progress,
                                          boolean fromUser) {
                radiussummary.setText(converntSeekbarToString(progress));
                mCircle.setRadius(seekBarradius.getProgress()*100);

             place.getLatLng();

                CameraPosition SYDNEY =
                        new CameraPosition.Builder().target(place.getLatLng())
                                .zoom(15.5f - progress / 8 )
                                .build();

               mMap.moveCamera(CameraUpdateFactory.newCameraPosition(SYDNEY));


            }
        });

        piketime = (Button) findViewById(R.id.picktime_button);

        piketime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getFragmentManager(),"TimePicker");

            }
        });



        TextView tv = (TextView) findViewById(R.id.time_choosen);
        //Set a message for user
        //Display the user changed time on TextView


        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        hour1.setText(String.valueOf(hour));

        minute1.setText(String.valueOf(minute));


        tv.setText( convertDate(hour)+":"+
                convertDate(minute) );


        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new TimePickerFragment();
                newFragment.show(getFragmentManager(),"TimePicker");
            }
        });

       TextView repet = (TextView)findViewById(R.id.repert_des);
        repet.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DialogFragment newFragment = new pickDayDialog();
                newFragment.show(getFragmentManager(),"dayFrafment");
            }
        });
    }

    public void start()
    {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int interval = 8000;

        manager.setInexactRepeating(AlarmManager.RTC_WAKEUP,
                                    System.currentTimeMillis(),
                                    interval,
                                    pendingIntent);

        Toast.makeText(this, "Alarm Set", Toast.LENGTH_SHORT).show();
    }

    public void cancel() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        manager.cancel(pendingIntent);
        Toast.makeText(this, "Alarm Canceled", Toast.LENGTH_SHORT).show();
    }

    public void startAt10() {
        AlarmManager manager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        int interval = 1000 * 60 * 20;

        /* Set the alarm to start at 10:30 AM */
        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, 10);
        calendar.set(Calendar.MINUTE, 30);

        /* Repeating on every 20 minutes interval */
        manager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(),
                1000 * 60 * 20, pendingIntent);
    }

    @Override
    public void onMapReady(GoogleMap map) {
        mMap = map;

        mMap.setOnMyLocationButtonClickListener(this);
        mMap.setOnMyLocationClickListener(this);
        enableMyLocation();

    }

    public String convertDate(int input) {
        if (input >= 10) {
            return String.valueOf(input);
        } else {
            return "0" + String.valueOf(input);
        }
    }
    public boolean onCreateOptionsMenu(Menu menu) {
        /* Use AppCompatActivity's method getMenuInflater to get a handle on the menu inflater */
        MenuInflater inflater = getMenuInflater();
        /* Use the inflater's inflate method to inflate our menu layout to this menu */
        inflater.inflate(R.menu.menu, menu);
        /* Return true so that the menu is displayed in the Toolbar */
        return true;
    }


    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.action_ADD:
                AddNewItem();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    protected long AddNewItem(){
        ContentValues cv = new ContentValues();
        cv.put(AlarmClockDbConfiguration.alarmclockEntry.COLUMN_RANGE_SIZE, seekBarradius.getProgress());
        cv.put(AlarmClockDbConfiguration.alarmclockEntry.COLUMN_DAY_1, Integer.valueOf(DAY1.getText().toString()));
        cv.put(AlarmClockDbConfiguration.alarmclockEntry.COLUMN_DAY_2, Integer.valueOf(DAY2.getText().toString()));
        cv.put(AlarmClockDbConfiguration.alarmclockEntry.COLUMN_DAY_3, Integer.valueOf(DAY3.getText().toString()));
        cv.put(AlarmClockDbConfiguration.alarmclockEntry.COLUMN_DAY_4, Integer.valueOf(DAY4.getText().toString()));
        cv.put(AlarmClockDbConfiguration.alarmclockEntry.COLUMN_DAY_5, Integer.valueOf(DAY5.getText().toString()));
        cv.put(AlarmClockDbConfiguration.alarmclockEntry.COLUMN_DAY_6, Integer.valueOf(DAY6.getText().toString()));
        cv.put(AlarmClockDbConfiguration.alarmclockEntry.COLUMN_DAY_7, Integer.valueOf(DAY7.getText().toString()));
        cv.put(AlarmClockDbConfiguration.alarmclockEntry.COLUMN_ALARMState, 1);
        cv.put(AlarmClockDbConfiguration.alarmclockEntry.COLUMN_LATITUDE, place.getLatLng().latitude);
        cv.put(AlarmClockDbConfiguration.alarmclockEntry.COLUMN_LONGITUTE, place.getLatLng().longitude);
        cv.put(AlarmClockDbConfiguration.alarmclockEntry.COLUMN_MINUTE, Integer.valueOf(hour1.getText().toString()));
        cv.put(AlarmClockDbConfiguration.alarmclockEntry.COLUMN_HOUR, Integer.valueOf(minute1.getText().toString()));
        cv.put(AlarmClockDbConfiguration.alarmclockEntry.COLUMN_LOCATION_ADDRESS, place.getAddress().toString());
        return mDb.insert(AlarmClockDbConfiguration.alarmclockEntry.TABLE_NAME, null, cv);

    }
    public String converntSeekbarToString(int progress)
    {

        Double progress1 = progress*1.0;
        String returnStetment = getResources().getString(R.string.err_seek_bar);
        if(progress < 10)
        {
            return (String.valueOf(progress*100)+"M");
        }
        else
        {
            if(progress % 10 == 0)
            {
                return (String.valueOf(progress/10)+"Km");
            }
            else{
                return (String.valueOf(progress1/10)+"Km");
            }
        }

    }
    /**
     * Listener that handles selections from suggestions from the AutoCompleteTextView that
     * displays Place suggestions.
     * Gets the place id of the selected item and issues a request to the Places Geo Data Client
     * to retrieve more details about the place.
     *
     * @see GeoDataClient#getPlaceById(String...)
     */
    private AdapterView.OnItemClickListener mAutocompleteClickListener
            = new AdapterView.OnItemClickListener() {
        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a AutocompletePrediction from which we
             read the place ID and title.
              */
            final AutocompletePrediction item = mAdapter.getItem(position);
            final String placeId = item.getPlaceId();
            final CharSequence primaryText = item.getPrimaryText(null);

            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);

            }


            /*
             Issue a request to the Places Geo Data Client to retrieve a Place object with
             additional details about the place.
              */
            Task<PlaceBufferResponse> placeResult = mGeoDataClient.getPlaceById(placeId);
            placeResult.addOnCompleteListener(mUpdatePlaceDetailsCallback);


//            Toast.makeText(getApplicationContext(), "Clicked: " + primaryText,
//                    Toast.LENGTH_SHORT).show();
        }
    };


    public void drop()
    {

    }
    /**
     * Callback for results from a Places Geo Data Client query that shows the first place result in
     * the details view on screen.
     */


    private OnCompleteListener<PlaceBufferResponse> mUpdatePlaceDetailsCallback
            = new OnCompleteListener<PlaceBufferResponse>() {
        @Override
        public void onComplete(Task<PlaceBufferResponse> task) {
            try {
                PlaceBufferResponse places = task.getResult();

                // Get the Place object from the buffer.
                place = places.get(0);

                // Format details of the place for display and show it in a TextView.
                mPlaceDetailsText.setText(place.getAddress());

                // Display the third party attributions if set.
                final CharSequence thirdPartyAttribution = places.getAttributions();

                  CameraPosition SYDNEY =
                        new CameraPosition.Builder().target(place.getLatLng())
                                .zoom(15.5f)
                                .build();
                if(mBrisbane != null)
                {
                    mBrisbane.remove();
                }
                  mBrisbane = mMap.addMarker(new MarkerOptions()
                        .position(place.getLatLng())
                        .title("Brisbane")
                        .snippet("Population: 2,074,200")
                        .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_AZURE)));

                    if(mCircle != null)
                    {
                        mCircle.remove();
                    }

                 int mFillColorArgb;
                mFillColorArgb = Color.HSVToColor(
                       70, new float[]{30, 70, 206});

                mCircle = mMap.addCircle(new CircleOptions()
                        .center(place.getLatLng())
                         .fillColor(mFillColorArgb)
                        .radius(seekBarradius.getProgress()*10));

                mMap.moveCamera(CameraUpdateFactory.newCameraPosition(SYDNEY));

            } catch (RuntimeRemoteException e) {
                // Request did not complete successfully
                return;
            }
        }
    };

    private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
                                              CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
        return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber,
                websiteUri));

    }



    private void updateValuesFromBundle(Bundle savedInstanceState) {
        if (savedInstanceState != null) {
            // Check savedInstanceState to see if the address was previously requested.
            if (savedInstanceState.keySet().contains(ADDRESS_REQUESTED_KEY)) {
                mAddressRequested = savedInstanceState.getBoolean(ADDRESS_REQUESTED_KEY);
            }
            // Check savedInstanceState to see if the location address string was previously found
            // and stored in the Bundle. If it was found, display the address string in the UI.
            if (savedInstanceState.keySet().contains(LOCATION_ADDRESS_KEY)) {
                mAddressOutput = savedInstanceState.getString(LOCATION_ADDRESS_KEY);
            }
        }
    }



    /**
     * Gets the address for the last known location.
     */
    @SuppressWarnings("MissingPermission")
    public void getAddress() {
        mFusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        if (location == null) {
                            return;
                        }

                        mLastLocation = location;
                        // Determine whether a Geocoder is available.
                        if (!Geocoder.isPresent()) {
                            showSnackbar(getString(R.string.no_geocoder_available));
                            return;
                        }

                        // If the user pressed the fetch address button before we had the location,
                        // this will be set to true indicating that we should kick off the intent
                        // service after fetching the location.
                        if (mAddressRequested) {

                        }
                    }
                })
                .addOnFailureListener(this, new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                    }
                });
    }

    /**
     * Updates the address in the UI.
     */

    /**
     * Shows a toast with the given text.
     */
    private void showToast(String text) {
        Toast.makeText(this, text, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState) {
        // Save whether the address has been requested.
        savedInstanceState.putBoolean(ADDRESS_REQUESTED_KEY, mAddressRequested);

        // Save the address string.
        savedInstanceState.putString(LOCATION_ADDRESS_KEY, mAddressOutput);
        super.onSaveInstanceState(savedInstanceState);
    }

    /**
     * Receiver for data sent from FetchAddressIntentService.
     */
    private class AddressResultReceiver2 extends ResultReceiver {
        AddressResultReceiver2(Handler handler) {
            super(handler);
        }

        /**
         *  Receives data sent from FetchAddressIntentService and updates the UI in MainActivity.
         */
        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {

            // Display the address string or an error message sent from the intent service.
            mAddressOutput = resultData.getString(Constants.RESULT_DATA_KEY);

            // Show a toast message if an address was found.
            if (resultCode == Constants.SUCCESS_RESULT) {
                showToast(getString(R.string.address_found));
            }

            // Reset. Enable the Fetch Address button and stop showing the progress bar.
            mAddressRequested = false;
        }
    }

    /**
     * Shows a {@link Snackbar} using {@code text}.
     *
     * @param text The Snackbar text.
     */
    private void showSnackbar(final String text) {
        View container = findViewById(android.R.id.content);
        if (container != null) {
            Snackbar.make(container, text, Snackbar.LENGTH_LONG).show();
        }
    }

    /**
     * Shows a {@link Snackbar}.
     *
     * @param mainTextStringId The id for the string resource for the Snackbar text.
     * @param actionStringId   The text of the action item.
     * @param listener         The listener associated with the Snackbar action.
     */
    private void showSnackbar(final int mainTextStringId, final int actionStringId,
                              View.OnClickListener listener) {
        Snackbar.make(findViewById(android.R.id.content),
                getString(mainTextStringId),
                Snackbar.LENGTH_INDEFINITE)
                .setAction(getString(actionStringId), listener).show();
    }

    /**
     * Return the current state of the permissions needed.
     */
    private boolean checkPermissions() {
        int permissionState = ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
        return permissionState == PackageManager.PERMISSION_GRANTED;
    }

    private void requestPermissions() {
        boolean shouldProvideRationale =
                ActivityCompat.shouldShowRequestPermissionRationale(this,
                        Manifest.permission.ACCESS_FINE_LOCATION);

        // Provide an additional rationale to the user. This would happen if the user denied the
        // request previously, but didn't check the "Don't ask again" checkbox.
        if (shouldProvideRationale) {

            showSnackbar(R.string.permission_rationale, android.R.string.ok,
                    new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            // Request permission
                            ActivityCompat.requestPermissions(AddActivity.this,
                                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                                    REQUEST_PERMISSIONS_REQUEST_CODE);
                        }
                    });

        } else {
            // Request permission. It's possible this can be auto answered if device policy
            // sets the permission in a given state or the user denied the permission
            // previously and checked "Never ask again".
            ActivityCompat.requestPermissions(AddActivity.this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    REQUEST_PERMISSIONS_REQUEST_CODE);
        }
    }


    @SuppressWarnings("MissingPermission")
    private void getLastLocation() {
        mFusedLocationClient.getLastLocation()
                .addOnCompleteListener(this, new OnCompleteListener<Location>() {
                    @Override
                    public void onComplete(@NonNull Task<Location> task) {
                        if (task.isSuccessful() && task.getResult() != null) {
                            mLastLocation = task.getResult();
                        }
                    }
                });
    }

    @Override
    public void onPlaceSelected(Place place) {
        Toast.makeText(this, String.valueOf(place.getLatLng()), Toast.LENGTH_SHORT).show();
    }
    @Override
    public void onError(Status status) {

        Toast.makeText(this, "Place selection failed: " + status.getStatusMessage(),
                Toast.LENGTH_SHORT).show();
    }


}
