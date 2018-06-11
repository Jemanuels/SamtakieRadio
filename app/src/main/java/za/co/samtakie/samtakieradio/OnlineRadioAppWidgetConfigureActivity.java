package za.co.samtakie.samtakieradio;

import android.appwidget.AppWidgetManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import za.co.samtakie.samtakieradio.ui.ItemFragment;

/**
 * The configuration screen for the {@link OnlineRadioAppWidget OnlineRadioAppWidget} AppWidget.
 */
public class OnlineRadioAppWidgetConfigureActivity extends AppCompatActivity implements ItemFragment.RadioWidgetAdapterOnClickHandler {


    //private static SharedPreferences radioWidgetPref;
    public static final String PREFS_NAME = "za.co.samtakie.samtakieradio.OnlineRadioAppWidget";
    private static final String PREF_PREFIX_KEY = "appwidget_";
    private static final String PREF_PREFIX_WIDGETID = "widgetid_";
    private static final String PREF_PREFIX_POSITION = "recipePosition_";
    private static final String PREF_PREFIX_RADIO_NAME = "radioName_";
    private static final String PREF_PREFIX_RADIO_IMAGE = "radioImage_";
    private static final String PREF_PREFIX_RADIO_ID = "radioID_";
    private static final String PREF_PREFIX_RADIO_LINK = "radioLink_";
    private static final String TAG = OnlineRadioAppWidgetConfigureActivity.class.getSimpleName();

    int mAppWidgetId = AppWidgetManager.INVALID_APPWIDGET_ID;

    View.OnClickListener mOnClickListener = new View.OnClickListener() {
        public void onClick(View v) {
            // If the user click on the cancel button we want to cancel the setup
            // of the AppWidget.
            finish();
        }
    };

    public OnlineRadioAppWidgetConfigureActivity() {
        super();
    }

    // Write the prefix to the SharedPreferences object for this widget
    /*int radioID, View view, int adapterPosition, String radioLink, String radioName, String radioImage*/
    /*
    * intent.putExtra("radio_name", radioName); // add the radio name in the intent
        intent.putExtra("radio_image", radioImage); // add the raoio image in the intent
        intent.putExtra("radioID", radioID); // add the radio ID in the intent
        intent.setData(Uri.parse(radioLink)); // add the radio link in the intent
    * */
    static boolean saveRadioDataPref(Context context, int appWidgetId, String appwidget, String radioName, String radioImage, int radioID, String radioLink) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.putString(PREF_PREFIX_KEY + appWidgetId, appwidget);
        prefs.putString(PREF_PREFIX_RADIO_NAME + appWidgetId, radioName);
        prefs.putString(PREF_PREFIX_RADIO_IMAGE + appWidgetId, radioImage);
        prefs.putInt(PREF_PREFIX_RADIO_ID + appWidgetId, radioID);
        prefs.putString(PREF_PREFIX_RADIO_LINK + appWidgetId, radioLink);
        prefs.apply();

        return true;
    }

    // Read the prefix from the SharedPreferences object for this widget.
    // If there is no preference saved, get the default from a resource
    static String loadTitlePref(Context context, int appWidgetId) {
        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String titleValue = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        if (titleValue != null) {
            return titleValue;
        } else {
            return context.getString(R.string.appwidget_text);
        }
    }

    static RadioOnline loadRadioNamePref(Context context, int appWidgetId) {

        SharedPreferences prefs = context.getSharedPreferences(PREFS_NAME, 0);
        String appWidget = prefs.getString(PREF_PREFIX_KEY + appWidgetId, null);
        String radioName = prefs.getString(PREF_PREFIX_RADIO_NAME + appWidgetId, null);
        String radioImage = prefs.getString(PREF_PREFIX_RADIO_IMAGE + appWidgetId, null);
        int radioID = prefs.getInt(PREF_PREFIX_RADIO_ID + appWidgetId, 0);
        String radioLink = prefs.getString(PREF_PREFIX_RADIO_LINK + appWidgetId, null);

        RadioOnline radioData = new RadioOnline(appWidget,radioName, radioImage, radioID, radioLink);


        if (radioData != null) {
            Log.d("LoadRadioNamePref" , "radioData is not Null");
            return radioData;
        } else {
            return null;
        }
    }

    static void deleteTitlePref(Context context, int appWidgetId) {
        SharedPreferences.Editor prefs = context.getSharedPreferences(PREFS_NAME, 0).edit();
        prefs.remove(PREF_PREFIX_KEY + appWidgetId);
        prefs.apply();
    }

    @Override
    public void onCreate(Bundle icicle) {
        super.onCreate(icicle);
        Log.d("ConfigurationActivity", "onCreate has been called");
        // Set the result to CANCELED.  This will cause the widget host to cancel
        // out of the widget placement if the user presses the back button.
        setResult(RESULT_CANCELED);

        //Set the layout of the radio app widget configuration activity
        setContentView(R.layout.online_radio_app_widget_configure);

        // set the cancel button and add the listener to the button
        // If the user click on this button it will cancel the widget placement
        findViewById(R.id.cancel_button).setOnClickListener(mOnClickListener);

        // Find the widget id from the intent.
        Intent intent = getIntent();
        Bundle extras = intent.getExtras();
        if (extras != null) {
            mAppWidgetId = extras.getInt(
                    AppWidgetManager.EXTRA_APPWIDGET_ID, AppWidgetManager.INVALID_APPWIDGET_ID);
        }

        // If this activity was started with an intent without an app widget ID, finish with an error.
        if (mAppWidgetId == AppWidgetManager.INVALID_APPWIDGET_ID) {
            finish();
            return;
        }
    }


    @Override
    public void radioItemOnClickHandler(int radioID, View view, int adapterPosition, String radioLink, String radioName, String radioImage) {
        displayToast("Get the view ID clicked " + view.getId());
        displayToast("Radio ID is " + radioID);
        displayToast("The Radio Link is " + radioLink);
        displayToast("The Radio name is " + radioName);
        displayToast("The radioImage url is " + radioImage);
        //Todo: Create the appwidget and set the defualt data to be viewed everytime the widget is being clicked

        final Context context = OnlineRadioAppWidgetConfigureActivity.this;

        // When the button is clicked, store the string locally
        String widgetText = radioName;
        boolean saveData =  saveRadioDataPref(context, mAppWidgetId, widgetText, radioName, radioImage, radioID, radioLink);

        // It is the responsibility of the configuration activity to update the app widget
        // if the saveData return true we can call updateAppWidget.
        if(saveData) {
            AppWidgetManager appWidgetManager = AppWidgetManager.getInstance(context);
            OnlineRadioAppWidget.updateAppWidget(context, appWidgetManager, mAppWidgetId);

            // Make sure we pass back the original appWidgetId
            Intent resultValue = new Intent();
            resultValue.putExtra(AppWidgetManager.EXTRA_APPWIDGET_ID, mAppWidgetId);
            setResult(RESULT_OK, resultValue);
            finish();
        }
    }

    public void displayToast(String message){
        //Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
        Log.d(TAG, message);
    }
}