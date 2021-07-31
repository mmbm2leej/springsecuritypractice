package com.example.demo.auth;

import com.google.common.collect.Lists;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

import static com.example.demo.security.ApplicationUserRole.*;

@Repository("fakerepo")
public class FakeApplicationUserDAOService implements ApplicationUserDAO {

    //need the password encoder
    private final PasswordEncoder passEncoder;

    @Autowired
    public FakeApplicationUserDAOService(PasswordEncoder passEncoder) {
        this.passEncoder = passEncoder;
    }

    @Override
    public Optional<ApplicationUser> selectApplicationUserByUsername(String username) {
        return getApplicationUsers()
                .stream()
                .filter(applicationUser -> username.equals(applicationUser.getUsername()))
                .findFirst();
    }

    //normally not like this, usually from a separate database
    private List<ApplicationUser> getApplicationUsers() {
        List<ApplicationUser> appUsers = Lists.newArrayList(

                new ApplicationUser(
                        STUDENT.getGrantedAuthorities(),
                        passEncoder.encode("notthebees"),
                        "mirajones",
                        true,
                        true,
                        true,
                        true
                ),

                new ApplicationUser(
                        ADMIN.getGrantedAuthorities(),
                        passEncoder.encode("password123"),
                        "linda",
                        true,
                        true,
                        true,
                        true
                ),

                new ApplicationUser(
                        ADMINTRAINEE.getGrantedAuthorities(),
                        passEncoder.encode("thisisapass"),
                        "tomtrainee",
                        true,
                        true,
                        true,
                        true
                )
        );

        return appUsers;
    }

}
