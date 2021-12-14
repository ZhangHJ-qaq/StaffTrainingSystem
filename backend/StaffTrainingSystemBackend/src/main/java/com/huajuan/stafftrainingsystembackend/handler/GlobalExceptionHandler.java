package com.huajuan.stafftrainingsystembackend.handler;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.huajuan.stafftrainingsystembackend.http.MyHttpResponseObj;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@ControllerAdvice
public class GlobalExceptionHandler {

    @Autowired
    JWTAccessDeniedHandler accessDeniedHandler;

    @ResponseBody
    @ExceptionHandler(value = Exception.class)
    public void exceptionHandler(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Exception e) {
        try {
            e.printStackTrace();
            if (e instanceof AccessDeniedException) {//在spring security鉴权失败后，会抛出AccessDeniedException，由于这个是全局异常处理器，因此会在这里被捕获
                //将其交给正确的AccessDeniedHandler处理
                accessDeniedHandler.handle(httpServletRequest, httpServletResponse, (AccessDeniedException) e);
                return;
            }

            httpServletResponse.setStatus(HttpServletResponse.SC_BAD_REQUEST);
            httpServletResponse.setContentType("application/json");
            httpServletResponse.getOutputStream().write(
                    new ObjectMapper().writeValueAsBytes(
                            new MyHttpResponseObj(HttpServletResponse.SC_BAD_REQUEST, e.getMessage())
                    )
            );

        } catch (IOException ignored) {
        }


    }
}
