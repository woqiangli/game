package com.game.servlet;


import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.ArrayList;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

import javax.servlet.http.HttpServletRequest;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.StreamInbound;
import org.apache.catalina.websocket.WebSocketServlet;
import org.apache.catalina.websocket.WsOutbound;

import javax.servlet.ServletContext;

import com.game.models.Room;
import com.game.models.User;
import com.game.WsMessage;

public class WsServlet extends WebSocketServlet {
	protected StreamInbound createWebSocketInbound(String arg0,HttpServletRequest request) {
		WsMessage wsMessage=null;
		User user=(User) request.getSession().getAttribute("user");
		if(user.getWsMessage()!=null)
			wsMessage=user.getWsMessage();
		else
			wsMessage=new WsMessage(user);
		return wsMessage;
	}
}
