package com.metric.auth.server.config;

import com.metric.auth.common.model.SecurityConstants;
import com.metric.auth.core.util.SpringApplicationHelper;
import com.metric.auth.server.filter.filters.CrossOriginFilter;
import com.metric.auth.server.filter.filters.JwtAuthorizationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpMethod;
import org.springframework.http.HttpStatus;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.authentication.HttpStatusEntryPoint;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.zalando.problem.spring.web.advice.security.SecurityProblemSupport;

/**
 * @description:
 * @author: YangShu
 * @create: 2024-07-01
 **/
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true)
@Import(SecurityProblemSupport.class)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {

//    @Autowired
//    private CorsFilter corsFilter;

    @Autowired
    private SecurityProblemSupport securityProblemSupport;

    /**
     * 使用 Spring Security 推荐的加密方式进行登录密码的加密
     */
    @Bean
    public BCryptPasswordEncoder bCryptPasswordEncoder(){
        return new BCryptPasswordEncoder();
    }

    /**
     * 此方法配置的资源路径不会进入 Spring Security 机制进行验证
     */
    @Override
    public void configure(WebSecurity web) {
        web.ignoring()
                // todo 后面在认证的时候需要注释了
                .antMatchers("/**/**")
                .antMatchers(HttpMethod.OPTIONS, "/**")
                .antMatchers("/app/**/*.{js,html}")
                .antMatchers("/v3/api-docs/**")
                .antMatchers("/i18n/**")
                .antMatchers("/test/**")
                .antMatchers("/auth/error")
                .antMatchers("/auth/exceptionHandler")
                .antMatchers("/auth/v3/api-docs")
                .antMatchers("/auth/auth-doc-json")
                .antMatchers("/auth/doc.html")
                .antMatchers("/doc.html")
                .antMatchers("/auth/swagger/**")
                .antMatchers("/auth/api/v3/resource/block-item/run")
                .antMatchers("/auth/*.xlsx")
                .antMatchers("/**/*.html")
                .antMatchers("/auth/favicon.ico")
                .antMatchers("/auth/favicon")
                .antMatchers("/auth/index.html*")
                .antMatchers("/auth/manifest.json*")
                .antMatchers("/auth/robots.txt*")
                .antMatchers("/auth/webjars/**")
                .antMatchers("/auth/swagger-resources/**")
                .antMatchers("/v1/api-docs-ext")
                .antMatchers("/auth/precache-manifest.*")
                .antMatchers("/auth/init/**")
                .antMatchers("/h2")
                .antMatchers("/favicon.ico")
                .antMatchers("/auth/favicon.ico")
                .antMatchers("/content/**")
                .antMatchers("/webjars/springfox-swagger-ui/**")
                .antMatchers("/swagger-resources/**")
                .antMatchers("/swagger-ui.html");
    }

    /**
     * 定义安全策略，设置 HTTP 访问规则
     */
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .addFilterBefore(SpringApplicationHelper.getBean(CrossOriginFilter.class), UsernamePasswordAuthenticationFilter.class)
                .exceptionHandling()
                // 当用户无权访问资源时发送 401 响应
                .authenticationEntryPoint(new HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))
                // 当用户访问资源因权限不足时发送 403 响应
                .accessDeniedHandler(securityProblemSupport)
                .and()
                // 禁用 CSRF
                .csrf().disable()
                .headers().frameOptions().disable()
                .and()
                .logout().logoutUrl("/auth/logout").and()
                .authorizeRequests()
                // 指定路径下的资源需要进行验证后才能访问
                .antMatchers("/").permitAll()
                // 配置登录地址
                .antMatchers(HttpMethod.POST, SecurityConstants.AUTH_LOGIN_URL).permitAll()
                .antMatchers(HttpMethod.POST,"/auth/api/v1/user/register").permitAll()
                // 其他请求需验证
                .anyRequest().authenticated()
                .and()
                // 不需要 session（不创建会话）
                .sessionManagement()
                .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                .and()
                .apply(securityConfigurationAdapter());
//        super.configure(http);
    }

    private JwtConfig securityConfigurationAdapter() throws Exception{
        return new JwtConfig(new JwtAuthorizationFilter(authenticationManager()));
    }
}

