package za.co.samtakie.samtakieradio.ui;


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
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * A simple {@link Fragment} subclass.
 */
public class DetailFragment extends Fragment {

    private int radioID;
    private int adapterPosition;
    private String radioLink;
    private String radioName;
    private String radioImage;

    private ImageView mAlbumArt;
    private TextView mTitleTextView;
    private TextView mArtistTextView;
    private ImageView mMediaControlsImage;
    //private MediaSeekBar mSeekBarAudio;
    private MediaBrowserHelper mMediaBrowserHelper;
    private boolean mIsPlaying;

    private FloatingActionButton fab;
    private FloatingActionButton fabDel;
    private Context context;
    MusicLibrary musicLibrary;
    boolean firstime = true;

    public DetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(savedInstanceState != null){
            radioID = savedInstanceState.getInt("radioID", 0);
            radioName = savedInstanceState.getString("radio_name");
            radioImage = savedInstanceState.getString("radio_image");
            radioLink = savedInstanceState.getString("radio_link");
            Log.d("radioImage", radioImage);
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(context);


        mTitleTextView = view.findViewById(R.id.song_title);
        mArtistTextView = view.findViewById(R.id.song_artist);
        mAlbumArt = view.findViewById(R.id.album_art);
        mMediaControlsImage = view.findViewById(R.id.button_play);
        //mSeekBarAudio = view.findViewById(R.id.seekbar_audio);

        final ClickListener clickListener = new ClickListener();
        //findViewById(R.id.button_previous).setOnClickListener(clickListener);
        mMediaControlsImage.setOnClickListener(clickListener);
        //findViewById(R.id.button_next).setOnClickListener(clickListener);




        //musicLibrary.setRadioTitle(radioTitle);
        musicLibrary = new MusicLibrary("Djoga_"+radioID, radioName, "Samtakie", "Online Radio Samtakie", "Radio", 100, TimeUnit.SECONDS,
                radioLink, R.drawable.album_jazz_blues, radioImage);


        musicLibrary.createMediaMetadataCompat();
        mMediaBrowserHelper = new MediaBrowserConnection(context);
        mMediaBrowserHelper.registerCallback(new MediaBrowserListener());

        Log.d("Detail", "onCreate has been called");

        //

        /*editor.putString("radio_name", radioTitle);
        editor.commit();*/

        fab = view.findViewById(R.id.fab);
        fabDel = view.findViewById(R.id.fabDel);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                /*Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                String[] mProjection = {Contract.RadioEntry.COLUMN_ONLINE_RADIO_ID};
                String mSelectionClause = Contract.RadioEntry.COLUMN_ONLINE_RADIO_ID + " = ?";
                String[] selectionArgs = {""};
                selectionArgs[0] = String.valueOf(radioID);

                // Insert data to the online radio fav table via a contentresolver
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
                // if it is, ignore adding the data and igform the user.
                assert cursor != null;
                if(cursor.getCount() == 0){
                    getActivity().getContentResolver().insert(Contract.RadioEntry.CONTENT_URI_ONLINE_RADIO_FAV, contentValues);
                    Snackbar.make(view, radioName + " has been added to your favorite", Snackbar.LENGTH_LONG).show();
                    fab.hide();
                    fabDel.show();
                } else {
                    Snackbar.make(view, radioName + " is already in your favorite", Snackbar.LENGTH_LONG).show();
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
                /*Snackbar.make(view, "Here's a Snackbar", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();*/
                String[] mProjection = {Contract.RadioEntry.COLUMN_ONLINE_RADIO_ID};
                String mSelectionClause = Contract.RadioEntry.COLUMN_ONLINE_RADIO_ID + " = ?";
                String[] selectionArgs = {""};
                selectionArgs[0] = String.valueOf(radioID);

                // Insert data to the online radio fav table via a contentresolver
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
                // if it is, ignore adding the data and igform the user.
                assert cursor != null;
                if(cursor.getCount() != 0){
                    getActivity().getContentResolver().delete(Contract.RadioEntry.CONTENT_URI_ONLINE_RADIO_FAV, mSelectionClause, selectionArgs);
                    Snackbar.make(view, radioName + " has been removed from your favorite", Snackbar.LENGTH_LONG).show();
                    fab.show();
                    fabDel.hide();
                } else {
                    Snackbar.make(view, radioName + " is already removed from your favorite", Snackbar.LENGTH_LONG).show();
                    // show the del Fab button

                    // hide the Add to fav fab button
                    fab.show();
                    fabDel.hide();
                }


            }
        });

        // Show the Add and the Del Fab button if the Radio Online data exist in the Favorite table
        radioInFavorite(radioID);


        return view;
    }

    public void radioInFavorite(int id){
        Contract.RadioEntry.buildRadioFavItemUri(1);


        String[] mProjection = {Contract.RadioEntry.COLUMN_ONLINE_RADIO_ID};
        String mSelectionClause = Contract.RadioEntry.COLUMN_ONLINE_RADIO_ID + " = ?";
        String[] selectionArgs = {""};
        selectionArgs[0] = String.valueOf(id);

        Cursor cursor = getActivity().getContentResolver().query(
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
        //mSeekBarAudio.disconnectController();
        mMediaBrowserHelper.onStop();
        Log.d("Detail", "onStop has been called");

    }

    @Override
    public void onPause() {
        super.onPause();
        //mSeekBarAudio.disconnectController();
        mMediaBrowserHelper.onStop();
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
            //mSeekBarAudio.setMediaController(mediaController);

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
            /*mAlbumArt.setImageBitmap(MusicLibrary.getAlbumBitmap(
                    DetailActivity.this,
                    mediaMetadata.getString(MediaMetadataCompat.METADATA_KEY_MEDIA_ID)));*/
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