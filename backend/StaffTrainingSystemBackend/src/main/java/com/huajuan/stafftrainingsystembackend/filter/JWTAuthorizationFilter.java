package com.huajuan.stafftrainingsystembackend.filter;

import com.huajuan.stafftrainingsystembackend.contants.SecurityConstants;
import com.huajuan.stafftrainingsystembackend.utils.JwtTokenUtils;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.SignatureException;
import org.apache.log4j.Logger;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;
import org.springframework.util.StringUtils;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

/**
 * 过滤器处理所有HTTP请求，并检查是否存在带有正确令牌的Authorization标头。例如，如果令牌未过期或签名密钥正确。
 *
 * @author shuang.kou
 */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

    private static final Logger logger = Logger.getLogger(JWTAuthorizationFilter.class.getName());

    public JWTAuthorizationFilter(AuthenticationManager authenticationManager) {
        super(authenticationManager);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws IOException, ServletException {

        String authorization = request.getHeader(SecurityConstants.TOKEN_HEADER);
        // 如果请求头中没有Authorization信息则直接放行了
        if (authorization == null || !authorization.startsWith(SecurityConstants.TOKEN_PREFIX)) {
            chain.doFilter(request, response);
            return;
        }
        // 如果请求头中有token，则进行解析，并且设置授权信息
        SecurityContextHolder.getContext().setAuthentication(getAuthentication(authorization));
        super.doFilterInternal(request, response, chain);
    }

    /**
     * 这里从token中获取用户信息并新建一个token
     */
    private UsernamePasswordAuthenticationToken getAuthentication(String authorization) {
        String token = authorization.replace(SecurityConstants.TOKEN_PREFIX, "");

        try {
            String username = JwtTokenUtils.getUsernameByToken(token);
            // 通过 token 获取用户具有的角色
            List<SimpleGrantedAuthority> userAuthorities = JwtTokenUtils.getUserRolesByToken(token);
            if (!StringUtils.isEmpty(username)) {
                return new UsernamePasswordAuthenticationToken(username, null, userAuthorities);
            }
        } catch (SignatureException | ExpiredJwtException exception) {
            logger.info("Request to parse JWT with invalid signature . Detail : " + exception.getMessage());
        }
        return null;
    }
}