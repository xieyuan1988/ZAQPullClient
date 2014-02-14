package com.zaq.client.socket.vo;

import java.util.Date;

import com.google.gson.annotations.Expose;

/**
 * 聊天室短消息
 * @author zaq
 *
 */
public class RoomMessage extends ShortMessage{
	@Expose
	private Room room;

	public RoomMessage(){}
	/**
	 * 发送聊天室消息
	 * @param senderId
	 * @param sender
	 * @param content
	 * @param msgType
	 * @param sendTime
	 * @param room    必须字段 ID ,title
	 */
	public RoomMessage(Long senderId,String sender,String content,Short msgType,Date sendTime,Room room){
		this.senderId=senderId;
		this.sender=sender;
		this.msgType=msgType;
		this.content=content;
		this.room=room;
		this.sendTime=sendTime;
	}
	
	/**
	 * @return the room
	 */
	public Room getRoom() {
		return room;
	}

	/**
	 * @param room the room to set
	 */
	public void setRoom(Room room) {
		this.room = room;
	}
}
