package com.zaq.client.socket;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import org.apache.commons.lang.StringUtils;

import com.zaq.client.socket.protocol.ZAQprotocolException;
import com.zaq.client.socket.util.PacketUtil;
/**
 * 
 * @author zaq
 * 解析数据包
 */
public class ReciveMessage {
	//分配内存   
	private static int BLOCK=1024*10;
	private static ByteBuffer buffer = ByteBuffer.allocate(BLOCK);  
//	private static Logger logger=Logger.getLogger(ReciveMessage.class);
//	private static AtomicInteger atomicInteger=new AtomicInteger();
	/**
	 * 同步解析数据包
	 * @param channel
	 * @return
	 */
	public synchronized static boolean recive(SocketChannel channel ){
		int count=0;  //读到的字节数
		ByteArrayOutputStream arrayOutputStream=null;//解析到的字节存放内存流
		int packetLength=0;	//消息体长
		boolean ishead=true; //消息头         
		Long tag=null;		//回执
		 // 解析开始
        try {
        	int position=0;//buffer的初始位置标记
        	
        	if(buffer.position()!=0){//上一次有未读完的字节
				position=buffer.position();//记录上一次未读内容的开始位置
				buffer.position(position+buffer.remaining());//定位上一次未尾
//				System.out.println((atomicInteger.get())+"当前位置postion:"+position+"buffer.remaining():"+buffer.remaining()+"buffer.limit():"+buffer.limit());
			}
        	//如果buffer上有内容就不读，先处理上一次未读
			while((position!=0)||(count = channel.read(buffer))> 0){
				if(position==0){
					buffer.flip();
				}else{
					buffer.position(position);//定位到上一次未读的位置
				}
				 
				if(ishead){//是否是开始解析
					
					if(buffer.remaining()<4){//上一次或传送过来的字节不能小于4个字节(包头int的字节长度) 和 回执long 8个字节
						position=0;
						ByteBuffer tmp=buffer.slice();//小于4个字节将其重新放到一个新的buffer中下一次再读
						buffer.clear();
						buffer.put(tmp);
						continue;
					}
					
					packetLength= buffer.getInt()-4;//获取包头上将要读取的包体长度
					
					if(packetLength==8){//判断为回执信息
						if(buffer.remaining()<8){
							position=0;
							ByteBuffer tmp=buffer.slice();//回执信息小于8个字节将其重新放到一个新的buffer中下一次再读
							buffer.clear();
							buffer.put(tmp);
							continue;
						}
						tag=buffer.getLong();
						break;//解析结束
					}
					
					arrayOutputStream=new ByteArrayOutputStream();
					ishead=false;//解析进行中。。。。。。。
				}
				
				if(packetLength>=buffer.remaining()){//buffer上的字节全是正在解析的包体内容
					packetLength-=buffer.remaining();
					
					byte[] byteArray=new byte[buffer.remaining()];
					
					buffer.get(byteArray, 0, buffer.remaining());
					
					arrayOutputStream.write(byteArray);//写到内存字节流上
					position=0;
					if(packetLength==0){
						break;	//解析结束
					}
				}else{
					//将buffer上所属正在解析包的内容写到内存字节流上
					byte[] byteArray=new byte[packetLength];
					buffer.get(byteArray, 0, packetLength);
					
					arrayOutputStream.write(byteArray);
					break; //解析结束
				}
				
			    buffer.clear();  //解析结束 清除buffer上的内容
			    position=0;
			}
		} catch (IOException e) {
			log("连接断开",e);
			return false;
		}
		
		if(count==-1){
			log("连接断开");
			return false;
		}
		
		if(null!=tag){//发送的message(及子类 非room请求) 回执操作
			PullMessage.doTAG(tag);
			return true;
		}
		
		//反编译包体内容   接收到空  java.lang.NullPointerException   KeepAliveService会catch而断开连接
		String reJsonVal=PacketUtil.decodePacketToCB(ByteBuffer.wrap(arrayOutputStream.toByteArray()));
		
		try {
			arrayOutputStream.close();
		} catch (IOException e) {
			log("内存字节流关闭失败", e);
		}
		
//		log("接收到的消息"+(atomicInteger.getAndIncrement())+"："+reJsonVal);
		log("接收到的消息"+"："+reJsonVal);
        
        if(StringUtils.isEmpty(reJsonVal)){
        	return true;
        }
        
        try {
        	 //处理接收到的信息
			SocketClientProcess.parseJsonPacket(reJsonVal);
		} catch (ZAQprotocolException e1) {
			log(e1.getMessage(), e1);
		}
		
		return true;
	}
	
	private static void log(String message,Throwable... es)
	{
//		logger.info( message);
		System.out.println(message);
		for(Throwable e:es){
			System.out.println(e.getMessage());
		}
	}
}
