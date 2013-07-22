package com.game.models;

import java.util.ArrayList;

public class Room {
	private String roomId=null;
	private User hostUser=null;
	ArrayList<User> rUsersList=new ArrayList<User>();
	
	public String getRoomId() {
		return roomId;
	}
	public void setRoomId(String roomId) {
		this.roomId = roomId;
	}
	public User getHostUser() {
		return hostUser;
	}
	public void setHostUser(User hostUser) {
		this.hostUser = hostUser;
	}
	public ArrayList<User> getrUsersList() {
		return rUsersList;
	}
	public void setrUsersList(ArrayList<User> rUsersList) {
		this.rUsersList = rUsersList;
	}
}
