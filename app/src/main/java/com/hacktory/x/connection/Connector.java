package com.hacktory.x.connection;

import com.hacktory.x.Constants;
import com.hacktory.x.data.Message;
import com.parse.ParseObject;

/**
 * Created by paweldylag on 07/11/15.
 * Unused connector?
 */
@Deprecated
public class Connector {

    public static void sendMessage(Message message){
        ParseObject parseObject = new ParseObject("Messages");
        parseObject.add("message", message.getMessage());
        parseObject.add("isValid", message.isEnemyDetected());
        parseObject.add("deviceId", Constants.APP_ID);
        parseObject.saveInBackground();
    }

}
