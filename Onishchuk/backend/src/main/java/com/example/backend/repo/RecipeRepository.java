package com.example.backend.repo;

import com.example.backend.model.Recipe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface RecipeRepository extends JpaRepository<Recipe, Long> {

    @Query("SELECT DISTINCT r FROM Recipe r JOIN r.ingredients i WHERE i.name IN :names")
    List<Recipe> findByIngredientNames(@Param("names") List<String> names);

    interface IngredientRepo extends JpaRepository<com.example.backend.model.Ingredient, Long> {
        com.example.backend.model.Ingredient findByName(String name);
    }
}