package com.hacktory.x;

import android.app.Application;

import com.parse.Parse;
import com.parse.ParseInstallation;

/**
 * Created by paweldylag on 07/11/15.
 */
public class App extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        Parse.enableLocalDatastore(this);
        Parse.initialize(this, "7B9e7gJ5HcKYQqX6MQdELo6gN2VBo4u7RXiE4KAO", "9LYM2aJhzOgPFxoueAuOCkAf6VaxhsMBxGFRYBjT");
    }
}
