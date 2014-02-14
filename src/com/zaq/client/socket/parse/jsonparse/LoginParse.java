package com.zaq.client.socket.parse.jsonparse;

import com.zaq.client.socket.parse.BaseJsonParse;
import com.zaq.client.socket.protocol.JsonPacket;
import com.zaq.client.socket.vo.Login;
/**
 * 给admin用户调用的 用户登陆状态改变的事件
 * @author zaq
 *
 */
public class LoginParse extends BaseJsonParse<JsonPacket<Login>>{

	public LoginParse(String jsonPacketStr) {
		super(jsonPacketStr);
	}

	@Override
	public void parse() {
		//待第三方应用重写
		System.out.println("用户"+packet.getObject().getUserName()+"状态为"+packet.getObject().getState());
	}

}
