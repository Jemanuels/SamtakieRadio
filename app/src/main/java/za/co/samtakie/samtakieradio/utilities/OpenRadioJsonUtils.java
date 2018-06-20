package za.co.samtakie.samtakieradio.utilities;

import android.content.ContentValues;
import android.content.Context;
import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import za.co.samtakie.samtakieradio.provider.Contract;

public class OpenRadioJsonUtils {

    public static final String RADIO_ID = "ID";
    public static final String RADIO_NAME = "radio_name";
    public static final String RADIO_LINK = "radio_link";
    public static final String RADIO_IMAGE = "radio_image";

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

            Log.d("Json Util ", "ID is " + String.valueOf(radioID));
            Log.d("Json Util ", "Name is " + radioName);
            Log.d("Json Util ", "Link is " + radioLink);
            Log.d("Json Util ", "Image is " + radioImage + "\n");

            radioValues.put(Contract.RadioEntry.COLUMN_ONLINE_RADIO_ID, radioID);
            radioValues.put(Contract.RadioEntry.COLUMN_ONLINE_RADIO_NAME, radioName);
            radioValues.put(Contract.RadioEntry.COLUMN_ONLINE_RADIO_LINK, radioLink);
            radioValues.put(Contract.RadioEntry.COLUMN_ONLINE_RADIO_IMAGE, radioImage);

            radioContentValues[i] = radioValues;

            Log.d("Radio Values", radioValues.toString());
        }

        return radioContentValues;
    }
}