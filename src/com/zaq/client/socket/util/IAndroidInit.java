package com.zaq.client.socket.util;

import java.util.Properties;

public interface IAndroidInit {
	/**
	 * 路由初始化
	 */
	public void zaqRouterProInit(Properties props);
	/**
	 * 客户端初始化
	 */
	public void clientUtilProInit(Properties props);
}
