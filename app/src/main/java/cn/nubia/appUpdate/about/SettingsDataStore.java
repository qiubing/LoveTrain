package cn.nubia.appUpdate.about;

import android.content.Context;
import android.content.SharedPreferences;

import java.lang.ref.WeakReference;

/**
 * Created by WJ on 2015/10/26.
 */
public class SettingsDataStore {
    public static final String SETTING_SP_NAME = "settings_name";
    public static final String SETTING_NOTIFICATION_KEY = "settings_notification_key";
    public static final boolean SETTING_NOTIFICATION = false;
    public static final String EXIT_COMPLETELY_KEY = "exit_completely_key";
    public static final String DETECT_NEW_VERSION_KEY = "detect_new_version_key";
    public static final String STORE_NOT_ENOUGH_ON_OFF = "store_not_enough_on_off";
    public static final String GARBAGE_NOTIFICATION_ON_OFF = "garbage_notification_on_off";
    public static final String INSTORE_NOT_ENOUGH_ON_OFF = "instore_not_enough_on_off";
    public static final String WIFI_AUTO_UPDATE_DB = "wifi_auto_update_db";
    public static final String WHETHER_REMIND_AGAIN = "whether_remind_again";
    private static final String INIT_FINISH = "init_finish";
    private static final String AUTO_LAUNCH = "auto_launch";

    private WeakReference<Context> mContextWeakRef;
    private SharedPreferences mPreferences;

    public SettingsDataStore(Context context){
        mContextWeakRef = new WeakReference<Context>(context);
        if(null != context){
            mPreferences = context.getSharedPreferences(SETTING_SP_NAME,
                    Context.MODE_MULTI_PROCESS);
        }
    }

    public boolean getSwitchStatus(String key){
        SharedPreferences sp = mContextWeakRef.get().getSharedPreferences(
                SETTING_SP_NAME,0);
        return sp.getBoolean(key,false);
    }

    public boolean getSwitchStatus(String key,boolean defValue){
        SharedPreferences sp = mContextWeakRef.get()
                .getSharedPreferences(SETTING_SP_NAME, Context.MODE_PRIVATE);
        return sp.getBoolean(key,defValue);
    }

    public void setSwitchStatus(String key,boolean value){
        SharedPreferences sp = mContextWeakRef.get()
                .getSharedPreferences(SETTING_SP_NAME, 0);
        SharedPreferences.Editor editor = sp.edit().putBoolean(key,value);
        editor.apply();
    }

    /**
     * 查询是否开机自启动
     *
     * */
    public boolean isAutoLaunch(){
        Context context = mContextWeakRef.get();
        if(null != context){
            SharedPreferences sp = context.getSharedPreferences(SETTING_SP_NAME, Context.MODE_PRIVATE);
            return sp.getBoolean(AUTO_LAUNCH,false);
        }
        return false;
    }

    public void enableAutoLaunch(boolean enabled){
        Context context = mContextWeakRef.get();
        if(null != context){
            SharedPreferences.Editor editor = context.getSharedPreferences(
                    SETTING_SP_NAME,Context.MODE_PRIVATE)
                    .edit();
            editor.putBoolean(AUTO_LAUNCH,enabled);
            editor.commit();
        }
    }

    public boolean isInited(){
        return mPreferences != null && mPreferences.getBoolean(INIT_FINISH,false);
    }

    public void setInited(boolean inited){
        if( null != mPreferences){
            SharedPreferences.Editor editor = mPreferences.edit();
            editor.putBoolean(INIT_FINISH,inited);
            editor.commit();
        }
    }
}
