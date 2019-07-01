package com.tianma.fuckmiui.xp.hook.security;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;

import com.tianma.fuckmiui.utils.XLog;
import com.tianma.fuckmiui.utils.XSPUtils;
import com.tianma.fuckmiui.xp.hook.base.BaseSubHook;
import com.tianma.fuckmiui.xp.wrapper.MethodHookWrapper;
import com.tianma.fuckmiui.xp.wrapper.XposedWrapper;

import de.robv.android.xposed.XSharedPreferences;

public class AdbInstallActivityHook extends BaseSubHook {

    private static final String CLASS_PMS_INJECTOR = "com.miui.permcenter.install.AdbInstallActivity";

    private boolean mDisableAdbInstallVerify;

    public AdbInstallActivityHook(ClassLoader classLoader, XSharedPreferences xsp) {
        super(classLoader, xsp);

        mDisableAdbInstallVerify = XSPUtils.disableAdbInstallVerify(xsp);
    }

    @Override
    public void startHook() {
        try {
            XLog.d("Hooking AdbInstallActivity...");
            if (mDisableAdbInstallVerify) {
                hookOnCreate();
            }
        } catch (Throwable t) {
            XLog.e("Error occurs when hook AdbInstallActivity", t);
        }
    }

    // #onCreate()
    private void hookOnCreate() {
        XposedWrapper.findAndHookMethod(CLASS_PMS_INJECTOR,
                mClassLoader,
                "onCreate",
                Bundle.class,
                new MethodHookWrapper() {
                    @Override
                    protected void after(MethodHookParam param) {
                        Activity activity = (Activity) param.thisObject;
                        Resources res = activity.getResources();
                        int stringId = res.getIdentifier("continue_install", "string", SecurityCenterHook.PACKAGE_NAME);
                        String acceptStr = res.getString(stringId);
                        View contentView = activity.findViewById(android.R.id.content);

                        View positiveBtn = AlertActivityHelper.findPositiveButton(contentView, acceptStr);
                        if (positiveBtn != null) {
                            positiveBtn.performClick();
                        }
                    }
                });
    }
}
