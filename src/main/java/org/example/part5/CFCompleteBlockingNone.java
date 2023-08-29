package org.example.part5;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class CFCompleteBlockingNone {
    public static void main(String[] args) throws ExecutionException, InterruptedException {
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        int x = 1337;

        CompletableFuture<Integer> a = new CompletableFuture<>(); // fx
        CompletableFuture<Integer> b = new CompletableFuture<>(); // gx
        CompletableFuture<Integer> c = a.thenCombine(b, (y, z) -> y + z); // fx + gx

        executorService.submit(() -> a.complete(f(x))); // asynchronous
        executorService.submit(() -> b.complete(g(x))); // asynchronous

        System.out.println("f(x) + g(x) = " + c.get());

        executorService.shutdown();
    }

    private static int f(int x) throws InterruptedException {
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
