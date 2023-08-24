package org.example.part4;

public class Tmp {

    public interface A {
        default void hello() {
            System.out.println("Hello from A");
        }
    }

    public interface B {
        default void hello() {
            System.out.println("Hello from B");
        }
    }

    public class C implements B, A {

        @Override
        public void hello() {
            B.super.hello(); // B의 default 메서드 호출
        }
    }
}
