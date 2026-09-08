package com.example.base.ui;

import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.checkbox.Checkbox;
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

public class ScreenView extends VerticalLayout {
    private final List<Recipe> recipeList = new ArrayList<>();
    private final VerticalLayout verticalLayoutRecipe = new VerticalLayout();
    private HorizontalLayout horizontalLayoutCheckAndTitle;


    public ScreenView() {

        H1 title = new H1("Mon application de recettes de la mort qui tue");
        Button addButton = new Button("+");
        Span textAddRecipe = new Span("Add new recipe");
        addButton.addClickListener(event -> AddRecipe());

        Span vide = new Span();
        Button basketButton = new Button("Basket");

        basketButton.addClickListener(e -> OpenBasket());

        HorizontalLayout topBar = new HorizontalLayout(addButton, textAddRecipe, vide, basketButton);
        topBar.setWidthFull();
        topBar.expand(vide);
        topBar.setAlignItems(Alignment.CENTER);
        add(title, topBar, verticalLayoutRecipe);

    }

    private void AddRecipe() {

        Dialog formRecipe = new Dialog();
        Span title = new Span("Ajout d'une nouvelle recette");

        TextField titleRecipe = new TextField("Nom de la recette");
        VerticalLayout layoutIngredients = new VerticalLayout();
        Span ingredientTitle = new Span("Ingrédients");

        Button buttonAddIngredient = new Button("+");
        buttonAddIngredient.addClickListener(event -> {
            TextField ingredient = new TextField();
            layoutIngredients.add(ingredient);
        });

        HorizontalLayout horizontalLayout = new HorizontalLayout(titleRecipe);

        Button buttonValid = new Button("Ajouter la recette");
        buttonValid.addClickListener(event -> {
            String name = titleRecipe.getValue();
            List<String> ingredients = new ArrayList<>();

            layoutIngredients.getChildren().forEach(component -> {
                if (component instanceof TextField) {
                    String textIngredient = ((TextField) component).getValue();
                    if (!textIngredient.isBlank()) {
                        ingredients.add(textIngredient);
                    }
                }
            });

            if (!name.isBlank()) {
                Recipe newRecipe = new Recipe(name, ingredients);
                recipeList.add(newRecipe);
                Span recipeText = new Span(newRecipe.getName());
                recipeText.getStyle().set("cursor", "pointer"); // Met le curseur "main" au survol

                recipeText.addClickListener(e -> {
                    ShowRecipeDetails(newRecipe);
                });
                // --- RAFRAÎCHISSEMENT DE L'INTERFACE ---

                Checkbox checkboxRecipe = new Checkbox();
                ComponentUtil.setData(checkboxRecipe, Recipe.class, newRecipe);
                horizontalLayoutCheckAndTitle = new HorizontalLayout();
                horizontalLayoutCheckAndTitle.add(checkboxRecipe, recipeText);

                verticalLayoutRecipe.add(horizontalLayoutCheckAndTitle);
            }

            formRecipe.close();
        });

        formRecipe.add(title, horizontalLayout, ingredientTitle, buttonAddIngredient, layoutIngredients, buttonValid);
        formRecipe.open();

    }

    private void ShowRecipeDetails(Recipe newRecipe) {
        Dialog detailsDialog = new Dialog();

        Span titleRecipe = new Span(newRecipe.getName());

        VerticalLayout ingredientsLayout = new VerticalLayout();
        ingredientsLayout.add(new Span("Ingrédients :"));

        for (String ingredient : newRecipe.getIngredients()) {
            ingredientsLayout.add(new Span("• " + ingredient));
        }

        Button closeButton = new Button("Fermer", e -> detailsDialog.close());

        detailsDialog.add(titleRecipe, ingredientsLayout, closeButton);
        detailsDialog.open();
    }


    private List<String> GetIngredients() {

        List<String> ingredients = new ArrayList<>();
        verticalLayoutRecipe.getChildren().forEach(component ->
        {
            if (component instanceof HorizontalLayout layout) {
                Checkbox checkboxRecipe = (Checkbox) layout.getComponentAt(0);

                if (checkboxRecipe.getValue()) {
                    Recipe recipe = ComponentUtil.getData(checkboxRecipe, Recipe.class);

                    if (recipe != null) {
                        ingredients.addAll(recipe.getIngredients());
                    }
                }
            }
        });
        return ingredients;
    }

    private void OpenBasket(){
        List<String> ingredients = GetIngredients();

        Dialog basketDialog = new Dialog();
        VerticalLayout verticalLayoutIngredients = new VerticalLayout();
        for(String ingredient : ingredients)
        {
            verticalLayoutIngredients.add(new Span (ingredient));
        }

        Button close = new Button("Close", e -> basketDialog.close());
        verticalLayoutIngredients.add(close);
        basketDialog.add(verticalLayoutIngredients);
        basketDialog.open();


    }
}

