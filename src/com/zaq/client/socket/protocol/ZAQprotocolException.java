package com.zaq.client.socket.protocol;
/**
 * 我的协议异常处理类
 * @author zyj
 *
 */
public class ZAQprotocolException  extends Exception {
	private static final long serialVersionUID = -7495476589370015758L;

	public ZAQprotocolException() {
        super();
    }

    public ZAQprotocolException(String msg) {
        super(msg);
    }

}