package com.openmove.bluetoothscanner;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONObject;
import android.util.Log;
import android.bluetooth.BluetoothDevice;
import com.openmove.bluetoothscanner.BluetoothHelper;
import android.content.Context;
import java.util.List;

public class BluetoothScanner extends CordovaPlugin {
    String TAG = "BluetoothScanner";

    @Override
    public boolean execute(String action, JSONArray data, CallbackContext callbackContext){
        Log.e(TAG, action);

        if (action.equals("startScan")) {
            BluetoothHelper bluetoothHelper = BluetoothHelper.getInstance();

            bluetoothHelper.setOnBluetoothDeviceFound(new OnBluetoothDeviceFound() {
                @Override
                public void onDeviceFound(BluetoothDevice bluetoothDevice) {
                    callbackContext.success(bluetoothDeviceToString(bluetoothDevice));
                }
            });

            bluetoothHelper.setOnBluetoothScan(new OnBluetoothScan() {
                @Override
                public void onStart() {
                    callbackContext.success(String.format("{status: %s}", "start"));
                }
    
                @Override
                public void onEnd() {
                    callbackContext.success(String.format("{status: %s}", "end"));
                }
            });

            boolean isStart = bluetoothHelper.startScan(cordova.getActivity());

            if (!isStart) {
                callbackContext.error("Unable to start scan");
            }
        }

        if (action.equals("stopScan")) {
            BluetoothHelper bluetoothHelper = BluetoothHelper.getInstance();

            bluetoothHelper.setOnBluetoothScan(new OnBluetoothScan() {
                @Override
                public void onStart() {
                    callbackContext.success(String.format("{status: %s}", "start"));
                }
    
                @Override
                public void onEnd() {
                    callbackContext.success(String.format("{status: %s}", "end"));
                }
            });

            bluetoothHelper.stopScan(cordova.getActivity());
        }

        if (action.equals("getFoundedDevices")) {
            BluetoothHelper bluetoothHelper = BluetoothHelper.getInstance();

            List<BluetoothDevice> btDevices = bluetoothHelper.getFoundedDevices();
        
            for (BluetoothDevice foundedDevice : btDevices) {
                callbackContext.success(bluetoothDeviceToString(foundedDevice));
            }

            callbackContext.success();
        }

        return true;
    }

    private String bluetoothDeviceToString(BluetoothDevice btDevice) {
        return String.format("{address: %s}", btDevice.getAddress());
    }
}
