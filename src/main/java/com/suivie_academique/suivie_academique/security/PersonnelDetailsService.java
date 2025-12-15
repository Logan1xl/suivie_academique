package com.suivie_academique.suivie_academique.security;

import com.suivie_academique.suivie_academique.entities.Personnel;
import com.suivie_academique.suivie_academique.repositories.PersonnelRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.*;
import org.springframework.stereotype.Service;

@Service
public class PersonnelDetailsService implements UserDetailsService {

    private final PersonnelRepository personnelRepository;

    @Autowired
    public PersonnelDetailsService(PersonnelRepository personnelRepository) {
        this.personnelRepository = personnelRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        Personnel p = personnelRepository.findByLoginPersonnel(username)
                .orElseThrow(() -> new UsernameNotFoundException("Login introuvable"));
        return new PersonnelUserDetails(p);
    }
}
