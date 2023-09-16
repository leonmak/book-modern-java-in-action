package org.example.part5.reactive.reportemp;

import io.reactivex.rxjava3.core.Observable;

import java.util.concurrent.Flow;
import java.util.concurrent.TimeUnit;

public class Client {

    public static void main(String[] args) {
//        getTemperatures("Seoul") // Publisher 생성
//                .subscribe(new TempSubscriber()); // Subscriber 등록

        // RxJava
//        Observable<Long> onePerSec = Observable.interval(1, TimeUnit.SECONDS);

        // Daemon thread로 실행
//        onePerSec.subscribe(i -> System.out.println(TempInfo.fetch( "New York" )));

//        onePerSec.blockingSubscribe(
//                i -> System.out.println(TempInfo.fetch("New York"))
//        );

        // use RxJava
        Observable<TempInfo> observable = getTemperature("New York");
        observable.blockingSubscribe(new TempObserver());
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


    /*
    * 1초 마다 5번 온도를 보고하는 Observable을 생성
    * */
    public static Observable<TempInfo> getTemperature(String town) {

        return Observable.create(emitter -> // Observable을 생성
                Observable.interval(1, TimeUnit.SECONDS)
                        .subscribe(i -> {
                            if (!emitter.isDisposed()) { // 구독이 해지되었는지 확인
                                if (i >= 5) { // 5번 온도를 보고했으면 완료
                                    emitter.onComplete(); // 완료를 알림
                                } else {
                                    try {
                                        emitter.onNext(TempInfo.fetch(town)); // 온도를 Subscriber로 전달
                                    } catch (Exception e) {
                                        emitter.onError(e);
                                    }
                                }
                            }
                        }));
    }
}
