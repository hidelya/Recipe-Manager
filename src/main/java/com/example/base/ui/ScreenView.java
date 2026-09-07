package com.example.base.ui;

import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.component.button.Button; // ✅ BON IMPORT

import java.util.ArrayList;
import java.util.List;


@Route("screen")

public class ScreenView extends VerticalLayout
{   List<Recipe> recipeList = new ArrayList<>();
    Grid<Recipe> grid = new Grid<>();
    public ScreenView(){

        H1 title = new H1("Mon application de recettes de la mort qui tue");
        Button addButton = new Button("+");
        Span textAddRecipe = new Span("Add new recipe");
        addButton.addClickListener(event -> AddRecipe());

        Span vide = new Span();
        Button basketButton = new Button("Basket");

        HorizontalLayout topBar = new HorizontalLayout(addButton, textAddRecipe, vide, basketButton);
        topBar.setWidthFull();
        topBar.expand(vide);
        topBar.setAlignItems(Alignment.CENTER);


        VerticalLayout verticalLayoutRecipe = new VerticalLayout();

        grid.addColumn(Recipe::getName).setHeader("Nom de la recette");
        grid.addColumn(recipe -> String.join(", ", recipe.getIngredients()))
                .setHeader("Ingrédients");

        // Liaison initiale de la liste
        grid.setItems(recipeList);

        // Ajout de la grid au layout principal



        add(title, topBar);
        add(grid);
    }

    private void AddRecipe()
    {

        Dialog formRecipe = new Dialog();
        Span title = new Span("Ajout d'une nouvelle recette");

        TextField titleRecipe = new TextField("Nom de la recette");
        VerticalLayout layoutIngredients = new VerticalLayout();
        Span ingredientTitle = new Span ("Ingrédients");
        Button buttonAddIngredient = new Button("+");
        buttonAddIngredient.addClickListener(event -> {TextField ingredient = new TextField();
        layoutIngredients.add(ingredient);});

        HorizontalLayout horizontalLayout = new HorizontalLayout(titleRecipe);

        Button buttonValid = new Button("Ajouter la recette");
        buttonValid.addClickListener(event -> {
           String name = titleRecipe.getValue();
           List <String> ingredients = new ArrayList<>();
           layoutIngredients.getChildren().forEach(component -> {
               if (component instanceof TextField){
                   String textIngredient = ((TextField) component).getValue();
                   if (!textIngredient.isBlank()){
                       ingredients.add(textIngredient);
                   }
               }
           });
           Recipe newRecipe = new Recipe(name, ingredients);
           recipeList.add(newRecipe);
           grid.getDataProvider().refreshAll();

           formRecipe.close();
        });

        formRecipe.add(title, horizontalLayout,ingredientTitle, buttonAddIngredient, layoutIngredients, buttonValid);
        formRecipe.open();

    }


}
