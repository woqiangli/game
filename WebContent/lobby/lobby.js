/*在私聊聊天面板中添加信息   */

function addPriPanet(message) {
	var messagePane = document.getElementById("priPane");
	var p = document.createElement("p");
	p.innerHTML = message;
	messagePane.appendChild(p);
	messagePane.scrollTop = messagePane.scrollHeight;
}

/* 在大厅聊天面板中添加信息 */
function addText(message) {
	var messagePane = document.getElementById("messagePane");
	var p = document.createElement("p");
	p.innerHTML = message;
	messagePane.appendChild(p);
	messagePane.scrollTop = messagePane.scrollHeight;
}

/* 用户进入大厅，会广播进入信息 */
function enterLobby(userId) {
	var usersList = document.getElementById("usersList");
	var user = document.createElement("li");
	var a = document.createElement("a");
	a.id = "user" + userId;
	a.className = "user";
	a.innerHTML = userId;
	user.appendChild(a);
	usersList.appendChild(user);
}
/* 用户离开大厅 */
function leaveLobby(userId) {
	var user = document.getElementById("user" + userId);
	user.parentNode.parentNode.removeChild(user.parentNode);
}
/* 在大厅创建房间 */
function createRoom(roomId) {
	var a = document.createElement("a");
	a.id = roomId;
	a.className = "room";
	a.href = "enterRoomServlet?roomId=" + roomId;
	var span = document.createElement("span");
	span.className = "roomId";
	span.innerHTML = roomId;
	a.appendChild(span);
	document.getElementById("roomPane").appendChild(a);
}
/* 在大厅删除房间 */
function deleteRoom(roomId) {
	var room = document.getElementById(roomId);
	room.parentNode.removeChild(room);
}
/* 进入房间时调用 */
function go(href) {
	window.setTimeout(function() {
		alert("你已经离开了游戏大厅！");
	}, 30);
	window.location.href = href;
}