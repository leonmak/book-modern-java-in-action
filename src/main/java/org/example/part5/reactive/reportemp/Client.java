package org.example.part5.reactive.reportemp;

import java.util.concurrent.Flow;

public class Client {

    public static void main(String[] args) {
        getTemperatures("Seoul") // Publisher 생성
                .subscribe(new TempSubscriber()); // Subscriber 등록
    }

    public static Flow.Publisher<TempInfo> getTemperatures(String town) {
//        return subscriber -> subscriber.onSubscribe(
//                new TempSubscription(subscriber, town));

        return subscriber -> {
            TempProcessor processor = new TempProcessor();
            processor.subscribe(subscriber);
            processor.onSubscribe(new TempSubscription(processor, town));
        };
    }
}
