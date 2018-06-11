package za.co.samtakie.samtakieradio.constants;

import android.app.NotificationManager;

public class Constants {

    public interface ACTION{
        public static String MAIN_ACTION = "za.co.samtakie.samtakieradio.action.MAIN_ACTION";
        public static String START_FOREGROUND_ACTION = "za.co.samtakie.samtakieradio.action.START_FOREGROUND_ACTION";
        public static String STOP_FOREGROUND_ACTION = "za.co.samtakie.samtakieradio.action.STOP_FOREGROUND_ACTION";
        public static final String ACTION_PLAY = "za.co.samtakie.samtakieradio.action.ACTION_PLAY";
        public static final String ACTION_PAUSE = "za.co.samtakie.samtakieradio.action.ACTION_PAUSE";
        public static final String ACTION_REWIND = "za.co.samtakie.samtakieradio.action.ACTION_REWIND";
        public static final String ACTION_FAST_FORWARD = "za.co.samtakie.samtakieradio.action.ACTION_FAST_FORWARD";
        public static final String ACTION_NEXT = "za.co.samtakie.samtakieradio.action.ACTION_NEXT";
        public static final String ACTION_PREVIOUS = "za.co.samtakie.samtakieradio.action.ACTION_PREVIOUS";
        public static final String ACTION_STOP = "za.co.samtakie.samtakieradio.action.ACTION_STOP";
    }

    public interface NOTIFICATION_DATA {
        public static int FOREGROUND_SERVICE = 101;
        public static final int NOTIFICATION_ID = 100;
        public static final String CHANNEL_ID = "samtakie_channel_01";
        public static final CharSequence NAME = "Samtakie";
        public static final int IMPORTANCE = NotificationManager.IMPORTANCE_HIGH;
    }
}