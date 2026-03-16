package com.segurosbolivar.polizasapi.repository;

import com.segurosbolivar.polizasapi.entity.Riesgo;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface RiesgoRepository extends JpaRepository<Riesgo, Long> {
}
