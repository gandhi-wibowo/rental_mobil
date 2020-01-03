package com.example.gandhi.rental;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

/**
 * Created by gandhi on 12/4/16.
 */

public class MyFirebaseInstanceIdService extends FirebaseInstanceIdService {
    private static final String REG_TOKEN = "REG_TOKEN";

    @Override
    public void onTokenRefresh() {
        String recent_token = FirebaseInstanceId.getInstance().getToken();
        CreateShared("Data_token","Key_token",recent_token);
        Log.d(REG_TOKEN,recent_token);
    }

    private void CreateShared(String PrefName, String KeyName, String Value){
        SharedPreferences sharedpreferences = getSharedPreferences(PrefName, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString(KeyName,Value);
        editor.commit();
    }
}
