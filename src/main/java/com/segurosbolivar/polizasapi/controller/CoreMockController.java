package com.segurosbolivar.polizasapi.controller;

import com.segurosbolivar.polizasapi.dto.EventoCoreDto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/core-mock")
public class CoreMockController {

    private static final Logger logger = LoggerFactory.getLogger(CoreMockController.class);

    @PostMapping("/evento")
    public ResponseEntity<String> registrarEventoCore(@RequestBody EventoCoreDto eventoDto) {

        logger.info("=======================================================");
        logger.info("MOCK CORE - Intentando enviar operación al CORE legado.");
        logger.info("Tipo de Evento: {}", eventoDto.getEvento());
        logger.info("ID de Póliza afectada: {}", eventoDto.getPolizaId());
        logger.info("=======================================================");

        return ResponseEntity.ok("Evento registrado en logs exitosamente (Mock CORE)");
    }
}