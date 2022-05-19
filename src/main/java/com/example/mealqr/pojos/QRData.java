package com.example.mealqr.pojos;

import lombok.Builder;
import lombok.Getter;

import java.util.Arrays;

@Getter
@Builder
public class QRData {

    private final String customerMail;
    private final String[] dishes;
    private final int[] dishQuantities;
    private final double[] itemPrices;
    private final double sum;

    public QRData(String customerMail, String[] dishes, double[] itemPrices, double sum, int[] dishQuantities) {
        this.customerMail = customerMail;
        this.dishes = dishes;
        this.dishQuantities = dishQuantities;
        this.itemPrices = itemPrices;
        this.sum = Arrays.stream(itemPrices).sum();
    }
}
