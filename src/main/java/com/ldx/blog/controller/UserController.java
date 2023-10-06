package com.ldx.blog.controller;

import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.temp.SaTempUtil;
import cn.dev33.satoken.util.SaResult;
import com.ldx.blog.pojo.user.User;
import com.ldx.blog.result.Result;
import com.ldx.blog.result.ResultCodeEnum;
import com.ldx.blog.service.impl.user.UserServiceImpl;
import com.ldx.blog.utils.IPUtil;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Map;

/**
 * @author Uaena
 * @date 2023/5/21 12:01
 */
@RestController
@RequestMapping("user")
@CrossOrigin
public class UserController {
    @Resource
    private UserServiceImpl userService;

    @RequestMapping("/refreshToken")
    public Result<String> refreshToken(String refreshToken) {
        // 1、验证
        Object userId = SaTempUtil.parseToken(refreshToken);
        if (userId == null) {
            return Result.fail(ResultCodeEnum.REFRESH_TOKEN_FAIL);
        }
        // 2、为其生成新的短 token
        String accessToken = StpUtil.createLoginSession(userId);
        // 3、返回
        return Result.success(accessToken);
    }

    @PostMapping("login")
    public Result<Map<String,String>> doLoginApi(HttpServletRequest request, @RequestBody Map<String, String> loginForm) {
        String ip = IPUtil.ip(request);
        return userService.doLogin(loginForm, ip);
    }

    @PostMapping("login/verification")
    public Result<SaTokenInfo> loginByVerificationCode(HttpServletRequest request, @RequestBody Map<String, String> loginForm) {
        String ip = IPUtil.ip(request);
        return userService.loginByVerificationCode(loginForm, ip);
    }

    @PostMapping("registry")
    public Result<ResultCodeEnum> doRegistryApi(HttpServletRequest request, @RequestBody @Valid User user) {
        String ip = IPUtil.ip(request);
        return userService.doRegistry(user, ip);
    }

    @PostMapping("logout")
    public Result<ResultCodeEnum> logoutApi() {
        try {
            StpUtil.logout();
            return Result.success(ResultCodeEnum.LOGOUT_SUCCESS);
        } catch (RuntimeException e) {
            return Result.fail(ResultCodeEnum.FAIL);
        }
    }

    @GetMapping("info")
    public Result<User> userSafeInfoApi() {
        return userService.userSafeInfo();
    }

    @PostMapping("info")
    public Result<Boolean> updateUserInfoApi(@Valid @RequestBody User user) {
        return userService.updateUserInfo(user);
    }
}
