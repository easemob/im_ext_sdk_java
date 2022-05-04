package com.easemob.ext_sdk.dispatch;

import com.easemob.ext_sdk.common.ExtSdkCallback;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMPushConfigs;
import com.hyphenate.chat.EMPushManager;
import com.hyphenate.exceptions.HyphenateException;
import java.util.ArrayList;
import java.util.List;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ExtSdkPushManagerWrapper extends ExtSdkWrapper {

    public static class SingleHolder { static ExtSdkPushManagerWrapper instance = new ExtSdkPushManagerWrapper(); }

    public static ExtSdkPushManagerWrapper getInstance() { return ExtSdkPushManagerWrapper.SingleHolder.instance; }

    public void getImPushConfig(JSONObject params, String channelName, ExtSdkCallback result) throws JSONException {
        EMPushConfigs configs = EMClient.getInstance().pushManager().getPushConfigs();
        onSuccess(result, channelName, ExtSdkPushConfigsHelper.toJson(configs));
    }

    public void getImPushConfigFromServer(JSONObject params, String channelName, ExtSdkCallback result)
        throws JSONException {
        try {
            EMPushConfigs configs = EMClient.getInstance().pushManager().getPushConfigsFromServer();
            onSuccess(result, channelName, ExtSdkPushConfigsHelper.toJson(configs));
        } catch (HyphenateException e) {
            ExtSdkWrapper.onError(result, e, null);
        }
    }

    public void updatePushNickname(JSONObject params, String channelName, ExtSdkCallback result) throws JSONException {
        String nickname = params.getString("nickname");
        try {
            EMClient.getInstance().pushManager().updatePushNickname(nickname);
            onSuccess(result, channelName, nickname);
        } catch (HyphenateException e) {
            ExtSdkWrapper.onError(result, e, null);
        }
    }

    public void enableOfflinePush(JSONObject params, String channelName, ExtSdkCallback result) throws JSONException {
        try {
            EMClient.getInstance().pushManager().enableOfflinePush();
            onSuccess(result, channelName, null);
        } catch (HyphenateException e) {
            ExtSdkWrapper.onError(result, e, null);
        }
    }

    public void disableOfflinePush(JSONObject params, String channelName, ExtSdkCallback result) throws JSONException {
        int startTime = params.getInt("start");
        int endTime = params.getInt("end");
        try {
            EMClient.getInstance().pushManager().disableOfflinePush(startTime, endTime);
            onSuccess(result, channelName, null);
        } catch (HyphenateException e) {
            ExtSdkWrapper.onError(result, e, null);
        }
    }

    public void getNoPushGroups(JSONObject params, String channelName, ExtSdkCallback result) throws JSONException {
        List<String> groups = EMClient.getInstance().pushManager().getNoPushGroups();
        onSuccess(result, channelName, groups);
    }

    public void getNoPushUsers(JSONObject params, String channelName, ExtSdkCallback result) throws JSONException {
        List<String> list = EMClient.getInstance().pushManager().getNoPushUsers();
        onSuccess(result, channelName, list);
    }

    public void updateImPushStyle(JSONObject params, String channelName, ExtSdkCallback result) throws JSONException {
        EMPushManager.DisplayStyle style = params.getInt("pushStyle") == 0 ? EMPushManager.DisplayStyle.SimpleBanner
                                                                           : EMPushManager.DisplayStyle.MessageSummary;
        EMClient.getInstance().pushManager().asyncUpdatePushDisplayStyle(style, new EMCallBack() {
            @Override
            public void onSuccess() {
                ExtSdkWrapper.onSuccess(result, channelName, true);
            }

            @Override
            public void onError(int code, String error) {
                ExtSdkWrapper.onError(result, code, error);
            }
        });
    }

    public void updateGroupPushService(JSONObject params, String channelName, ExtSdkCallback result)
        throws JSONException {
        JSONArray groupIds = params.getJSONArray("group_ids");
        boolean noPush = params.getBoolean("noPush");

        List<String> groupList = new ArrayList<>();
        for (int i = 0; i < groupIds.length(); i++) {
            String groupId = groupIds.getString(i);
            groupList.add(groupId);
        }
        try {
            EMClient.getInstance().pushManager().updatePushServiceForGroup(groupList, noPush);
            onSuccess(result, channelName, null);
        } catch (HyphenateException e) {
            ExtSdkWrapper.onError(result, e, null);
        }
    }

    public void updateUserPushService(JSONObject params, String channelName, ExtSdkCallback result)
        throws JSONException {
        JSONArray groupIds = params.getJSONArray("user_ids");
        boolean noPush = params.getBoolean("noPush");

        List<String> userList = new ArrayList<>();
        for (int i = 0; i < groupIds.length(); i++) {
            String userId = groupIds.getString(i);
            userList.add(userId);
        }
        try {
            EMClient.getInstance().pushManager().updatePushServiceForUsers(userList, noPush);
            onSuccess(result, channelName, null);
        } catch (HyphenateException e) {
            ExtSdkWrapper.onError(result, e, null);
        }
    }

    public void updateHMSPushToken(JSONObject params, String channelName, ExtSdkCallback result) throws JSONException {
        String token = params.getString("token");
        EMClient.getInstance().sendHMSPushTokenToServer(token);
        onSuccess(result, channelName, token);
    }

    public void updateFCMPushToken(JSONObject params, String channelName, ExtSdkCallback result) throws JSONException {
        String token = params.getString("token");
        EMClient.getInstance().sendFCMTokenToServer(token);
        onSuccess(result, channelName, token);
    }
}
