package in.threadpoolwithjobaffinity.command;

import javax.naming.OperationNotSupportedException;

/**
 * This interface represents a job that is submitted to the any on of the queues
 * internally by our thread pool. Jobs in one queue will be run by the same
 * thread in the thread pool.
 * 
 * The jobs are of 2 types
 * 
 * a. {@link RunnableJob} - These jobs will return a runnable when getRunnable
 * is called. This is the task that will be run by the thread in the thread
 * pool.
 * 
 * b. {@link ShutDownJob} - When a thread pulling jobs from the queue encounters
 * a shutdown job it assumes that this job is there because the shutdown command
 * has been called on the thread pool and then stops itself
 * 
 */
public interface Job {

	/**
	 * Gets the Runnable assosiated with this job
	 * 
	 * @return the {@link Runnable} to be executed by the thread
	 * @throws OperationNotSupportedException
	 *             if its a {@link ShutDownJob}
	 */
	public Runnable getRunnable() throws OperationNotSupportedException;

	/**
	 * Returns true if the job is a {@link ShutDownJob}
	 * 
	 * @return boolean value which tells if its a {@link ShutDownJob}
	 */
	public boolean isShutdownJob();

}
