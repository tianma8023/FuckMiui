package com.tianma.fkmiui.xp;


import com.tianma.fkmiui.xp.hook.base.BaseHook;
import com.tianma.fkmiui.xp.hook.ModuleUtilsHook;
import com.tianma.fkmiui.xp.hook.security.SecurityCenterHook;
import com.tianma.fkmiui.xp.hook.settings.SettingsHook;

import java.util.ArrayList;
import java.util.List;

import de.robv.android.xposed.IXposedHookLoadPackage;
import de.robv.android.xposed.IXposedHookZygoteInit;
import de.robv.android.xposed.callbacks.XC_LoadPackage;

public class HookEntry implements IXposedHookLoadPackage, IXposedHookZygoteInit {

    private List<BaseHook> mHookList;

    {
        mHookList = new ArrayList<>();
        mHookList.add(new ModuleUtilsHook()); // Current Module Hook
        mHookList.add(new SettingsHook()); // Miui Setting Hook
//        mHookList.add(new SystemServiceHook()); // Android(Miui) System Service Hook
        mHookList.add(new SecurityCenterHook()); // Security Center Hook
    }

    @Override
    public void initZygote(StartupParam startupParam) throws Throwable {
        for (BaseHook hook : mHookList) {
            if (hook.hookInitZygote()) {
                hook.initZygote(startupParam);
            }
        }
    }

    @Override
    public void handleLoadPackage(XC_LoadPackage.LoadPackageParam lpparam) throws Throwable {
        for (BaseHook hook : mHookList) {
            if (hook.hookOnLoadPackage()) {
                hook.onLoadPackage(lpparam);
            }
        }
    }
}
