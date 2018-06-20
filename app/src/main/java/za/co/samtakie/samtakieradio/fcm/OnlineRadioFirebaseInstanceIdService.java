package za.co.samtakie.samtakieradio.fcm;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.google.firebase.messaging.FirebaseMessaging;

public class OnlineRadioFirebaseInstanceIdService extends FirebaseInstanceIdService {

    /**
     * Called if instance ID token is updated. This may occur of the security of
     * the previous token had been compromised. Note that this is called when the Instance ID token
     * is initially generated so this is where you would retrieve the token.
     */
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        // If you want to send messages to this application instance or
        // manage this app subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
        // subscribe to topic susanie
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/susanie");
    }

    /***
     * Persist token to the third-party servers.
     * <p>
     *     Modify this method to associate the user's FCM InstanceID token with any server-side account
     *     maintained by your application.
     * </p>
     * @param token The new Token.
     */
    private void sendRegistrationToServer(String token){
        // This method is blank, but if you were to build a server that stores users token
        // information, this is where you'd send the token to the server.
    }
}