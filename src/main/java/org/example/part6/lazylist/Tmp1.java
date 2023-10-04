package org.example.part6.lazylist;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

public class Tmp1 {
    interface MyList<T> {
        T head();

        MyList<T> tail();

        default boolean isEmpty() {
            return true;
        }
    }

    class MyLinkedList<T> implements MyList<T> {
        private final T head;
        private final MyList<T> tail;

        public MyLinkedList(T head, MyList<T> tail) {
            this.head = head;
            this.tail = tail;
        }

        public T head() {
            return head;
        }

        public MyList<T> tail() {
            return tail;
        }

        public boolean isEmpty() {
            return false;
        }

        @Override
        public String toString() {
            return "MyLinkedList{" +
                    "head=" + head +
                    ", tail=" + tail +
                    '}';
        }
    }

    class Empty<T> implements MyList<T> {
        public T head() {
            throw new UnsupportedOperationException();
        }

        public MyList<T> tail() {
            throw new UnsupportedOperationException();
        }

    }

    @Test
    @DisplayName("sample MyLinkedList")
    public void tst1() {
        MyList<Integer> l = new MyLinkedList<>(5, new MyLinkedList<>(10, new Empty<>()));
        System.out.println(l.toString());
    }

    class LazyList<T> implements MyList<T> {
        final T head;
        final Supplier<MyList<T>> tail;

        public LazyList(T head, Supplier<MyList<T>> tail) {
            this.head = head;
            this.tail = tail;
        }

        public T head() {
            return head;
        }

        public MyList<T> tail() {
            return tail.get(); // Supplier를 통해 Lazy하게 계산
        }

        public boolean isEmpty() {
            return false;
        }
    }

    public LazyList<Integer> from(int n) {
        return new LazyList<Integer>(n, () -> from(n + 1));
    }

    @Test
    @DisplayName("sample LazyList")
    public void tst2() {
        LazyList<Integer> numbers = from(2);
        int two = numbers.head();
        int three = numbers.tail() // lazy : 3
                .head();

        int four = numbers.tail().tail() // lazy : 4
                .head();

        System.out.println(two + " " + three + " " + four);
    }



}
