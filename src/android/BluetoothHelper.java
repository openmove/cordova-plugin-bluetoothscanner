package com.openmove.bluetoothscanner;

import android.Manifest;
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

                BluetoothDevice thisDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                int rssi = intent.getShortExtra(BluetoothDevice.EXTRA_RSSI, Short.MIN_VALUE);

                if (thisDevice != null) {
                    MyBluetoothDevice device = new MyBluetoothDevice(thisDevice.getAddress());
                    device.setRssi(rssi);

                    foundedDevices.add(device);

                    if (onBluetoothDeviceFound != null) {
                        onBluetoothDeviceFound.onDeviceFound(device);
                    }
                }
            }

            if (BluetoothAdapter.ACTION_DISCOVERY_STARTED.equals(action)) {

                isScanning = true;

                if (onBluetoothScan != null) {
                    onBluetoothScan.onStart();
                }
            }

            if (BluetoothAdapter.ACTION_DISCOVERY_FINISHED.equals(action)) {

                isScanning = false;

                if (onBluetoothScan != null) {
                    onBluetoothScan.onEnd();
                }
            }
        }
    };

    private List<MyBluetoothDevice> foundedDevices = new ArrayList<MyBluetoothDevice>();
    private OnBluetoothDeviceFound onBluetoothDeviceFound;
    private OnBluetoothScan onBluetoothScan;
    private Boolean isScanning = false;

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

        if (isScanning) {
            forceStopScan(context);
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
        forceStopScan(context);
    }

    public List<MyBluetoothDevice> getFoundedDevices() {
        List<MyBluetoothDevice> out = new ArrayList<MyBluetoothDevice> (foundedDevices);
        return out;
    }

    public void setOnBluetoothDeviceFound(OnBluetoothDeviceFound onBluetoothDeviceFound) {
        this.onBluetoothDeviceFound = onBluetoothDeviceFound;
    }

    public void setOnBluetoothScan(OnBluetoothScan onBluetoothScan) {
        this.onBluetoothScan = onBluetoothScan;
    }

    private void forceStopScan(Context context) {
        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
        mBluetoothAdapter.cancelDiscovery();

        isScanning = false;

        context.unregisterReceiver(receiver);
    }
}
