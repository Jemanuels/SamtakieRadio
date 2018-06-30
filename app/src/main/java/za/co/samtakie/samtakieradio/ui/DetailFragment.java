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


import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.media.MediaBrowserCompat;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.MediaControllerCompat;
import android.support.v4.media.session.MediaSessionCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.concurrent.TimeUnit;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import za.co.samtakie.samtakieradio.IOnFocusListenable;
import za.co.samtakie.samtakieradio.R;
import za.co.samtakie.samtakieradio.client.MediaBrowserHelper;
import za.co.samtakie.samtakieradio.provider.Contract;
import za.co.samtakie.samtakieradio.services.MusicPlayerService;
import za.co.samtakie.samtakieradio.services.contentcatalogs.MusicLibrary;

/**
 * A simple {@link Fragment} subclass.
 */
@SuppressWarnings("unused")
public class DetailFragment extends Fragment implements IOnFocusListenable{

    private int radioID;
    private String radioLink;
    private String radioName;
    private String radioImage;

    @BindView(R.id.album_art) ImageView mAlbumArt;
    @BindView(R.id.song_title) TextView mTitleTextView;
    @BindView(R.id.song_artist) TextView mArtistTextView;
    @BindView(R.id.button_play) ImageButton mMediaControlsImage;
    @BindView(R.id.fab) FloatingActionButton fab;
    @BindView(R.id.fabDel) FloatingActionButton fabDel;
    @BindView(R.id.adView) AdView mAdView;

    private MediaBrowserHelper mMediaBrowserHelper;
    private boolean mIsPlaying;

    private Context context;
    MusicLibrary musicLibrary;

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null){
            radioID = savedInstanceState.getInt("radioID", 0);
            radioName = savedInstanceState.getString("radio_name");
            radioImage = savedInstanceState.getString("radio_image");
            radioLink = savedInstanceState.getString("radio_link");
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {

        context = getContext();

        if(savedInstanceState != null){
            radioName = savedInstanceState.getString("radio_name");
            radioID = savedInstanceState.getInt("radioID", 0);
            radioImage = savedInstanceState.getString("radio_image");
            radioLink = savedInstanceState.getString("radio_link");
        }
        super.onCreate(savedInstanceState);
    }

    @SuppressLint("Recycle")
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);
        ButterKnife.bind(this, view);
        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);

        final ClickListener clickListener = new ClickListener();
        mMediaControlsImage.setOnClickListener(clickListener);

        musicLibrary = new MusicLibrary("Djoga_"+radioID, radioName, getString(R.string.artist_text), getString(R.string.album_string), getString(R.string.genre_text), 100, TimeUnit.SECONDS,
                radioLink, R.drawable.main_background, radioImage);

        musicLibrary.createMediaMetadataCompat();
        mMediaBrowserHelper = new MediaBrowserConnection(context);
        mMediaBrowserHelper.registerCallback(new MediaBrowserListener());

        // Show the Add and the Del Fab button if the Radio Online data exist in the Favorite table
        radioInFavorite(radioID);


        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        return view;
    }


    @OnClick(R.id.fab)
    public void addToFavorite(View view){

                String[] mProjection = {Contract.RadioEntry.COLUMN_ONLINE_RADIO_ID};
                String mSelectionClause = Contract.RadioEntry.COLUMN_ONLINE_RADIO_ID + " = ?";
                String[] selectionArgs = {""};
                selectionArgs[0] = String.valueOf(radioID);

                // Insert data to the online radio fav table via a content resolver
                ContentValues contentValues = new ContentValues();

                contentValues.put(Contract.RadioEntry.COLUMN_ONLINE_RADIO_ID, radioID);
                contentValues.put(Contract.RadioEntry.COLUMN_ONLINE_RADIO_NAME, radioName);
                contentValues.put(Contract.RadioEntry.COLUMN_ONLINE_RADIO_LINK, radioLink);
                contentValues.put(Contract.RadioEntry.COLUMN_ONLINE_RADIO_IMAGE, radioImage);

                Cursor cursor = getActivity().getContentResolver().query(
                        Contract.RadioEntry.CONTENT_URI_ONLINE_RADIO_FAV,
                        mProjection,
                        mSelectionClause,
                        selectionArgs,
                        null);

                // check and make sure the online radio doesn't exits in the fav table
                // if it is, ignore adding the data and inform the user.
                assert cursor != null;
                if(cursor.getCount() == 0){
                    getActivity().getContentResolver().insert(Contract.RadioEntry.CONTENT_URI_ONLINE_RADIO_FAV, contentValues);
                    Snackbar.make(view, radioName + " " + getString(R.string.add_fav_text), Snackbar.LENGTH_LONG).show();
                    fab.hide();
                    fabDel.show();
                } else {
                    Snackbar.make(view, radioName + " " + getString(R.string.already_add_fav_text), Snackbar.LENGTH_LONG).show();
                    // show the del Fab button
                    fabDel.show();
                }

    }

    @OnClick(R.id.fabDel)
    public void removeFromFavorite(View view){
        String[] mProjection = {Contract.RadioEntry.COLUMN_ONLINE_RADIO_ID};
        String mSelectionClause = Contract.RadioEntry.COLUMN_ONLINE_RADIO_ID + " = ?";
        String[] selectionArgs = {""};
        selectionArgs[0] = String.valueOf(radioID);

        // Insert data to the online radio fav table via a content resolver
        ContentValues contentValues = new ContentValues();

        contentValues.put(Contract.RadioEntry.COLUMN_ONLINE_RADIO_ID, radioID);
        contentValues.put(Contract.RadioEntry.COLUMN_ONLINE_RADIO_NAME, radioName);
        contentValues.put(Contract.RadioEntry.COLUMN_ONLINE_RADIO_LINK, radioLink);
        contentValues.put(Contract.RadioEntry.COLUMN_ONLINE_RADIO_IMAGE, radioImage);

        Cursor cursor = getActivity().getContentResolver().query(
                Contract.RadioEntry.CONTENT_URI_ONLINE_RADIO_FAV,
                mProjection,
                mSelectionClause,
                selectionArgs,
                null);

        // check and make sure the online radio doesn't exits in the fav table
        // if it is, ignore adding the data and inform the user.
        assert cursor != null;
        if(cursor.getCount() != 0){
            getActivity().getContentResolver().delete(Contract.RadioEntry.CONTENT_URI_ONLINE_RADIO_FAV, mSelectionClause, selectionArgs);
            Snackbar.make(view, radioName + " " + getString(R.string.remove_fav_text), Snackbar.LENGTH_LONG).show();
            fab.show();
            fabDel.hide();
        } else {
            Snackbar.make(view, radioName + " " + getString(R.string.already_remove_fav_text), Snackbar.LENGTH_LONG).show();
            // show the show Fab button
            // hide the Add to del fab button
            fab.show();
            fabDel.hide();
        }
    }

    @SuppressWarnings("ConstantConditions")
    public void radioInFavorite(int id){
        Contract.RadioEntry.buildRadioFavItemUri(1);
        String[] mProjection = {Contract.RadioEntry.COLUMN_ONLINE_RADIO_ID};
        String mSelectionClause = Contract.RadioEntry.COLUMN_ONLINE_RADIO_ID + " = ?";
        String[] selectionArgs = {""};
        selectionArgs[0] = String.valueOf(id);

        @SuppressLint("Recycle") Cursor cursor = getActivity().getContentResolver().query(
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

    }

    @Override
    public void onStart() {
        super.onStart();
        mMediaBrowserHelper.onStart();

    }

    @Override
    public void onStop() {
        super.onStop();
        musicLibrary = null;
        mMediaBrowserHelper.onStop();
    }


    @Override
    public void onPause() {
        super.onPause();
        mMediaBrowserHelper.onStop();
    }

    @SuppressWarnings("ConstantConditions")
    @Override
    public void onWindowFocusChanged(boolean hasFocus) {
       if(mIsPlaying){
            mMediaControlsImage.setPressed(mIsPlaying);
        }
    }


    /**
     * Convenience class to collect the click listeners together.
     * <p>
     * In a larger app it's better to split the listeners out or to use your favorite
     * library.
     */
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

    /**
     * Customize the connection to our {@link android.support.v4.media.MediaBrowserServiceCompat}
     * and implement our app specific desires.
     */
    private class MediaBrowserConnection extends MediaBrowserHelper {
        private MediaBrowserConnection(Context context) {
            super(context, MusicPlayerService.class);
        }

        @Override
        protected void onConnected(@NonNull MediaControllerCompat mediaController) {

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

    /**
     * Implementation of the {@link MediaControllerCompat.Callback} methods we're interested in.
     * <p>
     * Here would also be where one could override
     * {@code onQueueChanged(List<MediaSessionCompat.QueueItem> queue)} to get informed when items
     * are added or removed from the queue. We don't do this here in order to keep the UI
     * simple.
     */
    @SuppressWarnings("EmptyMethod")
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
            // Use Picasso
            Picasso picasso = Picasso.get();
            picasso.load(imgRadioUrl)
                    .placeholder(R.drawable.main_background)
                    .error(R.drawable.ic_error_200dp)
                    .fit()
                    .into(mAlbumArt);
        }

        @Override
        public void onQueueChanged(List<MediaSessionCompat.QueueItem> queue) {
            super.onQueueChanged(queue);
        }
    }


    public void setRadioID(int radioID) {
        this.radioID = radioID;
    }


    public void setRadioLink(String radioLink) {
        this.radioLink = radioLink;
    }

    public void setRadioName(String radioName) {
        this.radioName = radioName;
    }

    public void setRadioImage(String radioImage) {
        this.radioImage = radioImage;
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {

        super.onSaveInstanceState(outState);
        outState.putString("radio_name", radioName);
        outState.putInt("radioID", radioID);
        outState.putString("radio_image", radioImage);
        outState.putString("radio_link", radioLink);

    }
}