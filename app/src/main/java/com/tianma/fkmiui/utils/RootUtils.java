package com.tianma.fkmiui.utils;

import com.jaredrummler.android.shell.Shell;
import com.tianma.fkmiui.xp.hook.android.SystemServiceHook;
import com.tianma.fkmiui.xp.hook.security.SecurityCenterHook;
import com.tianma.fkmiui.xp.hook.settings.SettingsHook;

/**
 * Utils for root action
 */
public class RootUtils {

    private RootUtils() {

    }

    public static void killSettings() {
        killAll(SettingsHook.PACKAGE_NAME);
    }

    public static void killAndroid() {
        killAll(SystemServiceHook.PACKAGE_NAME);
    }

    public static void killSecurityCenter() {
        killAll(SecurityCenterHook.PACKAGE_NAME);
    }

    /**
     * killall <process name>
     *
     * @param processName process name
     */
    private static void killAll(String processName) {
        String cmd = String.format("killall %s", processName);
        Shell.SU.run(cmd);
    }

    /**
     * Reboot
     */
    public static void reboot() {
        Shell.SU.run("reboot");
    }

    /**
     * Soft Reboot
     */
    public static void softReboot() {
        Shell.SU.run("setprop ctl.restart surfaceflinger; setprop ctl.restart zygote");
    }

}
