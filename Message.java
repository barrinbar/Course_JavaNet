package chapter30;

import java.util.ArrayList;

public class Message {
	private String msg;
	private String sender;
	private ArrayList<String> destination;
	
	public Message(String msg, String sender, ArrayList<String> destination) {
		this.msg = msg;
		this.sender = sender;
		this.destination = new ArrayList<String>(destination);
	}
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public ArrayList<String> getDestination() {
		return destination;
	}

	public void setDestination(ArrayList<String> destination) {
		this.destination = destination;
	}

	@Override
	public String toString() {
		return sender + ": " + msg;
	}
}
