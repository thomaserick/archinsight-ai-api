package com.fiap.pj.core.analise.domain;


import com.fiap.pj.infra.sk.jpa.BaseCrudRepository;

import java.util.UUID;

public interface AnaliseDiagramaDomainRepository extends BaseCrudRepository<AnaliseDiagrama, UUID> {

    @Override
    AnaliseDiagrama findByIdOrThrowNotFound(UUID id);
}
