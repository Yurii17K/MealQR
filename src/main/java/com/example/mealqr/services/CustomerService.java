package com.example.mealqr.services;

import com.example.mealqr.pojos.Customer;
import com.example.mealqr.repositories.CustomerRepository;
import org.springframework.stereotype.Service;

@Service
public class CustomerService {

    private final CustomerRepository customerRepository;

    public CustomerService(CustomerRepository customerRepository) {
        this.customerRepository = customerRepository;
    }

    public void signUpCustomer(String name, String lastName, String city, String allergies, String email, String pass) {
        customerRepository.save(Customer.builder()
                .name(name)
                .lastName(lastName)
                .city(city)
                .allergies(allergies)
                .email(email)
                .pass(pass)
                .build());
    }
}
