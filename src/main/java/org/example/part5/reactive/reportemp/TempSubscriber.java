package org.example.part5.reactive.reportemp;

import java.util.concurrent.Flow;

public class TempSubscriber implements Flow.Subscriber<TempInfo> {
    private Flow.Subscription subscription;

    @Override
    public void onSubscribe(Flow.Subscription subscription) {
        this.subscription = subscription; // Subscription을 저장하고 첫 번째 요청을 전달
        subscription.request(1L);
    }

    @Override
    public void onNext(TempInfo item) {
        System.out.println(item); // 수신한 온도 출력
        subscription.request(1L); // 다음 정보를 요청
    }

    @Override
    public void onError(Throwable throwable) {
        System.err.println(throwable.getMessage());
    }

    @Override
    public void onComplete() {
        System.out.println("Done!");
    }
}
