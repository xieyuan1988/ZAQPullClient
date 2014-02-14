package com.zaq.client.socket.parse;

import java.lang.reflect.Type;

import org.apache.commons.lang.StringUtils;

import com.zaq.client.Constants;
import com.zaq.client.socket.PullMessage;
import com.zaq.client.socket.protocol.BasePacket;
import com.zaq.client.socket.protocol.ZAQprotocolException;
import com.zaq.client.socket.util.TypeUtil;
/**
 * 处理分析器基类
 * @author zaq
 *
 * @param <T>
 */
public abstract class BaseJsonParse<T extends BasePacket> extends Iparse{
	protected Type type;//=new TypeToken<T>(){}.getType();
	protected T packet; 
//	public BaseJsonParse(){}

	public BaseJsonParse(String jsonPacketStr){
		super(jsonPacketStr);
	}	
	@Override
	public void initParse() throws ZAQprotocolException {}
	@Override
	public final void beforeParse() throws ZAQprotocolException{
//		type=((ParameterizedType)this.getClass().getGenericSuperclass()).getActualTypeArguments()[0];
		if(null==type){
			type=TypeUtil.getSuperclassTypeParameter(this.getClass());
		}

		if(StringUtils.isEmpty(packetVal)){
			throw new ZAQprotocolException("请在创建运行"+this.getClass()+"前调用 setJsonVal(String jsonVal)方法");
		}
		packet= INGson.fromJson(packetVal,type);
		
		msgTAG=packet.getMsgTAG();
		
		if(packet.getMsgTAG()!=0){
			if(packet.isRequest()){
				PullMessage.doTAG(msgTAG);
			}else{
				//发送接收成功的回执
				PullMessage.pullTag(msgTAG);
			}
		}
	};
	@Override
	public void afterParse(){};
}
