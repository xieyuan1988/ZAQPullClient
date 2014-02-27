package com.zaq.client.socket;

import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zaq.client.Constants;
import com.zaq.client.KeepAliveService;
import com.zaq.client.socket.protocol.JsonPacket;
import com.zaq.client.socket.util.PacketUtil;
import com.zaq.client.socket.vo.SendMessage;
import com.zaq.client.socket.vo.ShortMessage;
/**
 * 推送消息
 * @author zaq
 *
 */
public class PullMessage {
	
	private static AtomicBoolean isConnect=new AtomicBoolean(false);
	private static ByteBuffer buffer= ByteBuffer.allocate(4);
	private static AtomicLong MESSAGE_SEND_TAG=new AtomicLong(0);

	public static Map<Long, Callable<Void>> map=new ConcurrentHashMap<Long, Callable<Void>>();
	/**
	 * 处理消息回执
	 * @param msgTag
	 */
	public static void doTAG(long msgTag){
		System.out.println("收到回执"+msgTag);
		if(msgTag==0){
			return ;
		}
		Callable<Void >  callable=map.remove(msgTag);
		
//		if(null==callable){
//			try {
//				Thread.sleep(100);
//			} catch (InterruptedException e) {}
//			callable=map.remove(msgTag);
//		}
		
		if(null!=callable){
			try {
				callable.call();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}else{
			System.out.println("map没有"+msgTag);
		}
		
	}
	
	/**
	 * 推送登陆消息
	 * @param jsonVal
	 * @return
	 */
	public synchronized static boolean pullLogin(String jsonVal){
		return pull(jsonVal,null,true);
	}
	
	/**
	 * 推送
	 * @param obj JsonPacket数据包  或 回执
	 * @param msgTag	回执编号
	 * @param isLogin 是否是登陆
	 * @return 返回发送成功状态 true:成功 false:失败
	 */
	private static boolean pull(Object obj,Long msgTag ,boolean isLogin){
		
		SocketChannel  channel=KeepAliveService.getClientSC();
		if(null==channel){
			return false;
		}
		
		if(!channel.isConnected()){
			return false;
		}
		
		if(isConnect.get()||isLogin){
			/**
			 * 推送内容转成服务器编码
			 */
//			try {//确保消息发送完成
				if(obj instanceof String){
					ByteBuffer byteBuffer=PacketUtil.encodePacketToBB((String)obj);
					//不需要回执的推送
					if(null==msgTag){
						return pull(channel,byteBuffer);//立刻发送且回复
					}
					
					// 需要等待回执
					final Lock lock=new ReentrantLock();
					final Condition condition=lock.newCondition();
					//先放入map
					map.put(msgTag, new Callable<Void>() {
						@Override
						public Void call() throws Exception {
							lock.lock();
							condition.signal();
							lock.unlock();
							return null;
						}
					});
					//再发送
					if(!pull(channel,byteBuffer)){
						map.remove(msgTag);//本地失败立刻返回 并清除map键
						return false;
					}
					
					System.out.println(map.size()+  "map put"+msgTag);
					//本地成功 等待回执
					boolean retVal=false;
					lock.lock();
					try {
						condition.await(Constants.TIMEOUT_DELAY, TimeUnit.MILLISECONDS);
						if(!map.containsKey(msgTag)){
							retVal=true;
						}
					} catch (InterruptedException e) {
						retVal=false;
					}finally{
						lock.unlock();
					}
					
					return retVal;
				}else{
					//发送回执
					ByteBuffer byteBuffer=ByteBuffer.allocate(8);
					byteBuffer.putLong((Long)obj).flip();
					
					return pull(channel,byteBuffer);
				}
//			} catch (Exception e) {
//				e.printStackTrace();
//				System.out.println("连接断开");
//				
//				return false;
//			}
		}else {
			return false;
		}
		
	}
	/**
	 * 同步发送包
	 * @param channel
	 * @param head
	 * @param body
	 * @throws IOException
	 */
	private static synchronized boolean pull(SocketChannel channel,ByteBuffer body){
		buffer.putInt(body.remaining()+4).flip();//+4
		try {
			while(buffer.hasRemaining()){
				channel.write(buffer);
			}
			while(body.hasRemaining()){
				channel.write(body);
			}
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		body.clear();
		buffer.clear();
		return true;
	}
	/**
	 * 推送
	 * @param <T>
	 * @param packet JsonPacket数据包
	 * @param type  JsonPacket的Type
	 * @return 返回发送成功状态 true:成功 false:失败
	 */
	public  static <T> boolean pull(JsonPacket<T> packet ,Type type)throws Exception{
		synchronized (packet) {
			long msgTag=MESSAGE_SEND_TAG.incrementAndGet();
			packet.setMsgTAG(msgTag);//放置消息TAG
			Gson OUTGson=new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setDateFormat(Constants.DATE_FORMAT_FULL).create();

			return pull(OUTGson.toJson(packet,type),msgTag,false);
		}
	}
	
	/**
	 * 发送回执给服务器
	 * @param msgTag
	 */
	public static void pullTag(long msgTag){
		pull(msgTag,null,false);
	}
	
	public static boolean isConnect() {
		return isConnect.get();
	}
	/**
	 * 设置连接成功
	 * @param isConnect
	 */
	public synchronized static void  setConnect(boolean isConnect) {
		PullMessage.isConnect.set(isConnect);
	}
	
	public static void main(String[] args) throws Exception {
		JsonPacket<SendMessage<ShortMessage>> jsonPacket=new JsonPacket<SendMessage<ShortMessage>>("ee");
		pull(jsonPacket,new TypeToken<JsonPacket<SendMessage<ShortMessage>>>() {
		}.getType());
	}

}
