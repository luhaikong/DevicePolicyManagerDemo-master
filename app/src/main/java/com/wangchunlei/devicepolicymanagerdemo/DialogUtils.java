package com.wangchunlei.devicepolicymanagerdemo;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.widget.Button;

/**
 * Created by Administrator on 2019/6/14.
 */

public class DialogUtils {

    public static void showDialog(Context context, String title, String msg
            , String txtYes, DialogInterface.OnClickListener onYesListener
            , String txtNo, DialogInterface.OnClickListener onNoListener){
        AlertDialog.Builder builder = new AlertDialog.Builder(context);
        builder.setTitle(title);
        builder.setMessage(msg);
        if (txtNo!=null&&!txtNo.isEmpty()){
            builder.setNegativeButton(txtNo, onNoListener);
        }
        if (txtYes!=null&&!txtYes.isEmpty()){
            builder.setPositiveButton(txtYes, onYesListener);
        }
        AlertDialog dialog = builder.create();
        dialog.show();
        Button buttonYes = dialog.getButton(DialogInterface.BUTTON_POSITIVE);
        buttonYes.setTextColor(Color.BLUE);
        Button buttonNo = dialog.getButton(DialogInterface.BUTTON_NEGATIVE);
        buttonNo.setTextColor(Color.RED);
    }
}
