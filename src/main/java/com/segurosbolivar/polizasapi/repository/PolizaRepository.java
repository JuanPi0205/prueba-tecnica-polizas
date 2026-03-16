package com.segurosbolivar.polizasapi.repository;

import com.segurosbolivar.polizasapi.entity.Poliza;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.segurosbolivar.polizasapi.entity.TipoPoliza;
import com.segurosbolivar.polizasapi.entity.EstadoPoliza;

import java.util.List;

@Repository
public interface PolizaRepository extends JpaRepository<Poliza, Long> {

    List<Poliza> findByTipoAndEstado(TipoPoliza tipo, EstadoPoliza estado);
}
