package com.game.servlet.filter;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;

public class UserInfoFilter extends HttpServlet implements Filter {
	private FilterConfig filterConfig;
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
		this.filterConfig = filterConfig;
		
	}
	@Override
	public void doFilter(ServletRequest request, ServletResponse response,
			FilterChain filterChain) throws IOException, ServletException {
		HttpSession session=((HttpServletRequest)request).getSession();
		response.setCharacterEncoding("utf-8");
		if(session.getAttribute("user")==null){
			PrintWriter out=response.getWriter();
			out.print("<Script language=javascript>window.location.href='/game/index.jsp';</script>");
		}else {
			filterChain.doFilter(request,response);
		}
	}
	public void destroy(){
		
	}
}
