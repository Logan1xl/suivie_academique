package com.suivie_academique.suivie_academique.utils;

import com.suivie_academique.suivie_academique.repositories.PersonnelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.util.concurrent.ThreadLocalRandom;

@Component
@RequiredArgsConstructor
public class CodeGenerator {

    private final PersonnelRepository personnelRepository;

    public String generate(String roleString) {

        String prefix = switch (roleString) {
            case "ENSEIGNANT" -> "ENS";
            case "RESPONSABLE_ACADEMIQUE" -> "RA";
            case "RESPONSABLE_DISCIPLINE" -> "RD";
            case "RESPONSABLE_PERSONNEL" -> "RP";
            default -> "XX";
        };

        int year = LocalDate.now().getYear();

        while (true) {
            long randomNumber = ThreadLocalRandom.current().nextLong(1000, 100000);
            String code = prefix + year + randomNumber;

            if (!personnelRepository.existsById(code)) {
                return code;
            }
        }
    }
}
