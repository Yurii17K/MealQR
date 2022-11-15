package com.example.mealqr.services.mappers;

import com.example.mealqr.domain.Dish;
import com.example.mealqr.domain.DishComment;
import com.example.mealqr.rest.reponse.DishWithOpinionsRes;
import io.vavr.collection.Seq;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class DishWithOpinionsResMapper {

    public static DishWithOpinionsRes mapToDishWithOpinionsRes(Dish dish, double rating, Seq<DishComment> dishComments) {
        return DishWithOpinionsRes.of(
                dish.getDishId().toString(),//
                dish.getDishName(),//
                dish.getRestaurant().getRestaurantName(),//
                ImageDtoMapper.mapToImageDto(dish.getDishImage()),//
                dish.getDishPrice().doubleValue(),//
                dish.getDishDescription(),//
                rating,//
                dishComments.map(dishComment -> DishWithOpinionsRes.DishCommentDto.of(
                                dishComment.getUserEmail(),//
                                dishComment.getComment())));
    }
}
