package za.co.samtakie.samtakieradio.data;

import android.net.Uri;
import android.provider.BaseColumns;

/***
 * Created by Jurgen Emanuels on 2018/05/01
 * This contract class defines constants that helps the application work with the content URIs,
 * column names, intent actions, and other features of the Baking content provider.
 * It also includes a @RadioEntry class
 */

public class Contract {

    /* The authority, which is how the code knows which Content Provider to access */
    public static final String AUTHORITY = "za.co.samtakie.samtakieradio";

    /*The base content URI = "content://" + <authority> */
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + AUTHORITY);

    /* Define the possible paths for accessing data in this contract
     * This is the path for the radio directory. */
    public static final String PATH_ONLINE_RADIO = "online_radio";

    /* Define the possible paths for accessing data in this contract
     * This is the path for the radio fav directory. */
    public static final String PATH_ONLINE_RADIO_FAV = "online_radio_fav";

    /* This is a variable for the Widget app in case there is no valid recipe ID */
    public static final int INVALID_RADIO_ID = -1;

    /***
     * RadioEntry class that implements the BaseColumns interface
     * Create static final members for the table name and each of the db columns
     * in this class
     */
    public static final class RadioEntry implements BaseColumns{

        /* RadioEntry content URI = base content URI + path for the radio directory */
        public static final Uri CONTENT_URI_ONLINE_RADIO = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ONLINE_RADIO).build();

        /* RadioEntry content URI = base content URI + path for the radio fav directory */
        public static final Uri CONTENT_URI_ONLINE_RADIO_FAV = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ONLINE_RADIO_FAV).build();

        /* Create static final members for the table name and each of the db columns
         * Online_Radio table and columns */

        // Table name
        public static final String TABLE_NAME = "online_radio";

        // Table name favorite list
        public static final String TABLE_NAME_FAV = "online_radio_fav";

        // column online radio ID
        public static final String COLUMN_ONLINE_RADIO_ID = "id";

        // column online radio name
        public static final String COLUMN_ONLINE_RADIO_NAME = "radio_name";

        // column online radio link
        public static final String COLUMN_ONLINE_RADIO_LINK = "radio_link";

        // column online radio name
        public static final String COLUMN_ONLINE_RADIO_IMAGE = "radio_image";

        /***
         * This static method huilds an Uri that adds the radio id to the end of the radio content Uri path.
         * This is used to query details about a single recipe entry by id.
         * @param radioID this is the id of the radio in the Database
         * @return the Uri link for a single item
         */
        public static Uri buildRadioItemUri(int radioID){
            return CONTENT_URI_ONLINE_RADIO.buildUpon()
                    .appendPath(Integer.toString(radioID))
                    .build();
        }

        /***
         * This static method builds an Uri that adds the radio id to the end of the radio content Uri path.
         * This is used to query details about a single recipe entry by id.
         * @param radioID this is the id of the radio in the Database
         * @return the Uri link for a single item
         */
        public static Uri buildRadioFavItemUri(int radioID){
            return CONTENT_URI_ONLINE_RADIO_FAV.buildUpon()
                    .appendPath(Integer.toString(radioID))
                    .build();
        }

        /***
         * This static method builds an Uri that returns all the recipe in the radio database.
         * @return the Uri for all item
         */
        public static Uri buildRadioAll(){
            return CONTENT_URI_ONLINE_RADIO.buildUpon().build();
        }

        /***
         * This static method builds an Uri that returns all the recipe in the radio database.
         * @return the Uri for all item
         */
        public static Uri buildRadioFavAll(){
            return CONTENT_URI_ONLINE_RADIO_FAV.buildUpon().build();
        }
    }
}