package za.co.samtakie.samtakieradio.provider;

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

    /* Define the possible paths for accessing data in this contract
     * This is the path for the radio news directory. */
    public static final String PATH_ONLINE_RADIO_NEWS = "online_radio_news";

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

        /* RadioEntry content URI = base content URI + path for the radio fav directory */
        public static final Uri CONTENT_URI_ONLINE_RADIO_NEWS = BASE_CONTENT_URI.buildUpon().appendPath(PATH_ONLINE_RADIO_NEWS).build();

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

        /**
         * This part holds the static variable for the online radio news table
         */

        // Table name for the online radio news table
        public static final String TABLE_NAME_NEWS = "online_radio_news";

        // column online radio news title column
        public static final String COLUMN_ONLINE_RADIO_NEWS_TITLE = "online_radio_news_title";

        // column online radio news title column
        public static final String COLUMN_ONLINE_RADIO_NEWS_DATE = "online_radio_news_date";

        // column online radio news message column
        public static final String COLUMN_ONLINE_RADIO_NEWS_MESSAGE = "online_radio_news_message";

        /**
         * End of the online radio static variables
         */

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
         * This static method builds an Uri that returns all the online radio in the radio database.
         * @return the Uri for all item
         */
        public static Uri buildRadioAll(){
            return CONTENT_URI_ONLINE_RADIO.buildUpon().build();
        }

        /***
         * This static method builds an Uri that returns all the fav radio in the radio database.
         * @return the Uri for all item
         */
        public static Uri buildRadioFavAll(){
            return CONTENT_URI_ONLINE_RADIO_FAV.buildUpon().build();
        }

        /***
         * This static method builds an Uri that returns all the radio news in the radio database.
         * @return the Uri for all item
         */
        public static Uri buildRadioNewsAll(){
            return CONTENT_URI_ONLINE_RADIO_NEWS.buildUpon().build();
        }

        /***
         * This static method builds an Uri that adds the radio id to the end of the radio news content Uri path.
         * This is used to query details about a single recipe entry by id.
         * @param id this is the id of the radio in the Database
         * @return the Uri link for a single item
         */
        public static Uri buildRadioNewsItemUri(int id){
            return CONTENT_URI_ONLINE_RADIO_NEWS.buildUpon()
                    .appendPath(Integer.toString(id))
                    .build();
        }
    }
}