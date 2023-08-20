package org.example.part3;

import io.cucumber.java.en.Given;
import io.cucumber.java.en.Then;
import io.cucumber.java.en.When;
import io.cucumber.java8.En;
import org.example.part3.order.Order;
import org.example.part3.order.Stock;
import org.example.part3.order.Trade;

import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class BuyStocksSteps implements En {

    private Map<String, Integer> stockUnitPrices = new HashMap<>();
    private Order order = new Order();

    @Given("^the price of a \"(.*?)\" stock is (\\d+)\\$$")
    public void setUnitPrice(String stock, int unitPrice) {
        stockUnitPrices.put(stock, unitPrice);
    }

    @When("^I buy (\\d+) \"(.*?)\"$")
    public void buiStocks(int quantity, String stockName) {
        Trade trade = new Trade();
        trade.setType(Trade.Type.BUY);

        Stock stock = new Stock();
        stock.setSymbol(stockName);

        trade.setStock(stock);
        trade.setQuantity(quantity);
        trade.setPrice(stockUnitPrices.get(stockName));
        order.addTrade(trade);
    }

    @Then("^the order value should be (\\d+)\\$$")
    public void checkOrderValue(int expectedValue) {
        assertEquals(expectedValue, order.getValue());
    }

    public BuyStocksSteps(){
        Given("\"^the price of a \\\"(.*?)\\\" stock is (\\\\d+)\\\\$$\"", (stock, unitPrice) ->{
            stockUnitPrices.put((String) stock, (Integer) unitPrice);
        });
    }
}
