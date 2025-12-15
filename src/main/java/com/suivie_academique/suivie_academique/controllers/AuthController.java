package com.suivie_academique.suivie_academique.controllers;

import com.suivie_academique.suivie_academique.dto.PersonnelDto;
import com.suivie_academique.suivie_academique.entities.Personnel;
import com.suivie_academique.suivie_academique.repositories.PersonnelRepository;
import com.suivie_academique.suivie_academique.security.JwtUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final PersonnelRepository personnelRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtils jwtUtils;

    public AuthController(AuthenticationManager authenticationManager,
                          PersonnelRepository personnelRepository,
                          BCryptPasswordEncoder passwordEncoder,
                          JwtUtils jwtUtils) {
        this.authenticationManager = authenticationManager;
        this.personnelRepository = personnelRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtUtils = jwtUtils;
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(@RequestBody Map<String, String> body) {
        String login = body.get("loginPersonnel");
        String nom = body.get("nomPersonnel");
        String phone = body.get("phonePersonnel");
        String sexe = body.get("sexePersonnel");
        String role = body.get("rolePersonnel");
        String rawPassword = body.get("motDePasse");

        if (personnelRepository.existsByLoginPersonnel(login)) {
            return ResponseEntity.badRequest().body("Login already used");
        }

        // create entity
        Personnel p = new Personnel();
        // generate codePersonnel — tu peux utiliser ton CodeGenerator; ici on laisse vide ou génère simple
        p.setCodePersonnel("P-" + System.currentTimeMillis());
        p.setLoginPersonnel(login);
        p.setNomPersonnel(nom);
        p.setPhonePersonnel(phone);
        p.setSexePersonnel(sexe);
        p.setRolePersonnel(com.suivie_academique.suivie_academique.utils.RolePersonnel.valueOf(role));
        p.setPadPersonnel(passwordEncoder.encode(rawPassword)); // encode

        Personnel saved = personnelRepository.save(p);

        return ResponseEntity.ok("Utilisateur créé");
    }
@PostMapping("/login")
public ResponseEntity<?> login(@RequestBody Map<String, String> body) {
    String login = body.get("loginPersonnel");
    String password = body.get("motDePasse");

    try {
        // Authentification via Spring Security
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(login, password)
        );

        // Récupérer l'utilisateur connecté
        Optional<Personnel> personnelOpt = personnelRepository.findByLoginPersonnel(login);
        if (personnelOpt.isEmpty()) {
            return ResponseEntity.status(404).body("Utilisateur introuvable");
        }

        Personnel personnel = personnelOpt.get();

        // Générer le token JWT
        String jwt = jwtUtils.generateToken(login);

        // Retourner token + informations utilisateur
        return ResponseEntity.ok(
                Map.of(
                        "token", jwt,
                        "user", Map.of(
                                "codePersonnel", personnel.getCodePersonnel(),
                                "nomPersonnel", personnel.getNomPersonnel(),
                                "loginPersonnel", personnel.getLoginPersonnel(),
                                "phonePersonnel", personnel.getPhonePersonnel(),
                                "sexePersonnel", personnel.getSexePersonnel(),
                                "rolePersonnel", personnel.getRolePersonnel().toString()
                        )
                )
        );

    } catch (BadCredentialsException ex) {
        return ResponseEntity.status(401).body("Login ou mot de passe incorrect");
    }
}

}
