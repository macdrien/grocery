package fr.sidranie.grocery.category.dto;

import fr.sidranie.grocery.data.identifier.Identifier;

public class CategoryDto {
    private Identifier name;
    private Long parentId;

    public CategoryDto(Identifier name, Long parentId) {
        this.name = name;
        this.parentId = parentId;
    }

    public Identifier getName() {
        return name;
    }

    public void setName(Identifier name) {
        this.name = name;
    }

    public Long getParentId() {
        return parentId;
    }

    public void setParentId(Long parentId) {
        this.parentId = parentId;
    }

    @Override
    public String toString() {
        return "CategoryDto{" +
                "name=" + name +
                ", parentId=" + parentId +
                '}';
    }
}
