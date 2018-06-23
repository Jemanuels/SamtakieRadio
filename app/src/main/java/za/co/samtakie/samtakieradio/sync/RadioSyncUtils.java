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

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.support.annotation.NonNull;

import za.co.samtakie.samtakieradio.provider.Contract;

public class RadioSyncUtils {

    private static boolean sInitialize;

    synchronized public static void initialize(@NonNull final Context context, final Uri uri){
        if(sInitialize){
            return;
        }

        sInitialize = true;

        Thread checkForEmpty = new Thread(new Runnable() {
            @Override
            public void run() {
                //Uri radioQueryUri = uri;

                String[] projectionColumns = {Contract.RadioEntry._ID};

                Cursor cursor = context.getContentResolver().query(
                        uri,
                        projectionColumns,
                        null,
                        null,
                        null);

                if(null == cursor || cursor.getCount() == 0){
                    startImmediateSync(context);
                }

                assert cursor != null;
                cursor.close();
            }
        });

        checkForEmpty.start();
    }


    public static void startImmediateSync(Context context) {
        // start the RadioSyncIntentService
        Intent intentToSyncImmediately = new Intent(context, RadioSyncIntentService.class);
        context.startService(intentToSyncImmediately);
    }
}