package com.segurosbolivar.polizasapi.service;

import com.segurosbolivar.polizasapi.entity.EstadoRiesgo;
import com.segurosbolivar.polizasapi.entity.Riesgo;
import com.segurosbolivar.polizasapi.repository.RiesgoRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class RiesgoService {

    private final RiesgoRepository riesgoRepository;
    private final CoreIntegrationService coreIntegrationService;

    @Transactional
    public Riesgo cancelarRiesgo(Long id) {
        Riesgo riesgo = riesgoRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Riesgo no encontrado con ID: " + id));

        riesgo.setEstado(EstadoRiesgo.CANCELADO);
        Riesgo riesgoGuardado = riesgoRepository.save(riesgo);

        coreIntegrationService.notificarCore("CANCELACION_RIESGO", riesgo.getPoliza().getId());

        return riesgoGuardado;
    }
}
