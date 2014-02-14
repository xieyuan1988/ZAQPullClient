package com.zaq.client.socket.util;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.nio.charset.Charset;

import com.zaq.client.Constants;

/**
 * 數據包轉碼
 * @author zaq
 *
 */
public class PacketUtil {
	private static Charset charset=Charset.forName(Constants.SERVER_ENCODE);
	public static String encodePacket(String valStr){
		try {
			valStr= URLEncoder.encode(valStr,Constants.SERVER_ENCODE);
		} catch (UnsupportedEncodingException e) {}
		return valStr;
	}
	
	public static String decodePacket(String valStr){
		try {
			valStr= URLDecoder.decode(valStr,Constants.SERVER_ENCODE);
		} catch (UnsupportedEncodingException e) {}
		return valStr;
	}
	
	public static ByteBuffer encodePacketToBB(String valStr){
		return charset.encode(CharBuffer.wrap(valStr.toCharArray()));
	}
	
	public static String decodePacketToCB(ByteBuffer bb){
		return charset.decode(bb).toString();
	}
	
	public static CharBuffer decodePacketToCBF(ByteBuffer bb){
		return charset.decode(bb);
	}
	
	public static void main(String[] args) throws UnsupportedEncodingException {
		ByteBuffer byteBuffer=encodePacketToBB("asd231饿23asd");
		System.out.println(new String(byteBuffer.array(),"gbk"));
	}
}
