package org.example.part5.reactive.reportemp;

import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.Flow;
import java.util.concurrent.TimeUnit;

public class Client {

    public static void main(String[] args) {
//        getTemperatures("Seoul") // Publisher 생성
//                .subscribe(new TempSubscriber()); // Subscriber 등록

        // RxJava
        Observable<Long> onePerSec = Observable.interval(1, TimeUnit.SECONDS);
//        onePerSec.subscribe(i -> System.out.println(TempInfo.fetch( "New York" )));
        onePerSec.blockingSubscribe(
                i -> System.out.println(TempInfo.fetch("New York"))
        );
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
