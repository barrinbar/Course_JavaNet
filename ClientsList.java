package chat;

import java.io.Serializable;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Collectors;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ClientsList implements Serializable {

	private static final long serialVersionUID = 3788105916990447765L;
	private Map<ClientKey, String> clients = null;
	
	public ClientsList(Map<ClientKey, String> clients) {
		this.setClients(clients);
	}

	public ClientsList() {
		setClients(null);
	}
	
	public boolean isEmpty() {
		if (clients == null)
			return true;
		return (clients.isEmpty());
	}
	
	public boolean nameExists(String name) {
		if (clients == null || clients.isEmpty())
			return false;
		return (clients.containsValue(name));
	}
	
	public boolean clientExists(ClientKey key) {
		if (clients == null || clients.isEmpty())
			return false;
		return (clients.containsKey(key));
	}
	
	public void addClient(ClientKey key, String name) {
		if (clients == null) {
			clients = new HashMap<ClientKey, String>();
		}
		clients.put(key, name);
	}
	
	public void removeClient(ClientKey key) throws Throwable {
		if (clients.containsKey(key))
			clients.remove(key);
		else
			throw new Throwable("Client not found"); 

	}
	
	public String findByKey(ClientKey key) throws Throwable {
		if (clients == null || clients.isEmpty())
			throw new Throwable("Client not found"); 
		
		return clients.get(key);
	}
	
	public ClientKey findByVal(String val) {
		for (Map.Entry<ClientKey, String> entry : clients.entrySet()) {
			if (entry.getValue() == val)
				return entry.getKey();
		}
		return null;
	}
	
	public ObservableList<String> getNamesList() {
		return FXCollections.<String>observableList(new LinkedList<String>(clients.values()));		
	}
	
	/*public <K, V extends Comparable<? super V>> Map<ClientKey, String> sortByValue() {
        
        HashMap<K, V> hm =
                new HashMap<>();

        for (Entry<ClientKey, String> entry : clients.entrySet()) {
            hm.put((K) entry.getKey(), (V)entry.getValue());
        }

        LinkedList<Map.Entry<K, V>> list = new LinkedList<>( hm.entrySet() );

        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        Map<ClientKey, String> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put((ClientKey)entry.getKey(), (String)entry.getValue());
        }
        clients = result;
        return result;
    }*/

	public void sortByValue() {
		clients = sortByValue(clients);
	}
	
	private static <K, V extends Comparable<? super V>> Map<K, V> sortByValue(Map<K, V> map) {
	    return map.entrySet()
	              .stream()
	              .sorted(Map.Entry.comparingByValue(/*Collections.reverseOrder()*/))
	              .collect(Collectors.toMap(
	                Map.Entry::getKey, 
	                Map.Entry::getValue, 
	                (e1, e2) -> e1, 
	                LinkedHashMap::new
	              ));
	}

	public Map<ClientKey, String> getClients() {
		return clients;
	}

	protected void setClients(Map<ClientKey, String> clients) {
		this.clients = clients;
	}
}
