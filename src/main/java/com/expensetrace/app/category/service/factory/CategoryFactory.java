package com.expensetrace.app.category.service.factory;

import com.expensetrace.app.category.enums.CategoryType;
import com.expensetrace.app.category.model.Category;
import com.expensetrace.app.model.User;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CategoryFactory {

    public List<Category> createDefaultCategories(User user) {
        return List.of(
                createCategory(user, "Food", CategoryType.EXPENSE, "apple", "red", false,true),
                createCategory(user, "Travel", CategoryType.EXPENSE, "airplane", "blue", false,true),
                createCategory(user, "Medical", CategoryType.EXPENSE, "pill", "teal", false,true),
                createCategory(user, "Shopping", CategoryType.EXPENSE, "shopping-bag", "violet", false,true),
                createCategory(user, "Family", CategoryType.EXPENSE, "home", "orange", false,true),
                createCategory(user, "Entertainment", CategoryType.EXPENSE, "game", "pink", false,true),
                createCategory(user, "Utilities", CategoryType.EXPENSE, "electricity", "amber", false,true),
                createCategory(user, "Miscellaneous", CategoryType.EXPENSE, "note", "gray", false,true),
                createCategory(user, "Others", CategoryType.EXPENSE, "others", "gray", true,false),
                createCategory(user, "Salary", CategoryType.INCOME, "wallet", "green", false,true),
                createCategory(user, "Investment", CategoryType.INCOME, "bank", "yellow", false,true),
                createCategory(user, "Gift", CategoryType.INCOME, "gift", "peach", false,true),
                createCategory(user, "Bonus", CategoryType.INCOME, "money-bill", "emerald", false,true),
                createCategory(user, "Side Hustle", CategoryType.INCOME, "briefcase", "cyan", false,true),
                createCategory(user, "Loan", CategoryType.INCOME, "loan", "lime", false,true),
                createCategory(user, "Other", CategoryType.INCOME, "misc", "zinc", false,true),
                createCategory(user, "Others", CategoryType.INCOME, "others", "gray", true,false)
        );
    }

    private Category createCategory(User user, String name, CategoryType type,
                                    String icon, String color,
                                    boolean isDefault, boolean isDeletable) {
        Category category = new Category();
        category.setUser(user);
        category.setName(name);
        category.setType(type);
        category.setIcon(icon);
        category.setColor(color);
        category.setDefault(isDefault);
        category.setDeletable(isDeletable);
        return category;
    }
}
