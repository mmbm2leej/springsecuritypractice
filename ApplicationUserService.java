package com.example.demo.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class ApplicationUserService implements UserDetailsService {

    private final ApplicationUserDAO appUserDAO;

    @Autowired
    public ApplicationUserService(@Qualifier("fakerepo") ApplicationUserDAO appUserDAO) {
        this.appUserDAO = appUserDAO;
    }

    @Override
    public UserDetails loadUserByUsername(String s) throws UsernameNotFoundException {
        return appUserDAO
                .selectApplicationUserByUsername(s)
                .orElseThrow(
                        () -> new UsernameNotFoundException(String.format("Username %s not found", s))
                );
    }

}
