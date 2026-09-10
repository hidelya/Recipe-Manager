package com.example.base.ui;

import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.checkbox.Checkbox;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.Route;

import java.util.ArrayList;
import java.util.List;

@Route("screen")
public class ScreenView extends VerticalLayout {
    private final List<Recipe> recipeList = new ArrayList<>();
    private final VerticalLayout verticalLayoutRecipe = new VerticalLayout();
    private HorizontalLayout horizontalLayoutCheckAndTitle;

    public ScreenView() {
        VerticalLayout titleLayout = new VerticalLayout();
        titleLayout.add(new H1("Mon application de recettes de la mort qui tue"));
        titleLayout.setWidthFull();
        titleLayout.setAlignItems(Alignment.CENTER);

        Button addButton = new Button("+");
        addButton.setId("addButton");
        Span textAddRecipe = new Span("Add new recipe");
        addButton.addClickListener(event -> AddRecipe());

        Span vide = new Span();
        Button basketButton = new Button("Basket");
        basketButton.setId("basketButton");
        basketButton.addClickListener(e -> OpenBasket());

        HorizontalLayout topBar = new HorizontalLayout(addButton, textAddRecipe, vide, basketButton);
        topBar.setWidthFull();
        topBar.expand(vide);
        topBar.setAlignItems(Alignment.CENTER);

        VerticalLayout mainLayout = new VerticalLayout(titleLayout, topBar, verticalLayoutRecipe);
        mainLayout.setMaxWidth("900px");
        mainLayout.setMargin(true);
        mainLayout.getStyle().set("margin-left", "auto").set("margin-right", "auto");
        mainLayout.getStyle().setPaddingBottom("800px");
        mainLayout.getStyle().setBackgroundColor("#ccb993");

        add(mainLayout);
    }

    private void AddRecipe() {
        Dialog formRecipe = new Dialog();
        Span title = new Span("Ajout d'une nouvelle recette");

        TextField titleRecipe = new TextField("Nom de la recette");
        titleRecipe.setId("nameRecipe");

        Span ingredientTitle = new Span("Ingrédients et Quantités");
        VerticalLayout layoutIngredients = new VerticalLayout();

        Button buttonAddIngredient = new Button("+ (Ingrédient)");
        buttonAddIngredient.addClickListener(event -> {
            TextField ingredient = new TextField("Ingrédient");
            TextField quantite = new TextField("Quantité");
            HorizontalLayout layoutIngredientsQuantite = new HorizontalLayout(ingredient, quantite);
            layoutIngredients.add(layoutIngredientsQuantite);
        });

        HorizontalLayout horizontalLayout = new HorizontalLayout(titleRecipe);

        Button buttonValid = new Button("Ajouter la recette");
        buttonValid.setId("validButton");
        buttonValid.addClickListener(event -> {
            String name = titleRecipe.getValue();
            List<String> ingredients = new ArrayList<>();
            List<String> quantites = new ArrayList<>();

            // Parcours des lignes (HorizontalLayout) contenant Ingrédient + Quantité
            layoutIngredients.getChildren().forEach(component -> {
                if (component instanceof HorizontalLayout lineLayout) {
                    TextField ingredientField = (TextField) lineLayout.getComponentAt(0);
                    TextField quantiteField = (TextField) lineLayout.getComponentAt(1);

                    String textIngredient = ingredientField.getValue();
                    String textQuantite = quantiteField.getValue();

                    if (!textIngredient.isBlank()) {
                        ingredients.add(textIngredient);
                        quantites.add(textQuantite);
                    }
                }
            });

            if (!name.isBlank()) {
                Recipe newRecipe = new Recipe(name, ingredients, quantites);
                recipeList.add(newRecipe);

                Span recipeText = new Span(newRecipe.getName());
                recipeText.getStyle().set("cursor", "pointer");
                recipeText.addClickListener(e -> ShowRecipeDetails(newRecipe));

                Checkbox checkboxRecipe = new Checkbox();
                checkboxRecipe.setId("recipeCheckbox");
                ComponentUtil.setData(checkboxRecipe, Recipe.class, newRecipe);

                horizontalLayoutCheckAndTitle = new HorizontalLayout();
                horizontalLayoutCheckAndTitle.add(checkboxRecipe, recipeText);

                verticalLayoutRecipe.add(horizontalLayoutCheckAndTitle);
            }

            formRecipe.close();
        });

        formRecipe.add(new VerticalLayout(title, horizontalLayout, ingredientTitle, buttonAddIngredient, layoutIngredients, buttonValid));
        formRecipe.open();
    }

    private void ShowRecipeDetails(Recipe recipe) {
        Dialog detailsDialog = new Dialog();

        Span titleRecipe = new Span("Recette : " + recipe.getName());
        titleRecipe.getStyle().set("font-weight", "bold");

        VerticalLayout ingredientsLayout = new VerticalLayout();

        List<String> ingredients = recipe.getIngredients();
        List<String> quantites = recipe.getQuantite();

        for (int i = 0; i < ingredients.size(); i++) {
            String ing = ingredients.get(i);
            String qte = (i < quantites.size()) ? quantites.get(i) : "";

            HorizontalLayout line = new HorizontalLayout(
                    new Span("• " + ing),
                    new Span(qte.isBlank() ? "" : "(" + qte + ")")
            );
            ingredientsLayout.add(line);
        }

        Button closeButton = new Button("Fermer", event -> detailsDialog.close());
        closeButton.setId("closeButton");

        detailsDialog.add(new VerticalLayout(titleRecipe, ingredientsLayout, closeButton));
        detailsDialog.open();
    }

    private List<String> GetIngredients() {
        List<String> ingredients = new ArrayList<>();
        verticalLayoutRecipe.getChildren().forEach(component -> {
            if (component instanceof HorizontalLayout layout) {
                Checkbox checkboxRecipe = (Checkbox) layout.getComponentAt(0);

                if (checkboxRecipe.getValue()) {
                    Recipe recipe = ComponentUtil.getData(checkboxRecipe, Recipe.class);
                    if (recipe != null) {
                        for (int i = 0; i < recipe.getIngredients().size(); i++) {
                            String ing = recipe.getIngredients().get(i);
                            String qte = recipe.getQuantite().size() > i ? recipe.getQuantite().get(i) : "";
                            ingredients.add(ing + (qte.isBlank() ? "" : " - " + qte));
                        }
                    }
                }
            }
        });
        return ingredients;
    }

    private void OpenBasket() {
        List<String> ingredients = GetIngredients();

        Dialog basketDialog = new Dialog();
        VerticalLayout verticalLayoutIngredients = new VerticalLayout();
        verticalLayoutIngredients.add(new Span("Ingrédients sélectionnés :"));

        for (String ingredient : ingredients) {
            verticalLayoutIngredients.add(new Span("• " + ingredient));
        }

        Button close = new Button("Close", e -> basketDialog.close());
        close.setId("closeBasketButton");
        verticalLayoutIngredients.add(close);

        basketDialog.add(verticalLayoutIngredients);
        basketDialog.open();
    }
}