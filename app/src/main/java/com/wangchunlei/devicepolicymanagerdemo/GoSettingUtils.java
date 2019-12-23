package com.wangchunlei.devicepolicymanagerdemo;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;

/**
 * Created by Administrator on 2019/12/23.
 */

public class GoSettingUtils {

    private void goSmartisanSetting(Context context) {
        showActivity("com.smartisanos.security",context);
    }

    private void goLetvSetting(Context context) {
        showActivity("com.letv.android.letvsafe",
                "com.letv.android.letvsafe.AutobootManageActivity",context);
    }

    private void goSamsungSetting(Context context) {
        try {
            showActivity("com.samsung.android.sm_cn",context);
        } catch (Exception e) {
            showActivity("com.samsung.android.sm",context);
        }
    }

    private void goMeizuSetting(Context context) {
        showActivity("com.meizu.safe",context);
    }

    private void goVIVOSetting(Context context) {
        showActivity("com.iqoo.secure",context);
    }

    private void goOPPOSetting(Context context) {
        try {
            showActivity("com.coloros.phonemanager",context);
        } catch (Exception e1) {
            try {
                showActivity("com.oppo.safe",context);
            } catch (Exception e2) {
                try {
                    showActivity("com.coloros.oppoguardelf",context);
                } catch (Exception e3) {
                    showActivity("com.coloros.safecenter",context);
                }
            }
        }
    }

    private void goXiaomiSetting(Context context) {
        showActivity("com.miui.securitycenter",
                "com.miui.permcenter.autostart.AutoStartManagementActivity",context);
    }

    private void goHuaweiSetting(Context context) {
        try {
            showActivity("com.huawei.systemmanager",
                    "com.huawei.systemmanager.startupmgr.ui.StartupNormalAppListActivity",context);
        } catch (Exception e) {
            showActivity("com.huawei.systemmanager",
                    "com.huawei.systemmanager.optimize.bootstart.BootStartActivity",context);
        }
    }

    /**
     * 跳转到指定应用的首页
     */
    private void showActivity(String packageName, Context context) {
        if (packageName!=null&&!packageName.isEmpty()){
            Intent intent = context.getPackageManager().getLaunchIntentForPackage(packageName);
            context.startActivity(intent);
        }
    }

    /**
     * 跳转到指定应用的指定页面
     */
    private void showActivity(String packageName, String activityDir, Context context) {
        if (packageName!=null&&!packageName.isEmpty()&&activityDir!=null&&!activityDir.isEmpty()){
            Intent intent = new Intent();
            intent.setComponent(new ComponentName(packageName, activityDir));
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(intent);
        }
    }

}
