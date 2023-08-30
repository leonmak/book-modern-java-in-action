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


        // return [shopName]:[price]:[DiscountCode]
        public String getPrice(String product) {
            double price = calculatePrice(product);
            Discount.Code code = Discount.Code.values()[new Random().nextInt(Discount.Code.values().length)];
            return String.format("%s:%.2f:%s", name, price, code);
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
    private final Executor customExecutor = Executors.newFixedThreadPool(Math.min(shops.size(), 100), new ThreadFactory() {
        @Override
        public Thread newThread(Runnable r) {
            Thread t = new Thread(r);
            // 데몬 스레드로 설정
            t.setDaemon(true);
            return t;
        }
    });

    public List<String> findPrices(String product) {
        return shops.stream()
                .map(shop -> shop.getPrice(product)) // 가격 세팅 [shopName]:[price]:[DiscountCode]
                .map(Quote::parse) // 가격 정보 파싱, Quote 객체 생성
                .map(Discount::applyDiscount)// Quote 기반 할인 가격 문자열 생성, [shopName] price is [price]
                .collect(toList());
    }

    public List<String> findPricesAsync(String product) {
        List<CompletableFuture<String>> priceFutures = shops.stream() // return Stream<Shop>
                .map(shop -> CompletableFuture.supplyAsync(()
                        -> shop.getPrice(product), customExecutor))// return Stream<CompletableFuture<String>>, async
                .map(future -> future.thenApply(Quote::parse))// return Stream<CompletableFuture<Quote>>, sync
                .map(future -> future.thenCompose(quote ->
                        CompletableFuture.supplyAsync(() ->
                                Discount.applyDiscount(quote), customExecutor))) // return Stream<CompletableFuture<String>>, async
                .collect(toList());

        return priceFutures.stream()
                .map(CompletableFuture::join) // return Stream<String>
                .collect(toList());
    }

    @Test
    public void tst2() {
        long start = System.nanoTime();
        System.out.println(findPricesAsync("Aespa - MY WORLD (정규 3집)"));
        long duration = (System.nanoTime() - start) / 1_000_000;
        System.out.println("Done in " + duration + " msecs");
    }

}
