package com.zaq.test;

import java.util.Map;
import java.util.concurrent.Callable;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

import com.zaq.client.Constants;

public class Test4 {
	public static Map<Long, Callable<Void>> map=new ConcurrentHashMap<Long, Callable<Void>>();

	public static void doTAG(long msgTag){
		if(msgTag==0){
			return ;
		}
		try {
			map.remove(msgTag).call();
		} catch (Exception e) {
			e.printStackTrace();
		}
		
	}
	public static void main(String[] args) {
		boolean retVal=false;
		
		final Lock lock=new ReentrantLock();
		final Condition condition=lock.newCondition();
		
		map.put(11l, new Callable<Void>() {
			
			@Override
			public Void call() throws Exception {
				lock.lock();
				condition.signal();
				lock.unlock();
				return null;
			}
		});
		long cTime=System.currentTimeMillis();
		lock.lock();
		new Thread(new Runnable() {
			
			@Override
			public void run() {
				try {
					Thread.sleep(5000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				doTAG(11l);
			}
		}).start();
		try {
			condition.await(Constants.TIMEOUT_DELAY, TimeUnit.MILLISECONDS);
			if(!map.containsKey(11l)){
				retVal=true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			retVal=false;
		}finally{
			lock.unlock();
		}
		
		System.out.println(System.currentTimeMillis()-cTime+"ms---"+retVal);
	}
}
