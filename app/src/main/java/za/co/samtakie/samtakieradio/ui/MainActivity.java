package za.co.samtakie.samtakieradio.ui;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.ShareActionProvider;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import za.co.samtakie.samtakieradio.R;
import za.co.samtakie.samtakieradio.RadioConnection;
import za.co.samtakie.samtakieradio.SettingsActivity;
import za.co.samtakie.samtakieradio.data.Contract;
import za.co.samtakie.samtakieradio.data.RadioAdapter;
import za.co.samtakie.samtakieradio.sync.RadioSyncUtils;

public class MainActivity extends AppCompatActivity implements MainFragment.RadioAdapterOnClickHandler {

    private RecyclerView mRecyclerView; // declare a variable of Object RecyclerView

    private RadioAdapter mAdapter; // declare a variable of Object RadioAdapter

    /*Initialize a constant of Type int with a value 10, this will be used to query the Online radio
    table*/
    private static final int ID_RADIO_LOADER = 10;

    /*Initialize a constant of Type int with a value 20, this will be used to query the Online Favorite
    table*/
    private static final int ID_RADIO_FAV_LOADER = 20;

    private ShareActionProvider mShareActionProvider;


    /* Initialize a constant of Type String to hold the Activity class name */
    private static final String TAG = MainActivity.class.getSimpleName();

    // initialize variable for wifi and mobile connection
    private boolean isConnectedWifi;
    private boolean isConnectedMobile;

    /* Static variable linked to the column index for passing into the Cursor to get the correct
    *  column data  0 represent the first column and n represent the last column */
    public static final int INDEX_COLUMN_ONLINE_RADIO_ID = 0;
    public static final int INDEX_COLUMN_ONLINE_RADIO_NAME = 1;
    public static final int INDEX_COLUMN_ONLINE_RADIO_LINK = 2;
    public static final int INDEX_COLUMN_ONLINE_RADIO_IMAGE = 3;

    /* The columns of data that we are interested in displaying within our MainActivity's list of
    * Radio data */
    public static final String[] MAIN_RADIO_PROJECTION = {
            Contract.RadioEntry._ID,
            Contract.RadioEntry.COLUMN_ONLINE_RADIO_NAME,
            Contract.RadioEntry.COLUMN_ONLINE_RADIO_LINK,
            Contract.RadioEntry.COLUMN_ONLINE_RADIO_IMAGE,
            Contract.RadioEntry.COLUMN_ONLINE_RADIO_ID
    };

    private static final String TAG_MAIN_FRAGMENT = "mainFragment";

    private boolean tabletSize;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        MainFragment mainFragment = new MainFragment();

        tabletSize = getResources().getBoolean(R.bool.screen_large);

        if(tabletSize){
            Log.d(TAG, "Activity has been loaded in a Tablet");
            if(savedInstanceState == null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.fragment_main_master, mainFragment, TAG_MAIN_FRAGMENT)
                        .commit();
            } else {
                mainFragment = (MainFragment) getSupportFragmentManager().findFragmentByTag(TAG_MAIN_FRAGMENT);

            }
        } else {
            if(savedInstanceState == null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.fragment_main, mainFragment, TAG_MAIN_FRAGMENT)
                        .commit();
            } else {
                mainFragment = (MainFragment) getSupportFragmentManager().findFragmentByTag(TAG_MAIN_FRAGMENT);

            }
        }



// Run this once if the app is being run for the first time.
        // This will add the data into the database before a Loader call is being made
        RadioSyncUtils.initialize(this, Contract.RadioEntry.CONTENT_URI_ONLINE_RADIO);



        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //prefs.registerOnSharedPreferenceChangeListener(this);*/



        Log.d("Main", "oncreate is being called");



        isConnectedWifi = RadioConnection.haveNetworkConnectionWifi(this);
        isConnectedMobile = RadioConnection.haveNetworkConnectionMobile(this);
        boolean mobileData = prefs.getBoolean("example_switch", false);
        //prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Log.d("Switch", ""+prefs.getBoolean("example_switch", false));
        Log.d("View", ""+prefs.getString("example_list", "0"));
        if(mobileData) {
            if (RadioConnection.haveNetworkConnectionMobile(this)) {
                Toast.makeText(this, "You are connected via Mobile", Toast.LENGTH_LONG).show();
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(this);
                }
                builder.setTitle("Mobile Data Activated")
                        .setMessage("Are you sure you want activate it?")
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent settingsIntent = new Intent(MainActivity.this, SettingsActivity.class);
                                startActivity(settingsIntent);
                            }
                        })
                        .setNegativeButton(android.R.string.no, new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                MainActivity.this.finish();
                            }
                        })
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .show();
            }
        }

        if(RadioConnection.haveNetworkConnectionWifi(this)){
            Toast.makeText(this, "You are connected via Wifi", Toast.LENGTH_LONG).show();
        }

        /*// Initialize the mRecyclerView
        mRecyclerView = (RecyclerView)findViewById(R.id.recyclerView);

        // Set the layout Manager, use a GridLayoutManager to be able to add more than one column
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

        *//* Initialize the mAdapter by creating a new object of the RadioAdapter passing the following parameters
        Context(this), ClickHandler(this) *//*
        mAdapter = new RadioAdapter(this, this);

        // Bind the mRecyclerView with the mAdapter object
        mRecyclerView.setAdapter(mAdapter);

        // Initialize the toolbar
        Toolbar toolbar = findViewById(R.id.toolbar);

        //Set the toolbar to act as the ActionBar for this Activity window.
        setSupportActionBar(toolbar);*/

        // Run this once if the app is being run for the first time.
        // This will add the data into the database before a Loader call is being made
        //RadioSyncUtils.initialize(this, Contract.RadioEntry.CONTENT_URI_ONLINE_RADIO);

        /* Ensures a loader is initialized and active. If the loader doesn't already exist,
        one is created and (if the activity/fragment is currently started) starts the loader.
        Otherwise the last created loader is re-used.*/

        Toolbar toolbar = findViewById(R.id.toolbar);

        //Set the toolbar to act as the ActionBar for this Activity window.
        setSupportActionBar(toolbar);



    }



    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // inflate the menu layout
        getMenuInflater().inflate(R.menu.menu_main, menu);


        return true;
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // get the menu id
        int id = item.getItemId();

        // check which menu item has been clicked and perform the switch case action
        switch (id){
            case R.id.action_settings:
                displayToast("List has been clicked!");
                // Start the settings activity when this setting menu item has been clicked
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;

            case R.id.action_share:
                String message = "I'm listening to Samtakie Radio";
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, message);
                startActivity(share);
                return true;

                default:
                    // Do nothing for the time being

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This function wil show the Toast message on screen
     * @param message Hold the text of the message to be shown
     */
    public void displayToast(String message){
        //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        Log.d(TAG, message);
    }

    /*@NonNull
    @Override
    public Loader<Cursor> onCreateLoader(int id, @Nullable Bundle args) {

        switch (id){
            case ID_RADIO_LOADER:
                Uri radioQueryUri = Contract.RadioEntry.CONTENT_URI_ONLINE_RADIO;
                displayToast("ID Radio Loader request");
                String sortRadio = Contract.RadioEntry.COLUMN_ONLINE_RADIO_NAME + " ASC";

                return new CursorLoader(
                        this,
                        radioQueryUri,
                        MAIN_RADIO_PROJECTION,
                        null,
                        null,
                        sortRadio);

            case ID_RADIO_FAV_LOADER:
                Uri radioFavQueryUri = Contract.RadioEntry.CONTENT_URI_ONLINE_RADIO_FAV;
                displayToast("ID Radio Loader request for Fav list");
                String sortRadioFav = Contract.RadioEntry.COLUMN_ONLINE_RADIO_NAME + " ASC";

                return new CursorLoader(
                        this,
                        radioFavQueryUri,
                        MAIN_RADIO_PROJECTION,
                        null,
                        null,
                        sortRadioFav);

                default:
                    throw new RuntimeException("Loader Not Implemented: " + id);
        }
    }

    @Override
    public void onLoadFinished(@NonNull Loader<Cursor> loader, Cursor data) {

        // Todo: Add a loading indicator while the recycler view is loading the data
        // Todo: hide the Loading indicator after the data has been successfully loaded in the recycler view


        mAdapter.swapCursor(data);



        if(data.getCount() != 0){
            // Todo: use this if block to show the list if the data object is greater than 0
        }
    }

    @Override
    public void onLoaderReset(@NonNull Loader<Cursor> loader) {
        mAdapter.swapCursor(null);
    }*/

    public void startDetailActivity(View view,int radioID,  int contentPosition, String radioLink, String radioName, String radioImage) {



        if(tabletSize){
            Log.d(TAG, "Activity has been loaded in a Tablet");
            //if(savedInstanceState == null) {
                DetailFragment detailFragment = new DetailFragment();
            detailFragment.setRadioID(radioID);
            detailFragment.setRadioImage(radioImage);
            detailFragment.setRadioLink(radioLink);
            detailFragment.setRadioName(radioName);
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_detail_child, detailFragment)
                        .commit();

        } else {

            Intent intent = new Intent(this, DetailActivity.class);
            intent.putExtra("radio_name", radioName); // add the radio name in the intent
            intent.putExtra("radio_image", radioImage); // add the radio image in the intent
            intent.putExtra("radioID", radioID); // add the radio ID in the intent
            intent.setData(Uri.parse(radioLink)); // add the radio link in the intent
            startActivity(intent);
        }


    }

    @Override
    public void radioItemOnClickHandler(int radioID, View view, int adapterPosition, String radioLink, String radioName, String radioImage) {
        displayToast("Get the view ID clicked " + view.getId());
        displayToast("Radio ID is " + radioID);
        displayToast("The Radio Link is " + radioLink);
        displayToast("The Radio name is " + radioName);
        displayToast("The radioImage url is " + radioImage);
        //Todo: call the startDetailActivity with all the parameters for the MusicLibrary class.
        startDetailActivity(view, radioID, adapterPosition, radioLink, radioName, radioImage);
    }

    /*@Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        if(sharedPreferences.getString(s, "Default_List").equals("Default_List")){
            Log.d("PrefMain", "DEfault has been selected");
            getSupportLoaderManager().initLoader(ID_RADIO_LOADER, null, this);
        } else {
            Log.d("PrefMain", "Something is missing: " + s);
            getSupportLoaderManager().initLoader(ID_RADIO_FAV_LOADER, null, this);
        }
    }*/

    @Override
    protected void onStop() {
        super.onStop();
        /*SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        prefs.unregisterOnSharedPreferenceChangeListener(this);*/
        Log.d("Main", "onstop is being called");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);
        //prefs.unregisterOnSharedPreferenceChangeListener(this);
        Log.d("Main", "ondestroy is being called");
    }
}