package com.expensetrace.app.service.category;

import com.expensetrace.app.enums.CategoryType;
import com.expensetrace.app.model.Category;
import com.expensetrace.app.model.User;
import com.expensetrace.app.dto.request.CategoryRequestDto;
import com.expensetrace.app.exception.AlreadyExistsException;
import com.expensetrace.app.exception.ResourceNotFoundException;
import com.expensetrace.app.repository.CategoryRepository;
import com.expensetrace.app.dto.response.CategoryResponseDto;
import com.expensetrace.app.util.SecurityUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CategoryService implements ICategoryService {
    private final CategoryRepository categoryRepository;
    private final SecurityUtil securityUtil;
    private final ModelMapper modelMapper;
    @Override
    public CategoryResponseDto getCategoryById(UUID id) {
        UUID userId = securityUtil.getAuthenticatedUserId();
        Category category = categoryRepository.findByIdAndUserId(id, userId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found!"));
        return modelMapper.map(category, CategoryResponseDto.class);
    }


    @Override
    public List<CategoryResponseDto> getAllCategories() {
        UUID userId = securityUtil.getAuthenticatedUserId();
        return categoryRepository.findByUserId(userId).stream()
                .map(category -> modelMapper.map(category, CategoryResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponseDto addCategory(CategoryRequestDto categoryRequestDto) {
        UUID userId = securityUtil.getAuthenticatedUserId();
        if (categoryRepository.existsByNameAndUserId(categoryRequestDto.getName(),userId)) {
            throw new AlreadyExistsException(categoryRequestDto.getName() + " already exists");
        }

        Category category = modelMapper.map(categoryRequestDto, Category.class);
        User user=new User();
        user.setId(userId);
        category.setUser(user);

        Category savedCategory = categoryRepository.save(category);
        return modelMapper.map(savedCategory, CategoryResponseDto.class);
    }
    @Override
    public CategoryResponseDto updateCategory(CategoryRequestDto categoryRequestDto, UUID id) {
        UUID userId = securityUtil.getAuthenticatedUserId();
        Category existing = categoryRepository.findByIdAndUserId(id,userId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found!"));

        existing.setName(categoryRequestDto.getName());
        existing.setType(categoryRequestDto.getType());
        existing.setIcon(categoryRequestDto.getIcon());
        existing.setColor(categoryRequestDto.getColor());

        Category updated = categoryRepository.save(existing);
        return modelMapper.map(updated, CategoryResponseDto.class);
    }

    @Override
    public void deleteCategoryById(UUID id) {
        UUID userId = securityUtil.getAuthenticatedUserId();
        categoryRepository.findByIdAndUserId(id,userId)
                .ifPresentOrElse(categoryRepository::delete, () -> {
                    throw new ResourceNotFoundException("Category not found!");
                });
    }

    @Override
    public List<CategoryResponseDto> getAllIncomeCategories() {
        UUID userId = securityUtil.getAuthenticatedUserId();
        return categoryRepository.findByUserIdAndType(userId, CategoryType.INCOME).stream()
                .map(category -> modelMapper.map(category, CategoryResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<CategoryResponseDto> getAllExpenseCategories() {
        UUID userId = securityUtil.getAuthenticatedUserId();
        return categoryRepository.findByUserIdAndType(userId, CategoryType.EXPENSE).stream()
                .map(category -> modelMapper.map(category, CategoryResponseDto.class))
                .collect(Collectors.toList());
    }

    @Override
    public CategoryResponseDto getDefaultExpenseCategoryByUserId() {
        UUID userId = securityUtil.getAuthenticatedUserId();
        Category defaultCategory = categoryRepository.findByUserIdAndTypeAndIsDefaultTrue(userId,CategoryType.EXPENSE)
                .orElseThrow(() -> new ResourceNotFoundException("No default category found for user"));
        return modelMapper.map(defaultCategory, CategoryResponseDto.class);
    }

    @Override
    public CategoryResponseDto updateDefaultExpenseCategory(UUID categoryId) {
        UUID userId = securityUtil.getAuthenticatedUserId();
        Category categoryToSetDefault = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found!"));

        if (!categoryToSetDefault.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Category does not belong to the user");
        }
        if (!categoryToSetDefault.getType().equals(CategoryType.EXPENSE)) {
            throw new ResourceNotFoundException("This Category does not belong to the Expense");
        }
        List<Category> userCategory = categoryRepository.findByUserIdAndType(userId,CategoryType.EXPENSE);
        userCategory.forEach(a -> {
            if (a.isDefault()) {
                a.setDefault(false);
                categoryRepository.save(a);
            }
        });

        categoryToSetDefault.setDefault(true);
        categoryRepository.save(categoryToSetDefault);

        return modelMapper.map(categoryToSetDefault, CategoryResponseDto.class);
    }

    @Override
    public CategoryResponseDto getDefaultIncomeCategoryByUserId() {
        UUID userId = securityUtil.getAuthenticatedUserId();
        Category defaultCategory = categoryRepository.findByUserIdAndTypeAndIsDefaultTrue(userId,CategoryType.INCOME)
                .orElseThrow(() -> new ResourceNotFoundException("No default category found for user"));
        return modelMapper.map(defaultCategory, CategoryResponseDto.class);
    }

    @Override
    public CategoryResponseDto updateDefaultIncomeCategory(UUID categoryId) {
        UUID userId = securityUtil.getAuthenticatedUserId();
        Category categoryToSetDefault = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found!"));

        if (!categoryToSetDefault.getUser().getId().equals(userId)) {
            throw new ResourceNotFoundException("Category does not belong to the user");
        }
        if (!categoryToSetDefault.getType().equals(CategoryType.INCOME)) {
            throw new ResourceNotFoundException("This Category does not belong to the Income");
        }

        List<Category> userCategory = categoryRepository.findByUserIdAndType(userId,CategoryType.INCOME);
        userCategory.forEach(a -> {
            if (a.isDefault()) {
                a.setDefault(false);
                categoryRepository.save(a);
            }
        });

        categoryToSetDefault.setDefault(true);
        categoryRepository.save(categoryToSetDefault);

        return modelMapper.map(categoryToSetDefault, CategoryResponseDto.class);
    }

    public void createDefaultCategoriesForUser(User user) {
        List<Category> expenseCategories = List.of(
                createCategory(user, "Food", CategoryType.EXPENSE, "apple", "red", false,true),
                createCategory(user, "Travel", CategoryType.EXPENSE, "airplane", "blue", false,true),
                createCategory(user, "Medical", CategoryType.EXPENSE, "pill", "teal", false,true),
                createCategory(user, "Shopping", CategoryType.EXPENSE, "shopping-bag", "violet", false,true),
                createCategory(user, "Family", CategoryType.EXPENSE, "home", "orange", false,true),
                createCategory(user, "Entertainment", CategoryType.EXPENSE, "game", "pink", false,true),
                createCategory(user, "Utilities", CategoryType.EXPENSE, "electricity", "amber", false,true),
                createCategory(user, "Miscellaneous", CategoryType.EXPENSE, "note", "gray", false,true),
                createCategory(user, "Others", CategoryType.EXPENSE, "others", "gray", true,false)
        );

        List<Category> incomeCategories = List.of(
                createCategory(user, "Salary", CategoryType.INCOME, "wallet", "green", false,true),
                createCategory(user, "Investment", CategoryType.INCOME, "bank", "yellow", false,true),
                createCategory(user, "Gift", CategoryType.INCOME, "gift", "peach", false,true),
                createCategory(user, "Bonus", CategoryType.INCOME, "money-bill", "emerald", false,true),
                createCategory(user, "Side Hustle", CategoryType.INCOME, "briefcase", "cyan", false,true),
                createCategory(user, "Loan", CategoryType.INCOME, "loan", "lime", false,true),
                createCategory(user, "Other", CategoryType.INCOME, "misc", "zinc", false,true),
                createCategory(user, "Others", CategoryType.INCOME, "others", "gray", true,false)
        );

        categoryRepository.saveAll(expenseCategories);
        categoryRepository.saveAll(incomeCategories);
    }


    private Category createCategory(User user, String name, CategoryType type, String icon, String color,boolean isDefault, boolean isDeletable) {
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
