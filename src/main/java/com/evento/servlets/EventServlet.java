package com.evento.servlets;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import com.evento.bean.Event;
import com.evento.facebook.EventHandler;
import com.evento.facebook.FacebookConnect;

public class EventServlet extends HttpServlet {

	private static final long serialVersionUID = -5757413148863033959L;
	
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		String path = request.getRequestURI();
		
		HttpSession session = request.getSession(false);
		FacebookConnect fb = session.getAttribute("facebookConnect") != null ? (FacebookConnect)session.getAttribute("facebookConnect") : null;

		if (path.contains("/event")) {
			getEvent(request, response, fb);
		} else {
			throw new ServletException(new UnsupportedOperationException());
		}
	}

	public void getEvent(HttpServletRequest request, HttpServletResponse response, FacebookConnect fb) throws ServletException, IOException {
		
		String path = request.getRequestURI();
		String id = request.getRequestURI().substring(path.lastIndexOf("/")+1);
		
		Event event = new EventHandler(id, fb.getAccessToken()).getEvent(false);
		
		request.setAttribute("event", event);
		request.getRequestDispatcher("/event.jsp").forward(request, response);
		return;
	}
}