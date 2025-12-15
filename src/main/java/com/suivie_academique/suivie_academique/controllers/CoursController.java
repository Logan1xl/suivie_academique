package com.suivie_academique.suivie_academique.controllers;

import com.suivie_academique.suivie_academique.dto.CoursDto;
import com.suivie_academique.suivie_academique.services.CourService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/cours")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CoursController {

    private final CourService coursService;

    /**
     * POST /api/cours - Créer un cours
     */
    @PostMapping
    public ResponseEntity<CoursDto> creerCours(@RequestBody CoursDto coursDto) {
        try {
            CoursDto created = coursService.creerCour(coursDto);
            return ResponseEntity.status(HttpStatus.CREATED).body(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * GET /api/cours - Obtenir tous les cours
     */
    @GetMapping
    public ResponseEntity<List<CoursDto>> getAllCours() {
        return ResponseEntity.ok(coursService.getAllCours());
    }

    /**
     * DELETE /api/cours/{code} - Supprimer un cours
     */
    @DeleteMapping("/{code}")
    public ResponseEntity<Void> deleteCours(@PathVariable String code) {
        try {
            coursService.deleteCours(code);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}