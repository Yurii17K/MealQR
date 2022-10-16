package com.example.mealqr;

import com.example.mealqr.domain.Dish;
import com.example.mealqr.domain.DishComment;
import com.example.mealqr.domain.DishRating;
import com.example.mealqr.repositories.DishCommentRepository;
import com.example.mealqr.repositories.DishRatingRepository;
import com.example.mealqr.repositories.DishRepository;
import com.example.mealqr.services.DishOpinionService;
import io.vavr.control.Either;
import io.vavr.control.Option;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static com.example.mealqr.TestData.*;
import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class DishOpinionServiceTests {

    @Mock
    private DishCommentRepository dishCommentRepository;
    @Mock
    private  DishRatingRepository dishRatingRepository;
    @Mock
    private  DishRepository dishRepository;

    @InjectMocks
    private DishOpinionService dishOpinionService;

    @Test
    void addOrUpdateRatingWhenDishDoesNotExistCaseInvalid(){
        //given
        when(dishRepository.findByDishNameAndRestaurantName(DISH_RATING_REQ.getDishName(),
                DISH_RATING_REQ.getRestaurantName())).thenReturn(Option.none());
        //when
        Either<String, DishRating> result = dishOpinionService.addOrUpdateRating(DISH_RATING_REQ);
        //then
        assertTrue(result.isLeft());
        assertThat(result.getLeft()).isEqualTo(SUCH_DISH_DOES_NOT_EXIST);
    }

    @Test
    void addOrUpdateRatingWhenRatingDoesNotExistCaseValid(){
        //given
        when(dishRepository.findByDishNameAndRestaurantName(DISH_RATING_REQ.getDishName(),
                DISH_RATING_REQ.getRestaurantName())).thenReturn(Option.of(DISH));
        when(dishRatingRepository.findByDishDishIdAndUserEmail(GLOBAL_ID_Integer, DISH_RATING_REQ.getUserEmail()))
                .thenReturn(Option.none());
        when(dishRatingRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        //when
        Either<String, DishRating> result = dishOpinionService.addOrUpdateRating(DISH_RATING_REQ);
        DishRating expected = DishRating.builder()//
                .rating(DISH_RATING_REQ.getRating())//
                .userEmail(DISH_RATING_REQ.getUserEmail())//
                .dish(Dish.builder().dishId(DISH.getDishId()).build())//
                .build();
        //then
        assertTrue(result.isRight());
        assertThat(result.get()).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void addOrUpdateRatingWhenRatingExistsCaseValid(){
        //given
        when(dishRepository.findByDishNameAndRestaurantName(DISH_RATING_REQ.getDishName(),
                DISH_RATING_REQ.getRestaurantName())).thenReturn(Option.of(DISH));
        when(dishRatingRepository.findByDishDishIdAndUserEmail(GLOBAL_ID_Integer, DISH_RATING_REQ.getUserEmail()))
                .thenReturn(Option.of(DISH_RATING));
        when(dishRatingRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        //when
        Either<String, DishRating> result = dishOpinionService.addOrUpdateRating(DISH_RATING_REQ);
        DishRating expected = DISH_RATING.withRating(DISH_RATING_REQ.getRating());
        //then
        assertTrue(result.isRight());
        assertThat(result.get()).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void addOrUpdateCommentWhenDishDoesNotExistCaseInvalid(){
        //given
        when(dishRepository.findByDishNameAndRestaurantName(DISH_COMMENT_REQ.getDishName(),
                DISH_COMMENT_REQ.getRestaurantName())).thenReturn(Option.none());
        //when
        Either<String, DishComment> result = dishOpinionService.addOrUpdateComment(DISH_COMMENT_REQ);
        //then
        assertTrue(result.isLeft());
        assertThat(result.getLeft()).isEqualTo(SUCH_DISH_DOES_NOT_EXIST);
    }

    @Test
    void addOrUpdateCommentWhenRatingDoesNotExistCaseValid(){
        //given
        when(dishRepository.findByDishNameAndRestaurantName(DISH_COMMENT_REQ.getDishName(),
                DISH_COMMENT_REQ.getRestaurantName())).thenReturn(Option.of(DISH));
        when(dishCommentRepository.findByDishDishIdAndUserEmail(GLOBAL_ID_Integer, DISH_COMMENT_REQ.getUserEmail()))
                .thenReturn(Option.none());
        when(dishCommentRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        //when
        Either<String, DishComment> result = dishOpinionService.addOrUpdateComment(DISH_COMMENT_REQ);
        DishComment expected = DishComment.builder()//
                .comment(DISH_COMMENT_REQ.getComment())//
                .userEmail(DISH_COMMENT_REQ.getUserEmail())//
                .dish(Dish.builder().dishId(DISH.getDishId()).build())//
                .build();
        //then
        assertTrue(result.isRight());
        assertThat(result.get()).usingRecursiveComparison().isEqualTo(expected);
    }

    @Test
    void addOrUpdateCommentWhenRatingExistsCaseValid(){
        //given
        when(dishRepository.findByDishNameAndRestaurantName(DISH_COMMENT_REQ.getDishName(),
                DISH_COMMENT_REQ.getRestaurantName())).thenReturn(Option.of(DISH));
        when(dishCommentRepository.findByDishDishIdAndUserEmail(GLOBAL_ID_Integer, DISH_COMMENT_REQ.getUserEmail()))
                .thenReturn(Option.of(DISH_COMMENT));
        when(dishCommentRepository.save(any())).thenAnswer(i -> i.getArguments()[0]);
        //when
        Either<String, DishComment> result = dishOpinionService.addOrUpdateComment(DISH_COMMENT_REQ);
        DishComment expected = DISH_COMMENT.withComment(DISH_COMMENT_REQ.getComment());
        //then
        assertTrue(result.isRight());
        assertThat(result.get()).usingRecursiveComparison().isEqualTo(expected);
    }
}
