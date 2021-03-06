package za.co.samtakie.samtakieradio.widget;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Handler;
import android.os.Looper;
import android.widget.RemoteViews;

import com.squareup.picasso.Picasso;

import za.co.samtakie.samtakieradio.R;
import za.co.samtakie.samtakieradio.RadioOnline;
import za.co.samtakie.samtakieradio.ui.DetailActivity;

/**
 * Implementation of App Widget functionality.
 * App Widget Configuration implemented in {@link OnlineRadioAppWidgetConfigureActivity OnlineRadioAppWidgetConfigureActivity}
 */
public class OnlineRadioAppWidget extends AppWidgetProvider {

    //private final static String ACTION_SIMPLEWIDGET = "ACTION_BROADCASTWIDGETSAMPLE";
    //private static int counter = 0;

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId) {
        RadioOnline radioData = OnlineRadioAppWidgetConfigureActivity.loadRadioNamePref(context, appWidgetId);

        CharSequence widgetText = radioData.getAppwidget();
            String radioName = radioData.getRadioName();
            String radioImage = radioData.getRadioImage();
            int radioID = radioData.getRadioID();
            String radioLink = radioData.getRadioLink();
            final int[] appWidId = {appWidgetId};

        // Construct the RemoteViews object
        final RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.online_radio_app_widget);
        views.setTextViewText(R.id.appwidget_text,context.getString(R.string.widget_play_text) + widgetText);

        /* Load the image using Picasso */
        final Picasso picasso = Picasso.get();
        String samtakieUrl = "http://www.samtakie.co.za/img/samtakie_radio/";
        final String imgRadioUrl = samtakieUrl + radioImage +".jpg";


        Handler uiHandler = new Handler(Looper.getMainLooper());
        uiHandler.post(new Runnable() {
            @Override
            public void run() {

                picasso.load(imgRadioUrl)
                        .into(views, R.id.radioImage, appWidId);
            }
        });



        Intent intent = new Intent(context, DetailActivity.class);
        if(radioLink != null) {
            intent.setData(Uri.parse(radioLink));
        }
        intent.putExtra("radio_name", radioName); // add the radio name in the intent
        intent.putExtra("radio_image", radioImage); // add the radio image in the intent
        intent.putExtra("radioID", radioID); // add the radio ID in the intent
        //PendingIntent pendingIntent =  MediaButtonReceiver.buildMediaButtonPendingIntent(context, PlaybackStateCompat.ACTION_PLAY);
        PendingIntent pendingIntent = PendingIntent.getActivity(context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.playBtn, pendingIntent);

        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }



    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        for (int appWidgetId : appWidgetIds) {
            AppWidgetIntentService.startActionUpdateWidget(context,appWidgetId );
        }
    }

    @Override
    public void onDeleted(Context context, int[] appWidgetIds) {
        // When the user deletes the widget, delete the preference associated with it.
        for (int appWidgetId : appWidgetIds) {
            OnlineRadioAppWidgetConfigureActivity.deleteTitlePref(context, appWidgetId);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}