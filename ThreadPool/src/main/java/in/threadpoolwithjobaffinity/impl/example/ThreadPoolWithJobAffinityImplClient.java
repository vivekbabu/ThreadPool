package in.threadpoolwithjobaffinity.impl.example;

import in.threadpoolwithjobaffinity.ThreadPoolWithJobAffinity;
import in.threadpoolwithjobaffinity.impl.ThreadPoolWithJobAffinityImpl;

public class ThreadPoolWithJobAffinityImplClient {
	public static void main(String[] args) throws InterruptedException {
		ThreadPoolWithJobAffinity threadPoolWithJobAffinity = new ThreadPoolWithJobAffinityImpl(3);
		for (long i = 1; i <= 20; i++) {
			RunnableWithJobId runnableWithJobId = new RunnableWithJobId(i);
			System.out.println("Submitted job " + i);
			try {
				threadPoolWithJobAffinity.submit("Job " + i, runnableWithJobId);
			} catch (Exception e) {
				System.out.println(e.getMessage());
			}

		}

		threadPoolWithJobAffinity.shutdown();

		for (int i = 0; i < 10; i++) {
			Thread.sleep(2000);
			try {
				threadPoolWithJobAffinity.submit("Job " + i, new Runnable() {

					@Override
					public void run() {
						System.out.println("Shouldnt run");

					}
				});

			} catch (Exception e) {
				System.out.println(e.getMessage());
			}
		}

		System.out.println("All jobs submitted");

	}
}
