package com.zaq.client.socket;

import java.io.IOException;
import java.io.InputStream;
import java.nio.channels.SocketChannel;
import java.util.Properties;

import com.zaq.client.socket.parse.Iparse;
import com.zaq.client.socket.protocol.ZAQprotocolException;
import com.zaq.client.socket.util.ClientUtil;
import com.zaq.client.socket.util.IAndroidInit;
/**
 * 分发处理器
 * @author zaq
 *
 */
public class ZAQRouter {
	private static Properties properties;
	
	private String FILE_NAME="router.properties";
	private String DEFAULT_CLASS_PATH;
	
	private static ZAQRouter router=new ZAQRouter();
	private ZAQRouter(){};
	
	public static ZAQRouter getRouter(){
		if(null==router){
			 router=new ZAQRouter();
		}
		return router;
	}
	/**
	 * 获取类全名
	 * @param clazz 类简称
	 * @return
	 */
	private String getActionName(String clazz){
		if(null==properties.get(clazz)){
			return DEFAULT_CLASS_PATH+clazz+"Parse";
		}else {
			return (String)properties.get(clazz);
		}
	}
	/**
	 * 创建处理线程
	 * @param clazz
	 * @param getJsonVal
	 * @return
	 * @throws ZAQprotocolException
	 */
	public Iparse getParseThread(String clazz,String getJsonVal) throws ZAQprotocolException{
		
		clazz=getActionName(clazz);
		
		Iparse instance=null;
		try {
			instance=(Iparse)Class.forName(clazz).getConstructor(String.class).newInstance(getJsonVal);//newInstance();
		} catch (Exception e) {
			throw new ZAQprotocolException("找不到协议class："+clazz+"----:"+e.getMessage());
		}
		return instance;
		
	}
	/**
	 * 创建处理线程
	 * @param channel
	 * @param clazz
	 * @param getJsonVal
	 * @return
	 * @throws ZAQprotocolException
	 */
	@Deprecated
	public Iparse getParseThread(SocketChannel channel,String clazz,String getJsonVal) throws ZAQprotocolException{
		
		clazz=getActionName(clazz);
		
		Iparse instance=null;
		try {
			instance=(Iparse)Class.forName(clazz).getConstructor(SocketChannel.class,String.class).newInstance(channel,getJsonVal);//newInstance();
		} catch (Exception e) {
			throw new ZAQprotocolException("找不到协议class："+clazz+e.getMessage());
		}
		
		return instance;
		
	}
	
	public void init(IAndroidInit... androidInits){
		String thizClassFullName=ZAQRouter.class.getName();
		DEFAULT_CLASS_PATH=thizClassFullName.substring(0, thizClassFullName.lastIndexOf("."))+".parse.jsonparse.";
		System.out.println(DEFAULT_CLASS_PATH);
		InputStream inStream=this.getClass().getClassLoader().getResourceAsStream(FILE_NAME);
		properties=new Properties();
		if(ClientUtil.osIsAndroid()||androidInits.length!=0){
			androidInits[0].zaqRouterProInit(properties);
		}else{
			try {
				properties.load(inStream);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
	
	public static void main(String[] args) throws ZAQprotocolException {
		String jsonTest="{\"clazz$Id\":\"Login\",\"object\":{\"password\":\"zaq123\",\"userName\":\"admin\"},\"sendTime\":1388382879934}";
		ZAQRouter router=new ZAQRouter();
		router.init();
		Iparse iparse=router.getParseThread("Login", jsonTest);
	}

}
