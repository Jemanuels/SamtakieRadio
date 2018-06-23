/*Copyright [2018] [Jurgen Emanuels]

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.*/
package za.co.samtakie.samtakieradio.ui;


import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Build;
import android.preference.PreferenceManager;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.messaging.FirebaseMessaging;

import za.co.samtakie.samtakieradio.R;
import za.co.samtakie.samtakieradio.UpdateOnlineRadio;
import za.co.samtakie.samtakieradio.utilities.RadioConnection;
import za.co.samtakie.samtakieradio.provider.Contract;
import za.co.samtakie.samtakieradio.sync.RadioSyncUtils;

@SuppressWarnings({"FieldCanBeLocal", "unused"})
public class MainActivity extends AppCompatActivity implements MainFragment.RadioAdapterOnClickHandler {

    /* Initialize a constant of Type String to hold the Activity class name */
    private static final String TAG = MainActivity.class.getSimpleName();

    // initialize variable for wifi and mobile connection
    private boolean isConnectedWifi;
    private boolean isConnectedMobile;
    // Check in place for showing the Alert dialog one in the app lifecycle
    private boolean showDialog = true;

    private Snackbar mySnackBar;
    private Toolbar toolbar;

    /* Static variable linked to the column index for passing into the Cursor to get the correct
     *  column data  0 represent the first column and n represent the last column */
    public static final int INDEX_COLUMN_ONLINE_RADIO_ID = 0;
    public static final int INDEX_COLUMN_ONLINE_RADIO_NAME = 1;
    public static final int INDEX_COLUMN_ONLINE_RADIO_LINK = 2;
    public static final int INDEX_COLUMN_ONLINE_RADIO_IMAGE = 3;

    /* Set the Fragment tag name to be use for restoring the main Fragment */
    private static final String TAG_MAIN_FRAGMENT = "mainFragment";

    /* set true if the device is a tablet size */
    private boolean tabletSize;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        /* Subscribe to the susanie topics, this will make sure that all message sent with this topic
         * will be received by any user using this app */
        FirebaseMessaging.getInstance().subscribeToTopic("susanie");

        mySnackBar = Snackbar.make(findViewById(R.id.myLayout), R.string.update_data, Snackbar.LENGTH_SHORT);

        /* Create a new object of MainFragment*/
        MainFragment mainFragment = new MainFragment();

        /* set tabletSize to true if the device is a tablet size.
         * The layout file in values-large or sw600dp will be used which is true */
        tabletSize = getResources().getBoolean(R.bool.screen_large);

        /* If the tabletSize is true and the savedInstance is equal to null, add the mainFragment object
         * to the fragmentManager and use layout fragment_main_master and commit
         * This means that Main and Detail will be loaded in one activity */
        if (tabletSize) {
            if (savedInstanceState == null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .add(R.id.fragment_main_master, mainFragment, TAG_MAIN_FRAGMENT)
                        .commit();
            } else {
                /* else reload the mainFragment using the tag name */
                //noinspection UnusedAssignment
                mainFragment = (MainFragment) getSupportFragmentManager().findFragmentByTag(TAG_MAIN_FRAGMENT);
            }

        } else {
            /* Else if not a tablet size load fragment_main for phone devices, and if the savedInstanceState
             * is null add the mainFragment object to the fragmentManager and commit */
            if (savedInstanceState == null) {
                FragmentManager fragmentManager = getSupportFragmentManager();
                fragmentManager.beginTransaction()
                        .replace(R.id.fragment_main, mainFragment, TAG_MAIN_FRAGMENT)
                        .commit();
            } else {
                /* Else reload the mainFragment using the tag name*/
                //noinspection UnusedAssignment
                mainFragment = (MainFragment) getSupportFragmentManager().findFragmentByTag(TAG_MAIN_FRAGMENT);

            }
        }


        /* Run this once if the app is being run for the first time.
         * This will add the data into the database before a Loader call is being made. */
        RadioSyncUtils.initialize(this, Contract.RadioEntry.CONTENT_URI_ONLINE_RADIO);

        /* Gets a SharedPreferences instance that points to the default file that is used by
        the preference framework in the given context. Returns a SharedPreferences(prefs) instance that
        can be used to retrieve and listen to values of the preferences. */
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        // If connected via WiFi set the value of isConnectedWifi to true
        isConnectedWifi = RadioConnection.haveNetworkConnectionWifi(this);

        // If connected via Mobile Data set the value of isConnectedMobile to true;
        isConnectedMobile = RadioConnection.haveNetworkConnectionMobile(this);


        boolean mobileData = prefs.getBoolean("example_switch1", false);
        //prefs = PreferenceManager.getDefaultSharedPreferences(this);
        Log.d("Switch", "" + prefs.getBoolean("example_switch", false));
        Log.d("View", "" + prefs.getString("example_list", "0"));
        if (mobileData) {
            if (isConnectedMobile) {
                // Get the value of MobileData in the preference settings
                //Boolean mobileDataOn = prefs.getBoolean("example_switch1", false);
                String msgMobileDataOn = "Mobile Data is on for the App";

                //Toast.makeText(this, "You are connected via Mobile", Toast.LENGTH_LONG).show();
                AlertDialog.Builder builder;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
                } else {
                    builder = new AlertDialog.Builder(this);
                }
                if (showDialog) {
                    builder.setTitle(R.string.title_alert_mobile_data)
                            .setMessage(getString(R.string.text_message_alert_mobile_data) +
                                    " screen. " + msgMobileDataOn)
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
                            .setIcon(R.drawable.ic_sync)
                            .show();
                    showDialog = false;
                }
            }
        }

        // Show the user a Toast message if the user is connected via the WiFi
        if (isConnectedWifi) {
            Toast.makeText(this, R.string.text_wifi, Toast.LENGTH_LONG).show();
        }

        // Set the toolbar to be loaded.
        toolbar = findViewById(R.id.toolbar);
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
        switch (id) {
            case R.id.action_settings:

                // Start the settings activity when this setting menu item has been clicked
                Intent settingsIntent = new Intent(this, SettingsActivity.class);
                startActivity(settingsIntent);
                return true;

            case R.id.action_share:
                String message = getString(R.string.share_message);
                Intent share = new Intent(Intent.ACTION_SEND);
                share.setType("text/plain");
                share.putExtra(Intent.EXTRA_TEXT, message);
                startActivity(share);
                return true;

            case R.id.action_refresh:
                new UpdateOnlineRadio(this);
                return true;

            case R.id.action_news:
                Intent news = new Intent(this, News.class);
                startActivity(news);
                return true;

            default:
                // Do nothing for the time being

        }
        return super.onOptionsItemSelected(item);
    }

    /**
     * This function will show the Toast message on screen
     *
     * @param message Hold the text of the message to be shown
     */
    public void displayToast(String message) {
        //Todo: Add Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show(); to @displayToast method
        Log.d(TAG, message);
    }

    public void startDetailActivity(View view, int radioID, int contentPosition, String radioLink, String radioName, String radioImage) {
        if (tabletSize) {

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
        startDetailActivity(view, radioID, adapterPosition, radioLink, radioName, radioImage);
    }


    @Override
    protected void onPause() {
        super.onPause();
        if (!showDialog) {
            showDialog = true;
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        if (showDialog) {
            showDialog = false;
        }

    }

}