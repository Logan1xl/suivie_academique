package com.suivie_academique.suivie_academique.controllers;

import com.suivie_academique.suivie_academique.dto.AffectationDto;
import com.suivie_academique.suivie_academique.services.AffectationService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/affectations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class AffectationController {

    private final AffectationService affectationService;

    /**
     * POST /api/affectations - Créer une affectation
     * Body: { "codePersonnel": "ENS...", "codeCours": "..." }
     */
    @PostMapping
    public ResponseEntity<AffectationDto> creerAffectation(
            @RequestBody Map<String, String> request) {
        try {
            String codePersonnel = request.get("codePersonnel");
            String codeCours = request.get("codeCours");

            AffectationDto created = affectationService.createAffectation(codePersonnel, codeCours);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * GET /api/affectations - Obtenir toutes les affectations
     */
    @GetMapping
    public ResponseEntity<List<AffectationDto>> getAllAffectations() {
        return ResponseEntity.ok(affectationService.findAll());
    }

    /**
     * GET /api/affectations/personnel/{codePersonnel} - Affectations d'un personnel
     */
    @GetMapping("/personnel/{codePersonnel}")
    public ResponseEntity<List<AffectationDto>> getByPersonnel(@PathVariable String codePersonnel) {
        return ResponseEntity.ok(affectationService.getAffectationsByPersonnel(codePersonnel));
    }

    /**
     * GET /api/affectations/cours/{codeCours} - Affectations d'un cours
     */
    @GetMapping("/cours/{codeCours}")
    public ResponseEntity<List<AffectationDto>> getByCours(@PathVariable String codeCours) {
        return ResponseEntity.ok(affectationService.getAffectationsByCours(codeCours));
    }

    /**
     * DELETE /api/affectations - Supprimer une affectation
     * Query params: ?codePersonnel=...&codeCours=...
     */
    @DeleteMapping
    public ResponseEntity<Void> deleteAffectation(
            @RequestParam String codePersonnel,
            @RequestParam String codeCours) {
        try {
            affectationService.deleteAffectation(codePersonnel, codeCours);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * GET /api/affectations/count/personnel/{codePersonnel} - Compter affectations
     */
    @GetMapping("/count/personnel/{codePersonnel}")
    public ResponseEntity<Long> countByPersonnel(@PathVariable String codePersonnel) {
        return ResponseEntity.ok(affectationService.countByPersonnel(codePersonnel));
    }

    /**
     * GET /api/affectations/count/cours/{codeCours} - Compter affectations
     */
    @GetMapping("/count/cours/{codeCours}")
    public ResponseEntity<Long> countByCours(@PathVariable String codeCours) {
        return ResponseEntity.ok(affectationService.countByCours(codeCours));
    }
}