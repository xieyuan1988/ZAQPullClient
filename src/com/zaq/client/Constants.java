package com.zaq.client;


public class Constants {

	public static final int LIMIT=20;//每页显示记录数
	
	public static final String SERVER_ENCODE="GBK";//服务的编码方式
	
	public static final long SYSTEM_USER=-1l;
	public static final String SYSTEM_USER_FULLNAME="系统";
	public static final Short FLAG_DELETED = 1;
	public static final Short FLAG_UNDELETED = 0;
	public static final String DATE_FORMAT_FULL = "yyyy-MM-dd HH:mm:ss";
	public static final String DATE_FORMAT_YMD="yyyy-MM-dd"; 
	
	
	//返回状态信息
//	public final static Short STATE_HEART=100;
	public final static Short STATE_SUCCESS=200;
	public final static Short STATE_LOGINERROR=401;
	public final static Short STATE_LOGINOTHER=402;//异地登陆
	public final static Short STATE_FORBIT=403;
	public final static Short STATE_UNFOUND=404;
	public final static Short STATE_ERROR=500;
	public final static Short STATE_ERROR_ROOM_CREATE=501;
	public final static Short STATE_ERROR_ROOM_ADDUSER=502;
	
	//心跳检测数据
	public static final String HEART_PACKET="Z";
	//消息10秒延时
	public static final int TIMEOUT_DELAY=1000*10;
}
