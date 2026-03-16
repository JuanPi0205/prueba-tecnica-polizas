package com.segurosbolivar.polizasapi.controller;

import com.segurosbolivar.polizasapi.entity.Riesgo;
import com.segurosbolivar.polizasapi.service.RiesgoService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/riesgos")
@RequiredArgsConstructor
public class RiesgoController {

    private final RiesgoService riesgoService;


    @PostMapping("/{id}/cancelar")
    public ResponseEntity<Riesgo> cancelarRiesgo(@PathVariable Long id) {
        return ResponseEntity.ok(riesgoService.cancelarRiesgo(id));
    }
}
