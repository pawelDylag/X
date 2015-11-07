package com.hacktory.x;

import android.annotation.TargetApi;
import android.app.FragmentTransaction;
import android.bluetooth.BluetoothAdapter;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.NetworkInfo;
import android.net.wifi.WpsInfo;
import android.net.wifi.p2p.WifiP2pConfig;
import android.net.wifi.p2p.WifiP2pDevice;
import android.net.wifi.p2p.WifiP2pDeviceList;
import android.net.wifi.p2p.WifiP2pInfo;
import android.net.wifi.p2p.WifiP2pManager;
import android.os.Build;
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
import com.estimote.sdk.Utils;
import com.hacktory.x.receive.ReceiveFragment;
import com.hacktory.x.send.SendFragment;
import com.tt.whorlviewlibrary.WhorlView;

import java.io.IOException;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Scanner;

import butterknife.Bind;
import butterknife.ButterKnife;


public class MainActivity extends AppCompatActivity {

    public static final String TAG = MainActivity.class.getSimpleName();
    private static int SELECTED_FRAGMENT = 0;
    private static final int REQUEST_ENABLE_BT = 1234;
    private BeaconManager beaconManager;
    private List<Beacon> filteredSortedList = new ArrayList<>();

    private static MainFragment fragmentReference = null;

    @Bind(R.id.progressBarRanging)
    public WhorlView progressBar;
    @Bind(R.id.parent)
    public RelativeLayout parent;

    private IntentFilter intentFilter = new IntentFilter();
    private BroadcastReceiver broadcastReceiver;

    private static final String GALAXY_NAME = "Galaxy S5";
    private boolean isP2pConnected = false;

    private static final int PORT = 1030;

    WifiP2pManager.Channel p2pChannel;
    WifiP2pManager p2pManager;
    WifiP2pManager.ConnectionInfoListener connectionInfoListener;

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
        initPeerListener();
        initConnectionInfoListener();
        showProgressBar(true, "Setup beacon ranging...");
    }


    public void setFragment(int selectedFragment) {
        Log.d(TAG, "selected fragment: " + selectedFragment);
//        Fragment fragment = null;
        switch (selectedFragment) {
            case Constants.FRAGMENT_MAIN:
                SELECTED_FRAGMENT = Constants.FRAGMENT_MAIN;
                MainFragment fragment = MainFragment.newInstance();
                fragmentReference = fragment;
                FragmentTransaction ft = getFragmentManager().beginTransaction();
                ft.replace(R.id.fragment_placeholder_main, fragment);
                ft.commit();
                break;
            case Constants.FRAGMENT_RECEIVE:
                fragmentReference = null;
                SELECTED_FRAGMENT = Constants.FRAGMENT_RECEIVE;
                ReceiveFragment fragmentR = ReceiveFragment.newInstance();
                FragmentTransaction ftR = getFragmentManager().beginTransaction();
                ftR.replace(R.id.fragment_placeholder_main, fragmentR);
                ftR.commit();
                break;
            case Constants.FRAGMENT_SEND:
                fragmentReference = null;
                SELECTED_FRAGMENT = Constants.FRAGMENT_SEND;
                SendFragment fragmentS = SendFragment.newInstance();
                FragmentTransaction ftS = getFragmentManager().beginTransaction();
                ftS.replace(R.id.fragment_placeholder_main, fragmentS);
                ftS.commit();
                break;
            default:
                fragmentReference = null;
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

                if (peerList.getDeviceList().size() > 0) {

                    for (WifiP2pDevice nextDevice : peerList.getDeviceList()) {

                        if (nextDevice.deviceName.equals(GALAXY_NAME)) {
                            if (!isP2pConnected) {
                                connectToP2PWifiDevice(nextDevice);
                            }
                        } else {
                            isP2pConnected = false;
                        }

                        Log.d(TAG, "Wifi_p2p scanned devices - device name: " + nextDevice.deviceName);
                        Log.d(TAG, "Wifi_p2p scanned devices - device address: " + nextDevice.deviceAddress);

                    }
                }
            }
        };
    }

    private void initConnectionInfoListener() {
        Log.d(TAG, "initConnectionInfoListener() called with: " + "");

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            connectionInfoListener = new WifiP2pManager.ConnectionInfoListener() {
                @TargetApi(Build.VERSION_CODES.KITKAT)
                @Override
                public void onConnectionInfoAvailable(final WifiP2pInfo info) {
                    Log.d(TAG, "onConnectionInfoAvailable");

                    // InetAddress from WifiP2pInfo struct.
                    String groupOwnerAddress = info.groupOwnerAddress.getHostAddress();

                    // After the group negotiation, we can determine the group owner.
                    if (info.groupFormed && info.isGroupOwner) {
                        Log.d(TAG, "I'm the group owner !");
                        try {

                            Thread thread = new Thread(new Runnable() {
                                @Override
                                public void run() {

                                    ServerSocket serverSocket = null;
                                    try {
                                        serverSocket = new ServerSocket(PORT);
                                    } catch (IOException e) {
                                        Log.e(TAG, "Server error: " + e.getMessage());
                                        e.printStackTrace();
                                    }

                                    Socket sck = null;

                                    while (true) {

                                        try {
                                            sck = serverSocket.accept();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                            Log.e(TAG, "Server error: " + e.getMessage());
                                        }
                                        try (Scanner input = new Scanner(sck.getInputStream())) {

                                            while (input.hasNextLine()) {
                                                Log.d((TAG), "Server input: " + input.nextLine());
                                            }

                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                        try {
                                            serverSocket.close();
                                        } catch (IOException e) {
                                            e.printStackTrace();
                                        }
                                    }
                                }
                            });

                            thread.start();

                            // Do whatever tasks are specific to the group owner.
                            // One common case is creating a server thread and accepting
                            // incoming connections.
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (info.groupFormed) {
                        Log.d(TAG, "I'm the client !");

                        Thread clientThread = new Thread(new Runnable() {
                            @Override
                            public void run() {
                                try {
                                    Log.d(TAG, "I'm sending !");
                                    Thread.sleep(3000);
                                    Socket socket = new Socket(info.groupOwnerAddress, PORT);
                                    OutputStream outputStream = socket.getOutputStream();
                                    outputStream.write(Byte.valueOf("Send a message to server !"));
                                    outputStream.close();

                                } catch (IOException e) {
                                    e.printStackTrace();
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            }
                        });

                        clientThread.start();
                    }
                }
            };
        }
    }

    private void connectToP2PWifiDevice(final WifiP2pDevice device) {
        Log.d(TAG, "connectToP2PWifiDevice() called with: " + "device = [" + device + "]");

        WifiP2pConfig config = new WifiP2pConfig();
        config.deviceAddress = device.deviceAddress;
        config.wps.setup = WpsInfo.PBC;

        p2pManager.connect(p2pChannel, config, new WifiP2pManager.ActionListener() {
            @Override
            public void onSuccess() {
                Log.d(TAG, "Connected to a: " + device.deviceName);
                isP2pConnected = true;
            }

            @Override
            public void onFailure(int i) {
                Log.d(TAG, "Connection failured !");
            }
        });
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

                    if (p2pManager != null) {
                        p2pManager.requestPeers(p2pChannel, peerListListener);
                    }


                } else if (WifiP2pManager.WIFI_P2P_CONNECTION_CHANGED_ACTION.equals(action)) {
                    Log.d(TAG, "Wifi_P2P_connection_changed !");

                    if (p2pManager == null) {
                        return;
                    }

                    NetworkInfo networkInfo = (NetworkInfo) intent
                            .getParcelableExtra(WifiP2pManager.EXTRA_NETWORK_INFO);

                    if (networkInfo.isConnected()) {
                        Log.d(TAG, "Network info(): connected !");
                        p2pManager.requestConnectionInfo(p2pChannel, connectionInfoListener);
                    } else {
                        Log.d(TAG, "Network info(): disconnected !");
                    }

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

    @Override
    public void onBackPressed() {

        if (SELECTED_FRAGMENT != Constants.FRAGMENT_MAIN) {
            setFragment(Constants.FRAGMENT_MAIN);
        } else {
            super.onBackPressed();
        }

    }

    public void showProgressBar(final boolean show) {
        showProgressBar(show, null);
    }

    public void showProgressBar(final boolean show, @Nullable String message) {
        Log.d(TAG, "showProgressBar " + show);
        if (progressBar == null)
            return;
        if (show) {
            progressBar.start();
            progressBar.setVisibility(View.VISIBLE);
        } else {
            progressBar.stop();
            progressBar.setVisibility(View.GONE);
        }
        if (message != null)
            Snackbar.make(parent, message, Snackbar.LENGTH_SHORT).show();

        parent.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                return show;
            }
        });
    }

    private void connectToService() {
        Log.d(TAG, "connectToService ");
        beaconManager.setMonitoringListener(new BeaconManager.MonitoringListener() {
            @Override
            public void onEnteredRegion(Region region, List<Beacon> list) {

            }

            @Override
            public void onExitedRegion(Region region) {

            }
        });
        beaconManager.setRangingListener(new BeaconManager.RangingListener() {
            @Override
            public void onBeaconsDiscovered(Region region, List<Beacon> list) {

                if (list == null || list.size() == 0)
                    return;
                if (BeaconHelper.firstScan) {
                    BeaconHelper.firstScan = false;
                    showProgressBar(false, "Beacon scan started!");
                }

                filteredSortedList.clear();
                filteredSortedList.addAll(list);
                Collections.sort(filteredSortedList, BeaconHelper.getMostNearbyComparator());
                for (Beacon beacon : filteredSortedList) {
                    Log.d(TAG, "discovered beacon: " + beacon.getRssi()
                            + ", minor:" + beacon.getMinor() + ", major:"
                            + beacon.getMajor() + "," + Utils.computeProximity(beacon).name());
                }
                Beacon mostPowerful = filteredSortedList.get(0);
                if (Utils.computeProximity(mostPowerful).compareTo(Utils.Proximity.IMMEDIATE) == 0)
                    BeaconHelper.INSTANCE.insertNewCheckPoint(mostPowerful);

                BeaconHelper.INSTANCE.printCurrentSequence();
                BeaconHelper.INSTANCE.printTargetSequence();
                if (BeaconHelper.INSTANCE.sequencesNotEqual(fragmentReference)) {
                    if (fragmentReference != null && BeaconHelper.INSTANCE.getBeaconSequence().size() > 0)
                        fragmentReference.onValidationFailed();
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
