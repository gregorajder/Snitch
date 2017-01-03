package com.gregorajdergmail.snitch.util;

import android.text.TextUtils;

public final class Log {
    static String TAG = "fewf";

    public static void d(String tag, String msg) {
        android.util.Log.d(tag, Thread.currentThread().getName() + " " + getLocation() + " " + msg);
    }

    public static void d(String msg) {
        android.util.Log.d(TAG, Thread.currentThread().getName() + " " +  getLocation() + " " + msg);
    }

    public static void d() {
        android.util.Log.d(TAG, Thread.currentThread().getName() + " " +  getLocation());
    }

    private static String getLocation() {
        final String className = Log.class.getName();
        final StackTraceElement[] traces = Thread.currentThread().getStackTrace();
        boolean found = false;
        for (int i = 0; i < traces.length; i++) {
            StackTraceElement trace = traces[i];
            try {
                if (found) {
                    if (!trace.getClassName().startsWith(className)) {
                        Class<?> clazz = Class.forName(trace.getClassName());
                        return "[" + getClassName(clazz) + ":" + trace.getMethodName() + ":" + trace.getLineNumber() + "]: ";
                    }
                } else if (trace.getClassName().startsWith(className)) {
                    found = true;
                    continue;
                }
            } catch (ClassNotFoundException e) {
            }
        }
        return "[]: ";
    }

    private static String getClassName(Class<?> clazz) {
        if (clazz != null) {
            if (!TextUtils.isEmpty(clazz.getSimpleName())) {
                return clazz.getSimpleName();
            }
            return getClassName(clazz.getEnclosingClass());
        }
        return "";
    }
}

