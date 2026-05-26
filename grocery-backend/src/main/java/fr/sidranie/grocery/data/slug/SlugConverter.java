package fr.sidranie.grocery.data.slug;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class SlugConverter implements AttributeConverter<Slug, String> {
    @Override
    public String convertToDatabaseColumn(Slug attribute) {
        return attribute.getValue();
    }

    @Override
    public Slug convertToEntityAttribute(String dbData) {
        return dbData == null ? null : new Slug(dbData);
    }
}
