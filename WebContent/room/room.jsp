<!--显示房间的页面
	request中的参数
		room:对象
	sesion:
		user对象
-->
<%@ page language="java" contentType="text/html; charset=UTF-8" import="com.game.models.*,java.util.ArrayList"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<%
	request.setCharacterEncoding("UTF-8");
	response.setCharacterEncoding("UTF-8");
	User user=(User) session.getAttribute("user");
	Room room=(Room) request.getAttribute("room");
	if(room==null){
		response.sendRedirect("../lobby/lobby.jsp");
		return;
	}
		ArrayList<User> rUsersList=room.getrUsersList();
%>
<html>
	<head>
		<title>房间</title>
		<link rel="stylesheet" type="text/css" href="../room/room.css">
		<script type="text/javascript">
			var socket={};
			socket.connect=function(){
				addText("connecting......");
				/*创建websocket对象  */
				if('WebSocket' in window){
					socket=new WebSocket("ws://"+ window.location.host + "/game/wsServlet?mode=2");
				}
				else if('MozWebSocket' in window){
					socket=new MozWebSocket("ws://"+ window.location.host + "/game/wsServlet?mode=2");
				}
				socket.onopen=function(){
					addText("*连接成功,socket初始化成功！");
					document.getElementById("message").onkeydown = function(event) {
			            if (event.keyCode == 13) {
			            	sendMessage("08");
			            }
			        };
					sendMessage("06");
				};
				socket.onclose=function(){
					document.getElementById("message").onkeydown=null;
					document.getElementById("sendMessage").onclick=null;
					addText("*连接已断开！");
				};
				socket.onmessage=function(message){
					var sign=message.data.substring(0,2);
					var message=message.data.substring(2);
					switch (sign){
					case "06" : addText(message+"进入了房间");enterRoom(message);break;
					case "07" : addText(message+"离开了房间");leaveRoom(message);break;
					case "08" : addText(message);break;
					}
					
				};
			};
			/*
				在房间聊天室的面板中添加信息
				参数：
					message:添加的信息，字符串
			*/
			function addText(message){
				var chatPane=document.getElementById("chatPane");
				var p=document.createElement("p");
				p.innerHTML=message;
				chatPane.appendChild(p);
				chatPane.scrollTop=chatPane.scrollHeight;
			};
			/*
				向服务器发送数据
				 type:区分信息类型，字条串
			*/
			function sendMessage(type){
				switch (type){
				case "06" :socket.send("06<%=room.getRoomId() %>");break;
				case "08" :
					var message=document.getElementById("message").value;
					if(message!=""){
						socket.send("08"+message);
						document.getElementById("message").value="";
					}
					break;
				}
			};
			/*  
				用户进入房间
			*/
			function enterRoom(userId){
				var rUsersList=document.getElementById("rUsersList");
				var roomUser=document.createElement("li");
				roomUser.id="roomUser"+userId;
				roomUser.innerHTML=userId;
				rUsersList.appendChild(roomUser);
			}
			/*  
				用户离开房间
			*/
			function leaveRoom(userId){
				var roomUser=document.getElementById("roomUser"+userId);
				roomUser.parentNode.removeChild(roomUser);
			}
			window.onload=function(){
				socket.connect();
			}; 
			window.onbeforeunload=function(){
				socket.close();
			};
		</script>
	</head>
	<body>
		<div id="container">
			<div>房间：<%=room.getRoomId() %></div>
			<div>用户：<%=user.getUserInfo().getuserId() %></div>
			<div id="leftPane">
				<div id="chatPane"></div>
				<input type="text" id="message" name="message">
				<button id="sendMessage" onclick="sendMessage('08');" >发送</button>
			</div>
			<div id="rightPane">
				<ul id="rUsersList">
				<!--输出用户列表  -->
				<%
					for(int i=0;i<rUsersList.size();i++){
						User temp=rUsersList.get(i);
						out.println("<li id='roomUser"+temp.getUserInfo().getuserId()+"'>"+temp.getUserInfo().getuserId()+"</li>");
					}
				%>
				</ul>
			</div>
		</div>
	</body>
</html>