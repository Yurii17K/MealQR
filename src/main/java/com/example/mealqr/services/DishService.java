package com.example.mealqr.services;

import com.example.mealqr.pojos.Dish;
import com.example.mealqr.repositories.DishRepository;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Random;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DishService {

    private final DishRepository dishRepository;

    public List<Dish> getAllDishesInRestaurant(@NotBlank String restaurantName) {
        return dishRepository.findAllByRestaurantName(restaurantName);
    }

    public Tuple2<Boolean, String> addDishToRestaurantOffer(@NotNull Dish dishToAdd) {
        if (dishRepository.findByDishNameAndRestaurantName(dishToAdd.getDishName(), dishToAdd.getRestaurantName()).isPresent()) {
            return Tuple.of(false, "Dish with this name already exists in this restaurant offer");
        } else {
            dishRepository.save(dishToAdd);

            // check if the dish was actually added
            if (dishRepository.findByDishNameAndRestaurantName(dishToAdd.getDishName(), dishToAdd.getRestaurantName()).isEmpty()) {
                return Tuple.of(false, "Sth went wrong when attempting to add a dish");
            } else {
                return Tuple.of(true, "Dish " + dishToAdd.getDishName() + " added to " + dishToAdd.getRestaurantName() + " offer");
            }
        }
    }

    public Tuple2<Boolean, String> removeDishFromRestaurantOffer(@NotBlank String dishName, @NotBlank String restaurantName) {
        if (dishRepository.findByDishNameAndRestaurantName(dishName, restaurantName).isEmpty()) {
            return Tuple.of(false, "Dish with this name does not exist in this restaurant offer");
        } else {
            dishRepository.deleteByDishNameAndRestaurantName(dishName, restaurantName);

            // check if the dish was actually deleted
            if (dishRepository.findByDishNameAndRestaurantName(dishName, restaurantName).isPresent()) {
                return Tuple.of(false, "Sth went wrong when attempting to delete a dish");
            } else {
                return Tuple.of(true, "Dish " + dishName + " removed from " + restaurantName + " offer");
            }
        }
    }

    public Tuple2<Boolean, String> updateDishInRestaurantOffer(@NotNull Dish dishWithNewData) {
        Optional<Dish> optionalDish = dishRepository
                .findByDishNameAndRestaurantName(dishWithNewData.getDishName(), dishWithNewData.getRestaurantName());

        if (optionalDish.isEmpty()) {
            return Tuple.of(false, "No such dish in " + dishWithNewData.getRestaurantName() + " offer");
        } else {

            Dish dishToCommit = Dish.builder()
                    .ID(optionalDish.get().getID())
                    .dishName(dishWithNewData.getDishName())
                    .dishImg(dishWithNewData.getDishImg())
                    .dishPrice(dishWithNewData.getDishPrice())
                    .dishDescription(dishWithNewData.getDishDescription())
                    .build();

            dishRepository.save(dishToCommit);

            return Tuple.of(true, "Dish " + dishWithNewData.getDishName() + " data updated in " + dishWithNewData.getRestaurantName() + " offer");
        }
    }

    public Dish getRandomDish() {
        List<Dish> dishList = dishRepository.findAll();

        if (dishList.isEmpty()) {
            return new Dish();
        }

        return dishList.get(new Random().nextInt(dishList.size()));
    }

    public Dish getRandomDishFromRestaurantOffer(@NotBlank String restaurantName) {
        List<Dish> dishList = dishRepository.findAllByRestaurantName(restaurantName);

        if (dishList.isEmpty()) {
            return new Dish();
        }

        return dishList.get(new Random().nextInt(dishList.size()));
    }




}
