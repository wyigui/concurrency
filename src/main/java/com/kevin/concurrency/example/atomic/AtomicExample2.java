package com.kevin.concurrency.example.atomic;

import com.kevin.concurrency.annotations.NotThreadSafe;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.atomic.LongAdder;


/**
 * Created by Kevin on 2018/8/24.
 * LongAdder
 */
@Slf4j
@NotThreadSafe
public class AtomicExample2 {
    // 总请求数
    public static int clientTotal = 5000;
    //同时执行并发数
    public static int threadTotal = 200;
    public static LongAdder count = new LongAdder();

    public static void main(String[] args) {
        ExecutorService executorService = Executors.newCachedThreadPool();
        final Semaphore semaphore = new Semaphore(threadTotal);
        final CountDownLatch countDownLatch = new CountDownLatch(clientTotal);
        for (int i = 0; i < clientTotal; i++) {
            executorService.execute(() -> {
                try {
                    semaphore.acquire();
                    add();
                    semaphore.release();

                } catch (Exception e) {
                    log.error("exception", e);
                }
                countDownLatch.countDown();

            });
        }
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        executorService.shutdown();
        log.info("count:{}", count);
    }

    private static void add() {
        count.add(1L);
    }

}

