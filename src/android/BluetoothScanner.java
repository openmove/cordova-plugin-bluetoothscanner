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
    public boolean execute(String action, JSONArray data, final CallbackContext callbackContext){
        Log.e(TAG, action);

        if (action.equals("startScan")) {
            BluetoothHelper bluetoothHelper = BluetoothHelper.getInstance();

            bluetoothHelper.setOnBluetoothDeviceFound(new OnBluetoothDeviceFound() {
                @Override
                public void onDeviceFound(BluetoothDevice bluetoothDevice) {
                    PluginResult result = new PluginResult(PluginResult.Status.OK, bluetoothDeviceToString(bluetoothDevice));
                    result.setKeepCallback(true);
                    callbackContext.sendPluginResult(result);
                }
            });

            bluetoothHelper.setOnBluetoothScan(new OnBluetoothScan() {
                @Override
                public void onStart() {
                    PluginResult result = new PluginResult(PluginResult.Status.OK, String.format("{status: %s}", "start"));
                    result.setKeepCallback(true);
                    callbackContext.sendPluginResult(result);
                }
    
                @Override
                public void onEnd() {
                    PluginResult result = new PluginResult(PluginResult.Status.OK, String.format("{status: %s}", "end"));
                    result.setKeepCallback(false);
                    callbackContext.sendPluginResult(result);
                }
            });

            boolean isStart = bluetoothHelper.startScan(cordova.getActivity());

            if (!isStart) {
                callbackContext.error("Unable to start scan");
            }
        }

        if (action.equals("stopScan")) {
            BluetoothHelper bluetoothHelper = BluetoothHelper.getInstance();

            bluetoothHelper.stopScan(cordova.getActivity());

            callbackContext.success();
        }

        if (action.equals("getFoundedDevices")) {
            BluetoothHelper bluetoothHelper = BluetoothHelper.getInstance();

            List<BluetoothDevice> btDevices = bluetoothHelper.getFoundedDevices();
        
            for (BluetoothDevice foundedDevice : btDevices) {
                PluginResult result = new PluginResult(PluginResult.Status.OK, bluetoothDeviceToString(foundedDevice));
                result.setKeepCallback(true);
                callbackContext.sendPluginResult(result);
            }

            PluginResult result = new PluginResult(PluginResult.Status.OK, null);
            result.setKeepCallback(false);
            callbackContext.sendPluginResult(result);
        }

        return true;
    }

    private String bluetoothDeviceToString(BluetoothDevice btDevice) {
        return String.format("{address: %s}", btDevice.getAddress());
    }
}
