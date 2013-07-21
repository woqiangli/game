/*websocket逻辑处理的类*/

package com.game;

import java.io.IOException; 
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.ArrayList;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.WsOutbound;

import com.game.*;
import com.game.models.*;
import com.game.servlet.WsServlet;;

public class WsMessage extends MessageInbound {
	/*获取MyList类中的usersList,roomsList的引用*/
	private static ArrayList<User> usersList=MyList.usersList;
	private static ArrayList<Room> roomsList=MyList.roomsList;
	User user=new User();
	/*构造方法，设置用户的user对象，每个WsMessage实例的user对象不同*/
	public WsMessage(User user){
		user.setWsMessage(this);
		this.user=user;
	}
	
	protected void onOpen(WsOutbound outbound){
	}
	/*用户关闭websocket时调用*/
	protected void onClose( int status){
		/*usersList.remove(this.user);*/
		/*如果用户在大厅的操作*/
		if(user.getRoom()==null){
			usersList.remove(this.user);
			String message=new String("02"+user.getUserInfo().getuserId());
			broadcast(usersList,message);
		}
		/*如果用户在房间的操作*/
		else{
			ArrayList<User> rUsersList=leaveRoom();
			String message=new String("07"+user.getUserInfo().getuserId());
			broadcast(rUsersList,message);
		}
	}
	
	protected void onBinaryMessage(ByteBuffer arg0) throws IOException {
		// TODO Auto-generated method stub
		
	}

	/*接收到由前端发送的信息时触发*/
	protected void onTextMessage(CharBuffer messageBuffer) throws IOException {
		String messageString=messageBuffer.toString();
		String sign=messageString.substring(0, 2);
		switch (sign){
		case "01": enterLobby();broadcast(usersList,"01"+user.getUserInfo().getuserId());break;
		case "03": String message=new String(sign+user.getUserInfo().getuserId()+":"+messageString.substring(2));broadcast(usersList,message);break;
		case "04": addRoom(messageString.substring(2));broadcast(usersList,messageString);break;
		case "05": removeRoom(messageString.substring(2));broadcast(usersList,messageString);break;
		case "06": enterRoom(messageString.substring(2));broadcast(user.getRoom().getrUsersList(),"06"+user.getUserInfo().getuserId());break;
		case "08": broadcast(user.getRoom().getrUsersList(),sign+user.getUserInfo().getuserId()+":"+messageString.substring(2));break;
		case "09": case09(messageString.substring(2));break;
		}
	}
	//09私聊
	private void case09(String message){
		ArrayList<User> wsMessageList=new ArrayList<User>();
		int  index =message.indexOf("-"); 
		if(index!=-1){
			String userName=message.substring(0, index);
			String mes="09"+user.getUserInfo().getuserId()+"-"+userName+":"+message.substring(index+1);
			for(User u:usersList){
				if(u.getUserInfo().getuserId().equals(userName)){
					wsMessageList.add(u);
				}
			}
			if(!wsMessageList.isEmpty()){
				wsMessageList.add(user);
				broadcast(wsMessageList,mes);
			}
		}
	}
	/*向前端广播信息*/
	private void broadcast(ArrayList<User> wsMessageList,String message){
		System.out.println("broadcast:"+message);
		for(int i=0;i<wsMessageList.size();i++){
			User user=wsMessageList.get(i);
			WsMessage wsMessage=user.getWsMessage();
			CharBuffer buffer=CharBuffer.wrap(message);
			try {
				wsMessage.getWsOutbound().writeTextMessage(buffer);
			} catch (IOException e) {
				// TODO Auto-generated catch block
				System.out.println(user.getUserInfo().getuserId());
				e.printStackTrace();
			}
		}
	}
	/*用户进入大厅时调用*/
	private void enterLobby(){
		usersList.add(this.user);
	}
	/*用户创建房间时调用*/
	private void addRoom(String roomId){
		Room room=new Room();
		room.setRoomId(roomId);
		roomsList.add(room);
	}
	/*房间删除时调用*/
	private void removeRoom(String roomId){
		for(int i=0;i<roomsList.size();i++){
			Room room=roomsList.get(i);
			if(room.getRoomId().equals(roomId)){
				roomsList.remove(room);
			}
		}
	}
	/*进入房间时调用，会在room中的rUsersList中加入this.user对象，和设置this.user中的room对象*/
	private void enterRoom(String roomId){
		Room room=new Room();
		for(int i=0;i<roomsList.size();i++){
			Room temp=roomsList.get(i);
			if(temp.getRoomId().equals(roomId)){
				room=temp;
			}
		}
		usersList.remove(this.user);
		room.getrUsersList().add(user);
		user.setRoom(room);
	}
	/*用户离开房间时调用*/
	private ArrayList<User> leaveRoom(){
		Room room=user.getRoom();
		room.getrUsersList().remove(this.user);
		user.setRoom(null);
		if(room.getrUsersList().size()==0){
			roomsList.remove(room);
			broadcast(usersList,"05"+room.getRoomId());
		}
		return room.getrUsersList();
	}
}
