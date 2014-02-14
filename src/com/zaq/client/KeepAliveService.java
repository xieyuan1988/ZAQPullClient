package com.zaq.client;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Random;
import java.util.concurrent.atomic.AtomicBoolean;

import com.zaq.client.socket.PullMessage;
import com.zaq.client.socket.ReciveMessage;
import com.zaq.client.socket.listener.ClientRegister;
import com.zaq.client.socket.listener.IBaseListener;
import com.zaq.client.socket.protocol.ZAQprotocolException;
import com.zaq.client.socket.util.ClientUtil;
import com.zaq.client.socket.vo.Login;

/**
 * 推送服务
 * @author zaq
 *
 */
public class KeepAliveService
{
	private static final String HOST = ClientUtil.getPropertity("server.pullSocketIP");
	private static final int PORT =Integer.valueOf(ClientUtil.getPropertity("server.pullSocketPort"));
	private static ConnectionThread mConnection;//当前的连接线程
	private static AtomicBoolean singalStop=new AtomicBoolean(false);			//是否发送服务关闭指示
	private static KeepAliveService aliveService=null;
	
	private KeepAliveService(){};
	/**
	 * 启动连接推送服务
	 */
	public synchronized static void initAndReStart(){
		singalStop.set(false);
		if(null==mConnection){
			if(null==aliveService){
				aliveService=new KeepAliveService();
			}
			aliveService.start();
		}
	}
	/**
	 * 获取客户端通道
	 * @return
	 */
	public static SocketChannel getClientSC(){
		if(null==mConnection){
			return null;
		}
		return mConnection.getClinetSC();
	}
	
	private synchronized void log(String message)
	{
//		logger.info( message);
		System.out.println(message);
	}

	/**
	 * 启动推送服务
	 */
	private void start()
	{
		mConnection = new ConnectionThread(HOST, PORT);
		mConnection.start();
	}
	/**
	 * 停止推送服务
	 */
	public static synchronized void stop()
	{
		singalStop.set(true);//接收到停止指示
		if (mConnection != null)
		{
			try {
				mConnection.mSocketChannel.close();
				mConnection.selector.close();
			} catch (IOException e) {
				e.printStackTrace();
			}
			mConnection = null;
		}
	}
	/**
	 * 重新连接
	 * @param schedul 是否为调度任务  
	 */
	private synchronized void reconnectIfNecessary(boolean schedul)
	{
		if(singalStop.get()){
			return;
		}
		if ( mConnection == null)
		{
			mConnection = new ConnectionThread(HOST, PORT);
			int delay=0;
			if(schedul){
				/**
				 * 一分钟内随机连接一次
				 */
				delay=new Random().nextInt(30*1000);
			
			}
			log("Reconnecting delay:"+delay+"ms...");
			if(delay!=0){
				try {
					Thread.sleep(delay);
				} catch (InterruptedException e) {
					log(e.getMessage());
				}
			}
			
			mConnection.start();
		}
	}

	/**
	 * socket连接线程
	 * @author zaq
	 *
	 */
	private class ConnectionThread extends Thread
	{
		private  SocketChannel mSocketChannel;
		private  String mHost;
		private  int mPort;
		private String loninJsonVal;
		private volatile boolean mAbort = false;//socket取消开关
		private  boolean finishConnect = false; //请求连接的状态
		private  Selector selector;
		public SocketChannel getClinetSC(){
			return mSocketChannel;
		}
		
		public ConnectionThread(String host, int port)
		{
			mHost = host;
			mPort = port;
			loninJsonVal=Login.getCurrentLogin();//登陆推送信息
		}

		public void run()
		{
			if(null!=mSocketChannel&&mSocketChannel.isConnected()){
				log("防止重复连接");
				return;
			}
			try {
                //打开Socket通道   
				mSocketChannel = SocketChannel.open();   
                //设置为非阻塞模式   
				mSocketChannel.configureBlocking(false);   
                //打开选择器   
                selector = Selector.open();   
                //注册连接服务端socket动作   
                mSocketChannel.register(selector, SelectionKey.OP_CONNECT);   
                //连接   
                InetSocketAddress ip = new InetSocketAddress(mHost, mPort);
                mSocketChannel.connect(ip);   
                
                while(!mAbort) {   
                	//设置超时时间        超时则断开
                    int n=selector.select(Constants.TIMEOUT_DELAY+1000*Integer.valueOf(ClientUtil.getPropertity("IdleStatus.BOTH_IDLE")));   
                   
                    if(n<=0){
                    	throw new ZAQprotocolException("连接超时，断开网络。。。。");
                    }
                    
                    Iterator iter = selector.selectedKeys().iterator();   

                    while (iter.hasNext()) {   
                        SelectionKey key = (SelectionKey) iter.next();   
                        if(!key.isValid()){
                        	continue;
                        }
                        if (key.isConnectable()) {   
                            SocketChannel channel = (SocketChannel) key   
                                    .channel();   
                            if (channel.isConnectionPending())
								try {
									channel.finishConnect();
									
									mAbort=false;
									finishConnect=true;
								} catch (Exception e) {
									log(e.getMessage());
									mAbort=true;
									break;
								}   
                            //发送登陆信息
							if(!PullMessage.pullLogin(loninJsonVal)){
								log("发送登陆信息失败");
								mAbort=true;
                            	break;
							}

                            channel.register(selector, SelectionKey.OP_READ);   
                        } else if (key.isReadable()) {   
                            SocketChannel channel = (SocketChannel) key   
                                    .channel();   
                            
                            if(!ReciveMessage.recive(channel)){//密码不正确or服务器挂掉or挤下线
                            	mAbort=true;
                            	break;
                            }
                        }   
                        iter.remove(); //移除选择key
                    }   
                }   
                
			} catch (Exception e) {
				log("Unexpected  error: " + e.toString());
				e.printStackTrace();
			}finally{
				scRelease();
			}
		}
		
		/**
		 * 释放资源
		 */
		public void scRelease()
		{
			mAbort=true;
			try {
				selector.close();	   //释放资源
				mSocketChannel.close();//释放资源
			} catch (Exception e) {
				log("通道关闭失败Unexpected I/O error: " + e.toString());
			}
			mConnection=null;//note 重新连接前，必须失去引用 ，
//			reconnectIfNecessary(true);//调度重新连接    改用了监听方式
			PullMessage.setConnect(false);
			
			if(finishConnect){
				//触发断开事件
				IBaseListener listener= (IBaseListener)ClientRegister.getListener(ClientRegister.MYLISTENER);
				listener.disconnect();
			}else{
				//连接失败
				reconnectIfNecessary(true);//调度重新连接  
			}
			
		}
	}
}