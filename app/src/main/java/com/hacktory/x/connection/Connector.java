package com.hacktory.x.connection;

import android.util.Log;

import com.hacktory.x.Constants;
import com.hacktory.x.data.Message;
import com.parse.FindCallback;
import com.parse.ParseInstallation;
import com.parse.ParseObject;
import com.parse.ParseQuery;
import com.parse.ParseUser;

import java.text.ParseException;
import java.util.Collections;
import java.util.List;

/**
 * Created by paweldylag on 07/11/15.
 */
public class Connector {

    public static void sendMessage(Message message){
        ParseObject parseObject = new ParseObject("Messages");
        parseObject.add("message", message.getMessage());
        parseObject.add("isValid", message.isEnemyDetected());
        parseObject.add("deviceId", Constants.APP_ID);
        parseObject.saveInBackground();
    }

}
