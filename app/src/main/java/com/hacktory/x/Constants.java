package com.hacktory.x;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.Region;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by lukasz on 07.11.15.
 */
public class Constants {

    public static final int FRAGMENT_MAIN = 0;
    public static final int FRAGMENT_RECEIVE = 1;
    public static final int FRAGMENT_SEND = 2;

    public static final Region ALL_ESTIMOTE_BEACONS_REGION = new Region("rid", null, null, null);
    public static final Region CYAN_ESTIMOTE_REGION = new Region("rid", null, 54321, null);
    private static Comparator<? super Beacon> mostNearbyComparator = new Comparator<Beacon>() {
        @Override
        public int compare(Beacon lhs, Beacon rhs) {
            return lhs.getRssi() < rhs.getRssi() ? -1 : 1;
        }
    };

    //// TODO: 07.11.15 fill with proper values!!!
    public static List<Region> getSchema() {
        List<Region> list = new ArrayList<>();
        return list;
    }

    public static Comparator<? super Beacon> getMostNearbyComparator() {
        return mostNearbyComparator;
    }


}
