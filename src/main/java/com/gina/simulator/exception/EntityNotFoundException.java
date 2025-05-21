package com.gina.simulator.exception;

import lombok.extern.slf4j.Slf4j;

import java.util.UUID;

/**
 * Exception handling missing entities in a database.
 */
@Slf4j
public class EntityNotFoundException extends RuntimeException {
    public EntityNotFoundException(Class<?> clazz) {
        super(String.format("Entity of type '%s' not found in DB.", clazz.getSimpleName()));
    }

    public EntityNotFoundException(Class<?> clazz, UUID id) {
        super(String.format("Entity of type '%s' with id '%s' not found in DB.", clazz.getSimpleName(), id));
    }

    public EntityNotFoundException(Class<?> clazz, String id) {
        super(String.format("Entity of type '%s' with id '%s' not found in DB.", clazz.getSimpleName(), id));
    }
}
