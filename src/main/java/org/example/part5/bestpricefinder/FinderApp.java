package org.example.part5.bestpricefinder;

import org.junit.jupiter.api.Test;

import java.util.Random;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

public class FinderApp {


    public class Shopp {

        private String name;

        public Shopp() {
        }

        public Shopp(String name) {
            this.name = name;
        }

        public static void delay() {
            try {
                Thread.sleep(1000L);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        public double getPrice(String product) {
            return calculatePrice(product);
        }

        public Future<Double> getPriceAsync(String product) {
//            CompletableFuture<Double> futurePrice = new CompletableFuture<>();
//            new Thread(() -> {
//                try {
//                    double price = calculatePrice(product);
//                    futurePrice.complete(price);
//                } catch (Exception ex) {
//                    futurePrice.completeExceptionally(ex); // 에러 발생시 exception을 포함하여 complete
//                }
//            }).start();
//            return futurePrice;
            return CompletableFuture.supplyAsync(() -> calculatePrice(product));
        }

        private double calculatePrice(String product) {
//            if (product.equals("IPhone 12"))
//                throw new RuntimeException("에러 발생 ~~");

            delay();
            return new Random().nextDouble() * product.charAt(0) + product.charAt(1);
        }

    }

    @Test
    public void tst1() throws ExecutionException, InterruptedException {
        Shopp shop = new Shopp("Best Shop");

        long start = System.nanoTime();
        Future<Double> futurePrice = shop.getPriceAsync("IPhone 12");
        long invocationTime = ((System.nanoTime() - start) / 1_000_000);
        System.out.println("Invocation returned after " + invocationTime + " msecs");

        double price = futurePrice.get();
        System.out.printf("Price is %.2f%n", price);

        long retrievalTime = ((System.nanoTime() - start) / 1_000_000);
        System.out.println("Price returned after " + retrievalTime + " msecs");
    }

}
