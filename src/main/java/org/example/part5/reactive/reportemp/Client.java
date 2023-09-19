package org.example.part5.reactive.reportemp;

import io.reactivex.rxjava3.core.Observable;

import java.util.Arrays;
import java.util.concurrent.Flow;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

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
//        Observable<TempInfo> observable = getTemperature("New York");
//        observable.blockingSubscribe(new TempObserver());

        Observable<TempInfo> observable = getCelsiusTemperatures("Seoul", "New York", "Tokyo");
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

    public static Observable<TempInfo> getCelsiusTemperature(String town) {
        return getTemperature(town) // return Observable<TempInfo>
                .map(temp ->
                        new TempInfo(temp.getTown(), (temp.getTemp() - 32) * 5 / 9)); // return Observable<TempInfo>
    }

    public static Observable<TempInfo> getTemperatureOnlyNegative(String town){
        return getTemperature(town)
                .filter(temp -> temp.getTemp() < 0);
    }

    public static Observable<TempInfo> getCelsiusTemperatures(String... towns){
        return Observable.merge(Arrays.stream(towns) // return Stream<String>
                .map(Client::getCelsiusTemperature) // return Stream<Observable<TempInfo>>
                .collect(Collectors.toList())); // return List<Observable<TempInfo>> -> Observable<TempInfo>
    }
}
