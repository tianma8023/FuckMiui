package com.tianma.fkmiui.xp.hook.security;

import android.content.res.Resources;
import android.util.ArrayMap;

public class ResHelper {

    private static ArrayMap<String, Integer> sNameIdMap;

    static {
        sNameIdMap = new ArrayMap<>();
    }

    public static Integer getId(Resources res, String name) {
        if (!sNameIdMap.containsKey(name)) {
            int id = res.getIdentifier(name, "id", SecurityCenterHook.PACKAGE_NAME);
            sNameIdMap.put(name, id);
        }
        return sNameIdMap.get(name);
    }

}
