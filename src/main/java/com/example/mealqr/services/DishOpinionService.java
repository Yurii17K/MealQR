package com.example.mealqr.services;

import com.example.mealqr.pojos.*;
import com.example.mealqr.preferenceAnalysis.SlopeOne;
import com.example.mealqr.repositories.*;
import io.vavr.Tuple;
import io.vavr.Tuple2;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@AllArgsConstructor
public class DishOpinionService {

    private final DishCommentRepository dishCommentRepository;
    private final DishRatingRepository dishRatingRepository;
    private final DishRepository dishRepository;
    private final UserRepository userRepository;
    private final CustomerAllergyRepository customerAllergyRepository;
    private final RestaurantEmployeeRepository restaurantEmployeeRepository;
    private final SlopeOne slopeOne;
    private final HashSet<String> curseWords;
    private final Map<String, String> evasiveSymbols;

    public List<Tuple2<Dish, Tuple2<Double, List<String>>>> getAllDishesInRestaurantConfiguredForUser(
            @NotBlank String userEmail, @NotBlank String restaurantName) {

        if (!restaurantEmployeeRepository.existsByRestaurantName(restaurantName)) {
            return new ArrayList<>();
        }

        return getAllDishesInRestaurantSortedByUserPreference(userEmail, restaurantName) // analyse user preferences
                .stream()
                .filter(dish -> !isUserAllergicToDish(userEmail, dish)) // remove allergies
                .map(dish -> Tuple.of(dish, Tuple.of(getDishAverageRating(dish.getID()), getDishComments(dish.getID()))))
                .collect(Collectors.toCollection(LinkedList::new));
    }

    public Tuple2<Boolean, String> addOrUpdateRating(@NotBlank String userEmail, @NotBlank String dishName,
                                                     @NotBlank String restaurantName, @NotNull Integer rating) {
        Optional<Dish> optionalDish = dishRepository.findByDishNameAndRestaurantName(dishName, restaurantName);

        // might happen if restaurant employee removed a dish while this request was processing
        if (optionalDish.isEmpty()) {
            return Tuple.of(false, "Such dish does not exist");
        }

        Optional<DishRating> optionalDishRating =
                dishRatingRepository.findByDishIdAndUserEmail(optionalDish.get().getID(), userEmail);

        DishRating dishRating = DishRating.builder()
                .dishId(optionalDish.get().getID())
                .userEmail(userEmail)
                .rating(rating)
                .build();

        // if the rating is not present -> add it
        if (optionalDishRating.isEmpty()) {
            dishRatingRepository.save(dishRating);
            return Tuple.of(true, "Added rating to dish " + dishName + " from " + restaurantName);

        } else { // if the rating is present -> update it
            dishRating.setID(optionalDishRating.get().getID());
            dishRatingRepository.save(dishRating);
            return Tuple.of(true, "Updated rating to dish " + dishName + " from " + restaurantName);

        }

    }

    public Tuple2<Boolean, String> addOrUpdateComment(@NotBlank String userEmail, @NotBlank String dishName,
                                                      @NotBlank String restaurantName, @NotBlank String comment) {
        Optional<Dish> optionalDish = dishRepository.findByDishNameAndRestaurantName(dishName, restaurantName);

        // might happen if restaurant employee removed a dish while this request was processing
        if (optionalDish.isEmpty()) {
            return Tuple.of(false, "Such dish does not exist");
        }

        comment = filterBadLanguage(comment);

        Optional<DishComment> optionalDishComment =
                dishCommentRepository.findByDishIdAndUserEmail(optionalDish.get().getID(), userEmail);

        DishComment dishComment = DishComment.builder()
                .dishId(optionalDish.get().getID())
                .userEmail(userEmail)
                .comment(comment)
                .build();

        // if the comment is not present -> add it
        if (optionalDishComment.isEmpty()) {
            dishCommentRepository.save(dishComment);
            return Tuple.of(true, "Added comment to dish " + dishName + " from " + restaurantName);

        } else { // if the comment is present -> update it
            dishComment.setID(optionalDishComment.get().getID());
            dishCommentRepository.save(dishComment);
            return Tuple.of(true, "Updated comment to dish " + dishName + " from " + restaurantName);
        }

    }

    private String filterBadLanguage(String originalComment) {
        loadCurseWords();
        loadEvasiveSymbols();

        String copyOfOriginalComment = originalComment;


        // getting rid of sly symbols that hide word meanings
        for (Map.Entry<String, String> entry : evasiveSymbols.entrySet()) {
            copyOfOriginalComment = copyOfOriginalComment.replaceAll(entry.getKey(), entry.getValue());
        }

        String[] copyOfOriginalCommentTokenized = copyOfOriginalComment.trim().split(" ");
        String[] originalCommentTokenized = originalComment.trim().split(" ");

        // check all word permutations of the comment
        for (int k = 0; k < copyOfOriginalCommentTokenized.length; k++) {
            StringBuilder accumulatingSentenceToCheck = new StringBuilder(copyOfOriginalCommentTokenized[k]);

            for (int l = k + 1; l < copyOfOriginalCommentTokenized.length + 1; l++) {

                if (curseWords.contains(accumulatingSentenceToCheck.toString().toLowerCase(Locale.ROOT))) {
                    for (int i = k; i < l; i++) {
                        originalCommentTokenized[i] = "#".repeat(originalCommentTokenized[i].length());
                    }
                }

                // avoid going out of index on last array index
                if (l < copyOfOriginalCommentTokenized.length)
                    accumulatingSentenceToCheck.append(" ").append(copyOfOriginalCommentTokenized[l].trim());
            }
        }

        return String.join(" ", originalCommentTokenized);
    }

    private void loadCurseWords() {
        try(Scanner scanner = new Scanner(new File("src/main/resources/bad-words.csv"))) {
            String line;

            // read curse words and phrases and put in a map
            while (scanner.hasNext()) {
                line = scanner.nextLine();

                // skip first lines and language indication lines
                if (line.length() < 1 || line.charAt(0) == '-') {
                    continue;
                }

                curseWords.add(line.trim().toLowerCase(Locale.ROOT).replaceAll("[,]", ""));
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void loadEvasiveSymbols() {
        try(Scanner scanner = new Scanner(new File("src/main/resources/bad-words.csv"))) {
            String[] pairOfSymbols;

            // read curse words and phrases and put in a map
            while (scanner.hasNext()) {
                pairOfSymbols = scanner.nextLine().replaceAll(",", "").split(" ");

                evasiveSymbols.put(pairOfSymbols[1], pairOfSymbols[0]);
            }

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    private boolean isUserAllergicToDish(@NotBlank String userEmail, @NotNull Dish dish){
        Optional<CustomerAllergy> userAllergiesOptional = customerAllergyRepository.findByUserEmail(userEmail);

        if (userAllergiesOptional.isPresent()) {
            String[] userAllergies = userAllergiesOptional.get().getAllergies().toLowerCase().split("(, )|,");

            String dishDescription = dish.getDishDescription().toLowerCase();

            for (String userAllergy : userAllergies) {
                if (dishDescription.contains(userAllergy)) {
                    return true;
                }
            }
        }

        return false;
    }

    private Map<String, Map<Integer, Double>> getDataForPreferenceAnalysis(@NotBlank String restaurantName) {
        List<Integer> dishIdsInRestaurant = dishRepository.findAllByRestaurantName(restaurantName).stream().map(Dish::getID).sorted().collect(Collectors.toList());

        // take only the customers that rated sth in the restaurant
        List<String> customerEmails = userRepository.findAllCustomersWhoRatedAnythingInRestaurant(restaurantName)
                .stream().distinct().collect(Collectors.toList());

        return customerEmails.stream()
                .collect(Collectors.toMap(
                        Function.identity(),
                        email -> {

                            Map<Integer, Double> dishIdsAndRatingListForUser =
                                    dishRatingRepository
                                            .findAllByUserEmailAndRestaurantName(email, restaurantName)
                                            .stream()
                                            .collect(Collectors.toMap(
                                                    DishRating::getDishId,
                                                    d -> (double) d.getRating()
                                            ));


                            if (dishIdsInRestaurant.size() != dishIdsAndRatingListForUser.size()) {
                                for (int i = dishIdsAndRatingListForUser.size(); i < dishIdsInRestaurant.size(); i++) {
                                    dishIdsAndRatingListForUser.put(dishIdsInRestaurant.get(i),
                                            Math.random() * 5);
                                }
                            }

                            return dishIdsAndRatingListForUser;
                        }
                ));

    }

    private LinkedList<Dish> getAllDishesInRestaurantSortedByUserPreference(@NotBlank String userEmail,
                                                                            @NotBlank String restaurantName) {

        Map<Integer, Dish> dishesWithIdsInRestaurant = dishRepository.findAllByRestaurantName(restaurantName)
                .stream()
                .collect(Collectors.toMap(
                        Dish::getID,
                        Function.identity()
                ));

        // results of user preference analysis
        return slopeOne.slopeOne(userEmail, getDataForPreferenceAnalysis(restaurantName))
                .entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder())) // get most preferable at the top
                .map(integerDoubleEntry -> dishesWithIdsInRestaurant.get(integerDoubleEntry.getKey()))// map dishIds to Dish objects
                .collect(Collectors.toCollection(LinkedList::new)); // collect to a linked list to preserve sorted order
    }

    private List<String> getDishComments(@NotNull Integer dishID) {
        return dishCommentRepository
                .findAllByDishId(dishID)
                .stream()
                .map(DishComment::getComment)
                .collect(Collectors.toList());
    }

    private double getDishAverageRating(@NotNull Integer dishID) {
        return Math.round(dishRatingRepository
                .findAllByDishId(dishID)
                .stream()
                .mapToInt(DishRating::getRating)
                .average().orElse(0.d) * 2) / 2.0;
    }
}
