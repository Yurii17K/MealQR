package com.example.mealqr.domain;

import com.example.mealqr.services.ProfanitiesFilter;
import com.example.mealqr.web.rest.request.DishCommentReq;
import lombok.*;
import lombok.experimental.FieldDefaults;
import lombok.experimental.SuperBuilder;

import javax.persistence.Entity;
import javax.persistence.Table;
import java.util.UUID;

@Getter
@SuperBuilder
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "dish_comments")
@FieldDefaults(level = AccessLevel.PRIVATE)
public class DishComment extends DishOpinion{

    @Setter
    private String comment;

    public static DishComment of(String userEmail, DishCommentReq dishCommentReq, Dish dish) {
        return DishComment.builder()//
                .dishOpinionId(UUID.randomUUID().toString())//
                .comment(ProfanitiesFilter.filterBadLanguage(dishCommentReq.getComment()))//
                .userEmail(userEmail)//
                .dish(Dish.builder().dishId(dish.getDishId()).build())//
                .build();
    }

    public DishComment withComment(String comment) {
        this.setComment(ProfanitiesFilter.filterBadLanguage(comment));
        return this;
    }
}
