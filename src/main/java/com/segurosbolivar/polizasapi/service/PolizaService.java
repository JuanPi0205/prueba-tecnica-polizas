package com.segurosbolivar.polizasapi.service;

import com.segurosbolivar.polizasapi.entity.*;
import com.segurosbolivar.polizasapi.repository.PolizaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PolizaService {
    private final PolizaRepository polizaRepository;
    private final CoreIntegrationService coreIntegrationService;

    private static final BigDecimal IPC = new BigDecimal("0.10");

    public List<Poliza> listarPolizas(TipoPoliza tipo, EstadoPoliza estado) {
        if (tipo != null && estado != null) {
            return polizaRepository.findByTipoAndEstado(tipo, estado);
        }
        return polizaRepository.findAll();
    }

    public List<Riesgo> obtenerRiesgosDePoliza(Long id) {
        Poliza poliza = buscarPolizaOThrow(id);
        return poliza.getRiesgos();
    }

    @Transactional
    public Poliza renovarPoliza(Long id) {
        Poliza poliza = buscarPolizaOThrow(id);

        if (poliza.getEstado() == EstadoPoliza.CANCELADA) {
            throw new IllegalStateException("No se puede renovar una póliza cancelada.");
        }


        BigDecimal incrementoCanon = poliza.getValorCanonMensual().multiply(IPC);
        poliza.setValorCanonMensual(poliza.getValorCanonMensual().add(incrementoCanon));


        BigDecimal incrementoPrima = poliza.getValorPrima().multiply(IPC);
        poliza.setValorPrima(poliza.getValorPrima().add(incrementoPrima));


        poliza.setEstado(EstadoPoliza.RENOVADA);

        Poliza polizaGuardada = polizaRepository.save(poliza);
        coreIntegrationService.notificarCore("RENOVACION", polizaGuardada.getId());

        return polizaGuardada;
    }

    @Transactional
    public Poliza cancelarPoliza(Long id) {
        Poliza poliza = buscarPolizaOThrow(id);
        poliza.setEstado(EstadoPoliza.CANCELADA);


        poliza.getRiesgos().forEach(riesgo -> riesgo.setEstado(EstadoRiesgo.CANCELADO));

        Poliza polizaGuardada = polizaRepository.save(poliza);
        coreIntegrationService.notificarCore("CANCELACION", polizaGuardada.getId());

        return polizaGuardada;
    }

    @Transactional
    public Riesgo agregarRiesgo(Long idPoliza, Riesgo nuevoRiesgo) {
        Poliza poliza = buscarPolizaOThrow(idPoliza);


        if (poliza.getTipo() != TipoPoliza.COLECTIVA) {
            throw new IllegalStateException("Solo se pueden agregar riesgos a pólizas COLECTIVAS.");
        }

        nuevoRiesgo.setEstado(EstadoRiesgo.ACTIVO);
        poliza.addRiesgo(nuevoRiesgo);
        polizaRepository.save(poliza);

        coreIntegrationService.notificarCore("AGREGAR_RIESGO", poliza.getId());

        return nuevoRiesgo;
    }

    private Poliza buscarPolizaOThrow(Long id) {
        return polizaRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Póliza no encontrada con ID: " + id));
    }
}
