package fr.orsan.config

import fr.orsan.utils.config.OrsanUserDetailsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.social.security.SpringSocialConfigurer

/**
 * Created by yawo on 22/11/15.
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(securedEnabled = true)
public class SecurityConfig {

    @Autowired OrsanUserDetailsService userDetailsService

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .formLogin()
            .loginPage("/signin")
            .loginProcessingUrl("/signin/authenticate")
            .failureUrl("/signin?param.error=bad_credentials")
            .and()
            .logout()
            .logoutUrl("/signout")
            .deleteCookies("JSESSIONID")
            .and()
            .authorizeRequests()
            .antMatchers("/resources/**", "/favicon.ico").permitAll()
            .antMatchers("/**").authenticated()
            .and()
            .rememberMe()
            .and()
            .apply(new SpringSocialConfigurer());
    }
}