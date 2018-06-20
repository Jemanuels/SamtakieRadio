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
                Uri radioQueryUri = uri;

                String[] projectionColumns = {Contract.RadioEntry._ID};

                Cursor cursor = context.getContentResolver().query(
                        radioQueryUri,
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


    private static void startImmediateSync(Context context) {
        // start the RadioSyncIntentService
        Intent intentToSyncImmediately = new Intent(context, RadioSyncIntentService.class);
        context.startService(intentToSyncImmediately);
    }
}