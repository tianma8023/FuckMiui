package com.tianma.fkmiui.xp.hook;


import com.tianma.fkmiui.BuildConfig;
import com.tianma.fkmiui.utils.ModuleUtils;
import com.tianma.fkmiui.utils.XLog;
import com.tianma.fkmiui.xp.hook.base.BaseHook;
import com.tianma.fkmiui.xp.wrapper.MethodHookWrapper;
import com.tianma.fkmiui.xp.wrapper.XposedWrapper;

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
