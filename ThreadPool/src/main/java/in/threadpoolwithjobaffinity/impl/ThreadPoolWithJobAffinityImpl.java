package in.threadpoolwithjobaffinity.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.atomic.AtomicBoolean;

import in.threadpoolwithjobaffinity.ThreadPoolWithJobAffinity;
import in.threadpoolwithjobaffinity.command.Job;
import in.threadpoolwithjobaffinity.command.RunnableJob;
import in.threadpoolwithjobaffinity.command.ShutDownJob;
import in.threadpoolwithjobaffinity.threadwithid.ThreadWithName;

/**
 * An implementation of
 * 
 * @author Vivek
 * 
 */
public class ThreadPoolWithJobAffinityImpl implements ThreadPoolWithJobAffinity {
	private int threadPoolSize;

	List<LinkedBlockingQueue<Job>> jobQueues;

	private AtomicBoolean takeMoreJobs = new AtomicBoolean(true);

	public ThreadPoolWithJobAffinityImpl(int threadPoolSize) {
		this.threadPoolSize = threadPoolSize;

		jobQueues = new ArrayList<LinkedBlockingQueue<Job>>(threadPoolSize);
		for (int i = 0; i < threadPoolSize; i++) {
			jobQueues.add(i, new LinkedBlockingQueue<Job>());
		}
		initializeThreadPool();
	}

	@Override
	public int poolSize() {
		return threadPoolSize;
	}

	/**
	 * Any jobs that are submitted after shutdown is called will be ignored. But
	 * All the jobs that are submitted before shutdown will be completed even if
	 * shutdown is called.
	 */
	@Override
	public void submit(String jobId, Runnable job) {
		if (takeMoreJobs.get()) {
			int queuePostition = Math.abs(jobId.hashCode()) % threadPoolSize;
			System.out.println(jobId + " added to queue " + queuePostition);
			jobQueues.get(queuePostition).add(new RunnableJob(job));

		} else {
			System.out.println("Already Shutdown cant take more jobs");
		}

	}

	@Override
	public void shutdown() {
		if (takeMoreJobs.compareAndSet(true, false)) {
			addShutDownJobToAllQueues();
			System.out.println("Exector service shutdown called");

		} else {
			System.out.println("Shutdown called already");
		}
	}

	private void addShutDownJobToAllQueues() {
		for (LinkedBlockingQueue<Job> jobQueue : jobQueues) {
			jobQueue.add(new ShutDownJob());
		}

	}

	private void initializeThreadPool() {
		for (int i = 0; i < threadPoolSize; i++) {
			Thread thread = new ThreadWithName(i, jobQueues.get(i));
			thread.start();

		}
	}

}
