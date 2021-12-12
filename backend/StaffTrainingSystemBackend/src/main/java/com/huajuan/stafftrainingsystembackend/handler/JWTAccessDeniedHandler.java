package com.huajuan.stafftrainingsystembackend.handler;

import com.huajuan.stafftrainingsystembackend.http.MyHttpResponseObj;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * @author shuang.kou
 * AccessDeineHandler 用来解决认证过的用户访问需要权限才能访问的资源时的异常
 */
@Component
public class JWTAccessDeniedHandler implements AccessDeniedHandler {
    /**
     * 当用户尝试访问需要权限才能的REST资源而权限不足的时候，
     * 将调用此方法发送403响应以及错误信息
     */
    @Override
    public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException accessDeniedException) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType("application/json");
        response.getOutputStream().write(
                new ObjectMapper().writeValueAsBytes(
                        new MyHttpResponseObj(HttpServletResponse.SC_FORBIDDEN, "对不起，你的权限不足以访问此页面"))
        );
    }
}