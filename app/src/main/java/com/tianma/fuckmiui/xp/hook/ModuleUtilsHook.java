package com.tianma.fuckmiui.xp.hook;


import com.tianma.fuckmiui.BuildConfig;
import com.tianma.fuckmiui.utils.ModuleUtils;
import com.tianma.fuckmiui.utils.XLog;
import com.tianma.fuckmiui.xp.hook.base.BaseHook;
import com.tianma.fuckmiui.xp.wrapper.MethodHookWrapper;
import com.tianma.fuckmiui.xp.wrapper.XposedWrapper;

import de.robv.android.xposed.callbacks.XC_LoadPackage;

/**
 * Hook class com.github.tianma8023.xposed.smscode.utils.ModuleUtils
 */
public class ModuleUtilsHook extends BaseHook {

    private static final String MI_TWEAKS_PACKAGE = BuildConfig.APPLICATION_ID;

    @Override
    public void onLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        if (MI_TWEAKS_PACKAGE.equals(lpparam.packageName)) {
            try {
                XLog.i("Hooking current Xposed module status...");
                hookModuleUtils(lpparam);
            } catch (Throwable e) {
                XLog.e("Failed to hook current Xposed module status.");
            }
        }

    }

    private void hookModuleUtils(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        String className = ModuleUtils.class.getName();

        XposedWrapper.findAndHookMethod(className, lpparam.classLoader,
                "isModuleActive",
                new MethodHookWrapper() {
                    @Override
                    protected void before(MethodHookParam param) {
                        param.setResult(true);
                    }
                });
    }

}
