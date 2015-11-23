package fr.orsan.utils;

import org.springframework.security.core.GrantedAuthority;

/**
 * Created by ykpotufe on 23/11/2015.
 */
public enum OrsanRole implements GrantedAuthority {
    ROLE_ADMIN ,
    ROLE_CUSTOMER,
    ROLE_ANONYMOUS;

    @Override
    public String getAuthority() {
        return name();
    }
}
