//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by FernFlower decompiler)
//

package xyz.kumaraswamy.autostart;

import android.content.Context;
import android.os.Build;
import android.util.Log;

import androidx.annotation.Nullable;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.ArrayList;
import me.weishu.reflection.Reflection;

public class Autostart {
    private static final String TAG = "Autostart";
    private static final String XIAOMI_NAME = "xiaomi";
    private static final String MIUI_CLAZZ = "android.miui.AppOpsUtils";
    private static final String POLICY_CLAZZ = "miui.content.pm.PreloadedAppPolicy";
    private static boolean isReflectionEnabled = false;
    private static final int STATE_ENABLED = 0;
    private static final int STATE_DISABLED = 1;
    private final Context context;

    public static boolean isXiaomi() {
        return Build.MANUFACTURER.equalsIgnoreCase("xiaomi");
    }

    public Autostart(Context context) throws Exception {
        if (!isXiaomi()) {
            throw new Exception("Not a Xiaomi device");
        } else {
            this.context = context;
            if (!isReflectionEnabled) {
                Reflection.unseal(context);
                isReflectionEnabled = true;
            }

        }
    }

    public boolean isAutoStartEnabled() throws Exception {
        return this.getAutoStartState() == Autostart.State.ENABLED;
    }

    public Autostart.State getAutoStartState() throws Exception {
        Class<?> clazz = this.getClazz("android.miui.AppOpsUtils");
        if (clazz == null) {
            return Autostart.State.NO_INFO;
        } else {
            Method method = this.findMethod(clazz);
            if (method == null) {
                return Autostart.State.NO_INFO;
            } else {
                Object result = method.invoke((Object)null, this.context, this.context.getPackageName());
                if (!(result instanceof Integer)) {
                    return Autostart.State.UNEXPECTED_RESULT;
                } else {
                    int _int = (Integer)result;
                    if (_int == 0) {
                        return Autostart.State.ENABLED;
                    } else {
                        return _int == 1 ? Autostart.State.DISABLED : Autostart.State.UNEXPECTED_RESULT;
                    }
                }
            }
        }
    }

    public String[] defaultWhiteListedPackages() throws NoSuchFieldException, IllegalAccessException {
        Class<?> clazz = this.getClazz("miui.content.pm.PreloadedAppPolicy");
        if (clazz == null) {
            return new String[0];
        } else {
            Field field = clazz.getDeclaredField("sProtectedDataApps");
            field.setAccessible(true);
            Object result = field.get((Object)null);
            if (result instanceof ArrayList) {
                return (String[])((ArrayList)result).toArray(new String[0]);
            } else {
                String message = "defaultWhiteListedPackages() unexpected result type";
                if (result == null) {
                    return new String[0];
                } else {
                    Log.e("Autostart", "defaultWhiteListedPackages() unexpected result type " + result.getClass());
                    return new String[0];
                }
            }
        }
    }

    private Class<?> getClazz(String name) {
        try {
            return Class.forName(name);
        } catch (ClassNotFoundException var3) {
            return null;
        }
    }

    @Nullable
    private Method findMethod(Class<?> clazz) {
        String var2 = "getApplicationAutoStart";

        try {
            Method method = clazz.getDeclaredMethod("getApplicationAutoStart", Context.class, String.class);
            method.setAccessible(true);
            return method;
        } catch (NoSuchMethodException var8) {
            Method[] var4 = clazz.getDeclaredMethods();
            int var5 = var4.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                Method method = var4[var6];
                if (method.getName().equals("getApplicationAutoStart")) {
                    Log.i("Autostart", "Found a new method matching method name");
                }
            }

            return null;
        }
    }

    public static enum State {
        ENABLED,
        DISABLED,
        NO_INFO,
        UNEXPECTED_RESULT;

        private State() {
        }
    }
}
