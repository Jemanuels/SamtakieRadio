/*
 * Copyright 2017 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package za.co.samtakie.samtakieradio.ui;

import android.content.res.Resources;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import za.co.samtakie.samtakieradio.IOnFocusListenable;
import za.co.samtakie.samtakieradio.R;
import za.co.samtakie.samtakieradio.client.MediaBrowserHelper;
import za.co.samtakie.samtakieradio.services.contentcatalogs.MusicLibrary;

public class DetailActivity extends AppCompatActivity {

    private ImageView mAlbumArt;
    private TextView mTitleTextView;
    private TextView mArtistTextView;
    private ImageView mMediaControlsImage;
    private MediaBrowserHelper mMediaBrowserHelper;
    private boolean mIsPlaying;

    private String radioLink;
    private String radioTitle;
    private int radioID;
    private String radioImage;

    private FloatingActionButton fab;
    private FloatingActionButton fabDel;

    DetailFragment detailFragment;

    MusicLibrary musicLibrary;

    private static final String TAG_MY_FRAGMENT = "detailFragment";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

         detailFragment = new DetailFragment();

        /* Get the Bundle from the Widget and Parent App */
        Bundle extras = getIntent().getExtras();


        if(savedInstanceState == null){

            // if the activity has been started set below variable by getting the data send via Intent
            radioTitle = getIntent().getStringExtra("radio_name");
            radioID = getIntent().getIntExtra("radioID", 0);
            radioLink = getIntent().getData().toString();
            radioImage = getIntent().getStringExtra("radio_image");

            detailFragment.setRadioName(radioTitle);
            detailFragment.setRadioID(radioID);
            detailFragment.setRadioLink(radioLink);
            detailFragment.setRadioImage(radioImage);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .add(R.id.fragment_details, detailFragment, TAG_MY_FRAGMENT)
                    .commit();

        } else {

            // if the activity has been paused and resume use the data that has been saved in the Bundle
            radioTitle = savedInstanceState.getString("radio_name");
            radioID = savedInstanceState.getInt("radioID", 0);
            radioLink = savedInstanceState.getString("radio_link");
            radioImage = savedInstanceState.getString("radio_image");

            detailFragment.setRadioName(radioTitle);
            detailFragment.setRadioID(radioID);
            detailFragment.setRadioLink(radioLink);
            detailFragment.setRadioImage(radioImage);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_details, detailFragment)
                    .commit();
        }

        // set the title of the activity linked to the selected online radio to be played
        String detailTitle = getString(R.string.title_activity_detail, radioTitle);
        setTitle(detailTitle);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {

        outState.putString("radio_link", radioLink);
        outState.putString("radio_name", radioTitle);
        outState.putInt("radioID", radioID);
        outState.putString("radio_image", radioImage);

        super.onSaveInstanceState(outState);

    }


    @Override
    public void onStart() {
        super.onStart();
        //mMediaBrowserHelper.onStart();


        Log.d("Detail", "onStart has been called");
    }

    @Override
    public void onStop() {
        super.onStop();
        /*musicLibrary = null;
        mSeekBarAudio.disconnectController();
        mMediaBrowserHelper.onStop();
        Log.d("Detail", "onStop has been called");*/
    }

    @Override
    protected void onPause() {
        super.onPause();
        /*mSeekBarAudio.disconnectController();
        mMediaBrowserHelper.onStop();*/
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (mIsPlaying) {
            //mMediaBrowserHelper.getTransportControls().stop();
        }
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch (item.getItemId()){
            case android.R.id.home:
                if (mIsPlaying) {
                    //mMediaBrowserHelper.getTransportControls().stop();
                }
                NavUtils.navigateUpFromSameTask(this);
                return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
        super.onWindowFocusChanged(hasFocus);

        // This will fixed the play and pause button when the Deatil Activity gained focus
        if( detailFragment instanceof IOnFocusListenable){
            ((IOnFocusListenable) detailFragment).onWindowFocusChanged(hasFocus);
        }
    }
}