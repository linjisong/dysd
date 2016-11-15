/**
 * Copyright (c) 2016-2017, the original author or authors (dysd_2016@163.com).
 * <p>
 * Licensed under the GPL, Version 3.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.gnu.org/licenses/gpl.html
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.dysd.util.sync;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import org.dysd.util.sync.impl.SingleonRunStatus;
import org.junit.Test;

public class SingleonRunStatusTest {

	@Test
	public void test(){
		int threads = 10000;
		CountDownLatch tasks = new CountDownLatch(threads);
		final CountDownLatch flag = new CountDownLatch(1);
		
		final SingleonTask task = new SingleonTask();	
		
		for(int i = 1; i <= threads; i++){
			final int no = i;
			new Thread("Thread "+ i){
				@Override
				public void run() {
					try {
						flag.await();
						task.doTask(no);
					} catch (InterruptedException e) {
						e.printStackTrace();
					} catch (Exception e) {
						e.printStackTrace();
					}
				}
			}.start();
			tasks.countDown();
		}
		
		try {
			tasks.await();
			flag.countDown();
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
		try {
			TimeUnit.MINUTES.sleep(1);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}
	
	private static class SingleonTask{
		private static final SingleonRunStatus run = SingleonRunStatus.newInstance("TestSingleonTask");
		/**
		 * 加载配置
		 * @throws Exception 
		 */
		public void doTask(int i) throws Exception{
			if(run.start()){
				try{
					System.out.println(Thread.currentThread().getName()+" ===> start. " );
					TimeUnit.SECONDS.sleep(1);
					run.success();
					System.out.println(Thread.currentThread().getName()+" ===> success. " );
				}catch(Exception e){
					run.failure();
					System.out.println(Thread.currentThread().getName()+" ===> failure. " );
					throw e;
				}
			}
		}
	}
}
