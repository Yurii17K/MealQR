package com.example.mealqr.pojos;

import lombok.Builder;
import lombok.Getter;

import java.util.Arrays;

@Getter
@Builder
public class QRData {

    private final String userEmail;
    private final String[] dishes;
    private final int[] dishQuantities;
    private final double[] itemCosts;
    private final double sum;

    // customization of Builder
    public abstract static class CustomQRDataBuilder extends QRDataBuilder{

        @Override
        public QRData build() {
            super.sum = Arrays.stream(super.itemCosts).sum();
            return super.build();
        }
    }
}
