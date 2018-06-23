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
package za.co.samtakie.samtakieradio.sync;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import java.net.URL;

import za.co.samtakie.samtakieradio.provider.Contract;
import za.co.samtakie.samtakieradio.utilities.NetworkUtils;
import za.co.samtakie.samtakieradio.utilities.OpenRadioJsonUtils;

@SuppressWarnings("WeakerAccess")
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