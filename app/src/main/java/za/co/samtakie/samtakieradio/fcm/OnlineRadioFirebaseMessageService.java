package za.co.samtakie.samtakieradio.fcm;



import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.RequiresApi;
import android.support.v4.app.NotificationCompat;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;

import java.text.SimpleDateFormat;
import java.util.Map;

import za.co.samtakie.samtakieradio.R;
import za.co.samtakie.samtakieradio.provider.Contract;
import za.co.samtakie.samtakieradio.ui.News;

public class OnlineRadioFirebaseMessageService extends FirebaseMessagingService {

    private final String CHANNEL_ID = getString(R.string.channelid_cloud_message);

    private final int NOTIFICATION_MAX_CHARACTERS = getResources().getInteger(R.integer.notification_max_characters);

    /***
     * Called when message is received.
     *
     * @param remoteMessage Object representing the message received from Firebase Cloud Messaging
     */
    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {

        // There are two types of messages data messages and notification messages. Data messages
        // are handled
        // here in onMessageReceived whether the app is in the foreground or background. Data
        // messages are the type
        // traditionally used with FCM. Notification messages are only received here in
        // onMessageReceived when the app
        // is in the foreground. When the app is in the background an automatically generated
        // notification is displayed.
        // When the user taps on the notification they are returned to the app. Messages
        // containing both notification
        // and data payloads are treated as notification messages. The Firebase console always
        // sends notification
        // messages. For more see: https://firebase.google.com/docs/cloud-messaging/concept-options\

        // The Online Radio server always sends just *data* messages, meaning that onMessageReceived when
        // the app is both in the foreground AND the background

        // Check if message contains a data payload.
        Map<String, String> data = remoteMessage.getData();
        if (data.size() > 0) {
            // check and make sure that the message received is not empty
            // Ignore if empty
            if (!data.get(getString(R.string.cloud_message)).equals("")) {

                sendNotification(data);
                insertNews(data);
            }
        }
    }

    /**
     * Inserts a single squawk into the database;
     *
     * @param data Map which has the message data in it
     */
    private void insertNews(final Map<String, String> data) {

        // Database operations should not be done on the main thread
        // For this we have created a AsyncTask to process it in the background
        AsyncTask<Void, Void, Void> insertNewsTask = new AsyncTask<Void, Void, Void>() {

            @Override
            protected Void doInBackground(Void... voids) {
                ContentValues newMessage = new ContentValues();
                newMessage.put(Contract.RadioEntry.COLUMN_ONLINE_RADIO_NEWS_TITLE, data.get(getString(R.string.cloud_title)));
                newMessage.put(Contract.RadioEntry.COLUMN_ONLINE_RADIO_NEWS_DATE, Long.valueOf(data.get(getString(R.string.cloud_date))));
                newMessage.put(Contract.RadioEntry.COLUMN_ONLINE_RADIO_NEWS_MESSAGE, data.get(getString(R.string.cloud_message)).trim());
                getContentResolver().insert(Contract.RadioEntry.CONTENT_URI_ONLINE_RADIO_NEWS, newMessage);
                return null;
            }
        };

        // execute the Task
        insertNewsTask.execute();
    }

    // This method will return true if the android version is O or earlier
    private boolean isAndroidOOrHigher() {
        return Build.VERSION.SDK_INT >= Build.VERSION_CODES.O;
    }

    /***
     * Does nothing on versions of Android earlier than O.
     * @param mNotificationManager a reference to the NotificationManager class
     */
    @RequiresApi(Build.VERSION_CODES.O)
    private void createChannel( NotificationManager mNotificationManager) {
        if (mNotificationManager.getNotificationChannel(CHANNEL_ID) == null) {
            // The user-visible name of the channel.
            CharSequence name = getString(R.string.createChannel_name);
            // The user-visible description of the channel.
            String description = getString(R.string.createChannel_description);
            int importance = NotificationManager.IMPORTANCE_LOW;
            NotificationChannel mChannel = new NotificationChannel(CHANNEL_ID, name, importance);
            // Configure the notification channel.
            mChannel.setDescription(description);
            mChannel.enableLights(true);
            // Sets the notification light color for notifications posted to this
            // channel, if the device supports this feature.
            mChannel.setLightColor(Color.RED);
            mChannel.enableVibration(true);
            mChannel.setVibrationPattern(
                    new long[]{100, 200, 300, 400, 500, 400, 300, 200, 400});
            mNotificationManager.createNotificationChannel(mChannel);
        }
    }

    /**
     * Create and show a simple notification containing the received FCM message
     *
     * @param data Map which has the message data in it
     */
    private void sendNotification(Map<String, String> data) {
        Intent intent = new Intent(this, News.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        // Create the pending intent to launch the activity
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0 /* Request code */, intent,
                PendingIntent.FLAG_ONE_SHOT);

        // Get the tile of the cloud message
        String title = data.get(getString(R.string.cloud_title));
        // Get the body of the cloud message
        String message = data.get(getString(R.string.cloud_message));

        // If the message is longer than the max number of characters we want in our
        // notification, truncate it and add the unicode character for ellipsis
        if (message.length() > NOTIFICATION_MAX_CHARACTERS) {
            message = message.substring(0, NOTIFICATION_MAX_CHARACTERS) + "\u2026";
        }

        Uri defaultSoundUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setSmallIcon(R.drawable.exo_notification_small_icon)
                .setContentTitle(title)
                .setContentText(message)
                .setAutoCancel(true)
                .setSound(defaultSoundUri)
                .setContentIntent(pendingIntent);

        NotificationManager notificationManager =
                (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);

        // Create the Channel if Android version is O or earlier
        if(isAndroidOOrHigher()) {
            createChannel(notificationManager);
        }

        notificationManager.notify(0 /* ID of notification */, notificationBuilder.build());
    }
}