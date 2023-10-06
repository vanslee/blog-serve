package com.ldx.blog.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.ldx.blog.pojo.article.Tags;
import com.ldx.blog.result.Result;
import com.ldx.blog.service.impl.article.TagsServiceImpl;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import javax.annotation.Resource;

/**
 * @author Uaena
 * @date 2023/6/18 18:24
 */
@RestController
@RequestMapping("/tag")
public class TagController {
    @Resource
    private TagsServiceImpl tagsService;
    @GetMapping("/list")
    public Result<IPage<Tags>> getCategoriesApi(@RequestParam("current") Integer current, @RequestParam("size") Integer size){
        return Result.success(tagsService.getTagsPage(current,size));
    }
}
