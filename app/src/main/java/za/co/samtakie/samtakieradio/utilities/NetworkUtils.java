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

import android.net.Uri;
import java.io.IOException;
import java.io.InputStream;
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
}