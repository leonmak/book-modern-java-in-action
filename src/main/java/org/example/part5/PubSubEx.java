package org.example.part5;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Flow;

public class PubSubEx {

    @Test
    public void tst1() {
        SimpleCell c1 = new SimpleCell("C1");
        SimpleCell c2 = new SimpleCell("C2");
        SimpleCell c3 = new SimpleCell("C3");

        c1.subscribe(c3); // c3가 c1을 구독

        c1.onNext(10); // c1에 10을 전달 -> c3에 10이 전달
        c2.onNext(20); // c2에 20을 전달

    }

    @Test
    public void tst2() {

        ArithmeticCell c3 = new ArithmeticCell("c3");
        SimpleCell c2 = new SimpleCell("c2");
        SimpleCell c1 = new SimpleCell("c1");

        // TODO : compile error : Subscriber<Integer> is not a functional interface
//        c1.subscribe(c3::setLeft);
//        c2.subscribe(c3::setRight);

        c1.onNext(10); // Update value of C1 to 10
        c2.onNext(20); // update value of C2 to 20
        c1.onNext(15); // update value of C1 to 15


    }

    public class SimpleCell implements Flow.Publisher<Integer>, Flow.Subscriber<Integer> {
        private int value = 0;
        private String name;
        private List<Flow.Subscriber> subscribers = new ArrayList<>();

        public SimpleCell(String name) {
            this.name = name;
        }

        @Override
        public void subscribe(Flow.Subscriber<? super Integer> subscriber) {
            subscribers.add(subscriber);
        }

        @Override
        public void onSubscribe(Flow.Subscription subscription) {

        }

        @Override
        public void onNext(Integer newValue) {
            this.value = newValue;
            notifyAllSubscribers();
        }

        private void notifyAllSubscribers() {
            subscribers.forEach(subscriber -> subscriber.onNext(this.value));
        }


        @Override
        public void onError(Throwable throwable) {

        }

        @Override
        public void onComplete() {

        }


    }


    public class ArithmeticCell extends SimpleCell {
        private int left;
        private int right;

        public ArithmeticCell(String name) {
            super(name);
        }

        public void setLeft(int left) {
            this.left = left;
            onNext(left + this.right);
        }

        public void setRight(int right) {
            this.right = right;
            onNext(right + this.left);
        }
    }
}
