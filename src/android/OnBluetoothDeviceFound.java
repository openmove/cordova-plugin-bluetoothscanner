package com.openmove.bluetoothscanner;

import android.bluetooth.BluetoothDevice;

public interface OnBluetoothDeviceFound {
    void onDeviceFound(BluetoothDevice bluetoothDevice);
}
