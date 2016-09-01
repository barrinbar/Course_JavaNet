package chat;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;

public class SerClients implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	Collection<String> clients;
	
	public SerClients(Collection<String> clients) {
		ArrayList<String> list = new ArrayList<String>(clients);
		Collections.sort(list);
		this.clients = list;
	}

	public Collection<String> getClients() {
		return clients;
	}

	public void setClients(Collection<String> clients) {
		this.clients = clients;
	}
}
