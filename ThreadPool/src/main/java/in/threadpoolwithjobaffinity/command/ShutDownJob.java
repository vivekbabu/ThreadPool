package in.threadpoolwithjobaffinity.command;

import in.threadpoolwithjobaffinity.ThreadPoolWithJobAffinity;

import javax.naming.OperationNotSupportedException;

/**
 * This class is a concrete implementation of {@link Job}. This class will
 * return true for isShutdownJob() method indicating this is a signal to
 * shutdown the thread in the {@link ThreadPoolWithJobAffinity}
 */
public class ShutDownJob implements Job {

	@Override
	public Runnable getRunnable() throws OperationNotSupportedException {
		throw new OperationNotSupportedException();
	}

	@Override
	public boolean isShutdownJob() {
		return true;
	}

}
