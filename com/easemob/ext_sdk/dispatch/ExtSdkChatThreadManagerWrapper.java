package com.easemob.ext_sdk.dispatch;

import com.easemob.ext_sdk.common.ExtSdkCallback;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMClient;
import org.json.JSONException;
import org.json.JSONObject;

public class ExtSdkChatThreadManagerWrapper {
    public static class SingleHolder {
        static ExtSdkChatThreadManagerWrapper instance = new ExtSdkChatThreadManagerWrapper();
    }

    public static ExtSdkChatThreadManagerWrapper getInstance() {
        return ExtSdkChatThreadManagerWrapper.SingleHolder.instance;
    }

    ExtSdkChatThreadManagerWrapper() { registerEaseListener(); }

    private void registerEaseListener() {}

    public void fetchChatThread(JSONObject param, String channelName, ExtSdkCallback result) throws JSONException {
        // TODO:
    }

    public void fetchChatThreadDetail(JSONObject param, String channelName, ExtSdkCallback result)
        throws JSONException {
        // TODO:
    }
    public void fetchJoinedChatThreads(JSONObject param, String channelName, ExtSdkCallback result)
        throws JSONException {
        // TODO:
    }
    public void fetchChatThreadsWithParentId(JSONObject param, String channelName, ExtSdkCallback result)
        throws JSONException {
        // TODO:
    }
    public void fetchChatThreadMember(JSONObject param, String channelName, ExtSdkCallback result)
        throws JSONException {
        // TODO:
    }
    public void fetchLastMessageWithChatThreads(JSONObject param, String channelName, ExtSdkCallback result)
        throws JSONException {
        // TODO:
    }
    public void removeMemberFromChatThread(JSONObject param, String channelName, ExtSdkCallback result)
        throws JSONException {
        // TODO:
    }
    public void updateChatThreadSubject(JSONObject param, String channelName, ExtSdkCallback result)
        throws JSONException {
        // TODO:
    }
    public void createChatThread(JSONObject param, String channelName, ExtSdkCallback result) throws JSONException {
        // TODO:
    }
    public void joinChatThread(JSONObject param, String channelName, ExtSdkCallback result) throws JSONException {
        // TODO:
    }
    public void leaveChatThread(JSONObject param, String channelName, ExtSdkCallback result) throws JSONException {
        // TODO:
    }
    public void destroyChatThread(JSONObject param, String channelName, ExtSdkCallback result) throws JSONException {
        // TODO:
    }
}
