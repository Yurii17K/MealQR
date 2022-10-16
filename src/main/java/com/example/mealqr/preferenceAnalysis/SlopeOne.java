package com.example.mealqr.preferenceAnalysis;

import org.springframework.stereotype.Component;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

/**
 * Slope One algorithm implementation
 */
@Component
public class SlopeOne {

    private static final Map<Integer, Map<Integer, Double>> diff = new HashMap<>();
    private static final Map<Integer, Map<Integer, Integer>> freq = new HashMap<>();
    private Map<String, Map<Integer, Double>> inputData = new HashMap<>();
    private final Map<String, Map<Integer, Double>> outputData = new HashMap<>();

    public Map<Integer, Double> slopeOne(@NotBlank String userEmail,
                                         @NotNull Map<String, Map<Integer, Double>> inputData) {

        this.inputData = inputData;

//        System.out.println("Slope One - Before the Prediction\n");
        buildDifferencesMatrix(inputData);
//        System.out.println("\nSlope One - With Predictions\n");
        return predict(inputData, userEmail);
    }

    /**
     * Based on the available data, calculate the relationships between the
     * items and number of occurences
     *
     * @param data
     *            existing user data and their items' ratings
     */
    private void buildDifferencesMatrix(Map<String, Map<Integer, Double>> data) {
        for (Map<Integer, Double> userRatings : data.values()) {
            for (Entry<Integer, Double> e : userRatings.entrySet()) {

                if (!diff.containsKey(e.getKey())) {
                    diff.put(e.getKey(), new HashMap<>());
                    freq.put(e.getKey(), new HashMap<>());
                }

                for (Entry<Integer, Double> e2 : userRatings.entrySet()) {
                    int oldCount = 0;
                    if (freq.get(e.getKey()).containsKey(e2.getKey())) {
                        oldCount = freq.get(e.getKey()).get(e2.getKey());
                    }
                    double oldDiff = 0.0;
                    if (diff.get(e.getKey()).containsKey(e2.getKey())) {
                        oldDiff = diff.get(e.getKey()).get(e2.getKey());
                    }
                    double observedDiff = e.getValue() - e2.getValue();
                    freq.get(e.getKey()).put(e2.getKey(), oldCount + 1);
                    diff.get(e.getKey()).put(e2.getKey(), oldDiff + observedDiff);
                }
            }
        }

        for (Integer j : diff.keySet()) {
            for (Integer i : diff.get(j).keySet()) {
                double oldValue = diff.get(j).get(i);
                int count = freq.get(j).get(i);
                diff.get(j).put(i, oldValue / count);
            }
        }

//        printData(data);
    }

    /**
     * Based on existing data predict all missing ratings. If prediction is not
     * possible, the value will be equal to -1
     *
     * @param data
     *            existing user data and their items' ratings
     */
    private Map<Integer, Double> predict(Map<String, Map<Integer, Double>> data, String userEmail) {
        Map<Integer, Double> uPred = new HashMap<>();
        Map<Integer, Integer> uFreq = new HashMap<>();

        for (Integer j : diff.keySet()) {
            uFreq.put(j, 0);
            uPred.put(j, 0.0);
        }

        for (Entry<String, Map<Integer, Double>> e : data.entrySet()) {
            for (Integer j : e.getValue().keySet()) {
                for (Integer k : diff.keySet()) {
                    try {
                        double predictedValue = diff.get(k).get(j) + e.getValue().get(j);
                        double finalValue = predictedValue * freq.get(k).get(j);
                        uPred.put(k, uPred.get(k) + finalValue);
                        uFreq.put(k, uFreq.get(k) + freq.get(k).get(j));
                    } catch (NullPointerException e1) {
                        System.out.println("NULLBUODFYBWILABFOYLWRBOIYLBUOYLBH");
                    }
                }
            }
            HashMap<Integer, Double> clean = new HashMap<>();
            for (Integer j : uPred.keySet()) {
                if (uFreq.get(j) > 0) {
                    clean.put(j, uPred.get(j) / uFreq.get(j));
                }
            }
            for (Integer j : inputData.values().stream().findFirst().get().keySet()) {
                if (e.getValue().containsKey(j)) {
                    clean.put(j, e.getValue().get(j));
                } else if (!clean.containsKey(j)) {
                    clean.put(j, -1.0);
                }
            }
            outputData.put(e.getKey(), clean);
        }

        return outputData.get(userEmail);
    }

//    private void printData(Map<String, Map<Integer, Double>> data) {
//        for (String userEmail : data.keySet()) {
//            System.out.println(userEmail + ":");
//            print(data.get(userEmail));
//        }
//    }
//
//    private void print(Map<Integer, Double> hashMap) {
//        NumberFormat formatter = new DecimalFormat("#0.000");
//        for (Integer j : hashMap.keySet()) {
//            System.out.println("Dish ID: " + j + " --> " + formatter.format(hashMap.get(j).doubleValue()));
//        }
//    }

}