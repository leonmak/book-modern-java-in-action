package org.example.part6;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.function.Function;

public class Main19 {

    static class TrainJourney {
        public int price; // 가격
        public TrainJourney onward; // 다음 열차

        public TrainJourney(int p, TrainJourney t) {
            price = p;
            onward = t;
        }

        @Override
        public String toString() {
            return "TrainJourney{" +
                    "price=" + price +
                    ", onward=" + onward +
                    '}';
        }
    }


    // from 의 끝에 to 를 연결한다. (linked list)
    static TrainJourney link(TrainJourney from, TrainJourney to) {
        if (from == null) {
            return to;
        }

        TrainJourney t = from;
        while (t.onward != null) {
            t = t.onward;
        }
        t.onward = to;
        return from;
    }

    @Test
    @DisplayName("x -> y -> z")
    public void tst1() {
        TrainJourney firstJourney = new TrainJourney(10, null);
        TrainJourney secondJourney = new TrainJourney(20, null);

        TrainJourney linkedJourney = link(firstJourney, secondJourney);

        System.out.println(linkedJourney); // TrainJourney{price=10, onward=TrainJourney{price=20, onward=null}}
        System.out.println(firstJourney); // TrainJourney{price=10, onward=TrainJourney{price=20, onward=null}}
    }

    static TrainJourney append(TrainJourney a, TrainJourney b) {
        return a == null ? b : new TrainJourney(a.price, append(a.onward, b));
    }

    @Test
    @DisplayName("x -> y -> z (funtional)")
    public void tst2() {
        TrainJourney firstJourney = new TrainJourney(10, null);
        TrainJourney secondJourney = new TrainJourney(20, null);

        TrainJourney linkedJourney = append(firstJourney, secondJourney);

        System.out.println(linkedJourney); // TrainJourney{price=10, onward=TrainJourney{price=20, onward=null}}
        System.out.println(firstJourney); // TrainJourney{price=10, onward=null}
    }


    static class Tree {
        private String key;
        private int val;
        private Tree left, right;

        public Tree(String k, int v, Tree l, Tree r) {
            key = k;
            val = v;
            left = l;
            right = r;
        }
    }

    class TreeProcessor {
        public static int lookup(String k, int defaultval, Tree t) {
            if (t == null) return defaultval;
            if (k.equals(t.key)) return t.val;
            return lookup(k, defaultval,
                    k.compareTo(t.key) < 0 ? t.left : t.right);
        }
        // other methods processing a Tree
    }

    public static void update1(String k, int newval, Tree t) {
        if (t == null) { /* should add a new node */ } else if (k.equals(t.key)) t.val = newval;
        else update1(k, newval, k.compareTo(t.key) < 0 ? t.left : t.right);
    }

    public static Tree update2(String k, int newval, Tree t) {
        if (t == null)
            t = new Tree(k, newval, null, null);
        else if (k.equals(t.key))
            t.val = newval;
        else if (k.compareTo(t.key) < 0)
            t.left = update2(k, newval, t.left);
        else
            t.right = update2(k, newval, t.right);
        return t;
    }

    public static Tree updateFunctional(String k, int newval, Tree t) {
        return (t == null) ?
                new Tree(k, newval, null, null) :
                k.equals(t.key) ?
                        new Tree(k, newval, t.left, t.right) :
                        k.compareTo(t.key) < 0 ?
                                new Tree(t.key, t.val, updateFunctional(k, newval, t.left), t.right) :
                                new Tree(t.key, t.val, t.left, updateFunctional(k, newval, t.right));
    }


    static <A, B, C> Function<A, C> compose(Function<B, C> g, Function<A, B> f) {
        return x -> g.apply(f.apply(x));
    }

    static <A> Function<A, A> repeat(int n, Function<A, A> f) {
        return n == 0 ? x -> x : compose(f, repeat(n - 1, f));
    }

    @Test
    @DisplayName("repeat functional method")
    public void tst3() {
        Function<Integer, Integer> f = x -> x + 1;
        Function<Integer, Integer> g = repeat(3, f);

        System.out.println(repeat(3, (Integer x) -> 2 * x).apply(10)); // 80
    }


}
