package org.example.part5;

public class ThreadExample {

    public static void main(String[] args) throws InterruptedException {
        int x = 1337;
        Result result = new Result();

        Thread thread1 = new Thread(() -> {
            result.left = f(x);
        });

        Thread thread2 = new Thread(() -> {
            result.right = g(x);
        });

        thread1.start();
        thread2.start();

        thread1.join();
        thread2.join();



    }

    public static int f(int x) {
        return 0;
    }

    public static int g(int x) {
        return 0;
    }

    private static class Result {
        private int left;
        private int right;
    }
}
