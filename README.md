# modular_location_permission

Location permission plugin to be used with the [Modular App Permissions plugin](https://github.com/uptech/modular_app_permissions).

## Plugin Information

This plugin is not intended to be used on its own. It is designed to be used in the [Modular App Permissions
plugin](https://github.com/uptech/modular_app_permissions). Using this plugin by itself will work
but the developer is responsible for building the Dart interface.  

## Permission Definition

*Android*: This plugin automatically includes the ` <uses-permission android:name="android.permission.ACCESS_FINE_LOCATION"/>`.
*iOS*: You _MUST_ include the proper NSUSAGE key in your `info.plist`. Failure to do so will result in 
improper use of this library. 
- [NSLocationAlwaysUsageDescription](https://developer.apple.com/documentation/bundleresources/information_property_list/nslocationalwaysusagedescription)
- [NSLocationWhenInUseUsageDescription](https://developer.apple.com/documentation/bundleresources/information_property_list/nslocationwheninuseusagedescription)
- [NSLocationAlwaysAndWhenInUseUsageDescription](https://developer.apple.com/documentation/bundleresources/information_property_list/nslocationalwaysandwheninuseusagedescription)

