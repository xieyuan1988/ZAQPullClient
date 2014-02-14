package com.zaq.client.socket.parse;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zaq.client.Constants;
import com.zaq.client.socket.protocol.ZAQprotocolException;

/**
 * 解析总接口
 * @author zyj
 *
 */
public abstract class Iparse  implements Runnable{
	protected String packetVal;
	
	protected  Gson INGson=new GsonBuilder().setDateFormat(Constants.DATE_FORMAT_FULL).create();
	protected  Gson OUTGson=new GsonBuilder().excludeFieldsWithoutExposeAnnotation().setDateFormat(Constants.DATE_FORMAT_FULL).create();

	protected Long msgTAG;
	
	public Iparse(String jsonPacketStr){
		this.packetVal=jsonPacketStr;
	}	
	
	/**
	 * 处理前初始化  可重写
	 */
	public abstract void initParse() throws ZAQprotocolException ;
	
	/**
	 * 处理前事件 可重写
	 */
	public abstract void beforeParse() throws ZAQprotocolException;
	/**
	 * 处理
	 */
	public abstract void parse();
	/**
	 * 处理后事件 可重写
	 */	
	public abstract void afterParse();
	
	/**
	 * 处理模版
	 */
	@Override
	public final void run(){
		try {
			initParse();
			beforeParse();
		} catch (Exception e) {
			e.printStackTrace();
			return;
		}
		parse();
		afterParse();
	}
	
	public String getPacketVal() {
		return packetVal;
	}
	public void setPacketVal(String packetVal) {
		this.packetVal = packetVal;
	}
	
}
