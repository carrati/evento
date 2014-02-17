/**
 * 
 */
package com.evento.utils;

import java.net.URLDecoder;
import java.net.URLEncoder;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

/**
 * Classe utilitaria para operacoes com cookies
 */
@SuppressWarnings("deprecation")
public class Cookies {

	public static final int PERMANENT_LIFETIME = 31104000;

	public static final int _15_DAYS_LIFETIME = 60 * 60 * 24 * 15;
	
	public static final int _30_DAYS_LIFETIME = 60 * 60 * 24 * 30;
	
	public static void addCookie(HttpServletResponse response, String key, String value) {
        addCookie(response, key, value, null);
    }
	
	public static void addCookie(HttpServletResponse response, String key, String value, String domain) {
        addCookie(response, key, value, PERMANENT_LIFETIME, false, domain);
    }
	
	public static void addCookie(HttpServletResponse response, String key, String value, int lifetime) {
		addCookie(response, key, value, lifetime, false, null);
	}
	
	public static void addCookie(HttpServletResponse response, String key, String value, int lifetime, boolean secure, String domain) {
		if (value == null)
			return;
		
		Cookie c = new Cookie(key, URLEncoder.encode(value));
		
		if (domain != null)
			c.setDomain(domain);
		
        c.setMaxAge(lifetime);
        c.setPath("/");
        c.setSecure(secure);
        response.addCookie(c);
	}
	
	public static void addSessionCookie(HttpServletResponse response, String key, String value) {
		addSessionCookie(response, key, value, null);
	}
	
	public static void addSessionCookie(HttpServletResponse response, String key, String value, String pattern) {
		if (value == null)
			return;
		
        Cookie c = new Cookie(key, URLEncoder.encode(value));
        c.setPath("/");
        if (pattern != null)
        	c.setDomain(pattern);
        response.addCookie(c);
    }
	
	public static void removeCookie(HttpServletResponse response, String key) {
		removeCookie(response, key, null);
	}
	
	public static void removeCookie(HttpServletResponse response, String key, String pattern) {
        Cookie c = new Cookie(key, "");
        c.setMaxAge(0);
        c.setPath("/");
        if (pattern != null)
        	c.setDomain(pattern);
        response.addCookie(c);
    }
    
    public static String getCookie(HttpServletRequest request, String key) {
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
	        for (int i = 0; i < cookies.length; i++) {
    	        if (cookies[i].getName().equals(key)) {
        	        return URLDecoder.decode(cookies[i].getValue());
                }
            }
        }
        return null;
   	}
}