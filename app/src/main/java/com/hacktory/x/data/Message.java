package com.hacktory.x.data;

import android.text.format.DateUtils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Pojedyncza wiadomosc
 * Created by paweldylag on 07/11/15.
 */
public class Message {

    private String message;
    private long timestamp;
    private boolean isEnemyDetected;

    public Message(String message, long timestamp, boolean isEnemyDetected) {
        this.message = message;
        this.timestamp = timestamp;
        this.isEnemyDetected = isEnemyDetected;
    }

    public String getMessage() {
        return message;
    }

    public long getTimestamp() {
        return timestamp;
    }


    public String getReadableTimestamp() {
        Date date = new Date(timestamp);
        SimpleDateFormat formater = new SimpleDateFormat("HH:mm", Locale.getDefault());
        return formater.format(date);
    }

    public boolean isEnemyDetected() {
        return isEnemyDetected;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
