package com.hacktory.x;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

/**
 * Created by lukasz on 07.11.15.
 * Secret code receiver enables to run from dialing keyboard
 */
public class MySecretCodeReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        if (intent.getAction().equals("android.provider.Telephony.SECRET_CODE")) {
            Intent i = new Intent(context, MainActivity.class);
            i.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(i);
            /**
             * every code is registered in manifest more codes here
             *
             * http://joyofandroid.com/android-secret-codes-hidden-menu-dialler-codes/
             *
             * */
        }
    }

}