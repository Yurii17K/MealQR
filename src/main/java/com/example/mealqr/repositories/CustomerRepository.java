package com.example.mealqr.repositories;

import com.example.mealqr.pojos.Customer;
import com.example.mealqr.pojos.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CustomerRepository extends JpaRepository<Customer, Integer>{


}
