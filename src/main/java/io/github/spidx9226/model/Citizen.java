package io.github.spidx9226.model;

import java.util.UUID;

public record Citizen(
        UUID id,
        String fullName,
        String passportNumber,
        String phone
) {}