package com.penglecode.xmodule.hydra.oauth2.examples.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.penglecode.xmodule.common.security.consts.SecurityApplicationConstants;
import com.penglecode.xmodule.hydra.oauth2.examples.service.DefaultUserDetailsService;
import com.penglecode.xmodule.hydra.oauth2.examples.web.security.HydraLogoutSuccessHandler;

@Configuration
@EnableWebSecurity
public class HydraLoginSecurityConfiguration extends WebSecurityConfigurerAdapter {

	@Bean
    public PasswordEncoder passwordEncoder(){
        return SecurityApplicationConstants.DEFAULT_PASSWORD_ENCODER;
    }
	
	@Override
    protected void configure(HttpSecurity http) throws Exception {
		http.antMatcher("/**")
			.authorizeRequests()
				.antMatchers("/login").permitAll()
         		.anyRequest().authenticated()
         	.and()
         		.formLogin().loginPage("/login").failureForwardUrl("/login/failure").successForwardUrl("/login/success")
         	.and()
         		.logout().logoutUrl("/logout").logoutSuccessHandler(new HydraLogoutSuccessHandler("/logout/success")).invalidateHttpSession(true)
         	.and()
         		.csrf().disable();
    }
	
	@Bean
    public DaoAuthenticationProvider authenticationProvider() {
        final DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

	@Bean
	@Override
	protected UserDetailsService userDetailsService() {
		return new DefaultUserDetailsService();
	}
	
}
