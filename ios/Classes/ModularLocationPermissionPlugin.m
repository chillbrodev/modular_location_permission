#import "ModularLocationPermissionPlugin.h"
#if __has_include(<modular_location_permission/modular_location_permission-Swift.h>)
#import <modular_location_permission/modular_location_permission-Swift.h>
#else
// Support project import fallback if the generated compatibility header
// is not copied when this plugin is created as a library.
// https://forums.swift.org/t/swift-static-libraries-dont-copy-generated-objective-c-header/19816
#import "modular_location_permission-Swift.h"
#endif

@implementation ModularLocationPermissionPlugin
+ (void)registerWithRegistrar:(NSObject<FlutterPluginRegistrar>*)registrar {
  [SwiftModularLocationPermissionPlugin registerWithRegistrar:registrar];
}
@end
