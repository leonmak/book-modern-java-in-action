package org.example.part3.order.mr;

import org.example.part3.order.Order;
public class Tax {
    public static double regional(double value) {
        return value * 1.1;
    }

    public static double general(double value) {
        return value * 1.3;
    }

    public static double surcharge(double value) {
        return value * 1.05;
    }

    public static double calculate(Order order, Boolean useRegional, Boolean useGeneral, Boolean useSurcharge) {
        double value = order.getValue();
        if (useRegional) {
            value = Tax.regional(value);
        }
        if (useGeneral) {
            value = Tax.general(value);
        }
        if (useSurcharge) {
            value = Tax.surcharge(value);
        }
        return value;
    }
}
