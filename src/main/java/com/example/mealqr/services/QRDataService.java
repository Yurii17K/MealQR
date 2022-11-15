package com.example.mealqr.services;

import com.example.mealqr.domain.CartItem;
import com.example.mealqr.repositories.CartItemRepository;
import com.example.mealqr.repositories.PromoCodeRepository;
import com.example.mealqr.rest.reponse.QRDataRes;
import io.vavr.collection.Seq;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;

@Service
@RequiredArgsConstructor
public class QRDataService {

    private final CartItemRepository cartItemRepository;
    private final PromoCodeRepository promoCodeRepository;

    public QRDataRes generateQRDataFromCustomerCart(@NotBlank String userEmail) {
        Seq<CartItem> customerCartItems = cartItemRepository.getCustomerCart(userEmail);
        return QRDataRes.of(
                userEmail,
                customerCartItems.map(cartItem -> QRDataRes.CartItemRes.of(
                                cartItem.getDish().getDishName(),//
                                cartItem.getCartItemCost(),//
                                cartItem.getDishQuantity())));
    }

//    public QRDataRes generateQRDataFromCustomerCart(@NotBlank String userEmail, @NotBlank String promoCodeString) {
//        Seq<CartItem> customerCartItems = cartItemRepository.getCustomerCart(userEmail);
//        Option<PromoCode> promoCode = promoCodeRepository.findByPromoCodeStringAndAndRestaurant(promoCodeString,
//                customerCartItems.headOption().get().getDish().getRestaurant().getRestaurantId());
//
////        List<PromoCode> promoCodes = customerCartItems
////                .map(CartItem::getDish)
////                .flatMap(promoCodeRepository::findByDish)
////                .filter(x->(!x.isUsed()))
////                .collect(Collectors.toList());
////        Optional<PromoCode> existingCode = promoCodes.stream().filter(x->x.getPromoCodeString().compareTo(promocode)==0).findFirst();
////        if(existingCode.isEmpty()){
////            return generateQRDataFromCustomerCart(userEmail);
////        }
////        PromoCode theCode = existingCode.get();
//        theCode.setUsed(true);
//        promoCodeRepository.save(theCode);
//        return QRDataRes.of(
//                userEmail,
//                customerCartItems.map(cartItem -> QRDataRes.CartItemRes.of(
//                        cartItem.getDish().getDishName(),//
//                        cartItem.getCartItemCost().subtract(existingCode.get().getPriceReduction()).max(BigDecimal.ZERO),
//                        cartItem.getDishQuantity())),
//                promocode);
//    }
}
