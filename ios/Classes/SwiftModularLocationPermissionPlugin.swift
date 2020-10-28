import Flutter
import UIKit
import CoreLocation

public class SwiftModularLocationPermissionPlugin: NSObject, FlutterPlugin, CLLocationManagerDelegate {
  let locationMgr = CLLocationManager()
  var result: FlutterResult?

  public static func register(with registrar: FlutterPluginRegistrar) {
    let channel = FlutterMethodChannel(name: "ch.upte.modularLocationPermissions", binaryMessenger: registrar.messenger())
    let instance = SwiftModularLocationPermissionPlugin()
    registrar.addMethodCallDelegate(instance, channel: channel)
  }

  public func handle(_ call: FlutterMethodCall, result: @escaping FlutterResult) {
    self.result = result;
    if call.method == "checkLocationPermission" {
        let status = self.getCoreLocationStatus(locationMgr)
        self.reportCLAuthStatusToFlutterResult(status)
    }

    if call.method == "requestLocationPermission" {
        locationMgr.delegate = self

        if let args = call.arguments as? Dictionary<String, Any>,
            let locationType = args["permissionArgs"] as? String {

            let status = getCoreLocationStatus(locationMgr)

            if locationType == "LocationAlways" {
                if status == .authorizedAlways {
                    self.result?("granted")
                } else if status == .denied {
                    self.result?("denied")
                } else {
                    locationMgr.requestAlwaysAuthorization()
                }
             } else if locationType == "LocationWhenInUse" {
                if status == .authorizedWhenInUse {
                    self.result?("granted")
                } else {
                    locationMgr.requestWhenInUseAuthorization()
                }
             }
         } else {
            self.result?(-1)
         }
    }

    if call.method == "openAppSettings" {
        guard let url = URL(string: UIApplication.openSettingsURLString) else {return}
        if #available(iOS 10.0, *) {
          UIApplication.shared.open(url, options: [:], completionHandler: { success in
              log.debug("Open app settings success: \(success)")
          })
        } else {
          // Fallback on earlier versions
          UIApplication.shared.openURL(url)
        }
    }
  }

  public func locationManagerDidChangeAuthorization(_ manager: CLLocationManager) {
        let status = self.getCoreLocationStatus(manager)
        self.reportCLAuthStatusToFlutterResult(status)
  }


  private func getCoreLocationStatus(_ manager: CLLocationManager) -> CLAuthorizationStatus {
    var status = CLAuthorizationStatus.notDetermined
    if #available(iOS 14, *) {
      status = manager.authorizationStatus
    } else {
      status = CLLocationManager.authorizationStatus()
    }
    return status;
  }

  private func reportCLAuthStatusToFlutterResult(_ authStatus: CLAuthorizationStatus) {
        switch authStatus {
            case .notDetermined:
                print("Current Location Status is not determined")
                self.result?("unknown")
            case .denied:
                print("Current Location Status is denied")
                self.result?("denied")
            case .restricted:
                print("Current Location Status is restricted")
                self.result?("restricted")
            case .authorizedAlways:
                print("Current Location Status is auth always")
                self.result?("granted")
            case .authorizedWhenInUse:
                print("Current Location Status is auth when in use")
                self.result?("granted")
            @unknown default:
                print("Current Location Status is unknown")
                self.result?("unknown")
        }
  }
}
