package com.example.mealqr.services;

import com.example.mealqr.domain.Dish;
import com.example.mealqr.domain.DishRating;
import com.example.mealqr.exceptions.ApiError;
import com.example.mealqr.preferenceAnalysis.FastTextClient;
import com.example.mealqr.preferenceAnalysis.SlopeOne;
import com.example.mealqr.repositories.DishRatingRepository;
import com.example.mealqr.repositories.DishRepository;
import com.example.mealqr.services.mappers.DishResMapper;
import com.example.mealqr.web.rest.reponse.DishRes;
import io.vavr.API;
import io.vavr.collection.List;
import io.vavr.collection.Seq;
import io.vavr.control.Either;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class MasterRecommenderService {

    private final DishRepository dishRepository;
    private final DishRatingRepository dishRatingRepository;
    private final DishOpinionService dishOpinionService;
    private final FastTextClient fastTextClient;
    private final SlopeOne slopeOne;


    private final Integer collaborativeVSContenntCutoff = 10; //At how many dish reviews do we switch to mostly using collaborative filtering instead of content-based
    private final Double maxSlopeOneContrib = 0.8;
    public Map<String, Integer> getNumberOfRatingsForDishesInRestaurant(String restaurantId){
        List<Dish> dishes = dishRepository.findAllByRestaurantRestaurantId(restaurantId).toList();
        Map<String, Integer> ratingCount = new HashMap<>();
        for (Dish dish : dishes){
            ratingCount.put(dish.getDishId(),0);
        }
        for(String dishId : ratingCount.keySet()){
            ratingCount.put(dishId,Integer.valueOf(dishRatingRepository.findAllByDishDishId(dishId).size()));
        }
        return ratingCount;
    }
    //TODO: Consider only the last n dishes rated by the user, change for to a parallel stream
    private Map<String,Double> getFastTextDishRatings(String userEmail, String restaurantId){
        Map<String,Double> finalRatingPredictions = new HashMap<>();
        Seq<DishRating> dishesRatedByUser = dishRatingRepository.findAllByUserEmail(userEmail);

        Seq<DishRes> restaurantDishes = getAllDishesInRestaurant(restaurantId).get();

        if(dishesRatedByUser.size()==0){
            for(DishRes res : restaurantDishes){
                finalRatingPredictions.put(res.getDishId(),2.5);
            }
        }
        else{
            for(DishRes res : restaurantDishes){
                Double value = 0.0;
                for(DishRating existingRating : dishesRatedByUser){
                    Dish dishFromRating = existingRating.getDish();
                    Double similarity = fastTextClient.getTextualSimilarityBetweenDishes(dishFromRating.getDishName(),dishFromRating.getDishDescription(),res.getDishName(),res.getDishDescription());
                    value+=similarity*existingRating.getRating();
                }
                finalRatingPredictions.put(res.getDishId(),value/dishesRatedByUser.size());
            }
        }
        return finalRatingPredictions;
    }

    public Map<String, Double> getDishRatingPredictionsForRestaurantAndUser(String userEmail, String restaurantId){
        Map<String,Double> finalRatingPredictions = new HashMap<>();
        Map<String,Double> slopeOneResults = slopeOne.slopeOne(userEmail, dishOpinionService.getDataForPreferenceAnalysis(restaurantId));
        Map<String,Double> fastTextResults = getFastTextDishRatings(userEmail, restaurantId);
        Map<String, Integer> ratingFrequencies = getNumberOfRatingsForDishesInRestaurant(restaurantId);
        for(String key: ratingFrequencies.keySet()){
            Integer ratingCount = ratingFrequencies.get(key);
            Double slopeOneRating = slopeOneResults.get(key);
            Double fastTextRating = fastTextResults.get(key);
            Double slopeOneContrib = Math.min(ratingCount/collaborativeVSContenntCutoff,maxSlopeOneContrib);
            Double finalRating = (slopeOneRating*slopeOneContrib)+(fastTextRating*(1-slopeOneContrib));
            finalRatingPredictions.put(key,finalRating);
        }
        return finalRatingPredictions;
    }

    private Either<ApiError, Seq<DishRes>> getAllDishesInRestaurant(@NotBlank String restaurantId) {
        return API.Some(dishRepository.findAllByRestaurantRestaurantId(restaurantId))//
                .filter(Seq::nonEmpty)//
                .map(dishes -> dishes.map(DishResMapper::mapToDishRes))//
                .toEither(ApiError.buildError("The restaurant is empty or doesn't exist"));
    }
}
