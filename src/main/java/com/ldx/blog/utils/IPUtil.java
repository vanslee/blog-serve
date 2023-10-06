package com.ldx.blog.utils;

import javax.servlet.http.HttpServletRequest;

/**
 * @author Uaena
 * @date 2023/6/4 17:33
 */
@SuppressWarnings("ALL")
public class IPUtil {
    public static String ip(HttpServletRequest request) {
        //目前则是网关ip
        String ip = request.getHeader("X-Real-IP");
        if (ip != null && !"".equals(ip) && !"unknown".equalsIgnoreCase(ip)) {
            return ip;
        }
        ip = request.getHeader("X-Forwarded-For");
        if (ip != null && !"".equals(ip) && !"unknown".equalsIgnoreCase(ip)) {
            int index = ip.indexOf(',');
            if (index != -1) {
                //只获取第一个值
                return ip.substring(0, index);
            } else {
                return ip;
            }
        } else {
            //取不到真实ip则返回空，不能返回内网地址。
            return "127.0.0.1";
        }
    }
}
