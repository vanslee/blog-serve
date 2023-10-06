package com.ldx.blog.service.impl.user;

import cn.dev33.satoken.secure.BCrypt;
import cn.dev33.satoken.stp.SaTokenInfo;
import cn.dev33.satoken.stp.StpUtil;
import cn.dev33.satoken.temp.SaTempUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.update.LambdaUpdateWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.ldx.blog.components.RedisDao;
import com.ldx.blog.constants.RedisKeys;
import com.ldx.blog.mapper.user.UserMapper;
import com.ldx.blog.pojo.user.User;
import com.ldx.blog.pojo.oath.gitee.GiteeUser;
import com.ldx.blog.pojo.oath.github.GithubUser;
import com.ldx.blog.pojo.oath.qq.QQUserInfo;
import com.ldx.blog.result.Result;
import com.ldx.blog.result.ResultCodeEnum;
import com.ldx.blog.service.impl.article.ArticleServiceImpl;
import com.ldx.blog.service.user.UserService;
import com.ldx.blog.utils.StringUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * @author ldx
 * @description 针对表【user】的数据库操作Service实现
 * @createDate 2023-05-15 23:17:28
 */
@Service
@Slf4j
public class UserServiceImpl extends ServiceImpl<UserMapper, User>
        implements UserService {
    @Value("${website.config.cdn}")
    private String CDN_WEBSITE;
    @Value("${redirect.url}")
    private String REDIRECT_URL;
    @Value("${redirect.error-page}")
    private String ERROR_PAGE;
    @Resource
    private ArticleServiceImpl articleService;
    @Resource
    private RedisDao redisDao;
    @Resource
    private UserMapper userMapper;

    public Result<SaTokenInfo> loginByVerificationCode(Map<String, String> loginForm, String ip) {
        if (Objects.isNull(loginForm)) {
            log.error("用户登录信息为空");
            return Result.fail(ResultCodeEnum.LOGIN_PARAM_NULL);
        }
        String receiverEmail = loginForm.get("receiverEmail");
        String verificationCode = loginForm.get("verificationCode");
        boolean b = redisDao.hasKey(RedisKeys.SIGN_IN_EMAIL.concat(receiverEmail));
        if (!b) {
            return Result.fail(ResultCodeEnum.CODE_INVALID);
        } else {
            String codeInRedis = redisDao.getString(RedisKeys.SIGN_IN_EMAIL.concat(receiverEmail));
            if (codeInRedis.equals(verificationCode)) {
                // 判断是新用户还是老用户
                LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
                lqw.eq(User::getUsername, receiverEmail);
                User user = userMapper.selectOne(lqw);
                if (Objects.isNull(user)) {
                    user = new User().setPassword("").setIp(ip).setRecentlyTime(System.currentTimeMillis() / 1000).setUsername(receiverEmail).setAvatarImgUrl("blog-litubao/th.jpg");
                    int rows = userMapper.insert(user);
                    if (rows < 1) {
                        log.error("插入新的user行失败,user-id:", user.getId());
                        return Result.fail(ResultCodeEnum.LOGIN_ERROR);
                    }
                } else {
                    LambdaUpdateWrapper<User> luw = new LambdaUpdateWrapper<>();
                    luw.set(User::getIp, ip).set(User::getRecentlyTime, System.currentTimeMillis() / 1000).eq(User::getId, user.getId());
                    int rows = userMapper.update(user, luw);
                    if (rows < 1) {
                        return Result.fail(ResultCodeEnum.LOGIN_ERROR);
                    }
                }
                StpUtil.login(user.getId());
                SaTokenInfo tokenInfo = StpUtil.getTokenInfo();
                tokenInfo.setTag(receiverEmail);
                return Result.success(ResultCodeEnum.LOGIN_SUCCESS, tokenInfo);
            }
            return Result.fail(ResultCodeEnum.LOGIN_ERROR);
        }
    }

    public Result<Map<String,String>> doLogin(Map<String, String> loginForm, String ip) {
        if (Objects.isNull(loginForm)) {
            log.error("登录用户信息为空");
            return Result.fail(ResultCodeEnum.LOGIN_PARAM_NULL);
        }
        String username = loginForm.get("username");
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.eq(User::getUsername, username);
        User user = userMapper.selectOne(lqw);
        if (Objects.isNull(user)) {
            return Result.fail(ResultCodeEnum.LOGIN_ERROR);
        }
        String password = loginForm.get("password");
        if (BCrypt.checkpw(password, user.getPassword())) {
            StpUtil.login(user.getId());
            user.setIp(ip);
            user.setRecentlyTime(System.currentTimeMillis() / 1000);
            userMapper.updateById(user);
            StpUtil.login(user.getId());
            String refreshToken = SaTempUtil.createToken(user.getId(), 60 * 60 * 15);
            String accessToken = StpUtil.getTokenValue();
            Map<String,String> result = new HashMap<>(2);
            result.put("ACCESS_TOKEN", accessToken);
            result.put("REFRESH_TOKEN", refreshToken);
            return Result.success(ResultCodeEnum.LOGIN_SUCCESS, result);
        } else {
            return Result.fail(ResultCodeEnum.LOGIN_ERROR);
        }
    }

    public Result<User> userSafeInfo() {
        Long userId = Long.parseLong(StpUtil.getLoginId().toString());
        User user = userMapper.selectById(userId).setPassword("********").setPhone("131********");
        user.setAvatarImgUrl(CDN_WEBSITE.concat(user.getAvatarImgUrl()));
        user.setCategories(articleService.getCategoriesByUid(userId));
        return Result.success(user);
    }

    public void oauthLogin(Object userInfo, String ip, HttpServletResponse response) throws IOException {
        // Gitee登录
        if (userInfo instanceof GiteeUser) {
            GiteeUser giteeUser = (GiteeUser) userInfo;
            LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
            lqw.eq(User::getUnionId, giteeUser.getId());
            User user = userMapper.selectOne(lqw);
            if (Objects.isNull(user)) {
                long ts = System.currentTimeMillis() / 1000;
                String password = BCrypt.hashpw(String.valueOf(ts));
                // 如果用户不存在
                User newUser = new User().setIp(ip)
                        .setUnionId(giteeUser.getId())
                        .setUsername(giteeUser.getLogin())
                        .setPassword(password)
                        .setNick(giteeUser.getName())
                        .setAvatarImgUrl("default.jpg")
                        .setEmail(giteeUser.getEmail())
                        .setRecentlyTime(ts);
                userMapper.insert(newUser);
                StpUtil.login(newUser.getId());
                response.sendRedirect(REDIRECT_URL + StpUtil.getTokenInfo().getTokenValue());
            } else {
                user.setIp(ip).setRecentlyTime(System.currentTimeMillis() / 1000);
                userMapper.updateById(user);
                StpUtil.login(user.getId());
                response.sendRedirect(REDIRECT_URL + StpUtil.getTokenInfo().getTokenValue());
            }
            return;
        }
        // QQ登录
        if (userInfo instanceof QQUserInfo) {
            QQUserInfo qqUserInfo = (QQUserInfo) userInfo;
            LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
            lqw.eq(User::getUnionId, qqUserInfo.getOpenid());
            User user = userMapper.selectOne(lqw);
            if (Objects.isNull(user)) {
                long ts = System.currentTimeMillis() / 1000;
                String password = BCrypt.hashpw(String.valueOf(ts));
                // 如果用户不存在
                User newUser = new User().setIp(ip)
                        .setUnionId(qqUserInfo.getOpenid())
                        .setUsername(qqUserInfo.getNickname())
                        .setPassword(password)
                        .setAvatarImgUrl("default.jpg")
                        .setRecentlyTime(ts);
                userMapper.insert(newUser);
                StpUtil.login(newUser.getId());
                response.sendRedirect(REDIRECT_URL + StpUtil.getTokenInfo().getTokenValue());
            } else {
                user.setIp(ip).setRecentlyTime(System.currentTimeMillis() / 1000);
                userMapper.updateById(user);
                StpUtil.login(user.getId());
                response.sendRedirect(REDIRECT_URL + StpUtil.getTokenInfo().getTokenValue());
            }
            return;
        }
        // GitHub登录
        if (userInfo instanceof GithubUser) {
            GithubUser githubUserInfo = (GithubUser) userInfo;
            LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
            lqw.eq(User::getUnionId, githubUserInfo.getId());
            User user = userMapper.selectOne(lqw);
            if (Objects.isNull(user)) {
                long ts = System.currentTimeMillis() / 1000;
                String password = BCrypt.hashpw(String.valueOf(ts));
                // 如果用户不存在
                User newUser = new User().setIp(ip)
                        .setUnionId(String.valueOf(githubUserInfo.getId()))
                        .setUsername(githubUserInfo.getName())
                        .setPassword(password)
                        .setAvatarImgUrl("default.jpg")
                        .setEmail(githubUserInfo.getEmail())
                        .setPersonalBrief(githubUserInfo.getBio())
                        .setRecentlyTime(ts);
                userMapper.insert(newUser);
                StpUtil.login(newUser.getId());
                response.sendRedirect(REDIRECT_URL + StpUtil.getTokenInfo().getTokenValue());
            } else {
                user.setIp(ip).setRecentlyTime(System.currentTimeMillis() / 1000);
                userMapper.updateById(user);
                StpUtil.login(user.getId());
                response.sendRedirect(REDIRECT_URL + StpUtil.getTokenInfo().getTokenValue());
            }
            return;
        }
        log.error("第三方登录失败");
        response.sendRedirect(ERROR_PAGE);
    }

    public Result<ResultCodeEnum> doRegistry(User params, String ip) {
        LambdaQueryWrapper<User> lqw = new LambdaQueryWrapper<>();
        lqw.eq(User::getUsername, params.getUsername());
        User user = userMapper.selectOne(lqw);
        if (!Objects.isNull(user)) {
            return Result.fail(ResultCodeEnum.USER_HAS_EXIST);
        }
        String hashpw = BCrypt.hashpw(params.getPassword());
        User newUser = new User()
                .setIp(ip)
                .setPassword(hashpw)
                .setUsername(params.getUsername())
                .setAvatarImgUrl("blog-litubao/th.jpg");
        int effectRows = userMapper.insert(newUser);
        if (effectRows > 0) {
            return Result.success(ResultCodeEnum.REGISTRY_SUCCESS);
        } else {
            return Result.fail(ResultCodeEnum.REGISTRY_ERROR);
        }
    }

    public Result<Boolean> updateUserInfo(User user) {
        LambdaQueryWrapper<User> lqw1 = new LambdaQueryWrapper<>();
        lqw1.select(User::getId);
        lqw1.eq(User::getUsername, user.getUsername());
        User userInDb = userMapper.selectOne(lqw1);
        if (!userInDb.getId().equals(user.getId())) {
            return Result.fail(ResultCodeEnum.USERNAME_HAS_EXIST);
        }
        LambdaUpdateWrapper<User> lqw = new LambdaUpdateWrapper<>();
        if (!StringUtil.isEmpty(user.getNewPassword())) {
            lqw.set(User::getPassword, BCrypt.hashpw(user.getNewPassword()));
        }
        lqw.set(User::getUsername, user.getUsername());
        lqw.set(User::getPhone, user.getPhone());
        lqw.set(User::getEmail, user.getEmail());
        lqw.set(User::getPersonalBrief, user.getPersonalBrief());
        lqw.set(User::isGender, user.isGender());
        lqw.eq(User::getId, user.getId());
        boolean update = this.update(lqw);
        if (update) {
            return Result.success(update);
        } else {
            return Result.fail(ResultCodeEnum.UPDATE_USER_ERROR);
        }
    }


}




