package fr.orsan.utils.config

import fr.orsan.repositories.PersonRepository
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Component

/**
 * Created by ykpotufe on 23/11/2015.
 */
@Component("userDetailsService")
class OrsanUserDetailsService implements UserDetailsService{
    @Autowired PersonRepository personRepository

    @Override
    UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return personRepository.findByUsername()
    }
}
