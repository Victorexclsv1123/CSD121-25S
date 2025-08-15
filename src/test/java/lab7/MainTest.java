package lab7;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;



class MainTest {

    @Test
    void testGetQuickRecipesReturnsEmptyListIfNoData() {
        var recipes = Main.getQuickRecipes(List::of);
        assertEquals(0, recipes.size());
    }

    @Test
    void testGetQuickRecipesReturnsEmptyListIfNoQuickRecipes() {
        var recipes = Main.getQuickRecipes(() -> List.of(
                new Recipe(0, "", "", "", 4, 10, 10, 16),
                new Recipe(1, "", "", "", 4, 10, 10, 20),
                new Recipe(2, "", "", "", 4, 10, 10, 200)
        ));
        assertEquals(0, recipes.size());
    }

    @Test
    void testGetQuickRecipesReturnsAllRecipesIfAllQuick() {

        var recipes = Main.getQuickRecipes(() -> List.of(
                new Recipe(0, "", "", "", 4, 10, 10, 15),
                new Recipe(1, "", "", "", 4, 10, 10, 1),
                new Recipe(2, "", "", "", 4, 10, 10, 10)
        ));

        assertEquals(3, recipes.size());
    }

    @Test
    void testGetQuickRecipesWorksOnTypicalData() {

        var recipes = Main.getQuickRecipes(() -> List.of(
                        new Recipe(0, "", "", "", 4, 10, 10, 10),
                        new Recipe(1, "", "", "", 4, 10, 10, 15),
                        new Recipe(2, "", "", "", 4, 10, 10, 16),
                        new Recipe(3, "", "", "", 4, 10, 10, 20),
                        new Recipe(4, "", "", "", 4, 10, 10, 2343)
        ));

        assertEquals(2, recipes.size());

        // Verify that the two recipes we expected are in fact in the list
        assertEquals(0, recipes.get(0).id());
        assertEquals(1, recipes.get(1).id());
    }

    // TODO: test the searchRecipes method

    // --- searchRecipes tests ---

    @Test
    void testSearchRecipesFindsByNameCaseInsensitive() {
        var results = Main.searchRecipes(() -> List.of(
                new Recipe(1, "Pasta Primavera", "fresh vegetables", "", 2, 5, 10, 15),
                new Recipe(2, "Burger", "grilled beef patty", "", 2, 5, 10, 20)
        ), "pasta");

        assertEquals(1, results.size());
        assertEquals(1, results.get(0).id());
    }

    @Test
    void testSearchRecipesFindsByDescriptionCaseInsensitive() {
        var results = Main.searchRecipes(() -> List.of(
                new Recipe(10, "Rice Bowl", "Spicy BEEF strips", "", 2, 5, 10, 15),
                new Recipe(11, "Salad", "greens", "", 1, 5, 5, 10)
        ), "beef");

        assertEquals(1, results.size());
        assertEquals(10, results.get(0).id());
    }

    @Test
    void testSearchRecipesReturnsEmptyIfNoMatch() {
        var results = Main.searchRecipes(() -> List.of(
                new Recipe(1, "Pasta", "tomato", "", 2, 5, 10, 15)
        ), "sushi");

        assertEquals(0, results.size());
    }




}