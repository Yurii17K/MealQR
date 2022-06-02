package com.example.mealqr;


import com.example.mealqr.pojos.QRData;
import com.example.mealqr.repositories.CartItemRepository;
import com.example.mealqr.services.QRDataService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.ArrayList;

import static org.junit.Assert.assertNotNull;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@RunWith(MockitoJUnitRunner.class)
public class QRDataServiceTests {

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private QRDataService qrDataService;

    @BeforeEach
    public void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void qrCodeCreated() {
        when(cartItemRepository.getCustomerCart(anyString())).thenReturn(new ArrayList<>());

        assertNotNull(qrDataService.generateQRDataFromCustomerCart("testUser@user.com"));
    }
}
