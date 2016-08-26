package chat;

public class ClientKey {

	private int port = -1;
	private String hostName = "";

	public ClientKey(int port, String hostName) {
		this.setPort(port);
		this.setHostName(hostName);
	}

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getHostName() {
		return hostName;
	}

	public void setHostName(String hostName) {
		this.hostName = hostName;
	}

	@Override
    public boolean equals(Object obj) {
        ClientKey other = (ClientKey)obj;
        return ((other.getHostName() == hostName) &&
                (other.getPort() == port));
	}
}
