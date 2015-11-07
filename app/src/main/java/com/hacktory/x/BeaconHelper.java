package com.hacktory.x;

import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.Region;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 * Created by lukasz on 07.11.15.
 */
public class BeaconHelper {
    public static final BeaconHelper INSTANCE = new BeaconHelper();
    public static final String TAG = BeaconHelper.class.getSimpleName();
    public static final Region OUR_BEACONS_REGION = new Region("rid", null, 54321, null);
    public static boolean firstScan = true;
    private static final int[] ourMinors = new int[]{33961, 53043, 33768, 57840};

    private static Comparator<? super Beacon> mostNearbyComparator = new Comparator<Beacon>() {
        @Override
        public int compare(Beacon lhs, Beacon rhs) {
            return lhs.getRssi() > rhs.getRssi() ? -1 : 1;
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

    private BeaconHelper() {
    }

    private Beacon lastDiscoveredBeacon = null;
    private Set<Integer> beaconSequence = new HashSet<>();

    public Beacon getLastDiscoveredBeacon() {
        return lastDiscoveredBeacon;
    }

    public void setLastDiscoveredBeacon(Beacon lastDiscoveredBeacon) {
        this.lastDiscoveredBeacon = lastDiscoveredBeacon;
    }

    public void insertNewCheckPoint(Beacon beacon) {
        beaconSequence.add(beacon.getMinor());
    }

    public void printCurrentSequence() {
        String sequence = "";
        for (Integer integer : beaconSequence) {
            sequence += integer + ",";
        }
        Log.d(TAG, "current sequence: " + sequence);
    }

    public void printTargetSequence() {
        String sequence = "";
        for (Integer integer : ourMinors) {
            sequence += integer + ",";
        }
        Log.d(TAG, "target sequence: " + sequence);
    }

    public boolean isValidatingFinished() {
        Log.d(TAG, "isValidatingFinished ");
        if (ourMinors.length != beaconSequence.size())
            return false;
        List<Integer> mockedMinors = new ArrayList<>();

        mockedMinors.addAll(beaconSequence);

        mockedMinors.addAll(beaconSequence);
        for (int j = 0; j < ourMinors.length; j++) {
            int schemaMinor = mockedMinors.get(j);
            if (ourMinors[j] == schemaMinor) {
                Log.d(TAG, "validation level " + (1 + j) + " achievved");
            } else {
                return false;
            }
        }
        return true;
    }
}
