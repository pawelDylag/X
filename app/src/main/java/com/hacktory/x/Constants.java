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

    public static final Region ALL_ESTIMOTE_BEACONS_REGION = new Region("rid", null, null, null);
    public static final Region CYAN_ESTIMOTE_REGION = new Region("rid", null, 22507, 21026);
    private static Comparator<? super Beacon> mostNearbyComparator = new Comparator<Beacon>() {
        @Override
        public int compare(Beacon lhs, Beacon rhs) {
            return lhs.getRssi() < rhs.getRssi() ? -1 : 1;
        }
    };

    //// TODO: 07.11.15 fill with proper values!!!
    public static List<Region> getSchema() {
        List<Region> list = new ArrayList<>();
        Integer major1 = 22507;
        Integer minor1 = 21026;
        list.add(new Region("rid", null, major1, minor1));
        Integer major2 = 22507;
        Integer minor2 = 21026;
        list.add(new Region("rid", null, major2, minor2));
        Integer major3 = 22507;
        Integer minor3 = 21026;
        list.add(new Region("rid", null, major3, minor3));
        Integer major4 = 22507;
        Integer minor4 = 21026;
        list.add(new Region("rid", null, major4, minor4));
        Integer major5 = 22507;
        Integer minor5 = 21026;
        list.add(new Region("rid", null, major5, minor5));

        return list;
    }

    public static Comparator<? super Beacon> getMostNearbyComparator() {
        return mostNearbyComparator;
    }
}
