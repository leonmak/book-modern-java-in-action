package org.example.part5;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CFCompleteBlocking {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        int x = 1337;

        CompletableFuture<Integer> a = new CompletableFuture<>();
        executorService.submit(() -> a.complete(f(x))); // asynchronous
        int b = g(x); // synchronous

        System.out.println("f(x) + g(x) = " + (a.get() + b)); // a.get() blocks until the result is ready

        executorService.shutdown();
    }

    private static int f(int x) {
        int result = x + 10;
        System.out.println("f(" + x + ") = " + result);

        return result;
    }

    private static int g(int x) {
        int result = x + 1;
        System.out.println("g(" + x + ") = " + result);
        return result;
    }
}
