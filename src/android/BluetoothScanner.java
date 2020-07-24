package com.openmove.bluetoothscanner;

import org.apache.cordova.*;
import org.json.JSONArray;
import org.json.JSONObject;
import org.json.JSONException;
import android.util.Log;
import android.bluetooth.BluetoothDevice;
import com.openmove.bluetoothscanner.BluetoothHelper;
import android.content.Context;
import java.util.List;

public class BluetoothScanner extends CordovaPlugin {
    String TAG = "BluetoothScanner";

    @Override
    public boolean execute(String action, JSONArray data, final CallbackContext callbackContext) {
        Log.e(TAG, action);

        if (action.equals("startScan")) {
            BluetoothHelper bluetoothHelper = BluetoothHelper.getInstance();

            bluetoothHelper.setOnBluetoothDeviceFound(new OnBluetoothDeviceFound() {
                @Override
                public void onDeviceFound(BluetoothDevice bluetoothDevice) {
                    JSONObject resObj = bluetoothDeviceToJson(bluetoothDevice);
                    PluginResult result = new PluginResult(PluginResult.Status.OK, resObj);
                    result.setKeepCallback(true);
                    callbackContext.sendPluginResult(result);
                }
            });

            bluetoothHelper.setOnBluetoothScan(new OnBluetoothScan() {
                @Override
                public void onStart() {
                    JSONObject resObj = stringToJson('{status: "start"}');
                    PluginResult result = new PluginResult(PluginResult.Status.OK, resObj);
                    result.setKeepCallback(true);
                    callbackContext.sendPluginResult(result);
                }
    
                @Override
                public void onEnd() {
                    JSONObject resObj = stringToJson('{status: "end"}');
                    PluginResult result = new PluginResult(PluginResult.Status.OK, resObj);
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
                JSONObject resObj = bluetoothDeviceToJson(foundedDevice);
                PluginResult result = new PluginResult(PluginResult.Status.OK, resObj);
                result.setKeepCallback(true);
                callbackContext.sendPluginResult(result);
            }

            PluginResult out = new PluginResult(PluginResult.Status.OK, "");
            out.setKeepCallback(false);
            callbackContext.sendPluginResult(out);
        }

        return true;
    }

    private JSONObject stringToJson(String obj) {
        JSONObject out = null;
        try {
            out = new JSONObject(obj);
        }catch(JSONException e) {

        }

        return out;
    }

    private JSONObject bluetoothDeviceToJson(BluetoothDevice device) {
        JSONObject out = new JSONObject();
        try {
            out.put("address", device.getAddress());
        }catch(JSONException e) {

        }

        return out;
    }
}
