package com.example.demo.security;

import com.google.common.collect.Sets;
import javassist.Loader;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Set;
import java.util.stream.Collectors;

import static com.example.demo.security.ApplicationUserPermission.*;

public enum ApplicationUserRole {
    STUDENT(Sets.newHashSet()),
    ADMIN(Sets.newHashSet(
            COURSE_READ,
            COURSE_WRITE,
            STUDENT_READ,
            STUDENT_WRITE)),
    ADMINTRAINEE(Sets.newHashSet(
            COURSE_READ,
            STUDENT_READ
            ));

    private final Set<ApplicationUserPermission> permissions;

    ApplicationUserRole(Set<ApplicationUserPermission> permissions) {
        this.permissions = permissions;
    }

    public Set<ApplicationUserPermission> getPermissions() {
        return permissions;
    }

    public Set<SimpleGrantedAuthority> getGrantedAuthorities() {
        Set<SimpleGrantedAuthority> perms = getPermissions()
                .stream()
                .map(perm -> new SimpleGrantedAuthority(perm.getPermission()))
                .collect(Collectors.toSet());

        perms.add(new SimpleGrantedAuthority("ROLE_" + this.name()));

        return perms;
    }
}
