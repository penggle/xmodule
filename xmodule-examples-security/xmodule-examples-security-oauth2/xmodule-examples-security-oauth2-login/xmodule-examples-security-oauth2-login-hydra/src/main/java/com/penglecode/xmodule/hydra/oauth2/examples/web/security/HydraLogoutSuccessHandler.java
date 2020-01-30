package com.penglecode.xmodule.hydra.oauth2.examples.web.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;

public class HydraLogoutSuccessHandler implements LogoutSuccessHandler {

	private static final Logger LOGGER = LoggerFactory.getLogger(HydraLogoutSuccessHandler.class);
	
	private final String logoutSuccessUrl;
	
	public HydraLogoutSuccessHandler(String logoutSuccessUrl) {
		super();
		this.logoutSuccessUrl = logoutSuccessUrl;
	}

	@Override
	public void onLogoutSuccess(HttpServletRequest request, HttpServletResponse response, Authentication authentication)
			throws IOException, ServletException {
		LOGGER.info(">>> User Logout Successfully!");
		request.getRequestDispatcher(logoutSuccessUrl).forward(request, response);
	}

}
