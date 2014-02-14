package com.zaq.client.socket.util;

import java.io.BufferedInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ClientUtil {
	private static String config="config.properties";
	private static Properties props ;
	private static String ANDROID_CLASS="android.content.Context";
//	private static String log4j="log4j.properties";
	public static void init(IAndroidInit... androidInits) throws IOException {

//		try {
//			PropertyConfigurator.configure(ClientUtil.class.getClassLoader().getResource(log4j).getFile());
//		} catch (Exception e) {
//			System.out.println("初始化logger4j失败：log4j.properties未放置classpath下"+e.getMessage());
//		} 
		
		props= new Properties();
		//判断客户端是否为android平台
		try {
			Class.forName(ANDROID_CLASS);
			props.put("os.isAndroid", "true");
		} catch (ClassNotFoundException e) {
			props.put("os.isAndroid", "false");
		}
		
		if(osIsAndroid()||androidInits.length!=0){
			androidInits[0].clientUtilProInit(props);
		}else{
			InputStream is = new BufferedInputStream(ClientUtil.class.getClassLoader().getResourceAsStream(config));
			props.load(is);
		}
	}

	public static String getPropertity(String string) {
		return props.getProperty(string, "");
	}
	/**
	 * 是否是安卓平台
	 * @return
	 */
	public static boolean osIsAndroid(){
		if(ClientUtil.getPropertity("os.isAndroid").equals("false")){
			return false;
		}else{
			return true;
		}
	}
}	
