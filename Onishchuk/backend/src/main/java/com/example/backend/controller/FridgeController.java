package com.example.backend.controller;

import com.example.backend.model.*;
import com.example.backend.repo.*;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class FridgeController {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;

    public FridgeController(RecipeRepository recipeRepository, IngredientRepository ingredientRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @PostMapping("/cook")
    public List<Recipe> findRecipesByBasket(@RequestBody List<String> basketRaw) {
        if (basketRaw.isEmpty()) return List.of();

        List<String> myBasket = basketRaw.stream()
                .map(String::toLowerCase)
                .map(String::trim)
                .toList();

        List<Recipe> candidates = recipeRepository.findByIngredientNames(basketRaw);

        return candidates.stream()
                .filter(recipe -> {
                    Set<Ingredient> requiredIngredients = recipe.getIngredients();

                    boolean hasAllIngredients = requiredIngredients.stream()
                            .allMatch(reqIng -> {
                                String reqName = reqIng.getName().toLowerCase();
                                return myBasket.stream()
                                        .anyMatch(basketItem -> isFuzzyMatch(basketItem, reqName));
                            });

                    return hasAllIngredients;
                })
                .collect(Collectors.toList());
    }

    @GetMapping("/recipes")
    public List<Recipe> getAllRecipes() {
        return recipeRepository.findAll();
    }


    @PostMapping("/recipe")
    @Transactional
    public void addRecipe(@RequestBody Recipe recipe) {
        Set<Ingredient> processedIngredients = new HashSet<>();
        for (Ingredient rawIng : recipe.getIngredients()) {
            Ingredient existing = ingredientRepository.findByName(rawIng.getName());
            if (existing != null) {
                processedIngredients.add(existing);
            } else {
                rawIng.setName(rawIng.getName().trim());
                processedIngredients.add(ingredientRepository.save(rawIng));
            }
        }
        recipe.setIngredients(processedIngredients);
        recipeRepository.save(recipe) ;
    }

    @GetMapping("/status")
    public String status() { return "OK"; }

    // ==========================================
    // Расстояние Левенштейна
    // ==========================================

    private boolean isFuzzyMatch(String input, String target) {
        if (target.equals(input) || target.contains(input)) return true;

        int distance = calculateWeightedLevenshtein(input, target);

        return distance <= 2;
    }

    private int calculateWeightedLevenshtein(String s1, String s2) {
        int m = s1.length();
        int n = s2.length();

        int[][] dp = new int[m + 1][n + 1];

        for (int i = 0; i <= m; i++) dp[i][0] = i;
        for (int j = 0; j <= n; j++) dp[0][j] = j;

        for (int i = 1; i <= m; i++) {
            for (int j = 1; j <= n; j++) {
                if (s1.charAt(i - 1) == s2.charAt(j - 1)) {
                    dp[i][j] = dp[i - 1][j - 1];
                } else {
                    int cost = (i <= 2 || j <= 2) ? 3 : 1;

                    dp[i][j] = Math.min(
                            dp[i - 1][j - 1] + cost,
                            Math.min(
                                    dp[i - 1][j] + cost,
                                    dp[i][j - 1] + cost
                            )
                    );
                }
            }
        }
        return dp[m][n];
    }
}