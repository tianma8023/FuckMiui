package com.tianma.fuckmiui.xp.hook.android;

import com.tianma.fuckmiui.utils.XLog;
import com.tianma.fuckmiui.xp.hook.base.BaseSubHook;
import com.tianma.fuckmiui.xp.wrapper.MethodHookWrapper;
import com.tianma.fuckmiui.xp.wrapper.XposedWrapper;

import de.robv.android.xposed.XSharedPreferences;

public class PMSInjectorHook extends BaseSubHook {

    private static final String CLASS_PMS_INJECTOR = "com.android.server.pm.PackageManagerServiceInjector";

    private boolean mCancelAdbInstallLimitation;

    public PMSInjectorHook(ClassLoader classLoader, XSharedPreferences xsp) {
        super(classLoader, xsp);

        mCancelAdbInstallLimitation = true;
    }

    @Override
    public void startHook() {
        try {
            XLog.d("Hooking PMS Injector...");
            if (mCancelAdbInstallLimitation) {
                hookIsScreenHasClockGadgets();
            }
        } catch (Throwable t) {
            XLog.e("Error occurs when hook PMS Injector", t);
        }
    }

    // #isAllowedInstall()
    private void hookIsScreenHasClockGadgets() {
        Class<?> cls = XposedWrapper.findClass(CLASS_PMS_INJECTOR, mClassLoader);
        if (cls != null) {
            XposedWrapper.hookAllMethods(cls, "isAllowedInstall", new MethodHookWrapper() {
                @Override
                protected void before(MethodHookParam param) {
                    param.setResult(true);
                }
            });
        }
    }
}
