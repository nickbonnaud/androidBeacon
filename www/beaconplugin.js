// Empty constructor
function BeaconPlugin() {}

// The function that passes work along to native shells
BeaconPlugin.prototype.start = function(uuid, successCallback, errorCallback) {
	var options = {};
	options.uuid = uuid;

	cordova.exec(successCallback, errorCallback, 'BeaconPlugin', 'start', [options]);
}

BeaconPlugin.prototype.stop = function(successCallback, errorCallback) {
	var options = {};
	cordova.exec(successCallback, errorCallback, 'BeaconPlugin', 'stop', [options]);
}

// Installation constructor that binds BeaconPlugin to window
BeaconPlugin.install = function() {
	if (!window.plugins) {
		window.plugins = {};
	}
	
	window.plugins.beaconPlugin = new BeaconPlugin();
	return window.plugins.beaconPlugin;
}

cordova.addConstructor(BeaconPlugin.install);