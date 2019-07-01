package com.tianma.fuckmiui.xp.hook.base;

import com.tianma.fuckmiui.utils.rom.MiuiVersion;

import de.robv.android.xposed.XSharedPreferences;

public abstract class BaseSubHook {

    protected ClassLoader mClassLoader;
    protected XSharedPreferences xsp;
    protected MiuiVersion mMiuiVersion;

    public BaseSubHook(ClassLoader classLoader, XSharedPreferences xsp) {
        this(classLoader, xsp, null);
    }

    public BaseSubHook(ClassLoader classLoader, XSharedPreferences xsp, MiuiVersion miuiVersion) {
        mClassLoader = classLoader;
        this.xsp = xsp;
        mMiuiVersion = miuiVersion;
    }

    public abstract void startHook();

}
