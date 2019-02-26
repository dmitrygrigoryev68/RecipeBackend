package de.recipe.controller;

import de.recipe.model.Recipe;
import de.recipe.service.RecipeService;
import de.recipe.service.RecipeServiceWebTechnical;
import de.recipe.web.RecipeWeb;
import de.recipe.web.RecipeWebOutput;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RecipeController {
    private final RecipeService recipeService;
    private final RecipeServiceWebTechnical serviceWebTechnical;

    @Autowired
    public RecipeController(RecipeService recipeService, RecipeServiceWebTechnical serviceWebTechnical) {
        this.recipeService = recipeService;
        this.serviceWebTechnical = serviceWebTechnical;
    }


    @GetMapping( value = "/getAllRecipes" )
    public List <RecipeWebOutput> getAllRecipe() {
        return recipeService.getAllRecipe();
    }

    @PostMapping( value = "/addNewRecipes", consumes = "application/json" )
    public RecipeWeb creatRecipeController(@RequestBody RecipeWeb recipeWeb) {
        recipeService.creatRecipe(recipeWeb);
        return recipeWeb;
    }

    @GetMapping( "/recipes/{id}" )
    public RecipeWebOutput getRecipeById(@PathVariable Long id) {
        return recipeService.getRecipeById(id);
    }

    @DeleteMapping( value = "/recipes/{id}" )
    public List <RecipeWebOutput> delletRecipeByID(@PathVariable Long id) {
        recipeService.deleteRecipeById(id);
        return getAllRecipe();

    }

    @GetMapping( "/recipes/ingredients/{name_ingredient}" )
    public List findByIngredient(@PathVariable String name_ingredient) {
        return recipeService.findByIngredientsContaining(name_ingredient);
    }

    @GetMapping( value = "/recipes/author/{nameAuthor}" )
    public List <RecipeWebOutput> findBYAuthor(@PathVariable String nameAuthor) {
        List <RecipeWebOutput> recipeWebOutputList = recipeService.findbyAuthor(nameAuthor);
        return recipeWebOutputList;
    }

    @PutMapping( value = "/recipes/{id}" )
    public void updateRecipeById(@RequestBody RecipeWeb recipeWeb, @PathVariable long id) {
        Recipe recipe = (Recipe) serviceWebTechnical.convertTheReceiptsIntoAnotherEmbodiment(recipeWeb, Recipe.class);
        recipeService.updateRecipe(recipe, id);

    }

    //test
    @DeleteMapping( value = "/recipes/ingredients/name" )
    private void deleteIngredientsToRecipe(String nameIngredient) {
        recipeService.deleteIngredientsToRecipes(nameIngredient);
    }
}
