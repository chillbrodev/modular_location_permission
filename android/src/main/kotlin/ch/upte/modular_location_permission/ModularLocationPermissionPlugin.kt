// DO NOT EDIT THIS FILE. GENERATED VIA KOTLIN POET
package ch.upte.modular_location_permission
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import io.flutter.embedding.engine.plugins.FlutterPlugin
import io.flutter.embedding.engine.plugins.activity.ActivityAware
import io.flutter.embedding.engine.plugins.activity.ActivityPluginBinding
import io.flutter.plugin.common.MethodCall
import io.flutter.plugin.common.MethodChannel
import io.flutter.plugin.common.MethodChannel.MethodCallHandler
import io.flutter.plugin.common.PluginRegistry.RequestPermissionsResultListener
import kotlin.Array
import kotlin.Boolean
import kotlin.Int
import kotlin.IntArray
import kotlin.String
import android.util.Log
import io.flutter.plugin.common.PluginRegistry

class ModularLocationPermissionPlugin : FlutterPlugin, MethodCallHandler, ActivityAware,
        RequestPermissionsResultListener {
  private var channel: MethodChannel? = null
  private var activity: Activity? = null
  private var result: MethodChannel.Result? = null
  private val permissionCode: Int = 121
  override fun onAttachedToEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    channel = MethodChannel(binding.binaryMessenger, "ch.upte.modularLocationPermissions")
    channel?.setMethodCallHandler(this)
  }
  override fun onMethodCall(call: MethodCall, result: MethodChannel.Result) {
    this.result = result
    when (call.method) {
      "checkLocationPermission" -> {
        val status = checkPermissionStatus("android.permission.ACCESS_FINE_LOCATION")
        if(status){
          result.success("granted")
        } else {
          result.success("denied")
        }
      }
      "requestLocationPermission" -> {
        activity?.let { validActivity ->
          requestPermission("android.permission.ACCESS_FINE_LOCATION", validActivity,
                  result)
        } ?: kotlin.run {
          result.error("No-Activity", "Not attached to an activity", null)
        }
      }
      "openAppSettings" -> {
        activity?.let { validActivity ->
          validActivity.startActivity(android.content.Intent().apply {
            action = android.provider.Settings.ACTION_APPLICATION_DETAILS_SETTINGS
            data = android.net.Uri.fromParts("package", validActivity.packageName, null)
          })
        }
      }
    }
  }
  override fun onDetachedFromEngine(binding: FlutterPlugin.FlutterPluginBinding) {
    channel?.setMethodCallHandler(null)
  }
  override fun onDetachedFromActivity() {
    activity = null
  }
  override fun onReattachedToActivityForConfigChanges(binding: ActivityPluginBinding) {
    activity = binding.activity
    binding.addRequestPermissionsResultListener(this)
  }
  override fun onAttachedToActivity(binding: ActivityPluginBinding) {
    activity = binding.activity
    binding.addRequestPermissionsResultListener(this)
  }
  override fun onDetachedFromActivityForConfigChanges() {
    activity = null
  }
  override fun onRequestPermissionsResult(
          requestCode: Int,
          permissions: Array<String>,
          grantResults: IntArray
  ): Boolean {
    handlePermissionsRequest(permissions, grantResults)
    return true
  }
  private fun requestPermission(
          permission: String,
          activity: Activity,
          result: MethodChannel.Result
  ) {
    val prefs = activity.getSharedPreferences("ch.upte.modular.permissions", Context.MODE_PRIVATE)
    val asked = prefs.getBoolean("P-$permission-asked", false)
    val showRationale = ActivityCompat.shouldShowRequestPermissionRationale(activity, permission)
    val permissionStatusGranted = checkPermissionStatus(permission)
    if(permissionStatusGranted) {
      result.success("granted")
      return
    }
    if(!showRationale) {
      prefs.edit().putBoolean("P-$permission-asked", true).apply()
    }
    if(asked && !showRationale) {
      result.success("restricted")
    } else {
      ActivityCompat.requestPermissions(activity, arrayOf(permission), permissionCode)
    }
  }
  private fun checkPermissionStatus(permission: String): Boolean {
    if(activity == null) {
      return false
    }
    else {
      val granted = PackageManager.PERMISSION_GRANTED
      return ContextCompat.checkSelfPermission(activity!!, permission) == granted
    }
  }
  private fun handlePermissionsRequest(permissions: Array<String>, grantResults: IntArray) {
    fun isLocationPermission(permission: String) =
            permission == "android.permission.ACCESS_COARSE_LOCATION" ||
                    permission == "android.permission.ACCESS_FINE_LOCATION"

    for (i in permissions.indices) {
      if (isLocationPermission(permissions[i])) {
        if (grantResults[i] == PackageManager.PERMISSION_GRANTED) {
          this.result?.success("granted")
        } else {
          this.result?.success("denied")
        }
      }
    }
  }
}