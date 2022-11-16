package com.example.mealqr;

import com.example.mealqr.domain.*;
import com.example.mealqr.rest.request.DishCommentReq;
import com.example.mealqr.rest.request.DishRatingReq;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;

import java.math.BigDecimal;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
@FieldDefaults(makeFinal = true, level = AccessLevel.PACKAGE)
public class TestData {

    static String GLOBAL_ID_String = "globalId";
    static String USER_EMAIL = "userEmail";
    static String DISH_DESCRIPTION = "dishDescription";
    static BigDecimal DISH_PRICE = BigDecimal.ONE;
    static String DISH_NAME = "dishName";
    static String RESTAURANT_ID = "restaurantId";
    static Integer GLOBAL_ID_Integer = 1;
    static Integer DISH_QUANTITY = 4;
    static Integer DISH_RATING_SCORE = 5;
    static String DISH_COMMENT_COMMENT = "dishComment";

    static Dish DISH = Dish.builder()//
            .dishId(GLOBAL_ID_Integer)//
            .dishPrice(BigDecimal.ONE)//
            .dishDescription(DISH_DESCRIPTION)//
            .restaurant(Restaurant.builder().restaurantId(RESTAURANT_ID).build())//
            .dishName(DISH_NAME)//
            .build();

    static CartItem CART_ITEM = CartItem.builder()//
            .cartItemId(GLOBAL_ID_Integer)//
            .cartItemCost(BigDecimal.ONE)//
            .dish(DISH)//
            .userEmail(USER_EMAIL)//
            .dishQuantity(4)//
            .build();

    static DishRatingReq DISH_RATING_REQ = DishRatingReq.of(
            DISH_NAME,
            RESTAURANT_ID,
            DISH_RATING_SCORE);

    static DishCommentReq DISH_COMMENT_REQ = DishCommentReq.of(
            DISH_NAME,
            RESTAURANT_ID,
            DISH_COMMENT_COMMENT);

    static DishRating DISH_RATING = DishRating.builder()//
            .dishOpinionId(GLOBAL_ID_Integer)//
            .userEmail(USER_EMAIL)//
            .rating(DISH_RATING_SCORE)//
            .dish(DISH)//
            .build();

    static DishComment DISH_COMMENT = DishComment.builder()//
            .dishOpinionId(GLOBAL_ID_Integer)//
            .userEmail(USER_EMAIL)//
            .comment(DISH_COMMENT_COMMENT)//
            .dish(DISH)//
            .build();
}
