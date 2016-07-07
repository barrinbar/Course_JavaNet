import java.util.concurrent.Semaphore;

public class Exc2 {
	static String obj1 = new String("First");
	static String obj2 = new String("Second");
	
	public Exc2() {
		MyThread thr1 = new MyThread(0, obj1, obj2);
		MyThread thr2 = new MyThread(1, obj2, obj1);
		
		thr1.start();
		thr2.start();
	}

	public static void main(String[] args) throws InterruptedException {

		Exc2 exc = new Exc2();
		
	}
}

class MyThread extends Thread {
	
	int m_id;
	String obj1;
	String obj2;
	
	// CTor
	public MyThread(int sid, String str1, String str2) {
		m_id = sid;
		obj1 = str1;
		obj2 = str2;
	}
	
	@Override
	public void run() {
		System.out.println("Thread #" + m_id + " trying to lock " + obj1 + " object");
		for(int i=0; i<3600000; i++);
		synchronized (obj1) {
			System.out.println("Thread #" + m_id + " has locked " + obj1 + " object");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		
		System.out.println("Thread #" + m_id + " trying to lock " + obj2 + " object");
		for(int i=0; i<3600000; i++);
		synchronized (obj2) {
			System.out.println("Thread #" + m_id + " has locked " + obj2 + " object");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
}
