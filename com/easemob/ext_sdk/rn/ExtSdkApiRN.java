package com.easemob.ext_sdk.rn;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.easemob.ext_sdk.common.ExtSdkApi;
import com.easemob.ext_sdk.common.ExtSdkCallback;
import com.easemob.ext_sdk.common.ExtSdkListener;
import com.easemob.ext_sdk.common.ExtSdkThreadUtil;
import com.easemob.ext_sdk.jni.ExtSdkApiJni;
import com.facebook.react.module.annotations.ReactModule;
import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;
import com.facebook.react.modules.core.DeviceEventManagerModule;


@ReactModule(name = ExtSdkApiRN.NAME)
public class ExtSdkApiRN extends ReactContextBaseJavaModule implements ExtSdkApi {
    private static final String TAG = "ExtSdkApiRN";
    public static final String NAME = "ExtSdkApiRN";
    private ReactApplicationContext reactContext;
    private DeviceEventManagerModule.RCTDeviceEventEmitter eventEmitter;

    public ExtSdkApiRN(ReactApplicationContext reactContext) {
        super(reactContext);
        Log.d(TAG, "ExtSdkApiRN: ");
        this.reactContext = reactContext;
    }

    @Override
    public void initialize() {
        Log.d(TAG, "initialize: ");
        super.initialize();
        eventEmitter = reactContext.getJSModule(DeviceEventManagerModule.RCTDeviceEventEmitter.class);
        this.init(new Object());
    }

    @Override
    public void invalidate() {
        Log.d(TAG, "invalidate: ");
        super.invalidate();
        this.unInit(null);
    }

    @Override
    @NonNull
    public String getName() {
        return NAME;
    }

    @Override
    public void init(@NonNull Object config) {
        Log.d(TAG, "init: " + config);
        ExtSdkApiJni.nativeInit(config);
        this.addListener(new ExtSdkListener() {
            @Override
            public void onReceive(@NonNull String methodType, @Nullable Object data) {
                ExtSdkThreadUtil.mainThreadExecute(() -> {
                    eventEmitter.emit(methodType, data);
                });
            }

            @Override
            public void setType(@NonNull String listenerType) {

            }

            @Override
            public void getType() {

            }
        });
    }

    @Override
    public void unInit(@Nullable Object params) {
        Log.d(TAG, "unInit: " + params);
        ExtSdkApiJni.nativeUnInit();
    }

    @Override
    public void addListener(@NonNull ExtSdkListener listener) {
        Log.d(TAG, "addListener: " + listener);
        ExtSdkApiJni.nativeAddListener(listener);
    }

    @Override
    public void delListener(@NonNull ExtSdkListener listener) {
        Log.d(TAG, "delListener: " + listener);
        ExtSdkApiJni.nativeDelListener(listener);
    }

    @Override
    public void callSdkApi(@NonNull String methodType, @Nullable Object params, @NonNull ExtSdkCallback callback) {
        ExtSdkApiJni.nativeCallSdkApi(methodType, params, callback);
    }

    @ReactMethod
    public void callSdkApiRN(String methodType, Object params, Promise promise) {
        Log.d(TAG, "callSdkApiRN: " + methodType + ": " + (params != null ? params.toString() : ""));
        ExtSdkThreadUtil.asyncExecute(() -> {
            this.callSdkApi(methodType, params, new ExtSdkCallback() {
                @Override
                public void success(@Nullable Object data) {
                    ExtSdkThreadUtil.mainThreadExecute(() -> {
                        promise.resolve(data);
                    });
                }

                @Override
                public void fail(@NonNull int code, @Nullable Object ext) {
                    ExtSdkThreadUtil.mainThreadExecute(() -> {
                        // todo: errorMessage: 是code的字符串形式
                        promise.reject(String.valueOf(code), "");
                    });
                }
            });
        });
    }
}
