package com.zaq.test;

import java.util.Date;

import com.google.gson.reflect.TypeToken;
import com.zaq.client.ClientRun;
import com.zaq.client.socket.PullMessage;
import com.zaq.client.socket.protocol.JsonPacket;
import com.zaq.client.socket.vo.Message;
import com.zaq.client.socket.vo.Room;
import com.zaq.client.socket.vo.RoomMessage;
import com.zaq.client.socket.vo.SendMessage;

public class Test3 {
	public static void main(String[] args) throws Exception {
		ClientRun.start();
		Thread.sleep(3000);
		//创建聊天室
		Room room=new Room();
		
		room.getAddUserId().add(365l);
		room.getAddUserId().add(254l);
		room.getAddUserId().add(256l);
		room.getAddUserId().add(446l);
		room.getAddUserId().add(102l);
		
		room.getAddUserFullNames().add("刘政");
		room.getAddUserFullNames().add("费志浩");
		room.getAddUserFullNames().add("居哥");
		room.getAddUserFullNames().add("章英杰");
		room.getAddUserFullNames().add("系统管理员");
		
		
		JsonPacket<Room> jsonPacket=new JsonPacket<Room>(room);
		
		PullMessage.pull(jsonPacket, new TypeToken<JsonPacket<Room>>(){}.getType());
		
		Room room2=new Room(1L);
		room2.setTitle("信息中心的聊天室");
		SendMessage<RoomMessage> message=SendMessage.newRoomSendMessage(6l, "朱伟", "这是一个聊天室的消息zrdsgtargdrgrgergergregergergergergergergergergergergergergerger有的遥遥遥遥遥遥遥的23", Message.MSG_TYPE_IM_, new Date(), room2);
		
		JsonPacket<SendMessage<RoomMessage>> packet=new JsonPacket<SendMessage<RoomMessage>>(message);
		
		PullMessage.pull(packet, new TypeToken<JsonPacket<SendMessage<RoomMessage>> >(){}.getType());
		
	}
}
