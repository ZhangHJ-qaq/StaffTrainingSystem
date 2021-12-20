package com.huajuan.stafftrainingsystembackend.contants;


/**
 * @author shuang.kou
 * @description Spring Security相关配置常量
 */
public final class SecurityConstants {

    /**
     * 角色的key
     **/
    public static final String ROLE_CLAIMS = "roles";
    public static final String USERNAME_CONSTANT = "employeeID";

    /**
     * rememberMe 为 false 的时候过期时间是1个小时
     */
    public static final long EXPIRATION = 60 * 60L;

    /**
     * rememberMe 为 true 的时候过期时间是7天
     */
    public static final long EXPIRATION_REMEMBER = 60 * 60 * 24 * 7L;

    // JWT token defaults
    public static final String TOKEN_HEADER = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer ";
    public static final String TOKEN_TYPE = "JWT";

    // Swagger WHITELIST
    public static final String[] SWAGGER_WHITELIST = {
            "/swagger-ui.html",
            "/swagger-ui/*",
            "/swagger-resources/**",
            "/v2/api-docs",
            "/v3/api-docs",
            "/webjars/**",
            //knife4j
            "/doc.html",
    };

    public static final String H2_CONSOLE = "/h2-console/**";

    public static final String AUTH_LOGIN_URL = "/auth/login";

    // System WHITELIST
    public static final String[] SYSTEM_WHITELIST = {
            "/auth/login",
            "/users/sign-up"
    };

    private SecurityConstants() {
    }

}