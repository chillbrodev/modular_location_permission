
import 'dart:async';

import 'package:flutter/services.dart';

class ModularLocationPermission {
  static const MethodChannel _channel =
      const MethodChannel('modular_location_permission');

  static Future<String> get platformVersion async {
    final String version = await _channel.invokeMethod('getPlatformVersion');
    return version;
  }
}
