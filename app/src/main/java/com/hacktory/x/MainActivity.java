package com.hacktory.x;

import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.EstimoteSDK;
import com.estimote.sdk.Region;
import com.hacktory.x.receive.MessageFragment;
import com.tt.whorlviewlibrary.WhorlView;

import java.util.Collections;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_ENABLE_BT = 1234;
    private BeaconManager beaconManager;
    private WhorlView progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        setFragment(Constants.FRAGMENT_MAIN);
        setupEstimoteSDK();
    }

    private void setFragment(int selectedFragment){
        Log.d(TAG, "selected fragment: " + selectedFragment);
//        Fragment fragment = null;
        switch (selectedFragment){
            case Constants.FRAGMENT_MAIN:
                MainFragment fragment = new MainFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_placeholder_main, fragment);
                ft.commit();
                break;
            case Constants.FRAGMENT_RECEIVE:
//              MessageFragment frasgment = new MessageFragment();
                break;
            case Constants.FRAGMENT_SEND:
                break;
            default:
//                fragment = new MainFragment();
                break;
        }


    }

    private void setupEstimoteSDK() {
        Log.d(TAG, "setupEstimoteSDK ");
        EstimoteSDK.initialize(this, "estimons-mzy", "e2c71dee0a386b6a548d0cde0754384a");
        beaconManager = new BeaconManager(this);
        beaconManager.setForegroundScanPeriod(300, 0);

    }

    @Override
    protected void onDestroy() {
        beaconManager.disconnect();
        super.onDestroy();
        Log.d(TAG, "onDestroy ");
    }

    @Override
    protected void onStart() {
        super.onStart();
        Log.d(TAG, "onStart ");
        if (!beaconManager.isBluetoothEnabled()) {
            Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
            startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
        } else {
            connectToService();
        }
    }

    public void showProgressBar(final boolean show) {
        Log.d(TAG, "showProgressBar " + show);
        if (progressBar == null)
            return;
        int vis = show ? View.VISIBLE : View.GONE;
        if (show) progressBar.start();
        else progressBar.stop();
        progressBar.setVisibility(vis);
    }

    private void connectToService() {
        Log.d(TAG, "connectToService ");
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                showProgressBar(false);
                Collections.sort(list, Constants.getMostNearbyComparator());
                for (Beacon beacon : list) {
                    Log.d(TAG, "discovered beacon: " + beacon.getRssi()
                            + ", minor:" + beacon.getMinor() + ", major:" + beacon.getMajor());
                }
            }
        });
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(Constants.ALL_ESTIMOTE_BEACONS_REGION);
//                beaconManager.startNearableDiscovery();
            }
        });
    }
}
