package com.segurosbolivar.polizasapi.controller;

import com.segurosbolivar.polizasapi.entity.EstadoPoliza;
import com.segurosbolivar.polizasapi.entity.Poliza;
import com.segurosbolivar.polizasapi.entity.Riesgo;
import com.segurosbolivar.polizasapi.entity.TipoPoliza;
import com.segurosbolivar.polizasapi.service.PolizaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/polizas")
@RequiredArgsConstructor
public class PolizaController {
    private final PolizaService polizaService;


    @GetMapping
    public ResponseEntity<List<Poliza>> listarPolizas(
            @RequestParam(required = false) TipoPoliza tipo,
            @RequestParam(required = false) EstadoPoliza estado) {
        return ResponseEntity.ok(polizaService.listarPolizas(tipo, estado));
    }


    @GetMapping("/{id}/riesgos")
    public ResponseEntity<List<Riesgo>> obtenerRiesgos(@PathVariable Long id) {
        return ResponseEntity.ok(polizaService.obtenerRiesgosDePoliza(id));
    }


    @PostMapping("/{id}/renovar")
    public ResponseEntity<Poliza> renovarPoliza(@PathVariable Long id) {
        return ResponseEntity.ok(polizaService.renovarPoliza(id));
    }


    @PostMapping("/{id}/cancelar")
    public ResponseEntity<Poliza> cancelarPoliza(@PathVariable Long id) {
        return ResponseEntity.ok(polizaService.cancelarPoliza(id));
    }


    @PostMapping("/{id}/riesgos")
    public ResponseEntity<Riesgo> agregarRiesgo(@PathVariable Long id, @RequestBody Riesgo riesgo) {
        return ResponseEntity.ok(polizaService.agregarRiesgo(id, riesgo));
    }
}
