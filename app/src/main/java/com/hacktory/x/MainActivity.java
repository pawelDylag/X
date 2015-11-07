package com.hacktory.x;

import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.RelativeLayout;

import com.estimote.sdk.Beacon;
import com.estimote.sdk.BeaconManager;
import com.estimote.sdk.EstimoteSDK;
import com.estimote.sdk.Region;
import com.hacktory.x.receive.ReceiveFragment;
import com.tt.whorlviewlibrary.WhorlView;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    private static final int REQUEST_ENABLE_BT = 1234;
    private BeaconManager beaconManager;
    private List<Beacon> filteredSortedList = new ArrayList<>();

    @Bind(R.id.progressBarRanging)
    public WhorlView progressBar;
    @Bind(R.id.parent)
    public RelativeLayout parent;

    private IntentFilter intentFilter = new IntentFilter();
    private BroadcastReceiver broadcastReceiver;

    WifiP2pManager.Channel p2pChannel;
    WifiP2pManager p2pManager;

    private WifiP2pManager.PeerListListener peerListListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        setFragment(Constants.FRAGMENT_MAIN);
        setupEstimoteSDK();
        initIntentFilters();
        initP2PChannel();
        initBroadcastReceiver();
        showProgressBar(true, "Setup beacon ranging...", "Ranging activated!");
    }


    public void setFragment(int selectedFragment) {
        Log.d(TAG, "selected fragment: " + selectedFragment);
//        Fragment fragment = null;
        switch (selectedFragment) {
            case Constants.FRAGMENT_MAIN:
                MainFragment fragment = new MainFragment();
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_placeholder_main, fragment);
                ft.commit();
                break;
            case Constants.FRAGMENT_RECEIVE:
                ReceiveFragment fragmentM = new ReceiveFragment();
                FragmentTransaction ftM = getFragmentManager().beginTransaction();
                ftM.replace(R.id.fragment_placeholder_main, fragmentM);
                ftM.commit();
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
        beaconManager.setForegroundScanPeriod(500, 1000);
    }

    private void initIntentFilters() {
        Log.d(TAG, "initIntentFilters() called with: " + "");

        intentFilter.addAction(WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION);
        intentFilter.addAction(WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION);
    }

    @Override
    protected void onDestroy() {
        beaconManager.disconnect();
        super.onDestroy();
        Log.d(TAG, "onDestroy ");
    }

    private void initP2PChannel() {
        Log.d(TAG, "initP2PChannel() called with: " + "");

        p2pManager = (WifiP2pManager) getSystemService(Context.WIFI_P2P_SERVICE);
        p2pChannel = p2pManager.initialize(this, getMainLooper(), null);

        p2pManager.discoverPeers(p2pChannel, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "discoverPeers(): onSuccess");
                initPeerListener();
            }

            @Override
            public void onFailure(int i) {
                Log.d(TAG, "discoverPeers(): onFailure");
            }
        });
    }

    private void initPeerListener() {
        Log.d(TAG, "initPeerListener() called with: " + "");

        peerListListener = new WifiP2pManager.PeerListListener() {
            @Override
            public void onPeersAvailable(WifiP2pDeviceList peerList) {
                Log.d(TAG, "Peers available: " + peerList.getDeviceList().size());

            }
        };
    }

    private void initBroadcastReceiver() {
        Log.d(TAG, "initBroadcastReceiver() called with: " + "");

        broadcastReceiver = new BroadcastReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {

                String action = intent.getAction();

                if (WifiP2pManager.WIFI_P2P_STATE_CHANGED_ACTION.equals(action)) {
                    // Determine if Wifi P2P mode is enabled or not, alert
                    // the Activity.
                    int state = intent.getIntExtra(WifiP2pManager.EXTRA_WIFI_STATE, -1);
                    if (state == WifiP2pManager.WIFI_P2P_STATE_ENABLED) {
                        Log.d(TAG, "Wifi_P2P_enabled !");
                    } else {
                        Log.d(TAG, "Wifi_P2P_disabled !");
                    }
                } else if (WifiP2pManager.WIFI_P2P_PEERS_CHANGED_ACTION.equals(action)) {
                    Log.d(TAG, "Wifi_P2P peers changed !");
                    // The peer list has changed!  We should probably do something about
                    // that.


                } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
                    Log.d(TAG, "Wifi_P2P_connection_changed !");

                } else if (WifiP2pManager.WIFI_P2P_THIS_DEVICE_CHANGED_ACTION.equals(action)) {
                    Log.d(TAG, "Wifi_P2P_this_device_changed !");
                }
            }
        };
    }

    private void registerReceiver() {
        Log.d(TAG, "registerReceiver() called with: " + "");

        registerReceiver(broadcastReceiver, intentFilter);
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

    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver();
    }

    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(broadcastReceiver);
    }

    public void showProgressBar(final boolean show) {
        showProgressBar(show, null, null);
    }

    public void showProgressBar(final boolean show, @Nullable String showMessage, @Nullable String hideMessage) {
        Log.d(TAG, "showProgressBar " + show);
        if (progressBar == null)
            return;
        if (show) {
            progressBar.start();
            progressBar.setVisibility(View.VISIBLE);
            if (showMessage != null)
                Snackbar.make(parent, showMessage, Snackbar.LENGTH_SHORT).show();
        } else {
            progressBar.stop();
            progressBar.setVisibility(View.GONE);
            if (hideMessage != null)
                Snackbar.make(parent, hideMessage, Snackbar.LENGTH_SHORT).show();
        }
        parent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return show;
            }
        });
    }

    private void connectToService() {
        Log.d(TAG, "connectToService ");
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {
                showProgressBar(false);
                filteredSortedList.clear();
                filteredSortedList.addAll(list);
                Collections.sort(filteredSortedList, BeaconHelper.getMostNearbyComparator());
                for (Beacon beacon : filteredSortedList) {
                    Log.d(TAG, "discovered beacon: " + beacon.getRssi()
                            + ", minor:" + beacon.getMinor() + ", major:" + beacon.getMajor());
                }
                if (BeaconHelper.INSTANCE.isValidatingFinished()) {
                    Log.i(TAG, "sequence valid!!!");
                } else {
                    Log.i(TAG, "sequence invalid!!!");
                }
            }
        });
        beaconManager.connect(new BeaconManager.ServiceReadyCallback() {
            @Override
            public void onServiceReady() {
                beaconManager.startRanging(BeaconHelper.OUR_BEACONS_REGION);
//                beaconManager.startNearableDiscovery();
            }
        });
    }
}
