package com.example.base.ui;

import java.util.ArrayList;
import java.util.List;

public class Recipe {
    private String name;
    private List<String> ingredients;
    private List<String> quantite; //

    public Recipe(String name, List<String> ingredients, List<String> quantite) {
        this.name = name;
        this.ingredients = (ingredients != null) ? ingredients : new ArrayList<>();
        this.quantite = (quantite != null) ? quantite : new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public List<String> getIngredients() {
        return ingredients;
    }

    public List<String> getQuantite() {
        return quantite;
    }
}