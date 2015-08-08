package in.threadpoolwithjobaffinity.command;

import javax.naming.OperationNotSupportedException;

import org.testng.Assert;
import org.testng.annotations.Test;

public class ShutDownJobTest {
	
	@Test
	public void isShutDownJob() {
		ShutDownJob shutDownJob = new ShutDownJob();
		Assert.assertTrue(shutDownJob.isShutdownJob());
	}
	
	@Test(expectedExceptions = OperationNotSupportedException.class)
	public void getRunnableShouldThrowException() throws OperationNotSupportedException {
		ShutDownJob shutDownJob = new ShutDownJob();
		shutDownJob.getRunnable();
	}
}
