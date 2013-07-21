/*找出用户进入的是那个房间，并在request中绑定相应的room对象*/

package com.game.servlet;


import java.io.IOException;
import java.io.PrintWriter;
import java.sql.*;
import java.util.ArrayList;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.game.MyList;
import com.game.models.Room;

public class EnterRoomServlet extends HttpServlet {
	public void doGet(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
		request.setCharacterEncoding("UTF-8");
		response.setCharacterEncoding("UTF-8");
		String roomId=request.getParameter("roomId");
		System.out.println("roomId!!"+roomId);
		ArrayList<Room> roomsList=MyList.roomsList;
		Room room=null;
		for(int i=0;i<roomsList.size();i++){
			Room temp=roomsList.get(i);
			if(temp.getRoomId().equals(roomId)){
				room=temp;
			}
		}
		request.setAttribute("room", room);
		
		RequestDispatcher requestDispatcher = request.getRequestDispatcher("/room/room.jsp");
        requestDispatcher.forward(request, response);
	}
	public void doPost(HttpServletRequest request,HttpServletResponse response) throws ServletException,IOException{
		doGet(request,response);
	}
}
