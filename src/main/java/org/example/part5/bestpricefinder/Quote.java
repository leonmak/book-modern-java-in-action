package org.example.part5.bestpricefinder;

public class Quote {
    private final String shopName;
    private final double price;
    private final Discount.Code discountCode;

    public Quote(String shopName, double price, Discount.Code code) {
        this.shopName = shopName;
        this.price = price;
        this.discountCode = code;
    }

    // 문자열을 파싱해서 Quote 객체를 만드는 factory 메서드
    public static Quote parse(String s) {
        String[] split = s.split(":");
        String shopName = split[0]; // 상점명
        double price = Double.parseDouble(split[1]); // 가격
        Discount.Code discountCode = Discount.Code.valueOf(split[2]); // 할인 코드
        return new Quote(shopName, price, discountCode); // Quote 객체 생성
    }

    public String getShopName() {
        return shopName;
    }

    public double getPrice() {
        return price;
    }

    public Discount.Code getDiscountCode() {
        return discountCode;
    }
}
