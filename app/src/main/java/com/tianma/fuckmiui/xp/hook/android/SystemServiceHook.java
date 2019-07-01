package com.tianma.fuckmiui.xp.hook.android;

import com.tianma.fuckmiui.utils.XLog;
import com.tianma.fuckmiui.utils.XSPUtils;
import com.tianma.fuckmiui.utils.rom.MiuiUtils;
import com.tianma.fuckmiui.xp.hook.base.BaseHook;

import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class SystemServiceHook extends BaseHook {

    public static final String PACKAGE_NAME = "android";

    public SystemServiceHook() {
    }

    @Override
    public void onLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (PACKAGE_NAME.equals(lpparam.packageName)) {
            XLog.i("Hooking Android system service...");

            XSharedPreferences xsp = XSPUtils.getXSharedPreferences();

            ClassLoader classLoader = lpparam.classLoader;
            if (XSPUtils.isMainSwitchEnabled(xsp)) {
                if (!MiuiUtils.isMiui()) {
                    return;
                }
                new PMSInjectorHook(classLoader, xsp).startHook();
            }

        }
    }

}
