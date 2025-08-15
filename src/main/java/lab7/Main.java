package lab7;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Arrays;
import java.util.List;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    /**
     * Get all recipes that can be made in 15 minutes or less
     * @param dataService a service that provides access to recipe data
     * @return a list of quick recipes
     */
    public static List<Recipe> getQuickRecipes(DataService dataService) {
        try {
            var recipes = dataService.getRecipes();
            return recipes.stream().filter( r -> r.totalTime() <= 15 ).toList();
        } catch(Exception e ) {
            logger.error("Error while getting quick recipes: " + e.getMessage());
            logger.debug("Stack trace: " + Arrays.toString(e.getStackTrace()));
            return List.of();
        }
    }



    // TODO: implement the searchRecipes method

    public static void main(String[] args) {
        DataService ds = new CsvDataService("recipes.csv");
        // no-arg constructor

        var quick = getQuickRecipes(ds);
        System.out.println("Quick Recipes:");
        quick.forEach(r -> System.out.println(" - " + r.id() + ": " + r.name()));

        var hits = searchRecipes(ds, "chicken");
        System.out.println("\nSearch results for 'chicken':");
        hits.forEach(r -> System.out.println(" - " + r.id() + ": " + r.name()));
    }


    /**
     * Search recipes by name or description (case-insensitive).
     * Returns an empty list (not null) if the query is blank or if errors occur.
     */
    public static List<Recipe> searchRecipes(DataService dataService, String search) {
        try {
            if (search == null || search.isBlank()) return List.of();

            final String q = search.toLowerCase();
            return dataService.getRecipes().stream()
                    .filter(r -> {
                        String name = r.name() == null ? "" : r.name().toLowerCase();
                        String desc = r.description() == null ? "" : r.description().toLowerCase();
                        return name.contains(q) || desc.contains(q);
                    })
                    .toList();
        } catch (Exception e) {
            logger.error("Error while searching recipes: " + e.getMessage());
            logger.debug("Stack trace: " + Arrays.toString(e.getStackTrace()));


            return List.of();
        }
    }
}


