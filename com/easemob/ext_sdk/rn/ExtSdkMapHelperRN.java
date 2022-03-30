package com.easemob.ext_sdk.rn;

import android.os.Build;

import androidx.annotation.RequiresApi;

import com.facebook.react.bridge.Arguments;
import com.facebook.react.bridge.ReadableMap;
import com.facebook.react.bridge.WritableMap;
import com.facebook.react.bridge.WritableNativeArray;
import com.facebook.react.bridge.WritableNativeMap;

import java.util.Iterator;
import java.util.Map;

public class ExtSdkMapHelperRN {

    public void toWritableMap(Map<String, Object> data, WritableMap result) {
        ++safeCount;
        if (MAX_COUNT < safeCount) {
            throw new RuntimeException("Too many recursions. " + safeCount);
        }
        Iterator<Map.Entry<String, Object>> iterator = data.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry<String, Object> entry = iterator.next();
            String key = entry.getKey();
            Object value = entry.getValue();
            if (value == null) {
                result.putNull(key);
                continue;
            }
            Class valueClass = value.getClass();
            if (valueClass == Boolean.class) {
                result.putBoolean(key, ((Boolean) value).booleanValue());
            } else if (valueClass == Integer.class) {
                result.putDouble(key, ((Integer) value).doubleValue());
            } else if (valueClass == Double.class) {
                result.putDouble(key, ((Double) value).doubleValue());
            } else if (valueClass == Float.class) {
                result.putDouble(key, ((Float) value).doubleValue());
            } else if (valueClass == String.class) {
                result.putString(key, value.toString());
            } else if (value instanceof Map) {
                WritableMap m = Arguments.createMap();
                toWritableMap((Map<String, Object>) value, m);
                result.putMap(key, (WritableNativeMap) m);
            } else if (value instanceof Object[]) {
                WritableNativeArray a = Arguments.fromJavaArgs(null);
                toWritableArray((Object[]) value, a);
                result.putArray(key, (WritableNativeArray) a);
            } else {
                throw new RuntimeException("Cannot convert argument of type " + value);
            }
        }
    }

    protected void toWritableArray(Object[] data, WritableNativeArray result) {
        ++safeCount;
        if (MAX_COUNT < safeCount) {
            throw new RuntimeException("Too many recursions. " + safeCount);
        }
        for (int i = 0; i < data.length; i++) {
            Object value = data[i];
            if (value == null) {
                result.pushNull();
                continue;
            }
            Class valueClass = value.getClass();
            if (valueClass == Boolean.class) {
                result.pushBoolean(((Boolean) value).booleanValue());
            } else if (valueClass == Integer.class) {
                result.pushDouble(((Integer) value).doubleValue());
            } else if (valueClass == Double.class) {
                result.pushDouble(((Double) value).doubleValue());
            } else if (valueClass == Float.class) {
                result.pushDouble(((Float) value).doubleValue());
            } else if (valueClass == String.class) {
                result.pushString(value.toString());
            } else if (value instanceof Map) {
                WritableMap m = Arguments.createMap();
                toWritableMap((Map<String, Object>) value, m);
                result.pushMap((WritableNativeMap) m);
            } else if (value instanceof Object[]) {
                WritableNativeArray a = Arguments.fromJavaArgs(null);
                toWritableArray((Object[]) value, a);
                result.pushArray((WritableNativeArray) a);
            } else {
                throw new RuntimeException("Cannot convert argument of type " + valueClass);
            }
        }
    }

    private int safeCount = 0;
    private static final int MAX_COUNT = 50;
}
