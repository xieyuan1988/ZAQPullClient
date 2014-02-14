package com.zaq.client.socket.parse.jsonparse;

import java.lang.reflect.Type;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import com.google.gson.reflect.TypeToken;
import com.zaq.client.socket.PullMessage;
import com.zaq.client.socket.parse.BaseJsonParse;
import com.zaq.client.socket.protocol.JsonPacket;
import com.zaq.client.socket.protocol.ZAQprotocolException;
import com.zaq.client.socket.vo.RoomMessage;
import com.zaq.client.socket.vo.SendMessage;
import com.zaq.client.socket.vo.ShortMessage;
/**
 * 消息分析中转器
 * @author zaq
 *
 */
public class SendMessageParse extends BaseJsonParse<JsonPacket<SendMessage>>{
	protected final Pattern typePattern=Pattern.compile("\"typeStr\\$type\":\"(\\w+)\"");//动作匹配
	protected static Type shortType=new TypeToken<JsonPacket<SendMessage<ShortMessage>>>(){}.getType();
	protected static Type roomType=new TypeToken<JsonPacket<SendMessage<RoomMessage>>>(){}.getType();
	
	public SendMessageParse(String jsonPacketStr) {
		super(jsonPacketStr);
	}
	
	@Override
	public void initParse() throws ZAQprotocolException {
		String typeStr=null;
			Matcher matcher= typePattern.matcher(getPacketVal());
		if(matcher.find()){
			typeStr=matcher.group(1);
		}else {
			throw new ZAQprotocolException("传送协议错误---推送的message消息---typeStr$type不能为空");
		}
		
		if(typeStr.equals(ShortMessage.class.getSimpleName())){
			this.type=shortType;
		}else if(typeStr.equals(RoomMessage.class.getSimpleName())){
			this.type=roomType;
		}
	}
	
	@Override
	/**
	 * 第三方应用 可重写此方法，处理业务 ，（需配置router.properties） 
	 */
	public void parse() {
		//暂不支持 packet.getObjects()
		if(null==packet.getObject()){
			return ;
		}
		//记录接收推送日志
		System.out.println("SendMessageParse:"+getPacketVal());
	}
}
