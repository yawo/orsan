package fr.orsan.config
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.core.annotation.Order
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.social.security.SpringSocialConfigurer

/**
 * Created by yawo on 22/11/15.
 */
@Configuration
@EnableWebSecurity
@Order(3)
public class SecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired UserDetailsService userDetailsService

    @Autowired
    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(new BCryptPasswordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        println("************* CONF *************")
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
            .antMatchers("/", "/favicon.ico","/auth/**","/signin/**").permitAll()
            .antMatchers("/**").permitAll()
            .and()
            .rememberMe()
            .and()
            .apply(new SpringSocialConfigurer());
    }

}