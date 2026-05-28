package fr.sidranie.grocery.data.price;

import java.util.Objects;
import java.util.regex.Pattern;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonValue;

public class Price {
    private Float value;

    public Price() {
    }

    @JsonCreator
    public Price(Float value) {
        this.value = value;
    }

    @JsonValue
    public Float getValue() {
        return value;
    }

    public void setValue(Float value) {
        this.value = value;
    }

    public boolean isValid() {
        if (this.value < 0.) {
            return false;
        }

        String priceAsString = String.valueOf(this.value);
        return Pattern.matches("^\\d+\\.+\\d{0,2}$", priceAsString);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        Price that = (Price) o;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(value);
    }

    @Override
    public String toString() {
        return "Price{'" + value + "'}";
    }

}
