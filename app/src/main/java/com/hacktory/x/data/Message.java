package com.hacktory.x.data;

import android.support.annotation.NonNull;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * Pojedyncza wiadomosc
 * Created by paweldylag on 07/11/15.
 */
public class Message implements Comparable {

    @Override
    public int compareTo(@NonNull Object another) {
        if (another instanceof Message) {
            Message m = (Message) another;
            if ( m.getTimestamp() > this.timestamp) return 1;
            if ( m.getTimestamp() == this.timestamp) return 0;
            if ( m.getTimestamp() < this.timestamp) return -1;
            else return 100;
        }
        else throw new ArithmeticException("Not comparable");
    }

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
