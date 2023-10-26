package akkademo.search_utility.virtual_thread;

import org.apache.commons.lang3.time.StopWatch;

import java.io.File;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

import static java.util.concurrent.TimeUnit.SECONDS;

public class DirectorySearcherVirtualThread {

    public static void main(String[] args) throws Exception {
        StopWatch stopWatch = new StopWatch();
        stopWatch.start();
        //ExecutorService executorService = Executors.newVirtualThreadPerTaskExecutor();
        String searchDirectory = "C:\\";
        AtomicInteger totalNoOfFiles = new AtomicInteger();
        AtomicInteger activeThreads = new AtomicInteger();
        //Set<Thread> threadSets = Collections.synchronizedSet(new HashSet<>());

        startNewVirtualThread(searchDirectory, totalNoOfFiles, activeThreads);

        while (activeThreads.get() != 0){
            Thread.sleep(1);
        }
        stopWatch.stop();
        System.out.println("No of files " + totalNoOfFiles);
        System.out.println("Time Took By Virtual Thread= " + stopWatch.getTime(SECONDS));
    }

    private static void startNewVirtualThread(String path, AtomicInteger totalNoOfFiles, AtomicInteger activeThreads) {
        Thread.ofVirtual().start(() -> {
            activeThreads.incrementAndGet();
            File file = new File(path);
            if (file.list() != null) {
                for (String childPath : file.list()) {
                    File childFile = new File(path + childPath);
                    if (childFile.isDirectory() && !childFile.isHidden()) {
                        startNewVirtualThread(path + childPath + "\\", totalNoOfFiles, activeThreads);
                    } else {
                        System.out.println("FileName is " + childFile.getPath());
                        totalNoOfFiles.incrementAndGet();
                    }
                }
            }
            activeThreads.decrementAndGet();
        });
    }
}
