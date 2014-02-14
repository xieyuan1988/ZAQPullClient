package com.zaq.client.socket.listener;

import java.util.HashMap;
import java.util.Map;

import com.zaq.client.socket.listener.impl.BaseDefaultListener;

public class ClientRegister {
	private static Map<String, IBaseListener> regMap=new HashMap<String, IBaseListener>();
	public static String MYLISTENER="LoginOrOutListener";
	static{
		regMap.put(MYLISTENER, new BaseDefaultListener());
	}
	public static void register(String key,IBaseListener listener){
		regMap.put(key, listener);
	}
	public static IBaseListener getListener(String listener){
		return regMap.get(listener);
	}
} 
