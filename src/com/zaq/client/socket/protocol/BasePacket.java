package com.zaq.client.socket.protocol;

import com.google.gson.annotations.Expose;
/**
 * 数据包基础类
 * @author zaq
 *
 */
public abstract class BasePacket {
	protected boolean isRequest=false;
	@Expose
	protected long msgTAG;
	@Expose
	protected String msg;
	@Expose
	protected boolean isRePost;//是否为重新超时发送的消息
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	public boolean isRequest() {
		return isRequest;
	}
	/**
	 * @return the msgTAG
	 */
	public long getMsgTAG() {
		return msgTAG;
	}
	/**
	 * @param msgTAG the msgTAG to set
	 */
	public void setMsgTAG(long msgTAG) {
		this.msgTAG = msgTAG;
	}
	/**
	 * @param isRequest the isRequest to set
	 */
	public void setRequest(boolean isRequest) {
		this.isRequest = isRequest;
	}
	/**
	 * @return the isRePost
	 */
	public boolean isRePost() {
		return isRePost;
	}
	/**
	 * @param isRePost the isRePost to set
	 */
	public void setRePost(boolean isRePost) {
		this.isRePost = isRePost;
	}
}
