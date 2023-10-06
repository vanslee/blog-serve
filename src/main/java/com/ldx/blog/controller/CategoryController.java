package com.ldx.blog.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ldx.blog.pojo.category.Category;
import com.ldx.blog.pojo.category.PageParams;
import com.ldx.blog.result.Result;
import com.ldx.blog.service.impl.category.CategoryServiceImpl;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

/**
 * @author Uaena
 * @date 2023/6/18 18:22
 */
@RestController
@RequestMapping("/category")
public class CategoryController {
    @Resource
    private CategoryServiceImpl categoriesService;
    @PostMapping("/list")
    public Result<IPage<Category>> getCategoriesApi(@RequestBody PageParams params){
        return Result.success(categoriesService.getCategories(params));
    }
    @PutMapping("/create")
    public Result<Boolean> insertOrUpdateCategory(@RequestBody Category category) {
        return Result.success(categoriesService.insertOrUpdateCategory(category));
    }
}
