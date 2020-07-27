package com.openmove.bluetoothscanner;

public class MyBluetoothDevice {
    private String address;
    private int rssi;

    public MyBluetoothDevice(String address) {
        this.setAddress(address);
    }

    public MyBluetoothDevice(String address, int rssi) {
        this(address);
        this.setRssi(rssi);
    }


    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        if (address != null)
            this.address = address;
    }

    public int getRssi() {
        return rssi;
    }

    public void setRssi(int rssi) {
        this.rssi = rssi;
    }
}