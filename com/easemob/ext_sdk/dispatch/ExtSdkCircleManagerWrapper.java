package com.easemob.ext_sdk.dispatch;

import com.easemob.ext_sdk.common.ExtSdkCallback;
import com.easemob.ext_sdk.common.ExtSdkMethodType;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMCircleChannelListener;
import com.hyphenate.EMCircleServerListener;
import com.hyphenate.EMValueCallBack;
import com.hyphenate.chat.EMCircleChannel;
import com.hyphenate.chat.EMCircleChannelAttribute;
import com.hyphenate.chat.EMCircleChannelInviteInfo;
import com.hyphenate.chat.EMCircleChannelStyle;
import com.hyphenate.chat.EMCircleServer;
import com.hyphenate.chat.EMCircleServerAttribute;
import com.hyphenate.chat.EMCircleServerEvent;
import com.hyphenate.chat.EMCircleTag;
import com.hyphenate.chat.EMCircleUser;
import com.hyphenate.chat.EMCircleUserRole;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMCursorResult;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class ExtSdkCircleManagerWrapper extends ExtSdkWrapper {
    public static class SingleHolder { static ExtSdkCircleManagerWrapper instance = new ExtSdkCircleManagerWrapper(); }

    public static ExtSdkCircleManagerWrapper getInstance() { return ExtSdkCircleManagerWrapper.SingleHolder.instance; }

    private EMCircleServerListener serverListener = null;
    private EMCircleChannelListener channelListener = null;

    ExtSdkCircleManagerWrapper() { registerEaseListener(); }

    private void registerEaseListener() {
        if (this.channelListener != null) {
            EMClient.getInstance().chatCircleManager().removeChannelListener(this.channelListener);
        }
        this.channelListener = new EMCircleChannelListener() {
            @Override
            public void onChannelCreated(String serverId, String channelId, String creator) {
                Map<String, Object> data = new HashMap<>();
                data.put("serverId", serverId);
                data.put("channelId", channelId);
                data.put("creator", creator);
                ExtSdkWrapper.onReceive(ExtSdkMethodType.MJonCircleChannelCreated, data);
            }

            @Override
            public void onChannelDestroyed(String serverId, String channelId, String initiator) {
                Map<String, Object> data = new HashMap<>();
                data.put("serverId", serverId);
                data.put("channelId", channelId);
                data.put("initiator", initiator);
                ExtSdkWrapper.onReceive(ExtSdkMethodType.MJonCircleChannelDestroyed, data);
            }

            @Override
            public void onChannelUpdated(String serverId, String channelId, String channelName, String channelDesc,
                                         String initiator) {
                Map<String, Object> data = new HashMap<>();
                data.put("serverId", serverId);
                data.put("channelId", channelId);
                data.put("channelName", channelName);
                data.put("channelDescription", channelDesc);
                data.put("initiator", initiator);
                ExtSdkWrapper.onReceive(ExtSdkMethodType.MJonCircleChannelUpdated, data);
            }

            @Override
            public void onMemberJoinedChannel(String serverId, String channelId, String member) {
                Map<String, Object> data = new HashMap<>();
                data.put("serverId", serverId);
                data.put("channelId", channelId);
                data.put("memberId", member);
                ExtSdkWrapper.onReceive(ExtSdkMethodType.MJonMemberJoinedCircleChannel, data);
            }

            @Override
            public void onMemberLeftChannel(String serverId, String channelId, String member) {
                Map<String, Object> data = new HashMap<>();
                data.put("serverId", serverId);
                data.put("channelId", channelId);
                data.put("memberId", member);
                ExtSdkWrapper.onReceive(ExtSdkMethodType.MJonMemberLeftCircleChannel, data);
            }

            @Override
            public void onMemberRemovedFromChannel(String serverId, String channelId, String member, String initiator) {
                Map<String, Object> data = new HashMap<>();
                data.put("serverId", serverId);
                data.put("channelId", channelId);
                data.put("memberId", member);
                data.put("initiator", initiator);
                ExtSdkWrapper.onReceive(ExtSdkMethodType.MJonMemberRemovedFromCircleChannel, data);
            }

            @Override
            public void onReceiveInvitation(EMCircleChannelInviteInfo inviteInfo, String inviter) {
                Map<String, Object> data = new HashMap<>();
                data.put("channelExtension", ExtSdkCircleChannelInviteInfoHelper.toJson(inviteInfo));
                data.put("inviter", inviter);
                ExtSdkWrapper.onReceive(ExtSdkMethodType.MJonReceiveInvitationFromCircleChannel, data);
            }

            @Override
            public void onInvitationBeAccepted(String serverId, String channelId, String invitee) {
                Map<String, Object> data = new HashMap<>();
                data.put("serverId", serverId);
                data.put("channelId", channelId);
                data.put("invitee", invitee);
                ExtSdkWrapper.onReceive(ExtSdkMethodType.MJonInvitationBeAcceptedFromCircleChannel, data);
            }

            @Override
            public void onInvitationBeDeclined(String serverId, String channelId, String invitee) {
                Map<String, Object> data = new HashMap<>();
                data.put("serverId", serverId);
                data.put("channelId", channelId);
                data.put("invitee", invitee);
                ExtSdkWrapper.onReceive(ExtSdkMethodType.MJonInvitationBeDeclinedFromCircleChannel, data);
            }

            @Override
            public void onMemberMuteChanged(String serverId, String channelId, boolean isMuted,
                                            List<String> muteMembers) {
                Map<String, Object> data = new HashMap<>();
                data.put("serverId", serverId);
                data.put("channelId", channelId);
                data.put("isMuted", isMuted);
                data.put("memberIds", muteMembers);
                ExtSdkWrapper.onReceive(ExtSdkMethodType.MJonMemberMuteChangedInCircleChannel, data);
            }
        };
        EMClient.getInstance().chatCircleManager().addChannelListener(this.channelListener);

        if (this.serverListener != null) {
            EMClient.getInstance().chatCircleManager().removeServerListener(this.serverListener);
        }
        this.serverListener = new EMCircleServerListener() {
            @Override
            public void onServerDestroyed(String serverId, String initiator) {
                Map<String, Object> data = new HashMap<>();
                data.put("serverId", serverId);
                data.put("initiator", initiator);
                ExtSdkWrapper.onReceive(ExtSdkMethodType.MJonCircleServerDestroyed, data);
            }

            @Override
            public void onServerUpdated(EMCircleServerEvent event) {
                ExtSdkWrapper.onReceive(ExtSdkMethodType.MJonCircleServerUpdated,
                                        ExtSdkCircleServerEventHelper.toJson(event));
            }

            @Override
            public void onMemberJoinedServer(String serverId, String member) {
                Map<String, Object> data = new HashMap<>();
                data.put("serverId", serverId);
                data.put("memberId", member);
                ExtSdkWrapper.onReceive(ExtSdkMethodType.MJonMemberJoinedCircleServer, data);
            }

            @Override
            public void onMemberLeftServer(String serverId, String member) {
                Map<String, Object> data = new HashMap<>();
                data.put("serverId", serverId);
                data.put("memberId", member);
                ExtSdkWrapper.onReceive(ExtSdkMethodType.MJonMemberLeftCircleServer, data);
            }

            @Override
            public void onMemberRemovedFromServer(String serverId, List<String> members) {
                Map<String, Object> data = new HashMap<>();
                data.put("serverId", serverId);
                data.put("memberIds", members);
                ExtSdkWrapper.onReceive(ExtSdkMethodType.MJonMemberRemovedFromCircleServer, data);
            }

            @Override
            public void onReceiveInvitation(EMCircleServerEvent event, String inviter) {
                Map<String, Object> data = new HashMap<>();
                data.put("event", ExtSdkCircleServerEventHelper.toJson(event));
                data.put("inviter", inviter);
                ExtSdkWrapper.onReceive(ExtSdkMethodType.MJonReceiveInvitationFromCircleServer, data);
            }

            @Override
            public void onInvitationBeAccepted(String serverId, String invitee) {
                Map<String, Object> data = new HashMap<>();
                data.put("serverId", serverId);
                data.put("invitee", invitee);
                ExtSdkWrapper.onReceive(ExtSdkMethodType.MJonInvitationBeAcceptedFromCircleServer, data);
            }

            @Override
            public void onInvitationBeDeclined(String serverId, String invitee) {
                Map<String, Object> data = new HashMap<>();
                data.put("serverId", serverId);
                data.put("invitee", invitee);
                ExtSdkWrapper.onReceive(ExtSdkMethodType.MJonInvitationBeDeclinedFromCircleServer, data);
            }

            @Override
            public void onRoleAssigned(String serverId, String member, EMCircleUserRole role) {
                Map<String, Object> data = new HashMap<>();
                data.put("serverId", serverId);
                data.put("memberId", member);
                data.put("role", role.getRoleId());
                ExtSdkWrapper.onReceive(ExtSdkMethodType.MJonRoleAssignedFromCircleServer, data);
            }
        };
        EMClient.getInstance().chatCircleManager().addServerListener(this.serverListener);
    }

    public void createCircleServer(JSONObject param, String channelName, ExtSdkCallback result) throws JSONException {
        EMCircleServerAttribute attribute = new EMCircleServerAttribute();
        attribute.setName(param.getString("serverName"));
        if (param.has("serverIcon")) {
            attribute.setIcon(param.getString("serverIcon"));
        }
        if (param.has("serverDescription")) {
            attribute.setDesc(param.getString("serverDescription"));
        }
        if (param.has("serverExtension")) {
            attribute.setExt(param.getString("serverExtension"));
        }
        EMClient.getInstance().chatCircleManager().createServer(attribute, new EMValueCallBack<EMCircleServer>() {
            @Override
            public void onSuccess(EMCircleServer emCircleServer) {
                ExtSdkWrapper.onSuccess(result, channelName, ExtSdkCircleServerHelper.toJson(emCircleServer));
            }

            @Override
            public void onError(final int error, final String errorMsg) {
                ExtSdkWrapper.onError(result, error, errorMsg);
            }
        });
    }

    public void destroyCircleServer(JSONObject param, String channelName, ExtSdkCallback result) throws JSONException {
        String serverId = param.getString("serverId");
        EMClient.getInstance().chatCircleManager().destroyServer(serverId, new EMCallBack() {
            @Override
            public void onSuccess() {
                ExtSdkWrapper.onSuccess(result, channelName, null);
            }

            @Override
            public void onError(int code, String error) {
                ExtSdkWrapper.onError(result, code, error);
            }
        });
    }

    public void updateCircleServer(JSONObject param, String channelName, ExtSdkCallback result) throws JSONException {
        String serverId = param.getString("serverId");
        EMCircleServerAttribute attribute = new EMCircleServerAttribute();
        if (param.has("serverName")) {
            attribute.setName(param.getString("serverName"));
        }
        if (param.has("serverIcon")) {
            attribute.setIcon(param.getString("serverIcon"));
        }
        if (param.has("serverDescription")) {
            attribute.setDesc(param.getString("serverDescription"));
        }
        if (param.has("serverExtension")) {
            attribute.setExt(param.getString("serverExtension"));
        }
        EMClient.getInstance().chatCircleManager().updateServer(
            serverId, attribute, new EMValueCallBack<EMCircleServer>() {
                @Override
                public void onSuccess(EMCircleServer value) {
                    ExtSdkWrapper.onSuccess(result, channelName, ExtSdkCircleServerHelper.toJson(value));
                }

                @Override
                public void onError(final int error, final String errorMsg) {
                    ExtSdkWrapper.onError(result, error, errorMsg);
                }
            });
    }

    public void joinCircleServer(JSONObject param, String channelName, ExtSdkCallback result) throws JSONException {
        String serverId = param.getString("serverId");
        EMClient.getInstance().chatCircleManager().joinServer(serverId, new EMValueCallBack<EMCircleServer>() {
            @Override
            public void onSuccess(EMCircleServer value) {
                ExtSdkWrapper.onSuccess(result, channelName, ExtSdkCircleServerHelper.toJson(value));
            }

            @Override
            public void onError(final int error, final String errorMsg) {
                ExtSdkWrapper.onError(result, error, errorMsg);
            }
        });
    }

    public void leaveCircleServer(JSONObject param, String channelName, ExtSdkCallback result) throws JSONException {
        String serverId = param.getString("serverId");
        EMClient.getInstance().chatCircleManager().leaveServer(serverId, new EMCallBack() {
            @Override
            public void onSuccess() {
                ExtSdkWrapper.onSuccess(result, channelName, null);
            }

            @Override
            public void onError(int code, String error) {
                ExtSdkWrapper.onError(result, code, error);
            }
        });
    }

    public void removeUserFromCircleServer(JSONObject param, String channelName, ExtSdkCallback result)
        throws JSONException {
        String serverId = param.getString("serverId");
        String userId = param.getString("userId");
        EMClient.getInstance().chatCircleManager().removeUserFromServer(serverId, userId, new EMCallBack() {
            @Override
            public void onSuccess() {
                ExtSdkWrapper.onSuccess(result, channelName, null);
            }

            @Override
            public void onError(int code, String error) {
                ExtSdkWrapper.onError(result, code, error);
            }
        });
    }

    public void inviteUserToCircleServer(JSONObject param, String channelName, ExtSdkCallback result)
        throws JSONException {
        String serverId = param.getString("serverId");
        String userId = param.getString("userId");
        String welcome = param.getString("welcome");
        EMClient.getInstance().chatCircleManager().inviteUserToServer(serverId, userId, welcome, new EMCallBack() {
            @Override
            public void onSuccess() {
                ExtSdkWrapper.onSuccess(result, channelName, null);
            }

            @Override
            public void onError(int code, String error) {
                ExtSdkWrapper.onError(result, code, error);
            }
        });
    }

    public void acceptCircleServerInvitation(JSONObject param, String channelName, ExtSdkCallback result)
        throws JSONException {
        String serverId = param.getString("serverId");
        String inviter = param.getString("inviter");
        EMClient.getInstance().chatCircleManager().acceptServerInvitation(
            serverId, inviter, new EMValueCallBack<EMCircleServer>() {
                @Override
                public void onSuccess(EMCircleServer value) {
                    ExtSdkWrapper.onSuccess(result, channelName, ExtSdkCircleServerHelper.toJson(value));
                }

                @Override
                public void onError(final int error, final String errorMsg) {
                    ExtSdkWrapper.onError(result, error, errorMsg);
                }
            });
    }

    public void declineCircleServerInvitation(JSONObject param, String channelName, ExtSdkCallback result)
        throws JSONException {
        String serverId = param.getString("serverId");
        String inviter = param.getString("inviter");
        EMClient.getInstance().chatCircleManager().declineServerInvitation(serverId, inviter, new EMCallBack() {
            @Override
            public void onSuccess() {
                ExtSdkWrapper.onSuccess(result, channelName, null);
            }

            @Override
            public void onError(int code, String error) {
                ExtSdkWrapper.onError(result, code, error);
            }
        });
    }

    public void addTagsToCircleServer(JSONObject param, String channelName, ExtSdkCallback result)
        throws JSONException {
        String serverId = param.getString("serverId");
        JSONArray jsonServerTags = param.getJSONArray("serverTags");
        List<String> serverTags = new ArrayList<>();
        for (int i = 0; i < jsonServerTags.length(); ++i) {
            serverTags.add(jsonServerTags.getString(i));
        }
        EMClient.getInstance().chatCircleManager().addTagsToServer(
            serverId, serverTags, new EMValueCallBack<List<EMCircleTag>>() {
                @Override
                public void onSuccess(List<EMCircleTag> tags) {
                    List<Object> jsonTags = new ArrayList<>();
                    for (EMCircleTag tag : tags) {
                        jsonTags.add(ExtSdkCircleTagHelper.toJson(tag));
                    }
                    ExtSdkWrapper.onSuccess(result, channelName, jsonTags);
                }

                @Override
                public void onError(final int error, final String errorMsg) {
                    ExtSdkWrapper.onError(result, error, errorMsg);
                }
            });
    }

    public void removeTagsFromCircleServer(JSONObject param, String channelName, ExtSdkCallback result)
        throws JSONException {
        String serverId = param.getString("serverId");
        JSONArray jsonServerTags = param.getJSONArray("serverTags");
        List<String> serverTags = new ArrayList<>();
        for (int i = 0; i < jsonServerTags.length(); ++i) {
            serverTags.add(jsonServerTags.getString(i));
        }
        EMClient.getInstance().chatCircleManager().removeTagsFromServer(serverId, serverTags, new EMCallBack() {
            @Override
            public void onSuccess() {
                ExtSdkWrapper.onSuccess(result, channelName, null);
            }

            @Override
            public void onError(int code, String error) {
                ExtSdkWrapper.onError(result, code, error);
            }
        });
    }

    public void fetchCircleServerTags(JSONObject param, String channelName, ExtSdkCallback result)
        throws JSONException {
        String serverId = param.getString("serverId");
        EMClient.getInstance().chatCircleManager().fetchServerTags(serverId, new EMValueCallBack<List<EMCircleTag>>() {
            @Override
            public void onSuccess(List<EMCircleTag> tags) {
                List<Object> jsonTags = new ArrayList<>();
                for (EMCircleTag tag : tags) {
                    jsonTags.add(ExtSdkCircleTagHelper.toJson(tag));
                }
                ExtSdkWrapper.onSuccess(result, channelName, jsonTags);
            }

            @Override
            public void onError(int error, String errorMsg) {
                ExtSdkWrapper.onError(result, error, errorMsg);
            }
        });
    }

    public void addModeratorToCircleServer(JSONObject param, String channelName, ExtSdkCallback result)
        throws JSONException {
        String serverId = param.getString("serverId");
        String userId = param.getString("userId");
        EMClient.getInstance().chatCircleManager().addModeratorToServer(serverId, userId, new EMCallBack() {
            @Override
            public void onSuccess() {
                ExtSdkWrapper.onSuccess(result, channelName, null);
            }

            @Override
            public void onError(int code, String error) {
                ExtSdkWrapper.onError(result, code, error);
            }
        });
    }

    public void removeModeratorFromCircleServer(JSONObject param, String channelName, ExtSdkCallback result)
        throws JSONException {
        String serverId = param.getString("serverId");
        String userId = param.getString("userId");
        EMClient.getInstance().chatCircleManager().removeModeratorFromServer(serverId, userId, new EMCallBack() {
            @Override
            public void onSuccess() {
                ExtSdkWrapper.onSuccess(result, channelName, null);
            }

            @Override
            public void onError(int code, String error) {
                ExtSdkWrapper.onError(result, code, error);
            }
        });
    }

    public void fetchSelfCircleServerRole(JSONObject param, String channelName, ExtSdkCallback result)
        throws JSONException {
        String serverId = param.getString("serverId");
        EMClient.getInstance().chatCircleManager().fetchSelfServerRole(
            serverId, new EMValueCallBack<EMCircleUserRole>() {
                @Override
                public void onSuccess(EMCircleUserRole value) {
                    ExtSdkWrapper.onSuccess(result, channelName, value.getRoleId());
                }

                @Override
                public void onError(int error, String errorMsg) {
                    ExtSdkWrapper.onError(result, error, errorMsg);
                }
            });
    }

    public void fetchJoinedCircleServers(JSONObject param, String channelName, ExtSdkCallback result)
        throws JSONException {
        int limit = param.getInt("pageSize");
        String cursor = param.getString("cursor");
        EMClient.getInstance().chatCircleManager().fetchJoinedServers(
            limit, cursor, new EMValueCallBack<EMCursorResult<EMCircleServer>>() {
                @Override
                public void onSuccess(EMCursorResult<EMCircleServer> value) {
                    ExtSdkWrapper.onSuccess(result, channelName, ExtSdkCursorResultHelper.toJson(value));
                }

                @Override
                public void onError(int error, String errorMsg) {
                    ExtSdkWrapper.onError(result, error, errorMsg);
                }
            });
    }

    public void fetchCircleServerDetail(JSONObject param, String channelName, ExtSdkCallback result)
        throws JSONException {
        String serverId = param.getString("serverId");
        EMClient.getInstance().chatCircleManager().fetchServerDetail(serverId, new EMValueCallBack<EMCircleServer>() {
            @Override
            public void onSuccess(EMCircleServer value) {
                ExtSdkWrapper.onSuccess(result, channelName, ExtSdkCircleServerHelper.toJson(value));
            }

            @Override
            public void onError(int error, String errorMsg) {
                ExtSdkWrapper.onError(result, error, errorMsg);
            }
        });
    }

    public void fetchCircleServersWithKeyword(JSONObject param, String channelName, ExtSdkCallback result)
        throws JSONException {
        String keyword = param.getString("keyword");
        EMClient.getInstance().chatCircleManager().fetchServersWithKeyword(
            keyword, new EMValueCallBack<List<EMCircleServer>>() {
                @Override
                public void onSuccess(List<EMCircleServer> servers) {
                    List<Object> jsonServers = new ArrayList<>();
                    for (EMCircleServer server : servers) {
                        jsonServers.add(ExtSdkCircleServerHelper.toJson(server));
                    }
                    ExtSdkWrapper.onSuccess(result, channelName, jsonServers);
                }

                @Override
                public void onError(int error, String errorMsg) {
                    ExtSdkWrapper.onError(result, error, errorMsg);
                }
            });
    }

    public void fetchCircleServerMembers(JSONObject param, String channelName, ExtSdkCallback result)
        throws JSONException {
        String serverId = param.getString("serverId");
        int limit = param.getInt("pageSize");
        String cursor = param.getString("cursor");
        EMClient.getInstance().chatCircleManager().fetchServerMembers(
            serverId, limit, cursor, new EMValueCallBack<EMCursorResult<EMCircleUser>>() {
                @Override
                public void onSuccess(EMCursorResult<EMCircleUser> value) {
                    ExtSdkWrapper.onSuccess(result, channelName, ExtSdkCursorResultHelper.toJson(value));
                }

                @Override
                public void onError(int error, String errorMsg) {
                    ExtSdkWrapper.onError(result, error, errorMsg);
                }
            });
    }

    public void checkSelfInCircleServer(JSONObject param, String channelName, ExtSdkCallback result)
        throws JSONException {
        String serverId = param.getString("serverId");
        EMClient.getInstance().chatCircleManager().checkSelfIsInServer(serverId, new EMValueCallBack<Boolean>() {
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

    public void createCircleChannel(JSONObject param, String channelName, ExtSdkCallback result) throws JSONException {
        String serverId = param.getString("serverId");
        EMCircleChannelAttribute attribute = new EMCircleChannelAttribute();
        attribute.setName(param.getString("channelName"));
        if (param.has("channelDescription")) {
            attribute.setDesc(param.getString("channelDescription"));
        }
        if (param.has("channelExtension")) {
            attribute.setExt(param.getString("channelExtension"));
        }
        attribute.setRank(ExtSdkCircleChannelHelper.rankFromInt(param.getInt("channelRank")));
        int jsonType = param.getInt("channelType");
        EMCircleChannelStyle type = ExtSdkCircleChannelHelper.typeFromInt(jsonType);
        EMClient.getInstance().chatCircleManager().createChannel(
            serverId, attribute, type, new EMValueCallBack<EMCircleChannel>() {
                @Override
                public void onSuccess(EMCircleChannel value) {
                    ExtSdkWrapper.onSuccess(result, channelName, ExtSdkCircleChannelHelper.toJson(value));
                }

                @Override
                public void onError(int error, String errorMsg) {
                    ExtSdkWrapper.onError(result, error, errorMsg);
                }
            });
    }

    public void destroyCircleChannel(JSONObject param, String channelName, ExtSdkCallback result) throws JSONException {
        String serverId = param.getString("serverId");
        String channelId = param.getString("channelId");
        EMClient.getInstance().chatCircleManager().destroyChannel(serverId, channelId, new EMCallBack() {
            @Override
            public void onSuccess() {
                ExtSdkWrapper.onSuccess(result, channelName, null);
            }

            @Override
            public void onError(int code, String error) {
                ExtSdkWrapper.onError(result, code, error);
            }
        });
    }

    public void updateCircleChannel(JSONObject param, String channelName, ExtSdkCallback result) throws JSONException {
        String serverId = param.getString("serverId");
        String channelId = param.getString("channelId");
        EMCircleChannelAttribute attribute = new EMCircleChannelAttribute();
        if (param.has("channelName")) {
            attribute.setName(param.getString("channelName"));
        }
        if (param.has("channelDescription")) {
            attribute.setDesc(param.getString("channelDescription"));
        }
        if (param.has("channelExtension")) {
            attribute.setExt(param.getString("channelExtension"));
        }
        if (param.has("channelRank")) {
            attribute.setRank(ExtSdkCircleChannelHelper.rankFromInt(param.getInt("channelRank")));
        }
        EMClient.getInstance().chatCircleManager().updateChannel(
            serverId, channelId, attribute, new EMValueCallBack<EMCircleChannel>() {
                @Override
                public void onSuccess(EMCircleChannel value) {
                    ExtSdkWrapper.onSuccess(result, channelName, ExtSdkCircleChannelHelper.toJson(value));
                }

                @Override
                public void onError(int error, String errorMsg) {
                    ExtSdkWrapper.onError(result, error, errorMsg);
                }
            });
    }

    public void joinCircleChannel(JSONObject param, String channelName, ExtSdkCallback result) throws JSONException {
        String serverId = param.getString("serverId");
        String channelId = param.getString("channelId");
        EMClient.getInstance().chatCircleManager().joinChannel(
            serverId, channelId, new EMValueCallBack<EMCircleChannel>() {
                @Override
                public void onSuccess(EMCircleChannel value) {
                    ExtSdkWrapper.onSuccess(result, channelName, ExtSdkCircleChannelHelper.toJson(value));
                }

                @Override
                public void onError(int error, String errorMsg) {
                    ExtSdkWrapper.onError(result, error, errorMsg);
                }
            });
    }

    public void leaveCircleChannel(JSONObject param, String channelName, ExtSdkCallback result) throws JSONException {
        String serverId = param.getString("serverId");
        String channelId = param.getString("channelId");
        EMClient.getInstance().chatCircleManager().leaveChannel(serverId, channelId, new EMCallBack() {
            @Override
            public void onSuccess() {
                ExtSdkWrapper.onSuccess(result, channelName, null);
            }

            @Override
            public void onError(int code, String error) {
                ExtSdkWrapper.onError(result, code, error);
            }
        });
    }

    public void removeUserFromCircleChannel(JSONObject param, String channelName, ExtSdkCallback result)
        throws JSONException {
        String serverId = param.getString("serverId");
        String channelId = param.getString("channelId");
        String userId = param.getString("userId");
        EMClient.getInstance().chatCircleManager().removeUserFromChannel(serverId, channelId, userId, new EMCallBack() {
            @Override
            public void onSuccess() {
                ExtSdkWrapper.onSuccess(result, channelName, null);
            }

            @Override
            public void onError(int code, String error) {
                ExtSdkWrapper.onError(result, code, error);
            }
        });
    }

    public void inviteUserToCircleChannel(JSONObject param, String channelName, ExtSdkCallback result)
        throws JSONException {
        String serverId = param.getString("serverId");
        String channelId = param.getString("channelId");
        String userId = param.getString("userId");
        String welcome = param.getString("welcome");
        EMClient.getInstance().chatCircleManager().inviteUserToChannel(
            serverId, channelId, userId, welcome, new EMCallBack() {
                @Override
                public void onSuccess() {
                    ExtSdkWrapper.onSuccess(result, channelName, null);
                }

                @Override
                public void onError(int code, String error) {
                    ExtSdkWrapper.onError(result, code, error);
                }
            });
    }

    public void acceptCircleChannelInvitation(JSONObject param, String channelName, ExtSdkCallback result)
        throws JSONException {
        String serverId = param.getString("serverId");
        String channelId = param.getString("channelId");
        String inviter = param.getString("inviter");
        EMClient.getInstance().chatCircleManager().acceptChannelInvitation(
            serverId, channelId, inviter, new EMValueCallBack<EMCircleChannel>() {
                @Override
                public void onSuccess(EMCircleChannel value) {
                    ExtSdkWrapper.onSuccess(result, channelName, ExtSdkCircleChannelHelper.toJson(value));
                }

                @Override
                public void onError(int error, String errorMsg) {
                    ExtSdkWrapper.onError(result, error, errorMsg);
                }
            });
    }

    public void declineCircleChannelInvitation(JSONObject param, String channelName, ExtSdkCallback result)
        throws JSONException {
        String serverId = param.getString("serverId");
        String channelId = param.getString("channelId");
        String inviter = param.getString("inviter");
        EMClient.getInstance().chatCircleManager().declineChannelInvitation(
            serverId, channelId, inviter, new EMCallBack() {
                @Override
                public void onSuccess() {
                    ExtSdkWrapper.onSuccess(result, channelName, null);
                }

                @Override
                public void onError(int code, String error) {
                    ExtSdkWrapper.onError(result, code, error);
                }
            });
    }

    public void muteUserInCircleChannel(JSONObject param, String channelName, ExtSdkCallback result)
        throws JSONException {
        String serverId = param.getString("serverId");
        String channelId = param.getString("channelId");
        String userId = param.getString("userId");
        int duration = param.getInt("duration");
        EMClient.getInstance().chatCircleManager().muteUserInChannel(
            serverId, channelId, userId, duration, new EMCallBack() {
                @Override
                public void onSuccess() {
                    ExtSdkWrapper.onSuccess(result, channelName, null);
                }

                @Override
                public void onError(int code, String error) {
                    ExtSdkWrapper.onError(result, code, error);
                }
            });
    }

    public void unmuteUserInCircleChannel(JSONObject param, String channelName, ExtSdkCallback result)
        throws JSONException {
        String serverId = param.getString("serverId");
        String channelId = param.getString("channelId");
        String userId = param.getString("userId");
        EMClient.getInstance().chatCircleManager().unmuteUserInChannel(serverId, channelId, userId, new EMCallBack() {
            @Override
            public void onSuccess() {
                ExtSdkWrapper.onSuccess(result, channelName, null);
            }

            @Override
            public void onError(int code, String error) {
                ExtSdkWrapper.onError(result, code, error);
            }
        });
    }

    public void fetchCircleChannelDetail(JSONObject param, String channelName, ExtSdkCallback result)
        throws JSONException {
        String serverId = param.getString("serverId");
        String channelId = param.getString("channelId");
        EMClient.getInstance().chatCircleManager().fetchChannelDetail(
            serverId, channelId, new EMValueCallBack<EMCircleChannel>() {
                @Override
                public void onSuccess(EMCircleChannel value) {
                    ExtSdkWrapper.onSuccess(result, channelName, ExtSdkCircleChannelHelper.toJson(value));
                }

                @Override
                public void onError(int error, String errorMsg) {
                    ExtSdkWrapper.onError(result, error, errorMsg);
                }
            });
    }

    public void fetchPublicCircleChannelInServer(JSONObject param, String channelName, ExtSdkCallback result)
        throws JSONException {
        String serverId = param.getString("serverId");
        String cursor = param.getString("cursor");
        int limit = param.getInt("pageSize");
        EMClient.getInstance().chatCircleManager().fetchPublicChannelsInServer(
            serverId, limit, cursor, new EMValueCallBack<EMCursorResult<EMCircleChannel>>() {
                @Override
                public void onSuccess(EMCursorResult<EMCircleChannel> value) {
                    ExtSdkWrapper.onSuccess(result, channelName, ExtSdkCursorResultHelper.toJson(value));
                }

                @Override
                public void onError(int error, String errorMsg) {
                    ExtSdkWrapper.onError(result, error, errorMsg);
                }
            });
    }

    public void fetchCircleChannelMembers(JSONObject param, String channelName, ExtSdkCallback result)
        throws JSONException {
        String serverId = param.getString("serverId");
        String channelId = param.getString("channelId");
        String cursor = param.getString("cursor");
        int limit = param.getInt("pageSize");
        EMClient.getInstance().chatCircleManager().fetchChannelMembers(
            serverId, channelId, limit, cursor, new EMValueCallBack<EMCursorResult<EMCircleUser>>() {
                @Override
                public void onSuccess(EMCursorResult<EMCircleUser> value) {
                    ExtSdkWrapper.onSuccess(result, channelName, ExtSdkCursorResultHelper.toJson(value));
                }

                @Override
                public void onError(int error, String errorMsg) {
                    ExtSdkWrapper.onError(result, error, errorMsg);
                }
            });
    }

    public void fetchVisiblePrivateCircleChannelInServer(JSONObject param, String channelName, ExtSdkCallback result)
        throws JSONException {
        String serverId = param.getString("serverId");
        String cursor = param.getString("cursor");
        int limit = param.getInt("pageSize");
        EMClient.getInstance().chatCircleManager().fetchVisiblePrivateChannelsInServer(
            serverId, limit, cursor, new EMValueCallBack<EMCursorResult<EMCircleChannel>>() {
                @Override
                public void onSuccess(EMCursorResult<EMCircleChannel> value) {
                    ExtSdkWrapper.onSuccess(result, channelName, ExtSdkCursorResultHelper.toJson(value));
                }

                @Override
                public void onError(int error, String errorMsg) {
                    ExtSdkWrapper.onError(result, error, errorMsg);
                }
            });
    }

    public void checkSelfIsInCircleChannel(JSONObject param, String channelName, ExtSdkCallback result)
        throws JSONException {
        String serverId = param.getString("serverId");
        String channelId = param.getString("channelId");
        EMClient.getInstance().chatCircleManager().checkSelfIsInChannel(
            serverId, channelId, new EMValueCallBack<Boolean>() {
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

    public void fetchCircleChannelMuteUsers(JSONObject param, String channelName, ExtSdkCallback result)
        throws JSONException {
        String serverId = param.getString("serverId");
        String channelId = param.getString("channelId");
        EMClient.getInstance().chatCircleManager().fetchChannelMuteUsers(
            serverId, channelId, new EMValueCallBack<Map<String, Long>>() {
                @Override
                public void onSuccess(Map<String, Long> value) {
                    ExtSdkWrapper.onSuccess(result, channelName, value);
                }

                @Override
                public void onError(int error, String errorMsg) {
                    ExtSdkWrapper.onError(result, error, errorMsg);
                }
            });
    }
}
