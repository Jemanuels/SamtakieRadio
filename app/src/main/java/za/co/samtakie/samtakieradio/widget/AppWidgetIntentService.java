package za.co.samtakie.samtakieradio.widget;

import android.app.IntentService;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.appwidget.AppWidgetManager;
import android.content.Intent;
import android.content.Context;
import android.os.Build;
import android.util.Log;

import za.co.samtakie.samtakieradio.R;

/**
 * An {@link IntentService} subclass for handling asynchronous task requests in
 * a service on a separate handler thread.
 * <p>
 * TODO: Customize class - update intent actions, extra parameters and static
 * helper methods.
 */
public class AppWidgetIntentService extends IntentService {

    private static final String ACTION_UPDATE_WIDGET = "za.co.samtakie.samtakieradio.action.UPDATE_WIDGET";
    @SuppressWarnings("FieldCanBeLocal")
    private final String CHANNEL_ID = "channel_1";


    private static final String EXTRA_APPWIDGETID = "za.co.samtakie.samtakieradio.extra.APPWIDGETID";

    public AppWidgetIntentService() {
        super("AppWidgetIntentService");
    }

    /**
     * Starts this service to perform action Foo with the given parameters. If
     * the service is already performing a task this action will be queued.
     *
     * @see IntentService
     */
    // TODO: Customize helper method
    public static void startActionUpdateWidget(Context context, int appwidgetId) {
        Intent intent = new Intent(context, AppWidgetIntentService.class);
        intent.setAction(ACTION_UPDATE_WIDGET);
        intent.putExtra(EXTRA_APPWIDGETID, appwidgetId);
        //intent.putExtra(EXTRA_PARAM2, param2);
        if(Build.VERSION.SDK_INT > Build.VERSION_CODES.N_MR1){
            context.startForegroundService(intent);
        } else {
            context.startService(intent);
        }
    }



    @Override
    public void onCreate() {
        super.onCreate();

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            // Create the NotificationChannel, but only on API 26+ because
            // the NotificationChannel class is new and not in the support library
            CharSequence name = "Samtakie";
            String description = "Samtakie Online Radio";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(CHANNEL_ID, name, importance);
            channel.setDescription(description);
            // Register the channel with the system
            NotificationManager notificationManager = (NotificationManager) this
                    .getSystemService(Context.NOTIFICATION_SERVICE);
            assert notificationManager != null;
            notificationManager.createNotificationChannel(channel);


            Notification.Builder mBuilder =
                    new Notification.Builder(this, CHANNEL_ID)
                            .setSmallIcon(R.mipmap.ic_launcher)
                            .setContentTitle("Online radio Notification");

            notificationManager.notify(1, mBuilder.build());
            startForeground(1,mBuilder.build());
        }
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        if (intent != null) {
            final String action = intent.getAction();
            if (ACTION_UPDATE_WIDGET.equals(action)) {
                final int appwidgetId = intent.getIntExtra(EXTRA_APPWIDGETID, 0);
                handleActionUpdateWidget(appwidgetId);

            }
        }
    }

    /**
     * Handle action Foo in the provided background thread with the provided
     * parameters.
     */
    private void handleActionUpdateWidget(int AppwidgetId) {
        // TODO: Handle action Foo
        AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(this);

        //int[] appWidgetIds = appWidgetManager.getAppWidgetIds(new ComponentName(this, OnlineRadioAppWidget.class));
        //for (int appWidgetId: appWidgetIds){
            //Log.d("Intent",""+appWidgetId);
        OnlineRadioAppWidget.updateAppWidget(this, appWidgetManager, AppwidgetId);


    }

}
