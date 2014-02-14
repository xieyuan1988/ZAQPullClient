package com.zaq.client;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zaq.client.socket.PullMessage;
import com.zaq.client.socket.protocol.JsonPacket;
import com.zaq.client.socket.vo.FileMessage;
import com.zaq.client.socket.vo.SendManyMessage;
import com.zaq.client.socket.vo.SendMessage;
import com.zaq.client.socket.vo.ShortMessage;
/**
 * 测试发送消息demo
 * @author zaq
 *
 */
public class ClinetRunTest {
	public static void main(String[] args) throws IOException, InterruptedException {
//		System.out.println((char)99);
//		System.out.println(new File("lib/gson-1.3.jar").length());
//		System.out.println((1024*1024*10)>Integer.MAX_VALUE);
		//启动程序，请查看内部步骤
		ClientRun.start();
		Thread.sleep(2000);
		
		//群法消息
		SendManyMessage<ShortMessage> sendMessage=new SendManyMessage<ShortMessage>();
		
		sendMessage.getUserIds().add(5l);
		sendMessage.getUserIds().add(6l);
		
		sendMessage.getUserFullnames().add("mzhu");
		sendMessage.getUserFullnames().add("wzhu");
		
		sendMessage.setMessage(new ShortMessage(-1l, "系统", "这是个群发的test"));
		
        final Type type=new TypeToken<JsonPacket<SendManyMessage<ShortMessage>>>(){}.getType();
        Gson gson=new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
        final JsonPacket<SendManyMessage<ShortMessage> > packet=new JsonPacket<SendManyMessage<ShortMessage> >(sendMessage);
       
        boolean sendSuccess=false;
        /**
         * 发送消息
         */
        final String msg=gson.toJson(packet,type);
        try {
			sendSuccess= PullMessage.pull(packet,type); 
        } catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
			for(int i=0;i<2000;i++){
				new Thread(){

					@Override
					public void run() {
						try {
							PullMessage.pull(packet,type);
						} catch (Exception e) {
							e.printStackTrace();
						} 
					}
					
				}.start();
			}
		
//        
        
//        sendSuccess=PullMessage.pull(packet,type);  
//        System.out.println("推送："+sendSuccess);
        //发送文件消息
        
        SendMessage<FileMessage> messageFile=new SendMessage<FileMessage>();
        messageFile.setUserId(-1l);
        messageFile.setUserFullname("系统");
        FileMessage fileMessage=new FileMessage("properties", new File("lib/gson-1.3.jar"));
        messageFile.setMessage(fileMessage);
        
        JsonPacket<SendMessage<FileMessage>> packetFile=new JsonPacket<SendMessage<FileMessage>>(messageFile);
        
        Type type1=new TypeToken<JsonPacket<SendMessage<FileMessage>>>(){}.getType();
        
//        sendSuccess= PullMessage.pull(gson.toJson(packetFile,type1)); 
//       final  String fielMsg=gson.toJson(packetFile,type1);
//        System.out.println("推送111："+fielMsg);
//        System.out.println("推送："+sendSuccess);
        
//      for(int i=0;i<100;i++){
//    	new Thread(){
//
//			@Override
//			public void run() {
//				System.out.println(Thread.currentThread().getName()+"推送："+PullMessage.pull(fielMsg));; 
//			}
//    		
//    	}.start();
//    }
        
//        sendSuccess=PullMessage.pull(packet,type);  
//        System.out.println("推送："+sendSuccess);
        
	}
}
