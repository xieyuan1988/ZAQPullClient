package com.zaq.client;

import java.io.IOException;
import java.util.Properties;

import com.zaq.client.socket.ZAQRouter;
import com.zaq.client.socket.listener.ClientRegister;
import com.zaq.client.socket.listener.IBaseListener;
import com.zaq.client.socket.protocol.JsonPacket;
import com.zaq.client.socket.util.ClientUtil;
import com.zaq.client.socket.util.IAndroidInit;
import com.zaq.client.socket.util.ThreadPool;
/**
 * 测试初始化demo
 * @author zaq
 *
 */
public class ClientRun {
	/**
	 * 客户端启动  4-5步
	 * @throws IOException
	 */
	public static void start() throws IOException{
		/**
		 * android 端初始化系统配置  详情请见两个配置文件
		 */
		IAndroidInit androidInit=new IAndroidInit() {
			@Override
			public void zaqRouterProInit(Properties props) {
				//详情router.properties
			}
			@Override
			public void clientUtilProInit(Properties props) {
				//client.password client.username server.pullSocketPort server.pullSocketIP  IdleStatus.BOTH_IDLE必须
			}
		};
		
		/**
		 * 初始化系统变量
		 */
		ClientUtil.init();
		/**
		 * 初始化路由器
		 */
		ZAQRouter.getRouter().init();
		/**
		 * 注册监听事件
		 */
		ClientRegister.register(ClientRegister.MYLISTENER, new IBaseListener() {
			
			@Override
			public void unfound(JsonPacket<String> jsonPacket) {
				
			}
			
			@Override
			public void pullerror(JsonPacket<String> jsonPacket) {
				
			}
			
			@Override
			public void loginSuccess(JsonPacket<String> jsonPacket) {
				System.out.println("loginSuccess");
			}
			
			@Override
			public void loginOnOther(JsonPacket<String> jsonPacket) {
				
			}
			
			@Override
			public void loginFail(JsonPacket<String> jsonPacket) {
				
			}
			
			@Override
			public void forbin(JsonPacket<String> jsonPacket) {
				
			}
			
			@Override
			public void disconnect() {
				System.out.println("disconnect");
			}
		});
		
		/**
		 * 开启推送服务 必须在子线程中开启
		 */
		ThreadPool.execute(new Runnable() {
			
			@Override
			public void run() {
				KeepAliveService.initAndReStart();				
			}
		});
		
	}
}
