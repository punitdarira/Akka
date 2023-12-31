package akkademo.thread_to_akka.BlockChainMiningMultiThreaded.thread.multiThreadedExample;

import akkademo.thread_to_akka.BlockChainMiningMultiThreaded.thread.model.HashResult;

public class CheckForResults implements Runnable {
	
	private HashResult hashResult;
	
	public CheckForResults(HashResult hashResult) {
		this.hashResult = hashResult;
	}



	@Override
	public void run() {
		while (!hashResult.isComplete()) {
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}

}
