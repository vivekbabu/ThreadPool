package in.threadpoolwithjobaffinity.command;

import javax.naming.OperationNotSupportedException;

import org.testng.Assert;
import org.testng.annotations.Test;

public class RunnableJobTest {

	@Test
	public void testRunnableJob() {
		RunnableJob runnableJob = new RunnableJob(new Runnable() {
			
			@Override
			public void run() {
				//Empty Job
			}
		});
		
		try {
			Assert.assertNotNull(runnableJob.getRunnable());
			Assert.assertFalse(runnableJob.isShutdownJob());
		} catch (OperationNotSupportedException e) {
			Assert.fail("Should not throw exception", e);
		}
	}
}
