package com.easemob.ext_sdk.dispatch;

import com.easemob.ext_sdk.common.ExtSdkCallback;
import com.easemob.ext_sdk.common.ExtSdkMethodType;
import com.hyphenate.EMChatRoomChangeListener;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMChatRoom;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCursorResult;
import com.hyphenate.chat.EMPageResult;
import com.hyphenate.exceptions.HyphenateException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ExtSdkChatRoomManagerWrapper extends ExtSdkWrapper {

    public static class SingleHolder { static ExtSdkChatRoomManagerWrapper instance = new ExtSdkChatRoomManagerWrapper(); }

    public static ExtSdkChatRoomManagerWrapper getInstance() { return ExtSdkChatRoomManagerWrapper.SingleHolder.instance; }

    ExtSdkChatRoomManagerWrapper() { registerEaseListener(); }

    public void joinChatRoom(JSONObject param, String channelName, ExtSdkCallback result) throws JSONException {
        String roomId = param.getString("roomId");
        EMClient.getInstance().chatroomManager().joinChatRoom(roomId, new EMValueCallBack<EMChatRoom>() {
            @Override
            public void onSuccess(EMChatRoom value) {
                ExtSdkWrapper.onSuccess(result, channelName, ExtSdkChatRoomHelper.toJson(value));
            }

            @Override
            public void onError(int error, String errorMsg) {
                ExtSdkWrapper.onError(result, error, errorMsg);
            }
        });
    }

    public void leaveChatRoom(JSONObject param, String channelName, ExtSdkCallback result) throws JSONException {
        String roomId = param.getString("roomId");
        EMClient.getInstance().chatroomManager().leaveChatRoom(roomId);
        onSuccess(result, channelName, true);
    }

    public void fetchPublicChatRoomsFromServer(JSONObject param, String channelName, ExtSdkCallback result)
        throws JSONException {
        int pageNum = param.getInt("pageNum");
        int pageSize = param.getInt("pageSize");
        EMClient.getInstance().chatroomManager().asyncFetchPublicChatRoomsFromServer(
            pageNum, pageSize, new EMValueCallBack<EMPageResult<EMChatRoom>>() {
                @Override
                public void onSuccess(EMPageResult<EMChatRoom> value) {
                    ExtSdkWrapper.onSuccess(result, channelName, ExtSdkPageResultHelper.toJson(value));
                }

                @Override
                public void onError(int error, String errorMsg) {
                    ExtSdkWrapper.onError(result, error, errorMsg);
                }
            });
    }

    public void fetchChatRoomInfoFromServer(JSONObject param, String channelName, ExtSdkCallback result)
        throws JSONException {
        String roomId = param.getString("roomId");
        boolean fetchMembers = param.getBoolean("fetchMembers");
        try {
            EMChatRoom room = null;
            if (fetchMembers) {
                room = EMClient.getInstance().chatroomManager().fetchChatRoomFromServer(roomId, true);
            } else {
                room = EMClient.getInstance().chatroomManager().fetchChatRoomFromServer(roomId);
            }

            onSuccess(result, channelName, ExtSdkChatRoomHelper.toJson(room));
        } catch (HyphenateException e) {
            onError(result, e, null);
        }
    }

    public void getChatRoom(JSONObject param, String channelName, ExtSdkCallback result) throws JSONException {
        String roomId = param.getString("roomId");
        EMChatRoom room = EMClient.getInstance().chatroomManager().getChatRoom(roomId);
        onSuccess(result, channelName, ExtSdkChatRoomHelper.toJson(room));
    }

    public void getAllChatRooms(JSONObject param, String channelName, ExtSdkCallback result) throws JSONException {
        List<EMChatRoom> list = EMClient.getInstance().chatroomManager().getAllChatRooms();
        List<Map<String, Object>> roomList = new ArrayList<>();
        for (EMChatRoom room : list) {
            roomList.add(ExtSdkChatRoomHelper.toJson(room));
        }
        onSuccess(result, channelName, roomList);
    }

    public void createChatRoom(JSONObject param, String channelName, ExtSdkCallback result) throws JSONException {
        String subject = param.getString("subject");
        String description = param.getString("desc");
        String welcomeMessage = param.getString("welcomeMsg");
        int maxUserCount = param.getInt("maxUserCount");
        JSONArray members = param.getJSONArray("members");
        List<String> membersList = new ArrayList<>();
        for (int i = 0; i < members.length(); i++) {
            membersList.add((String)members.get(i));
        }
        try {
            EMChatRoom room = EMClient.getInstance().chatroomManager().createChatRoom(
                subject, description, welcomeMessage, maxUserCount, membersList);
            onSuccess(result, channelName, ExtSdkChatRoomHelper.toJson(room));
        } catch (HyphenateException e) {
            onError(result, e, null);
        }
    }

    public void destroyChatRoom(JSONObject param, String channelName, ExtSdkCallback result) throws JSONException {
        String roomId = param.getString("roomId");
        try {
            EMClient.getInstance().chatroomManager().destroyChatRoom(roomId);
            onSuccess(result, channelName, true);
        } catch (HyphenateException e) {
            onError(result, e, null);
        }
    }

    public void changeChatRoomSubject(JSONObject param, String channelName, ExtSdkCallback result)
        throws JSONException {
        String roomId = param.getString("roomId");
        String subject = param.getString("subject");
        try {
            EMChatRoom room = EMClient.getInstance().chatroomManager().changeChatRoomSubject(roomId, subject);
            onSuccess(result, channelName, ExtSdkChatRoomHelper.toJson(room));
        } catch (HyphenateException e) {
            onError(result, e, null);
        }
    }

    public void changeChatRoomDescription(JSONObject param, String channelName, ExtSdkCallback result)
        throws JSONException {
        String roomId = param.getString("roomId");
        String description = param.getString("description");
        try {
            EMChatRoom room = EMClient.getInstance().chatroomManager().changeChatroomDescription(roomId, description);
            onSuccess(result, channelName, ExtSdkChatRoomHelper.toJson(room));
        } catch (HyphenateException e) {
            onError(result, e, null);
        }
    }

    public void fetchChatRoomMembers(JSONObject param, String channelName, ExtSdkCallback result) throws JSONException {
        String roomId = param.getString("roomId");
        String cursor = null;
        if (param.has("cursor")) {
            cursor = param.getString("cursor");
        }
        int pageSize = param.getInt("pageSize");
        try {
            EMCursorResult<String> cursorResult =
                EMClient.getInstance().chatroomManager().fetchChatRoomMembers(roomId, cursor, pageSize);
            onSuccess(result, channelName, ExtSdkCursorResultHelper.toJson(cursorResult));
        } catch (HyphenateException e) {
            onError(result, e, null);
        }
    }

    public void muteChatRoomMembers(JSONObject param, String channelName, ExtSdkCallback result) throws JSONException {
        String roomId = param.getString("roomId");
        long duration = Long.parseLong(param.getString("duration"));
        JSONArray muteMembers = param.getJSONArray("muteMembers");
        List<String> muteMembersList = new ArrayList<>();
        for (int i = 0; i < muteMembers.length(); i++) {
            muteMembersList.add((String)muteMembers.get(i));
        }
        try {
            EMChatRoom room =
                EMClient.getInstance().chatroomManager().muteChatRoomMembers(roomId, muteMembersList, duration);
            onSuccess(result, channelName, ExtSdkChatRoomHelper.toJson(room));
        } catch (HyphenateException e) {
            onError(result, e, null);
        }
    }

    public void unMuteChatRoomMembers(JSONObject param, String channelName, ExtSdkCallback result)
        throws JSONException {
        String roomId = param.getString("roomId");
        JSONArray muteMembers = param.getJSONArray("unMuteMembers");
        List<String> unMuteMembersList = new ArrayList<>();
        for (int i = 0; i < muteMembers.length(); i++) {
            unMuteMembersList.add((String)muteMembers.get(i));
        }
        try {
            EMChatRoom room = EMClient.getInstance().chatroomManager().unMuteChatRoomMembers(roomId, unMuteMembersList);
            onSuccess(result, channelName, ExtSdkChatRoomHelper.toJson(room));
        } catch (HyphenateException e) {
            onError(result, e, null);
        }
    }

    public void changeChatRoomOwner(JSONObject param, String channelName, ExtSdkCallback result) throws JSONException {
        String roomId = param.getString("roomId");
        String newOwner = param.getString("newOwner");
        try {
            EMChatRoom room = EMClient.getInstance().chatroomManager().changeOwner(roomId, newOwner);
            onSuccess(result, channelName, ExtSdkChatRoomHelper.toJson(room));
        } catch (HyphenateException e) {
            onError(result, e, null);
        }
    }

    public void addChatRoomAdmin(JSONObject param, String channelName, ExtSdkCallback result) throws JSONException {
        String roomId = param.getString("roomId");
        String admin = param.getString("admin");
        try {
            EMChatRoom room = EMClient.getInstance().chatroomManager().addChatRoomAdmin(roomId, admin);
            onSuccess(result, channelName, ExtSdkChatRoomHelper.toJson(room));
        } catch (HyphenateException e) {
            onError(result, e, null);
        }
    }

    public void removeChatRoomAdmin(JSONObject param, String channelName, ExtSdkCallback result) throws JSONException {
        String roomId = param.getString("roomId");
        String admin = param.getString("admin");
        try {
            EMChatRoom room = EMClient.getInstance().chatroomManager().removeChatRoomAdmin(roomId, admin);
            onSuccess(result, channelName, ExtSdkChatRoomHelper.toJson(room));
        } catch (HyphenateException e) {
            onError(result, e, null);
        }
    }

    public void fetchChatRoomMuteList(JSONObject param, String channelName, ExtSdkCallback result)
        throws JSONException {
        String roomId = param.getString("roomId");
        int pageNum = param.getInt("pageNum");
        int pageSize = param.getInt("pageSize");
        try {
            Map map = EMClient.getInstance().chatroomManager().fetchChatRoomMuteList(roomId, pageNum, pageSize);
            onSuccess(result, channelName, map);
        } catch (HyphenateException e) {
            onError(result, e, null);
        }
    }

    public void removeChatRoomMembers(JSONObject param, String channelName, ExtSdkCallback result)
        throws JSONException {
        String roomId = param.getString("roomId");
        JSONArray members = param.getJSONArray("members");
        List<String> membersList = new ArrayList<>();
        for (int i = 0; i < members.length(); i++) {
            membersList.add((String)members.get(i));
        }
        try {
            EMChatRoom room = EMClient.getInstance().chatroomManager().removeChatRoomMembers(roomId, membersList);
            onSuccess(result, channelName, ExtSdkChatRoomHelper.toJson(room));
        } catch (HyphenateException e) {
            onError(result, e, null);
        }
    }

    public void blockChatRoomMembers(JSONObject param, String channelName, ExtSdkCallback result) throws JSONException {
        String roomId = param.getString("roomId");
        JSONArray blockMembers = param.getJSONArray("members");
        List<String> blockMembersList = new ArrayList<>();
        for (int i = 0; i < blockMembers.length(); i++) {
            blockMembersList.add((String)blockMembers.get(i));
        }
        try {
            EMChatRoom room = EMClient.getInstance().chatroomManager().blockChatroomMembers(roomId, blockMembersList);
            onSuccess(result, channelName, ExtSdkChatRoomHelper.toJson(room));
        } catch (HyphenateException e) {
            onError(result, e, null);
        }
    }

    public void unBlockChatRoomMembers(JSONObject param, String channelName, ExtSdkCallback result)
        throws JSONException {
        String roomId = param.getString("roomId");
        JSONArray blockMembers = param.getJSONArray("members");
        List<String> blockMembersList = new ArrayList<>();
        for (int i = 0; i < blockMembers.length(); i++) {
            blockMembersList.add((String)blockMembers.get(i));
        }
        try {
            EMChatRoom room = EMClient.getInstance().chatroomManager().unblockChatRoomMembers(roomId, blockMembersList);
            onSuccess(result, channelName, ExtSdkChatRoomHelper.toJson(room));
        } catch (HyphenateException e) {
            onError(result, e, null);
        }
    }

    public void fetchChatRoomBlockList(JSONObject param, String channelName, ExtSdkCallback result)
        throws JSONException {
        String roomId = param.getString("roomId");
        int pageNum = param.getInt("pageNum");
        int pageSize = param.getInt("pageSize");
        try {
            List<String> blockList =
                EMClient.getInstance().chatroomManager().fetchChatRoomBlackList(roomId, pageNum, pageSize);
            onSuccess(result, channelName, blockList);
        } catch (HyphenateException e) {
            onError(result, e, null);
        }
    }

    public void updateChatRoomAnnouncement(JSONObject param, String channelName, ExtSdkCallback result)
        throws JSONException {
        String roomId = param.getString("roomId");
        String announcement = param.getString("announcement");
        try {
            EMClient.getInstance().chatroomManager().updateChatRoomAnnouncement(roomId, announcement);
            onSuccess(result, channelName, true);
        } catch (HyphenateException e) {
            onError(result, e, null);
        }
    }

    public void fetchChatRoomAnnouncement(JSONObject param, String channelName, ExtSdkCallback result)
        throws JSONException {
        String roomId = param.getString("roomId");
        try {
            String announcement = EMClient.getInstance().chatroomManager().fetchChatRoomAnnouncement(roomId);
            onSuccess(result, channelName, announcement);
        } catch (HyphenateException e) {
            onError(result, e, null);
        }
    }

    public void addMembersToChatRoomWhiteList(JSONObject param, String channelName, ExtSdkCallback result)
        throws JSONException {
        String roomId = param.getString("roomId");
        JSONArray jsonAry = param.getJSONArray("members");
        List<String> members = new ArrayList<>();
        for (int i = 0; i < jsonAry.length(); i++) {
            members.add((String)jsonAry.get(i));
        }

        EMClient.getInstance().chatroomManager().addToChatRoomWhiteList(
            roomId, members, new EMValueCallBack<EMChatRoom>() {
                @Override
                public void onSuccess(EMChatRoom value) {
                    ExtSdkWrapper.onSuccess(result, channelName, ExtSdkChatRoomHelper.toJson(value));
                }

                @Override
                public void onError(int error, String errorMsg) {
                    ExtSdkWrapper.onError(result, error, errorMsg);
                }
            });
    }

    public void removeMembersFromChatRoomWhiteList(JSONObject param, String channelName, ExtSdkCallback result)
        throws JSONException {
        String roomId = param.getString("roomId");
        JSONArray jsonAry = param.getJSONArray("members");
        List<String> members = new ArrayList<>();
        for (int i = 0; i < jsonAry.length(); i++) {
            members.add((String)jsonAry.get(i));
        }

        EMClient.getInstance().chatroomManager().removeFromChatRoomWhiteList(
            roomId, members, new EMValueCallBack<EMChatRoom>() {
                @Override
                public void onSuccess(EMChatRoom value) {
                    ExtSdkWrapper.onSuccess(result, channelName, ExtSdkChatRoomHelper.toJson(value));
                }

                @Override
                public void onError(int error, String errorMsg) {
                    ExtSdkWrapper.onError(result, error, errorMsg);
                }
            });
    }

    public void isMemberInChatRoomWhiteListFromServer(JSONObject param, String channelName, ExtSdkCallback result)
        throws JSONException {
        String roomId = param.getString("roomId");
        EMClient.getInstance().chatroomManager().checkIfInChatRoomWhiteList(roomId, new EMValueCallBack<Boolean>() {
            @Override
            public void onSuccess(Boolean value) {
                ExtSdkWrapper.onSuccess(result, channelName, value);
            }

            @Override
            public void onError(int error, String errorMsg) {
                ExtSdkWrapper.onError(result, error, errorMsg);
            }
        });
    }

    public void fetchChatRoomWhiteListFromServer(JSONObject param, String channelName, ExtSdkCallback result)
        throws JSONException {
        String roomId = param.getString("roomId");
        EMClient.getInstance().chatroomManager().fetchChatRoomWhiteList(roomId, new EMValueCallBack<List<String>>() {
            @Override
            public void onSuccess(List<String> value) {
                ExtSdkWrapper.onSuccess(result, channelName, value);
            }

            @Override
            public void onError(int error, String errorMsg) {
                ExtSdkWrapper.onError(result, error, errorMsg);
            }
        });
    }

    public void muteAllChatRoomMembers(JSONObject param, String channelName, ExtSdkCallback result)
        throws JSONException {
        String roomId = param.getString("roomId");

        EMClient.getInstance().chatroomManager().muteAllMembers(roomId, new EMValueCallBack<EMChatRoom>() {
            @Override
            public void onSuccess(EMChatRoom value) {
                ExtSdkWrapper.onSuccess(result, channelName, ExtSdkChatRoomHelper.toJson(value));
            }

            @Override
            public void onError(int error, String errorMsg) {
                ExtSdkWrapper.onError(result, error, errorMsg);
            }
        });
    }

    public void unMuteAllChatRoomMembers(JSONObject param, String channelName, ExtSdkCallback result)
        throws JSONException {
        String roomId = param.getString("roomId");

        EMClient.getInstance().chatroomManager().unmuteAllMembers(roomId, new EMValueCallBack<EMChatRoom>() {
            @Override
            public void onSuccess(EMChatRoom value) {
                ExtSdkWrapper.onSuccess(result, channelName, null);
            }

            @Override
            public void onError(int error, String errorMsg) {
                ExtSdkWrapper.onError(result, error, errorMsg);
            }
        });
    }

    private void registerEaseListener() {
        EMClient.getInstance().chatroomManager().addChatRoomChangeListener(new EMChatRoomChangeListener() {
            @Override
            public void onWhiteListAdded(String chatRoomId, List<String> whitelist) {
                Map<String, Object> data = new HashMap<>();
                data.put("roomId", chatRoomId);
                data.put("whitelist", whitelist);
                data.put("type", "onWhiteListAdded");
                ExtSdkWrapper.onReceive(ExtSdkMethodType.chatRoomChange, data);
            }

            @Override
            public void onWhiteListRemoved(String chatRoomId, List<String> whitelist) {
                Map<String, Object> data = new HashMap<>();
                data.put("roomId", chatRoomId);
                data.put("whitelist", whitelist);
                data.put("type", "onWhiteListRemoved");
                ExtSdkWrapper.onReceive(ExtSdkMethodType.chatRoomChange, data);
            }

            @Override
            public void onAllMemberMuteStateChanged(String chatRoomId, boolean isMuted) {
                Map<String, Object> data = new HashMap<>();
                data.put("roomId", chatRoomId);
                data.put("isMuted", isMuted);
                data.put("type", "onAllMemberMuteStateChanged");
                ExtSdkWrapper.onReceive(ExtSdkMethodType.chatRoomChange, data);
            }

            @Override
            public void onChatRoomDestroyed(String roomId, String roomName) {
                Map<String, Object> data = new HashMap<>();
                data.put("roomId", roomId);
                data.put("roomName", roomName);
                data.put("type", "onChatRoomDestroyed");
                ExtSdkWrapper.onReceive(ExtSdkMethodType.chatRoomChange, data);
            }

            @Override
            public void onMemberJoined(String roomId, String participant) {
                Map<String, Object> data = new HashMap<>();
                data.put("roomId", roomId);
                data.put("participant", participant);
                data.put("type", "onMemberJoined");
                ExtSdkWrapper.onReceive(ExtSdkMethodType.chatRoomChange, data);
            }

            @Override
            public void onMemberExited(String roomId, String roomName, String participant) {
                Map<String, Object> data = new HashMap<>();
                data.put("roomId", roomId);
                data.put("roomName", roomName);
                data.put("participant", participant);
                data.put("type", "onMemberExited");
                ExtSdkWrapper.onReceive(ExtSdkMethodType.chatRoomChange, data);
            }

            @Override
            public void onRemovedFromChatRoom(int reason, String roomId, String roomName, String participant) {
                Map<String, Object> data = new HashMap<>();
                data.put("roomId", roomId);
                data.put("roomName", roomName);
                data.put("participant", participant);
                data.put("type", "onRemovedFromChatRoom");
                ExtSdkWrapper.onReceive(ExtSdkMethodType.chatRoomChange, data);
            }

            @Override
            public void onMuteListAdded(String chatRoomId, List<String> mutes, long expireTime) {
                Map<String, Object> data = new HashMap<>();
                data.put("roomId", chatRoomId);
                data.put("mutes", mutes);
                data.put("expireTime", String.valueOf(expireTime));
                data.put("type", "onMuteListAdded");
                ExtSdkWrapper.onReceive(ExtSdkMethodType.chatRoomChange, data);
            }

            @Override
            public void onMuteListRemoved(String chatRoomId, List<String> mutes) {
                Map<String, Object> data = new HashMap<>();
                data.put("roomId", chatRoomId);
                data.put("mutes", mutes);
                data.put("type", "onMuteListRemoved");
                ExtSdkWrapper.onReceive(ExtSdkMethodType.chatRoomChange, data);
            }

            @Override
            public void onAdminAdded(String chatRoomId, String admin) {
                Map<String, Object> data = new HashMap<>();
                data.put("roomId", chatRoomId);
                data.put("admin", admin);
                data.put("type", "onAdminAdded");
                ExtSdkWrapper.onReceive(ExtSdkMethodType.chatRoomChange, data);
            }

            @Override
            public void onAdminRemoved(String chatRoomId, String admin) {
                Map<String, Object> data = new HashMap<>();
                data.put("roomId", chatRoomId);
                data.put("admin", admin);
                data.put("type", "onAdminRemoved");
                ExtSdkWrapper.onReceive(ExtSdkMethodType.chatRoomChange, data);
            }

            @Override
            public void onOwnerChanged(String chatRoomId, String newOwner, String oldOwner) {
                Map<String, Object> data = new HashMap<>();
                data.put("roomId", chatRoomId);
                data.put("newOwner", newOwner);
                data.put("oldOwner", oldOwner);
                data.put("chatRoomChange", "onOwnerChanged");
                ExtSdkWrapper.onReceive(ExtSdkMethodType.chatRoomChange, data);
            }

            @Override
            public void onAnnouncementChanged(String chatRoomId, String announcement) {
                Map<String, Object> data = new HashMap<>();
                data.put("roomId", chatRoomId);
                data.put("announcement", announcement);
                data.put("chatRoomChange", "onAnnouncementChanged");
                ExtSdkWrapper.onReceive(ExtSdkMethodType.chatRoomChange, data);
            }
        });
    }
}
