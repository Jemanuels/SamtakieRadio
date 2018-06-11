package za.co.samtakie.samtakieradio.services;


import android.support.v4.media.session.PlaybackStateCompat;

/**
 * Listener to provide state updates from {@link PlayerAdapter} (the media player)
 * to {@link MusicPlayerService} (the service that holds our {@link android.support.v4.media.MediaMetadataCompat}.
 */
public abstract class PlaybackInfoListener {

    public abstract void onPlaybackStateChange(PlaybackStateCompat state);

    public void onPlaybackCompleted() {
    }
}
