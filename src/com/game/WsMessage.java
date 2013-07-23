/*websocket逻辑处理的类*/

package com.game;

import java.io.IOException; 
import java.nio.ByteBuffer;
import java.nio.CharBuffer;
import java.util.ArrayList;

import org.apache.catalina.websocket.MessageInbound;
import org.apache.catalina.websocket.WsOutbound;

import com.game.models.*;;

public class WsMessage extends MessageInbound {
	/*获取MyList类中的usersList,roomsList的引用*/
	private static ArrayList<User> usersList=MyList.usersList;
	private static ArrayList<Room> roomsList=MyList.roomsList;
	User user=new User();
	private boolean opened=false; //记录WsMessgae的状态
	private int state=0;//刷新状态，0为没有离开线程，1为开始离开线程，2为进房打断，3为进入大厅打断
	Thread thread=null;//记录离开线程
	/*构造方法，设置用户的user对象，每个WsMessage实例的user对象不同*/
	public WsMessage(User user){
		user.setWsMessage(this);
		this.user=user;
	}
	
	protected void onOpen(WsOutbound outbound){
		this.opened=true;
	}
	/*用户关闭websocket时调用*/
	protected void onClose( int status){
		opened=false;
		/*如果用户在大厅的操作*/
		if(user.getRoom()==null){
			leaveLobby();
		}
		/*如果用户在房间的操作*/
		else{
			leaveRoom();
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
		case "01": enterLobby();break;
		case "03": String message=new String(sign+user.getUserInfo().getuserId()+":"+messageString.substring(2));broadcast(usersList,message);break;
		case "04": addRoom(messageString);break;
		case "05": removeRoom(messageString.substring(2));broadcast(usersList,messageString);break;
		case "06": enterRoom(messageString.substring(2));break;
		case "08": broadcast(user.getRoom().getrUsersList(),sign+user.getUserInfo().getuserId()+":"+messageString.substring(2));break;
		case "09": privately(messageString.substring(2));break;
		}
	}
	//09私聊
	private void privately(String message){
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
			CharBuffer buffer=CharBuffer.wrap(message);
			User user=wsMessageList.get(i);
			WsMessage wsMessage=user.getWsMessage();
			if(wsMessage.opened){
				try {
					wsMessage.getWsOutbound().writeTextMessage(buffer);
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}
	/*用户进入大厅时调用，已加入离开线程打断*/
	private void enterLobby(){
		if(thread!=null){
			state=3;
			thread.interrupt();
		}
		if(!synchro(usersList,this.user)){
			usersList.add(this.user);
			broadcast(usersList,"01"+user.getUserInfo().getuserId());
		}
			
	}
	/*用户创建房间时调用*/
	private void addRoom(String messageString){
		if(synchro(usersList,this.user)){
			String roomId=messageString.substring(2);
			Room room=new Room();
			room.setRoomId(roomId);
			room.setHostUser(this.user);
			room.getrUsersList().add(this.user);
			roomsList.add(room);
			broadcast(usersList,messageString);
			this.user.setRoom(room);
			usersList.remove(this.user);
			broadcast(usersList,"02"+this.user.getUserInfo().getuserId());
		}
		
	}
	/*用户离开大厅时调用，已加入离开线程开始*/
	private void leaveLobby(){
		if(state!=0)
			return;
		thread=Thread.currentThread();
		state=1;
		try {
			Thread.sleep(3000);
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			/*e.printStackTrace();*/
		}
		if(state==1||state==2){
			if(synchro(usersList,this.user)){
				usersList.remove(this.user);
				String message=new String("02"+user.getUserInfo().getuserId());
				broadcast(usersList,message);
			}
		}
		state=0;
		thread=null;
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
	/*进入房间时调用，会在room中的rUsersList中加入this.user对象，和设置this.user中的room对象，已加入离开线程打断*/
	private void enterRoom(String roomId){
		/*检测是否有离开线程*/
		if(thread!=null){
			/*设置相应state*/
			state=2;
			/*打断离开线程*/
			thread.interrupt();
		}
		//为真时，不是房主
		if(user.getRoom()==null||user.getRoom().getHostUser()!=this.user){
			
			Room room=null;
			for(int i=0;i<roomsList.size();i++){
				Room temp=roomsList.get(i);
				if(temp.getRoomId().equals(roomId)){
					room=temp;
				}
			}
			if(room!=null&&!synchro(room.getrUsersList(),this.user)){
				usersList.remove(this.user);
				room.getrUsersList().add(user);
				user.setRoom(room);
				broadcast(user.getRoom().getrUsersList(),"06"+user.getUserInfo().getuserId());
			}
		}
		
	}
	/*用户离开房间时调用，已加入离开线程开始*/
	private void leaveRoom(){
		/*检测是否已有离开线程*/
		if(state!=0)
			return;
		/*开始离开线程*/
		thread=Thread.currentThread();
		state=1;
		try {
			/*时间3和秒*/
			Thread.sleep(3000);
		} 
		/*每次唤醒都会抛出InterrutpedException异常*/
		catch (InterruptedException e) {
			// TODO Auto-generated catch block
			/*e.printStackTrace();*/
		}
		/*检测是否有打断*/
		if(state==1||state==3){
			Room room=user.getRoom();
			if(room!=null&&synchro(room.getrUsersList(),this.user)){
				ArrayList<User> rUsersList=room.getrUsersList();
				rUsersList.remove(this.user);
				user.setRoom(null);
				/*没人时，删除房间*/
				if(room.getrUsersList().size()==0){
					roomsList.remove(room);
					broadcast(usersList,"05"+room.getRoomId());
				}
				/*设置新房主*/
				else if(room.getHostUser()==this.user){
					User user=rUsersList.get(0);
					room.setHostUser(user);
				}
				String message=new String("07"+user.getUserInfo().getuserId());
				broadcast(rUsersList,message);
			}
		}
		/*重置状态*/
		state=0;
		thread=null;
	}
	/*检测数据是否同步*/
	private <E> boolean synchro(ArrayList<E> list,E e){
		return list.contains(e);
	}
}
