package com.zaq.test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zaq.client.ClientRun;
import com.zaq.client.socket.protocol.JsonPacket;
import com.zaq.client.socket.vo.SendManyMessage;
import com.zaq.client.socket.vo.ShortMessage;

public class Test2 {
	public static void main(String[] args) throws IOException {
		ClientRun.start();
	SendManyMessage<ShortMessage> sendMessage=new SendManyMessage<ShortMessage>();
		
		sendMessage.getUserIds().add(471l);
		sendMessage.getUserIds().add(446l);
		
		sendMessage.getUserFullnames().add("mzhu");
		sendMessage.getUserFullnames().add("yjzhang415");
		
//		sendMessage.getUserIds().add(5l);
//		sendMessage.getUserIds().add(6l);
//		
//		sendMessage.getUserFullnames().add("mzhu");
//		sendMessage.getUserFullnames().add("wzhu");
		
		sendMessage.setMessage(new ShortMessage(-1l, "系统", "这是个群发的压力test"));
		
        Type type=new TypeToken<JsonPacket<SendManyMessage<ShortMessage>>>(){}.getType();
        Gson gson=new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        JsonPacket<SendManyMessage<ShortMessage> > packet=new JsonPacket<SendManyMessage<ShortMessage> >(sendMessage);
       final AtomicInteger atomicInteger=new AtomicInteger();
       final AtomicInteger atomicFail=new AtomicInteger();
        /**
         * 发送消息
         */
        final CountDownLatch countDownLatch=new CountDownLatch(5000);
        final String msg=gson.toJson(packet,type);
        
        System.out.println(msg.getBytes().length);
	}
}
