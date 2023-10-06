package com.ldx.blog.controller;

import com.ldx.blog.result.Result;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author Uaena
 * @date 2023/6/1 20:21
 */
@RequestMapping("/version")
@RestController
public class VersionController {
    @Value("${project.version}")
    private String version;
    @GetMapping("/")
    public Result<String> version(){
        return Result.success(version);
    }
}
