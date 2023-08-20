package org.example.part3.order;

public class TradeBuilder {

    private final MethodChainingOrderBuilder builder;
    private final Trade trade = new Trade();

    public TradeBuilder(MethodChainingOrderBuilder builder, Trade.Type type, int quantity) {
        this.builder = builder;
        this.trade.setType(type);
        this.trade.setQuantity(quantity);
    }

    public StockBuilder stock(String symbol) {
        return new StockBuilder(builder, trade, symbol);
    }
}
