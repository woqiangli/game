/*在私聊聊天面板中添加信息   */

function addPriPanet(message) {
	var messagePane = document.getElementById("priPane");
	var p = document.createElement("p");
	p.innerHTML = message;
	messagePane.appendChild(p);
	messagePane.scrollTop = messagePane.scrollHeight;
}