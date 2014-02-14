package com.zaq.client.socket.vo;

import java.util.Date;

import com.google.gson.annotations.Expose;
/**
 * 短消息bean
 * @author zaq
 *
 */
public class ShortMessage extends Message{

	@Expose
	protected String content;
	
	public ShortMessage(){}
	
	public ShortMessage(String content){
		this.msgType=MSG_TYPE_PERSONAL;
		this.content=content;
		this.sendTime=new Date();
	}
	public ShortMessage(Long senderId,String sender,String content){
		this.senderId=senderId;
		this.sender=sender;
		this.msgType=MSG_TYPE_PERSONAL;
		this.content=content;
		this.sendTime=new Date();
	}
	public ShortMessage(Long senderId,String sender,String content,Short msgType,Date sendTime){
		this.senderId=senderId;
		this.sender=sender;
		this.msgType=msgType;
		this.content=content;
		this.sendTime=sendTime;
	}

	public ShortMessage(Long senderId, String sender, Short msgtype, String content) {
		this.senderId=senderId;
		this.sender=sender;
		this.msgType=msgtype;
		this.content=content;
		this.sendTime=new Date();
		
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}
}
