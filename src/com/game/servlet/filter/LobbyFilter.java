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

import com.game.models.Room;
import com.game.models.User;

public class LobbyFilter extends HttpServlet implements Filter {
	private static final long serialVersionUID = 1L;
	public void destroy() {
		// TODO Auto-generated method stub
	}
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		response.setContentType("text/html;charset=UTF-8"); 
		System.out.println(check(request));
		if(check(request)){
			PrintWriter out = response.getWriter();
			out.print("<script language=javascript>alert('你正在进行游戏中');</script>");
		}else{
			chain.doFilter(request, response);
		}
	}
	//判断用户是否已进入房间
	private boolean check(ServletRequest request){
		HttpSession session =((HttpServletRequest)request).getSession();
		if(session.getAttribute("user")!=null){
			User user=(User) session.getAttribute("user");
			if(user.getRoom()!=null){
				return true;
			}else return false;
		}else return false;
	}
	public void init(FilterConfig fConfig) throws ServletException {
		// TODO Auto-generated method stub
	}

}
