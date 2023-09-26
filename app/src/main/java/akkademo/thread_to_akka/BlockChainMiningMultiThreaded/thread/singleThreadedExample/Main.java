package akkademo.thread_to_akka.BlockChainMiningMultiThreaded.thread.singleThreadedExample;

import akkademo.thread_to_akka.BlockChainMiningMultiThreaded.thread.model.*;
import akkademo.thread_to_akka.BlockChainMiningMultiThreaded.thread.utils.BlockChainUtils;
import akkademo.thread_to_akka.BlockChainMiningMultiThreaded.thread.utils.BlocksData;

public class Main {

	public static void main(String[] args) throws BlockValidationException {
		
		int difficultyLevel = 5;
		
		Long start = System.currentTimeMillis();
		BlockChain blocks = new BlockChain();
		
		String lastHash = "0";
		for (int i = 0; i < 10; i++) {
			Block nextBlock = BlocksData.getNextBlock(i, lastHash);

			HashResult hashResult = BlockChainUtils.mineBlock(nextBlock, difficultyLevel, 0, 100000000);
			if (hashResult == null) {
				throw new RuntimeException("Didn't find a valid hash for block " + i);
			}
			
			nextBlock.setHash(hashResult.getHash());
			nextBlock.setNonce(hashResult.getNonce());
			blocks.addBlock(nextBlock);
			System.out.println("Block " + i + " hash : " + nextBlock.getHash());
			System.out.println("Block " + i + " nonce: " + nextBlock.getNonce());
			lastHash = nextBlock.getHash();
		}
				
		Long end = System.currentTimeMillis();
		blocks.printAndValidate();
		
		System.out.println("Time taken " + (end - start) + " ms.");
	}
}
