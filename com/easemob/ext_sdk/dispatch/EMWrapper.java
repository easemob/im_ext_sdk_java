package com.easemob.ext_sdk.dispatch;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.easemob.ext_sdk.common.ExtSdkCallback;
import com.easemob.ext_sdk.common.ExtSdkThreadUtil;
import com.hyphenate.exceptions.HyphenateException;

import org.json.JSONException;

import java.util.HashMap;
import java.util.Map;

public class EMWrapper {

    protected static void onSuccess(@NonNull ExtSdkCallback callback, @NonNull String methodType, @Nullable Object object) {
        Log.d(TAG, "onSuccess: " + methodType + ": " + (object != null ? object : ""));
        Map<String, Object> data = new HashMap<>();
        if (object != null) {
            data.put(methodType, object);
        }
        callback.success(data);
    }

    public static void onError(@NonNull ExtSdkCallback callback, @NonNull Object e, @Nullable Object ext) {
        Log.d(TAG, "onError: " + (e != null ? e : "") + ": " + (ext != null ? ext : ""));
        Map<String, Object> data = new HashMap<>();
        if (e instanceof HyphenateException) {
            data.put("error", HyphenateExceptionHelper.toJson((HyphenateException)e));
        } else if (e instanceof Integer) {
            data.put("error", EMErrorHelper.toJson((int)e, ext.toString()));
        } else if (e instanceof JSONException) {
            data.put("error", JSONExceptionHelper.toJson((JSONException)e));
        } else {
            data.put("error", "no implement");
        }
        callback.success(data);
    }

    protected static void onReceive(@NonNull String methodType, @Nullable Object data) {
        Log.d(TAG, "onReceive: " + methodType + ": " + (data != null ? data : ""));
        ExtSdkDispatch.getInstance().onReceive(methodType, data);
    }

    private static final String TAG = "EMWrapper";
}
