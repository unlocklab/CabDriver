package com.example.cabbooking.driver.Fcm;

import android.app.IntentService;
import android.content.Intent;

import com.google.firebase.iid.FirebaseInstanceId;


/**
 * Created by Belal on 4/15/2016.
 */
public class GCMRegistrationIntentService extends IntentService {

    Throwable e;
    public GCMRegistrationIntentService() {
        super("");
    }


    @Override
    protected void onHandleIntent(Intent intent) {
        registerGCM();
    }

    private void registerGCM() {
        FirebaseInstanceId instanceID = FirebaseInstanceId.getInstance();
        String token = null;
        try {
             token = instanceID.getToken();

            System.out.println("tokens-------------rf----------------" + token);

        } catch (Exception e) {
        }

    }
}