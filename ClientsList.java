package chat;

import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.Map;
import java.util.Map.Entry;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class ClientsList {

	private Map<ClientKey, String> clients = null;
	
	public ClientsList(Map<ClientKey, String> clients) {
		this.setClients(clients);
	}

	public ClientsList() {
		setClients(null);
	}
	
	public boolean nameExists(String name) {
		return (clients.containsValue(name));
	}
	
	public boolean clientExists(ClientKey key) {
		return (clients.containsKey(key));
	}
	
	public void addClient(ClientKey key, String name) {
		clients.put(key, name);
	}
	
	public String findByKey(ClientKey key) {
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
	
	public <K, V extends Comparable<? super V>> Map<ClientKey, String> sortByValue() {
        
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
    }
	/*public <K, V extends Comparable<? super V>> Map<K, V> sortByValue( Map<K, V> map ) {
        String name;
        
        HashMap<K, V> hm =
                new HashMap<>();

        for (Entry<K, V> entry : map.entrySet()) {
            name = (String) entry.getValue();
            hm.put(entry.getKey(), (V)name);
        }

        LinkedList<Map.Entry<K, V>> list = new LinkedList<>( hm.entrySet() );

        Collections.sort(list, new Comparator<Map.Entry<K, V>>() {
            public int compare(Map.Entry<K, V> o1, Map.Entry<K, V> o2) {
                return (o1.getValue()).compareTo(o2.getValue());
            }
        });

        Map<K, V> result = new LinkedHashMap<>();
        for (Map.Entry<K, V> entry : list) {
            result.put(entry.getKey(), entry.getValue());
        }
        return result;
    }*/
	public Map<ClientKey, String> getClients() {
		return clients;
	}

	protected void setClients(Map<ClientKey, String> clients) {
		this.clients = clients;
	}
}
