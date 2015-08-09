package in.threadpoolwithjobaffinity.impl;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicInteger;

import in.threadpoolwithjobaffinity.ThreadPoolWithJobAffinity;
import in.threadpoolwithjobaffinity.impl.ThreadPoolWithJobAffinityImpl;

import org.testng.Assert;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;

public class ThreadPoolWIthJobAffinityImplTest {

	/**
	 * This is the data provider for the test. 1. 3 jobs are submitted with same
	 * name. We capture which the names of threads that are processing this job.
	 * They should all be same 2. 5 jobs are submitted with same name. We
	 * capture which the names of threads that are processing this job. They
	 * should all be same We have used a thread pool size of 5. In the above 2
	 * tests we also verify that irrespective of number of jobs the same thread
	 * will process jobs with same id's 3. 3 jobs are submitted with 3 different
	 * names. The job id's are given such that should be mapped to three
	 * different threads. We check whether we get 3 thread names for threads
	 * which processed these
	 */
	@DataProvider(name = "checkIfJobsAreHandledByExpectedThreads")
	public static Object[][] checkIfJobsAreHandledByExpectedThreads() {
		return new Object[][] { { 3, 1, true }, { 5, 1, true }, { 3, 3, false } };
	}

	@Test(dataProvider = "checkIfJobsAreHandledByExpectedThreads")
	public void threadsWithSameJobIdAreProcessedBySameThreadAndDifferentJobIdProcessedByDifferentThread(
			Integer numberOfJobs, Integer processingThreadNamesSetSize, Boolean useSameJobIds) {
		CountDownLatch countDownLatch = new CountDownLatch(numberOfJobs);
		Set<String> runningThreadNames = Collections.synchronizedSet(new HashSet<String>());
		ThreadPoolWithJobAffinity threadPoolWithJobAffinity = ThreadPoolWithJobAffinityImpl.newInstance(5);
		for (int i = 0; i < numberOfJobs; i++) {
			String jobId = "TestJob";
			if (!useSameJobIds) {
				jobId = jobId + i;
			}
			threadPoolWithJobAffinity.submit(jobId, new RunnableWithJobName(jobId, countDownLatch, runningThreadNames));

		}
		threadPoolWithJobAffinity.shutdown();
		try {
			countDownLatch.await();
			Assert.assertEquals(runningThreadNames.size(), processingThreadNamesSetSize.intValue());

		} catch (InterruptedException e) {
			Assert.fail("Should not be interrupted");
		}

	}

	@DataProvider(name = "shutDownAfterSubmittingNJobs")
	public static Object[][] shutDownAfterSubmittingNJobs() {
		return new Object[][] { { 5, 5 }, { 5, 3 } };
	}

	/**
	 * Shutdown after sumbitting N number of jobs and see that other jobs are
	 * not run
	 * 
	 * @param totalNumberOfJobsSubmitted
	 *            total number of jobs submitted
	 * @param shutdownAfterSubmittingJobs
	 *            number of jobs submitted before shutdown is called
	 */
	@Test(dataProvider = "shutDownAfterSubmittingNJobs")
	public void allJobsSubmittedBeforeShutdownAreCompletedAndAfterShutdownWillNotBeRun(
			Integer totalNumberOfJobsSubmitted, Integer shutdownAfterSubmittingJobs) {
		CountDownLatch countDownLatch = new CountDownLatch(totalNumberOfJobsSubmitted);
		Set<String> runningThreadNames = Collections.synchronizedSet(new HashSet<String>());
		AtomicInteger totalJobsCompleted = new AtomicInteger(0);
		ThreadPoolWithJobAffinity threadPoolWithJobAffinity = ThreadPoolWithJobAffinityImpl.newInstance(5);
		for (int i = 1; i <= totalNumberOfJobsSubmitted; i++) {
			String jobId = "TestJob" + i;

			threadPoolWithJobAffinity.submit(jobId, new RunnableWithJobName(jobId, countDownLatch, runningThreadNames,
					totalJobsCompleted));
			if (shutdownAfterSubmittingJobs != null && i == shutdownAfterSubmittingJobs) {
				threadPoolWithJobAffinity.shutdown();
			}
		}
		threadPoolWithJobAffinity.shutdown();
		try {
			countDownLatch.await(15, TimeUnit.SECONDS);
			Assert.assertEquals(totalJobsCompleted.get(), shutdownAfterSubmittingJobs.intValue());

		} catch (InterruptedException e) {
			Assert.fail("Should not be interrupted");
		}
	}

}

class RunnableWithJobName implements Runnable {
	private CountDownLatch countDownLatch;
	private Set<String> runningThreadNames;
	private String jobId;
	private AtomicInteger totalJobsCompleted;

	public RunnableWithJobName(String jobId, CountDownLatch countDownLatch, Set<String> runningThreadNames) {
		this(jobId, countDownLatch, runningThreadNames, new AtomicInteger(0));
	}

	public RunnableWithJobName(String jobId, CountDownLatch countDownLatch, Set<String> runningThreadNames,
			AtomicInteger totalJobsCompleted) {
		this.countDownLatch = countDownLatch;
		this.runningThreadNames = runningThreadNames;
		this.jobId = jobId;
		this.totalJobsCompleted = totalJobsCompleted;
	}

	@Override
	public void run() {
		try {
			Thread.sleep(3000);
			totalJobsCompleted.incrementAndGet();
			System.out.println(jobId + " successfully completed");

		} catch (InterruptedException e) {
			Assert.fail("Should not be interrupted");
		}
		String runningThreadName = Thread.currentThread().getName();
		runningThreadNames.add(runningThreadName);
		countDownLatch.countDown();
	}

}
