package org.example.part3.order.withlamda;

import org.example.part3.order.Order;
import org.example.part3.order.Trade;

import java.util.function.Consumer;

public class LambdaOrderBuilder {

    private Order order = new Order();

    public static Order order(Consumer<LambdaOrderBuilder> consumer) {
        LambdaOrderBuilder builder = new LambdaOrderBuilder();
        consumer.accept(builder); // landa 표현식 실행
        return builder.order; // builder.order를 반환
    }

    public void forCustomer(String customer) {
        order.setCustomer(customer);
    }

    public void buy(Consumer<TradeBuilder> consumer) {
        trade(consumer, Trade.Type.BUY);
    }

    public void sell(Consumer<TradeBuilder> consumer) {
        trade(consumer, Trade.Type.SELL);
    }

    private void trade(Consumer<TradeBuilder> consumer, Trade.Type type) {
        TradeBuilder builder = new TradeBuilder();
        builder.getTrade().setType(type);
        consumer.accept(builder); // landa 표현식 실행
        order.addTrade(builder.getTrade());  // builder.trade를 order에 추가
    }
}
