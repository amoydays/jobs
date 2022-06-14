package com.amoydays.jobs.interceptor;

import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
public class AdminLoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uri = request.getRequestURI();
        if (uri.startsWith("/admin") && null == request.getSession().getAttribute("adminUserName")) {
            response.sendRedirect(request.getContextPath() + "/goLogin");
            return false;
        } else if (uri.startsWith("/super") && null == request.getSession().getAttribute("superUserName")) {
            response.sendRedirect(request.getContextPath() + "/goSuperLogin");
            return false;
        } else {
            return true;
        }
    }
}
