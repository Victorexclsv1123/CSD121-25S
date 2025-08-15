package lab7;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvException;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class CsvDataService implements DataService {
    private final String resourceName; // e.g., "recipes.csv" in src/main/resources
    private static final boolean DEBUG = false; // set true to print header/first row

    public CsvDataService(String resourceName) {
        this.resourceName = resourceName;
    }

    @Override
    public List<Recipe> getRecipes() throws DataException {
        try (InputStream in = getClass().getClassLoader().getResourceAsStream(resourceName)) {
            if (in == null) throw new FileNotFoundException("Resource not found: " + resourceName);

            try (Reader rr = new InputStreamReader(in, StandardCharsets.UTF_8);
                 CSVReader reader = new CSVReader(rr)) {

                List<String[]> rows = reader.readAll();
                if (rows.isEmpty()) return List.of();

                // Build lowercase header -> index map
                String[] header = rows.get(0);
                Map<String,Integer> idx = new HashMap<>();
                for (int i = 0; i < header.length; i++) {
                    String h = safe(header[i]).toLowerCase();
                    if (!h.isEmpty()) idx.put(h, i);
                }

                if (DEBUG) {
                    System.out.println("CSV HEADER = " + Arrays.toString(header));
                    if (rows.size() > 1) System.out.println("ROW[1] = " + Arrays.toString(rows.get(1)));
                }

                List<Recipe> out = new ArrayList<>();
                long autoId = 1;

                for (int r = 1; r < rows.size(); r++) {
                    String[] row = rows.get(r);
                    if (row == null || row.length == 0) continue;

                    // 1) Try header-based parse
                    String name        = pick(row, idx, "name|title|recipe|recipe_name");
                    String description = pick(row, idx, "description|desc|details|about");
                    String instructions= pick(row, idx, "instructions|steps|method|directions|howto|procedure");
                    Integer servings   = tryParseInt(row, idx, "servings|serves|portion");
                    Integer prepTime   = tryParseInt(row, idx, "preptime|prep_time|prep|prepare");
                    Integer cookTime   = tryParseInt(row, idx, "cooktime|cook_time|cook");
                    Integer totalTime  = tryParseInt(row, idx, "totaltime|total_time|time");
                    Long id            = tryParseLong(row, idx, "id");

                    boolean headerFilled =
                            anyNonEmpty(name, description, instructions) ||
                                    anyNonNull(servings, prepTime, cookTime, totalTime, id);

                    // 2) Positional fallback if header-based didn’t find much
                    if (!headerFilled) {
                        // Expected positional order:
                        // name, description, instructions, servings, prep, cook[, total][, id]
                        name        = getCell(row, 0);
                        description = getCell(row, 1);
                        instructions= getCell(row, 2);
                        servings    = parseIntOrDefault(getCell(row, 3), 1);
                        prepTime    = parseIntOrDefault(getCell(row, 4), 0);
                        cookTime    = parseIntOrDefault(getCell(row, 5), 0);
                        totalTime   = parseIntOrDefault(getCell(row, 6), -1);
                        id          = parseLongOrDefault(getCell(row, 7), null);
                    }

                    // Final cleanups / defaults
                    if (isBlank(name)) {
                        // fallback to first non-empty cell as name
                        for (String cell : row) {
                            if (!isBlank(cell)) { name = unquote(cell.trim()); break; }
                        }
                    }
                    if (servings == null)  servings  = 1;
                    if (prepTime == null)  prepTime  = 0;
                    if (cookTime == null)  cookTime  = 0;
                    if (totalTime == null || totalTime < 0) totalTime = prepTime + cookTime;
                    if (id == null) id = autoId++;

                    out.add(new Recipe(
                            id,
                            nz(name),
                            nz(description),
                            nz(instructions),
                            servings,
                            prepTime,
                            cookTime,
                            totalTime
                    ));
                }

                return out;
            } catch (IOException | CsvException e) {
                throw new DataException(e);
            }
        } catch (IOException e) {
            throw new DataException(e);
        }
    }

    // --------- helpers ---------
    private static String pick(String[] row, Map<String,Integer> idx, String keys) {
        for (String key : keys.split("\\|")) {
            Integer i = idx.get(key.toLowerCase());
            if (i != null && i < row.length) {
                String v = safe(row[i]);
                if (!v.isEmpty()) return unquote(v);
            }
        }
        return null;
    }

    private static Integer tryParseInt(String[] row, Map<String,Integer> idx, String keys) {
        for (String key : keys.split("\\|")) {
            Integer i = idx.get(key.toLowerCase());
            if (i != null && i < row.length) {
                Integer val = toInt(safe(row[i]));
                if (val != null) return val;
            }
        }
        return null;
    }

    private static Long tryParseLong(String[] row, Map<String,Integer> idx, String key) {
        Integer i = idx.get(key.toLowerCase());
        if (i != null && i < row.length) return toLong(safe(row[i]));
        return null;
    }

    private static String getCell(String[] row, int i) {
        return (i >= 0 && i < row.length) ? unquote(safe(row[i])) : "";
    }

    private static Integer parseIntOrDefault(String s, int def) {
        Integer v = toInt(s);
        return v == null ? def : v;
    }

    private static Long parseLongOrDefault(String s, Long def) {
        Long v = toLong(s);
        return v == null ? def : v;
    }

    private static Integer toInt(String s) {
        if (isBlank(s)) return null;
        try { return Integer.parseInt(unquote(s.trim())); } catch (Exception e) { return null; }
    }

    private static Long toLong(String s) {
        if (isBlank(s)) return null;
        try { return Long.parseLong(unquote(s.trim())); } catch (Exception e) { return null; }
    }

    private static String unquote(String s) {
        if (s == null) return "";
        s = s.trim();
        if (s.length() >= 2) {
            char a = s.charAt(0), b = s.charAt(s.length() - 1);
            if ((a == '"' && b == '"') || (a == '\'' && b == '\'')) return s.substring(1, s.length() - 1);
        }
        return s;
    }

    private static boolean anyNonEmpty(String... vals) {
        if (vals == null) return false;
        for (String v : vals) if (!isBlank(v)) return true;
        return false;
    }

    private static boolean anyNonNull(Object... vals) {
        if (vals == null) return false;
        for (Object v : vals) if (v != null) return true;
        return false;
    }

    private static String nz(String s) { return s == null ? "" : s; }
    private static boolean isBlank(String s) { return s == null || s.trim().isEmpty(); }
    private static String safe(String s) { return s == null ? "" : s.trim(); }
}
