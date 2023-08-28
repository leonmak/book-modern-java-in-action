package org.example.part5;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

public class ScheduledExecutorServiceExample {

    public static void main(String[] args) {

        ScheduledExecutorService executorService = Executors.newScheduledThreadPool(1);

        work1();
        executorService.schedule(ScheduledExecutorServiceExample::work2, 10, TimeUnit.SECONDS); // 10 seconds delay

        executorService.shutdown();
    }

    private static void work1() {
        System.out.println("Hello from work1!");
    }

    private static void work2() {
        System.out.println("Hello from work2!");
    }

}
