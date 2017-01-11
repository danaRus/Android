package com.example.dana.carmanagement.firebaseutil;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FirebaseInstanceIDServiceImpl extends FirebaseInstanceIdService {

    private static final String TAG = "FirebaseIDServiceImpl";

    /**
     * Called if InstanceID token is updated. This may occur if the security of
     * the previous token had been compromised. This is called when the InstanceID token
     * is initially generated so this is where the token would be retreived.
     */
    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.d(TAG, "Refreshed token: " + refreshedToken);

        // If you want to send messages to this application instance or
        // manage this application's subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(refreshedToken);
    }

    // Persist token to third-party servers
    private void sendRegistrationToServer(String token) {
        // In this method, you could send the token to the application server
    }
}
