package fr.orsan.config

import fr.orsan.utils.config.OrsanUserDetailsService
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity

/**
 * Created by yawo on 22/11/15.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired OrsanUserDetailsService userDetailsService

    @Autowired
    public void configureGlobal(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService)
    }

}