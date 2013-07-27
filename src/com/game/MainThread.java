package com.game;

import com.game.models.Room;

public class MainThread extends Thread{
	private Room room=null;//记录相应房间
	private int day=0;//记录天数
	private String stage=null;//记录游戏阶段
	public MainThread(Room room){
		this.room=room;
		this.stage="s0";
	}
	public void run(){
		assignRole();
		thief();
		cupid();
		while(true){
			guard();
			werewolf();
			prophet();
			witch();
			voteSheriff();
			turnSpeak();
			voteDead();
			win();
		}
	}
	
	/*分配角色*/
	private void assignRole(){
	}
	/*盗贼阶段*/
	private void thief() {
	}
	/*丘比特阶段*/
	private void cupid(){
	}
	/*守卫阶段*/
	private void guard(){
	}
	/*狼人阶段*/
	private void werewolf(){
	}
	/*阴谋家阶段*/
	private void prophet(){
	}
	/*女巫阶段*/
	private void witch(){
	}
	/*选举警长阶段*/
	private void voteSheriff(){
	}
	/*轮流发言阶段*/
	private void turnSpeak(){
	}
	/*投票死亡阶段*/
	private void voteDead(){
	}
	/*判断游戏是否结束*/
	private void win(){
	}
}
