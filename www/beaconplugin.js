// Empty constructor
function BeaconPlugin() {}

// The function that passes work along to native shells
BeaconPlugin.prototype.init = function(uuid, successCallback, errorCallback) {
	var options = {};
	options.uuid = uuid;

	cordova.exec(successCallback, errorCallback, 'BeaconPlugin', 'init', [options]);
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