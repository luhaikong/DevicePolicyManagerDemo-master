package com.wangchunlei.devicepolicymanagerdemo;

import android.app.Activity;
import android.app.admin.DeviceAdminReceiver;
import android.app.admin.DevicePolicyManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;


public class MainActivity extends Activity {
	private final String tag = "Ray";

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
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu_main, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.action_settings) {
			registerDevicePolicyManager();
			return true;
		}

		return super.onOptionsItemSelected(item);
	}
}
