package org.example.part5.bestpricefinder;

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

import static java.util.stream.Collectors.toList;

public class FinderApp {


    public class Shop {

        private String name;

        public Shop() {
        }

        public Shop(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
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
    @Disabled
    public void tst1() throws ExecutionException, InterruptedException {
        Shop shop = new Shop("Best Shop");

        long start = System.nanoTime();
        Future<Double> futurePrice = shop.getPriceAsync("IPhone 12");
        long invocationTime = ((System.nanoTime() - start) / 1_000_000);
        System.out.println("Invocation returned after " + invocationTime + " msecs");

        double price = futurePrice.get();
        System.out.printf("Price is %.2f%n", price);

        long retrievalTime = ((System.nanoTime() - start) / 1_000_000);
        System.out.println("Price returned after " + retrievalTime + " msecs");
    }

    private List<Shop> shops = List.of(new Shop("11번가"), new Shop("G마켓"), new Shop("옥션"), new Shop("쿠팡"), new Shop("위메프"));
    private final Executor customeExecutor = Executors.newFixedThreadPool(Math.min(shops.size(), 100), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            // 데몬 스레드로 설정
            t.setDaemon(true);
            return t;
        }
    });

    public List<String> findPrices(String product) {
//        return shops.stream()
//                .map(shop -> String.format("%s price is %.2f",
//                        shop.getName(), shop.getPrice(product)))
//                .collect(toList());

//        return shops.parallelStream()
//                .map(shop -> String.format("%s price is %.2f",
//                        shop.getName(), shop.getPrice(product)))
//                .collect(toList());

        List<CompletableFuture<String>> priceFutures = shops.stream()
                .map(shop -> CompletableFuture.supplyAsync(() -> String.format("%s price is %.2f",
                        shop.getName(), shop.getPrice(product)), customeExecutor))
                .collect(toList())
                .stream()
                // .map(CompletableFuture::join) // join을 사용하면 supplyAsync() 완료될 때까지 블록킹
                .collect(toList());

        return priceFutures.stream()
                .map(CompletableFuture::join)
                .collect(toList());
    }

    @Test
    public void tst2() {
        System.out.println("Runtime.getRuntime().availableProcessors() = " + Runtime.getRuntime().availableProcessors());
        long start = System.nanoTime();
        System.out.println(findPrices("Aespa - MY WORLD (정규 3집)"));
        long duration = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Done in " + duration + " msecs");
    }


}
