package com.anqi.distribute.iputil;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * 登录拦截器
 */
@Slf4j
public class MyLoginInterceptor implements HandlerInterceptor {

    private static final String LOGIN_PATH = "user/login";

    private static Map<String, AtomicInteger> visitCount;
    private static final QQWry qqWry;
    static {
        visitCount = new HashMap<>(31);
        qqWry = new QQWry();
    }

    //展示访问数量不是精确指标，如果要做到完全正确需要使用锁，防止计数存在并发问题
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        log.info("【MyLoginInterceptor】调用了:{}", request.getRequestURI());
        if (request.getRequestURI().equals(LOGIN_PATH)) {
            String ipAddress = IPUtil.getIpAddress(request);
            String province = qqWry.findIP(ipAddress).getMainInfo();
            if (visitCount.containsKey(province)) {
                visitCount.put(province,new AtomicInteger(visitCount.get(province).incrementAndGet()));
            } else {
                visitCount.put(province,new AtomicInteger());
            }
        }
        return true;
    }

    @Override
    public void postHandle(HttpServletRequest request, HttpServletResponse response,
                           Object handler, ModelAndView modelAndView) throws Exception {}

    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response,
                                Object handler, Exception ex){}
}
