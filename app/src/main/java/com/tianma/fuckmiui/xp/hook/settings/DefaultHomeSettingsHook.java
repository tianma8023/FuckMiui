package com.tianma.fuckmiui.xp.hook.settings;

import com.tianma.fuckmiui.utils.XLog;
import com.tianma.fuckmiui.utils.XSPUtils;
import com.tianma.fuckmiui.xp.hook.base.BaseSubHook;
import com.tianma.fuckmiui.xp.wrapper.MethodHookWrapper;
import com.tianma.fuckmiui.xp.wrapper.XposedWrapper;

import java.lang.reflect.Method;

import de.robv.android.xposed.XSharedPreferences;

/**
 * com.android.settings.applications.DefaultHomeSettings
 */
public class DefaultHomeSettingsHook extends BaseSubHook {

    private static final String CLASS_DEFAULT_HOME_SETTINGS = "com.android.settings.applications.DefaultHomeSettings";

    private boolean mDisable3rdLauncherLimit;

    public DefaultHomeSettingsHook(ClassLoader classLoader, XSharedPreferences xsp) {
        super(classLoader, xsp);

        mDisable3rdLauncherLimit = XSPUtils.disable3rdLauncherLimit(xsp);
    }

    @Override
    public void startHook() {
        try {
            XLog.d("Hooking DefaultHomeSettings...");
            if (mDisable3rdLauncherLimit) {
                disable3rdLauncherLimit();
            }
        } catch (Throwable t) {
            XLog.e("Error occurs when hook DefaultHomeSettings", t);
        }
    }

    // #isScreenHasClockGadget()
    private void disable3rdLauncherLimit() {
        Class<?> cls = XposedWrapper.findClass(CLASS_DEFAULT_HOME_SETTINGS, mClassLoader);
        if (cls != null) {
            Method[] methods = cls.getDeclaredMethods();

            Method exactMethod = null;
            for(Method method : methods) {
                Class[] parameterTypes = method.getParameterTypes();
                if (parameterTypes.length == 1 && parameterTypes[0] == String.class) {
                    method.setAccessible(true);
                    exactMethod = method;
                    break;
                }
            }
            if (exactMethod != null) {
                XposedWrapper.hookMethod(exactMethod, new MethodHookWrapper() {
                    @Override
                    protected void before(MethodHookParam param) {
                        param.setResult(false);
                    }
                });
            }
        }
    }
}
