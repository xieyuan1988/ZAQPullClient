package com.zaq.client.socket.vo;

import java.util.Date;

import com.google.gson.annotations.Expose;
import com.zaq.client.Constants;
/**
 * 消息发送bean
 * @author zaq
 *
 */
public class SendMessage<T extends Message>{ 
	public static final Short FLAG_READ = Short.valueOf((short) 1);
	public static final Short FLAG_UNREAD = Short.valueOf((short) 0);
	private Long receiveId;//这是主键 
	@Expose
	private T message;
	@Expose
	private Long userId;	//接收人
	@Expose
	private String userFullname;
	@Expose
	private Short readFlag;
	private Short delFlag;

	public SendMessage(){}
	
	public SendMessage<ShortMessage> newShortSendMessage(Long toUserId,String toUserFullname,Message message){
		SendMessage<ShortMessage> sendMessage=new SendMessage<ShortMessage>();
		sendMessage.setUserId(toUserId);
		sendMessage.setUserFullname(toUserFullname);
		sendMessage.setDelFlag(Constants.FLAG_UNDELETED);
		sendMessage.setReadFlag(FLAG_UNREAD);
		sendMessage.setMessage((ShortMessage)message);
		
		return sendMessage;
	}
	
	public static SendMessage<ShortMessage> newShortSendMessage(Long toUserId,String toUserFullname,Long fromUserId,String fromUserFullname,String content){
		SendMessage<ShortMessage> sendMessage=new SendMessage<ShortMessage>();
		sendMessage.setUserId(toUserId);
		sendMessage.setUserFullname(toUserFullname);
		sendMessage.setDelFlag(Constants.FLAG_UNDELETED);
		sendMessage.setReadFlag(FLAG_UNREAD);
		sendMessage.setMessage(new ShortMessage(fromUserId, fromUserFullname, content));
		return sendMessage;
	}
	
	public static SendMessage<ShortMessage> newShortSendMessage(Long toUserId,String toUserFullname,Long fromUserId,String fromUserFullname,Short msgtype,String content){
		SendMessage<ShortMessage> sendMessage=new SendMessage<ShortMessage>();
		sendMessage.setUserId(toUserId);
		sendMessage.setUserFullname(toUserFullname);
		sendMessage.setDelFlag(Constants.FLAG_UNDELETED);
		sendMessage.setReadFlag(FLAG_UNREAD);
		sendMessage.setMessage(new ShortMessage(fromUserId, fromUserFullname,msgtype, content));
		return sendMessage;
	}
	
	public static SendMessage<ShortMessage> newShortSendMessage(Long toUserId,String toUserFullname,String content){
		SendMessage<ShortMessage> sendMessage=new SendMessage<ShortMessage>();
		sendMessage.setUserId(toUserId);
		sendMessage.setUserFullname(toUserFullname);
		sendMessage.setDelFlag(Constants.FLAG_UNDELETED);
		sendMessage.setReadFlag(FLAG_UNREAD);
		sendMessage.setMessage(new ShortMessage(null,null, content));
		return sendMessage;
	}
	
	public static SendMessage<ShortMessage> newShortSendMessage(Short readFlag ,Long senderId,String sender,String content,Short msgType,Date sendTime){
		SendMessage<ShortMessage> sendMessage=new SendMessage<ShortMessage>();
		sendMessage.setReadFlag(readFlag);
		sendMessage.setMessage(new ShortMessage(senderId,sender, content,msgType,sendTime));
		
		return sendMessage;
	}

	public static SendMessage<RoomMessage> newRoomSendMessage(Short readFlag ,Long senderId,String sender,String content,Short msgType,Date sendTime,Room room){
		SendMessage<RoomMessage> sendRoomMessage=new SendMessage<RoomMessage>();
		sendRoomMessage.setReadFlag(readFlag);
		sendRoomMessage.setMessage(new RoomMessage(senderId,sender, content,msgType,sendTime,room));
		
		return sendRoomMessage;
	}

	public static SendMessage<RoomMessage> newRoomSendMessage(Long senderId,String sender,String content,Short msgType,Date sendTime,Room room){
		SendMessage<RoomMessage> sendRoomMessage=new SendMessage<RoomMessage>();
		sendRoomMessage.setMessage(new RoomMessage(senderId,sender, content,msgType,sendTime,room));
		
		return sendRoomMessage;
	}
	
	public Message getMessage() {
		return message;
	}

	public void setMessage(T message) {
		this.message = message;
	}

	public Long getUserId() {
		return this.userId;
	}

	public void setUserId(Long userId) {
		this.userId = userId;
	}

	public String getUserFullname() {
		return this.userFullname;
	}

	public void setUserFullname(String userFullname) {
		this.userFullname = userFullname;
	}

	public Short getReadFlag() {
		return this.readFlag;
	}

	public void setReadFlag(Short readFlag) {
		this.readFlag = readFlag;
	}

	public Short getDelFlag() {
		return this.delFlag;
	}

	public void setDelFlag(Short delFlag) {
		this.delFlag = delFlag;
	}

	public Long getReceiveId() {
		return receiveId;
	}

	public void setReceiveId(Long receiveId) {
		this.receiveId = receiveId;
	}
}
