
//接收大厅广播信息时调用

function addLobbyMessage(message){
	var messagePane = getCookie("messagePane");
	if (messagePane != null && messagePane != "") {
		messagePane=messagePane+"[]"+message;
		setCookie('messagePane', messagePane, 0);
	}else{
		setCookie('messagePane', message, 0);
	}
	addText(message);
}

//接收私聊信息时调用

function addPriPaneMessage(message){
	//存储私聊cookies
	var priPane = getCookie("priPane");
	if (priPane != null && priPane != "") {
		priPane=priPane+"[]"+message;
		setCookie('priPane', priPane, 0);
	}else{
		setCookie('priPane', message, 0);
	}
	addPriPanet(message);
}

/*在私聊聊天面板中添加信息   */

function addPriPanet(message) {
	var messagePane = document.getElementById("priPane");
	var p = document.createElement("p");
	p.innerHTML = message;
	messagePane.appendChild(p);
	messagePane.scrollTop = messagePane.scrollHeight;
}

/*公共方法,获取指定参数cookies */

function getCookie(c_name) {
	if (document.cookie.length > 0) {
		c_start = document.cookie.indexOf(c_name + "=");
		if (c_start != -1) {
			c_start = c_start + c_name.length + 1;
			c_end = document.cookie.indexOf(";", c_start);
			if (c_end == -1) {
				c_end = document.cookie.length ;
			 }
			return unescape(document.cookie.substring(c_start, c_end));
		}
	}
	return "";
}

/*公共方法,添加指定参数cookies*/

function setCookie(c_name, value, expiresHours) {
	var exdate = new Date();
	exdate.setTime(exdate.getTime + expiresHours * 3600 * 1000);
	document.cookie=c_name+ "=" +escape(value)+((expiresHours==null) ? "" : ";expires="+exdate.toGMTString());
}

/*连接时检查cookies记录*/

function checkCookie() {
	//检查私聊的cookies记录
	var priPane = getCookie("priPane");
	if (priPane != null && priPane != "") {
		var arry = priPane.split('[]');
		var len=arry.length;
		for(var i= 0;i<len;i++)
			addPriPanet(arry[i]);
	}
	//检查大厅广播的cookies记录
	var messagePane = getCookie("messagePane");
	if (messagePane != null && messagePane != "") {
		var arry = messagePane.split('[]');
		var len=arry.length;
		for(var i= 0;i<len;i++)
			addText(arry[i]);
	}
}