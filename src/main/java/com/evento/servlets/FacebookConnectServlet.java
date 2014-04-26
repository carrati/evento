package com.evento.servlets;

import java.io.IOException;
import java.util.Date;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.evento.bean.User;
import com.evento.db.DAOFactory;
import com.evento.facebook.FacebookConnect;
import com.evento.utils.Cookies;
import com.evento.utils.DateUtils;

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
	
	//migrations
	/**
	 * ALTER TABLE `vaibombar`.`event` ADD COLUMN `all_members_count` INTEGER UNSIGNED NOT NULL DEFAULT 0 AFTER `name`;
	 * ALTER TABLE `vaibombar`.`event` CHANGE COLUMN `cover` `pic_big` VARCHAR(500) CHARACTER SET latin1 COLLATE latin1_swedish_ci DEFAULT NULL,
	 ADD COLUMN `pic_cover` VARCHAR(500) NOT NULL AFTER `end_time`,
	 ADD COLUMN `venue_latitute` VARCHAR(100) AFTER `pic_cover`,
	 ADD COLUMN `venue_longitude` VARCHAR(100) AFTER `venue_latitude`,
	 ADD COLUMN `venue_city` VARCHAR(300) AFTER `venue_longitude`,
	 ADD COLUMN `venue_state` VARCHAR(300) AFTER `venue_city`,
	 ADD COLUMN `venue_country` VARCHAR(300) AFTER `venue_state`,
	 ADD COLUMN `venue_id` BIGINT UNSIGNED AFTER `venue_country`,
	 ADD COLUMN `venue_street` VARCHAR(500) AFTER `venue_id`,
	 ADD COLUMN `venue_zip` VARCHAR(45) AFTER `venue_street`;
	 
	 ALTER TABLE `vaibombar`.`event` MODIFY COLUMN `venue_latitude` DECIMAL(18,15) DEFAULT NULL,
 MODIFY COLUMN `venue_longitude` DECIMAL(18,15) DEFAULT NULL;
 
 ALTER TABLE `vaibombar`.`event` ADD COLUMN `attending_count` INT(10) UNSIGNED NOT NULL AFTER `all_members_count`,
 ADD COLUMN `not_replied_count` INT(10) UNSIGNED NOT NULL AFTER `attending_count`,
 ADD COLUMN `declined_count` INT(10) UNSIGNED NOT NULL AFTER `not_replied_count`,
 ADD COLUMN `unsure_count` INT(10) UNSIGNED NOT NULL AFTER `declined_count`;

	 */

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
				user.setUsername((String)fb.getProfile().get("username"));
				if (fb.getProfile().get("location") != null) {
					user.setLocation((String) ((Map<String, Object>)fb.getProfile().get("location")).get("name") );
					user.setLocationId(Long.parseLong((String) ((Map<String, Object>)fb.getProfile().get("location")).get("id")) );
				}
				user.setEmail((String)fb.getProfile().get("email"));
				user.setId(Long.parseLong((String)fb.getProfile().get("id")));
				user.setGender((String)fb.getProfile().get("gender"));
				user.setLink((String)fb.getProfile().get("link"));
				user.setBirthday(DateUtils.parseDate((String)fb.getProfile().get("birthday"), "MM/dd/yyyy"));
				
				user.setLastLoginIP(request.getRemoteAddr());
				
				user.setLastLoginDate(new Date());

				DAOFactory.getInstance().getUserDAO().insert(user);
				
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