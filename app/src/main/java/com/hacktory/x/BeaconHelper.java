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
    private static final int[] ourMinors = new int[]{33961, 53043, 57840, 3104, 62776};

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

    private int currentAchieved = -1;

    public boolean isValidated(@Nullable Validable validator) {
        Log.d(TAG, "isValidated ");
        if (validator == null)
            return false;
        List<Integer> mockedMinors = new ArrayList<>();
        mockedMinors.addAll(beaconSequence);

        for (int j = 0; j < mockedMinors.size(); j++) {
            if (ourMinors[j] == mockedMinors.get(j)) {
                String message = "validation level " + (1 + j) + " achieved";
                Log.d(TAG, message);
                if (validator == null) {
                    Log.e(TAG, "validator is null");
                    return false;
                } else if (currentAchieved < j) {
                    currentAchieved = j;
                    validator.onValidationSuccess(j);
                    return true;
                } else if (mockedMinors.size() < ourMinors.length) {

                    for (int k = 0; k < mockedMinors.size(); k++) {
                        if (ourMinors[k] != mockedMinors.get(k)) {
                            validator.onValidationFailed();
                            return true;
                        }
                    }
                    return true;
                }
            }
        }
        return false;
    }

    public static void clearCurrentSequence() {
        Log.d(TAG, "clearCurrentSequence ");
        INSTANCE.beaconSequence.clear();

    }

    public boolean sequencesNotEqual() {
        int size = beaconSequence.size();
        if (size > 0) {
            for (int j = 0; j < size; j++)
                if (ourMinors[j] != beaconSequence.get(j))
                    return false;
        }
        return true;
    }
}
