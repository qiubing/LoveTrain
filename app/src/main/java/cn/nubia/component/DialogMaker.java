package cn.nubia.component;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.view.WindowManager;

/**
 * Created by JiangYu on 2015/9/21.
 */
public class DialogMaker {

    public static void finishCurrentDialog(Context displayContext, final Activity finishActivity, String msg, Boolean contextFinished){

        if(displayContext!=null) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(displayContext).setMessage(msg);

            DialogInterface.OnClickListener listenerStay = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {}
            };

            DialogInterface.OnClickListener listenerLeave = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(finishActivity!=null)
                        finishActivity.finish();
                }
            };

            if(contextFinished){
                dialogBuilder.setPositiveButton("确定", listenerLeave);
            }else{
                dialogBuilder.setPositiveButton("确定", listenerStay);
            }
            dialogBuilder.setNegativeButton("取消", listenerStay);

            AlertDialog dialog = dialogBuilder.create();
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            dialog.show();
        }
    }

    public static void jumpToTargetDialog(final Context displayContext, final Activity finishActivity,final Intent targetIntent, String msg, Boolean contextFinished){
        if(displayContext!=null) {
            AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(displayContext).setMessage(msg);

            DialogInterface.OnClickListener listenerStay = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {}
            };

            DialogInterface.OnClickListener listenerJump = new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    if(targetIntent!=null)
                        finishActivity.finish();
                        displayContext.startActivity(targetIntent);
                }
            };

            if(contextFinished){
                dialogBuilder.setPositiveButton("确定", listenerJump);
            }else{
                dialogBuilder.setPositiveButton("确定", listenerStay);
            }
            dialogBuilder.setNegativeButton("取消", listenerStay);

            AlertDialog dialog = dialogBuilder.create();
            dialog.getWindow().setType(WindowManager.LayoutParams.TYPE_SYSTEM_ALERT);
            dialog.show();
        }
    }
}
