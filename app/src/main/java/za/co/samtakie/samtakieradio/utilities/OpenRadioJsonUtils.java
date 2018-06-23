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
package za.co.samtakie.samtakieradio.utilities;

import android.content.ContentValues;
import android.content.Context;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import za.co.samtakie.samtakieradio.provider.Contract;

@SuppressWarnings("WeakerAccess")
public class OpenRadioJsonUtils {

    public static final String RADIO_ID = "ID";
    public static final String RADIO_NAME = "radio_name";
    public static final String RADIO_LINK = "radio_link";
    public static final String RADIO_IMAGE = "radio_image";

    @SuppressWarnings("unused")
    public static ContentValues[] getSimpleRadioStringFromJson(Context context, String jsonResponse) throws JSONException{
        JSONObject radioJsonObject = new JSONObject(jsonResponse);
        JSONArray radioArray = radioJsonObject.getJSONArray("online_radio");

        ContentValues[] radioContentValues = new ContentValues[radioArray.length()];

        for(int i = 0; i < radioArray.length(); i++){
            ContentValues radioValues = new ContentValues();

            //Log.d("Json Util ", radioArray.getString(i));

            String radioName;
            int radioID;
            String radioLink;
            String radioImage;

            JSONObject jsonObject = radioArray.getJSONObject(i);

            radioID = jsonObject.getInt(RADIO_ID);
            radioName = jsonObject.getString(RADIO_NAME);
            radioLink = jsonObject.getString(RADIO_LINK);
            radioImage = jsonObject.getString(RADIO_IMAGE);

            radioValues.put(Contract.RadioEntry.COLUMN_ONLINE_RADIO_ID, radioID);
            radioValues.put(Contract.RadioEntry.COLUMN_ONLINE_RADIO_NAME, radioName);
            radioValues.put(Contract.RadioEntry.COLUMN_ONLINE_RADIO_LINK, radioLink);
            radioValues.put(Contract.RadioEntry.COLUMN_ONLINE_RADIO_IMAGE, radioImage);

            radioContentValues[i] = radioValues;

        }

        return radioContentValues;
    }
}