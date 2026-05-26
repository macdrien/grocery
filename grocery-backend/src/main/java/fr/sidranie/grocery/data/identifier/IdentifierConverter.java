package fr.sidranie.grocery.data.identifier;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter
public class IdentifierConverter implements AttributeConverter<Identifier, String> {
    @Override
    public String convertToDatabaseColumn(Identifier attribute) {
        return attribute.getValue();
    }

    @Override
    public Identifier convertToEntityAttribute(String dbData) {
        return dbData == null ? null : new Identifier(dbData);
    }
}
