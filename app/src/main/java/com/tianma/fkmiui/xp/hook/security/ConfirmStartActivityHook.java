package com.tianma.fkmiui.xp.hook.security;

import android.app.Activity;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;

import com.tianma.fkmiui.utils.XLog;
import com.tianma.fkmiui.utils.XSPUtils;
import com.tianma.fkmiui.xp.hook.base.BaseSubHook;
import com.tianma.fkmiui.xp.wrapper.MethodHookWrapper;
import com.tianma.fkmiui.xp.wrapper.XposedWrapper;

import de.robv.android.xposed.XSharedPreferences;

public class ConfirmStartActivityHook extends BaseSubHook {

    private static final String CLASS_PMS_INJECTOR = "com.miui.wakepath.ui.ConfirmStartActivity";

    private boolean mDisableStartActivityConfirm;

    public ConfirmStartActivityHook(ClassLoader classLoader, XSharedPreferences xsp) {
        super(classLoader, xsp);

        mDisableStartActivityConfirm = XSPUtils.disableStartActivityConfirm(xsp);
    }

    @Override
    public void startHook() {
        try {
            XLog.d("Hooking ConfirmStartActivity...");
            if (mDisableStartActivityConfirm) {
                hookOnCreate();
            }
        } catch (Throwable t) {
            XLog.e("Error occurs when hook ConfirmStartActivity", t);
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
                        int stringId = res.getIdentifier("button_text_accept", "string", SecurityCenterHook.PACKAGE_NAME);
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
