package com.wangchunlei.devicepolicymanagerdemo;

import android.app.Activity;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.PowerManager;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity {
	private final String tag = "Ray";//注册应用到设备管理器中
	private final String tag1 = "Ray1";//申请加入到应用白名单中

	//设备管理器
	private DevicePolicyManager devicePolicyManager;
	private ComponentName componentName;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		registerDevicePolicyManager();
	}

	/**
	 * 注册成为"设备管理器"
	 */
	public void registerDevicePolicyManager() {
		componentName = new ComponentName(this, MyDeviceAdminReceiver.class);
		try {
			// 实例化系统的设备管理器
			devicePolicyManager = (DevicePolicyManager) getSystemService(Context.DEVICE_POLICY_SERVICE);
			//指定广播接收器
//			componentName = new ComponentName(this, DeviceAdminReceiver.class);
			//检测是否已经是设备管理器
			if (!devicePolicyManager.isAdminActive(componentName)) {
				//开始注册设备管理器，Action必须为DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN
				Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
				intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
				//注册设备管理器时可以显示一些话术，就在这里添加
				intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "我需要这些权限");
				//打开注册页面
				startActivityForResult(intent, 0);
			} else {
				// 已经是设备管理器了，就可以操作一些特殊的安全权限了
				DialogUtils.showDialog(this, "温馨提示", "您已经注册过设备管理器了，需要再来一次吗？"
						, "是的", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								unregisterDevicePolicyManager();

								//开始注册设备管理器，Action必须为DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN
								Intent intent = new Intent(DevicePolicyManager.ACTION_ADD_DEVICE_ADMIN);
								intent.putExtra(DevicePolicyManager.EXTRA_DEVICE_ADMIN, componentName);
								//注册设备管理器时可以显示一些话术，就在这里添加
								intent.putExtra(DevicePolicyManager.EXTRA_ADD_EXPLANATION, "我需要这些权限");
								//打开注册页面
								startActivityForResult(intent, 0);
								dialog.dismiss();
							}
						}, "打扰了", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});
			}
		} catch (Exception e) {
			e.printStackTrace();
			DialogUtils.showDialog(this, "错误信息", e.getMessage()
					, "是", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					}, "否", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					});
		}
	}

	private void unregisterDevicePolicyManager(){
		boolean active = devicePolicyManager.isAdminActive(componentName);
		if (active) {
			devicePolicyManager.removeActiveAdmin(componentName);
		}
	}

	/**
	 * 申请加入应用白名单
	 */
	public void requestIgnoreBatteryOptimizations() {
		try {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M && !isIgnoringBatteryOptimizations()) {
				Intent intent = new Intent(Settings.ACTION_REQUEST_IGNORE_BATTERY_OPTIMIZATIONS);
				intent.setData(Uri.parse("package:" + getPackageName()));
				startActivityForResult(intent,1);
			}
		} catch (Exception e) {
			e.printStackTrace();
			DialogUtils.showDialog(this, "申请结果", e.getMessage()
					, "知道了", new DialogInterface.OnClickListener() {
						@Override
						public void onClick(DialogInterface dialog, int which) {
							dialog.dismiss();
						}
					}, "算了", null);
		}
	}

	/**
	 * 判断我们的应用是否在白名单中
	 * @return
	 */
	private boolean isIgnoringBatteryOptimizations() {
		boolean isIgnoring = false;
		PowerManager powerManager = (PowerManager) getSystemService(Context.POWER_SERVICE);
		if (powerManager != null) {
			if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
				isIgnoring = powerManager.isIgnoringBatteryOptimizations(getPackageName());
			}
		}
		return isIgnoring;
	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		if (0 == requestCode) {
			if (resultCode == Activity.RESULT_OK) {
				// 用户同意了
				Log.i(tag, "Enable it");
				DialogUtils.showDialog(this, "申请结果", "同意了"
						, "是", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						}, "否", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});
			} else {
				Log.i(tag, "Cancle it");
				// 用户拒绝了
				DialogUtils.showDialog(this, "申请结果", "拒绝了"
						, "是", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						}, "否", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});
			}
		}

		if (1 == requestCode){
			if (resultCode == RESULT_OK){
				// 用户同意了
				Log.i(tag1, "Enable it");
				DialogUtils.showDialog(this, "申请结果", "同意了"
						, "是", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						}, "否", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});
			} else {
				Log.i(tag1, "Cancle it");
				// 用户拒绝了
				DialogUtils.showDialog(this, "申请结果", "拒绝了"
						, "是", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						}, "否", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		int id = item.getItemId();

		if (id == R.id.action_dpm) {
			registerDevicePolicyManager();
			return true;
		}
		if (id == R.id.action_white) {
			if (!isIgnoringBatteryOptimizations()){
				requestIgnoreBatteryOptimizations();
			} else {
				DialogUtils.showDialog(this, "申请结果", "已成功申请白名单！"
						, "是", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						}, "否", new DialogInterface.OnClickListener() {
							@Override
							public void onClick(DialogInterface dialog, int which) {
								dialog.dismiss();
							}
						});
			}
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
