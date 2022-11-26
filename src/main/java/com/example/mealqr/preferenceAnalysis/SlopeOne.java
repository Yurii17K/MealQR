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

    private static final Map<String, Map<String, Double>> diff = new HashMap<>(); //Differences between user ratings
    private static final Map<String, Map<String, Integer>> freq = new HashMap<>();
    private Map<String, Map<String, Double>> inputData = new HashMap<>();
    private final Map<String, Map<String, Double>> outputData = new HashMap<>();


    //Returns a mapping of <Dish_id, predicted_rating>
    //inputData is <User email, <Dish_id, Dish_rating>> for all users that rated something in the given restaurant
    //Eg. a map from users to their respective dish ratings
    public Map<String, Double> slopeOne(@NotBlank String userEmail,
                                         @NotNull Map<String, Map<String, Double>> inputData) {

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
    private void buildDifferencesMatrix(Map<String, Map<String, Double>> data) {
        for (Map<String, Double> userRatings : data.values()) {
            for (Entry<String, Double> e : userRatings.entrySet()) {

                if (!diff.containsKey(e.getKey())) {
                    diff.put(e.getKey(), new HashMap<>());
                    freq.put(e.getKey(), new HashMap<>());
                }

                for (Entry<String, Double> e2 : userRatings.entrySet()) {
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

        for (String j : diff.keySet()) {
            for (String i : diff.get(j).keySet()) {
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
    //data is <User email, <Dish_id, Dish_rating>> for all users that rated something in the given restaurant
    //Eg. a map from users to their respective dish ratings
    private Map<String, Double> predict(Map<String, Map<String, Double>> data, String userEmail) {
        Map<String, Double> uPred = new HashMap<>();
        Map<String, Integer> uFreq = new HashMap<>();

        for (String j : diff.keySet()) {
            uFreq.put(j, 0);
            uPred.put(j, 0.0);
        }

        for (Entry<String, Map<String, Double>> e : data.entrySet()) {
            for (String userId : e.getValue().keySet()) {
                for (String k : diff.keySet()) {
                    try {
                        double predictedValue = diff.get(k).get(userId) + e.getValue().get(userId);
                        double finalValue = predictedValue * freq.get(k).get(userId);
                        uPred.put(k, uPred.get(k) + finalValue);
                        uFreq.put(k, uFreq.get(k) + freq.get(k).get(userId));
                    } catch (NullPointerException e1) {
                        System.out.println("NULLBUODFYBWILABFOYLWRBOIYLBUOYLBH");
                    }
                }
            }
            HashMap<String, Double> clean = new HashMap<>();
            for (String j : uPred.keySet()) {
                if (uFreq.get(j) > 0) {
                    clean.put(j, uPred.get(j) / uFreq.get(j));
                }
            }
            for (String j : inputData.values().stream().findFirst().get().keySet()) {
                if (e.getValue().containsKey(j)) {
                    clean.put(j, e.getValue().get(j));
                } else {
                    clean.putIfAbsent(j, -1.0);
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