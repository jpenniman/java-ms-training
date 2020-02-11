package com.northwind.catalogservice.api;

import com.northwind.catalogservice.domain.Category;

public class CategoryMapper {

    public static CategoryModel toModel(Category entity) {
        CategoryModel model = new CategoryModel();
        model.setId(entity.getId());
        model.setCategoryName(entity.getCategoryName());
        model.setDesription(entity.getDescription());
        model.setVersion(entity.getVersion());
        return model;
    }

    public static Category newCategory(CategoryModel model) {
        Category entity = new Category(model.getCategoryName());
        entity.setDescription(model.getDesription());
        return entity;
    }

    public static Category merge(CategoryModel model, Category entity) {
        entity.setCategoryName((model.getCategoryName()));
        entity.setDescription(model.getDesription());
        return entity;
    }
}
