package za.co.samtakie.samtakieradio.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import java.net.URL;

import za.co.samtakie.samtakieradio.data.Contract;
import za.co.samtakie.samtakieradio.utilities.NetworkUtils;
import za.co.samtakie.samtakieradio.utilities.OpenRadioJsonUtils;

public class RadioSyncTask {

    /**
     * Performs the network request for updated radio stations, parses the JSON from that request, and
     * inserts the new online radio stations information into our ContentProvider.
     *
     * @param context Used to access utility methods and the ContentResolver
     */
    synchronized public static void syncRadio(Context context){
        try{
            /*
            * The getUrl method will return the URL that we need to get the radio data
            * JSON for the radios.*/
            URL radioRequestUrl = NetworkUtils.buildUrl();

            String jsonRadioResponse = NetworkUtils.getResponseFromHttpUrl(radioRequestUrl);

            ContentValues[] radioValues = OpenRadioJsonUtils.getSimpleRadioStringFromJson(context, jsonRadioResponse);

            if(radioValues != null && radioValues.length != 0){
                ContentResolver radioContentResolver = context.getContentResolver();

                // delete all data using the delete method from the content resolver.
                radioContentResolver.delete(Contract.RadioEntry.CONTENT_URI_ONLINE_RADIO,
                        null,
                        null);
                // insert our new data using the insert method from the content resolver.
                radioContentResolver.bulkInsert(Contract.RadioEntry.CONTENT_URI_ONLINE_RADIO,
                        radioValues);

            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}