package com.penglecode.xmodule.security.oauth2.examples.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.penglecode.xmodule.common.security.consts.SecurityApplicationConstants;

@Primary
@Configuration
@EnableWebSecurity
public class OAuth2LoginSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Bean
    public PasswordEncoder passwordEncoder(){
        return SecurityApplicationConstants.DEFAULT_PASSWORD_ENCODER;
    }
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
		http.antMatcher("/**")
			.authorizeRequests()
				.antMatchers("/api/keycloak/init").permitAll()
				.antMatchers("/api/hydra/init").permitAll()
				.antMatchers("/login").permitAll()
         		.anyRequest().authenticated()
         	.and()
         		/**
         		 * 本地用户登录配置
         		 */
         		.formLogin().loginPage("/login").failureForwardUrl("/login/failure").defaultSuccessUrl("/login/success")
         	.and()
         		/**
         		 * OAuth2异构系统用户登录配置
         		 * spring-security-oauth2 authorization_code模式登录流程：
         		 * 
         		 * 1、#OAuth2AuthorizationRequestRedirectFilter 拦截授权请求：/oauth2/authorization/{registrationId},
         		 *    如果匹配则创建#OAuth2AuthorizationRequest 对象(见#DefaultOAuth2AuthorizationRequestResolver)
         		 * 	  紧接着sendRedirect(#OAuth2AuthorizationRequest)。此即为authorization_code模式第一步(获取code)
         		 * 2、#OAuth2LoginAuthenticationFilter 拦截OAuth2Provider发来的携带了code的callback请求：/login/oauth2/code/{registrationId}
         		 * 	  如果匹配则从请求参数中提取code参数,然后执行认证(见#OAuth2LoginAuthenticationFilter.attemptAuthentication)：
         		 * 	  2.1 标准OAuth2授权的code换token：#OAuth2LoginAuthenticationProvider (如果scope中不包含'openid')
         		 *    2.2 标准OIDC授权的code换token：#OidcAuthorizationCodeAuthenticationProvider (如果scope中包含'openid')
         		 *    其中的code换token正是通过OAuth2Provider.tokenUri标识的请求来完成的
         		 *    通过code拿到的JWT令牌需要通过OAuth2Provider.jwkSetUri获取到的jwks来进行校验
         		 */
         		.oauth2Login().loginPage("/login").failureUrl("/login/failure").defaultSuccessUrl("/login/success")
         	.and()
         		.csrf().disable();
    }
	
	@Override
	protected void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.inMemoryAuthentication().withUser("admin").password(passwordEncoder().encode("123456")).roles("admin");
        auth.inMemoryAuthentication().withUser("user").password(passwordEncoder().encode("123456")).roles("user");
	}
	
}
