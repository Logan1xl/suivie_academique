package com.suivie_academique.suivie_academique.controllers;

import com.suivie_academique.suivie_academique.dto.PersonnelDto;
import com.suivie_academique.suivie_academique.services.PersonnelService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/personnel")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class PersonnelController {

    private final PersonnelService personnelService;

    /**
     * POST /api/personnel - Créer un personnel
     * Body doit contenir: DTO + motDePasse séparé
     */
    @PostMapping
    public ResponseEntity<PersonnelDto> creerPersonnel(
            @RequestBody Map<String, Object> request) {
        try {
            PersonnelDto dto = new PersonnelDto();
            dto.setNomPersonnel((String) request.get("nomPersonnel"));
            dto.setLoginPersonnel((String) request.get("loginPersonnel"));
            dto.setSexePersonnel((String) request.get("sexePersonnel"));
            dto.setPhonePersonnel((String) request.get("phonePersonnel"));
            dto.setRolePersonnel((String) request.get("rolePersonnel"));

            String motDePasse = (String) request.get("motDePasse");

            PersonnelDto created = personnelService.creerPersonnel(dto, motDePasse);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * GET /api/personnel - Obtenir tout le personnel
     */
    @GetMapping
    public ResponseEntity<List<PersonnelDto>> getAll() {
        return ResponseEntity.ok(personnelService.getAll());
    }

    /**
     * GET /api/personnel/enseignants - Obtenir les enseignants
     */
    @GetMapping("/enseignants")
    public ResponseEntity<List<PersonnelDto>> getEnseignants() {
        return ResponseEntity.ok(personnelService.getEnseignants());
    }

    /**
     * GET /api/personnel/role/{role} - Rechercher par rôle
     */
    @GetMapping("/role/{role}")
    public ResponseEntity<List<PersonnelDto>> getByRole(@PathVariable String role) {
        return ResponseEntity.ok(personnelService.getByRole(role));
    }

    /**
     * GET /api/personnel/search?nom=... - Rechercher par nom
     */
    @GetMapping("/search")
    public ResponseEntity<List<PersonnelDto>> searchByNom(@RequestParam String nom) {
        return ResponseEntity.ok(personnelService.rechercherParNom(nom));
    }

    /**
     * DELETE /api/personnel/{code} - Supprimer un personnel
     */
    @DeleteMapping("/{code}")
    public ResponseEntity<Void> deletePersonnel(@PathVariable String code) {
        try {
            personnelService.deletePersonnel(code);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * PUT /api/personnel/{code} - Modifier un personnel
     */
    @PutMapping("/{code}")
    public ResponseEntity<PersonnelDto> updatePersonnel(
            @PathVariable String code,
            @RequestBody PersonnelDto dto) {
        try {
            return ResponseEntity.ok(personnelService.updatePersonnel(code, dto));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}