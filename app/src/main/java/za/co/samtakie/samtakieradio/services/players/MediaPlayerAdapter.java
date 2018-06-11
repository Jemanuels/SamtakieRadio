package za.co.samtakie.samtakieradio.services.players;

import android.content.Context;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.SystemClock;
import android.support.v4.media.MediaMetadataCompat;
import android.support.v4.media.session.PlaybackStateCompat;
import android.util.Log;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.DefaultRenderersFactory;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSource;
import com.google.android.exoplayer2.upstream.DefaultHttpDataSourceFactory;

import za.co.samtakie.samtakieradio.services.PlaybackInfoListener;
import za.co.samtakie.samtakieradio.services.PlayerAdapter;
import za.co.samtakie.samtakieradio.services.contentcatalogs.MusicLibrary;
import za.co.samtakie.samtakieradio.ui.DetailActivity;

public final class MediaPlayerAdapter extends PlayerAdapter {

    private static final int BUFFER_SEGMENT_SIZE = 64 * 1024;
    private static final int BUFFER_SEGMENT_COUNT = 256;

    private final Context mContext;
    private MediaPlayer mMediaPlayer;
    private String mFilename;
    private PlaybackInfoListener mPlaybackInfoListener;
    private MediaMetadataCompat mCurrentMedia;
    private int mState;
    private boolean mCurrentMediaPlayedToCompletion;

    private SimpleExoPlayer player;
    private boolean playWhenReady;
    private MediaSource mediaSource;

    // Work-around for a MediaPlayer bug related to the behavior of MediaPlayer.seekTo()
    // while not playing.
    private int mSeekWhileNotPlaying = -1;

    public MediaPlayerAdapter(Context context, PlaybackInfoListener listener) {
        super(context);
        mContext = context.getApplicationContext();
        mPlaybackInfoListener = listener;
    }

    /**
     * Once the {@link MediaPlayer} is released, it can't be used again, and another one has to be
     * created. In the onStop() method of the {@link DetailActivity} the {@link MediaPlayer} is
     * released. Then in the onStart() of the {@link DetailActivity} a new {@link MediaPlayer}
     * object has to be created. That's why this method is private, and called by load(int) and
     * not the constructor.
     */
    private void initializeMediaPlayer() {

        player = ExoPlayerFactory.newSimpleInstance(
                new DefaultRenderersFactory(mContext),
                new DefaultTrackSelector(), new DefaultLoadControl());

        //player.addListener(mContext);
        //player.addAudioDebugListener(componentListener);


        player.setPlayWhenReady(playWhenReady);




        /*if (mMediaPlayer == null) {
            mMediaPlayer = new MediaPlayer();
            mMediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mediaPlayer) {
                    mPlaybackInfoListener.onPlaybackCompleted();

                    // Set the state to "paused" because it most closely matches the state
                    // in MediaPlayer with regards to available state transitions compared
                    // to "stop".
                    // Paused allows: seekTo(), start(), pause(), stop()
                    // Stop allows: stop()
                    setNewState(PlaybackStateCompat.STATE_PAUSED);
                }
            });
        }*/
    }

    private MediaSource buildMediaSource(Uri uri) {

        return new ExtractorMediaSource.Factory(
                new DefaultHttpDataSourceFactory("SamtakieRadio", null, DefaultHttpDataSource.DEFAULT_CONNECT_TIMEOUT_MILLIS, 1800000, true)).createMediaSource(uri);

    }

    // Implements PlaybackControl.
    @Override
    public void playFromMedia(MediaMetadataCompat metadata) {
        mCurrentMedia = metadata;
        final String mediaId = metadata.getDescription().getMediaId();
        playFile(MusicLibrary.getMusicFilename(mediaId));
        //playFile("http://s6.voscast.com:8150/;");

    }

    @Override
    public void playFromUri(MediaMetadataCompat metadata) {

    }

    @Override
    public MediaMetadataCompat getCurrentMedia() {
        return mCurrentMedia;
    }

    private void playFile(String filename) {
        boolean mediaChanged = (mFilename == null || !filename.equals(mFilename));
        if (mCurrentMediaPlayedToCompletion) {
            // Last audio file was played to completion, the resourceId hasn't changed, but the
            // player was released, so force a reload of the media file for playback.
            mediaChanged = true;
            mCurrentMediaPlayedToCompletion = false;
        }
        if (!mediaChanged) {
            if (!isPlaying()) {
                play();
            }
            return;
        } else {
            release();
        }

        mFilename = filename;

        initializeMediaPlayer();

        try {
            //AssetFileDescriptor assetFileDescriptor = mContext.getAssets().openFd(mFilename);
            //mMediaPlayer.setDataSource(mContext, Uri.parse(mFilename));
            //Uri uri = Uri.parse(getString(R.string.radio_10));
            mediaSource = buildMediaSource(Uri.parse(mFilename));

        } catch (Exception e) {
            throw new RuntimeException("Failed to open file: " + mFilename, e);
        }

        try {
            //mMediaPlayer.prepare();
            player.prepare(mediaSource, false, true);
        } catch (Exception e) {
            throw new RuntimeException("Failed to open file: " + mFilename, e);
        }

        play();
    }

    @Override
    public void onStop() {
        // Regardless of whether or not the MediaPlayer has been created / started, the state must
        // be updated, so that MediaNotificationManager can take down the notification.
        setNewState(PlaybackStateCompat.STATE_STOPPED);
        release();
    }

    private void release() {
        /*if (mMediaPlayer != null) {
            mMediaPlayer.release();
            mMediaPlayer = null;
        }*/
        if(player != null){
            playWhenReady = false;
            //player.removeListener(this);
            //player.removeAudioDebugListener(this);
            player.stop();
            player.release();
            player = null;
        }
    }

    @Override
    public boolean isPlaying() {
        Log.d("MPA", "Status" + player.getPlayWhenReady());
        // return true if the player is playing and false of the player is not playing
        return player != null && player.getPlayWhenReady();

    }

    @Override
    protected void onPlay() {
        if (player != null && !player.getPlayWhenReady()) {
            player.setPlayWhenReady(true); // start the player
            setNewState(PlaybackStateCompat.STATE_PLAYING);
        }
    }

    @Override
    protected void onPause() {
        if (player != null && player.getPlayWhenReady()) {
            player.setPlayWhenReady(false);
            setNewState(PlaybackStateCompat.STATE_PAUSED);
        }
    }

    // This is the main reducer for the player state machine.
    private void setNewState(@PlaybackStateCompat.State int newPlayerState) {
        mState = newPlayerState;

        // Whether playback goes to completion, or whether it is stopped, the
        // mCurrentMediaPlayedToCompletion is set to true.
        if (mState == PlaybackStateCompat.STATE_STOPPED) {
            mCurrentMediaPlayedToCompletion = true;
        }

        // Work around for MediaPlayer.getCurrentPosition() when it changes while not playing.
        final long reportPosition;
        if (mSeekWhileNotPlaying >= 0) {
            reportPosition = mSeekWhileNotPlaying;

            if (mState == PlaybackStateCompat.STATE_PLAYING) {
                mSeekWhileNotPlaying = -1;
            }
        } else {
            reportPosition = player == null ? 0 : player.getCurrentPosition();
        }

        final PlaybackStateCompat.Builder stateBuilder = new PlaybackStateCompat.Builder();
        stateBuilder.setActions(getAvailableActions());
        stateBuilder.setState(mState,
                reportPosition,
                1.0f,
                SystemClock.elapsedRealtime());
        mPlaybackInfoListener.onPlaybackStateChange(stateBuilder.build());
    }

    /**
     * Set the current capabilities available on this session. Note: If a capability is not
     * listed in the bitmask of capabilities then the MediaSession will not handle it. For
     * example, if you don't want ACTION_STOP to be handled by the MediaSession, then don't
     * included it in the bitmask that's returned.
     */
    @PlaybackStateCompat.Actions
    private long getAvailableActions() {
        long actions = PlaybackStateCompat.ACTION_PLAY_FROM_MEDIA_ID
                | PlaybackStateCompat.ACTION_PLAY_FROM_SEARCH;
                /*| PlaybackStateCompat.ACTION_SKIP_TO_NEXT
                | PlaybackStateCompat.ACTION_SKIP_TO_PREVIOUS;*/
        switch (mState) {
            case PlaybackStateCompat.STATE_STOPPED:
                actions |= PlaybackStateCompat.ACTION_PLAY
                        | PlaybackStateCompat.ACTION_PAUSE;
                break;
            case PlaybackStateCompat.STATE_PLAYING:
                actions |= PlaybackStateCompat.ACTION_STOP
                        | PlaybackStateCompat.ACTION_PAUSE
                        | PlaybackStateCompat.ACTION_SEEK_TO;
                break;
            case PlaybackStateCompat.STATE_PAUSED:
                actions |= PlaybackStateCompat.ACTION_PLAY
                        | PlaybackStateCompat.ACTION_STOP;
                break;
            default:
                actions |= PlaybackStateCompat.ACTION_PLAY
                        | PlaybackStateCompat.ACTION_PLAY_PAUSE
                        | PlaybackStateCompat.ACTION_STOP
                        | PlaybackStateCompat.ACTION_PAUSE;
        }
        return actions;
    }

    @Override
    public void seekTo(long position) {
        if (player != null) {
            if (!player.getPlayWhenReady()) {
                mSeekWhileNotPlaying = (int) position;
            }
            player.seekTo((int) position);

            // Set the state (to the current state) because the position changed and should
            // be reported to clients.
            setNewState(mState);
        }
    }

    @Override
    public void setVolume(float volume) {
        if (player != null) {
            player.setVolume(volume);
        }
    }

}