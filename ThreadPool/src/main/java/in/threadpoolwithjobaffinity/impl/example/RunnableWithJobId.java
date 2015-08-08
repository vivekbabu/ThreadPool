package in.threadpoolwithjobaffinity.impl.example;

import java.util.Random;

public class RunnableWithJobId implements Runnable {

	private long jobId;
	private Random random;

	public RunnableWithJobId(long id) {
		this.jobId = id;
		this.random = new Random();
	}

	@Override
	public void run() {
		System.out.println("Started job " + getJobId());
		try {
			Thread.sleep(random.nextInt(10) * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		System.out.println("Ended job " + getJobId());
	}

	public long getJobId() {
		return jobId;
	}
}
