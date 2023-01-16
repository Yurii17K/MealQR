package com.example.mealqr.services;

import com.example.mealqr.domain.CartItem;
import com.example.mealqr.domain.Dish;
import com.example.mealqr.domain.PromoCode;
import com.example.mealqr.exceptions.ApiError;
import com.example.mealqr.repositories.CartItemRepository;
import com.example.mealqr.repositories.DishRepository;
import com.example.mealqr.repositories.PromoCodeRepository;
import com.example.mealqr.security.CustomPrincipal;
import com.example.mealqr.services.mappers.CartItemResMapper;
import com.example.mealqr.services.mappers.DishResMapper;
import com.example.mealqr.web.rest.reponse.CartItemRes;
import com.example.mealqr.web.rest.reponse.DishRes;
import com.example.mealqr.web.rest.reponse.TokenRes;
import io.vavr.API;
import io.vavr.collection.Seq;
import io.vavr.control.Either;
import io.vavr.control.Option;
import io.vavr.control.Validation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.math.BigDecimal;
import java.util.UUID;

import static com.example.mealqr.security.JWT.generateToken;

@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;
    private final DishRepository dishRepository;
    private final PromoCodeRepository promoCodeRepository;
    public static final String PROMOCODE = "PROMOCODE";

    private static final String SUCH_DISH_DOES_NOT_EXIST = "Such dish does not exist";

    public Seq<CartItemRes> getCustomerCart(@NotBlank CustomPrincipal customPrincipal) {
        Seq<CartItem> customerCart = cartItemRepository.getCustomerCart(customPrincipal.getUsername());
        return Option.of(customPrincipal.getPromoCodeId())//
                .flatMap(promoId -> Option.ofOptional(promoCodeRepository.findById(customPrincipal.getPromoCodeId())))//
                .map(promo -> applyPromoToCartItems(promo, customerCart))//
                .getOrElse(customerCart)
                .map(CartItemResMapper::mapToCartItemRes);
    }

    public double getCustomerCartCost(@NotBlank CustomPrincipal customPrincipal) {
        return getCustomerCart(customPrincipal).map(CartItemRes::getCartItemCost).sum().doubleValue();
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
    public Either<ApiError, DishRes> addDishToCustomerCart(@NotBlank String userEmail, @NotBlank String dishId) {
        return validateDishAdd(userEmail, dishId)//
                .peek(dish -> cartItemRepository.findByUserEmailAndDishDishId(userEmail, dish.getDishId())//
                        .map(cartItem -> {
                            alterDishQuantityInCustomerCart(cartItem, dish, 1);
                            return dish;
                        })
                        .getOrElse(() -> {
                            addDishToCustomerCart(userEmail, dish);
                            return dish;
                        }))
                .map(DishResMapper::mapToDishRes)//
                .toEither();
    }

    private Validation<ApiError, Dish> validateDishAdd(String userEmail, String dishId) {
        Option<Dish> dishOption = dishRepository.findByDishId(dishId);
        if (dishOption.isEmpty()) {
            return Validation.invalid(ApiError.buildError(SUCH_DISH_DOES_NOT_EXIST, HttpStatus.NOT_FOUND));
        }
        Option<CartItem> cartItemOption = cartItemRepository.getCustomerCart(userEmail).headOption();
        if (cartItemOption.isEmpty()) {
            return Validation.valid(dishOption.get());
        } else {
            return cartItemOption//
                    .filter(cartItem -> cartItem.getDish().getRestaurant().getRestaurantId().equals(dishOption.get().getRestaurant().getRestaurantId()))//
                    .map(cartItem -> dishOption.get())//
                    .toValidation(ApiError.buildError("Dishes from different restaurants can not be added to the cart simultaneously"));
        }
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

    @Transactional
    public Either<ApiError, Boolean> deleteDishFromCustomerCart(@NotBlank String userEmail, @NotBlank String dishId) {
        return dishRepository.findByDishId(dishId)//
                .peek(dish -> cartItemRepository.deleteByUserEmailAndDishDishId(userEmail, dish.getDishId()))//
                .map(dish -> true)//
                .toEither(ApiError.buildError(SUCH_DISH_DOES_NOT_EXIST, HttpStatus.NOT_FOUND));
    }

    public Either<ApiError, TokenRes> registerPromoInSession(CustomPrincipal customPrincipal, String promoCode) {
        Seq<CartItem> customerCart = cartItemRepository.getCustomerCart(customPrincipal.getUsername());
        if (customerCart.isEmpty()) {
            return Either.left(ApiError.buildError("Please add dishes to your cart before applying a promo code"));
        }
        Option<PromoCode> currentPromo = promoCodeRepository.findByPromoCodeStringAndRestaurantRestaurantId(promoCode,
                customerCart.headOption().get().getDish().getRestaurant().getRestaurantId());
        return currentPromo
                .toValidation(ApiError.buildError("Promo code can not be used for this restaurant", HttpStatus.NOT_FOUND))//
                .flatMap(this::canPromoBeUsed)//
                .peek(promo -> promoCodeRepository.save(promo.withUsesLeft(promo.getUsesLeft() - 1)))//
                .map(PromoCode::getPromoCodeId)//
                .peek(customPrincipal::setPromoCodeId)//
                .map(validCode -> TokenRes.of(generateToken(customPrincipal), true))//
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
                return cartItems//
                        .filter(cartItem -> cartItem.getDish().getDishId().equals(currentPromo.getDish().getDishId()))//
                        .map(cartItem -> cartItem.withCartItemCost(cartItem.getCartItemCost().multiply(BigDecimal.valueOf(1.0d - 0.01 * currentPromo.getPriceReduction()))));
            case DISH_SPECIFIC:
                return cartItems//
                        .filter(cartItem -> cartItem.getDish().getDishId().equals(currentPromo.getDish().getDishId()))//
                        .map(cartItem -> cartItem.withCartItemCost(cartItem.getCartItemCost().subtract(BigDecimal.valueOf(currentPromo.getPriceReduction()))));
            case CART:
                return cartItems//
                        .map(cartItem -> cartItem.withCartItemCost(cartItem.getCartItemCost().subtract(BigDecimal.valueOf((float) currentPromo.getPriceReduction() / cartItems.size()))));
            case CART_PERCENT:
                return cartItems//
                        .map(cartItem -> cartItem.withCartItemCost(cartItem.getCartItemCost().multiply(BigDecimal.valueOf(1.0d - 0.01 * currentPromo.getPriceReduction()))));
            default:
                return cartItems;
        }
    }

    // Too costly to compute and replaced with on-place computation from cart items
    @Deprecated(forRemoval = true)
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
