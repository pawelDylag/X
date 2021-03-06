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

    private String nameOf(int minor) {
        switch (minor) {
            case 33961:
                return "Arek";
            case 53043:
                return "Łukasz";
            case 57840:
                return "Patryk";
            case 3104:
                return "Magda";
            case 62776:
                return "Paweł";
            default:
                return "Unknown";
        }
    }

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

    public List<Integer> getBeaconSequence() {
        return beaconSequence;
    }

    public void setBeaconSequence(List<Integer> beaconSequence) {
        this.beaconSequence = beaconSequence;
    }

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
            sequence += nameOf(integer) + ",";
        }
        Log.d(TAG, "current sequence: " + sequence);
    }

    public void printTargetSequence() {
        String sequence = "";
        for (Integer integer : ourMinors) {
            sequence += nameOf(integer) + ",";
        }
        Log.d(TAG, "current sequence: " + sequence);
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
                if (currentAchieved < j) {
                    currentAchieved = j;
                    validator.onValidationSuccess(j);
                    return true;
                } else if (mockedMinors.size() < ourMinors.length) {

                    for (int k = 0; k < mockedMinors.size(); k++) {
                        if (ourMinors[k] != mockedMinors.get(k)) {
                            Log.d(TAG, "isValidated : ourMinor: " + nameOf(ourMinors[k]));
                            Log.d(TAG, "isValidated : current: " + nameOf(mockedMinors.get(k)));
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

    public boolean sequencesNotEqual(Validable validator) {
        int size = beaconSequence.size();
        if (size > 0) {
            for (int j = 0; j < size; j++) {
                if (ourMinors[j] != beaconSequence.get(j)) {
                    return true;
                } else {
                    if (validator != null)
                        validator.onValidationSuccess(j);
                }
            }
            return false; //all OK, succesfully unlocking..
        } else return false; //first beacon
    }
}
