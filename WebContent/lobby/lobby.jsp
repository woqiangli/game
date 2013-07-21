<%@ page language="java" contentType="text/html; charset=UTF-8" import="com.game.MyList,java.util.ArrayList,com.game.models.*"
    pageEncoding="UTF-8"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<!--
	01:用户进入游戏大厅 ;  01+userId
	02:用户离开大厅;  02+userId
	03:大厅广播;  发:03+message 广播:03+userId+:+message
	04:大厅创建房间;  04+roomId
	05:大厅删除房间;  05+roomId
	06:用户进入房间;  06+userId
	07:用户离开房间;  07+userId
	08:房间广播;    发:08+message 广播:08+userId+:+message
-->

<%
	request.setCharacterEncoding("UTF-8");
	response.setCharacterEncoding("UTF-8");
	User user=(User)session.getAttribute("user");
	
%>
<!--获取MyList类中大厅用户链表usersList，房间列表roomList  -->
<%!
	ArrayList<User> usersList=MyList.usersList;
	ArrayList<Room> roomsList=MyList.roomsList;
%>
<html>
	<head>
		<title>游戏大厅</title>
		<link rel="stylesheet" type="text/css" href="../lobby/lobby.css">
		<script>
			var socket={};
			socket.connect=function(){
				addText("connect......");
				if('WebSocket' in window){
					socket=new WebSocket("ws://"+ window.location.host + "/game/wsServlet?mode=1");
				}
				else if('MozWebSocket' in window){
					socket=new MozWebSocket("ws://"+ window.location.host + "/game/wsServlet?mode=1");
				}
				socket.onopen=function(){
					addText("*连接成功,socket初始化成功！");
					document.getElementById("message").onkeydown = function(event) {
			            if (event.keyCode == 13) {
			            	sendMessage("03");
			            }
			        };
			        sendMessage("01");
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
					case "01":addText(message+"进入了游戏大厅");enterLobby(message);break;
					case "02":addText(message+"离开了游戏大厅");leaveLobby(message);break;
					case "03":addText(message);break;
					case "04":createRoom(message);break;
					case "05":deleteRoom(message);break;
					}
					
				};
			};
			/*在大厅聊天面板中添加信息   */
			function addText(message){
				var messagePane=document.getElementById("messagePane");
				var p=document.createElement("p");
				p.innerHTML=message;
				messagePane.appendChild(p);
				messagePane.scrollTop=messagePane.scrollHeight;
			}
			/*向服务器发送数据  */
			function sendMessage(type){
				switch (type){
				case "01":socket.send("01<%=user.getUserInfo().getuserId() %>");break;
				case "03":
					var message=document.getElementById("message").value;
					if(message!=""){
						socket.send("03"+message);
						document.getElementById("message").value="";
					}
					break;
				case "04":
					var roomId="room<%=user.getUserInfo().getuserId() %>";
					if(roomId!=""){
						socket.send("04"+roomId);
						window.location.href="enterRoomServlet?roomId="+roomId;
						/* document.getElementById("roomId").value=""; */
					}
					break;
				case "05":
					var dRoomId=document.getElementById("dRoomId").value;
					alert(dRoomId);
					if(dRoomId!=""){
						socket.send("05"+dRoomId);
						document.getElementById("dRoomId").value="";
					}
					break;
				}
			}
			/*用户进入大厅，会广播进入信息  */
			function enterLobby(userId){
				var usersList=document.getElementById("usersList");
				var user=document.createElement("li");
				var a=document.createElement("a");
				a.id="user"+userId;
				a.className="user";
				a.innerHTML=userId;
				user.appendChild(a);
				usersList.appendChild(user);
			}
			/*用户离开大厅  */
			function leaveLobby(userId){
				var user=document.getElementById("user"+userId);
				user.parentNode.parentNode.removeChild(user.parentNode);
			}
			/*在大厅创建房间  */
			function createRoom(roomId){
				var a=document.createElement("a");
				a.id=roomId;
				a.className="room";
				a.href="enterRoomServlet?roomId="+roomId;
				var span=document.createElement("span");
				span.className="roomId";
				span.innerHTML=roomId;
				a.appendChild(span);
				document.getElementById("roomPane").appendChild(a);
			}
			/*在大厅删除房间  */
			function deleteRoom(roomId){
				var room=document.getElementById(roomId);
				room.parentNode.removeChild(room);
			}
			/*进入房间时调用  */
			function go(href){
				window.setTimeout(function(){alert("你已经离开了游戏大厅！");}, 30);
				window.location.href=href;
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
			<div id="roomPane">
			<!--输出房间列表  -->
			<%
				for(int i=0;i<roomsList.size();i++){
					Room room=roomsList.get(i);
					%>
					<a id="<%=room.getRoomId() %>" class="room" href="enterRoomServlet?roomId=<%=room.getRoomId() %>" ><span class="roomId"><%=room.getRoomId() %></span></a>
					<%
				}
			%>
			</div>
			<div id="userPane">
				<div>用户：<%=user.getUserInfo().getuserId() %></div>
				<ul id="usersList">
				<!--输出用户列表  -->
				<% 
					for(int i=0;i<usersList.size();i++){
						String userId=((User)usersList.get(i)).getUserInfo().getuserId();
						out.println("<li><a id='user"+userId+"' class='user'>"+userId+"</a></li>");
					}
				%>
				</ul>
				<div id="messagePane"></div>
				聊天：<input id="message" type="text" >
				<button id="sendMessage" onclick="sendMessage('03');">发送</button>
				<br>
				<!-- 请输入不同的房间名：<input id="roomId" type="text"> -->
				<button id="createRoom" onclick="sendMessage('04')">创建房间</button>
				<br>
				请输入要删除的房间名：<input id="dRoomId" type="text">
				<button id="deleteRoom" onclick="sendMessage('05')">删除房间</button>
				
			</div>
		</div>
	</body>
</html>