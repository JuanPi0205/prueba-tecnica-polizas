package com.segurosbolivar.polizasapi.service;

import com.segurosbolivar.polizasapi.dto.EventoCoreDto;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;


@Service
public class CoreIntegrationService {

    private final RestTemplate restTemplate;

    private final String CORE_MOCK_URL = "http://localhost:8080/core-mock/evento";

    public CoreIntegrationService() {
        this.restTemplate = new RestTemplate();
    }

    public void notificarCore(String tipoEvento, Long polizaId) {
        EventoCoreDto evento = new EventoCoreDto();
        evento.setEvento(tipoEvento);
        evento.setPolizaId(polizaId);

        try {

            restTemplate.postForEntity(CORE_MOCK_URL, evento, String.class);
        } catch (Exception e) {

            System.err.println("Error al comunicar con el CORE: " + e.getMessage());
        }
    }
}
