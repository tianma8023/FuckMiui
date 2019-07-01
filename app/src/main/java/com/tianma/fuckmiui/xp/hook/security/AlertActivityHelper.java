package com.tianma.fuckmiui.xp.hook.security;

import android.view.View;

import java.util.ArrayList;

/**
 * Helper for miui.app.AlertActivity
 */
public class AlertActivityHelper {

    static View findPositiveButton(View contentView, String positiveText) {
        ArrayList<View> outViews = new ArrayList<>();
        contentView.findViewsWithText(outViews, positiveText, View.FIND_VIEWS_WITH_TEXT);
        View positiveBtn = null;
        if (outViews.size() > 0) {
            int size = outViews.size();
            for (int i = size - 1; i >= 0; i--) {
                View view = outViews.get(i);
                if ("com.miui.internal.widget.GroupButton".equals(view.getClass().getName())) {
                    positiveBtn = view;
                    break;
                }
            }
            if (positiveBtn == null) {
                positiveBtn = outViews.get(size - 1);
            }
        }
        return positiveBtn;
    }

}
