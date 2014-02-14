package com.zaq.client.socket.vo;

import java.util.Date;

import com.google.gson.annotations.Expose;
import com.zaq.client.socket.util.ClientUtil;
import com.zaq.client.socket.util.UUIDGenerator;

public abstract class Message {
	protected Long messageId;
	protected Long senderId;
	protected String sender;
	@Expose
	protected Short msgType;
	protected Date sendTime;
	@Expose
	protected String typeStr$type;
	@Expose //手机端请去掉此注解
	protected String messageUUID;//发送消息的uuid 保证推送消息的完整性
	
	public static final Short MSG_TYPE_PERSONAL = Short
			.valueOf((short) 1);
	public static final Short MSG_TYPE_CALENDAR = Short
			.valueOf((short) 2);
	public static final Short MSG_TYPE_PLAN = Short.valueOf((short) 3);
	public static final Short MSG_TYPE_TASK = Short.valueOf((short) 4);
	public static final Short MSG_TYPE_SYS = Short.valueOf((short) 5);
	public static final Short MSG_TYPE_IM_ = 6;		//聊天室群聊的消息
	public static final Short MSG_TYPE_IM_ALERT = 7;		//聊天室的提示消息
	public static final Short MSG_TYPE_IM_ROOM_CREATE = 8;		//创建聊天室的提示消息
	public static final Short MSG_TYPE_IM_ROOM_ADDUSER = 9;		//聊天室添加人的提示消息
	public static final Short MSG_TYPE_IM_ROOM_QUERY = 10;		//查询聊天室的提示消息
	public static final Short MSG_TYPE_IM_PTP = 11;		//单对单私聊的消息
	
	
	public Message(){
		this.typeStr$type=this.getClass().getSimpleName();
		if(!ClientUtil.osIsAndroid()){
			this.messageUUID=UUIDGenerator.getUUID();
		}
	}
	
	public Long getMessageId() {
		return messageId;
	}
	public void setMessageId(Long messageId) {
		this.messageId = messageId;
	}
	public Long getSenderId() {
		return senderId;
	}
	public void setSenderId(Long senderId) {
		this.senderId = senderId;
	}
	public String getSender() {
		return sender;
	}
	public void setSender(String sender) {
		this.sender = sender;
	}
	public Short getMsgType() {
		return msgType;
	}
	public void setMsgType(Short msgType) {
		this.msgType = msgType;
	}
	public Date getSendTime() {
		return sendTime;
	}
	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}
	public String getMessageUUID() {
		return messageUUID;
	}
	public void setMessageUUID(String messageUUID) {
		this.messageUUID = messageUUID;
	}
	/**
	 * 返回 发送的消息类别
	 * @return
	 */
	public String getTypeStr(){
		return this.getClass().getSimpleName();
	}

	public String getTypeStr$type() {
		return typeStr$type;
	}

	public void setTypeStr$type(String typeStr$type) {
		this.typeStr$type = typeStr$type;
	}
	
	public static void main(String[] args) {
		System.getProperties().list(System.out);
	}
}
