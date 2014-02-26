package com.evento.utils;

import java.math.BigDecimal;
import java.text.NumberFormat;
import java.util.Locale;

import org.apache.commons.codec.binary.Base64;

public class NumberFormatter {
	
	public static final NumberFormat CURRENCY_FORMAT = NumberFormat.getCurrencyInstance(new Locale("pt","BR"));
	public static final NumberFormat NF = NumberFormat.getInstance(new Locale("pt","BR"));
	
	public static final Base64  base64 = new Base64();
	
	public static String format(double number) {
		return NF.format(number);
	}
	
	public static String currency(double number) {
		return CURRENCY_FORMAT.format(number);
	}
	
	public static double round(double number) {
		BigDecimal bd = new BigDecimal(number);
		bd = bd.setScale(2, BigDecimal.ROUND_HALF_EVEN);
		
		return bd.doubleValue();
	}
	
	public static void main(String[] args) {
		try {
			System.out.println(new String(base64.decode("eyJhbGdvcml0aG0iOiJITUFDLVNIQTI1NiIsImlzc3VlZF9hdCI6MTMwMjI5MTk3NCwicGFnZSI6eyJpZCI6IjEyOTIzNDgwNzExODUxOCIsImxpa2VkIjpmYWxzZSwiYWRtaW4iOmZhbHNlfSwidXNlciI6eyJjb3VudHJ5IjoiYnIiLCJsb2NhbGUiOiJwdF9CUiIsImFnZSI6eyJtaW4iOjAsIm1heCI6MTJ9fX0".getBytes())));
			System.out.println(new String(base64.decode("eyJhbGdvcml0aG0iOiJITUFDLVNIQTI1NiIsImlzc3VlZF9hdCI6MTMwMjI5MTI4NSwicGFnZSI6eyJpZCI6IjExOTYwMjA3ODA4NjA2NiIsImxpa2VkIjp0cnVlLCJhZG1pbiI6ZmFsc2V9LCJ1c2VyIjp7ImNvdW50cnkiOiJiciIsImxvY2FsZSI6InB0X0JSIiwiYWdlIjp7Im1pbiI6MjF9fX0".getBytes())));
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
	}
}
