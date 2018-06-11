package za.co.samtakie.samtakieradio.utilities;

import android.net.Uri;
import android.net.UrlQuerySanitizer;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.Scanner;

import javax.net.ssl.HttpsURLConnection;

/**
 * Created by Jurgen Emanuels on 2017/04/2018
 * This class has the functionality to download the Json data from the web.
 */
public class NetworkUtils {

    private NetworkUtils(){}

    // Get the name of the current class and assign it to the TAG constant
    private static final String TAG = NetworkUtils.class.getSimpleName();

    // Set the url for downloading the data and assign it to constant DYNAMIC_RADIO_URL
    private static final String DYNAMIC_RADIO_URL = "https://www.samtakie.co.za/samtakie_json.php";

    /**
     * Build the url string and return the full parsed url string
     * @return The URL to query the samtakie online radio server
     */
    public static URL buildUrl(){
        Uri builtUri;
        builtUri = Uri.parse(DYNAMIC_RADIO_URL).buildUpon().build();
        URL url = null;

        try{
            url = new URL(builtUri.toString());
        } catch (MalformedURLException e) {
            e.printStackTrace();
        }
        logMessage(TAG, "The url string " + url);
        return url;
    }

    public static String getResponseFromHttpUrl(URL url) throws IOException{
        HttpsURLConnection urlConnection = (HttpsURLConnection) url.openConnection();

        try{
            InputStream in = urlConnection.getInputStream();
            Scanner scanner = new Scanner(in);
            scanner.useDelimiter("\\A");

            boolean hasInput = scanner.hasNext();
            if(hasInput){
                return scanner.next();
            } else {
                return null;
            }
        } finally {
            urlConnection.disconnect();
        }
    }

    /**
     * Helper function for printing data in the Logcat
     * @param classname  - the Class name
     * @param message    - The message to show
     */
    public static void logMessage(String classname, String message){
        Log.d(classname, message);
    }
}
