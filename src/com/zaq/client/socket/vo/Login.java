package com.zaq.client.socket.vo;

import java.lang.reflect.Type;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.zaq.client.socket.protocol.JsonPacket;
import com.zaq.client.socket.util.ClientUtil;

/**
 * socket登陆信息
 * @author zyj
 *
 */
public class Login {
	public static Short STATE_HIDE=0;//隐身
	public static Short STATE_ONLINE=1;//在线
	public static Short STATE_BUSY=2;//忙碌
	public static Short STATE_LEAVE=3;//离开
	private Short state;
	private String userName;
	private String password;
	
	private Long companyId;
	
	public String getUserName() {
		return userName;
	}
	public void setUserName(String userName) {
		this.userName = userName;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public Long getCompanyId() {
		return companyId;
	}
	public void setCompanyId(Long companyId) {
		this.companyId = companyId;
	}
	public Short getState() {
		return state;
	}
	public void setState(Short state) {
		this.state = state;
	}
	
	public static String getCurrentLogin(){
		Login login=new Login();  
        String UID = ClientUtil.getPropertity("client.username");// 用户名
        String PWD = ClientUtil.getPropertity("client.password");// 密码
        if(null!=ClientUtil.getPropertity("client.companyId")){
            Long companyId =Long.valueOf(ClientUtil.getPropertity("client.companyId"));// 公司ID
            login.setCompanyId(companyId);
        }
        
        login.setPassword(PWD);
        login.setUserName(UID);
    
        login.setState(STATE_ONLINE);
        
        JsonPacket<Login> packet=new JsonPacket<Login>(login);
        
        Type type=new TypeToken<JsonPacket<Login>>(){}.getType();
        
        Gson gson=new GsonBuilder().create();
		return gson.toJson(packet,type);
	}
}
