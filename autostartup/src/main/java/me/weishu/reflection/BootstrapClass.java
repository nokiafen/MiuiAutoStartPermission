//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package me.weishu.reflection;

import android.os.Build;
import android.os.Build.VERSION;
import android.util.Log;

import java.lang.reflect.Method;

public final class BootstrapClass {
    private static final String TAG = "BootstrapClass";
    private static Object sVmRuntime;
    private static Method setHiddenApiExemptions;

    public static boolean exempt(String method) {
        return exempt(new String[]{method});
    }

    public static boolean exempt(String[] methods) {
        if ((sVmRuntime == null) || (setHiddenApiExemptions == null)) {
            return false;
        }
        try {
            setHiddenApiExemptions.invoke(sVmRuntime, new Object[]{methods});
            return true;
        } catch (Throwable e) {
        }
        return false;
    }

    public static boolean exemptAll() {
        return exempt(new String[]{"L"});
    }

    static {
        if (Build.VERSION.SDK_INT >= 28)
            try {
                Method forName = Class.class.getDeclaredMethod("forName", new Class[]{String.class});
                Method getDeclaredMethod = Class.class.getDeclaredMethod("getDeclaredMethod", String.class, Class[].class);

                Class vmRuntimeClass = (Class) forName.invoke(null, new Object[]{"dalvik.system.VMRuntime"});
                Method getRuntime = (Method) getDeclaredMethod.invoke(vmRuntimeClass, new Object[]{"getRuntime", null});
                setHiddenApiExemptions = (Method) getDeclaredMethod.invoke(vmRuntimeClass, new Object[]{"setHiddenApiExemptions", java.lang.String.class});
                sVmRuntime = getRuntime.invoke(null, new Object[0]);
            } catch (Throwable e) {
                Log.w("BootstrapClass", "reflect bootstrap failed:", e);
            }
    }
}
