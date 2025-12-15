
package com.suivie_academique.suivie_academique.security;

import com.suivie_academique.suivie_academique.entities.Personnel;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.Collections;

public class PersonnelUserDetails implements UserDetails {

    private Personnel personnel;

    public PersonnelUserDetails(Personnel personnel) {
        this.personnel = personnel;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // rolePersonnel est un enum; on le transforme en ROLE_...
        String role = "ROLE_" + personnel.getRolePersonnel().name();
        return Collections.singletonList(new SimpleGrantedAuthority(role));
    }

    @Override
    public String getPassword() {
        return personnel.getPadPersonnel();
    }

    @Override
    public String getUsername() {
        return personnel.getLoginPersonnel();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }
    @Override
    public boolean isEnabled() {
        return true;
    }

    public Personnel getPersonnel() {
        return personnel;
    }
}
