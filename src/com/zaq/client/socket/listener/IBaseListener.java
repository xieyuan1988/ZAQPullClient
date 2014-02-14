package com.zaq.client.socket.listener;

import com.zaq.client.socket.protocol.JsonPacket;

public interface IBaseListener {
	/**
	 * 登陆成功
	 */
	public void loginSuccess(JsonPacket<String> jsonPacket);
	/**
	 * 登陆失败
	 */
	public void loginFail(JsonPacket<String> jsonPacket);
	/**
	 * 异地登陆
	 */
	public void loginOnOther(JsonPacket<String> jsonPacket);
	/**
	 * 连接失败
	 */
	public void disconnect();
	/**
	 * 推送异常
	 */
	public void pullerror(JsonPacket<String> jsonPacket);
	/**
	 * 访问禁止
	 */
	public void forbin(JsonPacket<String> jsonPacket);
	/**
	 * 访问不存在
	 */
	public void unfound(JsonPacket<String> jsonPacket);
}
