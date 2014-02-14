package com.zaq.test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import org.apache.log4j.Logger;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zaq.client.ClientRun;
import com.zaq.client.socket.PullMessage;
import com.zaq.client.socket.protocol.JsonPacket;
import com.zaq.client.socket.util.ThreadPool;
import com.zaq.client.socket.vo.SendManyMessage;
import com.zaq.client.socket.vo.ShortMessage;

public class Test1 {
	private static Logger logger=Logger.getLogger(Test1.class);
	public static void main(String[] args) throws Exception {
		ClientRun.start();
		Thread.sleep(3000);
		
		//群法消息
		SendManyMessage<ShortMessage> sendMessage=new SendManyMessage<ShortMessage>();
		
//		sendMessage.getUserIds().add(471l);
//		sendMessage.getUserIds().add(446l);
//		
//		sendMessage.getUserFullnames().add("mzhu");
//		sendMessage.getUserFullnames().add("yjzhang415");
		
		sendMessage.getUserIds().add(5l);
		sendMessage.getUserIds().add(256l);
		
		sendMessage.getUserFullnames().add("mzhu");
		sendMessage.getUserFullnames().add("qlju");
		
		sendMessage.setMessage(new ShortMessage(-1l, "系统", "这是个群发的压力test"));
		
       final Type type=new TypeToken<JsonPacket<SendManyMessage<ShortMessage>>>(){}.getType();
        Gson gson=new GsonBuilder().excludeFieldsWithoutExposeAnnotation().create();
       final JsonPacket<SendManyMessage<ShortMessage> > packet=new JsonPacket<SendManyMessage<ShortMessage> >(sendMessage);
       final AtomicInteger atomicInteger=new AtomicInteger();
       final AtomicInteger atomicFail=new AtomicInteger();
        /**
         * 发送消息
         */
        final CountDownLatch countDownLatch=new CountDownLatch(5000);
        final String msg=gson.toJson(packet,type);
        long sTime=System.currentTimeMillis();
        logger.info("消息发送开始...");
        for(int i=0;i<1;i++){

        	ThreadPool.execute(new Runnable() {
				
        		public void run() {
        			try {
						if(PullMessage.pull(packet,type)){
							atomicInteger.incrementAndGet();
							logger.info("成功消息发送完毕.."+atomicInteger.get());
						}else{
							atomicFail.incrementAndGet();
							logger.info("失败消息发送完毕.."+atomicFail.get());
						}
					} catch (Exception e) {
						e.printStackTrace();
					}
					
					countDownLatch.countDown();
				}
			})	;
        		
        }
        
//        for(int i=0;i<2000;i++){
//
//        	new Thread(){
//        		public void run() {
//        			if(PullMessage.pull(msg)){
//        				atomicInteger.incrementAndGet();
//        			}else{
//        				atomicFail.incrementAndGet();
//        				logger.info("失败消息发送完毕.."+atomicFail.get());
//        			}
//					
//					countDownLatch.countDown();
//				}
//        	}.start();
//        		
//        }
        
        countDownLatch.await();
        logger.info("成功消息发送完毕.."+atomicInteger.get()+".用时：："+(System.currentTimeMillis()-sTime)+"ms");
        logger.info("失败消息发送完毕.."+atomicFail.get()+".用时：："+(System.currentTimeMillis()-sTime)+"ms");
	}
}
