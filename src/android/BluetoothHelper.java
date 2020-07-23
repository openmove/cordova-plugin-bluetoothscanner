package com.openmove.bluetoothscanner;

import android.Manifest;
import android.app.Activity;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;

import java.util.ArrayList;
import java.util.List;

public class BluetoothHelper {

    private static BluetoothHelper thisHelper;

    private final BroadcastReceiver receiver = new BroadcastReceiver() {
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {

                BluetoothDevice device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                foundedDevices.add(device);

                if (onBluetoothDeviceFound != null) {
                    onBluetoothDeviceFound.onDeviceFound(device);
                }
            }

            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {
                if (onBluetoothScan != null) {
                    onBluetoothScan.onStart();
                }
            }

            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {
                if (onBluetoothScan != null) {
                    onBluetoothScan.onEnd();
                }
            }
        }
    };

    private List<BluetoothDevice> foundedDevices = new ArrayList<BluetoothDevice>();
    private OnBluetoothDeviceFound onBluetoothDeviceFound;
    private OnBluetoothScan onBluetoothScan;

    private BluetoothHelper() {

    }

    public static BluetoothHelper getInstance() {
        if (thisHelper == null) {
            thisHelper = new BluetoothHelper();
        }

        return thisHelper;
    }

    public boolean startScan(Context context) {
        foundedDevices.clear();

        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        if (mBluetoothAdapter == null) {
            return false;
        }

        if (!mBluetoothAdapter.isEnabled()) {
            return false;
        }

        int permission = context.checkCallingOrSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION);
        if (permission != PackageManager.PERMISSION_GRANTED) {
            return false;
        }

        IntentFilter filter = new IntentFilter();
        filter.addAction(BluetoothDevice.ACTION_FOUND);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        filter.addAction(BluetoothAdapter.ACTION_DISCOVERY_STARTED);

        context.registerReceiver(receiver, filter);

        mBluetoothAdapter.startDiscovery();

        return true;
    }

    public void stopScan(Context context) {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter.cancelDiscovery();

        context.unregisterReceiver(receiver);
    }

    public List<BluetoothDevice> getFoundedDevices() {
        List<BluetoothDevice> out = new ArrayList<BluetoothDevice> (foundedDevices);
        return out;
    }

    public void setOnBluetoothDeviceFound(OnBluetoothDeviceFound onBluetoothDeviceFound) {
        this.onBluetoothDeviceFound = onBluetoothDeviceFound;
    }

    public void setOnBluetoothScan(OnBluetoothScan onBluetoothScan) {
        this.onBluetoothScan = onBluetoothScan;
    }
}
