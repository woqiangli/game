package com.game.models;

import java.util.ArrayList;

import com.game.MainThread;

public class Room {
	private String roomId=null;
	private User hostUser=null;
	ArrayList<User> rUsersList=new ArrayList<User>();
	MainThread mainThread=null;
	
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
	public MainThread getMainThread() {
		return mainThread;
	}
	public void setMainThread(MainThread mainThread) {
		this.mainThread = mainThread;
	}
	
}
