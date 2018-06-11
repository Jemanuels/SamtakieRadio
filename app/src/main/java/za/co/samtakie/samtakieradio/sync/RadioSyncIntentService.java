package za.co.samtakie.samtakieradio.sync;

import android.app.IntentService;
import android.content.Intent;
import android.support.annotation.Nullable;

public class RadioSyncIntentService extends IntentService{

    public RadioSyncIntentService(){super("RadioSyncIntentService");}
    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        RadioSyncTask.syncRadio(this);
    }
}
