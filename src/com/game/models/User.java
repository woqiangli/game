package com.game.models;
import com.game.*;
public class User {
	private UserInfo userInfo=null;
	private WsMessage wsMessage=null;
	private Room room=null;
	public UserInfo getUserInfo() {
		return userInfo;
	}
	public void setUserInfo(UserInfo userInfo) {
		this.userInfo = userInfo;
	}
	public WsMessage getWsMessage() {
		return wsMessage;
	}
	public void setWsMessage(WsMessage wsMessage) {
		this.wsMessage = wsMessage;
	}
	public Room getRoom() {
		return room;
	}
	public void setRoom(Room room) {
		this.room = room;
	}
	
}
