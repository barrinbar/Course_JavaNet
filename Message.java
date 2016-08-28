package chat;

import java.util.ArrayList;

public class Message {
	private String msg;
	private ArrayList<String> destination;
	
	public Message(String msg, ArrayList<String> destination) {
		this.msg = msg;
		this.destination = new ArrayList<String>(destination);
	}
	
	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public ArrayList<String> getDestination() {
		return destination;
	}

	public void setDestination(ArrayList<String> destination) {
		this.destination = destination;
	}

	@Override
	public String toString() {
		return msg;
	}
}
