package fr.sidranie.grocery;

public class StringUtils {

    public static String toSlug(String text) {
        return text.toLowerCase()
                .replaceAll("[^a-z0-9\\-]+", "-")
                .replaceAll("^-+|-+$", "");
    }
}
