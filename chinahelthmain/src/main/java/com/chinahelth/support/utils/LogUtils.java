package com.chinahelth.support.utils;

import android.text.TextUtils;
import android.util.Log;

import com.chinahelth.HealthConfig;

import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Array;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.Collection;
import java.util.Map;

public class LogUtils {

    private static final boolean IS_DEBUG = HealthConfig.isDebug;
    private static final String TAG = "EasyMode.LogUtils";

    public LogUtils() {
    }

    private static String getLogPoi() {
        StackTraceElement[] sts = Thread.currentThread().getStackTrace();
        if (sts == null) {
            return null;
        }
        for (StackTraceElement st : sts) {
            if (st.isNativeMethod()) {
                continue;
            }
            if (st.getClassName().equals(Thread.class.getName())) {
                continue;
            }
            if (st.getClassName().equals(new LogUtils().getClass().getName())) {
                continue;
            }
            return "[" + st.getClassName() + ": " + st.getLineNumber() + "]\n";
        }
        return null;
    }

    public static void v(String str) {
        if (IS_DEBUG) {
            Log.v(TAG, getLogPoi());
            Log.v(TAG, str);
        }
    }

    public static void v(String tag, String str) {
        if (IS_DEBUG) {
            Log.v(tag, getLogPoi());
            Log.v(tag, str);
        }
    }

    public static void v(String tag, String str, Throwable throwable) {
        if (IS_DEBUG) {
            Log.v(tag, getLogPoi());
            Log.v(tag, str);
            Log.v(tag, str, throwable);
        }
    }

    public static void d(String str) {
        if (IS_DEBUG) {
            Log.d(TAG, getLogPoi());
            Log.d(TAG, str);
        }
    }

    public static void d(String tag, String str) {
        if (IS_DEBUG) {
            Log.d(tag, getLogPoi());
            Log.d(tag, str);
        }
    }

    public static void d(String tag, String str, Throwable throwable) {
        if (IS_DEBUG) {
            Log.d(tag, getLogPoi());
            Log.d(tag, str);
            Log.d(tag, str, throwable);
        }
    }

    public static void i(String str) {
        if (IS_DEBUG) {
            if (TextUtils.isEmpty(str)) {
                Log.i(TAG, "null");
            } else {
                Log.i(TAG, getLogPoi());
                Log.i(TAG, str);
            }
        }
    }

    public static void i(String tag, String str) {
        if (IS_DEBUG) {
            Log.i(tag, getLogPoi());
            Log.i(tag, str);
        }
    }

    public static void i(String tag, String str, Throwable throwable) {
        if (IS_DEBUG) {
            Log.i(tag, getLogPoi());
            Log.i(tag, str);
            Log.i(tag, str, throwable);
        }
    }

    public static void w(String str) {
        if (IS_DEBUG) {
            Log.w(TAG, getLogPoi());
            Log.w(TAG, str);
        }
    }

    public static void w(String tag, String str) {
        if (IS_DEBUG) {
            Log.w(tag, getLogPoi());
            Log.w(tag, str);
        }
    }

    public static void w(String tag, String str, Throwable throwable) {
        if (IS_DEBUG) {
            Log.w(tag, getLogPoi());
            Log.w(tag, str);
            Log.w(tag, str, throwable);
        }
    }

    public static void e(Exception ex) {
        if (true) {
            StringBuffer sb = new StringBuffer();
            String name = getLogPoi();
            StackTraceElement[] sts = ex.getStackTrace();

            if (name != null) {
                sb.append(name + ex + "\r\n");
            } else {
                sb.append(ex + "\r\n");
            }

            if (sts != null && sts.length > 0) {
                for (StackTraceElement st : sts) {
                    if (st != null) {
                        sb.append("[ " + st.getFileName() + ":" + st.getLineNumber() + " ]\r\n");
                    }
                }
            }

            Log.e(TAG, sb.toString());
        }
    }

    public static void e(String str) {
        if (IS_DEBUG) {
            Log.e(TAG, getLogPoi());
            Log.e(TAG, str);
        }
    }

    public static void e(String tag, String str) {
        if (IS_DEBUG) {
            Log.e(tag, getLogPoi());
            Log.e(tag, str);
        }
    }

    public static void e(String tag, String str, Throwable throwable) {
        if (IS_DEBUG) {
            Log.e(tag, getLogPoi());
            Log.e(tag, str);
            Log.e(tag, str, throwable);
        }
    }

    public static void o(Object o) {
        if (IS_DEBUG) {
            Log.i(TAG, getLogPoi());
            Log.i(TAG, getObjString(o));
        }
    }

    public static void c(Collection<?> c) {
        if (IS_DEBUG) {
            Log.i(TAG, getLogPoi());
            Log.i(TAG, getCollectionString(c));
        }
    }

    public static void m(Map<?, ?> m) {
        if (IS_DEBUG) {
            Log.i(TAG, getLogPoi());
            Log.i(TAG, getMapString(m));
        }
    }

    private static String getMapString(Map<?, ?> map) {
        if (map.size() == 0)
            return "";
        StringBuilder result = new StringBuilder("{\n");
        for (Map.Entry<?, ?> entity : map.entrySet()) {
            result.append("   " + getObjString(entity.getKey()) + "="
                    + getObjString(entity.getValue()));

            if (map.size() != 1)
                result.append("\n");
        }
        result.append("}");
        return result.toString();
    }

    private static String getCollectionString(Collection<?> c) {
        if (c.size() == 0)
            return "";
        Class<?> c1 = c.getClass();
        StringBuilder result = new StringBuilder("{\"" + c1.getName().substring(c1.getName().lastIndexOf(".") + 1)
                + "\"[");
        for (Object elem : c) {
            if (c.size() != 1)
                result.append("\n  ");
            result.append(getObjString(elem));
        }
        if (c.size() != 1)
            result.append("\n");
        result.append("]}");
        return result.toString();
    }

    public static String getObjString(Object obj) {

        if (obj == null)
            return "null";
        Class<?> cl = obj.getClass();
        if (cl == String.class)
            return (String) obj;
        if (cl.isArray()) {
            String returnString = "";
            returnString = cl.getComponentType() + "[]{";
            for (int i = 0; i < Array.getLength(obj); i++) {
                if (i > 0)
                    returnString += ",";
                Object val = Array.get(obj, i);
                if (cl.getComponentType().isPrimitive())
                    returnString += val;
            }
            return returnString;
        }

        String returnString = "{\"" + cl.getName().substring(cl.getName().lastIndexOf(".") + 1) + "\":";
        do {
            Field[] fields = cl.getDeclaredFields();
            AccessibleObject.setAccessible(fields, true);
            if (fields.length == 0)
                return returnString;

            returnString += "{";
            for (Field f : fields) {
                if (!Modifier.isStatic(f.getModifiers())) {
                    if (!returnString.endsWith("{"))
                        returnString += ", ";
                    returnString += f.getName() + "=";
                    try {
                        Class<?> t = f.getType();
                        Object val = f.get(obj);
                        if (t.isPrimitive())
                            returnString += val;
                        else if (t == String.class) {
                            returnString += getObjString(val);
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }
            returnString += "}}";
            cl = cl.getSuperclass();
        } while (cl != null);

        return returnString;
    }
}