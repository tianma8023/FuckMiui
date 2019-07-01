package com.tianma.fuckmiui.utils;

import com.tianma.fuckmiui.BuildConfig;
import com.tianma.fuckmiui.cons.AppConst;

import java.io.File;

import de.robv.android.xposed.XSharedPreferences;

import static com.tianma.fuckmiui.cons.PrefConst.DISABLE_3RD_LAUNCHER_LIMIT;
import static com.tianma.fuckmiui.cons.PrefConst.DISABLE_ADB_INSTALL_VERIFY;
import static com.tianma.fuckmiui.cons.PrefConst.DISABLE_START_ACTIVITY_CONFIRM;
import static com.tianma.fuckmiui.cons.PrefConst.MAIN_SWITCH;

public class XSPUtils {

    private XSPUtils() {
    }

    /**
     * 获取XSharedPreferences
     */
    public static XSharedPreferences getXSharedPreferences() {
        File prefsFile = new File("/data/user_de/0/" + BuildConfig.APPLICATION_ID + "/shared_prefs/" + AppConst.X_MIUI_CLOCK_PREFS_NAME + ".xml");
        XSharedPreferences xsp;
        if (prefsFile.exists()) { // Android 7.0+
            xsp = new XSharedPreferences(prefsFile);
        } else { // below Android 7.0
            xsp = new XSharedPreferences(BuildConfig.APPLICATION_ID);
        }
        try {
            xsp.makeWorldReadable();
        } catch (Throwable t) {
            XLog.e("", t);
        }
        return xsp;
    }

    /**
     * 是否打开总开关
     */
    public static boolean isMainSwitchEnabled(XSharedPreferences xsp) {
        return xsp.getBoolean(MAIN_SWITCH, true);
    }

    /**
     * 是否取消第三方桌面限制
     */
    public static boolean disable3rdLauncherLimit(XSharedPreferences xsp) {
        return xsp.getBoolean(DISABLE_3RD_LAUNCHER_LIMIT, false);
    }

    /**
     * 是否禁用ADB安装验证
     */
    public static boolean disableAdbInstallVerify(XSharedPreferences xsp) {
        return xsp.getBoolean(DISABLE_ADB_INSTALL_VERIFY, false);
    }

    /**
     * 是否禁用开启其他App时的验证
     */
    public static boolean disableStartActivityConfirm(XSharedPreferences xsp) {
        return xsp.getBoolean(DISABLE_START_ACTIVITY_CONFIRM, false);
    }

}
