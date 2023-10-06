package com.ldx.blog.service.impl.category;

import cn.dev33.satoken.stp.StpUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ldx.blog.mapper.category.CategoryMapper;
import com.ldx.blog.pojo.category.Category;
import com.ldx.blog.pojo.category.Category;
import com.ldx.blog.pojo.category.PageParams;
import com.ldx.blog.service.category.CategoryService;
import com.ldx.blog.utils.TextUtil;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author ldx
 * @description 针对表【Category】的数据库操作Service实现
 * @createDate 2023-05-22 20:47:41
 */
@Service
public class CategoryServiceImpl extends ServiceImpl<CategoryMapper, Category>
        implements CategoryService {
    @Resource
    private CategoryMapper categoryMapper;

    public IPage<Category> getTagsPage(Integer current, Integer size) {
        IPage<Category> iPage = new Page<>(current, size);
        page(iPage);
        return iPage;
    }

    @Override
    public IPage<Category> getCategories(PageParams params) {
        Long loginId = Long.parseLong(StpUtil.getLoginId().toString());
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Category::getUserId, loginId)
                .orderByDesc(params.isDesc(), Category::getCreateTime)
                .eq(params.getStatus() != 0, Category::getStatus, params.getStatus())
                .like(TextUtil.isNotEmpty(params.getKeyword()), Category::getName, params.getKeyword());
        IPage<Category> iPage = new Page<>(params.getPageNo(), params.getPageSize());
        page(iPage, lqw);
        return iPage;
    }

    @Override
    public Boolean insertOrUpdateCategory(Category category) {
        // 一个人只能有一个同名的专栏
        long loginId = Long.parseLong(StpUtil.getLoginId().toString());
        category.setUserId(loginId);
        LambdaQueryWrapper<Category> lqw = new LambdaQueryWrapper<>();
        lqw.eq(Category::getName,category.getName()).eq(Category::getUserId,category.getUserId());
        Long l = categoryMapper.selectCount(lqw);
        if ( l > 0) {
            return false;
        }
        if (Objects.isNull(category.getId())) {
            return categoryMapper.insert(category) > 0;
        } else {
            return categoryMapper.updateById(category) > 0;
        }
    }
}




