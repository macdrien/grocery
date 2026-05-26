package fr.sidranie.grocery.data.slug;

import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class Slug {
    private String value;

    public Slug() {
    }

    public static Slug fromString(String value) {
        return new Slug(value.toLowerCase()
                                .replaceAll("[^a-z0-9\\-]+", "-")
                                .replaceAll("^-+|-+$", ""));
    }

    @JsonCreator
    public Slug(String value) {
        this.value = value;
    }

    @JsonValue
    public String getValue() {
        return value;
    }

    public void setValue(String value) throws IllegalArgumentException {
        if (value != null && !value.equals(fromString(value).getValue())) {
            throw new IllegalArgumentException("The given slug does not have the right format");
        }

        this.value = value;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Slug slug = (Slug) o;
        return Objects.equals(value, slug.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return "Slug{'" + value + "'}";
    }
}
