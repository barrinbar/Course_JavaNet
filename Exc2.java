package Week_2;

import java.util.concurrent.Semaphore;

public class Exc2 {
	public static String obj1 = new String("First");
	public static String obj2 = new String("Second");
	public static Semaphore semaphore = new Semaphore(1);

	public Exc2() {
		MyThread_1 thr1 = new MyThread_1();
		MyThread_2 thr2 = new MyThread_2();
		thr1.start();
		thr2.start();
	}

	public static void main(String[] args) throws InterruptedException {
		Exc2 exc = new Exc2();
	}

	class MyThread_1 extends Thread {
		// CTor
		public MyThread_1() {
		}

		@Override
		public void run() {

			System.out.println("Thread 1" + " trying to lock " + obj1 + " object");
			synchronized (obj1) {
				System.out.println("Thread 1" + " has locked " + obj1 + " object");
				// Thread.sleep(1000);
				System.out.println("Thread 1" + " trying to lock " + obj2 + " object");
				synchronized (obj2) {
					System.out.println("Thread 1" + " has locked " + obj2 + " object");
				}
			}

		}
	}

	class MyThread_2 extends Thread {
		// CTor
		public MyThread_2() {
		}

		@Override
		public void run() {

			System.out.println("Thread 2" + " trying to lock " + obj2 + " object");
			synchronized (obj2) {
				System.out.println("Thread 2" + " has locked " + obj2 + " object");
				System.out.println("Thread 2" + " trying to lock " + obj1 + " object");
				synchronized (obj1) {
					System.out.println("Thread 2" + " has locked " + obj1 + " object");
				}
			}

		}
	}
}
