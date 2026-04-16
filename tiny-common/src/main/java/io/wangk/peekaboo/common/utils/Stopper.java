package io.wangk.peekaboo.common.utils;

import java.util.concurrent.atomic.AtomicBoolean;

/**
 *  if the process closes, a signal is placed as true, and all threads get this flag to stop working
 */
public class Stopper {

	private static final AtomicBoolean signal = new AtomicBoolean(false);
	
	public static boolean isStopped(){
		return signal.get();
	}
	
	public static boolean isRunning(){
		return !signal.get();
	}
	
	public static void stop(){
		signal.set(true);
	}
}
