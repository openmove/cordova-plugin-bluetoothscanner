module.exports = {
    startScan: function (successCallback, errorCallback, packageName) {
        cordova.exec(successCallback, errorCallback, "BluetoothScanner", "startScan", [packageName]);
    },
    stopScan: function (successCallback, errorCallback, packageName) {
        cordova.exec(successCallback, errorCallback, "BluetoothScanner", "stopScan", [packageName]);
    },
    getFoundedDevices: function (successCallback, errorCallback, packageName) {
        cordova.exec(successCallback, errorCallback, "BluetoothScanner", "getFoundedDevices", [packageName]);
    }
};
