package com.example.lockcontrol.interceptor;

import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

/**
 * 登录检查
 * @author ruiruisa
 * @create 2022-05-09 13:10
 */
public class LoginInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession();
        Object loginUser = session.getAttribute("user");
        if(loginUser != null){
            return true;
        }else{
            //拦截住，为登录，跳转到登录页
            session.setAttribute("msg","请先登录");
            response.sendRedirect("/");
            return false;
        }
    }
}
