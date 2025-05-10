package com.gina.simulator.enums;

import com.gina.simulator.enums.dto.CategoryDTO;
import com.gina.simulator.enums.dto.SubcategoryDTO;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("api/enums")
public class EnumsApi {

    @GetMapping("category")
    public List<CategoryDTO> getCategories() {
        return Arrays.stream(Category.values())
                .map(cat -> new CategoryDTO(cat.name(), cat.getDisplayName()))
                .collect(Collectors.toList());
    }

    @GetMapping("subcategory")
    public List<SubcategoryDTO> getSubcategories(@RequestParam("category") String categoryName) {
        try {
            Category category = Category.valueOf(categoryName);
            return Arrays.stream(Subcategory.values())
                    .filter(sub -> sub.getCategory().equals(category))
                    .map(sub -> new SubcategoryDTO(sub.name(), sub.getDisplayName()))
                    .collect(Collectors.toList());
        } catch (IllegalArgumentException e) {
            return List.of();
        }
    }
}
