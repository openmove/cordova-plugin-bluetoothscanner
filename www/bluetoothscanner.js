module.exports = {
    startScan: function (successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "BluetoothScanner", "startScan", []);
    },
    stopScan: function (successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "BluetoothScanner", "stopScan", []);
    },
    getFoundedDevices: function (successCallback, errorCallback) {
        cordova.exec(successCallback, errorCallback, "BluetoothScanner", "getFoundedDevices", []);
    }
};
