package in.threadpoolwithjobaffinity.command;

import in.threadpoolwithjobaffinity.ThreadPoolWithJobAffinity;

import javax.naming.OperationNotSupportedException;

/**
 * This class is a concrete implementation of {@link Job}. This class will
 * return a {@link Runnable} to be executed by a thread in the
 * {@link ThreadPoolWithJobAffinity}
 * 
 */
public class RunnableJob implements Job {
	private Runnable runnable;

	public RunnableJob(Runnable runnable) {
		this.runnable = runnable;
	}

	@Override
	public Runnable getRunnable() throws OperationNotSupportedException {
		return runnable;
	}

	@Override
	public boolean isShutdownJob() {
		return false;
	}

}
