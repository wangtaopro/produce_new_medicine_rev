package edu.trade.util;

import java.util.concurrent.Callable;
import java.util.concurrent.FutureTask;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

import org.apache.log4j.Logger;

/***********************************************************************
 * Module:  ThreadUtil.java
 * Author:  wangtao
 * Purpose: 通过JDK线程池处理线程任务
 ***********************************************************************/

public class ThreadUtil {
	
	private static final int MIN_MULTIPLY_FACTOR = 1;
	private static final int MAX_MULTIPLY_FACTOR = 2;
	private static final long KEEP_ALIVE_TIME = 60;
	private static final int PER_ALIVE_TIME = 60;
	private static final int AWAIT_QUEUE = 200;
	private static final int N_CPUS  = Runtime.getRuntime().availableProcessors();
	private static final Logger logger = Logger.getLogger(ThreadUtil.class);
	
	/** 设置线程池的数量 **/
	public static final ThreadPoolExecutor exec = new ThreadPoolExecutor(N_CPUS + MIN_MULTIPLY_FACTOR,
								N_CPUS * MAX_MULTIPLY_FACTOR,KEEP_ALIVE_TIME,TimeUnit.SECONDS,
								new LinkedBlockingQueue<Runnable>(AWAIT_QUEUE));
	
	/**
	 *  处理请求线程--异步
	 */
	public static void executeAsync(Runnable r, int inteval){
		
		logger.info("+++启用线程:+++");
		
		try {
			exec.execute(r);
			TimeUnit.SECONDS.sleep(PER_ALIVE_TIME * inteval);
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			logger.info("+++线程调用结束!+++");
		}
	}
	
	/**
	 *  处理请求线程--同步
	 */
	public static String executeSync(Callable<String> clientPeer){
		
		logger.info("+++启用线程:+++");
		
		try {
			FutureTask<String> future =  (FutureTask<String>) exec.submit(clientPeer);
			return future.get();
			
		} catch (Exception e) {
			e.printStackTrace();
		} finally{
			logger.info("+++线程调用结束!+++");
		}
		return null;
	}
}
