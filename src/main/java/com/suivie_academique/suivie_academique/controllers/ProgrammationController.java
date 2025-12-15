package com.suivie_academique.suivie_academique.controllers;

import com.suivie_academique.suivie_academique.dto.ProgrammationDto;
import com.suivie_academique.suivie_academique.services.ProgrammationService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Date;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/programmations")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class ProgrammationController {

    private final ProgrammationService programmationService;

    /**
     * POST /api/programmations - Créer une programmation
     * Body: JSON avec tous les champs requis
     */
    @PostMapping
    public ResponseEntity<?> creerProgrammation(
            @RequestBody ProgrammationDto programmationDto) {
        try {
            ProgrammationDto created = programmationService.creerProgrammation(programmationDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            // Retourner le message d'erreur pour debugging
            return ResponseEntity.badRequest().body(
                    Map.of("error", e.getMessage())
            );
        }
    }

    /**
     * GET /api/programmations - Obtenir toutes les programmations
     */
    @GetMapping
    public ResponseEntity<List<ProgrammationDto>> getAllProgrammations() {
        return ResponseEntity.ok(programmationService.getAllProgrammations());
    }

    /**
     * GET /api/programmations/{id} - Obtenir une programmation par ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<?> getProgrammationById(@PathVariable Integer id) {
        try {
            return ResponseEntity.ok(programmationService.getProgrammationById(id));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    /**
     * PUT /api/programmations/{id} - Modifier une programmation
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateProgrammation(
            @PathVariable Integer id,
            @RequestBody ProgrammationDto dto) {
        try {
            return ResponseEntity.ok(programmationService.updateProgrammation(id, dto));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", e.getMessage())
            );
        }
    }

    /**
     * PUT /api/programmations/{id}/valider - Valider une programmation
     */
    @PutMapping("/{id}/valider")
    public ResponseEntity<?> validerProgrammation(
            @PathVariable Integer id,
            @RequestParam String codePersonnelVal) {
        try {
            return ResponseEntity.ok(
                    programmationService.validerProgrammation(id, codePersonnelVal)
            );
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", e.getMessage())
            );
        }
    }

    /**
     * DELETE /api/programmations/{id} - Supprimer une programmation
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteProgrammation(@PathVariable Integer id) {
        try {
            programmationService.deleteProgrammation(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                    Map.of("error", e.getMessage())
            );
        }
    }

    /**
     * GET /api/programmations/statut/{statut} - Par statut
     */
    @GetMapping("/statut/{statut}")
    public ResponseEntity<List<ProgrammationDto>> getByStatut(@PathVariable String statut) {
        return ResponseEntity.ok(programmationService.getByStatut(statut));
    }

    /**
     * GET /api/programmations/salle/{codeSalle} - Par salle
     */
    @GetMapping("/salle/{codeSalle}")
    public ResponseEntity<List<ProgrammationDto>> getBySalle(@PathVariable String codeSalle) {
        return ResponseEntity.ok(programmationService.getBySalle(codeSalle));
    }

    /**
     * GET /api/programmations/cours/{codeCours} - Par cours
     */
    @GetMapping("/cours/{codeCours}")
    public ResponseEntity<List<ProgrammationDto>> getByCours(@PathVariable String codeCours) {
        return ResponseEntity.ok(programmationService.getByCours(codeCours));
    }

    /**
     * GET /api/programmations/personnel/{codePersonnel} - Par programmateur
     */
    @GetMapping("/personnel/{codePersonnel}")
    public ResponseEntity<List<ProgrammationDto>> getByPersonnel(@PathVariable String codePersonnel) {
        return ResponseEntity.ok(programmationService.getByPersonnel(codePersonnel));
    }

    /**
     * GET /api/programmations/en-attente - En attente de validation
     */
    @GetMapping("/en-attente")
    public ResponseEntity<List<ProgrammationDto>> getEnAttenteValidation() {
        return ResponseEntity.ok(programmationService.getEnAttenteValidation());
    }

    /**
     * GET /api/programmations/futures - Programmations futures
     */
    @GetMapping("/futures")
    public ResponseEntity<List<ProgrammationDto>> getProgrammationsFutures() {
        return ResponseEntity.ok(programmationService.getProgrammationsFutures());
    }

    /**
     * GET /api/programmations/periode - Par période
     * Format: yyyy-MM-dd HH:mm:ss
     */
    @GetMapping("/periode")
    public ResponseEntity<List<ProgrammationDto>> getByPeriode(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date debut,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") Date fin) {
        return ResponseEntity.ok(programmationService.getProgrammationsEntre(debut, fin));
    }
}