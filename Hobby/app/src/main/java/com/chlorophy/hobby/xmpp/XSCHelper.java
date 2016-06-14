package com.chlorophy.hobby.xmpp;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;

import com.chlorophy.hobby.freutil.system.SysUtil;

import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.PacketCollector;
import org.jivesoftware.smack.PacketListener;
import org.jivesoftware.smack.SmackConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;
import org.jivesoftware.smack.filter.AndFilter;
import org.jivesoftware.smack.filter.PacketFilter;
import org.jivesoftware.smack.filter.PacketIDFilter;
import org.jivesoftware.smack.filter.PacketTypeFilter;
import org.jivesoftware.smack.packet.IQ;
import org.jivesoftware.smack.packet.Message;
import org.jivesoftware.smack.packet.Packet;
import org.jivesoftware.smack.packet.Presence;
import org.jivesoftware.smack.packet.Registration;
import org.jivesoftware.smack.provider.PrivacyProvider;
import org.jivesoftware.smack.provider.ProviderManager;
import org.jivesoftware.smackx.Form;
import org.jivesoftware.smackx.FormField;
import org.jivesoftware.smackx.GroupChatInvitation;
import org.jivesoftware.smackx.PrivateDataManager;
import org.jivesoftware.smackx.bytestreams.socks5.provider.BytestreamsProvider;
import org.jivesoftware.smackx.muc.DiscussionHistory;
import org.jivesoftware.smackx.muc.MultiUserChat;
import org.jivesoftware.smackx.packet.ChatStateExtension;
import org.jivesoftware.smackx.packet.LastActivity;
import org.jivesoftware.smackx.packet.OfflineMessageInfo;
import org.jivesoftware.smackx.packet.OfflineMessageRequest;
import org.jivesoftware.smackx.packet.SharedGroupsInfo;
import org.jivesoftware.smackx.packet.VCard;
import org.jivesoftware.smackx.provider.AdHocCommandDataProvider;
import org.jivesoftware.smackx.provider.DataFormProvider;
import org.jivesoftware.smackx.provider.DelayInformationProvider;
import org.jivesoftware.smackx.provider.DiscoverInfoProvider;
import org.jivesoftware.smackx.provider.DiscoverItemsProvider;
import org.jivesoftware.smackx.provider.MUCAdminProvider;
import org.jivesoftware.smackx.provider.MUCOwnerProvider;
import org.jivesoftware.smackx.provider.MUCUserProvider;
import org.jivesoftware.smackx.provider.MessageEventProvider;
import org.jivesoftware.smackx.provider.MultipleAddressesProvider;
import org.jivesoftware.smackx.provider.RosterExchangeProvider;
import org.jivesoftware.smackx.provider.StreamInitiationProvider;
import org.jivesoftware.smackx.provider.VCardProvider;
import org.jivesoftware.smackx.provider.XHTMLExtensionProvider;
import org.jivesoftware.smackx.search.UserSearch;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class XSCHelper {

    //常量表
    public final static int SUCCESS = 1;
    public final static int FAILURE = 0;
    public final static int ERROR = 2;
    public final static int EXIST = 3;
    public final static int MESSAGE = 4;

    public final static String AVATAG = "lastModified";
    public final static String CREDIT = "credit";
    public final static String CONTRIBUTED = "contributed";
    public final static String SHARED = "shared";
    public final static String PROJS = "projs";
    public final static String PROJLISTS = "_projectLists";
    public final static String MSGFROM = "message_from";
    public final static String MSGBODY = "message_body";
    public final static String MSGCLOUD = "message_cloud";
    public final static String MSGCLOUD_KEY = "message_cloud_key";

    public final static String ROOM_OWNER = "muc#roomconfig_roomowners";
    public final static String ROOM_PSW = "muc#roomconfig_roomsecret";
    public final static String ROOM_KEEP_ALLOWED = "muc#roomconfig_persistentroom";
    public final static String ROOM_MEMBERS_ONLY = "muc#roomconfig_membersonly";
    public final static String ROOM_INVITE_ALLOWED = "muc#roomconfig_allowinvites";
    public final static String ROOM_PSW_REQUIRED = "muc#roomconfig_passwordprotectedroom";
    public final static String ROOM_LOG_ENABLED = "muc#roomconfig_enablelogging";
    public final static String ROOM_NICKNAME_ONLY = "x-muc#roomconfig_reservednick";
    public final static String ROOM_SPECIAL_NICKNAME_ALLOWED = "x-muc#roomconfig_canchangenick";
    public final static String ROOM_REGIST_NEW_ROOM_ALLOWED = "x-muc#roomconfig_registration";

    //服务器信息
    public static String HOST = "115.159.101.78";
    public static String HOST_NAME = "dollars.noip.me";
    public static final int PORT = 5222;
    public static final String GROUP = "dollars.helios";

    //本类全局变量
    private static XSCHelper helper = new XSCHelper();
    private XMPPConnection connection = null;
    private VCard vCard = null;
    boolean connectFlag = true;

    ////////////////////////////////////////////////////////////////////////
    ////////////////              单例模式            ///////////////////////
    ////////////////////////////////////////////////////////////////////////

    //单例模式
    private XSCHelper() {
        //配置Provider，如不配置则无法解析数据
        configureConnection(ProviderManager.getInstance());
    }

    synchronized public static XSCHelper getInstance() {
        return helper;
    }

    ////////////////////////////////////////////////////////////////////////
    ////////////////              自用连接            ///////////////////////
    ////////////////////////////////////////////////////////////////////////

    /**
     * 获取到服务器的连接 (必须在本类，多线程中调用(内部工具函数))
     *
     * @return connection  连接。如成功连接服务器返回连接，否则返回null
     */
    private XMPPConnection getConnection() {
        if (connection == null) {
            connectToServer();
        }
        return connection;
    }

    /**
     * 建立与服务器的持久连接 (必须仅能被getConnection()直接调用)
     *
     * @return boolean  成功。如成功连接服务器返回true，否则false
     */
    private boolean connectToServer() {
        if (connection == null || !connection.isConnected()) {
            try {


                //配置连接
                XMPPConnection.DEBUG_ENABLED = true;
                ConnectionConfiguration config =
                        new ConnectionConfiguration(HOST, PORT, GROUP);
                config.setReconnectionAllowed(true);
                config.setSendPresence(false);

                //创建并连接
                connection = new XMPPConnection(config);
                connection.connect();
                if(!connection.isConnected())
                    throw new XMPPException();
                return connection.isConnected();
            } catch (XMPPException e) {
                e.printStackTrace();
                connection = null;
            }
        }
        return false;
    }

    ////////////////////////////////////////////////////////////////////////
    ////////////////              公开连接            ///////////////////////
    ////////////////////////////////////////////////////////////////////////

    /**
     * 释放与服务器的连接
     */
    public void releaseConnection() {
        new Thread() {
            @Override
            public void run() {
                if(connection != null && connection.isConnected()) {
                    connection.disconnect();
                    connection = null;
                }
            }
        }.start();
    }

    /**
     * 获取当前连接状态
     */
    public boolean isConnected() {
        return connection != null;
    }

    /**
     * 获取与服务器的连接
     */
    public boolean connect() {
        if(isConnected())
            return true;

        connectFlag = true;
        new Thread(){
            @Override
            public void run() {
                getConnection();
                connectFlag = false;
            }
        }.start();

        while (connectFlag) {}

        return isConnected();
    }

    /**
     * 登陆。网络操作不可以在主线程中进行。多线程无法等待return，所以必须使用Handler
     *
     * @param account  登陆账户
     * @param password 登录密码
     * @param handler  处理信息的Handler
     */
    public void logIn(final String account, final String password, final Handler handler) {
        //先保存登录信息
        UserInfoBasic.account = account;
        UserInfoBasic.password = password;
        new Thread() {
            @Override
            public void run() {
                //如果无法连接服务器，Handle Error
                if (getConnection() == null) {
                    Log.e("------->", "ERROR");
                    handler.sendEmptyMessage(ERROR);
                    return;
                }

                //如果已经是登录状态，Handle Success
                if (getConnection().isAuthenticated()) {
                    Log.e("------->", "hasAuthenticated");
                    handler.sendEmptyMessage(SUCCESS);
                    return;
                }

                //如果状态正常，开始连接
                try {
                    getConnection().login(account, password, GROUP);
                    Log.e("------->", "Successful connection");
                } catch (XMPPException e) {
                    //登录失败，释放连接，方便下次登录
                    releaseConnection();
                    handler.sendEmptyMessage(FAILURE);
                    Log.e("------->", "Fail LogIn");
                    return;
                }
                Log.e("------->", "Succeed");
                setPresence(PRESENCE.ONLINE);
                handler.sendEmptyMessage(SUCCESS);
                refreshUserBasicData();
                refreshUserEnhancedData();
            }
        }.start();
    }

    /**
     * 注册。网络操作不可以在主线程中进行。多线程无法等待return，所以必须使用Handler
     *
     * @param account  登陆账户
     * @param password 登录密码
     * @param handler  处理信息的Handler
     */
    public void regist(final String account, final String password, final Handler handler) {
        new Thread(){
            @Override
            public void run() {
                //如果无法连接服务器，Handle Error
                if (getConnection() == null) {
                    Log.e("------->", "ERROR");
                    handler.sendEmptyMessage(ERROR);
                    return;
                }

                //如果已经是登录状态，Handle Failure
                if (getConnection().isAuthenticated()) {
                    Log.e("------->", "hasAuthenticated");
                    handler.sendEmptyMessage(FAILURE);
                    return;
                }

                //如果状态正常则配置注册信息
                Registration reg = new Registration();
                reg.setType(IQ.Type.SET);
                reg.setTo(getConnection().getServiceName());
                reg.setUsername(account);
                reg.setPassword(password);
                reg.addAttribute("H-S-A-2015", "ThronBird");

                //配置Filter过滤器，注明包ID信息
                PacketFilter filter = new AndFilter(
                        new PacketIDFilter(reg.getPacketID()),
                        new PacketTypeFilter(IQ.class));

                //用Filter过滤器配置Collector接受器以便接受信息，并发送封包
                PacketCollector collector =
                        getConnection().createPacketCollector(filter);
                getConnection().sendPacket(reg);

                //等待信息，注明超时时间
                IQ result = (IQ) collector.nextResult(
                        SmackConfiguration.getPacketReplyTimeout());

                //接收到信息就可以取消掉Collector了，处理接受的信息
                collector.cancel();
                if(result == null) {
                    handler.sendEmptyMessage(ERROR);
                    return;
                }

                if(result.getType() == IQ.Type.RESULT) {
                    handler.sendEmptyMessage(SUCCESS);
                    return;
                }

                //以上是超时或者成功的情况，一下是失败的情况（已有/失败）
                if(result.getError().toString().equalsIgnoreCase("conflict(409)")) {
                    handler.sendEmptyMessage(EXIST);
                    return;
                }

                handler.sendEmptyMessage(FAILURE);

            }
        }.run();
    }

    /**
     * 创建聊天室
     *
     * @param password 登录密码
     * @param type     登陆类型: 0 - ProjCloud Other - Personal
     * @param roomName 房间名称
     * @param handler  处理信息的Handler
     */
    public void createRoom(final String roomName, final int type, final String password, final Handler handler) {
        new Thread(){
            @Override
            public void run() {

                //如果无法连接服务器/超时，Handle Error
                if(getConnection() == null) {
                    handler.sendEmptyMessage(ERROR);
                    return;
                }

                //如果不是登录状态，Handle Failure
                if (!getConnection().isAuthenticated()) {
                    handler.sendEmptyMessage(FAILURE);
                    return;
                }

                try {
                    String service = type == 0 ? "@cloud." : "@personal.";

                    //写清房间地址，房产中心注册，开房，创建房间
                    String address = roomName + service + getConnection().getServiceName();
                    MultiUserChat room = new MultiUserChat(getConnection(), address);
                    room.create(roomName);

                    //装修房间配置信息：获取，配置，再提交
                    Form originForm = room.getConfigurationForm();
                    Form form = originForm.createAnswerForm();

                    //---基础：当前域内项非隐藏并且非空（即有实际内容）就设为新表的域
                    Iterator<FormField> iterator = originForm.getFields();
                    while (iterator.hasNext()) {
                        FormField field = iterator.next();
                        if(!field.getType().equals(FormField.TYPE_HIDDEN)
                                && field.getVariable() != null) {
                            form.setDefaultAnswer(field.getVariable());
                        }
                    }

                    //---设置当前房间主人
                    List<String> ownerList = new ArrayList<>();
                    ownerList.add(getConnection().getUser());

                    //---杂项
                    if(!password.equals("")) {
                        form.setAnswer(ROOM_PSW_REQUIRED, true);  //要求密码
                        form.setAnswer(ROOM_PSW, password);  //设定密码
                    }
                    form.setAnswer(ROOM_KEEP_ALLOWED, true);  //永久房间
                    form.setAnswer(ROOM_MEMBERS_ONLY, false);  //不禁止新入成员
                    form.setAnswer(ROOM_INVITE_ALLOWED, true);  //允许邀请
                    form.setAnswer(ROOM_LOG_ENABLED, true);  //开启日志
                    form.setAnswer(ROOM_NICKNAME_ONLY, true);  //昵称登陆
                    form.setAnswer(ROOM_SPECIAL_NICKNAME_ALLOWED, false);  //禁群内昵称
                    form.setAnswer(ROOM_REGIST_NEW_ROOM_ALLOWED, false);  //禁开子房

                    //发送表单更新房间信息
                    room.sendConfigurationForm(form);
                    handler.sendEmptyMessage(SUCCESS);
                } catch (XMPPException e) {
                    handler.sendEmptyMessage(FAILURE);
                    e.printStackTrace();
                }
            }
        }.start();
    }


    /**
     * 加入聊天室，不对外公开，对外公开包装后的两个方法：加入聊天聊天室，加入信息聊天室
     *
     * @param password 登录密码
     * @param type     登陆类型: 0 - ProjCloud Other - Personal
     * @param roomName 房间名称
     * @param handler  处理信息的Handler
     */
    private void joinRoom(final String roomName, final String password, final int type, final int msg, final Handler handler) {
        new Thread() {
            @Override
            public void run() {

                //如果无法连接服务器/超时，Handle Error
                if (getConnection() == null) {
                    handler.sendEmptyMessage(ERROR);
                    return;
                }

                //如果不是登录状态，Handle Failure
                if (!getConnection().isAuthenticated()) {
                    handler.sendEmptyMessage(FAILURE);
                    return;
                }

                try {
                    String service = type == 0 ? "@cloud." : "@personal.";
                    String address = roomName + service + getConnection().getServiceName();
                    MultiUserChat room = new MultiUserChat(getConnection(), address);
                    DiscussionHistory history = new DiscussionHistory();
                    history.setMaxStanzas(msg);
                    room.join(UserInfoBasic.nickName, password, history, SmackConfiguration.getPacketReplyTimeout());
                    room.addMessageListener(new PacketListener() {
                        @Override
                        public void processPacket(Packet packet) {
                            android.os.Message message = new android.os.Message();
                            message.what = MESSAGE;
                            message.obj = packet;
                            handler.sendMessage(message);
                        }
                    });
                    android.os.Message message = new android.os.Message();
                    message.what = SUCCESS;
                    message.obj = room;
                    handler.sendMessage(message);
                } catch (XMPPException e) {
                    handler.sendEmptyMessage(FAILURE);
                    e.printStackTrace();
                }

            }
        }.run();
    }

    /**
     * 加入工程云同步室
     *
     * @param handler  处理信息的Handler
     */
    public void joinProjChromRoom(final Handler handler) {
        joinRoom(UserInfoBasic.account, null, 0, 25, new Handler() {
            @Override
            public void handleMessage(android.os.Message msg) {
                android.os.Message message = new android.os.Message();
                message.what = msg.what;
                message.obj = msg.obj;
                switch (msg.what) {
                    case FAILURE:
                        handler.sendEmptyMessage(FAILURE);
                        break;
                    case SUCCESS:
                        handler.sendMessage(message);
                        break;
                    case MESSAGE:
                        String body = ((Message) message.obj).getBody();
                        message.obj = ((Message) message.obj).getProperty(MSGCLOUD);
                        Bundle args = new Bundle();
                        args.putString(MSGCLOUD_KEY, body);
                        message.setData(args);
                        handler.sendMessage(message);
                        break;
                }
            }
        });
    }

    /**
     * 发送信息到云同步室
     *
     * @param room  云同步室
     * @param msg   将要发送的信息
     */
    public void sendProjChromMsg(MultiUserChat room, Object msg) {
        if (!room.isJoined())
            return;
        try {
            Message message = new Message(room.getRoom(), Message.Type.groupchat);
            message.setBody(SysUtil.getInstance().getDateAndTimeFormated());
            message.setProperty(MSGCLOUD, msg);
            room.sendMessage(message);
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }


    /**
     * 加入信息聊天室
     *
     * @param password 登录密码
     * @param roomName 房间名称
     * @param maxMsg   接收离线消息数
     * @param handler  处理信息的Handler
     */
    public void joinChatRoom(final String roomName, final String password, final int maxMsg, final Handler handler) {
        joinRoom(roomName, password, 1, maxMsg, new Handler(){
            @Override
            public void handleMessage(android.os.Message msg) {
                android.os.Message message = new android.os.Message();
                message.what = msg.what;
                message.obj = msg.obj;
                switch (msg.what) {
                    case FAILURE:
                        handler.sendEmptyMessage(FAILURE);
                        break;
                    case SUCCESS:
                        handler.sendMessage(message);
                        break;
                    case MESSAGE:
                        Bundle args = new Bundle();
                        args.putString(MSGFROM, ((Message) msg.obj).getFrom());
                        args.putString(MSGBODY, ((Message) msg.obj).getBody());
                        message.setData(args);
                        handler.sendMessage(message);
                        break;
                }
            }
        });
    }

    ////////////////////////////////////////////////////////////////////////
    ////////////////              用户状态            ///////////////////////
    ////////////////////////////////////////////////////////////////////////

    /**
     * 发送当前状态
     *
     * @param status 状态。内部类PRESENCE里有两种状态 ONLINE 和 OFFLINE
     */
    public void setPresence(final int status) {
        //如果没有连接，则返回
        if (getConnection() == null) {
            return;
        }
        //新线程里发送状态封包
        new Thread() {
            @Override
            public void run() {
                switch (status) {
                    case PRESENCE.ONLINE:
                        getConnection().sendPacket(
                                new Presence(Presence.Type.available));
                        break;
                    case PRESENCE.OFFLINE:
                        getConnection().sendPacket(
                                new Presence(Presence.Type.unavailable));
                        break;
                    default:
                        break;
                }
            }
        }.start();
    }

    /**
     * 获取用户名片
     *
     * @return vCard  如成功获取名片返回名片，失败返回null
     *                用户应当使用一个VCard变量接受结果并操作
     *                “单例操作模式”会导致多次更新引发不必要的麻烦
     */
    public VCard getVCard() {
        //如果没有网络连接，直接返回null
        if (getConnection() == null)
            return null;
        //尝试获取名片
        try {
            if (vCard == null) {
                vCard = new VCard();
                vCard.load(getConnection());
            }
        } catch (XMPPException e) {
            vCard = null;
        }
        return vCard;
    }

    /**
     * 设置用户头像
     *
     * @param bytes 图像。从图像/图像文件获取到的字节集
     * @return tag  标签。如设置成功标签返回修改时间，设置失败返回null
     */
    public String setAvatar(byte[] bytes) {
        String tag;

        //确认网络状态
        if(getConnection() == null){
            return null;
        }
        //确认获取名片
        VCard card = getVCard();
        if(card == null){
            return null;
        }
        //开始获取信息
        try {
            //创建图像的唯一标识
            tag = System.currentTimeMillis() + UserInfoBasic.account;
            card.setAvatar(bytes);
            card.setField(AVATAG, tag);
            card.save(getConnection());
        } catch (XMPPException e) {
            tag = null;
        }

        return tag;
    }

    /**
     * 获取用户头像
     *
     * @param dir  可null。非空则将头像输出在文件中
     * @param pref 可null。非空，且需要更新头像则将头像唯一标识更新在配置文件中
     * @return Drawable 头像。如操作成功返回新的头像Drawable。否则返回null
    */
    public Drawable getAvatar(File dir, SharedPreferences pref) {
        try {
            VCard card = getVCard();
            //如果无法下载名片或者没有头像则返回null
            if (card == null || card.getAvatar() == null) {
                return null;
            }

            //如果有配置，没有必要更新则不更新
            if (pref != null) {
                if (card.getField(AVATAG).equals(pref.getString(AVATAG, ""))) {
                    if(dir != null && dir.exists())
                        return null;
                }
            }

            //如有dir，更新文件
            if(dir != null){
                FileOutputStream out = new FileOutputStream(dir);
                Bitmap image = BitmapFactory.decodeByteArray(
                        card.getAvatar(), 0, card.getAvatar().length);
                //压缩成功则输出，失败返回null
                if(image.compress(Bitmap.CompressFormat.JPEG, 100, out)) {
                    out.flush();
                    out.close();
                }
                else {
                    return null;
                }
            }

            //如有配置，更新配置
            if(pref != null){
                pref.edit().putString(AVATAG, card.getField(AVATAG)).apply();
            }

            return Drawable.createFromStream(new ByteArrayInputStream(card.getAvatar()), null);
        } catch (IOException e) {
            return null;
        }
    }

    /**
     * 刷新基础信息
     */
    public void refreshUserBasicData(){
        VCard card = getVCard();
        if(card != null){
            UserInfoBasic.nickName = card.getNickName() == null ? "" : card.getNickName();
        }
    }

    /**
     * 刷新进阶信息
     */
    public void refreshUserEnhancedData(){
        VCard card = getVCard();
        if(card != null){
            UserInfoEnhanced.credit = Double.valueOf(card.getField(CREDIT) == null
                    ? "0" : card.getField(CREDIT));
            UserInfoEnhanced.contributed = Integer.valueOf(card.getField(CONTRIBUTED) == null
                    ? "0" : card.getField(CONTRIBUTED));
            UserInfoEnhanced.shared = Integer.valueOf(card.getField(SHARED) == null
                    ? "0" : card.getField(SHARED));
            UserInfoEnhanced.projs = Integer.valueOf(card.getField(PROJS) == null
                    ? "0" : card.getField(PROJS));
        }
    }


    /**
     * 保存工程信息到本地
     */
    public void saveProjListToLocal(Activity activity) {
        ArrayList list = UserInfoEnhanced.projInfoList;
        if (list == null)
            return;

        FileOutputStream out = null;
        ObjectOutputStream listOut = null;
        try {
            out = activity.openFileOutput(PROJLISTS, Context.MODE_PRIVATE);
            listOut = new ObjectOutputStream(out);
            listOut.writeObject(list);
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                if(out != null)
                    out.close();
                if(listOut != null)
                    listOut.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 载入本地工程信息
     */
    public void loadProjListFromLocal(Activity activity) {
        FileInputStream in = null;
        ObjectInputStream listIn = null;
        try {
            in = activity.openFileInput(PROJLISTS);
            listIn = new ObjectInputStream(in);
            ArrayList<ProjInfo> list = (ArrayList)listIn.readObject();
            if(list != null && list.size() != 0) {
                UserInfoEnhanced.projInfoList = list;
            }
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        } finally {
            try {
                if(in != null)
                    in.close();
                if(listIn != null)
                    listIn.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 保存名片信息
     */
    public void saveVCard() {
        try {
            VCard card = getVCard();
            if (card != null)
                card.save(getConnection());
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }


    /**
     * 设置基础信息
     *
     * @param password 可null。非空则更改登录密码为password
     * @param nickName 可null。非空则更改昵称为nickName
     */
    public void setUserBasicData(String password, String nickName) {
        try {
            if(getConnection() == null || !getConnection().isAuthenticated())
                return;

            if (password != null) {
                getConnection().getAccountManager().changePassword(password);
            }

            VCard card = getVCard();
            if(nickName != null && card != null){
                card.setNickName(nickName);
                card.save(getConnection());
            }
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }

    /**
     * 设置进阶信息
     *
     * @param map 以String为键、String为值存储的键值对
     */
    public void setUserEnhancedData(LinkedHashMap<String, String> map) {
        //如果没有网络连接直接返回
        if(getConnection() == null)
            return;

        try {
            VCard card = getVCard();
            if (card != null) {
                for(Map.Entry<String, String> en : map.entrySet())
                    card.setField(en.getKey(), en.getValue());
                card.save(getConnection());
            }
        } catch (XMPPException e) {
            e.printStackTrace();
        }
    }


    ////////////////////////////////////////////////////////////////////////
    ////////////////              销毁操作            ///////////////////////
    ////////////////////////////////////////////////////////////////////////
    @Override
    protected void finalize() throws Throwable {
        releaseConnection();
        super.finalize();
    }

    ////////////////////////////////////////////////////////////////////////
    ////////////////              特殊工具            ///////////////////////
    ////////////////////////////////////////////////////////////////////////

    /**
     * 根据域名解析服务器IP地址
     */
    public void refreshIPbyHost() {
        new Thread() {
            @Override
            public void run() {
                try {
                    InetAddress address = InetAddress.getByName(HOST_NAME);
                    XSCHelper.HOST = address.getHostAddress();
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
            }
        }.start();
    }

    /**
     * 配置providers的函数
     * ASmack在/META-INF缺少一个smack.providers 文件
     */
    public void configureConnection(ProviderManager pm) {

        // Private Data Storage
        pm.addIQProvider("query", "jabber:iq:private",
                new PrivateDataManager.PrivateDataIQProvider());

        // Time
        try {
            pm.addIQProvider("query", "jabber:iq:time",
                    Class.forName("org.jivesoftware.smackx.packet.Time"));
        } catch (ClassNotFoundException e) {
            Log.w("TestClient",
                    "Can't load class for org.jivesoftware.smackx.packet.Time");
        }

        // Roster Exchange
        pm.addExtensionProvider("x", "jabber:x:roster",
                new RosterExchangeProvider());

        // Message Events
        pm.addExtensionProvider("x", "jabber:x:event",
                new MessageEventProvider());

        // Chat State
        pm.addExtensionProvider("active",
                "http://jabber.org/protocol/chatstates",
                new ChatStateExtension.Provider());
        pm.addExtensionProvider("composing",
                "http://jabber.org/protocol/chatstates",
                new ChatStateExtension.Provider());
        pm.addExtensionProvider("paused",
                "http://jabber.org/protocol/chatstates",
                new ChatStateExtension.Provider());
        pm.addExtensionProvider("inactive",
                "http://jabber.org/protocol/chatstates",
                new ChatStateExtension.Provider());
        pm.addExtensionProvider("gone",
                "http://jabber.org/protocol/chatstates",
                new ChatStateExtension.Provider());

        // XHTML
        pm.addExtensionProvider("html", "http://jabber.org/protocol/xhtml-im",
                new XHTMLExtensionProvider());

        // Group Chat Invitations
        pm.addExtensionProvider("x", "jabber:x:conference",
                new GroupChatInvitation.Provider());

        // Service Discovery # Items
        pm.addIQProvider("query", "http://jabber.org/protocol/disco#items",
                new DiscoverItemsProvider());

        // Service Discovery # Info
        pm.addIQProvider("query", "http://jabber.org/protocol/disco#info",
                new DiscoverInfoProvider());

        // Data Forms
        pm.addExtensionProvider("x", "jabber:x:data", new DataFormProvider());

        // MUC User
        pm.addExtensionProvider("x", "http://jabber.org/protocol/muc#user",
                new MUCUserProvider());

        // MUC Admin
        pm.addIQProvider("query", "http://jabber.org/protocol/muc#admin",
                new MUCAdminProvider());

        // MUC Owner
        pm.addIQProvider("query", "http://jabber.org/protocol/muc#owner",
                new MUCOwnerProvider());

        // Delayed Delivery
        pm.addExtensionProvider("x", "jabber:x:delay",
                new DelayInformationProvider());

        // Version
        try {
            pm.addIQProvider("query", "jabber:iq:version",
                    Class.forName("org.jivesoftware.smackx.packet.Version"));
        } catch (ClassNotFoundException e) {
            // Not sure what's happening here.
        }

        // VCard
        pm.addIQProvider("vCard", "vcard-temp", new VCardProvider());

        // Offline Message Requests
        pm.addIQProvider("offline", "http://jabber.org/protocol/offline",
                new OfflineMessageRequest.Provider());

        // Offline Message Indicator
        pm.addExtensionProvider("offline",
                "http://jabber.org/protocol/offline",
                new OfflineMessageInfo.Provider());

        // Last Activity
        pm.addIQProvider("query", "jabber:iq:last", new LastActivity.Provider());

        // User Search
        pm.addIQProvider("query", "jabber:iq:search", new UserSearch.Provider());

        // SharedGroupsInfo
        pm.addIQProvider("sharedgroup",
                "http://www.jivesoftware.org/protocol/sharedgroup",
                new SharedGroupsInfo.Provider());

        // JEP-33: Extended Stanza Addressing
        pm.addExtensionProvider("addresses",
                "http://jabber.org/protocol/address",
                new MultipleAddressesProvider());

        // FileTransfer
        pm.addIQProvider("si", "http://jabber.org/protocol/si",
                new StreamInitiationProvider());

        pm.addIQProvider("query", "http://jabber.org/protocol/bytestreams",
                new BytestreamsProvider());

        // Privacy
        pm.addIQProvider("query", "jabber:iq:privacy", new PrivacyProvider());
        pm.addIQProvider("command", "http://jabber.org/protocol/commands",
                new AdHocCommandDataProvider());
        pm.addExtensionProvider("malformed-action",
                "http://jabber.org/protocol/commands",
                new AdHocCommandDataProvider.MalformedActionError());
        pm.addExtensionProvider("bad-locale",
                "http://jabber.org/protocol/commands",
                new AdHocCommandDataProvider.BadLocaleError());
        pm.addExtensionProvider("bad-payload",
                "http://jabber.org/protocol/commands",
                new AdHocCommandDataProvider.BadPayloadError());
        pm.addExtensionProvider("bad-sessionid",
                "http://jabber.org/protocol/commands",
                new AdHocCommandDataProvider.BadSessionIDError());
        pm.addExtensionProvider("session-expired",
                "http://jabber.org/protocol/commands",
                new AdHocCommandDataProvider.SessionExpiredError());
    }

    public static class PRESENCE {
        public static final int ONLINE = 1;
        public static final int OFFLINE = 0;

        private PRESENCE() {
        }
    }

}



