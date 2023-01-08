package com.example.mealqr.services;

import com.example.mealqr.domain.CartItem;
import com.example.mealqr.domain.Dish;
import com.example.mealqr.domain.PromoCode;
import com.example.mealqr.exceptions.ApiError;
import com.example.mealqr.repositories.CartItemRepository;
import com.example.mealqr.repositories.DishRepository;
import com.example.mealqr.repositories.PromoCodeRepository;
import com.example.mealqr.services.mappers.CartItemResMapper;
import com.example.mealqr.web.rest.reponse.CartItemRes;
import io.vavr.API;
import io.vavr.collection.Seq;
import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final DishRepository dishRepository;
    private final PromoCodeRepository promoCodeRepository;
    public static final String PROMOCODE = "PROMOCODE";

    private static final String SUCH_DISH_DOES_NOT_EXIST = "Such dish does not exist";

    public Seq<CartItemRes> getCustomerCart(@NotBlank String userEmail) {
        Seq<CartItem> customerCart = cartItemRepository.getCustomerCart(userEmail);
        return Option.of(RequestContextHolder.currentRequestAttributes().getAttribute(PROMOCODE, RequestAttributes.SCOPE_SESSION))//
                .map(promo -> applyPromoToCartItems((PromoCode) promo, customerCart))//
                .getOrElse(customerCart)
                .map(CartItemResMapper::mapToCartItemRes);
    }

    public double getCustomerCartCost(@NotBlank String userEmail) {
        BigDecimal customerCartCost = cartItemRepository.getCustomerCart(userEmail)//
                .map(CartItem::getCartItemCost)//
                .reduce(BigDecimal::add);
        return Option.of(RequestContextHolder.currentRequestAttributes().getAttribute(PROMOCODE, RequestAttributes.SCOPE_SESSION))//
                .map(promo -> applyPromoToCartCost((PromoCode) promo, customerCartCost))//
                .getOrElse(customerCartCost)//
                .doubleValue();
    }

    @Transactional
    public boolean clearCustomerCart(@NotBlank String userEmail) {
        cartItemRepository.deleteAllByUserEmail(userEmail);
        return true;
    }

    @Transactional
    public boolean clearCustomerCart(@NotBlank String userEmail, String restaurantId) {
        cartItemRepository.deleteByUserEmailAndDishRestaurantRestaurantId(userEmail, restaurantId);
        return true;
    }

    @Transactional
    public Either<ApiError, Dish> addDishToCustomerCart(@NotBlank String userEmail, @NotBlank String dishId) {
        return dishRepository.findByDishId(dishId)//
                .peek(dish -> cartItemRepository.findByUserEmailAndDishDishId(userEmail, dish.getDishId())//
                        .map(cartItem -> {
                            alterDishQuantityInCustomerCart(cartItem, dish, 1);
                            return dish;
                        })
                        .getOrElse(() -> {
                            addDishToCustomerCart(userEmail, dish);
                            return dish;
                        }))
                .toEither(ApiError.buildError(SUCH_DISH_DOES_NOT_EXIST, HttpStatus.NOT_FOUND));
    }

    private void alterDishQuantityInCustomerCart(CartItem cartItem, Dish dish, int quantity) {
        if ((cartItem.getDishQuantity() + quantity) <= 0) {
            cartItemRepository.deleteById(cartItem.getCartItemId());
        } else {
            cartItemRepository.save(cartItem//
                    .withDishQuantity(cartItem.getDishQuantity() + quantity)//
                    .withCartItemCost(dish.getDishPrice().multiply(BigDecimal.valueOf(cartItem.getDishQuantity() + quantity))));
        }
    }

    private void addDishToCustomerCart(String userEmail, Dish dish) {
        cartItemRepository.save(CartItem.builder()//
                .cartItemId(UUID.randomUUID().toString())//
                .userEmail(userEmail)//
                .dish(dish)//
                .dishQuantity(1)//
                .cartItemCost(dish.getDishPrice())//
                .build());
    }

    @Transactional
    public Either<ApiError, Boolean> alterDishQuantityInCustomerCart(@NotBlank String userEmail, @NotBlank String dishId,
            @NotNull int quantity) {
        return dishRepository.findByDishId(dishId)//
                .peek(dish -> alterDishQuantityInCustomerCart(//
                        cartItemRepository.findByUserEmailAndDishDishId(userEmail, dishId).get(),//
                        dish,//
                        quantity))//
                .map(dish -> true)//
                .toEither(ApiError.buildError(SUCH_DISH_DOES_NOT_EXIST, HttpStatus.NOT_FOUND));
    }

    public Either<ApiError, Boolean> deleteDishFromCustomerCart(@NotBlank String userEmail, @NotBlank String dishName,
            @NotBlank String restaurantId) {
        return dishRepository.findByDishNameAndRestaurantRestaurantId(dishName, restaurantId)//
                .peek(dish -> cartItemRepository.deleteByUserEmailAndDishDishId(userEmail, dish.getDishId()))//
                .map(dish -> true)//
                .toEither(ApiError.buildError(SUCH_DISH_DOES_NOT_EXIST, HttpStatus.NOT_FOUND));
    }

    public Either<ApiError, Boolean> registerPromoInSession(String userEmail, String promoCode) {
        Seq<CartItem> customerCart = cartItemRepository.getCustomerCart(userEmail);
        Option<PromoCode> currentPromo = promoCodeRepository.findByPromoCodeStringAndRestaurantRestaurantId(promoCode,
                customerCart.headOption().get().getDish().getRestaurant().getRestaurantId());
        return currentPromo
                .toValidation(ApiError.buildError("Promo code is not valid", HttpStatus.NOT_FOUND))//
                .flatMap(this::canPromoBeUsed)//
                .peek(validCode -> RequestContextHolder.currentRequestAttributes().setAttribute(PROMOCODE, validCode, RequestAttributes.SCOPE_SESSION))//
                .map(validCode -> true)//
                .toEither();
    }

    private Validation<ApiError, PromoCode> canPromoBeUsed(PromoCode promoCode) {
        return API.Some(promoCode)//
                .filter(code -> code.getUsesLeft() > 0)//
                .toValidation(ApiError.buildError("Promo code exceeded the amount of uses"));
    }

    private Seq<CartItem> applyPromoToCartItems(PromoCode currentPromo, Seq<CartItem> cartItems) {
        switch (currentPromo.getPromoCodeType()) {
            case DISH_SPECIFIC_PERCENT:
                return cartItems
                        .filter(cartItem -> cartItem.getDish().getDishId().equals(currentPromo.getDish().getDishId()))
                        .map(cartItem -> cartItem.withCartItemCost(cartItem.getCartItemCost().multiply(BigDecimal.valueOf(1.0d - 0.01 * currentPromo.getPriceReduction()))));
            case DISH_SPECIFIC:
                return cartItems
                        .filter(cartItem -> cartItem.getDish().getDishId().equals(currentPromo.getDish().getDishId()))
                        .map(cartItem -> cartItem.withCartItemCost(cartItem.getCartItemCost().subtract(BigDecimal.valueOf(currentPromo.getPriceReduction()))));
            default:
                return cartItems;
        }
    }

    private BigDecimal applyPromoToCartCost(PromoCode currentPromo, BigDecimal cartCost) {
        switch (currentPromo.getPromoCodeType()) {
            case CART_PERCENT:
                return cartCost.multiply(BigDecimal.valueOf(1.0d - currentPromo.getPriceReduction()));
            case CART:
                return cartCost.subtract(BigDecimal.valueOf(currentPromo.getPriceReduction()));
            default:
                return cartCost;
        }
    }
}
