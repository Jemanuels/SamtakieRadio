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

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.database.Cursor;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TabLayout;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;


import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.concurrent.TimeUnit;

import za.co.samtakie.samtakieradio.MediaSeekBar;
import za.co.samtakie.samtakieradio.R;
import za.co.samtakie.samtakieradio.client.MediaBrowserHelper;
import za.co.samtakie.samtakieradio.data.Contract;
import za.co.samtakie.samtakieradio.services.MusicPlayerService;
import za.co.samtakie.samtakieradio.services.contentcatalogs.MusicLibrary;

import static za.co.samtakie.samtakieradio.R.string.title_activity_detail;

public class DetailActivity extends AppCompatActivity {

    private ImageView mAlbumArt;
    private TextView mTitleTextView;
    private TextView mArtistTextView;
    private ImageView mMediaControlsImage;
    private MediaSeekBar mSeekBarAudio;
    private MediaBrowserHelper mMediaBrowserHelper;
    private boolean mIsPlaying;

    private String radioLink;
    private String radioTitle;
    private int radioID;
    private String radioImage;

    private FloatingActionButton fab;
    private FloatingActionButton fabDel;

    MusicLibrary musicLibrary;
    boolean firstime = true;
    /*Context context = this;
    SharedPreferences sharedPref = context.getSharedPreferences("Djoga500", Context.MODE_PRIVATE);
    SharedPreferences.Editor editor = sharedPref.edit();*/

    private static final String TAG_MY_FRAGMENT = "detailFragment";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        DetailFragment detailFragment = new DetailFragment();

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

            //detailFragment = (DetailFragment) getSupportFragmentManager().findFragmentByTag(TAG_MY_FRAGMENT);

            FragmentManager fragmentManager = getSupportFragmentManager();
            fragmentManager.beginTransaction()
                    .replace(R.id.fragment_details, detailFragment)
                    .commit();
        }

        // set the title of the activity linked to the selected online radio to be played
        Resources titleDetailRes = getResources();
        String detailTitle = getString(R.string.title_activity_detail, radioTitle);
        setTitle(detailTitle);

        /*SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(this);

        if(savedInstanceState != null){
            firstime = savedInstanceState.getBoolean("firstime");
        } else {
            firstime = false;
        }
        mTitleTextView = findViewById(R.id.song_title);
        mArtistTextView = findViewById(R.id.song_artist);
        mAlbumArt = findViewById(R.id.album_art);
        mMediaControlsImage = findViewById(R.id.button_play);
        mSeekBarAudio = findViewById(R.id.seekbar_audio);

        final ClickListener clickListener = new ClickListener();
        //findViewById(R.id.button_previous).setOnClickListener(clickListener);
        mMediaControlsImage.setOnClickListener(clickListener);
        //findViewById(R.id.button_next).setOnClickListener(clickListener);

        radioTitle = getIntent().getStringExtra("radio_name");
        radioID = getIntent().getIntExtra("radioID", 0);
        radioLink = getIntent().getData().toString();
        radioImage = getIntent().getStringExtra("radio_image");
        Log.d("radioImage", radioImage);

        //musicLibrary.setRadioTitle(radioTitle);
        musicLibrary = new MusicLibrary("Djoga_"+radioID, radioTitle, "Samtakie", "Online Radio Samtakie", "Radio", 100, TimeUnit.SECONDS,
                radioLink, R.drawable.album_jazz_blues, radioImage);


        musicLibrary.createMediaMetadataCompat();
        mMediaBrowserHelper = new MediaBrowserConnection(this);
        mMediaBrowserHelper.registerCallback(new MediaBrowserListener());

        Log.d("Detail", "onCreate has been called");

        //

        *//*editor.putString("radio_name", radioTitle);
        editor.commit();*//*

        fab = findViewById(R.id.fab);
        fabDel = findViewById(R.id.fabDel);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                *//*Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*//*
                String[] mProjection = {Contract.RadioEntry.COLUMN_ONLINE_RADIO_ID};
                String mSelectionClause = Contract.RadioEntry.COLUMN_ONLINE_RADIO_ID + " = ?";
                String[] selectionArgs = {""};
                selectionArgs[0] = String.valueOf(radioID);

                // Insert data to the online radio fav table via a contentresolver
                ContentValues contentValues = new ContentValues();

                contentValues.put(Contract.RadioEntry.COLUMN_ONLINE_RADIO_ID, radioID);
                contentValues.put(Contract.RadioEntry.COLUMN_ONLINE_RADIO_NAME, radioTitle);
                contentValues.put(Contract.RadioEntry.COLUMN_ONLINE_RADIO_LINK, radioLink);
                contentValues.put(Contract.RadioEntry.COLUMN_ONLINE_RADIO_IMAGE, radioImage);

                Cursor cursor = getContentResolver().query(
                        Contract.RadioEntry.CONTENT_URI_ONLINE_RADIO_FAV,
                        mProjection,
                        mSelectionClause,
                        selectionArgs,
                        null);

                // check and make sure the online radio doesn't exits in the fav table
                // if it is, ignore adding the data and igform the user.
                assert cursor != null;
                if(cursor.getCount() == 0){
                    getContentResolver().insert(Contract.RadioEntry.CONTENT_URI_ONLINE_RADIO_FAV, contentValues);
                    Snackbar.make(view, radioTitle + " has been added to your favorite", Snackbar.LENGTH_LONG).show();
                    fab.hide();
                    fabDel.show();
                } else {
                    Snackbar.make(view, radioTitle + " is already in your favorite", Snackbar.LENGTH_LONG).show();
                    // show the del Fab button

                    // hide the Add to fav fab button
                    fabDel.show();
                }


            }
        });

        // Fab button click for removing the online radio from the Favo list
        fabDel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                *//*Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*//*
                String[] mProjection = {Contract.RadioEntry.COLUMN_ONLINE_RADIO_ID};
                String mSelectionClause = Contract.RadioEntry.COLUMN_ONLINE_RADIO_ID + " = ?";
                String[] selectionArgs = {""};
                selectionArgs[0] = String.valueOf(radioID);

                // Insert data to the online radio fav table via a contentresolver
                ContentValues contentValues = new ContentValues();

                contentValues.put(Contract.RadioEntry.COLUMN_ONLINE_RADIO_ID, radioID);
                contentValues.put(Contract.RadioEntry.COLUMN_ONLINE_RADIO_NAME, radioTitle);
                contentValues.put(Contract.RadioEntry.COLUMN_ONLINE_RADIO_LINK, radioLink);
                contentValues.put(Contract.RadioEntry.COLUMN_ONLINE_RADIO_IMAGE, radioImage);

                Cursor cursor = getContentResolver().query(
                        Contract.RadioEntry.CONTENT_URI_ONLINE_RADIO_FAV,
                        mProjection,
                        mSelectionClause,
                        selectionArgs,
                        null);

                // check and make sure the online radio doesn't exits in the fav table
                // if it is, ignore adding the data and igform the user.
                assert cursor != null;
                if(cursor.getCount() != 0){
                    getContentResolver().delete(Contract.RadioEntry.CONTENT_URI_ONLINE_RADIO_FAV, mSelectionClause, selectionArgs);
                    Snackbar.make(view, radioTitle + " has been removed from your favorite", Snackbar.LENGTH_LONG).show();
                    fab.show();
                    fabDel.hide();
                } else {
                    Snackbar.make(view, radioTitle + " is already removed from your favorite", Snackbar.LENGTH_LONG).show();
                    // show the del Fab button

                    // hide the Add to fav fab button
                    fab.show();
                    fabDel.hide();
                }


            }
        });

        // Show the Add and the Del Fab button if the Radio Online data exist in the Favorite table
        radioInFavorite(radioID);*/

    }



    /*public void radioInFavorite(int id){
        Contract.RadioEntry.buildRadioFavItemUri(1);


        String[] mProjection = {Contract.RadioEntry.COLUMN_ONLINE_RADIO_ID};
        String mSelectionClause = Contract.RadioEntry.COLUMN_ONLINE_RADIO_ID + " = ?";
        String[] selectionArgs = {""};
        selectionArgs[0] = String.valueOf(id);

        Cursor cursor = getContentResolver().query(
                Contract.RadioEntry.CONTENT_URI_ONLINE_RADIO_FAV,
                mProjection,
                mSelectionClause,
                selectionArgs,
                null);

        // This will check if the radio station exists in the favorite database
        // Handle the Fab button accordingly, hide add button if it exist and show the del button.
        assert cursor != null;
        if(cursor.getCount() == 0){
            fab.show();
            fabDel.hide();
        } else {
            fab.hide();
            fabDel.show();
        }

    }*/

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

   /* *//**
     * Convenience class to collect the click listeners together.
     * <p>
     * In a larger app it's better to split the listeners out or to use your favorite
     * library.
     *//*
    private class ClickListener implements View.OnClickListener {
        @Override
        public void onClick(View v) {
            switch (v.getId()) {

                case R.id.button_play:
                    if (mIsPlaying) {
                        mMediaBrowserHelper.getTransportControls().pause();
                    } else {
                        mMediaBrowserHelper.getTransportControls().play();
                    }
                    break;

                case R.id.home:
                    if (mIsPlaying) {
                        mMediaBrowserHelper.getTransportControls().stop();
                    }
                    break;
            }
        }
    }

    *//**
     * Customize the connection to our {@link android.support.v4.media.MediaBrowserServiceCompat}
     * and implement our app specific desires.
     *//*
    private class MediaBrowserConnection extends MediaBrowserHelper {
        private MediaBrowserConnection(Context context) {
            super(context, MusicPlayerService.class);
        }

        @Override
        protected void onConnected(@NonNull MediaControllerCompat mediaController) {
            mSeekBarAudio.setMediaController(mediaController);

        }

        @Override
        protected void onChildrenLoaded(@NonNull String parentId,
                                        @NonNull List<MediaBrowserCompat.MediaItem> children) {
            super.onChildrenLoaded(parentId, children);

            final MediaControllerCompat mediaController = getMediaController();

            // Queue up all media items for this simple sample.
            for (final MediaBrowserCompat.MediaItem mediaItem : children) {
                mediaController.addQueueItem(mediaItem.getDescription());
            }

            // Call prepare now so pressing play just works.
            mediaController.getTransportControls().prepare();
            mediaController.getTransportControls().play();
        }
    }

    *//**
     * Implementation of the {@link MediaControllerCompat.Callback} methods we're interested in.
     * <p>
     * Here would also be where one could override
     * {@code onQueueChanged(List<MediaSessionCompat.QueueItem> queue)} to get informed when items
     * are added or removed from the queue. We don't do this here in order to keep the UI
     * simple.
     *//*
    private class MediaBrowserListener extends MediaControllerCompat.Callback {
        @Override
        public void onPlaybackStateChanged(PlaybackStateCompat playbackState) {
            mIsPlaying = playbackState != null &&
                    playbackState.getState() == PlaybackStateCompat.STATE_PLAYING;
            mMediaControlsImage.setPressed(mIsPlaying);
        }

        @Override
        public void onMetadataChanged(MediaMetadataCompat mediaMetadata) {
            if (mediaMetadata == null) {
                return;
            }

            mTitleTextView.setText(
                    mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_TITLE));
            mArtistTextView.setText(
                    mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_ARTIST));
            String radioImgLink = mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_ALBUM_ART_URI);
            String samtakieUrl = "http://www.samtakie.co.za/img/samtakie_radio/";
            String imgRadioUrl = samtakieUrl + radioImgLink +".jpg";
            Log.d("Album", imgRadioUrl);
            // Use Picasso
            Picasso picasso = Picasso.get();
            picasso.load(imgRadioUrl)
                    .placeholder(R.drawable.main_background)
                    .error(R.drawable.main_background)
                    .fit()
                    .into(mAlbumArt);
            *//*mAlbumArt.setImageBitmap(MusicLibrary.getAlbumBitmap(
                    DetailActivity.this,
                    mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID)));*//*
        }

        @Override
        public void onSessionDestroyed() {
            super.onSessionDestroyed();
        }

        @Override
        public void onQueueChanged(List<MediaSessionCompat.QueueItem> queue) {
            super.onQueueChanged(queue);
            //queue.get(queue.size());
        }
    }*/

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
}
