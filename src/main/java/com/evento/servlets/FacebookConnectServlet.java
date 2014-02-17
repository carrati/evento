package com.evento.servlets;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.evento.bean.User;
import com.evento.facebook.FacebookConnect;
import com.evento.utils.Cookies;

public class FacebookConnectServlet extends HttpServlet {

	private static final long serialVersionUID = -5757413148863033959L;
	
	public static final String ATT_REFERER = "referer";
	public static final String SESSION_ATT_CLIENT = "_client";

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path = request.getRequestURI();

		if (path.contains("/login")) {
			doLogin(request, response);
		} else if (path.contains("/doLogin")) {
			response.sendRedirect(FacebookConnectServlet.redirectToDialogPermission(request));
			return;
		} else {
			throw new ServletException(new UnsupportedOperationException());
		}
	}

	@SuppressWarnings("unchecked")
	public void doLogin(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		HttpSession session = request.getSession(false);
		
		String code = request.getParameter("code");
		String accessToken = null;
		String referer = null;
		FacebookConnect fb = null;
		User user = null;
		
		if (code != null) {
			accessToken = FacebookConnect.getAccessToken(code, FacebookConnect.CLIENT_ID, 
					FacebookConnect.URL_REDIRECT_URI, FacebookConnect.CLIENT_SECRET);
			
			fb = new FacebookConnect(accessToken);
			user = new User();
			try {
				user.setName((String)fb.getProfile().get("name"));
				user.setFirstName((String)fb.getProfile().get("first_name"));
				user.setLastName((String)fb.getProfile().get("last_name"));

				user.setLocation((String) ((Map<String, Object>)fb.getProfile().get("location")).get("name") );
				user.setLocationId(Long.parseLong((String) ((Map<String, Object>)fb.getProfile().get("location")).get("id")) );

				user.setId(Long.parseLong((String)fb.getProfile().get("id")));
				user.setGender((String)fb.getProfile().get("gender"));
				//			user = UserBusiness.getInstance().loginWithFacebook(fb, request);
			} catch (Exception e) {
				
			}
		}
		
		if (session != null && session.getAttribute(ATT_REFERER) != null) {
			referer = (String)session.getAttribute(ATT_REFERER);
			session.removeAttribute(ATT_REFERER);
		}
		String msg = "";
		
		if (request.getParameter("error_code") != null || request.getParameter("error") != null) {
			msg = (referer.contains("?") ? "&" : "?") + "msg=Login+não+realizado";
		} else if (user != null && user.getId() > 0) {
			loginDoneUser(user, request, response);
			session.setAttribute("facebookConnect", fb);
		} else if (referer == null) {
			referer = redirectToDialogPermission(request);
		}
		response.sendRedirect(referer + msg);
		return;
	}

	public static String redirectToDialogPermission(HttpServletRequest request) {
		String referer =  "/";
		
		if (request.getParameter("referer") != null) {
			referer = request.getParameter("referer");
		} else if (request.getHeader("referer") != null
				&& (request.getHeader("referer").contains("http://www.zura.com") || request.getHeader("referer").contains("http://localhost:8080"))) {
			referer = request.getHeader("referer");
		}
		
		request.getSession(true).setAttribute("referer", referer);
		
		if (request.getParameter("uid") != null && !request.getParameter("uid").isEmpty())
			request.getSession(false).setAttribute("uid", Integer.parseInt(request.getParameter("uid")));
		
		return FacebookConnect.getDialogPermissionsURL(FacebookConnect.CLIENT_ID, FacebookConnect.URL_REDIRECT_URI);
	}
	
	public static void loginDoneUser(User user, HttpServletRequest request, HttpServletResponse response) {
		HttpSession session = request.getSession(true);
		session.setAttribute(SESSION_ATT_CLIENT, user);
		Cookies.addSessionCookie(response, "_user", user.getFirstName());
	}
}