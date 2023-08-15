package org.example.part3;

public class Main9 {
    public static void main(String[] args) {
        ch1();
    }

    private static void ch1() {
        int a = 10;
        Runnable r1 = new Runnable() {
            @Override
            public void run() {
                int a = 20;
                System.out.println("Hello World! : "+ a);
            }
        };

        // lamda
        Runnable r2 = () -> {
            int a = 30; // compile error : Variable 'a' is already defined in the scope
            System.out.println("Hello World");
        };

    }
}
