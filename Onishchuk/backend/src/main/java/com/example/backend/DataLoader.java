package com.example.backend;

import com.example.backend.model.*;
import com.example.backend.repo.*;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import java.util.Set;

@Component
public class DataLoader implements CommandLineRunner {

    private final RecipeRepository recipeRepository;
    private final IngredientRepository ingredientRepository;

    public DataLoader(RecipeRepository recipeRepository, IngredientRepository ingredientRepository) {
        this.recipeRepository = recipeRepository;
        this.ingredientRepository = ingredientRepository;
    }

    @Override
    public void run(String... args) {
        if (recipeRepository.count() > 0) return;

        // Создаем ингредиенты
        Ingredient eggs = ingredientRepository.save(new Ingredient("Яйца"));
        Ingredient milk = ingredientRepository.save(new Ingredient("Молоко"));
        Ingredient flour = ingredientRepository.save(new Ingredient("Мука"));
        Ingredient cheese = ingredientRepository.save(new Ingredient("Сыр"));
        Ingredient pasta = ingredientRepository.save(new Ingredient("Макароны"));
        Ingredient perec = ingredientRepository.save(new Ingredient("Перец"));
        Ingredient kapusta = ingredientRepository.save(new Ingredient("Капуста"));
        Ingredient pig = ingredientRepository.save(new Ingredient("Свинина"));

        Recipe pancakes = new Recipe();
        pancakes.setTitle("Блины");
        pancakes.setDescription("Спроси у мамы как приготовить");
        pancakes.setIngredients(Set.of(eggs, milk, flour));
        recipeRepository.save(pancakes);

        Recipe macncheese = new Recipe();
        macncheese.setTitle("Mac & Cheese");
        macncheese.setDescription("Свари макароны и натери сырка");
        macncheese.setIngredients(Set.of(pasta, cheese));
        recipeRepository.save(macncheese);

        Recipe hachapurri = new Recipe();
        hachapurri.setTitle("Хачапури");
        hachapurri.setDescription("Спроси у Грузина как приготовить");
        hachapurri.setIngredients(Set.of(eggs, flour, cheese, milk));
        recipeRepository.save(hachapurri);

        Recipe galubzy = new Recipe();
        galubzy.setTitle("Галубцы");
        galubzy.setDescription("ГАААлубцы даууааааай");
        galubzy.setIngredients(Set.of(pig, kapusta));
        recipeRepository.save(galubzy);

        System.out.println("Base data loaded!");
    }
}