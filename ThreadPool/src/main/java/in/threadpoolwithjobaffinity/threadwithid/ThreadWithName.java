package in.threadpoolwithjobaffinity.threadwithid;

import in.threadpoolwithjobaffinity.command.Job;

import java.util.concurrent.LinkedBlockingQueue;

import javax.naming.OperationNotSupportedException;

public class ThreadWithName extends Thread {

	private LinkedBlockingQueue<Job> queue;
	private boolean breakWhenQueueIsEmpty = false;

	public ThreadWithName(int threadName, LinkedBlockingQueue<Job> queue) {
		this.queue = queue;
		this.setName(String.valueOf(threadName));
	}

	@Override
	public void run() {
		while (!queue.isEmpty() || !breakWhenQueueIsEmpty) {
			try {
				Job job = queue.take();
				if (job.isShutdownJob()) {
					breakWhenQueueIsEmpty = true;
				} else {
					Runnable runnable = job.getRunnable();
					runnable.run();
				}
			} catch (OperationNotSupportedException e) {
				System.out.println("Run operation not supported");
			} catch (InterruptedException e) {
				System.out.println("Run Interrupted");
			}

		}
		System.out.println("Thread " + getName() + " of threadpool shutting down");
	}
}
