package com.zaq.client.socket;

import java.lang.reflect.Type;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zaq.client.Constants;
import com.zaq.client.socket.listener.ClientRegister;
import com.zaq.client.socket.listener.IBaseListener;
import com.zaq.client.socket.parse.Iparse;
import com.zaq.client.socket.protocol.JsonPacket;
import com.zaq.client.socket.protocol.ZAQprotocolException;
import com.zaq.client.socket.util.ThreadPool;

/**
 * android客户端socket消息处理器
 * @author zaq
 *
 */
public class SocketClientProcess {
	private static final Pattern clazzPattern=Pattern.compile("\"clazz\\$Id\":\"(\\w+)\"");
	private static Gson gson=new GsonBuilder().setDateFormat(Constants.DATE_FORMAT_FULL).create();
	private static Type typeStr=new TypeToken<JsonPacket<String>>(){}.getType();
	/**
	 * 解析JSON数据包
	 * @param getJsonVal
	 * @throws ZAQprotocolException 
	 */
	public static void parseJsonPacket(String getJsonVal) throws ZAQprotocolException {
		
		if(getJsonVal.equals(Constants.HEART_PACKET)){
			//心跳包，无视
			System.out.println("接收到一个心跳检测包");
			return;
		}
		
		String clazz="";
		Matcher matcher= clazzPattern.matcher(getJsonVal);
		if(matcher.find()){
			clazz=matcher.group(1);
		}else {
			throw new ZAQprotocolException("传送协议错误----或clazz$Id为空");
		}
		if(clazz.equals("String")){//消息推送
			
			//打印接收到推送的消息
			//做解析处理
			System.out.println(getJsonVal);
			
			JsonPacket<String> jsonPacket=gson.fromJson(getJsonVal, typeStr);
			
			PullMessage.doTAG(jsonPacket.getMsgTAG());//处理回执
			
			IBaseListener listener= (IBaseListener)ClientRegister.getListener(ClientRegister.MYLISTENER);
			
			switch (jsonPacket.getState().shortValue()) {
//			case 100:
//				//TODO 心跳检测
//				System.out.println("接收到心跳包了");
//				break;
			case 200:
				if(jsonPacket.getMsg().equals("登陆成功")){
					PullMessage.setConnect(true);
					listener.loginSuccess(jsonPacket);
				}
				break;
			case 401:
					listener.loginFail(jsonPacket);
				break;
			case 402:
					listener.loginOnOther(jsonPacket);
				break;
			case 403:
					listener.forbin(jsonPacket);
				break;
			case 404:
					listener.unfound(jsonPacket);
				break;
			case 500:
					listener.pullerror(jsonPacket);
				break;
			case 501:
				//创建聊天室失败
				break;
			case 502:
				//聊天室加人失败
				break;
			default:
				break;
			}
		}else{
			//路由解析
			Iparse parse= ZAQRouter.getRouter().getParseThread(clazz,getJsonVal);
			ThreadPool.execute(parse);
		}
		
	}
}
