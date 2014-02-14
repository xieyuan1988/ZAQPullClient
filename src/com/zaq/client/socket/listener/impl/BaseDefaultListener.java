package com.zaq.client.socket.listener.impl;

import com.zaq.client.socket.listener.IBaseListener;
import com.zaq.client.socket.protocol.JsonPacket;


public class BaseDefaultListener implements IBaseListener{

	@Override
	public void loginFail(JsonPacket<String> jsonPacket) {
		System.out.println("登陆失败"+jsonPacket.getMsg());
	}

	@Override
	public void loginSuccess(JsonPacket<String> jsonPacket) {
		System.out.println("登陆成功"+jsonPacket.getMsg());		
	}

	@Override
	public void disconnect() {
		System.out.println("连接失败");
	}

	@Override
	public void loginOnOther(JsonPacket<String> jsonPacket) {
		System.out.println("异地登陆"+jsonPacket.getMsg());
	}

	@Override
	public void pullerror(JsonPacket<String> jsonPacket) {
		System.out.println("推送异常"+jsonPacket.getMsg());		
	}

	@Override
	public void forbin(JsonPacket<String> jsonPacket) {
		System.out.println("访问禁止"+jsonPacket.getMsg());			
	}

	@Override
	public void unfound(JsonPacket<String> jsonPacket) {
		System.out.println("访问不存在:"+jsonPacket.getMsg());			
	}

}
