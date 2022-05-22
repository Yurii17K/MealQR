package com.example.mealqr.services;

import com.example.mealqr.pojos.CustomerAllergy;
import com.example.mealqr.pojos.RestaurantEmployee;
import com.example.mealqr.pojos.User;
import com.example.mealqr.repositories.CustomerAllergyRepository;
import com.example.mealqr.repositories.RestaurantEmployeeRepository;
import com.example.mealqr.repositories.UserRepository;
import com.example.mealqr.security.JWT;
import com.example.mealqr.security.Roles;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import lombok.AllArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Optional;
import java.util.function.Supplier;

@Service
@AllArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final CustomerAllergyRepository customerAllergyRepository;
    private final RestaurantEmployeeRepository restaurantEmployeeRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public Tuple2<Boolean, String> singInUser(@NotBlank String userEmail, @NotBlank String userPass) {
        Optional<User> optionalUser = userRepository.findUserByEmail(userEmail);

        if (optionalUser.isEmpty() || !bCryptPasswordEncoder.matches(userPass, optionalUser.get().getPass())) {
            return Tuple.of(false, "Wrong Credentials");
        } else {
            return Tuple.of(true, JWT.generateToken(optionalUser.get()));
        }
    }

    public Tuple2<Boolean, String> signUpCustomer(@NotNull User user, @NotBlank String allergies) {
        Supplier<Object> supplier =
                () -> customerAllergyRepository.save(new CustomerAllergy(user.getEmail(), allergies));

        return createUser(user, supplier);
    }

    public Tuple2<Boolean, String> signUpRestaurantEmployee(@NotNull User user, @NotBlank String restaurantName) {
        Supplier<Object> supplier =
                () -> restaurantEmployeeRepository.save(new RestaurantEmployee(user.getEmail(), restaurantName));

        return createUser(user, supplier);
    }

    public Tuple2<Boolean, String> updateCustomerAllergies(@NotBlank String userEmail, @NotBlank String allergies) {
        Optional<User> optionalUser = userRepository.findUserByEmail(userEmail);

        if (optionalUser.isEmpty() || !Roles.CUSTOMER.equals(optionalUser.get().getRole())) {
            return Tuple.of(false, "No such customer");
        }

        customerAllergyRepository.save(new CustomerAllergy(userEmail, allergies));

        Optional<CustomerAllergy> optionalCustomerAllergy = customerAllergyRepository.findByUserEmail(userEmail);

        if (optionalCustomerAllergy.isEmpty() || !allergies.equals(optionalCustomerAllergy.get().getAllergies())) {
            return Tuple.of(false, "Customer allergies are NOT updated");
        }

        return Tuple.of(true, "Customer allergies are updated");
    }

    private Tuple2<Boolean, String> createUser(@NotNull User user, @NotNull Supplier<Object> function) {
        if (userRepository.findUserByEmail(user.getEmail()).isPresent()) {
            return Tuple.of(false, "User with this email already exists");
        } else {

            user.setPass(bCryptPasswordEncoder.encode(user.getPass()));

            userRepository.save(user);

            // check if user was indeed created and throws exception if not
            if (userRepository.findUserByEmail(user.getEmail()).isEmpty()) {
                return Tuple.of(false, "Something went wrong when trying to add a user to the database");
            } else {

                // add user's allergies or restaurant name to corresponding tables depending on the type
                function.get();

                return Tuple.of(true, JWT.generateToken(user));
            }
        }
    }

}
