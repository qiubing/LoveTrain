package cn.nubia.component;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;

/**
 * Created by JiangYu on 2015/9/21.
 */
public class DialogMaker {

    public static AlertDialog.Builder make(final Activity activity,String msg,Boolean contextFinished){
        AlertDialog.Builder dialog = new AlertDialog.Builder(activity).setMessage(msg);

        DialogInterface.OnClickListener listenerStay = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {}
        };

        DialogInterface.OnClickListener ListenerLeave = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                activity.finish();
            }
        };
        if(contextFinished){
            dialog.setPositiveButton("确定",ListenerLeave);
        }else{
            dialog.setPositiveButton("确定",listenerStay);
        }
        dialog.setNegativeButton("取消", listenerStay);
        return dialog;
    }
}
