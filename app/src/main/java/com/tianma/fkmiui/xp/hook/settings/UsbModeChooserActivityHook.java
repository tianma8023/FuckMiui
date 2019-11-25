package com.tianma.fkmiui.xp.hook.settings;

import android.app.Activity;
import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.SystemClock;

import com.tianma.fkmiui.utils.XLog;
import com.tianma.fkmiui.utils.XSPUtils;
import com.tianma.fkmiui.xp.hook.base.BaseSubHook;
import com.tianma.fkmiui.xp.wrapper.MethodHookWrapper;
import com.tianma.fkmiui.xp.wrapper.XposedWrapper;

import de.robv.android.xposed.XSharedPreferences;
import de.robv.android.xposed.XposedHelpers;

/**
 * hook com.android.settings.connecteddevice.usb.UsbModeChooserActivity
 */
public class UsbModeChooserActivityHook extends BaseSubHook {

    private static final String CLASS_USB_MODE_CHOOSER_ACTIVITY = "com.android.settings.connecteddevice.usb.UsbModeChooserActivity";

    private boolean forbidUsbChooserWhenUsbConnected;

    private long mUsbConnectedTimeStamp = -1L;

    private volatile boolean mUsbStateListenerRegistered = false;

    public UsbModeChooserActivityHook(ClassLoader classLoader, XSharedPreferences xsp) {
        super(classLoader, xsp);
        forbidUsbChooserWhenUsbConnected = XSPUtils.forbidUsbModeChooserWhenUsbConnecte(xsp);
    }

    @Override
    public void startHook() {
        try {
            XLog.i("Hooking UsbModeChooserActivity...");
            if (forbidUsbChooserWhenUsbConnected) {
                hookOnCreate();
                hookInitDialog();
            }
        } catch (Throwable t) {
            XLog.e("Error occurs when hook UsbModeChooserActivity", t);
        }
    }

    // UsbModeChooserActivity#onCreate()
    private void hookOnCreate() {
        Class<?> cls = XposedWrapper.findClass(CLASS_USB_MODE_CHOOSER_ACTIVITY, mClassLoader);
        if (cls == null) {
            return;
        }

        XposedWrapper.findAndHookMethod(cls, "onCreate",
                Bundle.class,
                new MethodHookWrapper() {
                    @Override
                    protected void after(MethodHookParam param) {
                        Activity thisObject = (Activity) param.thisObject;
                        registerUsbStateReceiver(thisObject);
                    }
                });
    }

    // UsbModeChooserActivity#initDialog()
    private void hookInitDialog() {
        Class<?> cls = XposedWrapper.findClass(CLASS_USB_MODE_CHOOSER_ACTIVITY, mClassLoader);
        if(cls == null) {
            return;
        }
        XposedWrapper.findAndHookMethod(cls, "initDialog",
                new MethodHookWrapper() {
                    @Override
                    protected void before(MethodHookParam param) {
                        boolean dismissed = dismissDialogIfNeeded(param);
                        if (dismissed) {
                            // if dialog dismissed, block the initDialog() function
                            param.setResult(null);
                        }
                    }

                    @Override
                    protected void after(MethodHookParam param) {
                        dismissDialogIfNeeded(param);
                    }

                    private boolean dismissDialogIfNeeded(MethodHookParam param) {
                        Object thisObject = param.thisObject;
                        Dialog mDialog = (Dialog) XposedHelpers.getObjectField(thisObject, "mDialog");
                        if (mDialog != null) {
                            long curTimeStamp = SystemClock.elapsedRealtime();
                            if (Math.abs(curTimeStamp - mUsbConnectedTimeStamp) < 3000) {
                                mDialog.dismiss();
                                return true;
                            }
                        }
                        return false;
                    }
                });
    }

    private void registerUsbStateReceiver(Context settingsContext) {
        if (!mUsbStateListenerRegistered) {
            IntentFilter intentFilter = new IntentFilter();
            intentFilter.addAction(UsbStateReceiver.ACTION_USB_STATE);

            UsbStateReceiver usbStateReceiver = new UsbStateReceiver();
            settingsContext.getApplicationContext().registerReceiver(usbStateReceiver, intentFilter);
        }
    }

    private class UsbStateReceiver extends BroadcastReceiver {

        private static final String ACTION_USB_STATE = "android.hardware.usb.action.USB_STATE";

        @Override
        public void onReceive(Context context, Intent intent) {
            mUsbStateListenerRegistered = true;
            String action = intent.getAction();
            if (ACTION_USB_STATE.equals(action)) {
                // USB_STATE broadcast, intent extras info:
                // host_connected=true/false
                // connected=true/false
                // unlocked=true/false
                // adb=true/false
                // configured=true/false

                boolean connected = intent.getBooleanExtra("connected", false);
                boolean hostConnected = intent.getBooleanExtra("host_connected", false);
                if (connected || hostConnected) {
                    mUsbConnectedTimeStamp = SystemClock.elapsedRealtime();
                }
            }
        }
    }
}
