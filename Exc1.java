import java.util.concurrent.Semaphore;

// Barr Inbar
// Omer Elgrably

public class Exc1 {
	public static void main(String[] args) {
		UsingSemaphoresForSignalin[] arr =
				new UsingSemaphoresForSignalin[5];
		
		for (int i=0; i < 5; i++)
			arr[i] = new UsingSemaphoresForSignalin(i);
	
		for (int i=0; i < 5; i++)
			arr[i].start();
	}
}

class UsingSemaphoresForSignalin extends Thread {

	// Data Members
	private int m_id;
	private static Semaphore semaphore = new Semaphore (2);
	
	// CTor
	public UsingSemaphoresForSignalin(int sid) {
		this.m_id = sid;
	}
	
	@Override
	public void run() {
		while(true)
		{
			try {
				semaphore.acquire();
				System.out.println(m_id);
				Thread.sleep(3000);
				semaphore.release();
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
