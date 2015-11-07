package com.hacktory.x;

import android.support.annotation.Nullable;
import android.util.Log;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.Region;
import com.hacktory.x.interfaces.Validable;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

/**
 * Created by lukasz on 07.11.15.
 */
public class BeaconHelper {
    public static final BeaconHelper INSTANCE = new BeaconHelper();
    public static final String TAG = BeaconHelper.class.getSimpleName();
    public static final Region OUR_BEACONS_REGION = new Region("rid", null, 54321, null);
    public static boolean firstScan = true;
    /**
     * koleność trasy Beacona: Arek, Łukasz, Magda, Patryk
     */
    private static final int[] ourMinors = new int[]{33961, 53043,/* 33768,*/ 57840};

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
    private List<Integer> beaconSequence = new ArrayList<>();

    public Beacon getLastDiscoveredBeacon() {
        return lastDiscoveredBeacon;
    }

    public void setLastDiscoveredBeacon(Beacon lastDiscoveredBeacon) {
        this.lastDiscoveredBeacon = lastDiscoveredBeacon;
    }

    public void insertNewCheckPoint(Beacon beacon) {
        Log.d(TAG, "insertNewCheckPoint ");
        if (beaconSequence.size() > 0)
            for (Integer integer : beaconSequence)
                if (integer == beacon.getMinor())
                    return;
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

    public boolean isValidatingFinished(@Nullable Validable validator) {
        Log.d(TAG, "isValidatingFinished ");
        if (ourMinors.length != beaconSequence.size())
            return false;
        List<Integer> mockedMinors = new ArrayList<>();

        mockedMinors.addAll(beaconSequence);
        for (int j = 0; j < ourMinors.length; j++) {
            int schemaMinor = mockedMinors.get(j);
            if (ourMinors[j] == schemaMinor) {
                String message = "validation level " + (1 + j) + " achieved";
                Log.d(TAG, message);
                if (validator != null)
                    validator.onValidationSuccess(j);
            } else {
                if (ourMinors.length == beaconSequence.size())
                    if (validator != null)
                        validator.onValidationFailed();
                return false;
            }
        }
        return true;
    }

    public static void clearCurrentSequence() {
        Log.d(TAG, "clearCurrentSequence ");
        INSTANCE.beaconSequence.clear();
    }
}
