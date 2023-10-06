package com.ldx.blog.config;

import cn.dev33.satoken.annotation.SaIgnore;
import cn.dev33.satoken.exception.BackResultException;
import cn.dev33.satoken.exception.StopMatchException;
import cn.dev33.satoken.fun.SaParamFunction;
import cn.dev33.satoken.interceptor.SaInterceptor;
import cn.dev33.satoken.strategy.SaStrategy;
import com.ldx.blog.result.Result;
import com.ldx.blog.result.ResultCodeEnum;
import org.springframework.web.method.HandlerMethod;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.lang.reflect.Method;

/**
 * @author Uaena
 * @date 2023/5/21 16:05
 */
public class SaTokenInterceptor extends SaInterceptor {
    public SaTokenInterceptor(SaParamFunction<Object> auth) {
        super();
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        try {
            if (this.isAnnotation && handler instanceof HandlerMethod) {
                Method method = ((HandlerMethod) handler).getMethod();
                if ((Boolean) SaStrategy.me.isAnnotationPresent.apply(method, SaIgnore.class)) {
                    return true;
                }
                SaStrategy.me.checkMethodAnnotation.accept(method);
            }
            if (request.getMethod().equals("OPTIONS")) {
                return true;
            } else {
                this.auth.run(handler);
            }
        } catch (StopMatchException var5) {
        } catch (BackResultException var6) {
            if (response.getContentType() == null) {
                response.setContentType("text/plain; charset=utf-8");
            }
            response.getWriter().print(Result.fail(ResultCodeEnum.NOT_LOGIN));
            return false;
        }
        return true;
    }
}
