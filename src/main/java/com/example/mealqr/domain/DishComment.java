package com.example.mealqr.domain;

import com.example.mealqr.services.CurseLanguage;
import com.example.mealqr.web.rest.request.DishCommentReq;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;

@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "dish_comments")
@With
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DishComment extends DishOpinion{
    private String comment;

    public static DishComment of(String userEmail, DishCommentReq dishCommentReq, Dish dish) {
        return DishComment.builder()//
                .comment(CurseLanguage.filterBadLanguage(dishCommentReq.getComment()))//
                .userEmail(userEmail)//
                .dish(Dish.builder().dishId(dish.getDishId()).build())//
                .build();
    }
}
