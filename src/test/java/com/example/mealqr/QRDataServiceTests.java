package com.example.mealqr;


import com.example.mealqr.pojos.CartItem;
import com.example.mealqr.pojos.QRData;
import com.example.mealqr.repositories.CartItemRepository;
import com.example.mealqr.repositories.DishRepository;
import com.example.mealqr.services.CartItemService;
import com.example.mealqr.services.QRDataService;
import io.vavr.Tuple;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class QRDataServiceTests {
    @Mock
    private CartItemRepository cartItemRepository;

    @Mock
    private DishRepository dishRepository;

    @InjectMocks
    private QRDataService qrDataService;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this); //without this you will get NPE
    }

    @Test
    public void contextLoads() {
    }

    @Test
    public void qrCodeCreated() {
        when(cartItemRepository.getCustomerCart(anyString())).thenReturn(new ArrayList<>());
        //when(dishRepository.findByID(anyInt())).thenReturn(Optional.empty());

        Assertions.assertInstanceOf(QRData.class,qrDataService.generateQRDataFromCustomerCart("testUser@user.com"));
    }
}
