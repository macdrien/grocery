package fr.sidranie.grocery.data.price;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class PriceConverter implements AttributeConverter<Price, Float> {
    @Override
    public Float convertToDatabaseColumn(Price attribute) {
        return attribute.getValue();
    }

    @Override
    public Price convertToEntityAttribute(Float dbData) {
        return dbData == null ? null : new Price(dbData);
    }
}
